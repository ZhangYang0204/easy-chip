package pers.zhangyang.easychip.domain;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class Fortifier {
    private final String name;
    private final ItemStack itemStack;
    private Set<FortifySettingSetting> fortifySettingSettingSet = new HashSet<>();

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
    public Set<FortifySettingSetting> getIntensifySettingSet() {
        return fortifySettingSettingSet;
    }

    public void setIntensifySettingSet(@Nullable Set<FortifySettingSetting> fortifySettingSettingSet) {
        this.fortifySettingSettingSet = fortifySettingSettingSet;
    }
}
