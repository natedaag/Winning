package edu.cnm.bootcamp.natedaag.winning.helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.table.TableUtils;

import java.util.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.cnm.bootcamp.natedaag.winning.entities.LotteryType;
import edu.cnm.bootcamp.natedaag.winning.entities.Pick;
import edu.cnm.bootcamp.natedaag.winning.entities.PickValue;

/**
 * Created by natedaag on 7/27/17.
 */

public class OrmHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "winningDb.db";
    private static final int DATABASE_VERSION = 1;

    private Dao<Pick, Integer> pickDao = null;
    private Dao<PickValue, Integer> pickValueDao = null;
    private Dao<LotteryType, Integer> lotteryTypeDao = null;

    private static ConnectionSource source;

    public OrmHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);


    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {

        try {
            Log.i(OrmHelper.class.getName(), "onCreate");
            source = connectionSource;
            TableUtils.createTable(connectionSource, Pick.class);
            TableUtils.createTable(connectionSource, PickValue.class);
            TableUtils.createTable(connectionSource, LotteryType.class);
            populateDb();
        } catch (Exception e) {
            Log.e(OrmHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }

    private void populateDb() throws SQLException {
        LotteryType type = new LotteryType();
        type.setName("RoadRunner");
        type.setSizeOne(37);
        type.setDrawOne(5);
        getLotteryTypeDao().create(type);

        Pick pick = new Pick();
        pick.setLotteryType(type);
        pick.setPicked(new Date(2017, 0,1));
        pick.setHistorical(true);
        getPickDao().create(pick);

        PickValue value = new PickValue();
        value.setPick(pick);
        value.setValue(17);
        getPickValueDao().create(value);

        value = new PickValue();
        value.setPick(pick);
        value.setValue(35);
        getPickValueDao().create(value);

        value = new PickValue();
        value.setPick(pick);
        value.setValue(7);
        getPickValueDao().create(value);

        value = new PickValue();
        value.setPick(pick);
        value.setValue(27);
        getPickValueDao().create(value);

        value = new PickValue();
        value.setPick(pick);
        value.setValue(6);
        getPickValueDao().create(value);

        type.setName("PowerBall");
        getLotteryTypeDao().create(type);
        type.setSizeTwo(69);
        type.setDrawTwo(5);
        type.setDrawTwo(1);
        getLotteryTypeDao().create(type);

    }

    public synchronized Dao<LotteryType, Integer> getLotteryTypeDao() throws SQLException {
        if (lotteryTypeDao == null) {
            lotteryTypeDao = getDao(LotteryType.class);
        }
        return lotteryTypeDao;
    }

    public synchronized Dao<Pick, Integer> getPickDao() throws SQLException {
        if (pickDao == null) {
            pickDao = getDao(Pick.class);
        }
        return pickDao;
    }

    public synchronized Dao<PickValue, Integer> getPickValueDao() throws SQLException {
        if (pickValueDao == null) {
            pickValueDao = getDao(PickValue.class);
        }
        return pickValueDao;
    }

    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "message" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);

        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);

            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {

                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(Exception ex){
            Log.d("printing exception", ex.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }
    }

}