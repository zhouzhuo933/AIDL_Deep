package com.zhouzhuo.aidlfour;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by zhouzhuo on 2018/1/3.
 * Messenger方式
 */

public class MainActivity extends Activity implements View.OnClickListener{
    private static final int SAY_HELLO = 0;
    private Messenger messenger;
    private boolean bound;
    private Button button;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        button = findViewById(R.id.btn_button);
        button.setOnClickListener(this);
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("zhouzhuo","service Connected");
            messenger = new Messenger(service);
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("zhouzhuo","service disConnected");
            bound = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent();
        intent.setAction("com.zhouzhuo.server.service.MessengerService");
        intent.setPackage("com.zhouzhuo.server");
        bindService(intent,mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_button:
                if(bound){
                    //发送一条消息给服务端
                    Message message = Message.obtain(null,SAY_HELLO,0,0);
                    //Message message = Message.obtain();
                    try {
                        messenger.send(message);
                        Log.d("zhouzhuo","发送消息成功");
                    } catch (RemoteException e) {
                        Log.d("zhouzhuo","发送消息失败");
                        e.printStackTrace();
                    }

                }else {
                    Toast.makeText(MainActivity.this,"服务未启动",Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }
}
