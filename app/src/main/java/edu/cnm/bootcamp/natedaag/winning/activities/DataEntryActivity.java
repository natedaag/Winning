package edu.cnm.bootcamp.natedaag.winning.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import edu.cnm.bootcamp.natedaag.winning.R;
import edu.cnm.bootcamp.natedaag.winning.entities.LotteryType;
import edu.cnm.bootcamp.natedaag.winning.entities.PickValue;
import edu.cnm.bootcamp.natedaag.winning.helpers.OrmHelper;

import static android.R.attr.button;

public class DataEntryActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_data_entry);

        Button button = (Button) findViewById(R.id.dataButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
    public void onClick(View v) {
// this must be done on the download of pickValues from the internet - rather than on a button click
// then, put them all into the database.
         try {
              Dao<PickValue, Integer> dao = getOrmHelper().getPickValueDao();
              PickValue value = new PickValue();
//  EditText editText = (EditText) findViewById(R.id.editText);
//  value.setName(editText.getText().toString());
              dao.create(value);
             } catch (SQLException ex) {
             ex.printStackTrace();
             }
         }
       });
    }

}

//    @Override
//    public void onClick(View v) {
// this was what was in here before. assumes a button to populate database -
//i'll want to populate it on open - those values that have changed.
//        try {
//            Dao<LotteryType, Integer> dao = getOrmHelper().getLotteryTypeDao();
//            LotteryType type = new LotteryType();
//            EditText editText = (EditText) findViewById(R.id.editText);
//            type.setName(editText.getText().toString());
//            dao.create(type);
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
//    }




//try {
//        tableDAO.updateRaw("DELETE FROM table");
//        InputStream is = getResources().openRawResource(R.raw.populate_db);
//        DataInputStream in = new DataInputStream(is);
//        BufferedReader br = new BufferedReader(new InputStreamReader(in));
//        String strLine;
//        while ((strLine = br.readLine()) != null) {
//        tableDAO.updateRaw(strLine);
//        }
//        in.close();
//        } catch (Exception e) {
//        e.printStackTrace();
//        }