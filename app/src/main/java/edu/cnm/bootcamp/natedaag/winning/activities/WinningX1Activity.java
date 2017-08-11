package edu.cnm.bootcamp.natedaag.winning.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.query.In;

import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import edu.cnm.bootcamp.natedaag.winning.R;
import edu.cnm.bootcamp.natedaag.winning.entities.LotteryType;
import edu.cnm.bootcamp.natedaag.winning.entities.Pick;
import edu.cnm.bootcamp.natedaag.winning.entities.PickValue;
import edu.cnm.bootcamp.natedaag.winning.helpers.OrmHelper;
import edu.cnm.bootcamp.natedaag.winning.helpers.PickAdapter;

public class WinningX1Activity extends AppCompatActivity {


    private static Random rng = new SecureRandom();

    private PickAdapter listAdapter = null;

    int pickSize;
    LotteryType type = null;

    private OrmHelper helper = null;

    private OrmHelper getHelper() {
        if (helper == null) {
            helper = OpenHelperManager.getHelper(this, OrmHelper.class);
        }
        return helper;
    }

    private void releaseHelper() {
        OpenHelperManager.releaseHelper();
        helper = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winning_x1);

        pickSize = getIntent().getIntExtra(MainActivity.PICKSIZE, 5);
        try {
            Dao<LotteryType, Integer> dao = getHelper().getLotteryTypeDao();
            int typeId = getIntent().getIntExtra(MainActivity.LOTTERY_TYPE_ID, 0);
            type = dao.queryForId(typeId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        loadList(generateData(type, pickSize), false);

        Button blankX1Button = (Button) findViewById(R.id.buttonX1);
        blankX1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadList(generateData(type, pickSize), true);
            }
        });

        Button clearX1Button = (Button) findViewById(R.id.buttonClear);
        clearX1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadList(new ArrayList<Pick>(), false);
            }
        });
    }

    private void loadList(List<Pick> data, boolean append) {
        if (listAdapter == null) {
            listAdapter = new PickAdapter(this, R.layout.generate_layout, data);
        } else if (!append) {
            listAdapter.clear();
            listAdapter.addAll(data);
        } else {
            listAdapter.addAll(data);
        }
        ListView listView = (ListView) findViewById(R.id.listView2);
        listView.setAdapter(listAdapter);
    }

    private List<Pick> generateData(LotteryType type, int count) {
        List<Integer> poolOne = getWeightedPool(type, (type.getSizeTwo() > 0) ? 1 : 0);
        List<Integer> poolTwo = (type.getSizeTwo() > 0) ? getWeightedPool(type, 2) : new ArrayList<Integer>();

        ArrayList<Pick> picks = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            Pick pick = new Pick();
            pick.setLotteryType(type);
            pick.setNewValues(new ArrayList<PickValue>());
            Collections.shuffle(poolOne, rng);
            List<Integer> drawOne = new ArrayList<>();
            int position = 0;
            while (drawOne.size() < type.getDrawOne()) {
                Integer draw = poolOne.get(position++);
                if (!drawOne.contains(draw)) {
                    drawOne.add(draw);
                    PickValue value = new PickValue();
                    value.setPick(pick);
                    value.setValue(draw);
                    value.setSequence((type.getDrawTwo() > 0) ? 1 : 0);
                    pick.getNewValues().add(value);
                }
            }


              Collections.shuffle(poolTwo);
            List<Integer> drawTwo = new ArrayList<>();
            position = 0;
            while (drawTwo.size() < type.getDrawTwo()) {
                Integer draw = poolTwo.get(position++);
                if (!drawTwo.contains(draw)) {
                    drawTwo.add(draw);
                    PickValue value = new PickValue();
                    value.setPick(pick);
                    value.setValue(draw);
                    value.setSequence(2);
                    pick.getNewValues().add(value);

                }
            }
            picks.add(pick);

        }

        return picks;
    }

    private List<Integer> getWeightedPool(LotteryType type, int sequence) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, - 1);
            Dao<LotteryType, Integer> typeDao = getHelper().getLotteryTypeDao();
            Dao<Pick, Integer> pickDao = getHelper().getPickDao();
            Dao<PickValue, Integer> valueDao = getHelper().getPickValueDao();
            QueryBuilder<LotteryType, Integer> typeBuilder = typeDao.queryBuilder();
            QueryBuilder<Pick, Integer> pickBuilder = pickDao.queryBuilder();
            QueryBuilder<PickValue, Integer> valueBuilder = valueDao.queryBuilder();
            typeBuilder.where().idEq(type.getId());
            pickBuilder.join(typeBuilder);
            pickBuilder.where().ge("PICKED", cal.getTime());
            valueBuilder.join(pickBuilder);
            if (sequence > 0) {
                valueBuilder.where().eq("SEQUENCE", sequence);
            }
            List<PickValue> values = valueDao.query(valueBuilder.prepare());
            ArrayList<Integer> pool = new ArrayList<>();
            int limit = (sequence > 1) ? type.getSizeTwo() : type.getSizeOne();
            for (int i = 0; i < limit; i++) {
                pool.add(i + 1);
            }
            for (PickValue v : values) {
                pool.add(v.getValue());
            }
            return pool;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }
}
