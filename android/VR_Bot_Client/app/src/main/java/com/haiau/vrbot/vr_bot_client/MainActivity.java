package com.haiau.vrbot.vr_bot_client;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.haiau.vrbot.*;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText editTextAddress    = (EditText)findViewById(R.id.editTextAddress);
        final EditText editTextPort       = (EditText)findViewById(R.id.editTextPort);
        Button buttonConnect        = (Button)findViewById(R.id.buttonConnect);
        final TextView textViewResponse   = (TextView)findViewById(R.id.textViewResponse);

        buttonConnect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = editTextAddress.getText().toString();
                int port = Integer.parseInt(editTextPort.getText().toString());
                textViewResponse.setText("Response: "+address+":"+port);
            }
        });

    }
}
