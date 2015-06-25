package com.kreasys.dvendy.teslocation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
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
    EditText editText1, editText2;
    String status;

    final String baseUrl = "http://192.168.10.79"; //server address

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtLat = (TextView) findViewById(R.id.textview1);
        button1 = (Button) findViewById(R.id.button);
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        editText1 = (EditText) findViewById(R.id.editText);
        editText2 = (EditText) findViewById(R.id.editText2);

        appLocationService = new AppLocationService(
                StatusActivity.this);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                ServerDatabaseHandler serverDatabaseHandler = new ServerDatabaseHandler(baseUrl);
                if (serverDatabaseHandler.isServerReachable(baseUrl)) {
                    Location gpsLocation = appLocationService
                            .getLocation(LocationManager.GPS_PROVIDER);
                    if (gpsLocation != null) {
                        txtLat.setText("Latitude:" + gpsLocation.getLatitude() + ", Longitude:" + gpsLocation.getLongitude());
                        serverDatabaseHandler = new ServerDatabaseHandler(baseUrl);
                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = new Date();

                        try {
                            if(!serverDatabaseHandler.setLocation(editText2.getText().toString(), dateFormat.format(date), String.valueOf(gpsLocation.getLatitude()), String.valueOf(gpsLocation.getLongitude()), status, editText1.getText().toString())){
                                Toast.makeText(StatusActivity.this, "Terjadi kesalahan pada server, mohon coba beberapa saat lagi.", Toast.LENGTH_LONG).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(StatusActivity.this, "Terjadi kesalahan pada server, mohon coba beberapa saat lagi.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        Toast.makeText(StatusActivity.this, "Lokasi berhasil terupdate", Toast.LENGTH_LONG).show();
                    } else {
                        showSettingsAlert();
                    }
                }else
                    Toast.makeText(StatusActivity.this, "Tidak terdapat terhubung ke jaringan. Mohon aktifkan jaringan.", Toast.LENGTH_LONG).show();
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
        alertDialog.setMessage("Aktifkan lokasi! Masuk ke menu pengaturan?");
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
    }
}
