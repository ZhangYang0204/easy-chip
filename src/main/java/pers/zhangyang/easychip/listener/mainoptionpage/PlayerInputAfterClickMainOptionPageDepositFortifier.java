package pers.zhangyang.easychip.listener.mainoptionpage;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import pers.zhangyang.easychip.domain.Chip;
import pers.zhangyang.easychip.domain.Fortifier;
import pers.zhangyang.easychip.domain.IntensifySetting;
import pers.zhangyang.easychip.exception.DuplicateFortifierException;
import pers.zhangyang.easychip.exception.NotExistFortifierException;
import pers.zhangyang.easychip.meta.WorkStationMeta;
import pers.zhangyang.easychip.service.GuiService;
import pers.zhangyang.easychip.service.impl.GuiServiceImpl;
import pers.zhangyang.easychip.yaml.ChipYaml;
import pers.zhangyang.easychip.yaml.FortifierYaml;
import pers.zhangyang.easychip.yaml.MessageYaml;
import pers.zhangyang.easylibrary.base.FiniteInputListenerBase;
import pers.zhangyang.easylibrary.base.GuiPage;
import pers.zhangyang.easylibrary.util.ItemStackUtil;
import pers.zhangyang.easylibrary.util.MessageUtil;
import pers.zhangyang.easylibrary.util.PlayerUtil;
import pers.zhangyang.easylibrary.util.TransactionInvocationHandler;

import java.util.List;

public class PlayerInputAfterClickMainOptionPageDepositFortifier extends FiniteInputListenerBase {
    public PlayerInputAfterClickMainOptionPageDepositFortifier(Player player, OfflinePlayer owner, GuiPage previousPage) {
        super(player, owner, previousPage, 2);
        List<String> list = MessageYaml.INSTANCE.getStringList("message.chat.howToDepositFortifier");
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

        //检查配置文件是不是有这种强化剂
        Fortifier fortifier = null;
        for (Fortifier c : FortifierYaml.INSTANCE.listFortifier()) {
            if (c.getName().equals(messages[0])) {
                fortifier = c;
                break;
            }
        }
        if (fortifier == null) {
            List<String> list = MessageYaml.INSTANCE.getStringList("message.chat.notExistFortifierWhenDepositFortifier");
            MessageUtil.sendMessageTo(player, list);
            return;
        }

        //检查玩家是不是拥有这么多强化剂
        if (PlayerUtil.computeItemHave(fortifier.getItemStack(), player) < amount) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notEnoughFortifier"));
            return;
        }


        GuiService guiService = (GuiService) new TransactionInvocationHandler(new GuiServiceImpl()).getProxy();

        WorkStationMeta workStationMeta = guiService.getWorkStation(owner.getUniqueId().toString());


        //检查是不是存入芯片
        if (workStationMeta.getChipItemStack() == null) {
            List<String> list = MessageYaml.INSTANCE.getStringList("message.chat.notExistChipWhenDepositFortifier");
            MessageUtil.sendMessageTo(player, list);
            return;
        }

        //检查存入的强化剂数量是不是超标了
        Integer slotSize = null;
        if (fortifier.getIntensifySettingSet() != null) {
            for (IntensifySetting is : fortifier.getIntensifySettingSet()) {
                Chip chip = ChipYaml.INSTANCE.getChip("chip." + is.getLevel());
                if (chip != null && chip.getItemStack().equals(ItemStackUtil.itemStackDeserialize(workStationMeta.getChipItemStack()))) {
                    slotSize = is.getSlotSize();
                    break;
                }

            }
        }
        if (slotSize == null) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notSuitableFortifier"));
            return;
        }
        if (workStationMeta.getFortifierItemStack() != null && workStationMeta.getFortifierAmount() != null) {
            if (workStationMeta.getFortifierItemStack().equals(fortifier.getName()) && workStationMeta.getFortifierAmount() + amount > slotSize) {
                MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notMoreSpaceWhenDepositFortifier"));
                return;
            }
        } else {
            if (amount > slotSize) {
                MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notMoreSpaceWhenDepositFortifier"));
                return;
            }
        }


        //存入
        try {
            guiService.depositFortifier(owner.getUniqueId().toString(), ItemStackUtil.itemStackSerialize(fortifier.getItemStack()), amount);
        } catch (DuplicateFortifierException e) {
            List<String> list = MessageYaml.INSTANCE.getStringList("message.chat.duplicateFortifier");
            MessageUtil.sendMessageTo(player, list);
            return;
        } catch (NotExistFortifierException e) {
            e.printStackTrace();
            return;
        }
        PlayerUtil.removeItem(player, fortifier.getItemStack(), amount);


        List<String> list = MessageYaml.INSTANCE.getStringList("message.chat.depositFortifier");
        MessageUtil.sendMessageTo(player, list);
    }
}
