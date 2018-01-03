package com.zhouzhuo.server.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import com.zhouzhuo.client.Book;
import com.zhouzhuo.client.BookManager;
import com.zhouzhuo.client.IOnNewBookArrivedListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class AIDLService extends Service {
    private final String TAG = this.getClass().getSimpleName();
    //包含book对象的list
    private List<Book> mBooks = new ArrayList<>();
    private RemoteCallbackList<IOnNewBookArrivedListener> list = new RemoteCallbackList<>();

    private AtomicBoolean mIsServiceDestroyed = new AtomicBoolean(false);
    @Override
    public IBinder onBind(Intent intent) {
        return manager;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Book book = new Book();
        book.setName("Android 开发艺术探索");
        book.setPrice(28);
        mBooks.add(book);
        new Thread(new ServiceWorker()).start();
    }
    private BookManager.Stub manager = new BookManager.Stub() {
        @Override
        public List<Book> getBooks() throws RemoteException {
            Log.e("zhouzhuo","invoking getBooks() method,now the list is:"+mBooks.toString());
            if(mBooks!=null){
                return mBooks;
            }
            return new ArrayList<>();
        }

        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String packageName = null;
            String[] packages = getPackageManager().getPackagesForUid(getCallingUid());
            if(packages!=null&&packages.length>0){
                packageName = packages[0];
            }
            if(!packageName.startsWith("com.zhouzhuo")){
                return false;
            }
            Log.d("zhouzhuo","onTransact:"+packageName);
            return super.onTransact(code, data, reply, flags);
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            synchronized (this){
                if(mBooks == null){
                    mBooks = new ArrayList<>();
                }
                if(book == null){
                    book = new Book();
                    book.setPrice(22222);
                    book.setName("aaa");
                }

                if(!mBooks.contains(book)){
                    mBooks.add(book);
                }
                //打印mBooks列表,观察客户端传过来的值
                Log.e("zhouzhuo","invoking addBooks() method,now the list is:"+mBooks.toString());
            }

        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
            list.register(listener);
            final int N = list.beginBroadcast();
            list.finishBroadcast();;
            Log.d("zhouzhuo","registerListener:"+N);
        }

        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
            boolean success = list.unregister(listener);

            if (success) {
                Log.d(TAG, "unregister success.");
            } else {
                Log.d(TAG, "not found, can not unregister.");
            }
            final int N = list.beginBroadcast();
            list.finishBroadcast();
            Log.d(TAG, "unregisterListener, current size:" + N);

        }
    };

    private class ServiceWorker implements Runnable{

        @Override
        public void run() {
            //do background processing here。。。
            while (!mIsServiceDestroyed.get()){
                try {
                   Thread.sleep(5000);
                }catch (Exception e){
                    e.printStackTrace();
                }
                int bookId = mBooks.size()+1;
                Book book = new Book();
                book.setPrice(bookId);
                book.setName("new Book"+bookId);
                onNewBookArrived(book);
            }

        }
    }

    private void onNewBookArrived(Book book){
        mBooks.add(book);
        final int N = list.beginBroadcast();
        for (int i=0;i<N;i++){
            IOnNewBookArrivedListener l = list.getBroadcastItem(i);
            if(l!=null){
                try {
                    l.onNewBookArrived(book);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
        list.finishBroadcast();
    }


}
