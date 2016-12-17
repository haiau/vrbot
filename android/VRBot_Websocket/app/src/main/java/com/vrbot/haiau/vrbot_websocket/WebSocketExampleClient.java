package com.vrbot.haiau.vrbot_websocket;

import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Map;

/**
 * Created by haiau on 19/10/2016.
 */

public class WebSocketExampleClient extends WebSocketClient {
    public WebSocketExampleClient(URI serverUri) {
        super( serverUri );
    }

    public WebSocketExampleClient(URI serverUri, Draft draft, Map<String, String> headers, int timeout) {
        super( serverUri, draft, headers, timeout );
    }
    @Override
    public void onOpen( ServerHandshake handshakedata ) {
        Log.d("websocket", "open");
    }
    @Override
    public void onMessage( String message ) {
        final String msg = message;
        Log.d("websocket", msg);
        //Handle this message
    }
    @Override
    public void onClose( int code, String reason, boolean remote ) {
        Log.d("websocket", "closed");
    }
    @Override
    public void onError( Exception ex ) {
        ex.printStackTrace();
    }
}