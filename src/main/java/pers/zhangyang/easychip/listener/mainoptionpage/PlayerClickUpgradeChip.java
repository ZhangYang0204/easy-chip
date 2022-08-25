package pers.zhangyang.easychip.listener.mainoptionpage;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import pers.zhangyang.easychip.domain.*;
import pers.zhangyang.easychip.exception.*;
import pers.zhangyang.easychip.meta.WorkStationMeta;
import pers.zhangyang.easychip.service.GuiService;
import pers.zhangyang.easychip.service.impl.GuiServiceImpl;
import pers.zhangyang.easychip.yaml.*;
import pers.zhangyang.easylibrary.annotation.EventListener;
import pers.zhangyang.easylibrary.annotation.GuiDiscreteButtonHandler;
import pers.zhangyang.easylibrary.util.CommandUtil;
import pers.zhangyang.easylibrary.util.ItemStackUtil;
import pers.zhangyang.easylibrary.util.MessageUtil;
import pers.zhangyang.easylibrary.util.TransactionInvocationHandler;

import java.util.List;
import java.util.Random;

@EventListener
public class PlayerClickUpgradeChip implements Listener {
    @GuiDiscreteButtonHandler(guiPage = MainOptionPage.class, slot = {13},closeGui = false,refreshGui = true)
    public void on(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();
        MainOptionPage mainOptionPage = (MainOptionPage) event.getInventory().getHolder();
        assert mainOptionPage != null;


        Player onlineOwner = mainOptionPage.getOwner().getPlayer();
        if (onlineOwner == null) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notOnline"));
            return;
        }

        GuiService guiService = (GuiService) new TransactionInvocationHandler(new GuiServiceImpl()).getProxy();
        WorkStationMeta workStationMeta = guiService.getWorkStation(mainOptionPage.getOwner().getUniqueId().toString());

        //检查芯片是不是存入了
        if (workStationMeta.getChipItemStack() == null) {

            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notEnoughChipWhenUpgradeChip"));
            return;
        }

