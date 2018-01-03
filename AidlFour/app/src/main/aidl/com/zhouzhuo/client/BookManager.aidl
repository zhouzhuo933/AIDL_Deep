// BookManager.aidl
package com.zhouzhuo.client;

// Declare any non-default types here with import statements
import com.zhouzhuo.client.Book;
import com.zhouzhuo.client.IOnNewBookArrivedListener;

interface BookManager {
    List<Book> getBooks();
    void addBook(inout Book book);
    void registerListener(IOnNewBookArrivedListener listener);
    void unregisterListener(IOnNewBookArrivedListener listener);
}
