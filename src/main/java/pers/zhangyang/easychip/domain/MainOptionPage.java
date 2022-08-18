package pers.zhangyang.easychip.domain;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pers.zhangyang.easychip.yaml.GuiYaml;
import pers.zhangyang.easylibrary.base.BackAble;
import pers.zhangyang.easylibrary.base.GuiPage;
import pers.zhangyang.easylibrary.base.SingleGuiPageBase;
import pers.zhangyang.easylibrary.util.CommandUtil;

import java.util.List;

public class MainOptionPage extends SingleGuiPageBase implements BackAble {
    public MainOptionPage(Player viewer, GuiPage backPage, OfflinePlayer owner) {
        super(GuiYaml.INSTANCE.getString("gui.title.mainOptionPage"), viewer, backPage, owner);
    }

    @Override
    public void back() {
        List<String> cmdList = GuiYaml.INSTANCE.getStringList("gui.firstPageBackCommand");
        if (cmdList == null) {
            return;
        }
        CommandUtil.dispatchCommandList(viewer, cmdList);
    }

    @Override
    public void refresh() {

        ItemStack upgradeChip = GuiYaml.INSTANCE.getButtonDefault("gui.button.mainOptionPage.upgradeChip");
        inventory.setItem(13, upgradeChip);

        ItemStack depositChip = GuiYaml.INSTANCE.getButtonDefault("gui.button.mainOptionPage.depositChip");
        inventory.setItem(20, depositChip);
        ItemStack takeChip = GuiYaml.INSTANCE.getButtonDefault("gui.button.mainOptionPage.takeChip");
        inventory.setItem(29, takeChip);


        ItemStack depositItemStack = GuiYaml.INSTANCE.getButtonDefault("gui.button.mainOptionPage.depositItemStack");
        inventory.setItem(22, depositItemStack);
        ItemStack takeProp = GuiYaml.INSTANCE.getButtonDefault("gui.button.mainOptionPage.takeItemStack");
        inventory.setItem(31, takeProp);

        ItemStack installChip = GuiYaml.INSTANCE.getButtonDefault("gui.button.mainOptionPage.installChip");
        inventory.setItem(24, installChip);
        ItemStack uninstallChip = GuiYaml.INSTANCE.getButtonDefault("gui.button.mainOptionPage.uninstallChip");
        inventory.setItem(33, uninstallChip);


        ItemStack depositFortifier = GuiYaml.INSTANCE.getButtonDefault("gui.button.mainOptionPage.depositFortifier");
        inventory.setItem(18, depositFortifier);
        ItemStack takeFortifier = GuiYaml.INSTANCE.getButtonDefault("gui.button.mainOptionPage.takeFortifier");
        inventory.setItem(27, takeFortifier);


        ItemStack depositProtector = GuiYaml.INSTANCE.getButtonDefault("gui.button.mainOptionPage.depositProtector");
        inventory.setItem(26, depositProtector);
        ItemStack takeProtector = GuiYaml.INSTANCE.getButtonDefault("gui.button.mainOptionPage.takeProtector");
        inventory.setItem(35, takeProtector);


        ItemStack back = GuiYaml.INSTANCE.getButtonDefault("gui.button.mainOptionPage.back");
        inventory.setItem(49, back);

        viewer.openInventory(this.inventory);

    }
}
