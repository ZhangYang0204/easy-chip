package pers.zhangyang.easychip.listener.mainoptionpage;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import pers.zhangyang.easychip.domain.Chip;
import pers.zhangyang.easychip.domain.MainOptionPage;
import pers.zhangyang.easychip.exception.NotExistChipException;
import pers.zhangyang.easychip.meta.WorkStationMeta;
import pers.zhangyang.easychip.service.GuiService;
import pers.zhangyang.easychip.service.impl.GuiServiceImpl;
import pers.zhangyang.easychip.yaml.ChipYaml;
import pers.zhangyang.easychip.yaml.MessageYaml;
import pers.zhangyang.easylibrary.annotation.EventListener;
import pers.zhangyang.easylibrary.annotation.GuiDiscreteButtonHandler;
import pers.zhangyang.easylibrary.util.ItemStackUtil;
import pers.zhangyang.easylibrary.util.MessageUtil;
import pers.zhangyang.easylibrary.util.PlayerUtil;
import pers.zhangyang.easylibrary.util.TransactionInvocationHandler;

@EventListener
public class PlayerClickTakeChip implements Listener {
    @GuiDiscreteButtonHandler(guiPage = MainOptionPage.class, slot = {29},closeGui = false)
    public void on(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();
        MainOptionPage mainOptionPage = (MainOptionPage) event.getInventory().getHolder();
        assert mainOptionPage != null;

        GuiService guiService = (GuiService) new TransactionInvocationHandler(new GuiServiceImpl()).getProxy();
        WorkStationMeta workStationMeta = guiService.getWorkStation(mainOptionPage.getOwner().getUniqueId().toString());

        //检查是否存入芯片
        if (workStationMeta.getChipItemStack() == null) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistChipWhenTakeChip"));
            return;
        }

        //检查是否配置文件里有这种芯片
        Chip chip = null;
        for (Chip c : ChipYaml.INSTANCE.listChip()) {

            Chip cc = ChipYaml.INSTANCE.getChip("chip." + c.getLevel());
            if (cc != null && cc.getItemStack().equals(ItemStackUtil.itemStackDeserialize(workStationMeta.getChipItemStack()))) {
                chip = c;
                break;
            }
        }
        if (chip == null) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistChipWhenTakeChip"));
            return;
        }


        //检查背包是否有空间
        if (PlayerUtil.checkSpace(player, chip.getItemStack()) < chip.getItemStack().getAmount()) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notEnoughSpaceWhenTakeChip"));
            return;
        }

        //取出芯片
        try {
            guiService.takeChip(mainOptionPage.getOwner().getUniqueId().toString());
        } catch (NotExistChipException e) {
            e.printStackTrace();
            return;
        }

        //添加芯片
        PlayerUtil.addItem(player, chip.getItemStack(), chip.getItemStack().getAmount());


        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.takeChip"));
    }
}