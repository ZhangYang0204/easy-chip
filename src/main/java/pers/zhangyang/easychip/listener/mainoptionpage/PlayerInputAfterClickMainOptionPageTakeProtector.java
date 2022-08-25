package pers.zhangyang.easychip.listener.mainoptionpage;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pers.zhangyang.easychip.domain.Protector;
import pers.zhangyang.easychip.exception.NotExistProtectorException;
import pers.zhangyang.easychip.exception.NotMoreProtectorException;
import pers.zhangyang.easychip.meta.WorkStationMeta;
import pers.zhangyang.easychip.service.GuiService;
import pers.zhangyang.easychip.service.impl.GuiServiceImpl;
import pers.zhangyang.easychip.yaml.MessageYaml;
import pers.zhangyang.easychip.yaml.ProtectorYaml;
import pers.zhangyang.easylibrary.base.FiniteInputListenerBase;
import pers.zhangyang.easylibrary.base.GuiPage;
import pers.zhangyang.easylibrary.util.ItemStackUtil;
import pers.zhangyang.easylibrary.util.MessageUtil;
import pers.zhangyang.easylibrary.util.PlayerUtil;
import pers.zhangyang.easylibrary.util.TransactionInvocationHandler;

import java.util.List;

public class PlayerInputAfterClickMainOptionPageTakeProtector extends FiniteInputListenerBase {
    public PlayerInputAfterClickMainOptionPageTakeProtector(Player player, OfflinePlayer owner, GuiPage previousPage) {
        super(player, owner, previousPage, 1);
        List<String> list = MessageYaml.INSTANCE.getStringList("message.chat.howToTakeProtector");
        MessageUtil.sendMessageTo(player, list);
    }

    @Override
    public void run() {
        int amount;
        try {
            amount = Integer.parseInt(messages[0]);
        } catch (NumberFormatException e) {
            List<String> list = MessageYaml.INSTANCE.getStringList("message.chat.invalidNumber");
            MessageUtil.sendMessageTo(player, list);
            return;
        }
        if (amount < 0) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.invalidNumber"));
            return;
        }
        GuiService guiService = (GuiService) new TransactionInvocationHandler(new GuiServiceImpl()).getProxy();
        WorkStationMeta workStationMeta = guiService.getWorkStation(owner.getUniqueId().toString());
        if (workStationMeta.getProtectorItemStack() == null) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistProtectorWhenTakeProtector"));
            return;
        }
        //配置文件是不是有
        Protector chip = null;
        for (Protector c : ProtectorYaml.INSTANCE.listProtector()) {
            if (c.getItemStack().equals(ItemStackUtil.itemStackDeserialize(workStationMeta.getProtectorItemStack()))) {
                chip = c;
                break;
            }
        }
        if (chip == null) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistProtectorWhenTakeProtector"));
            return;
        }


        if (PlayerUtil.checkSpace(player, chip.getItemStack()) < chip.getItemStack().getAmount() * amount) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notEnoughSpaceWhenTakeProtector"));
            return;
        }

        try {
            guiService.takeProtector(owner.getUniqueId().toString(), amount);
        } catch (NotExistProtectorException | NotMoreProtectorException e) {
            e.printStackTrace();
            return;
        }
        ItemStack chipItemStack = chip.getItemStack();
        PlayerUtil.addItem(player, chipItemStack, chip.getItemStack().getAmount() * amount);
        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.takeProtector"));


    }
}
