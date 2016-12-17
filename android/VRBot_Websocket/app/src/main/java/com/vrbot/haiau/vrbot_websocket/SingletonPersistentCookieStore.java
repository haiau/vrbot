package com.vrbot.haiau.vrbot_websocket;

import com.loopj.android.http.PersistentCookieStore;

/**
 * Created by haiau on 23/11/2016.
 */
public class SingletonPersistentCookieStore extends PersistentCookieStore {

    private static SingletonPersistentCookieStore instance = null;

    protected SingletonPersistentCookieStore(android.content.Context context){
        super(context);
    }

    public static SingletonPersistentCookieStore getInstance(android.content.Context context){
        if(instance == null){
            instance = new SingletonPersistentCookieStore(context);
        }
        return instance;
    }
}