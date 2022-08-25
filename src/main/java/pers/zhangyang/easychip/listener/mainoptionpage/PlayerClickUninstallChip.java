package pers.zhangyang.easychip.listener.mainoptionpage;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import pers.zhangyang.easychip.EasyChip;
import pers.zhangyang.easychip.domain.Chip;
import pers.zhangyang.easychip.domain.MainOptionPage;
import pers.zhangyang.easychip.exception.DuplicateChipException;
import pers.zhangyang.easychip.exception.DuplicateItemStackException;
import pers.zhangyang.easychip.exception.NotExistChipException;
import pers.zhangyang.easychip.exception.NotExistItemStackException;
import pers.zhangyang.easychip.meta.WorkStationMeta;
import pers.zhangyang.easychip.service.GuiService;
import pers.zhangyang.easychip.service.impl.GuiServiceImpl;
import pers.zhangyang.easychip.yaml.ChipYaml;
import pers.zhangyang.easychip.yaml.MessageYaml;
import pers.zhangyang.easylibrary.annotation.EventListener;
import pers.zhangyang.easylibrary.annotation.GuiDiscreteButtonHandler;
import pers.zhangyang.easylibrary.util.ItemStackUtil;
import pers.zhangyang.easylibrary.util.MessageUtil;
import pers.zhangyang.easylibrary.util.ReplaceUtil;
import pers.zhangyang.easylibrary.util.TransactionInvocationHandler;

import java.util.Collections;
import java.util.List;

@EventListener
public class PlayerClickUninstallChip implements Listener {
    @GuiDiscreteButtonHandler(guiPage = MainOptionPage.class, slot = {33},closeGui = false,refreshGui = true)
    public void on(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();
        MainOptionPage mainOptionPage = (MainOptionPage) event.getInventory().getHolder();
        assert mainOptionPage != null;
        GuiService guiService = (GuiService) new TransactionInvocationHandler(new GuiServiceImpl()).getProxy();

        WorkStationMeta workStationMeta = guiService.getWorkStation(mainOptionPage.getOwner().getUniqueId().toString());

        //检查是否存入芯片，看看槽位是不是有空间
        if (workStationMeta.getChipItemStack() != null) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notEmptyChipWhenUninstallChip"));
            return;
        }
        //检查是否存入物品
        if (workStationMeta.getItemStackItemStack() == null) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistItemStackWhenUninstallChip"));
            return;
        }

        ItemStack itemStack = ItemStackUtil.itemStackDeserialize(workStationMeta.getItemStackItemStack());
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;

        //检查是不是安装有芯片
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        Integer level = persistentDataContainer.get(new NamespacedKey(EasyChip.instance, "chip"), PersistentDataType.INTEGER);
        if (level == null) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistChipWhenUninstallChip"));
            return;
        }


        //检查配置文件是不是含有芯片
        Chip chip = null;
        for (Chip c : ChipYaml.INSTANCE.listChip()) {
            if (c.getLevel() == level) {
                chip = c;
                break;
            }
        }
        if (chip == null) {
            persistentDataContainer.remove(new NamespacedKey(EasyChip.instance, "chip"));
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistChipWhenUninstallChip"));
            return;
        }
        try {
            guiService.takeItemStack(mainOptionPage.getOwner().getUniqueId().toString());
        } catch (NotExistItemStackException e) {
            e.printStackTrace();
            return;
        }

        //卸载芯片
        persistentDataContainer.remove(new NamespacedKey(EasyChip.instance, "chip"));
        List<String> lore = itemMeta.getLore();
        List<String> additionalLore = chip.getAdditionalLore();
        if (lore != null && additionalLore != null) {
            ReplaceUtil.replace(additionalLore, Collections.singletonMap("&", String.valueOf(ChatColor.COLOR_CHAR)));
            lore.removeAll(additionalLore);
        }
        itemMeta.setLore(lore);
        for (AttributeModifier attributeModifier : chip.getAttributeModifierAttributeMap().keySet()) {
            Attribute attribute = chip.getAttributeModifierAttributeMap().get(attributeModifier);
            itemMeta.removeAttributeModifier(attribute, attributeModifier);
        }
        itemStack.setItemMeta(itemMeta);

        try {
            guiService.depositItemStack(mainOptionPage.getOwner().getUniqueId().toString(), ItemStackUtil.itemStackSerialize(itemStack));
            guiService.depositChip(mainOptionPage.getOwner().getUniqueId().toString(), ItemStackUtil.itemStackSerialize(chip.getItemStack()));
        } catch (DuplicateItemStackException | DuplicateChipException | NotExistChipException e) {
            e.printStackTrace();
            return;
        }


        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.uninstallChip"));


    }
}