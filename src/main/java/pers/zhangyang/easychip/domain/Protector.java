package pers.zhangyang.easychip.domain;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class Protector {
    private final String name;
    private final ItemStack itemStack;
    private Set<ProtectSetting> protectSettingSet = new HashSet<>();

    public Protector(@NotNull String name, @NotNull ItemStack itemStack) {
        this.name = name;
        this.itemStack = itemStack;
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public ItemStack getItemStack() {
        return itemStack;
    }

    @Nullable
    public Set<ProtectSetting> getProtectSettingSet() {
        return protectSettingSet;
    }

    public void setProtectSettingSet(@Nullable Set<ProtectSetting> protectSettingSet) {
        this.protectSettingSet = protectSettingSet;
    }
}
