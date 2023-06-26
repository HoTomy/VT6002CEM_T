package com.example.vt6002c;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AE extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> hospNames;



    Button btnMain;
    TextView textUT;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ae);
        progressBar = findViewById(R.id.progressBar);
        btnMain=findViewById(R.id.m_b);
        listView = findViewById(R.id.listView);
        textUT = findViewById(R.id.text_ut);
        hospNames = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, hospNames);
        listView.setAdapter(adapter);

        DownloadTask task = new DownloadTask();
        task.execute("https://www.ha.org.hk/opendata/aed/aedwtdata-en.json");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String hospName = hospNames.get(position);
                Toast.makeText(AE.this, "Selected hospital: " + hospName, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=" + hospName));
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
            }
        });

        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AE.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            StringBuilder result = new StringBuilder();
            URL url;
            HttpURLConnection connection = null;
            try {
                url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                return result.toString();
            } catch (Exception e) {
                Log.e("DownloadTask", "Error downloading JSON data: " + e.getMessage());
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //           getLocation();
            progressBar.setVisibility(View.VISIBLE);
        }


        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.GONE);
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String updateTime = jsonObject.getString("updateTime");
                    textUT.setText("Update Time :       " +updateTime);
                    JSONArray waitTimeArray = jsonObject.getJSONArray("waitTime");
                    for (int i = 0; i < waitTimeArray.length(); i++) {
                        JSONObject waitTimeObject = waitTimeArray.getJSONObject(i);
                        String hospName = waitTimeObject.getString("hospName");
                        String topWait = waitTimeObject.getString("topWait");
                        hospNames.add(hospName + ": " + topWait);
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.e("DownloadTask", "Error parsing JSON data: " + e.getMessage());
                }
            } else {
                Toast.makeText(AE.this, "Failed to download JSON data", Toast.LENGTH_SHORT).show();
            }
        }
    }
}