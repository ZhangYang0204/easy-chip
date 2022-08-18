package pers.zhangyang.easychip.meta;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WorkStationMeta {
    private String ownerUuid;
    private String fortifierItemStack;
    private Integer fortifierAmount;
    private String protectorItemStack;
    private Integer protectorAmount;
    private String chipItemStack;
    private String itemStackItemStack;

    public WorkStationMeta() {
    }

    public WorkStationMeta(@NotNull String ownerUuid) {
        this.ownerUuid = ownerUuid;
    }

    @NotNull
    public String getOwnerUuid() {
        return ownerUuid;
    }

    @Nullable
    public String getFortifierItemStack() {
        return fortifierItemStack;
    }

    public void setFortifierItemStack(@Nullable String fortifierItemStack) {
        this.fortifierItemStack = fortifierItemStack;
    }

    @Nullable
    public String getProtectorItemStack() {
        return protectorItemStack;
    }

    public void setProtectorItemStack(@Nullable String protectorItemStack) {
        this.protectorItemStack = protectorItemStack;
    }

    @Nullable
    public String getChipItemStack() {
        return chipItemStack;
    }

    public void setChipItemStack(@Nullable String chipItemStack) {
        this.chipItemStack = chipItemStack;
    }

    @Nullable
    public Integer getFortifierAmount() {
        return fortifierAmount;
    }

    public void setFortifierAmount(@Nullable Integer fortifierAmount) {
        this.fortifierAmount = fortifierAmount;
    }

    @Nullable
    public Integer getProtectorAmount() {
        return protectorAmount;
    }

    public void setProtectorAmount(@Nullable Integer protectorAmount) {
        this.protectorAmount = protectorAmount;
    }

    @Nullable
    public String getItemStackItemStack() {
        return itemStackItemStack;
    }

    public void setItemStackItemStack(@Nullable String itemStackItemStack) {
        this.itemStackItemStack = itemStackItemStack;
    }

}
