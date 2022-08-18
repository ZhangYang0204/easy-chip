package pers.zhangyang.easychip.service.impl;

import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easychip.dao.WorkStationDao;
import pers.zhangyang.easychip.domain.Chip;
import pers.zhangyang.easychip.domain.Fortifier;
import pers.zhangyang.easychip.domain.Protector;
import pers.zhangyang.easychip.exception.*;
import pers.zhangyang.easychip.meta.WorkStationMeta;
import pers.zhangyang.easychip.service.GuiService;
import pers.zhangyang.easychip.yaml.ChipYaml;
import pers.zhangyang.easychip.yaml.FortifierYaml;
import pers.zhangyang.easychip.yaml.ProtectorYaml;
import pers.zhangyang.easylibrary.util.ItemStackUtil;

public class GuiServiceImpl implements GuiService {

    @Override
    public void depositFortifier(String playerUuid, String name, int amount) throws DuplicateFortifierException, NotExistFortifierException {

        Fortifier protector = null;
        for (Fortifier c : FortifierYaml.INSTANCE.listFortifier()) {
            if (c.getItemStack().equals(ItemStackUtil.itemStackDeserialize(name))) {
                protector = c;
                break;
            }
        }
        if (protector == null) {
            throw new NotExistFortifierException();
        }

        WorkStationMeta workStationMeta = new WorkStationDao().getByOwnerUuid(playerUuid);
        if (workStationMeta == null) {
            workStationMeta = new WorkStationMeta(playerUuid);
        }
        if (workStationMeta.getFortifierItemStack() != null && !workStationMeta.getFortifierItemStack().equals(name)) {
            throw new DuplicateFortifierException();
        }
        if (workStationMeta.getFortifierItemStack() == null) {
            workStationMeta.setFortifierItemStack(name);
        }
        if (workStationMeta.getFortifierAmount() == null) {
            workStationMeta.setFortifierAmount(amount);
        } else {
            workStationMeta.setFortifierAmount(workStationMeta.getFortifierAmount() + amount);
        }
        new WorkStationDao().deleteByOwnerUuid(playerUuid);
        new WorkStationDao().insert(workStationMeta);

    }

    @Override
    public void takeFortifier(String playerUuid, int amount) throws NotExistFortifierException, NotMoreFortifierException {
        WorkStationMeta workStationMeta = new WorkStationDao().getByOwnerUuid(playerUuid);
        if (workStationMeta == null) {
            workStationMeta = new WorkStationMeta(playerUuid);
        }
        if (workStationMeta.getFortifierItemStack() == null) {
            throw new NotExistFortifierException();
        }
        assert workStationMeta.getFortifierAmount() != null;


        Fortifier protector = null;
        for (Fortifier c : FortifierYaml.INSTANCE.listFortifier()) {
            if (c.getItemStack().equals(ItemStackUtil.itemStackDeserialize(workStationMeta.getFortifierItemStack()))) {
                protector = c;
                break;
            }
        }
        if (protector == null) {
            throw new NotExistFortifierException();
        }

        if (workStationMeta.getFortifierAmount() < amount) {
            throw new NotMoreFortifierException();
        }

        workStationMeta.setFortifierAmount(workStationMeta.getFortifierAmount() - amount);
        if (workStationMeta.getFortifierAmount() == 0) {
            workStationMeta.setFortifierItemStack(null);
            workStationMeta.setFortifierAmount(null);
        }
        new WorkStationDao().deleteByOwnerUuid(playerUuid);
        new WorkStationDao().insert(workStationMeta);
    }

    @Override
    public void depositProtector(String playerUuid, String iData, int amount) throws DuplicateProtectorException, NotExistProtectorException {
        Protector protector = null;
        for (Protector c : ProtectorYaml.INSTANCE.listProtector()) {
            if (c.getItemStack().equals(ItemStackUtil.itemStackDeserialize(iData))) {
                protector = c;
                break;
            }
        }
        if (protector == null) {
            throw new NotExistProtectorException();
        }

        WorkStationMeta workStationMeta = new WorkStationDao().getByOwnerUuid(playerUuid);
        if (workStationMeta == null) {
            workStationMeta = new WorkStationMeta(playerUuid);
        }
        if (workStationMeta.getProtectorItemStack() != null && !workStationMeta.getProtectorItemStack().equals(iData)) {
            throw new DuplicateProtectorException();
        }
        if (workStationMeta.getProtectorItemStack() == null) {
            workStationMeta.setProtectorItemStack(iData);
        }
        if (workStationMeta.getProtectorAmount() == null) {
            workStationMeta.setProtectorAmount(amount);
        } else {
            workStationMeta.setProtectorAmount(workStationMeta.getProtectorAmount() + amount);
        }
        new WorkStationDao().deleteByOwnerUuid(playerUuid);
        new WorkStationDao().insert(workStationMeta);
    }

