package com.vrbot.haiau.vrbot_ssl;

import android.content.Context;
import android.util.Log;
//import android.widget.Toast;

import org.apache.http.conn.ssl.SSLSocketFactory;

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

/**
 * Created by haiau on 01/11/2016.
 */

public class SSLSocketClient {
    private int port;
    private String address;
    private int connectTimeout;

    private char keystorepass[] = "haiau123".toCharArray();
    //private char keypassword[] = "haiau".toCharArray();

    private SSLSocket socket;
    private BufferedReader buffer_in;
    private BufferedWriter buffer_out;
    //for debug
    private final String TAG = "TAG";

    private Context context;

    public SSLSocketClient(Context context, String address,  int port) {
        this.context = context;
        this.address = address;
        this.port = port;
        this.connectTimeout = 2000; // miliseconds
        this.socket = null;
        this.buffer_in = null;
        this.buffer_out = null;
    }


    public void connect() {
        try{

            KeyStore ks = KeyStore.getInstance("BKS");
            InputStream keyin = context.getResources().openRawResource(R.raw.clienttruststore);
            ks.load(keyin,keystorepass);

            SSLSocketFactory socketFactory = new SSLSocketFactory(ks);
            socketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            //socket = (SSLSocket)socketFactory.createSocket(new Socket(address,port), address, port, false);
            //socket.startHandshake();

            socket = (SSLSocket)socketFactory.createSocket();
            socket.connect(new InetSocketAddress(address, port), connectTimeout);
            socket.startHandshake();
            buffer_out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            buffer_in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //DEBUG
            //printServerCertificate(socket);
            printSocketInfo(socket);

        } catch (UnknownHostException e) {
            //Toast.makeText(context, "Unknown host", Toast.LENGTH_SHORT).show();
            Log.i(TAG,"Unknown host");
            e.printStackTrace();
            //System.exit(1);
        } catch  (IOException e) {
            //Toast.makeText(context, "No I/O", Toast.LENGTH_SHORT).show();
            Log.i(TAG,"No I/O");
            e.printStackTrace();
            //System.exit(1);
        } catch (KeyStoreException e) {
            //Toast.makeText(context, "Keystore ks error", Toast.LENGTH_SHORT).show();
            Log.i(TAG,"Keystore ks error");
            e.printStackTrace();
            //System.exit(-1);
        } catch (NoSuchAlgorithmException e) {
            //Toast.makeText(context, "No such algorithm for ks.load", Toast.LENGTH_SHORT).show();
            Log.i(TAG,"No such algorithm for ks.load");
            e.printStackTrace();
            //System.exit(-1);
        } catch (CertificateException e) {
            //Toast.makeText(context, "certificate missing", Toast.LENGTH_SHORT).show();
            Log.i(TAG,"certificate missing");
            e.printStackTrace();
            //System.exit(-1);
        } catch (UnrecoverableKeyException e) {
            //Toast.makeText(context, "UnrecoverableKeyException", Toast.LENGTH_SHORT).show();
            Log.i(TAG,"unrecoverableKeyException");
            e.printStackTrace();
            //System.exit(-1);
        } catch (KeyManagementException e) {
            //Toast.makeText(context, "KeyManagementException", Toast.LENGTH_SHORT).show();
            Log.i(TAG,"key management exception");
            e.printStackTrace();
            //System.exit(-1);
        }//try catch...
    } //connect

    public boolean isConnected() {
        return socket.isConnected();
    }

    public void close() {
        if (socket.isConnected()) {
            try {
                socket.close();
            } catch (IOException e) {
                Log.i(TAG,"I/O exception");
                e.printStackTrace();
                //System.exit(-1);
            }
        }
    }//close

    private void printServerCertificate(SSLSocket socket) {
        try {
            Certificate[] serverCerts =
                    socket.getSession().getPeerCertificates();
            for (int i = 0; i < serverCerts.length; i++) {
                Certificate myCert = serverCerts[i];
                Log.i(TAG,"====Certificate:" + (i+1) + "====");
                Log.i(TAG,"-Public Key-\n" + myCert.getPublicKey());
                Log.i(TAG,"-Certificate Type-\n " + myCert.getType());

                System.out.println();
            }
        } catch (SSLPeerUnverifiedException e) {
            Log.i(TAG,"Could not verify peer");
            e.printStackTrace();
            //System.exit(-1);
        }
    }//printServerCertificate
    private void printSocketInfo(SSLSocket s) {
        Log.i(TAG,"Socket class: "+s.getClass());
        Log.i(TAG,"   Remote address = "
                +s.getInetAddress().toString());
        Log.i(TAG,"   Remote port = "+s.getPort());
        Log.i(TAG,"   Local socket address = "
                +s.getLocalSocketAddress().toString());
        Log.i(TAG,"   Local address = "
                +s.getLocalAddress().toString());
        Log.i(TAG,"   Local port = "+s.getLocalPort());
        Log.i(TAG,"   Need client authentication = "
                +s.getNeedClientAuth());
        SSLSession ss = s.getSession();
        Log.i(TAG,"   Cipher suite = "+ss.getCipherSuite());
        Log.i(TAG,"   Protocol = "+ss.getProtocol());
    }//printSocketInfo

    public void send(String message) {
        try {
            //Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            buffer_out.write(message+"\n");
            buffer_out.flush();
        } catch (IOException e) {
            //Toast.makeText(context, "Error: Send failed", Toast.LENGTH_SHORT).show();
            Log.i(TAG,"Error: Send failed");
            e.printStackTrace();
        }
    } //send

    public String receive() {
        String result = null;
        try {
            result = buffer_in.readLine();
        } catch (IOException e) {
            Log.i(TAG,"Error: Read failed");
            e.printStackTrace();
        }
        return result;
    }//receive

}
