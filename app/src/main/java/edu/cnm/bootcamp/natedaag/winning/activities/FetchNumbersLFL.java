package edu.cnm.bootcamp.natedaag.winning.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import edu.cnm.bootcamp.natedaag.winning.R;
import edu.cnm.bootcamp.natedaag.winning.entities.LotteryType;
import edu.cnm.bootcamp.natedaag.winning.entities.Pick;
import edu.cnm.bootcamp.natedaag.winning.entities.PickValue;
import edu.cnm.bootcamp.natedaag.winning.helpers.OrmHelper;
import edu.cnm.bootcamp.natedaag.winning.helpers.PickAdapter;

/**
 * Created by natedaag on 8/2/17.
 */

public class FetchNumbersLFL extends AsyncTask<Void, Void, List<Pick>> {

    private static final int BUFFER_SIZE = 4096;
    private static final int DATE_COLUMN = 0;
    private static final int FIRST_PICK_VALUE_COLUMN = 1;
    private static final int MAX_DAYS = 35;

    private Context context;

    private ListView listView;

    public FetchNumbersLFL(Context context, ListView listView) {
        super();
        this.context = context;
        this.listView = listView;
    }

    @Override
    protected List<Pick> doInBackground(Void... params) {
        List<String> rows = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(context.getString(R.string.luckyforlife_url));
            connection = (HttpURLConnection) url.openConnection();
            InputStream input = connection.getInputStream();
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage());
            }
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = 0;
            while ((bytesRead = input.read(buffer)) > 0) {
                output.write(buffer, 0, bytesRead);
            }
            output.close();
            updatePicks(output.toByteArray());
            return getPicks();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            connection.disconnect();
        }
    }

    @Override
    protected void onPostExecute(List<Pick> picks) {
        PickAdapter adapter = new PickAdapter(context, R.layout.pick_layout, picks);
        listView.setAdapter(adapter);
    }


    private void updatePicks(byte[] data) throws IOException {
        //       List<String>  strings = new ArrayList<>();
        try (
                ByteArrayInputStream stream = new ByteArrayInputStream(data);
                InputStreamReader input = new InputStreamReader(stream);
                BufferedReader reader = new BufferedReader(input);
        ) {
            OrmHelper helper = null;
            LotteryType type = null;
            Dao<Pick, Integer> pickDao = null;
            Dao<PickValue, Integer> valueDao = null;
            Dao<LotteryType, Integer> typeDao = null;

            try {
                helper = ((MainActivity) context).getHelper();
                typeDao = helper.getLotteryTypeDao();
                QueryBuilder builder = typeDao.queryBuilder();
                builder.where().eq("NAME", "LuckyForLife");
                type = typeDao.queryForFirst(builder.prepare());
            } catch (SQLException ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
            int counter = 0;
            String line = reader.readLine();                        // eat header line
            while ((line = reader.readLine()) != null && counter++ < MAX_DAYS) {
                String[] columns = line.split("\\s+");
                String row = "";
                if (columns.length > 2) {

                    try {
                        Pick pick = new Pick();
                        pick.setLotteryType(type);
                        pick.setHistorical(true);
                        pick.setPicked(new SimpleDateFormat("MM-dd-yyyy").parse(columns[0]));
                        pickDao = helper.getPickDao();
                        pickDao.create(pick);
                        valueDao = helper.getPickValueDao();
// to get white balls
                        for (int i = 1; i < columns.length - 2; ++i) {
//                          row += columns[i] + "          ";
                            PickValue value = new PickValue();
                            value.setPick(pick);
                            value.setValue(Integer.parseInt(columns[i]));
                            value.setSequence(1);
                            valueDao.create(value);
                        }
// to get powerballs
                        PickValue value = new PickValue();
                        value.setPick(pick);
                        value.setValue(Integer.parseInt(columns[columns.length - 2]));
                        value.setSequence(2);
                        valueDao.create(value);
//                      strings.add(row);


                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    } catch (NumberFormatException ex) {
                        ex.printStackTrace();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    }
                }
            }

            try {
                QueryBuilder typeQb = typeDao.queryBuilder();
                typeQb.where().eq("NAME", "LuckyForLife");
                QueryBuilder<Pick, Integer> qb = pickDao.queryBuilder();
                Calendar cutOff = Calendar.getInstance();
                cutOff.add(Calendar.MONTH, - 3);
                qb.join(typeQb);

                qb.where().lt("PICKED", cutOff.getTime());
                pickDao.delete(qb.query());
            } catch (SQLException ex) {
                // ignore
            }

        }
    }

    private List<Pick> getPicks() {
        List<String> strings = new ArrayList<>();
        OrmHelper helper = null;
//           LotteryType type = null;
        try {
            helper = ((MainActivity) context).getHelper();
            Dao<LotteryType, Integer> typeDao = helper.getLotteryTypeDao();
            Dao<Pick, Integer> pickDao = helper.getPickDao();
            QueryBuilder<LotteryType, Integer> typeBuilder = typeDao.queryBuilder();
            QueryBuilder<Pick, Integer> pickBuilder = pickDao.queryBuilder();
            typeBuilder.where().eq("NAME", "LuckyForLife");
            pickBuilder.join(typeBuilder);
            pickBuilder.orderBy("PICKED", false);
            List<Pick> picks = pickDao.query(pickBuilder.prepare());
            return picks;
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }
}
