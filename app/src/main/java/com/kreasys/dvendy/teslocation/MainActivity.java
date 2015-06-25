package com.kreasys.dvendy.teslocation;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends Activity {

    Button buttonLogin;
    EditText username;
    EditText password;
    String tUsername, tPassword;

    final String baseUrl = "http://192.168.10.79"; //server address

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pre login actions
                tUsername = username.getText().toString();
                tPassword = password.getText().toString();
                username.setEnabled(false);
                password.setEnabled(false);
                buttonLogin.setClickable(false);
                buttonLogin.setText("Connecting...");

                //login actions
                loginAsyncTask loginTask = new loginAsyncTask();
                loginTask.execute("abc", "10", "Hello world");
            }
        });

    }

    private class loginAsyncTask extends AsyncTask<String, Integer, Void> {

        @Override
        protected Void doInBackground(String... arg) {
            ServerDatabaseHandler serverDatabaseHandler = new ServerDatabaseHandler(baseUrl);
            if (serverDatabaseHandler.isServerReachable(baseUrl)) {
                if (serverDatabaseHandler.login(tUsername, tPassword)){
                    Intent intent = new Intent("com.kreasys.dvendy.teslocation.StatusActivity");

                    startActivity(intent);
                }else
                    showTaskStatus("Username atau password salah.");
            }else
                showTaskStatus("Tidak terdapat terhubung ke jaringan. Mohon aktifkan jaringan.");
            return null;
        }

        private void showTaskStatus(final String message)
        {
            runOnUiThread(new Thread() {
                public void run() {
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                    username.setEnabled(true);
                    password.setEnabled(true);
                    buttonLogin.setClickable(true);
                    buttonLogin.setText("Login");
                }
            });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
