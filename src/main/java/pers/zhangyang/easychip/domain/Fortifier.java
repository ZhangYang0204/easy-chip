package pers.zhangyang.easychip.domain;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class Fortifier {
    private final String name;
    private final ItemStack itemStack;
    private Set<IntensifySetting> intensifySettingSet = new HashSet<>();

    public Fortifier(@NotNull String name, @NotNull ItemStack itemStack) {
        this.name = name;
        this.itemStack = itemStack;
    }

    @NotNull
    public String getName() {
        return name;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    @Nullable
    public Set<IntensifySetting> getIntensifySettingSet() {
        return intensifySettingSet;
    }

    public void setIntensifySettingSet(@Nullable Set<IntensifySetting> intensifySettingSet) {
        this.intensifySettingSet = intensifySettingSet;
    }
}
