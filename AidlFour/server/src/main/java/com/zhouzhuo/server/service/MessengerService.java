package com.zhouzhuo.server.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

public class MessengerService extends Service {
    private static final int SAY_HELLO = 0;
    public MessengerService() {
    }
    static class ServiceHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SAY_HELLO:
                    Log.d("zhouzhuo","收到了客户端发来的消息sayHello");
                    break;
            }
        }
    }
    private Messenger messenger = new Messenger(new ServiceHandler());
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("zhouzhuo","MessengerService onCreate()");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return  messenger.getBinder();
    }
}
