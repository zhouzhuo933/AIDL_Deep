package com.zhouzhuo.aidlfour;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by zhouzhuo on 2018/1/3.
 */

public class WelcomeActivity extends Activity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    public void jumpAidl(View view){
        Intent intent = new Intent(WelcomeActivity.this,AIDLActivity.class);
        startActivity(intent);
    }

    public void jumpMessenger(View view){
        Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
        startActivity(intent);

    }
}