        //检查芯片是不是在配置文件里存在，否则清除
        Chip chip = null;
        for (Chip c : ChipYaml.INSTANCE.listChip()) {
            Chip cc = ChipYaml.INSTANCE.getChip("chip." + (c.getLevel()));
            if (cc != null && cc.getItemStack().equals(ItemStackUtil.itemStackDeserialize(workStationMeta.getChipItemStack()))) {
                chip = c;
                break;
            }
        }
        if (chip == null) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notEnoughChipWhenUpgradeChip"));
            return;
        }


        //检查是不是有下一级芯片，否则不让强化
        Chip nextChip = null;
        for (Chip c : ChipYaml.INSTANCE.listChip()) {
            if (c.getLevel() - 1 == chip.getLevel()) {
                nextChip = c;
                break;
            }
        }
        if (nextChip == null) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notNextChipWhenUpgradeChip"));
            return;
        }


        //检查强化设置是不是完成了
        Upgrader upgrader = UpgraderYaml.INSTANCE.getUpgrader("upgrader." + chip.getLevel());
        if (upgrader == null) {
            MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notExistUpgraderWhenUpgradeChip"));
            return;
        }

        //检查是否强化剂合适
        Fortifier fortifier = null;
        if (workStationMeta.getFortifierItemStack() != null) {
            for (Fortifier c : FortifierYaml.INSTANCE.listFortifier()) {
                if (c.getItemStack().equals(ItemStackUtil.itemStackDeserialize(workStationMeta.getFortifierItemStack()))) {
                    fortifier = c;
                    break;
                }
            }
        }
        if (fortifier != null) {
            boolean suit = false;
            if (fortifier.getIntensifySettingSet() != null) {
                for (FortifySettingSetting i : fortifier.getIntensifySettingSet()) {
                    if (i.getLevel() == chip.getLevel()) {
                        suit = true;
                        break;
                    }
                }
            }
            if (!suit) {
                MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notSuitableFortifier"));
                return;
            }
        }

        //检查是否保护剂合适
        Protector protector = null;
        if (workStationMeta.getProtectorItemStack() != null) {
            for (Protector c : ProtectorYaml.INSTANCE.listProtector()) {
                if (c.getItemStack().equals(ItemStackUtil.itemStackDeserialize(workStationMeta.getProtectorItemStack()))) {
                    protector = c;
                    break;
                }
            }
        }
        if (protector != null) {
            boolean suit = false;
            if (protector.getProtectSettingSet() != null) {
                for (ProtectSetting i : protector.getProtectSettingSet()) {
                    if (i.getLevel() == chip.getLevel()) {
                        suit = true;
                        break;
                    }
                }
            }
            if (!suit) {
                MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.notSuitableProtector"));
                return;
            }
        }


        //检查存入的强化剂数量是不是超标了
        if (fortifier != null) {
            Integer slotSize = null;
            if (fortifier.getIntensifySettingSet() != null) {
                for (FortifySettingSetting is : fortifier.getIntensifySettingSet()) {
                    if (is.getLevel() == chip.getLevel()) {
                        slotSize = is.getSlotSize();
                        break;
                    }

                }
            }
            assert slotSize != null;
            if (workStationMeta.getFortifierAmount() != null && workStationMeta.getFortifierAmount() > slotSize) {
                MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.tooMuchFortifierWhenDepositFortifier"));
                return;

            }
        }

        //检查存入的强化剂数量是不是超标了
        if (protector != null) {
            Integer slotSize = null;
            if (protector.getProtectSettingSet() != null) {
                for (ProtectSetting is : protector.getProtectSettingSet()) {
                    if (is.getLevel() == chip.getLevel()) {
                        slotSize = is.getSlotSize();
                        break;
                    }

                }
            }
            assert slotSize != null;
            if (workStationMeta.getProtectorAmount() != null && workStationMeta.getProtectorAmount() > slotSize) {
                MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.tooMuchFortifierWhenDepositProtector"));
                return;
            }
        }


        //升级
        Chip preciousChip = null;
        for (Chip c : ChipYaml.INSTANCE.listChip()) {
            if (c.getLevel() + 1 == chip.getLevel()) {
                preciousChip = c;
                break;
            }
        }

        double protectorPro = 0;
        if (protector != null && workStationMeta.getProtectorAmount() != null) {
            assert protector.getProtectSettingSet() != null;
            for (ProtectSetting ps : protector.getProtectSettingSet()) {
                if (ps.getLevel() != chip.getLevel()) {
                    continue;
                }
                protectorPro += ps.getProbability() * workStationMeta.getProtectorAmount();

            }
        }
        double fortifierPro = 0;
        if (fortifier != null && workStationMeta.getFortifierAmount() != null) {
            assert fortifier.getIntensifySettingSet() != null;
            for (FortifySettingSetting ps : fortifier.getIntensifySettingSet()) {
                if (ps.getLevel() != chip.getLevel()) {
                    continue;
                }
                fortifierPro += ps.getProbability() * workStationMeta.getFortifierAmount();
            }
        }
        boolean result = new Random().nextDouble() < fortifierPro;
        if (result) {
            try {
                guiService.setChip(mainOptionPage.getOwner().getUniqueId().toString(), workStationMeta.getChipItemStack() + 1);
            } catch (NotExistChipException e) {
                e.printStackTrace();
                return;
            }
            List<String> successCmd = upgrader.getSuccessCommand();
            if (successCmd != null) {
                CommandUtil.dispatchCommandList(onlineOwner, successCmd);
            }
        } else {
            boolean protectorResult = new Random().nextDouble() < protectorPro;
            if (!protectorResult) {
                if (upgrader.getEnableFailureDamage()) {

                    try {
                        guiService.takeChip(mainOptionPage.getOwner().getUniqueId().toString());
                    } catch (NotExistChipException e) {
                        e.printStackTrace();
                        return;
                    }

                } else if (upgrader.getEnableFailureDowngrade() && preciousChip != null) {

                    try {

                        guiService.setChip(mainOptionPage.getOwner().getUniqueId().toString(), ItemStackUtil.itemStackSerialize(preciousChip.getItemStack()));

                    } catch (NotExistChipException e) {
                        e.printStackTrace();
                        return;
                    }

                }
            }
            List<String> failureCommand = upgrader.getFailureCommand();
            if (failureCommand != null) {
                CommandUtil.dispatchCommandList(onlineOwner, failureCommand);
            }

        }
        //取出2个试剂
        try {
            if (workStationMeta.getFortifierItemStack() != null && workStationMeta.getFortifierAmount() != null) {
                guiService.takeFortifier(mainOptionPage.getOwner().getUniqueId().toString(), workStationMeta.getFortifierAmount());
            }
            if (workStationMeta.getProtectorItemStack() != null && workStationMeta.getProtectorAmount() != null) {
                guiService.takeProtector(mainOptionPage.getOwner().getUniqueId().toString(), workStationMeta.getProtectorAmount());
            }
        } catch (NotExistFortifierException | NotMoreFortifierException | NotExistProtectorException |
                 NotMoreProtectorException e) {
            e.printStackTrace();
            return;
        }

        MessageUtil.sendMessageTo(player, MessageYaml.INSTANCE.getStringList("message.chat.upgradeChip"));


    }
}