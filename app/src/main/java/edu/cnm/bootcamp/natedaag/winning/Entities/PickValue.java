package edu.cnm.bootcamp.natedaag.winning.Entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import edu.cnm.bootcamp.natedaag.winning.Entities.Pick;

/**
 * Created by natedaag on 7/26/17.
 */

@DatabaseTable(tableName = "PICKVALUE")
public class PickValue {

    @DatabaseField(columnName = "VALUE_ID", generatedId = true)
    private int valueId;

    @DatabaseField(columnName = "PICK_ID", foreign = true)
    private Pick pick;

    @DatabaseField(columnName = "VALUE")
    private int value;

    @DatabaseField(columnName = "SEQUENCE")
    private int sequence;

    public PickValue() {
    }

    public int getValueId() {
        return valueId;
    }

    public Pick getPick() {
        return pick;
    }

    public void setPick(Pick pick) {
        this.pick = pick;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }


}
