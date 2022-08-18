package pers.zhangyang.easychip.domain;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Upgrader {
    private final int level;
    private final boolean enableFailureDamage;
    private final boolean enableFailureDowngrade;
    private List<String> successCommand;
    private List<String> failureCommand;

    public Upgrader(@NotNull boolean enableFailureDamage, boolean enableFailureDowngrade, int level) {
        this.level = level;
        this.enableFailureDamage = enableFailureDamage;
        this.enableFailureDowngrade = enableFailureDowngrade;
    }


    @Nullable
    public List<String> getSuccessCommand() {
        return successCommand;
    }

    public void setSuccessCommand(@Nullable List<String> successCommand) {
        this.successCommand = successCommand;
    }

    @Nullable
    public List<String> getFailureCommand() {
        return failureCommand;
    }

    public void setFailureCommand(@Nullable List<String> failureCommand) {
        this.failureCommand = failureCommand;
    }

    public int getLevel() {
        return level;
    }

    public boolean getEnableFailureDamage() {
        return enableFailureDamage;
    }

    public boolean getEnableFailureDowngrade() {
        return enableFailureDowngrade;
    }
}
