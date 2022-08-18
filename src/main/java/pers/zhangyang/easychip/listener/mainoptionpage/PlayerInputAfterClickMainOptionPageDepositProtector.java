package pers.zhangyang.easychip.listener.mainoptionpage;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import pers.zhangyang.easychip.domain.Chip;
import pers.zhangyang.easychip.domain.ProtectSetting;
import pers.zhangyang.easychip.domain.Protector;
import pers.zhangyang.easychip.exception.DuplicateProtectorException;
import pers.zhangyang.easychip.exception.NotExistProtectorException;
import pers.zhangyang.easychip.meta.WorkStationMeta;
import pers.zhangyang.easychip.service.GuiService;
import pers.zhangyang.easychip.service.impl.GuiServiceImpl;
import pers.zhangyang.easychip.yaml.ChipYaml;
import pers.zhangyang.easychip.yaml.MessageYaml;
import pers.zhangyang.easychip.yaml.ProtectorYaml;
import pers.zhangyang.easylibrary.base.FiniteInputListenerBase;
import pers.zhangyang.easylibrary.base.GuiPage;
import pers.zhangyang.easylibrary.util.ItemStackUtil;
import pers.zhangyang.easylibrary.util.MessageUtil;
import pers.zhangyang.easylibrary.util.PlayerUtil;
import pers.zhangyang.easylibrary.util.TransactionInvocationHandler;

import java.util.List;

public class PlayerInputAfterClickMainOptionPageDepositProtector extends FiniteInputListenerBase {
    public PlayerInputAfterClickMainOptionPageDepositProtector(Player player, OfflinePlayer owner, GuiPage previousPage) {
        super(player, owner, previousPage, 2);
        List<String> list = MessageYaml.INSTANCE.getStringList("message.chat.howToDepositProtector");
        MessageUtil.sendMessageTo(player, list);
    }

    @Override
    public void run() {

        int amount;
        try {
            amount = Integer.parseInt(messages[1]);
        } catch (NumberFormatException e) {
            List<String> list = MessageYaml.INSTANCE.getStringList("message.chat.invalidNumber");
            MessageUtil.sendMessageTo(player, list);
            return;
        }
        if (amount < 0) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.invalidNumber"));
            return;
        }

        //检查配置文件是不是有这个保护剂
        Protector protector = null;
        for (Protector c : ProtectorYaml.INSTANCE.listProtector()) {
            if (c.getName().equals(messages[0])) {
                protector = c;
                break;
            }
        }
        if (protector == null) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistProtectorWhenDepositProtector"));
            return;
        }

        //检查玩家是不是拥有这么多保护剂
        if (PlayerUtil.computeItemHave(protector.getItemStack(), player) < amount) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notEnoughProtector"));
            return;
        }


        GuiService guiService = (GuiService) new TransactionInvocationHandler(new GuiServiceImpl()).getProxy();
        WorkStationMeta workStationMeta = guiService.getWorkStation(owner.getUniqueId().toString());

        //检查是不是存入芯片
        if (workStationMeta.getChipItemStack() == null) {
            List<String> list = MessageYaml.INSTANCE.getStringList("message.chat.notExistChipWhenDepositProtector");
            MessageUtil.sendMessageTo(player, list);
            return;
        }


        //检查是不是超标
        Integer slotSize = null;
        if (protector.getProtectSettingSet() != null) {
            for (ProtectSetting is : protector.getProtectSettingSet()) {
                Chip chip = ChipYaml.INSTANCE.getChip("chip." + is.getLevel());
                if (chip != null && chip.getItemStack().equals(ItemStackUtil.itemStackDeserialize(workStationMeta.getChipItemStack()))) {
                    slotSize = is.getSlotSize();
                    break;
                }

            }
        }
        if (slotSize == null) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notSuitableProtector"));
            return;
        }
        if (workStationMeta.getProtectorItemStack() != null && workStationMeta.getProtectorAmount() != null) {
            if (workStationMeta.getProtectorItemStack().equals(protector.getName()) && workStationMeta.getProtectorAmount() + amount > slotSize) {
                MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notMoreSpaceWhenDepositProtector"));
                return;
            }
        } else {
            if (amount > slotSize) {
                MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notMoreSpaceWhenDepositProtector"));
                return;
            }
        }

        //存入
        try {
            guiService.depositProtector(owner.getUniqueId().toString(), ItemStackUtil.itemStackSerialize(protector.getItemStack()), amount);
        } catch (DuplicateProtectorException e) {
            List<String> list = MessageYaml.INSTANCE.getStringList("message.chat.duplicateProtector");
            MessageUtil.sendMessageTo(player, list);
            return;
        } catch (NotExistProtectorException e) {
            e.printStackTrace();
            return;
        }
        PlayerUtil.removeItem(player, protector.getItemStack(), amount);
        List<String> list = MessageYaml.INSTANCE.getStringList("message.chat.depositProtector");
        MessageUtil.sendMessageTo(player, list);
    }
}
