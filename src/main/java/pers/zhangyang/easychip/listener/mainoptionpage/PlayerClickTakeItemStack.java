package pers.zhangyang.easychip.listener.mainoptionpage;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import pers.zhangyang.easychip.domain.MainOptionPage;
import pers.zhangyang.easychip.exception.NotExistItemStackException;
import pers.zhangyang.easychip.meta.WorkStationMeta;
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
public class PlayerClickTakeItemStack implements Listener {
    @GuiDiscreteButtonHandler(guiPage = MainOptionPage.class, slot = {31},closeGui = false)
    public void on(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();
        MainOptionPage mainOptionPage = (MainOptionPage) event.getInventory().getHolder();

        GuiService guiService = (GuiService) new TransactionInvocationHandler(new GuiServiceImpl()).getProxy();

        assert mainOptionPage != null;
        WorkStationMeta workStationMeta = guiService.getWorkStation(mainOptionPage.getOwner().getUniqueId().toString());

        //检查是否存入物品
        if (workStationMeta.getItemStackItemStack() == null) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistItemStackWhenTakeItemStack"));
            return;
        }

        ItemStack itemStack = ItemStackUtil.itemStackDeserialize(workStationMeta.getItemStackItemStack());

        //检查背包的空间
        if (PlayerUtil.checkSpace(player, itemStack) < 1) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notEnoughSpaceWhenTakeItemStack"));
            return;
        }
        try {
            guiService.takeItemStack(mainOptionPage.getOwner().getUniqueId().toString());
        } catch (NotExistItemStackException e) {
            e.printStackTrace();
            return;
        }

        PlayerUtil.addItem(player, itemStack, 1);

        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.takeItemStack"));
    }
}