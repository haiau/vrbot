package com.vrbot.haiau.vrbot_websocket;

import android.content.Context;
import android.os.AsyncTask;
import com.loopj.android.http.PersistentCookieStore;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.java_websocket.drafts.Draft_17;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;

import cz.msebera.android.httpclient.cookie.Cookie;


import android.view.View;
import android.widget.Toast;
import com.vrbot.haiau.vrbot_websocket.MainActivity;
import com.vrbot.haiau.vrbot_websocket.WebSocketExampleClient;
import org.java_websocket.client.DefaultSSLWebSocketClientFactory;
import java.io.FileInputStream;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;


/**
 * Created by haiau on 20/10/2016.
 */

public class SocketHandler {
    //Server IP address
    private static String localipaddress = "192.168.1.200";
    private static final String localWSSIP = "wss://" + localipaddress + ":8080/socket";

    private int port;
    private String address;
    private int connectTimeout;
    private Context context;
    private char keystorepass[] = "haiau123".toCharArray();

    private SSLSocket socket;
    private BufferedReader buffer_in;
    private BufferedWriter buffer_out;

    public SocketHandler(String output, MainActivity main){
        SocketConnector socketConnector = new SocketConnector();
        socketConnector.sendString = output;
        socketConnector.main = main;
        socketConnector.execute();
        this.context = main;
    }

    private class SocketConnector extends AsyncTask<Void, Void, Void> {
        private String sendString;
        private MainActivity main;

        @Override
        protected Void doInBackground(Void... voids) {
            PersistentCookieStore cookieStore = SingletonPersistentCookieStore.getInstance(main);
            final Cookie cookie = cookieStore.getCookies().get(0);
            try {
                Map<String, String> cmap = new HashMap<String, String>();
                String cookieString = cookie.getName()+"="+cookie.getValue();
                cmap.put("cookie", cookieString);

                URI uri = new URI(localWSSIP);

                WebSocketExampleClient webSocketExampleClient = new WebSocketExampleClient(uri, new Draft_17(), cmap, connectTimeout);

                //This part is needed in case you are going to use self-signed certificates

                TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
                    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
                }};


                try {
                    SSLContext sc = SSLContext.getInstance("TLS");
                    sc.init(null, trustAllCerts, new java.security.SecureRandom());

                    //Otherwise the line below is all that is needed.
                    //sc.init(null, null, null);
                    webSocketExampleClient.setWebSocketFactory(new DefaultSSLWebSocketClientFactory(sc));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                webSocketExampleClient.connectBlocking();
                webSocketExampleClient.send(sendString);
                /*

                KeyStore trustStore = KeyStore.getInstance("JKS");
                InputStream tsis = context.getResources().openRawResource(R.raw.servertruststore);
                trustStore.load(tsis, "haiau".toCharArray());
                tsis.close();

                KeyStore serverKeyStore = KeyStore.getInstance("JKS");
                InputStream ksis = context.getResources().openRawResource(R.raw.server);
                serverKeyStore.load(ksis, "haiau".toCharArray());
                ksis.close();

                TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                tmf.init(trustStore);

                KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                kmf.init(serverKeyStore, keystorepass);

                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

                */

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        } //doInBackground
    }
}