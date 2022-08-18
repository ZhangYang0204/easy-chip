package pers.zhangyang.easychip.yaml;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.zhangyang.easychip.domain.Upgrader;
import pers.zhangyang.easylibrary.base.YamlBase;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UpgraderYaml extends YamlBase {
    public static final UpgraderYaml INSTANCE = new UpgraderYaml();

    private UpgraderYaml() {
        super("upgrader.yml");
    }


    @NotNull
    public Set<Upgrader> listUpgrader() {
        Set<Upgrader> teleportPointList = new HashSet<>();

        ConfigurationSection configurationSection = yamlConfiguration.getConfigurationSection("upgrader");
        if (configurationSection == null) {
            return teleportPointList;
        }
        for (String s : configurationSection.getKeys(false)) {

            int level;
            try {
                level = Integer.parseInt(s);
            } catch (NumberFormatException e) {
                continue;
            }
            Boolean enableFailureDamage = getBoolean("upgrader." + s + ".enableFailureDamage");
            Boolean enableFailureDowngrade = getBoolean("upgrader." + s + ".enableFailureDowngrade");
            if (enableFailureDamage == null || enableFailureDowngrade == null) {
                continue;
            }
            Upgrader teleportPoint;
            teleportPoint = new Upgrader(enableFailureDamage, enableFailureDowngrade, level);


            List<String> successCommand = getStringList("upgrader." + s + ".successCommand");
            List<String> failureCommand = getStringList("upgrader." + s + ".failureCommand");
            teleportPoint.setFailureCommand(failureCommand);
            teleportPoint.setSuccessCommand(successCommand);
            teleportPointList.add(teleportPoint);

        }
        return teleportPointList;
    }

    @Nullable
    public Upgrader getUpgrader(String path) {


        int level = Integer.parseInt(path.split("\\.")[path.split("\\.").length - 1]);
        Boolean enableFailureDamage = getBoolean(path + ".enableFailureDamage");
        Boolean enableFailureDowngrade = getBoolean(path + ".enableFailureDowngrade");
        if (enableFailureDamage == null || enableFailureDowngrade == null) {
            return null;
        }
        Upgrader teleportPoint;
        teleportPoint = new Upgrader(enableFailureDamage, enableFailureDowngrade, level);

        List<String> successCommand = getStringList(path + ".successCommand");
        List<String> failureCommand = getStringList(path + ".failureCommand");
        teleportPoint.setFailureCommand(failureCommand);
        teleportPoint.setSuccessCommand(successCommand);
        return teleportPoint;

    }
}
