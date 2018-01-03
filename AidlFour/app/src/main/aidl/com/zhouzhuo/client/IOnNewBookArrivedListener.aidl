// IOnNewBookArrivedListener.aidl
package com.zhouzhuo.client;

// Declare any non-default types here with import statements
import com.zhouzhuo.client.Book;

interface IOnNewBookArrivedListener {
   void onNewBookArrived(in Book newBook);
}
