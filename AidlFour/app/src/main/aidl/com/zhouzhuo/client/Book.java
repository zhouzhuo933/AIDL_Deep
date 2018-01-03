package com.zhouzhuo.client;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhouzhuo on 2018/1/3.
 */

public class Book implements Parcelable{
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    private String name;
    private int price;

    public Book(){}

    public Book(Parcel in){
        name = in.readString();
        price = in.readInt();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(price);
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    /**
     * 参数是一个Parcel,用它来存储和传输数据
     * @param dest
     */
    public void readFromParcel(Parcel dest){
        //注意,此处的读值顺序应当是和WriteToParcel（）方法中一致的
        name = dest.readString();
        price = dest.readInt();
    }

    @Override
    public String toString() {
        return "name:"+name+",price"+price;
    }
}
