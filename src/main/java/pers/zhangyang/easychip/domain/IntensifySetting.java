package pers.zhangyang.easychip.domain;

import java.util.Objects;

public class IntensifySetting {
    private final int level;
    private final int slotSize;
    private final double probability;

    public IntensifySetting(int level, int slotSize, double probability) {
        this.level = level;
        this.slotSize = slotSize;
        this.probability = probability;
    }

    public int getLevel() {
        return level;
    }

    public int getSlotSize() {
        return slotSize;
    }

    public double getProbability() {
        return probability;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntensifySetting that = (IntensifySetting) o;
        return level == that.level && slotSize == that.slotSize && Double.compare(that.probability, probability) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(level, slotSize, probability);
    }
}
