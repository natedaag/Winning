package edu.cnm.bootcamp.natedaag.winning.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.lang.reflect.Array;

import edu.cnm.bootcamp.natedaag.winning.R;
import edu.cnm.bootcamp.natedaag.winning.helpers.AndroidDatabaseManager;

public class WinningX1Activity extends AppCompatActivity {

    //added - referenced below
    private ArrayAdapter<String> listAdapter = null;

    int pickSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winning_x1);

        pickSize = getIntent().getIntExtra(MainActivity.PICKSIZE, 5);
        loadList(generateBogusData(pickSize), false);

        Button blankX1Button = (Button) findViewById(R.id.buttonX1);
        blankX1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadList(generateBogusData(pickSize), true);
             }
        });

        Button clearX1Button = (Button) findViewById(R.id.buttonClear);
        clearX1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadList(new String[] {}, false);
            }
        });
    }

    private void loadList(String[] data, boolean append) {
        if (listAdapter == null) {
            listAdapter = new ArrayAdapter<>(this, R.layout.activity_pickview);
        }
        if (!append) {
            listAdapter.clear();
        }
        listAdapter.addAll(data);
        ListView listView = (ListView) findViewById(R.id.listView2);
        listView.setAdapter(listAdapter);
    }

    private String[] generateBogusData(int count) {
        String[] results = new String[count];
        for (int i = 0; i < count; i++) {
            StringBuilder builder = new StringBuilder();
            for (int j = 0; j < 5; j++) {
                builder.append(Math.round(1 + 36 * Math.random()));
                builder.append("          ");
            }
            results[i] = builder.toString().trim();
        }
        return results;
    }

    private String[] generateBogusDataPB(int count) {
        String[] results = new String[count];
        for (int i = 0; i < count; i++) {
            StringBuilder builder = new StringBuilder();
            for (int j = 0; j < 5; j++) {
                builder.append(Math.round(1 + 69 * Math.random()));
                builder.append("          ");
            }
            results[i] = builder.toString().trim();
        }
        return results;
    }


}
