package pers.zhangyang.easychip.listener.mainoptionpage;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import pers.zhangyang.easychip.domain.Chip;
import pers.zhangyang.easychip.exception.DuplicateChipException;
import pers.zhangyang.easychip.exception.NotExistChipException;
import pers.zhangyang.easychip.service.GuiService;
import pers.zhangyang.easychip.service.impl.GuiServiceImpl;
import pers.zhangyang.easychip.yaml.ChipYaml;
import pers.zhangyang.easychip.yaml.MessageYaml;
import pers.zhangyang.easylibrary.base.FiniteInputListenerBase;
import pers.zhangyang.easylibrary.base.GuiPage;
import pers.zhangyang.easylibrary.util.ItemStackUtil;
import pers.zhangyang.easylibrary.util.MessageUtil;
import pers.zhangyang.easylibrary.util.PlayerUtil;
import pers.zhangyang.easylibrary.util.TransactionInvocationHandler;

import java.util.List;

public class PlayerInputAfterClickMaInOptionPageDepositChip extends FiniteInputListenerBase {
    public PlayerInputAfterClickMaInOptionPageDepositChip(Player player, OfflinePlayer owner, GuiPage previousPage) {
        super(player, owner, previousPage, 1);
        List<String> list = MessageYaml.INSTANCE.getStringList("message.chat.howToDepositChip");
        MessageUtil.sendMessageTo(player, list);

    }

    @Override
    public void run() {

        int level;
        try {
            level = Integer.parseInt(messages[0]);
        } catch (NumberFormatException e) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.invalidNumber"));
            return;
        }
        if (level < 0) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.invalidNumber"));
            return;
        }

        Chip chip = null;
        for (Chip c : ChipYaml.INSTANCE.listChip()) {
            if (c.getLevel() == level) {
                chip = c;
                break;
            }
        }
        if (chip == null) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistChipWhenDepositChip"));
            return;
        }

        if (PlayerUtil.computeItemHave(chip.getItemStack(), player) < chip.getItemStack().getAmount()) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notEnoughChipWhenDepositChip"));
            return;
        }


        GuiService guiService = (GuiService) new TransactionInvocationHandler(new GuiServiceImpl()).getProxy();


        try {
            guiService.depositChip(owner.getUniqueId().toString(), ItemStackUtil.itemStackSerialize(chip.getItemStack()));
        } catch (DuplicateChipException e) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.duplicateChip"));
            return;
        } catch (NotExistChipException e) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistChipWhenDepositChip"));
            return;
        }

        PlayerUtil.removeItem(player, chip.getItemStack(), chip.getItemStack().getAmount());


        List<String> list = MessageYaml.INSTANCE.getStringList("message.chat.depositChip");
        MessageUtil.sendMessageTo(player, list);
    }
}
