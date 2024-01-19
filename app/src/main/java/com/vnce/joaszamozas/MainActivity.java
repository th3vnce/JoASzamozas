package com.vnce.joaszamozas;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button loadButton;
    private TextView dataTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadButton = findViewById(R.id.button);
        dataTextView = findViewById(R.id.dataTextView);

        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoadDataTask().execute("https://raw.githubusercontent.com/th3vnce/testsql/main/datas.csv");
            }
        });
    }

    private class LoadDataTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String urlString = params[0];
            try {
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStreamReader reader = new InputStreamReader(urlConnection.getInputStream());
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
                List<CSVRecord> records = csvParser.getRecords();

                StringBuilder result = new StringBuilder();
                for (CSVRecord record : records) {
                    result.append("Name: ").append(record.get(0))
                            .append(", Room: ").append(record.get(1))
                            .append(", Location: ").append(record.get(2))
                            .append("\n");
                }

                return result.toString();

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                dataTextView.setText(result);
            } else {
                Toast.makeText(MainActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