    @Override
    public void takeProtector(String playerUuid, int amount) throws NotExistProtectorException, NotMoreProtectorException {

        WorkStationMeta workStationMeta = new WorkStationDao().getByOwnerUuid(playerUuid);
        if (workStationMeta == null) {
            workStationMeta = new WorkStationMeta(playerUuid);
        }
        if (workStationMeta.getProtectorItemStack() == null) {
            throw new NotExistProtectorException();
        }
        assert workStationMeta.getProtectorAmount() != null;


        Protector protector = null;
        for (Protector c : ProtectorYaml.INSTANCE.listProtector()) {
            if (c.getItemStack().equals(ItemStackUtil.itemStackDeserialize(workStationMeta.getProtectorItemStack()))) {
                protector = c;
                break;
            }
        }
        if (protector == null) {
            throw new NotExistProtectorException();
        }

        if (workStationMeta.getProtectorAmount() < amount) {
            throw new NotMoreProtectorException();
        }
        workStationMeta.setProtectorAmount(workStationMeta.getProtectorAmount() - amount);
        if (workStationMeta.getProtectorAmount() == 0) {
            workStationMeta.setProtectorAmount(null);
            workStationMeta.setProtectorItemStack(null);
        }
        new WorkStationDao().deleteByOwnerUuid(playerUuid);
        new WorkStationDao().insert(workStationMeta);
    }

    @Override
    public void setChip(String playerUuid, String itemStackData) throws NotExistChipException {

        Chip chip = null;
        for (Chip c : ChipYaml.INSTANCE.listChip()) {
            if (c.getItemStack().equals(ItemStackUtil.itemStackDeserialize(itemStackData))) {
                chip = c;
                break;
            }
        }
        if (chip == null) {
            throw new NotExistChipException();
        }


        WorkStationMeta workStationMeta = new WorkStationDao().getByOwnerUuid(playerUuid);
        if (workStationMeta == null) {
            workStationMeta = new WorkStationMeta(playerUuid);
        }
        workStationMeta.setChipItemStack(itemStackData);
        new WorkStationDao().deleteByOwnerUuid(playerUuid);
        new WorkStationDao().insert(workStationMeta);
    }

    @Override
    public void depositChip(String playerUuid, String itemStackData) throws DuplicateChipException, NotExistChipException {

        Chip chip = null;
        for (Chip c : ChipYaml.INSTANCE.listChip()) {
            if (c.getItemStack().equals(ItemStackUtil.itemStackDeserialize(itemStackData))) {
                chip = c;
                break;
            }
        }
        if (chip == null) {
            throw new NotExistChipException();
        }
        WorkStationMeta workStationMeta = new WorkStationDao().getByOwnerUuid(playerUuid);
        if (workStationMeta == null) {
            workStationMeta = new WorkStationMeta(playerUuid);
        }
        if (workStationMeta.getChipItemStack() != null) {
            throw new DuplicateChipException();
        }
        workStationMeta.setChipItemStack(itemStackData);

        new WorkStationDao().deleteByOwnerUuid(playerUuid);
        new WorkStationDao().insert(workStationMeta);
    }

    @Override
    public void takeChip(String playerUuid) throws NotExistChipException {


        WorkStationMeta workStationMeta = new WorkStationDao().getByOwnerUuid(playerUuid);
        if (workStationMeta == null) {
            workStationMeta = new WorkStationMeta(playerUuid);
        }
        if (workStationMeta.getChipItemStack() == null) {
            throw new NotExistChipException();
        }
        String itemStackData = workStationMeta.getChipItemStack();
        Chip chip = null;
        for (Chip c : ChipYaml.INSTANCE.listChip()) {
            if (c.getItemStack().equals(ItemStackUtil.itemStackDeserialize(itemStackData))) {
                chip = c;
                break;
            }
        }
        if (chip == null) {
            throw new NotExistChipException();
        }

        workStationMeta.setChipItemStack(null);

        new WorkStationDao().deleteByOwnerUuid(playerUuid);
        new WorkStationDao().insert(workStationMeta);
    }

    @Override
    public void depositItemStack(String playerUuid, String itemStack) throws DuplicateItemStackException {
        WorkStationMeta workStationMeta = new WorkStationDao().getByOwnerUuid(playerUuid);
        if (workStationMeta == null) {
            workStationMeta = new WorkStationMeta(playerUuid);
        }
        if (workStationMeta.getItemStackItemStack() != null) {
            throw new DuplicateItemStackException();
        }
        workStationMeta.setItemStackItemStack(itemStack);

        new WorkStationDao().deleteByOwnerUuid(playerUuid);
        new WorkStationDao().insert(workStationMeta);
    }

    @Override
    public void takeItemStack(String playerUuid) throws NotExistItemStackException {
        WorkStationMeta workStationMeta = new WorkStationDao().getByOwnerUuid(playerUuid);
        if (workStationMeta == null) {
            workStationMeta = new WorkStationMeta(playerUuid);
        }
        if (workStationMeta.getItemStackItemStack() == null) {
            throw new NotExistItemStackException();
        }
        workStationMeta.setItemStackItemStack(null);

        new WorkStationDao().deleteByOwnerUuid(playerUuid);
        new WorkStationDao().insert(workStationMeta);
    }

    @NotNull
    @Override
    public WorkStationMeta getWorkStation(String playerUuid) {

        WorkStationMeta workStationMeta = new WorkStationDao().getByOwnerUuid(playerUuid);
        if (workStationMeta == null) {
            workStationMeta = new WorkStationMeta(playerUuid);
        }
        return workStationMeta;
    }
}
