package edu.cnm.bootcamp.natedaag.winning.entities;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.field.types.StringBytesType;
import com.j256.ormlite.table.DatabaseTable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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

    @DatabaseField(columnName = "PICKED", format = "yyyy-MM-dd", canBeNull = false, uniqueCombo = true)
    private Date picked;

    @DatabaseField(columnName = "HISTORICAL", canBeNull = false)
    private boolean historical;

    @DatabaseField(columnName = "TYPE_ID", foreign = true, uniqueCombo = true, foreignAutoRefresh = true)
    private LotteryType lotteryType;

    @ForeignCollectionField(eager = false, orderColumnName = "VALUE")
    private ForeignCollection<PickValue> values;

    // TODO Add unique constraint for LotteryType id and Picked Date.
    public Pick() {
    }

    public int getPickId() {
        return pickId;
    }

    public Timestamp getCreated() {
        return created;
    }

    public Date getPicked() {
        return picked;
    }

    public void setPicked(Date picked) {
        this.picked = picked;
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

    public ForeignCollection<PickValue> getValues() {
        return values;
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        DateFormat format = new SimpleDateFormat("MM/dd/yy");
        builder.append(format.format(getPicked()));
        builder.append("     ");
        if (getValues() != null) {
            ArrayList<PickValue> sorted = new ArrayList<>(getValues());
            Collections.sort(sorted);
            for (PickValue value : sorted) {
                builder.append(value.getValue());
                builder.append("    ");
            }
        }
        return builder.toString().trim();
    }

}
