package pers.zhangyang.easychip.yaml;

import pers.zhangyang.easylibrary.base.YamlBase;

public class GuiYaml extends YamlBase {
    public static final GuiYaml INSTANCE = new GuiYaml();

    private GuiYaml() {
        super("display/" + SettingYaml.INSTANCE.getDisplay() + "/gui.yml");
    }

}
