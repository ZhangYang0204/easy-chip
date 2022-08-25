package pers.zhangyang.easychip.listener.mainoptionpage;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@EventListener
public class PlayerClickInstallChip implements Listener {
    @GuiDiscreteButtonHandler(guiPage = MainOptionPage.class, slot = {24},closeGui = false)
    public void on(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();
        MainOptionPage mainOptionPage = (MainOptionPage) event.getInventory().getHolder();
        assert mainOptionPage != null;
        OfflinePlayer offlinePlayerOwner = mainOptionPage.getOwner();


        GuiService guiService = (GuiService) new TransactionInvocationHandler(new GuiServiceImpl()).getProxy();


        WorkStationMeta workStationMeta = guiService.getWorkStation(offlinePlayerOwner.getUniqueId().toString());

        //检查是不是存入了芯片
        if (workStationMeta.getChipItemStack() == null) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistChipWhenInstallChip"));
            return;
        }

        //检查是否存入物品
        if (workStationMeta.getItemStackItemStack() == null) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistItemStackWhenInstallChip"));
            return;
        }

        //检查配置文件有没有该芯片
        Chip chip = null;
        for (Chip c : ChipYaml.INSTANCE.listChip()) {
            Chip cc = ChipYaml.INSTANCE.getChip("chip." + c.getLevel());
            if (cc != null && cc.getItemStack().equals(ItemStackUtil.itemStackDeserialize(workStationMeta.getChipItemStack()))) {
                chip = c;
                break;
            }
        }
        if (chip == null) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistChipWhenInstallChip"));
            return;
        }


        //检查物品是否安装过芯片
        ItemStack itemStack = ItemStackUtil.itemStackDeserialize(workStationMeta.getItemStackItemStack());
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        if (persistentDataContainer.has(new NamespacedKey(EasyChip.instance, "chip"), PersistentDataType.INTEGER)) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notEmptyChipWhenInstallChip"));
            return;
        }

        //安装芯片
        try {
            guiService.takeChip(offlinePlayerOwner.getUniqueId().toString());
            guiService.takeItemStack(offlinePlayerOwner.getUniqueId().toString());
        } catch (NotExistChipException | NotExistItemStackException e) {
            e.printStackTrace();
            return;
        }
        List<String> lore = itemMeta.getLore();
        if (lore == null) {
            lore = new ArrayList<>();
        }
        List<String> additionalLore = chip.getAdditionalLore();
        if (additionalLore != null) {
            ReplaceUtil.replace(additionalLore, Collections.singletonMap("&", String.valueOf(ChatColor.COLOR_CHAR)));
            lore.addAll(additionalLore);
        }
        itemMeta.setLore(lore);
        for (AttributeModifier attributeModifier : chip.getAttributeModifierAttributeMap().keySet()) {
            Attribute attribute = chip.getAttributeModifierAttributeMap().get(attributeModifier);

            boolean conflict = false;
            if (itemMeta.getAttributeModifiers() != null) {
                for (Attribute aa : itemMeta.getAttributeModifiers().keySet()) {
                    for (AttributeModifier aaa : itemMeta.getAttributeModifiers().get(aa)) {
                        if (aaa.getUniqueId().equals(attributeModifier.getUniqueId())) {
                            conflict = true;
                        }
                    }
                }
            }
            if (conflict) {
                continue;
            }
            itemMeta.addAttributeModifier(attribute, attributeModifier);
        }
        persistentDataContainer.set(new NamespacedKey(EasyChip.instance, "chip"), PersistentDataType.INTEGER, chip.getLevel());
        itemStack.setItemMeta(itemMeta);
        try {
            guiService.depositItemStack(offlinePlayerOwner.getUniqueId().toString(), ItemStackUtil.itemStackSerialize(itemStack));
        } catch (DuplicateItemStackException e) {
            e.printStackTrace();
            return;
        }


        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.installChip"));


    }
}
