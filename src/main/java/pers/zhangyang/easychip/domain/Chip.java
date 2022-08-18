package pers.zhangyang.easychip.domain;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Chip {

    private final int level;
    private final ItemStack itemStack;
    private Map<AttributeModifier, Attribute> attributeModifierAttributeMap = new HashMap<>();
    private List<String> additionalLore;


    public Chip(int level, @NotNull ItemStack itemStack) {
        this.level = level;
        this.itemStack = itemStack;
    }


    @Nullable
    public List<String> getAdditionalLore() {
        return additionalLore;
    }

    public void setAdditionalLore(@Nullable List<String> additionalLore) {
        this.additionalLore = additionalLore;
    }

    public int getLevel() {
        return level;
    }

    @NotNull
    public ItemStack getItemStack() {
        return itemStack;
    }

    @NotNull
    public Map<AttributeModifier, Attribute> getAttributeModifierAttributeMap() {
        return attributeModifierAttributeMap;
    }

    public void setAttributeModifierAttributeMap(@Nullable Map<AttributeModifier, Attribute> attributeModifierAttributeMap) {
        this.attributeModifierAttributeMap = attributeModifierAttributeMap;
    }
}
