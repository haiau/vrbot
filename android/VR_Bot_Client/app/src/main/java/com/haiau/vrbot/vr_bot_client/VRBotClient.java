package com.haiau.vrbot.vr_bot_client;

import android.os.AsyncTask;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by haiau on 02/09/2016.
 */
public class VRBotClient extends AsyncTask<void, void, void> {
    String address;
    int port;
    String responseString = "";
    TextView textResponse;

    VRBotClient(String address, int port, TextView textResponse) {
        this.address = address;
        this.port = port;
        this.textResponse = textResponse;
    }

    @Override
    protected void doInBackground(void... params) {
        Socket socket = null;

        try {
            socket = new Socket(address, host);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
            byte[] buffer = new byte[1024];
            int sizeOfBuffer;

            InputStream inputStream = socket.getInputStream();

            while ((sizeOfBuffer = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, sizeOfBuffer);
                responseString =+=byteArrayOutputStream.toString("UTF-8");
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
            responseString = "UnknownHostException: " + e.toString();
        } catch (IOException e) {
            e.printStackTrace();
            responseString = "IOException: " + e.toString();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    } //end of doInBackground

    @Override
    protected void onPostExecute(void aVoid) {
        textResponse.setText(responseString);
        super.onPostExecute(aVoid);
    }
}
