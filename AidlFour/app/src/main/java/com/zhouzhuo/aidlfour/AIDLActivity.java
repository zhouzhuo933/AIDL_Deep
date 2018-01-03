package com.zhouzhuo.aidlfour;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.zhouzhuo.client.Book;
import com.zhouzhuo.client.BookManager;
import com.zhouzhuo.client.IOnNewBookArrivedListener;

import java.util.List;

/**
 * AIDL方式
 */

public class AIDLActivity extends AppCompatActivity {
   //由AIDL文件生成的java类
    private BookManager mBookManager;
    //标记当前与服务端连接的布尔值,false为未连接,true为连接中
    private boolean mBound ;

    private static final int MESSAGE_NEW_BOOK_ARRIVED = 1;

    //包含Book对象的list
    private List<Book> mBooks;


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MESSAGE_NEW_BOOK_ARRIVED:
                    Log.d("zhouzhuo","receive newbook:"+msg.obj);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl);
    }

    public void addBook(View view){
        //如果与服务端的连接处于未连接状态,则尝试连接
        if(!mBound){
            attemptToBindService();
            Toast.makeText(this,"当前与服务端处于未连接状态,正在尝试重连,请稍后再试",Toast.LENGTH_SHORT).show();
            return;
        }
        if(mBookManager == null){
            return;
        }
        Book book = new Book();
        book.setName("App研发录In");
        book.setPrice(30);
        try {
            mBookManager.addBook(book);
        } catch (RemoteException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!mBound){
            attemptToBindService();
        }
    }

    private IOnNewBookArrivedListener listener = new IOnNewBookArrivedListener.Stub(){

        @Override
        public void onNewBookArrived(Book newBook) throws RemoteException {
            handler.obtainMessage(MESSAGE_NEW_BOOK_ARRIVED,newBook).sendToTarget();
        }
    };

    private void attemptToBindService() {
        Intent intent = new Intent();
        intent.setAction("com.zhouzhuo.server.service.AIDLService");
        intent.setPackage("com.zhouzhuo.server");
        bindService(intent,connection, Context.BIND_AUTO_CREATE);

    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("zhouzhuo","onServiceConnected");
            mBookManager = BookManager.Stub.asInterface(service);
            mBound = true;
            if(mBookManager!=null){
                try {
                    mBooks = mBookManager.getBooks();
                   // mBookManager.registerListener();
                    Log.d("zhouzhuo","query book list:"+mBooks.toString());
                    Book newBook = new Book();
                    newBook.setName("Android进阶");
                    newBook.setPrice(3);
                    mBookManager.addBook(newBook);
                    List<Book> list = mBookManager.getBooks();
                    Log.d("zhouzhuo","query book list:"+list.size());
                    mBookManager.registerListener(listener);

                    Log.d("zhouzhuo","mBooks :"+mBooks.toString());
                } catch (RemoteException e) {
                    Log.d("zhouzhuo","eddddd=="+e.toString());
                    e.printStackTrace();
                }

            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("zhouzhuo","onServiceDisconnected");

        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        if(mBound){
            unbindService(connection);
            mBound = false;
        }
    }
}
