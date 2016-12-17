package com.vrbot.haiau.vrbot_ssl;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ControllerActivity extends AppCompatActivity {
    //SharedPreferences
    SharedPreferences sharedPreferences;
    private final String PREFERENCES    = "Prefs";
    private final String PREF_ADDRESS   = "Address";
    private final String PREF_PORT      = "Port";
    private final String PREF_USERNAME  = "Username";
    private final String PREF_PASSWORD  = "Password";

    //variables
    private int port;
    private String address;
    private String username;
    private String password;
    private JSONObject json;
    ExecutorService executor;

    //VideoView
    int videoPort = 5000; //Change video port
    ProgressDialog pDialog;
    VideoView videoview;
    String videoURL;

    public void alert(String text)  {
        Toast.makeText(getBaseContext(), text, Toast.LENGTH_SHORT).show();
    }

    /**************************************************************
     * ASYNC TASK
     */
    class VRBotClientAsyncTask extends AsyncTask<String, Void, Void> {
        SSLSocketClient sslSocketClient;

        @Override
        protected Void doInBackground(String... params) {
            if (sslSocketClient.isConnected()) {
                sslSocketClient.send(params[0]);
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //textViewResponse.setText("Connecting...");
            sslSocketClient = new SSLSocketClient(getBaseContext(), address, port);
            sslSocketClient.connect();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (sslSocketClient.isConnected()) {
                //textViewResponse.setText("Sent!");
                sslSocketClient.close();

            }
            else {
                //textViewResponse.setText("Send failed!");
            }
        }
    }//VRBotClientAsyncTask
    /**************************************************************/

    private int getScale(){
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        Double val = new Double(width)/new Double(640);
        val = val * 100d;
        return val.intValue();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);

        executor = Executors.newSingleThreadExecutor();



        //Load settings
        sharedPreferences = getSharedPreferences(PREFERENCES, this.MODE_PRIVATE);
        username    = sharedPreferences.getString(PREF_USERNAME, "");
        password    = sharedPreferences.getString(PREF_PASSWORD, "");
        address     = sharedPreferences.getString(PREF_ADDRESS, "");
        port        = sharedPreferences.getInt(PREF_PORT, 0);


        //BUTTON EVENT
        //videoview = (VideoView) findViewById(R.id.videoView);
        //Webview
        final WebView webView = (WebView)findViewById(R.id.webView);

        final Switch switchCamera = (Switch)findViewById(R.id.switchCamera);
        switchCamera.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    alert("Camera On");

                    //Webview
                    //webView.loadUrl("http://"+address+":"+port+"/livestream.html");
                    webView.setPadding(0, 0, 0, 0);
                    webView.setInitialScale(getScale());
                    webView.loadUrl("http://"+address+":8080/?action=stream");


                    //VideoView
//                    videoURL    = "http://"+address+":"+port+"/?action=stream";
//                    videoview.setVideoURI(Uri.parse(videoURL));
//                    videoview.requestFocus();
//                    MediaController mediaController = new MediaController(ControllerActivity.this);
//                    videoview.setMediaController(mediaController);
//                    videoview.start();


                    //Convert base64 to Bitmap
//                    String b64 = new VideoStreaming().execute(new String[]{"5000", password});
//                    byte[] decodedString = Base64.decode(, Base64.DEFAULT);
//                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                }
                else {
                    alert("Camera Off");
                    //videoview.stopPlayback();
                    webView.stopLoading();

                }


            }
        });

        //SETTING BUTTON
        Button buttonSetting = (Button)findViewById(R.id.buttonSetting);
        buttonSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ControllerActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //EXIT BUTTON
        Button buttonExit = (Button)findViewById(R.id.buttonExit);
        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });





        final TextView textViewRobotAction = (TextView)findViewById(R.id.textViewRobotAction);
        final TextView textViewCameraAction = (TextView)findViewById(R.id.textViewCameraAction);
        ImageView buttonRobotForward = (ImageView)findViewById(R.id.buttonRobotForward);
        buttonRobotForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewRobotAction.setText("Forward");
                try {
                    json = new JSONObject();
                    json.put("user", username);
                    json.put("pass", password);
                    json.put("cmd", "F"); //Robot Forward
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                //new VRBotClientAsyncTask().execute(new String[]{json.toString()});
                executor.execute(new Runnable() {
                    public void run() {
                        new VRBotClientAsyncTask().execute(new String[]{json.toString()});
                    }
                });



            }
        });

        ImageView buttonRobotBackward = (ImageView)findViewById(R.id.buttonRobotBackward);
        buttonRobotBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewRobotAction.setText("Backwark");
                try {
                    json = new JSONObject();
                    json.put("user", username);
                    json.put("pass", password);
                    json.put("cmd", "B"); //Robot Backward
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //new VRBotClientAsyncTask().execute(new String[]{json.toString()});
                executor.execute(new Runnable() {
                    public void run() {
                        new VRBotClientAsyncTask().execute(new String[]{json.toString()});
                    }
                });
            }
        });

        ImageView buttonRobotLeft = (ImageView)findViewById(R.id.buttonRobotLeft);
        buttonRobotLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewRobotAction.setText("Turn Left");
                try {
                    json = new JSONObject();
                    json.put("user", username);
                    json.put("pass", password);
                    json.put("cmd", "L"); //Robot Left
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //new VRBotClientAsyncTask().execute(new String[]{json.toString()});
                executor.execute(new Runnable() {
                    public void run() {
                        new VRBotClientAsyncTask().execute(new String[]{json.toString()});
                    }
                });

            }
        });

        ImageView buttonRobotRight = (ImageView)findViewById(R.id.buttonRobotRight);
        buttonRobotRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewRobotAction.setText("Turn Right");
                try {
                    json = new JSONObject();
                    json.put("user", username);
                    json.put("pass", password);
                    json.put("cmd", "R"); //Robot Right
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //new VRBotClientAsyncTask().execute(new String[]{json.toString()});
                executor.execute(new Runnable() {
                    public void run() {
                        new VRBotClientAsyncTask().execute(new String[]{json.toString()});
                    }
                });
            }
        });

        ImageView buttonCameraUp = (ImageView)findViewById(R.id.buttonCameraUp);
        buttonCameraUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewCameraAction.setText("Up");
                try {
                    json = new JSONObject();
                    json.put("user", username);
                    json.put("pass", password);
                    json.put("cmd", "u"); //Camera Up
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //new VRBotClientAsyncTask().execute(new String[]{json.toString()});
                executor.execute(new Runnable() {
                    public void run() {
                        new VRBotClientAsyncTask().execute(new String[]{json.toString()});
                    }
                });
            }
        });
        ImageView buttonCameraDown = (ImageView)findViewById(R.id.buttonCameraDown);
        buttonCameraDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewCameraAction.setText("Down");
                try {
                    json = new JSONObject();
                    json.put("user", username);
                    json.put("pass", password);
                    json.put("cmd", "d"); //Camera Down
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //new VRBotClientAsyncTask().execute(new String[]{json.toString()});
                executor.execute(new Runnable() {
                    public void run() {
                        new VRBotClientAsyncTask().execute(new String[]{json.toString()});
                    }
                });
            }
        });
        ImageView buttonCameraLeft = (ImageView)findViewById(R.id.buttonCameraLeft);
        buttonCameraLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewCameraAction.setText("Left");
                try {
                    json = new JSONObject();
                    json.put("user", username);
                    json.put("pass", password);
                    json.put("cmd", "l"); //Camera Left
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //new VRBotClientAsyncTask().execute(new String[]{json.toString()});
                executor.execute(new Runnable() {
                    public void run() {
                        new VRBotClientAsyncTask().execute(new String[]{json.toString()});
                    }
                });
            }
        });
        ImageView buttonCameraRight = (ImageView)findViewById(R.id.buttonCameraRight);
        buttonCameraRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewCameraAction.setText("Right");
                try {
                    json = new JSONObject();
                    json.put("user", username);
                    json.put("pass", password);
                    json.put("cmd", "r"); //Camera Right
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //new VRBotClientAsyncTask().execute(new String[]{json.toString()});
                executor.execute(new Runnable() {
                    public void run() {
                        new VRBotClientAsyncTask().execute(new String[]{json.toString()});
                    }
                });
            }
        });

    }


    /*********************************************************************************/
    /*STREAM VIDEO ASYNC TASK
     */
    // StreamVideo AsyncTask
    private class VideoStreaming extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                int udp_port = Integer.parseInt(params[0]);
                String pass = params[1];

                //Receive
                DatagramSocket ds = new DatagramSocket(port);
                byte[] inbuffer = new byte[4096];
                DatagramPacket dp = new DatagramPacket(inbuffer, inbuffer.length);

                return inbuffer.toString();
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


    }

    /*STREAM VIDEO ASYNC TASK*******************************************************************/
}
