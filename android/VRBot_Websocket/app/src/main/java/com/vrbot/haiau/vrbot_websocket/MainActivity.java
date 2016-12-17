package com.vrbot.haiau.vrbot_websocket;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.PersistentCookieStore;

import org.java_websocket.drafts.Draft_17;

import java.io.InputStream;
import java.net.URI;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import cz.msebera.android.httpclient.cookie.Cookie;

public class MainActivity extends AppCompatActivity {

    private String sendString;
    private MainActivity main;
    private char keystorepass[] = "haiau123".toCharArray();
    //private char keypassword[] = "haiau".toCharArray();
    //Server IP address
    private static String localipaddress = "192.168.1.9";
    private static final String localWSSIP = "wss://" + localipaddress + ":9000";

    private static final int TIMEOUT = 10000;

    void alert(String msg) {
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonSend = (Button)findViewById(R.id.buttonSend);
        final EditText editTextMessage = (EditText)findViewById(R.id.editTextMessage);
        final TextView textViewResult = (TextView)findViewById(R.id.textViewResult);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = editTextMessage.getText().toString();
                textViewResult.setText(msg);

                new SocketHandler("Hello", MainActivity.this);
            }
        });

    }
}
