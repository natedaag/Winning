package edu.cnm.bootcamp.natedaag.winning.entities;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.field.types.StringBytesType;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.Timestamp;

/**
 * Created by natedaag on 7/26/17.
 */

@DatabaseTable(tableName = "PICK")
public class Pick {

    @DatabaseField(columnName = "PICK_ID", generatedId = true)
    private int pickId;

    @DatabaseField(columnName = "CREATED", columnDefinition =  "TIMESTAMP DEFAULT CURRENT_TIMESTAMP",
            format = "yyyy-MM-dd HH:mm:ss", canBeNull = false, readOnly = true)
    private Timestamp created;

    @DatabaseField(columnName = "HISTORICAL", canBeNull = false)
    private boolean historical;

    @DatabaseField(columnName = "TYPE_ID", foreign = true)
    private LotteryType lotteryType;

    @ForeignCollectionField(eager = false)
    private ForeignCollection<PickValue> values;

    public Pick() {
    }

    public int getPickId() {
        return pickId;
    }

    public Timestamp getCreated() {
        return created;
    }

    public boolean isHistorical() {
        return historical;
    }

    public void setHistorical(boolean historical) {
        this.historical = historical;
    }

    public LotteryType getLotteryType() {
        return lotteryType;
    }

    public void setLotteryType(LotteryType lotteryType) {
        this.lotteryType = lotteryType;
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        for (PickValue value : values) {
            builder.append(value.getValue());
            builder.append("    ");
        }
        return builder.toString().trim();
    }

}
