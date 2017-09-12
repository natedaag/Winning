
package edu.cnm.bootcamp.natedaag.winning.activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;

import edu.cnm.bootcamp.natedaag.winning.R;
import edu.cnm.bootcamp.natedaag.winning.entities.LotteryType;
import edu.cnm.bootcamp.natedaag.winning.helpers.AndroidDatabaseManager;
import edu.cnm.bootcamp.natedaag.winning.helpers.OrmHelper;
import edu.cnm.bootcamp.natedaag.winning.helpers.PickAdapter;

public class MainActivity extends AppCompatActivity {

    private LotteryType type = null;

    public static final String LOTTERY_TYPE_ID = "edu.cnm.bootcamp.natedaag.winning.entities.LotteryType.id";
    public static final String PICKSIZE = "edu.cnm.bootcamp.natedaag.winning.activities.MainActivity.pickSize";

    private ArrayAdapter<String> listAdapter = null;

    private OrmHelper helper = null;

    public OrmHelper getHelper() {
        Log.d("database debug", "getHelper: " + helper);
        if (helper == null) {
            helper = OpenHelperManager.getHelper(this, OrmHelper.class);
        }
        return helper;
    }

    public void releaseHelper() {
        Log.d("database debug", "releaseHelper: " + helper);
        if (helper != null) {
            OpenHelperManager.releaseHelper();
            helper = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("database debug", "onCreate");
        getHelper().getWritableDatabase().close();
        setContentView(R.layout.activity_main);

//    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.activity_pickview, testArray);
//    listView.setAdapter(adapter);
        ListView listView = (ListView) findViewById(R.id.listView);

        try {
            Dao<LotteryType, Integer> dao = getHelper().getLotteryTypeDao();
            QueryBuilder<LotteryType, Integer> builder = dao.queryBuilder();
            builder.where().eq("NAME", "RoadRunner");
            type = dao.queryForFirst(builder.prepare());

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        new FetchNumbersRR(this, listView).execute();

        Button databaseButton = (Button)findViewById(R.id.databaseButton);
        databaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v3) {
                Intent testIntent3 = new Intent(MainActivity.this, AndroidDatabaseManager.class);
                startActivity(testIntent3);
            }
        });

        Button winningX1Button = (Button)findViewById(R.id.buttonX1);
        winningX1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v3) {
                Intent testIntent3 = new Intent(MainActivity.this, WinningX1Activity.class)
                        .putExtra(PICKSIZE, 1)
                        .putExtra(LOTTERY_TYPE_ID, type.getId());
                startActivity(testIntent3);
            }
        });

        Button winningX5Button = (Button)findViewById(R.id.buttonX5);
        winningX5Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v3) {
                Intent testIntent3 = new Intent(MainActivity.this, WinningX1Activity.class)
                        .putExtra(PICKSIZE, 5)
                        .putExtra(LOTTERY_TYPE_ID, type.getId());
                startActivity(testIntent3);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getHelper();
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseHelper();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.lottery_type_menu, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.lottery_type_id:
                ListView listView = (ListView) findViewById(R.id.listView);
                new FetchNumbersPB(this, listView).execute();
                try {
                    Dao<LotteryType, Integer> dao = getHelper().getLotteryTypeDao();
                    QueryBuilder<LotteryType, Integer> builder = dao.queryBuilder();
                    builder.where().eq("NAME", item.getTitle());
                    type = dao.queryForFirst(builder.prepare());

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                return true;

            case R.id.lottery_type_id2:
                ListView listView2 = (ListView) findViewById(R.id.listView);
                new FetchNumbersRR(this, listView2).execute();
                try {
                    Dao<LotteryType, Integer> dao = getHelper().getLotteryTypeDao();
                    QueryBuilder<LotteryType, Integer> builder = dao.queryBuilder();
                    builder.where().eq("NAME", item.getTitle());
                    type = dao.queryForFirst(builder.prepare());

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                return true;
// added for MM
            case R.id.lottery_type_id3:
                ListView listView3 = (ListView) findViewById(R.id.listView);
                new FetchNumbersMM(this, listView3).execute();
                try {
                    Dao<LotteryType, Integer> dao = getHelper().getLotteryTypeDao();
                    QueryBuilder<LotteryType, Integer> builder = dao.queryBuilder();
                    builder.where().eq("NAME", item.getTitle());
                    type = dao.queryForFirst(builder.prepare());

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
//ForeignCollection
