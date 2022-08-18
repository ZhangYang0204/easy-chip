package pers.zhangyang.easychip.yaml;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.zhangyang.easychip.domain.Fortifier;
import pers.zhangyang.easychip.domain.FortifySettingSetting;
import pers.zhangyang.easylibrary.base.YamlBase;

import java.util.HashSet;
import java.util.Set;

public class FortifierYaml extends YamlBase {
    public static final FortifierYaml INSTANCE = new FortifierYaml();

    private FortifierYaml() {
        super("fortifier.yml");
    }

    @NotNull
    public Set<Fortifier> listFortifier() {
        Set<Fortifier> teleportPointList = new HashSet<>();

        ConfigurationSection configurationSection = yamlConfiguration.getConfigurationSection("fortifier");
        if (configurationSection == null) {
            return teleportPointList;
        }
        for (String s : configurationSection.getKeys(false)) {


            ItemStack itemStack = getItemStack("fortifier." + s + ".itemStack");
            if (itemStack == null) {
                continue;
            }
            Set<FortifySettingSetting> fortifySettingSettingSet = new HashSet<>();


            ConfigurationSection configurationSection2 = yamlConfiguration.getConfigurationSection("fortifier." + s + ".fortifySetting");
            if (configurationSection2 == null) {
                Fortifier teleportPoint = new Fortifier(s, itemStack);
                teleportPoint.setIntensifySettingSet(fortifySettingSettingSet);
                teleportPointList.add(teleportPoint);
                continue;
            }
            for (String ss : configurationSection2.getKeys(false)) {
                int level;
                try {
                    level = Integer.parseInt(ss);
                } catch (NumberFormatException e) {
                    continue;
                }
                Integer slotSize;
                slotSize = getInteger("fortifier." + s + ".fortifySetting." + ss + ".slotSize");

                if (slotSize == null || slotSize < 0) {
                    continue;
                }
                Double probability;
                probability = getDouble("fortifier." + s + ".fortifySetting." + ss + ".probability");
                if (probability == null || probability < 0 || probability > 1) {
                    continue;
                }

                FortifySettingSetting fortifySettingSetting = new FortifySettingSetting(level, slotSize, probability);
                fortifySettingSettingSet.add(fortifySettingSetting);

            }


            Fortifier teleportPoint = new Fortifier(s, itemStack);
            teleportPoint.setIntensifySettingSet(fortifySettingSettingSet);
            teleportPointList.add(teleportPoint);

        }
        return teleportPointList;
    }

    @Nullable
    public Fortifier getFortifier(String path) {
        ItemStack itemStack = getItemStack(path + ".itemStack");
        if (itemStack == null) {
            return null;
        }
        Set<FortifySettingSetting> fortifySettingSettingSet = new HashSet<>();

        ConfigurationSection configurationSection2 = yamlConfiguration.getConfigurationSection(path + ".fortifySetting");
        if (configurationSection2 == null) {
            Fortifier fortifier = new Fortifier(path.split("\\.")[path.split("\\.").length - 1], itemStack);
            fortifier.setIntensifySettingSet(fortifySettingSettingSet);
            return fortifier;
        }
        for (String ss : configurationSection2.getKeys(false)) {
            int level;
            try {
                level = Integer.parseInt(ss);
            } catch (NumberFormatException e) {
                continue;
            }
            Integer slotSize;
            try {
                slotSize = getInteger(path + ".fortifySetting." + ss + ".slotSize");
            } catch (NumberFormatException e) {
                continue;
            }
            if (slotSize == null || slotSize < 0) {
                continue;
            }
            Double probability;
            try {
                probability = getDouble(path + ".fortifySetting." + ss + ".probability");
            } catch (NumberFormatException e) {
                continue;
            }
            if (probability == null || probability < 0 || probability > 1) {
                continue;
            }

            FortifySettingSetting fortifySettingSetting = new FortifySettingSetting(level, slotSize, probability);
            fortifySettingSettingSet.add(fortifySettingSetting);

        }


        Fortifier fortifier = new Fortifier(path.split("\\.")[path.split("\\.").length - 1], itemStack);
        fortifier.setIntensifySettingSet(fortifySettingSettingSet);
        return fortifier;
    }
}
