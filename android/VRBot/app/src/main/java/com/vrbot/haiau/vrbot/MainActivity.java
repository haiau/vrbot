package com.vrbot.haiau.vrbot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText editTextAddress    = (EditText)findViewById(R.id.editTextAddress);
        final EditText editTextPort       = (EditText)findViewById(R.id.editTextPort);
        Button buttonConnect        = (Button)findViewById(R.id.buttonConnect);
        final TextView textViewResponse   = (TextView)findViewById(R.id.textViewResponse);

        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = editTextAddress.getText().toString();
                int port = Integer.parseInt(editTextPort.getText().toString());
                textViewResponse.setText("Response: "+address+":"+port);
                VRBotClient vrBotClient = new VRBotClient(address, port, textViewResponse);

            }
        });

    }
}
