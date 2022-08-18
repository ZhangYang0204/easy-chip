package pers.zhangyang.easychip.service.impl;

import pers.zhangyang.easychip.dao.WorkStationDao;
import pers.zhangyang.easychip.domain.Chip;
import pers.zhangyang.easychip.domain.Fortifier;
import pers.zhangyang.easychip.domain.Protector;
import pers.zhangyang.easychip.meta.WorkStationMeta;
import pers.zhangyang.easychip.service.CommandService;
import pers.zhangyang.easychip.yaml.ChipYaml;
import pers.zhangyang.easychip.yaml.FortifierYaml;
import pers.zhangyang.easychip.yaml.ProtectorYaml;
import pers.zhangyang.easylibrary.util.ItemStackUtil;

import java.util.List;

public class CommandServiceImpl implements CommandService {


    @Override
    public void correctDatabase() {
        List<WorkStationMeta> workStationMetaList = new WorkStationDao().list();

        for (WorkStationMeta w : workStationMetaList) {
            Chip chip = null;
            if (w.getChipItemStack() != null) {
                for (Chip c : ChipYaml.INSTANCE.listChip()) {
                    if (c.getItemStack().equals(ItemStackUtil.itemStackDeserialize(w.getChipItemStack()))) {
                        chip = c;
                    }
                }
            }
            if (chip == null) {
                w.setChipItemStack(null);
            }

            Fortifier fortifier = null;
            if (w.getChipItemStack() != null) {
                for (Fortifier c : FortifierYaml.INSTANCE.listFortifier()) {
                    if (c.getItemStack().equals(ItemStackUtil.itemStackDeserialize(w.getChipItemStack()))) {
                        fortifier = c;
                    }
                }
            }
            if (fortifier == null) {
                w.setFortifierItemStack(null);
                w.setFortifierAmount(null);
            }


            Protector protector = null;
            if (w.getChipItemStack() != null) {
                for (Protector c : ProtectorYaml.INSTANCE.listProtector()) {
                    if (c.getItemStack().equals(ItemStackUtil.itemStackDeserialize(w.getChipItemStack()))) {
                        protector = c;
                    }
                }
            }
            if (protector == null) {
                w.setProtectorAmount(null);
                w.setProtectorItemStack(null);
            }

            new WorkStationDao().deleteByOwnerUuid(w.getOwnerUuid());
            new WorkStationDao().insert(w);
        }

    }


}
