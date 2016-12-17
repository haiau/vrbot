package com.vrbot.haiau.vrbot_ssl;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by haiau on 24/11/2016.
 */


public class UDPVideoStreaming extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {
        String result = null;

        try {
            String address = params[0];
            int port = Integer.parseInt(params[1]);

            JSONObject json = new JSONObject();
            json.put("user", params[2]);
            json.put("pass", params[3]);
            json.put("cmd", "stream");

            String message = json.toString();

            //Receive
            DatagramSocket ds = new DatagramSocket(port);
            byte[] inbuffer = new byte[4096];
            DatagramPacket dp = new DatagramPacket(inbuffer, inbuffer.length);

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return result;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

    }
}//VRBotClientAsyncTask