package com.vrbot.haiau.vrbot_wss;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.java_websocket.WebSocketImpl;
import org.java_websocket.client.DefaultSSLWebSocketClientFactory;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;import javax.net.ssl.TrustManagerFactory;

public class MainActivity extends AppCompatActivity {
    private Button buttonConnect;
    private EditText editTextMessage;

    public class VRBotClient extends WebSocketClient {

        public VRBotClient( URI serverUri ) {
            super( serverUri );
        }


        @Override
        public void onOpen( ServerHandshake handshakedata ) {
            Log.i("Event: ", "onOpen");
        }

        @Override
        public void onMessage( String message ) {
            Log.i("Event: ", "onMessage");
        }

        @Override
        public void onClose( int code, String reason, boolean remote ) {
            Log.i("Event: ", "onClose");
            System.exit( 0 );

        }

        @Override
        public void onError( Exception ex ) {
            Log.i("Event: ", "onError");
            ex.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonConnect = (Button)findViewById(R.id.buttonConnect);
        editTextMessage = (EditText)findViewById(R.id.editTextMessage);

        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebSocketImpl.DEBUG = true;

                // load up the key store
                String STORETYPE = "BKS";
                String KEYSTORE = "keystore.jks";
                String STOREPASSWORD = "haiau123";
                String KEYPASSWORD = "haiau";

                try {
                    VRBotClient chatclient = new VRBotClient( new URI( "wss://192.168.1.9:9000" ) );

                    KeyStore ks = KeyStore.getInstance(STORETYPE);
                    InputStream keyin = v.getResources().openRawResource(R.raw.clienttruststore);
                    ks.load(keyin, STOREPASSWORD.toCharArray());

                    KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                    kmf.init(ks, KEYPASSWORD.toCharArray());
                    TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                    tmf.init(ks);

                    SSLContext sslContext = null;
                    sslContext = SSLContext.getInstance("TLS");
                    sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
                    //sslContext.init( null, null, null ); // will use java's default key and trust store which is sufficient unless you deal with self-signed certificates

                    javax.net.ssl.SSLSocketFactory factory = sslContext.getSocketFactory();// (SSLSocketFactory) SSLSocketFactory.getDefault();

                    chatclient.setWebSocketFactory( new DefaultSSLWebSocketClientFactory( sslContext ) );

                    //chatclient.connectBlocking();
                    chatclient.connect();

                    //BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                    String line = editTextMessage.getText().toString();
                    if (line.equals("close")) {
                        chatclient.close();
                    } else {
                        chatclient.send(line);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (KeyManagementException e) {
                    e.printStackTrace();
                } catch (CertificateException e) {
                    e.printStackTrace();
                } catch (KeyStoreException e) {
                    e.printStackTrace();
                } catch (UnrecoverableKeyException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
//                catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
        });
    }
}
