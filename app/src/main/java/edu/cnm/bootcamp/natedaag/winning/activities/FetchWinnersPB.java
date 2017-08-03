package edu.cnm.bootcamp.natedaag.winning.activities;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import edu.cnm.bootcamp.natedaag.winning.R;

/**
 * Created by natedaag on 8/2/17.
 */

public class FetchWinnersPB extends AsyncTask<Void, Void, Void> {

    private static int BUFFER_SIZE = 4096;
    private static int DATE_COLUMN = 0;
    private static int FIRST_PICK_VALUE_COLUMN = 1;

    private Context context;

    public FetchWinnersPB(Context context) {
        super();
        this.context=context;
    }


    @Override
    protected Void doInBackground(Void... params) {
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
            updatePicks(output.toByteArray());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
        return  null;
    }

    private void updatePicks(byte[] data) throws IOException {
        try (
                ByteArrayInputStream stream = new ByteArrayInputStream(data);
                InputStreamReader input = new InputStreamReader(stream);
                BufferedReader reader = new BufferedReader(input);
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split("\\s* + \\s*");
                // TODO write column values to db
            }
        }
    }
}

