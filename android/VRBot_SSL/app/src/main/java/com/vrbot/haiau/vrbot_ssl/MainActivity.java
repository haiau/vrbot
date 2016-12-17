package com.vrbot.haiau.vrbot_ssl;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.conn.ssl.SSLSocketFactory;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

public class MainActivity extends AppCompatActivity {
    private int port;
    private String address;
    private String username;
    private String password;
    private JSONObject json;

    //Views
    private Button buttonConnect;
    private TextView textViewResponse;
    private EditText editTextAddress;
    private EditText editTextPort;
    private EditText editTextUsername;
    private EditText editTextPassword;

    //Log
    private final String TAG            = "DEBUG";

    //SharedPreferences
    SharedPreferences sharedPreferences;
    private final String PREFERENCES    = "Prefs";
    private final String PREF_ADDRESS   = "Address";
    private final String PREF_PORT      = "Port";
    private final String PREF_USERNAME  = "Username";
    private final String PREF_PASSWORD  = "Password";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Find views
        editTextAddress     = (EditText)findViewById(R.id.editTextAddress);
        editTextPort        = (EditText)findViewById(R.id.editTextPort);
        editTextUsername    = (EditText)findViewById(R.id.editTextUsername);
        editTextPassword    = (EditText)findViewById(R.id.editTextPassword);
        buttonConnect       = (Button)findViewById(R.id.buttonConnect);

        //Load settings
        sharedPreferences = getSharedPreferences(PREFERENCES, this.MODE_PRIVATE);
        editTextAddress.setText(sharedPreferences.getString(PREF_ADDRESS, ""));
        editTextPort.setText(Integer.toString(sharedPreferences.getInt(PREF_PORT, 0)));
        editTextUsername.setText(sharedPreferences.getString(PREF_USERNAME, ""));
        editTextPassword.setText(sharedPreferences.getString(PREF_PASSWORD, ""));


        //Fix StrickMode
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                address     = editTextAddress.getText().toString();
                port        = Integer.parseInt(editTextPort.getText().toString());
                username    = editTextUsername.getText().toString();
                password    = editTextPassword.getText().toString();

                //Save information to sharePreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(PREF_ADDRESS, address);
                editor.putInt(PREF_PORT, port);
                editor.putString(PREF_USERNAME, username);
                editor.putString(PREF_PASSWORD, password);
                editor.commit();

                Intent intent = new Intent(MainActivity.this, ControllerActivity.class);
                startActivity(intent);
                finish();

            } //onClick
        });//setOnClickListener
    } //onCreate

    void alert(String text)  {
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }



}

