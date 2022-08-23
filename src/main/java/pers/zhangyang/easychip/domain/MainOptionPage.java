package pers.zhangyang.easychip.domain;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pers.zhangyang.easychip.meta.WorkStationMeta;
import pers.zhangyang.easychip.service.GuiService;
import pers.zhangyang.easychip.service.impl.GuiServiceImpl;
import pers.zhangyang.easychip.yaml.ChipYaml;
import pers.zhangyang.easychip.yaml.FortifierYaml;
import pers.zhangyang.easychip.yaml.GuiYaml;
import pers.zhangyang.easychip.yaml.ProtectorYaml;
import pers.zhangyang.easylibrary.base.BackAble;
import pers.zhangyang.easylibrary.base.GuiPage;
import pers.zhangyang.easylibrary.base.SingleGuiPageBase;
import pers.zhangyang.easylibrary.util.CommandUtil;
import pers.zhangyang.easylibrary.util.ItemStackUtil;
import pers.zhangyang.easylibrary.util.ReplaceUtil;
import pers.zhangyang.easylibrary.util.TransactionInvocationHandler;

import java.util.HashMap;
import java.util.List;

public class MainOptionPage extends SingleGuiPageBase implements BackAble {
    public MainOptionPage(Player viewer, GuiPage backPage, OfflinePlayer owner) {
        super(GuiYaml.INSTANCE.getString("gui.title.mainOptionPage"), viewer, backPage, owner,54);
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
    public int getBackSlot() {
        return 49;
    }

    @Override
    public void refresh() {

        GuiService guiService= (GuiService) new TransactionInvocationHandler(new GuiServiceImpl()).getProxy();
        WorkStationMeta workStationMeta=guiService.getWorkStation(owner.getUniqueId().toString());
        Chip chip=null;
        if (workStationMeta.getChipItemStack()!=null) {
            for (Chip c : ChipYaml.INSTANCE.listChip()) {
                if (c.getItemStack().equals(ItemStackUtil.itemStackDeserialize(workStationMeta.getChipItemStack()))) {
                    chip=c;
                    break;
                }
            }
        }
        Fortifier fortifier=null;
        if (workStationMeta.getFortifierItemStack()!=null) {
            for (Fortifier c : FortifierYaml.INSTANCE.listFortifier()) {
                if (c.getItemStack().equals(ItemStackUtil.itemStackDeserialize(workStationMeta.getFortifierItemStack()))) {
                    fortifier=c;
                    break;
                }
            }
        }
        Protector protector=null;
        if (workStationMeta.getProtectorItemStack()!=null) {
            for (Protector c : ProtectorYaml.INSTANCE.listProtector()) {
                if (c.getItemStack().equals(ItemStackUtil.itemStackDeserialize(workStationMeta.getProtectorItemStack()))) {
                    protector=c;
                    break;
                }
            }
        }
        HashMap<String,String> rep=new HashMap<>();

        Double fortifyP=null;
        if (chip!=null&&fortifier!=null){
            Integer slotSize=null;
            Double p=null;
            if (fortifier.getIntensifySettingSet()!=null) {
                for (FortifySettingSetting fs : fortifier.getIntensifySettingSet()) {
                    if (fs.getLevel()==chip.getLevel()){
                        slotSize= fs.getSlotSize();
                        p=fs.getProbability();
                        break;
                    }
                }
            }
            if (slotSize != null&&workStationMeta.getFortifierAmount()!=null&&workStationMeta.getFortifierAmount()<=slotSize){
                fortifyP=workStationMeta.getFortifierAmount()*p;
                if (fortifyP>1){
                    fortifyP=1.0;
                }
            }
        }
        Double protectP=null;
        if (chip!=null&&protector!=null){
            Integer slotSize=null;
            Double p=null;
            if (protector.getProtectSettingSet()!=null) {
                for (ProtectSetting fs : protector.getProtectSettingSet()) {
                    if (fs.getLevel()==chip.getLevel()){
                        slotSize= fs.getSlotSize();
                        p=fs.getProbability();
                        break;
                    }
                }
            }
            if (slotSize != null&&workStationMeta.getProtectorAmount()!=null&&workStationMeta.getProtectorAmount()<=slotSize){
                protectP=workStationMeta.getProtectorAmount()*p;
                if (protectP>1){
                    protectP=1.0;
                }
            }
        }
        rep.put("{fortify_probability}",fortifyP==null?"/": String.valueOf(fortifyP));
        rep.put("{protect_probability}",protectP==null?"/": String.valueOf(protectP));
        ItemStack upgradeChip = GuiYaml.INSTANCE.getButtonDefault("gui.button.mainOptionPage.upgradeChip");
        ReplaceUtil.replaceLore(upgradeChip,rep);
        ReplaceUtil.replaceDisplayName(upgradeChip,rep);
        inventory.setItem(13, upgradeChip);




        rep=new HashMap<>();
        rep.put("{chip_level}",chip==null?"/": String.valueOf(chip.getLevel()));

        ItemStack depositChip = GuiYaml.INSTANCE.getButtonDefault("gui.button.mainOptionPage.depositChip");
        ReplaceUtil.replaceLore(depositChip,rep);
        ReplaceUtil.replaceDisplayName(depositChip,rep);
        inventory.setItem(20, depositChip);

        ItemStack takeChip = GuiYaml.INSTANCE.getButtonDefault("gui.button.mainOptionPage.takeChip");
        ReplaceUtil.replaceLore(takeChip,rep);
        ReplaceUtil.replaceDisplayName(takeChip,rep);
        inventory.setItem(29, takeChip);


        ItemStack depositItemStack = GuiYaml.INSTANCE.getButtonDefault("gui.button.mainOptionPage.depositItemStack");
        inventory.setItem(22, depositItemStack);
        ItemStack takeProp = GuiYaml.INSTANCE.getButtonDefault("gui.button.mainOptionPage.takeItemStack");
        inventory.setItem(31, takeProp);

        ItemStack installChip = GuiYaml.INSTANCE.getButtonDefault("gui.button.mainOptionPage.installChip");
        inventory.setItem(24, installChip);
        ItemStack uninstallChip = GuiYaml.INSTANCE.getButtonDefault("gui.button.mainOptionPage.uninstallChip");
        inventory.setItem(33, uninstallChip);


        rep=new HashMap<>();

        rep.put("{fortifier_name}",fortifier==null?"/":fortifier.getName());
        rep.put("{fortifier_amount}",fortifier==null?"/": String.valueOf(workStationMeta.getFortifierAmount()));
        ItemStack depositFortifier = GuiYaml.INSTANCE.getButtonDefault("gui.button.mainOptionPage.depositFortifier");
        ReplaceUtil.replaceLore(depositFortifier,rep);
        ReplaceUtil.replaceDisplayName(depositFortifier,rep);
        inventory.setItem(18, depositFortifier);

        ItemStack takeFortifier = GuiYaml.INSTANCE.getButtonDefault("gui.button.mainOptionPage.takeFortifier");
        ReplaceUtil.replaceLore(takeFortifier,rep);
        ReplaceUtil.replaceDisplayName(takeFortifier,rep);
        inventory.setItem(27, takeFortifier);

        rep=new HashMap<>();

        rep.put("{protector_name}",protector==null?"/":protector.getName());
        rep.put("{protector_amount}",protector==null?"/": String.valueOf(workStationMeta.getProtectorAmount()));
        ItemStack depositProtector = GuiYaml.INSTANCE.getButtonDefault("gui.button.mainOptionPage.depositProtector");
        ReplaceUtil.replaceLore(depositProtector,rep);
        ReplaceUtil.replaceDisplayName(depositProtector,rep);
        inventory.setItem(26, depositProtector);

        ItemStack takeProtector = GuiYaml.INSTANCE.getButtonDefault("gui.button.mainOptionPage.takeProtector");
        ReplaceUtil.replaceLore(takeProtector,rep);
        ReplaceUtil.replaceDisplayName(takeProtector,rep);
        inventory.setItem(35, takeProtector);


        ItemStack back = GuiYaml.INSTANCE.getButtonDefault("gui.button.mainOptionPage.back");
        inventory.setItem(49, back);

        viewer.openInventory(this.inventory);

    }
}
