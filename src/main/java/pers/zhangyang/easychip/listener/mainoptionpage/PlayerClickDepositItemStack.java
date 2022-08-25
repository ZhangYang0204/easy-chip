package pers.zhangyang.easychip.listener.mainoptionpage;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import pers.zhangyang.easychip.domain.MainOptionPage;
import pers.zhangyang.easychip.exception.DuplicateItemStackException;
import pers.zhangyang.easychip.service.GuiService;
import pers.zhangyang.easychip.service.impl.GuiServiceImpl;
import pers.zhangyang.easychip.yaml.MessageYaml;
import pers.zhangyang.easylibrary.annotation.EventListener;
import pers.zhangyang.easylibrary.annotation.GuiDiscreteButtonHandler;
import pers.zhangyang.easylibrary.util.ItemStackUtil;
import pers.zhangyang.easylibrary.util.MessageUtil;
import pers.zhangyang.easylibrary.util.PlayerUtil;
import pers.zhangyang.easylibrary.util.TransactionInvocationHandler;

@EventListener
public class PlayerClickDepositItemStack implements Listener {
    @GuiDiscreteButtonHandler(guiPage = MainOptionPage.class, slot = {22},closeGui = false)
    public void on(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();
        ItemStack itemStack = PlayerUtil.getItemInMainHand(player).clone();

        if (itemStack.getType().equals(Material.AIR)) {
            MessageUtil.notItemInMainHand(player);
            return;
        }
        itemStack.setAmount(1);


        MainOptionPage mainOptionPage = (MainOptionPage) event.getInventory().getHolder();
        assert mainOptionPage != null;

        GuiService guiService = (GuiService) new TransactionInvocationHandler(new GuiServiceImpl()).getProxy();
        try {
            guiService.depositItemStack(mainOptionPage.getOwner().getUniqueId().toString(), ItemStackUtil.itemStackSerialize(itemStack));
        } catch (DuplicateItemStackException e) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.duplicateItemStack"));
            return;
        }
        PlayerUtil.removeItem(player, itemStack, 1);

        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.depositItemStack"));

    }
}
