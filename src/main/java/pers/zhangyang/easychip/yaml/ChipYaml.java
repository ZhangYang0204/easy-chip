package pers.zhangyang.easychip.yaml;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.zhangyang.easychip.domain.Chip;
import pers.zhangyang.easylibrary.base.YamlBase;

import java.util.*;

public class ChipYaml extends YamlBase {
    public static final ChipYaml INSTANCE = new ChipYaml();

    private ChipYaml() {
        super("chip.yml");
    }

    @NotNull
    public Set<Chip> listChip() {
        Set<Chip> teleportPointList = new HashSet<>();

        ConfigurationSection configurationSection = yamlConfiguration.getConfigurationSection("chip");
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

            ItemStack itemStack = getItemStack("chip." + s + ".itemStack");
            if (itemStack == null) {
                continue;
            }
            Map<AttributeModifier, Attribute> attributeModifierAttributeMap = new HashMap<>();

            List<String> lore = getStringList("chip." + s + ".additionalLore");
            List<String> stringList = getStringList("chip." + s + ".effect");
            if (stringList != null) {
                for (String ss : stringList) {
                    String[] args = ss.split(":");
                    if (args.length != 5 && args.length != 6) {
                        continue;
                    }
                    Attribute attribute;
                    try {
                        attribute = Attribute.valueOf(args[0]);
                    } catch (IllegalArgumentException e) {
                        continue;
                    }
                    UUID uuid;
                    try {
                        uuid = UUID.fromString(args[1]);
                    } catch (IllegalArgumentException e) {
                        continue;
                    }

                    double value;
                    try {
                        value = Double.parseDouble(args[3]);
                    } catch (NumberFormatException e) {
                        continue;
                    }
                    AttributeModifier.Operation operation;
                    try {
                        operation = AttributeModifier.Operation.valueOf(args[4]);
                    } catch (IllegalArgumentException e) {
                        continue;
                    }

                    if (args.length == 5) {
                        attributeModifierAttributeMap.put(new AttributeModifier(uuid, args[2], value, operation), attribute);
                    }
                    if (args.length == 6) {
                        EquipmentSlot equipmentSlot;
                        try {
                            equipmentSlot = EquipmentSlot.valueOf(args[5]);
                        } catch (IllegalArgumentException e) {
                            continue;
                        }
                        attributeModifierAttributeMap.put(new AttributeModifier(uuid, args[2], value, operation, equipmentSlot),
                                attribute);

                    }


                }
            }


            Chip teleportPoint = new Chip(level, itemStack);
            teleportPoint.setAttributeModifierAttributeMap(attributeModifierAttributeMap);
            teleportPoint.setAdditionalLore(lore);
            teleportPointList.add(teleportPoint);

        }
        return teleportPointList;

    }

    @Nullable
    public Chip getChip(String path) {

        ItemStack itemStack = getItemStack(path + ".itemStack");
        if (itemStack == null) {
            return null;
        }

        List<String> lore = getStringList(path + ".additionalLore");


        Map<AttributeModifier, Attribute> attributeModifierAttributeMap = new HashMap<>();

        List<String> stringList = getStringList(path + ".effect");
        if (stringList != null) {
            for (String ss : stringList) {
                String[] args = ss.split(":");
                if (args.length != 5 && args.length != 6) {
                    continue;
                }
                Attribute attribute;
                try {

                    attribute = Attribute.valueOf(args[0]);
                } catch (IllegalArgumentException e) {
                    continue;
                }
                UUID uuid;
                try {
                    uuid = UUID.fromString(args[1]);
                } catch (IllegalArgumentException e) {
                    continue;
                }

                double value;
                try {
                    value = Double.parseDouble(args[3]);
                } catch (NumberFormatException e) {
                    continue;
                }
                AttributeModifier.Operation operation;
                try {
                    operation = AttributeModifier.Operation.valueOf(args[4]);
                } catch (IllegalArgumentException e) {
                    continue;
                }

                if (args.length == 5) {
                    attributeModifierAttributeMap.put(new AttributeModifier(uuid, args[2], value, operation), attribute);
                }
                if (args.length == 6) {
                    EquipmentSlot equipmentSlot;
                    try {
                        equipmentSlot = EquipmentSlot.valueOf(args[5]);
                    } catch (IllegalArgumentException e) {
                        continue;
                    }
                    attributeModifierAttributeMap.put(new AttributeModifier(uuid, args[2], value, operation, equipmentSlot),
                            attribute);

                }


            }
        }

        int level = Integer.parseInt(path.split("\\.")[path.split("\\.").length - 1]);

        Chip chip = new Chip(level, itemStack);
        chip.setAttributeModifierAttributeMap(attributeModifierAttributeMap);
        chip.setAdditionalLore(lore);
        return chip;

    }

}