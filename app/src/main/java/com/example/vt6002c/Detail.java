package com.example.vt6002c;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Detail extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    MapView mapView;
    TextView tvName, tvAddress, tvTel, tvBook, tvOh, tvDis, tvBookWeb;
    Button btnBack, btnMap, btnDir, btnDial, btnShare;
    LocationManager locationManager;

    Location cLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mapView = (MapView) findViewById(R.id.mapFrag);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        tvName = findViewById(R.id.text_name);
        tvAddress = findViewById(R.id.text_add);
        tvBook = findViewById(R.id.text_booktel);
        tvTel = findViewById(R.id.text_telephone);
        tvOh = findViewById(R.id.text_oh);
        btnBack = findViewById(R.id.btn_back);
        btnMap = findViewById(R.id.btn_map);
        btnDir = findViewById(R.id.btn_dir);
        btnDial = findViewById(R.id.btn_dial);
        btnShare = findViewById(R.id.btn_share);
        tvDis = findViewById(R.id.text_dis);
        tvBookWeb = findViewById(R.id.textView8);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            final ClinicInfo info = (ClinicInfo) bundle.getSerializable("data");
            tvName.setText(info.name);
            tvAddress.setText(info.address);
            tvTel.setText(info.telephone);
            if (info.web.startsWith("h")) {
                tvBook.setText(info.web);
                tvBookWeb.setText("Website");
            } else if (info.web.startsWith("N")) {
                tvBook.setText(info.web);
                tvBookWeb.setText("Website");
            } else {
                tvBook.setText(info.booktel);
            }

            btnDial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (info.booktel.equals("")) {
                        if (info.telephone.startsWith("N")) {
                        } else {
                            if (info.booktel.startsWith("N")) {
                            } else {
                                String number = info.telephone.substring(0, 9);

                                Uri call = Uri.parse("tel:" + number);
                                Intent surf = new Intent(Intent.ACTION_DIAL, call);
                                startActivity(surf);
                            }
                        }
                    } else {
                        String number = info.booktel.substring(0, 9);

                        Uri call = Uri.parse("tel:" + number);
                        Intent surf = new Intent(Intent.ACTION_DIAL, call);
                        startActivity(surf);
                    }
                }
            });

            btnDir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (info.distance < 2) {
                        String origin = String.format("%.8f", cLocation.getLatitude()) + "," + String.format("%.8f", cLocation.getLongitude());
                        String des = String.format("%.8f", info.lat) + "," + String.format("%.8f", info.lng);
                        //     String origin = "";
                        //     String des = "";
                        openGoogleMapDir(origin, des);
                    } else {
                        Toast.makeText(Detail.this, "Please try again within 2km", Toast.LENGTH_LONG).show();
                    }
                }

                void openGoogleMapDir(String origin, String des) {
                    String url = "https://www.google.com/maps/dir/?api=1&origin=" + origin + "&destination=" + des + "&travelmode=walking";
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);

                }
            });
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            cLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            tvOh.setText(info.oh);
            tvDis.setText(String.valueOf(info.disS));;

            btnShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "I am now going to"+info.name+ "\nAddress："+info.address+ "\nLocation：https://www.google.com/maps/search/?api=1&query="+info.lat+ "," + info.lng);
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                }
            });


            tvTel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (info.telephone.startsWith("N"))
                    {
                    } else {

                        String number = info.telephone.substring(0, 9);
                        Uri call = Uri.parse("tel:" + number);
                        Intent surf = new Intent(Intent.ACTION_DIAL, call);
                        startActivity(surf);

                    }
                }});

            tvBook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (info.booktel.startsWith("N"))
                    {
                    } else {

                        if(info.web.startsWith("N")) {}

                        else
                        {
                            String number = info.booktel.substring(0, 9);
                            {

                                Uri call = Uri.parse("tel:" + number);
                                Intent surf = new Intent(Intent.ACTION_DIAL, call);
                                startActivity(surf);
                            }}
                    }
                } })
            ;

            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }});

            tvAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startMap(info.lat, info.lng);
                }

                public void startMap(Double lat, Double lng) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=" + lat + "," + lng));
                        intent.setPackage("com.google.android.apps.maps");
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        startActivity(intent);}}});


            btnMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startMap(info.lat, info.lng);
                }

                public void startMap(Double lat, Double lng) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=" + lat + "," + lng));
                        intent.setPackage("com.google.android.apps.maps");
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        //      intent.setData(Uri.parse("geo:22.2571,114.2964"));
                        startActivity(intent);}}});

        }}

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }
}
