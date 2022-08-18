package pers.zhangyang.easychip.yaml;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.zhangyang.easychip.domain.ProtectSetting;
import pers.zhangyang.easychip.domain.Protector;
import pers.zhangyang.easylibrary.base.YamlBase;

import java.util.HashSet;
import java.util.Set;

public class ProtectorYaml extends YamlBase {
    public static final ProtectorYaml INSTANCE = new ProtectorYaml();

    private ProtectorYaml() {
        super("protector.yml");
    }

    @NotNull
    public Set<Protector> listProtector() {
        Set<Protector> teleportPointList = new HashSet<>();

        ConfigurationSection configurationSection = yamlConfiguration.getConfigurationSection("protector");
        if (configurationSection == null) {
            return teleportPointList;
        }
        for (String s : configurationSection.getKeys(false)) {


            ItemStack itemStack = getItemStack("protector." + s + ".itemStack");
            if (itemStack == null) {
                continue;
            }
            Set<ProtectSetting> intensifySettingSet = new HashSet<>();


            ConfigurationSection configurationSection2 = yamlConfiguration.getConfigurationSection("protector." + s + ".protectSetting");
            if (configurationSection2 == null) {

                Protector teleportPoint = new Protector(s, itemStack);
                teleportPoint.setProtectSettingSet(intensifySettingSet);
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
                slotSize = getInteger("protector." + s + ".protectSetting." + ss + ".slotSize");

                if (slotSize == null || slotSize < 0) {
                    continue;
                }
                Double probability;
                probability = getDouble("protector." + s + ".protectSetting." + ss + ".probability");

                if (probability == null || probability < 0 || probability > 1) {
                    continue;
                }

                ProtectSetting intensifySetting = new ProtectSetting(level, slotSize, probability);
                intensifySettingSet.add(intensifySetting);

            }


            Protector teleportPoint = new Protector(s, itemStack);
            teleportPoint.setProtectSettingSet(intensifySettingSet);
            teleportPointList.add(teleportPoint);

        }
        return teleportPointList;
    }

    @Nullable
    public Protector getProtector(String path) {
        ItemStack itemStack = getItemStack(path + ".itemStack");
        if (itemStack == null) {
            return null;
        }
        Set<ProtectSetting> intensifySettingSet = new HashSet<>();


        ConfigurationSection configurationSection2 = yamlConfiguration.getConfigurationSection(path + ".protectSetting");
        if (configurationSection2 == null) {
            Protector protector = new Protector(path.split("\\.")[path.split("\\.").length - 1], itemStack);
            protector.setProtectSettingSet(intensifySettingSet);
            return protector;
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
                slotSize = getInteger(path + ".protectSetting." + ss + ".slotSize");
            } catch (NumberFormatException e) {
                continue;
            }
            if (slotSize == null || slotSize < 0) {
                continue;
            }
            Double probability;
            try {
                probability = getDouble(path + ".protectSetting." + ss + ".probability");
            } catch (NumberFormatException e) {
                continue;
            }
            if (probability == null || probability < 0 || probability > 1) {
                continue;
            }

            ProtectSetting intensifySetting = new ProtectSetting(level, slotSize, probability);
            intensifySettingSet.add(intensifySetting);

        }


        Protector protector = new Protector(path.split("\\.")[path.split("\\.").length - 1], itemStack);
        protector.setProtectSettingSet(intensifySettingSet);
        return protector;
    }
}
