package com.example.vt6002c;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Hospital extends AppCompatActivity {

    ProgressBar progressBar;
    ArrayAdapter arrayAdapter;
    ArrayList<ClinicInfo> clinicList = new ArrayList<>();
    ListView listView;
    TextView textView, tvCL;
    Location cLocation;
    LocationManager locationManager;
    Button btnHo, btnHk, btnKl, btnNt, btnMain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);
        progressBar = findViewById(R.id.progressBar2);
        listView = findViewById(R.id.list_ho);
        textView = findViewById(R.id.text_head);
        tvCL=findViewById(R.id.tv_cl);
        btnHo = findViewById(R.id.btn_hoall);
        btnHk = findViewById(R.id.btn_hohk);
        btnKl = findViewById(R.id.btn_hokl);
        btnNt = findViewById(R.id.btn_hont);
        btnMain=findViewById(R.id.m_b);


        tvCL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Hospital.this, Clinic.class);
                startActivity(intent);
                ;
            }
        });

        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Hospital.this, MainActivity.class);
                startActivity(intent);
            }
        });



        GetTextDataTask task = new GetTextDataTask();
        textView.setText("Hospital - ALL Hong Kong");
        task.execute("https://geodata.gov.hk/gs/api/v1.0.0/geoDataQuery?q=%7Bv%3A%221%2E0%2E0%22%2Cid%3A25714788-aea1-4aaa-a76c-8a943513b5d0%2Clang%3A%22ALL%22%7D");

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        getLocation();
    }
    void checkPermission(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1010);}
            else {
            cLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0f, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    cLocation = location;
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {
                }
            });
        }}

    void getLocation()    {
    if (isGPSEnable()){
        checkPermission();
    }else {
        openGPS();
    }}

    boolean isGPSEnable(){
        return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void openGPS(){
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(intent,0);
    }

    class GetTextDataTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //           getLocation();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
            parseJSON(s);
            showDownloadedList();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection;
            URL url;
            String result = "";

            try {
                url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                InputStream in = connection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String line = null;
                while ((line = br.readLine()) != null) {
                    result += line;
                    result += "\n";
                }
                br.close();
                in.close();
            } catch (Exception e) {
            }
            return result;
        }
    }

    public void parseJSON(String result) {
        try {
            clinicList.clear();

            JSONObject jsonObject = new JSONObject(result);

            JSONArray featuresArray = jsonObject.getJSONArray("features");
            for (int i = 0; i < featuresArray.length(); i++) {

                JSONObject obj = featuresArray.getJSONObject(i);
                ClinicInfo clinicInfo = new ClinicInfo();
                JSONObject clinicProperty = obj.getJSONObject("properties");

                clinicInfo.name = clinicProperty.getString("Facility Name");
                clinicInfo.address = clinicProperty.getString("Address");
                clinicInfo.booktel = clinicProperty.getString("Telephone");
                clinicInfo.telephone = obj.getJSONObject("properties").getString("Telephone");
                clinicInfo.oh = clinicProperty.getString("Opening Hours").replace("<br>", "\n");
                clinicInfo.web = clinicProperty.getString("Website");
                clinicInfo.lat = obj.getJSONObject("geometry").getJSONArray("coordinates").getDouble(1);
                clinicInfo.lng = obj.getJSONObject("geometry").getJSONArray("coordinates").getDouble(0);
                    Location location = new Location(LocationManager.GPS_PROVIDER);
                    location.setLatitude(clinicInfo.lat);
                    location.setLongitude(clinicInfo.lng);
                    clinicInfo.distance = cLocation.distanceTo(location)/1000;

                   DecimalFormat df=new DecimalFormat("0.0");

                //NumberFormat nf = NumberFormat.getInstance();
                //nf.setMaximumFractionDigits(2);

                clinicInfo.disS = Double.valueOf(df.format(clinicInfo.distance));


                clinicList.add(clinicInfo);

                listView.setAdapter(arrayAdapter);
            }




        } catch (Exception e) {
        }
        Collections.sort(clinicList, new Comparator<ClinicInfo>() {
            @Override
            public int compare(ClinicInfo o1, ClinicInfo o2) {

                    return ((Double) o1.disS).compareTo((Double) o2.disS);
            }
        });
    }


    public void showDownloadedList() {
        String[] clinicName = new String[clinicList.size()];
        Double[] locD = new Double[clinicList.size()];
        List<Map<String, Object>> listItems = new ArrayList<>();

        for (int i = 0; i < clinicList.size(); i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", clinicName[i] = clinicList.get(i).name);
            item.put("dis", locD[i] = clinicList.get(i).disS);
            listItems.add(item);
            clinicName[i] = clinicList.get(i).name;
    /*        Collections.sort(listItems, new Comparator<Map<String, Object>>() {
                @Override
                public int compare(Map<String, Object> object1,
                                   Map<String, Object> object2) {
                    return ((Double) object1.get("dis")).compareTo((Double) object2.get("dis"));
         //           return ((String) object1.get("name")).compareTo((String) object2.get("name"));

                }
            });*/
        }
        SimpleAdapter adapter = new SimpleAdapter(this, listItems, R.layout.layout_listitem, new String[]{"name", "dis"}, new int[]{R.id.tv_name, R.id.tv_dis});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(listener);
    }

    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

         //   ClinicInfo item = clinicList.get(position);
            Intent intent = new Intent(Hospital.this, Detail.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", clinicList.get(position));
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };



    public void allDownload (View v){
        GetTextDataTask task = new GetTextDataTask();
        textView.setText("Hospital - ALL Hong Kong");
        task.execute("https://geodata.gov.hk/gs/api/v1.0.0/geoDataQuery?q=%7Bv%3A%221%2E0%2E0%22%2Cid%3A25714788-aea1-4aaa-a76c-8a943513b5d0%2Clang%3A%22ALL%22%7D");
    }

    public void hkDownload(View v) {
        GetTextDataTask task= new GetTextDataTask();
        textView.setText("Hospital - HKI");
        task.execute("https://geodata.gov.hk/gs/api/v1.0.0/geoDataQuery?q=%7Bv%3A%221%2E0%2E0%22%2Cid%3A25714788-aea1-4aaa-a76c-8a943513b5d0%2Clang%3A%22ALL%22%2Chk80MinX%3A800000%2Chk80MinY%3A800000%2Chk80MaxX%3A850000%2Chk80MaxY%3A817000%7D");
    }

    public void klDownload(View v) {
        GetTextDataTask task = new GetTextDataTask();
        textView.setText("Hospital - KLN");
        task.execute("https://geodata.gov.hk/gs/api/v1.0.0/geoDataQuery?q=%7Bv%3A%221%2E0%2E0%22%2Cid%3A25714788-aea1-4aaa-a76c-8a943513b5d0%2Clang%3A%22ALL%22%2Chk80MinX%3A800000%2Chk80MinY%3A817000%2Chk80MaxX%3A850000%2Chk80MaxY%3A823100%7D");
    }

    public void ntDownload(View v) {
        GetTextDataTask task = new GetTextDataTask();
        textView.setText("Hospital - NTW");
        task.execute("https://geodata.gov.hk/gs/api/v1.0.0/geoDataQuery?q=%7Bv%3A%221%2E0%2E0%22%2Cid%3A25714788-aea1-4aaa-a76c-8a943513b5d0%2Clang%3A%22ALL%22%2Chk80MinX%3A800000%2Chk80MinY%3A823100%2Chk80MaxX%3A850000%2Chk80MaxY%3A850000%7D");
    }

}