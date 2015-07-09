package com.kreasys.dvendy.teslocation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by DVendy - Kreasys on 6/25/2015.
 */
public class StatusActivity extends Activity {

    AppLocationService appLocationService;

    TextView txtLat;
    Button button1;
    Spinner spinner1;
    EditText editText1;
    String status, tUsername;

    Location gpsLocation;

    String baseUrl; //server address

    private Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intent = getIntent();
        tUsername = intent.getStringExtra("tUsername");
        baseUrl = intent.getStringExtra("baseUrl");

        //txtLat = (TextView) findViewById(R.id.textview1);
        button1 = (Button) findViewById(R.id.button);
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        editText1 = (EditText) findViewById(R.id.editText);

        appLocationService = new AppLocationService(
                StatusActivity.this);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                button1.setClickable(false);
                button1.setText("Updating...");

                if (appLocationService.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    gpsLocation = appLocationService
                            .getLocation(LocationManager.GPS_PROVIDER);
                    if (gpsLocation != null) {
//                        Toast.makeText(StatusActivity.this, "GPS", Toast.LENGTH_LONG).show();
                        LocGps locTask = new LocGps();
                        locTask.execute("abc", "10", "Hello world");
                    }else{
                        gpsLocation = appLocationService
                                .getLocation(LocationManager.NETWORK_PROVIDER);
                        if (gpsLocation != null) {
//                            Toast.makeText(StatusActivity.this, "Internet", Toast.LENGTH_LONG).show();
                            LocInternet locTask = new LocInternet();
                            locTask.execute("abc", "10", "Hello world");
                        }else {
                            Toast.makeText(StatusActivity.this, "Sedang mencari lokasi, mohon coba beberapa saat lagi.", Toast.LENGTH_LONG).show();
                            button1.setClickable(true);
                            button1.setText("Update");
                        }
                    }
                }else{
                    showSettingsAlert();
                }

            }
        });

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println((String) parent.getItemAtPosition(position));
                status = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                StatusActivity.this);
        alertDialog.setTitle("PENGATURAN");
        alertDialog.setMessage("Aktifkan GPS! Masuk ke menu pengaturan?");
        alertDialog.setPositiveButton("Pengaturan",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        StatusActivity.this.startActivity(intent);
                    }
                });
        alertDialog.setNegativeButton("Batal",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
        button1.setClickable(true);
        button1.setText("Update");
    }

    private class LocGps extends AsyncTask<String, Integer, Void> {

        @Override
        protected Void doInBackground(String... arg) {
            ServerDatabaseHandler serverDatabaseHandler = new ServerDatabaseHandler(baseUrl);
            if (serverDatabaseHandler.isServerReachable(baseUrl)) {
                //txtLat.setText("Latitude:" + gpsLocation.getLatitude() + ", Longitude:" + gpsLocation.getLongitude());
                serverDatabaseHandler = new ServerDatabaseHandler(baseUrl);
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();

                try {
                    if (!serverDatabaseHandler.setLocation(tUsername, dateFormat.format(date), String.valueOf(gpsLocation.getLatitude()), String.valueOf(gpsLocation.getLongitude()), status, editText1.getText().toString())) {
                        showTaskStatus("Terjadi kesalahan pada server, mohon coba beberapa saat lagi.");
                        return null;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showTaskStatus("Terjadi kesalahan pada server, mohon coba beberapa saat lagi.");
                    return null;
                }

                showTaskStatus("Lokasi berhasil terupdate");
            } else
                showTaskStatus("Tidak terdapat terhubung ke jaringan. Mohon aktifkan jaringan.");
            return null;
        }

        private void showTaskStatus(final String message) {
            runOnUiThread(new Thread() {
                public void run() {
                    Toast.makeText(StatusActivity.this, message, Toast.LENGTH_LONG).show();
                    button1.setClickable(true);
                    button1.setText("Update");
                }
            });
        }
    }

    private class LocInternet extends AsyncTask<String, Integer, Void> {

        @Override
        protected Void doInBackground(String... arg) {
            ServerDatabaseHandler serverDatabaseHandler = new ServerDatabaseHandler(baseUrl);
            if (serverDatabaseHandler.isServerReachable(baseUrl)) {
                //txtLat.setText("Latitude:" + gpsLocation.getLatitude() + ", Longitude:" + gpsLocation.getLongitude());
                serverDatabaseHandler = new ServerDatabaseHandler(baseUrl);
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();

                try {
                    if (!serverDatabaseHandler.setLocation(tUsername, dateFormat.format(date), String.valueOf(gpsLocation.getLatitude()), String.valueOf(gpsLocation.getLongitude()), status, editText1.getText().toString())) {
                        showTaskStatus("Terjadi kesalahan pada server, mohon coba beberapa saat lagi.");
                        return null;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showTaskStatus("Terjadi kesalahan pada server, mohon coba beberapa saat lagi.");
                    return null;
                }

                showTaskStatus("Lokasi berhasil terupdate");
            } else
                showTaskStatus("Tidak terdapat terhubung ke jaringan. Mohon aktifkan jaringan.");
            return null;
        }

        private void showTaskStatus(final String message) {
            runOnUiThread(new Thread() {
                public void run() {
                    Toast.makeText(StatusActivity.this, message, Toast.LENGTH_LONG).show();
                    button1.setClickable(true);
                    button1.setText("Update");
                }
            });
        }
    }
}
