package pers.zhangyang.easychip.dao;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.zhangyang.easychip.meta.WorkStationMeta;
import pers.zhangyang.easylibrary.base.DaoBase;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class WorkStationDao extends DaoBase {
    @Override
    public int init() {
        try {
            PreparedStatement ps = getConnection().prepareStatement("" +
                    "CREATE TABLE IF NOT EXISTS work_station(" +
                    "owner_uuid TEXT," +
                    "fortifier_item_stack TEXT," +
                    "fortifier_amount INT," +
                    "protector_item_stack TEXT," +
                    "protector_amount INT," +
                    "chip_item_stack INT ," +
                    "item_stack_item_stack TEXT" +
                    ")");
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int insert(WorkStationMeta residenceBlockMeta) {
        PreparedStatement ps = null;
        try {
            ps = getConnection().prepareStatement("" +
                    "INSERT INTO work_station (owner_uuid,fortifier_item_stack,fortifier_amount,protector_item_stack,protector_amount,chip_item_stack," +
                    "item_stack_item_stack)" +
                    "VALUES (?,?,?,?,?,?,?)");
            ps.setString(1, residenceBlockMeta.getOwnerUuid());
            ps.setString(2, residenceBlockMeta.getFortifierItemStack());
            ps.setObject(3, residenceBlockMeta.getFortifierAmount());
            ps.setString(4, residenceBlockMeta.getProtectorItemStack());
            ps.setObject(5, residenceBlockMeta.getProtectorAmount());
            ps.setString(6, residenceBlockMeta.getChipItemStack());
            ps.setString(7, residenceBlockMeta.getItemStackItemStack());
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Nullable
    public WorkStationMeta getByOwnerUuid(String ownerUuid) {
        PreparedStatement ps = null;
        try {
            ps = getConnection().prepareStatement("SELECT * FROM work_station WHERE owner_uuid=? ");
            ps.setString(1, ownerUuid);
            return singleTransform(ps.executeQuery(), WorkStationMeta.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public List<WorkStationMeta> list() {
        PreparedStatement ps = null;
        try {
            ps = getConnection().prepareStatement("SELECT * FROM work_station");
            return multipleTransform(ps.executeQuery(), WorkStationMeta.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int deleteByOwnerUuid(String ownerUuid) {
        PreparedStatement ps = null;
        try {
            ps = getConnection().prepareStatement("DELETE FROM work_station WHERE owner_uuid=? ");
            ps.setString(1, ownerUuid);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
