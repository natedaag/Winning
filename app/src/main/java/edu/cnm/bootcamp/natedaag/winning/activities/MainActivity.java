
package edu.cnm.bootcamp.natedaag.winning.activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.net.MalformedURLException;
import java.net.URL;

import edu.cnm.bootcamp.natedaag.winning.R;
import edu.cnm.bootcamp.natedaag.winning.helpers.AndroidDatabaseManager;
import edu.cnm.bootcamp.natedaag.winning.helpers.OrmHelper;

public class MainActivity extends AppCompatActivity {

    public static final String PICKSIZE = "edu.cnm.bootcamp.natedaag.winning.activities.picksize";

    String[] testArray = {"a", "b", "c", "d", "e"};

    private ArrayAdapter<String> listAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OrmHelper ormHelper = OpenHelperManager.getHelper(this, OrmHelper.class);
        SQLiteDatabase db = ormHelper.getWritableDatabase();
        db.close();
        setContentView(R.layout.activity_main);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.activity_pickview, testArray);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);


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
                        .putExtra(PICKSIZE, 1);
                startActivity(testIntent3);
            }
        });

        Button winningX5Button = (Button)findViewById(R.id.buttonX5);
        winningX5Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v3) {
                Intent testIntent3 = new Intent(MainActivity.this, WinningX1Activity.class)
                        .putExtra(PICKSIZE, 5);
                startActivity(testIntent3);
            }
        });

//        Button listButton = (Button)findViewById(R.id.listButton);
//        listButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v3) {
//                Intent testIntent6 = new Intent(MainActivity.this, Main2Activity.class);
//                startActivity(testIntent6);
//            }
//        });

    }


}
//ForeignCollection
