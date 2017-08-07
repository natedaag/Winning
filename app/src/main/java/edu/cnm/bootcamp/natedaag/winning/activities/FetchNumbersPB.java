package edu.cnm.bootcamp.natedaag.winning.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import edu.cnm.bootcamp.natedaag.winning.R;

/**
 * Created by natedaag on 8/2/17.
 */

// To fill a listView with the list of powerball numbers that may or may not be used.
public class FetchNumbersPB extends AsyncTask<Void, Void, List<String>> {

    private static int BUFFER_SIZE = 4096;
    private static int DATE_COLUMN = 0;
    private static int FIRST_PICK_VALUE_COLUMN = 1;

    private Context context;

    private ListView listView;

    public FetchNumbersPB(Context context, ListView listView) {
        super();
        this.context=context;
        this.listView=listView;
    }

    @Override
    protected List<String> doInBackground(Void... params) {
        List<String> rows = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(context.getString(R.string.powerball_url));
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
            rows = updatePicks(output.toByteArray());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
        return  rows;
    }

//    @Override
//    protected void onPostExecute(List<String> strings) {
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.activity_pickview, strings);
//        listView.setAdapter(adapter);
//    }

    private List<String> updatePicks(byte[] data) throws IOException {
        List<String>  strings = new ArrayList<>();
        try (
            ByteArrayInputStream stream = new ByteArrayInputStream(data);
            InputStreamReader input = new InputStreamReader(stream);
            BufferedReader reader = new BufferedReader(input);
        ) {
            // need to stop at 30 lines. this reads all lines.
            String line = reader.readLine();                            // eat header line
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split("\\s*+\\s*");
                String row = "";
                if (columns.length > 1) {
                    for (int i = 1; i < columns.length; ++i) {
                        row += columns[i] + "          ";
                    }
                    strings.add(row);
                }
            }
        }
        return strings;
    }
}
