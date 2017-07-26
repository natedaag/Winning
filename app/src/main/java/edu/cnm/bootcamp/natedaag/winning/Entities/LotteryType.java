package edu.cnm.bootcamp.natedaag.winning.Entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by natedaag on 7/26/17.
 */

@DatabaseTable(tableName = "LOTTERYTYPE")
public class LotteryType {

    @DatabaseField(columnName = "TYPE_ID", generatedId = true)
    private int type_id;

    @DatabaseField(columnName = "NAME", width = 100) // canBeNull = default - don't need to write
    private String name;

    @DatabaseField(canBeNull = false, columnName = "SIZE_ONE")
    private int sizeOne;

    @DatabaseField(canBeNull = false, columnName = "DRAW_ONE")
    private int drawOne;

    @DatabaseField(columnName = "SIZE_TWO")
    private int sizeTwo;

    @DatabaseField(columnName = "DRAW_TWO")
    private int drawTwo;

    public LotteryType() {
    }


    public int getType_id() {
        return type_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSizeOne() {
        return sizeOne;
    }

    public void setSizeOne(int sizeOne) {
        this.sizeOne = sizeOne;
    }

    public int getDrawOne() {
        return drawOne;
    }

    public void setDrawOne(int drawOne) {
        this.drawOne = drawOne;
    }

    public int getSizeTwo() {
        return sizeTwo;
    }

    public void setSizeTwo(int sizeTwo) {
        this.sizeTwo = sizeTwo;
    }

    public int getDrawTwo() {
        return drawTwo;
    }

    public void setDrawTwo(int drawTwo) {
        this.drawTwo = drawTwo;
    }


}
