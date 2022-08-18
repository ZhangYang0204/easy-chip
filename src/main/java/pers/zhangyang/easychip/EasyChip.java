package pers.zhangyang.easychip;

import org.bstats.bukkit.Metrics;
import pers.zhangyang.easylibrary.EasyPlugin;

public class EasyChip extends EasyPlugin {
    @Override
    public void onOpen() {
        new Metrics(this, 16156);
    }

    @Override
    public void onClose() {

    }
}
