package pers.zhangyang.easychip.service;

import org.jetbrains.annotations.NotNull;
import pers.zhangyang.easychip.exception.*;
import pers.zhangyang.easychip.meta.WorkStationMeta;

public interface GuiService {


    void depositFortifier(String playerUuid, String itemStackData, int amount) throws DuplicateFortifierException, NotExistFortifierException;

    void takeFortifier(String playerUuid, int amount) throws NotExistFortifierException, NotMoreFortifierException;


    void depositProtector(String playerUuid, String itemStackData, int amount) throws DuplicateProtectorException, NotExistProtectorException;

    void takeProtector(String playerUuid, int amount) throws NotExistProtectorException, NotMoreProtectorException;


    void setChip(String playerUuid, String itemStackData) throws NotExistChipException;

    void depositChip(String playerUuid, String itemStackData) throws DuplicateChipException, NotExistChipException;

    void takeChip(String playerUuid) throws NotExistChipException;

    void depositItemStack(String playerUuid, String ItemStack) throws DuplicateItemStackException;

    void takeItemStack(String playerUuid) throws NotExistItemStackException;

    @NotNull
    WorkStationMeta getWorkStation(String playerUuid);


}

