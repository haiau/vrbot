package com.vrbot.haiau.vrbot_ssl_websocket;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

public class MainActivity extends AppCompatActivity {

    static final String TAG = "de.tavendo.autobahn.echo";
    private static final String PREFS_NAME = "AutobahnAndroidEcho";

    static EditText mEditTextHostname;
    static EditText mEditTextPort;
    static TextView mTextViewStatusline;
    static Button mButtonStart;
    static EditText mEditTextMessage;
    static Button mButtonSendMessage;

    private SharedPreferences mSettings;

    private void alert(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }

    private void loadPrefs() {
        mEditTextHostname.setText(mSettings.getString("hostname", "192.168.1.9"));
        mEditTextPort.setText(mSettings.getString("port", "9000"));
    }

    private void savePrefs() {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString("hostname", mEditTextHostname.getText().toString());
        editor.putString("port", mEditTextPort.getText().toString());
        editor.commit();
    }

    private void setButtonConnect() {
        mEditTextHostname.setEnabled(true);
        mEditTextPort.setEnabled(true);
        mButtonStart.setText("Connect");
        mButtonStart.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                start();
            }
        });
    }

    private void setButtonDisconnect() {
        mEditTextHostname.setEnabled(false);
        mEditTextPort.setEnabled(false);
        mButtonStart.setText("Disconnect");
        mButtonStart.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                mConnection.disconnect();
            }
        });
    }

    //////////////////////////////////////////////////
    private final WebSocketConnection mConnection = new WebSocketConnection();

    private void start() {

        final String wsuri = "ws://" + mEditTextHostname.getText() + ":" + mEditTextPort.getText();
        mTextViewStatusline.setText("Status: Connecting to " + wsuri + " ..");
        setButtonDisconnect();

        try {
            mConnection.connect(wsuri, new WebSocketHandler() {

                @Override
                public void onOpen() {
                    mTextViewStatusline.setText("Status: Connected to " + wsuri);
                    alert("Status: Connected to " + wsuri);
                    savePrefs();
                    mButtonSendMessage.setEnabled(true);
                    mEditTextMessage.setEnabled(true);
                }

                @Override
                public void onTextMessage(String payload) {
                    alert("Got echo: " + payload);
                }

                @Override
                public void onClose(int code, String reason) {
                    alert("Connection lost.");
                    mTextViewStatusline.setText("Status: Ready.");
                    setButtonConnect();
                    mButtonSendMessage.setEnabled(false);
                    mEditTextMessage.setEnabled(false);
                }
            });
        } catch (WebSocketException e) {
            //Log.d(TAG, e.toString());
            alert(e.toString());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditTextHostname = (EditText) findViewById(R.id.editTextHostName);
        mEditTextPort = (EditText) findViewById(R.id.editTextPort);
        mTextViewStatusline = (TextView) findViewById(R.id.statusline);
        mButtonStart = (Button) findViewById(R.id.start);
        mEditTextMessage = (EditText) findViewById(R.id.editTextMessage);
        mButtonSendMessage = (Button) findViewById(R.id.sendMsg);

        mSettings = getSharedPreferences(PREFS_NAME, 0);
        loadPrefs();

        setButtonConnect();
        mButtonSendMessage.setEnabled(false);
        mEditTextMessage.setEnabled(false);

        mButtonSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConnection.sendTextMessage(mEditTextMessage.getText().toString());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mConnection.isConnected()) {
            mConnection.disconnect();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.quit:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
