package edu.cnm.bootcamp.natedaag.winning.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

import edu.cnm.bootcamp.natedaag.winning.R;
import edu.cnm.bootcamp.natedaag.winning.entities.LotteryType;
import edu.cnm.bootcamp.natedaag.winning.helpers.OrmHelper;

public class Main2Activity extends AppCompatActivity {

    private OrmHelper ormHelper = null;

    private synchronized OrmHelper getOrmHelper() {
        if (ormHelper == null) {
            ormHelper = OpenHelperManager.getHelper(this, OrmHelper.class);
        }
        return ormHelper;
    }

    private synchronized void releaseHelper() {
        if (ormHelper != null) {
            OpenHelperManager.releaseHelper();
            ormHelper = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        try {
            Dao<LotteryType, Integer> dao = getOrmHelper().getLotteryTypeDao();
            List<LotteryType> types = dao.queryForAll();
            ArrayAdapter<LotteryType> adapter = new ArrayAdapter<>(this, R.layout.activity_list, types);
            ((ListView) findViewById(R.id.queryList)).setAdapter(adapter);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        releaseHelper();
        super.onDestroy();
    }
}