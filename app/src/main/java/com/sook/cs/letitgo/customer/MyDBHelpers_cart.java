package com.sook.cs.letitgo.customer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.sook.cs.letitgo.item.Order;

import java.util.ArrayList;

public class MyDBHelpers_cart extends SQLiteOpenHelper {

    public MyDBHelpers_cart(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE cart(" +
                "seq INTEGER PRIMARY KEY AUTOINCREMENT," +
                "mSeq INTEGER NOT NULL," +
                "sSeq INTEGER NOT NULL," +
                "num INTEGER NOT NULL)";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertCart(int mSeq, int sSeq, int num) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("mSeq", mSeq);
        values.put("sSeq", sSeq);
        values.put("num", num);

        db.insert("cart", null, values);
        db.close();
    }

    public void updateCart(int mSeq, int num) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "select num from cart where mSeq=" + mSeq;
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();

        ContentValues values = new ContentValues();
        values.put("num", num + cursor.getInt(0));
        db.update("cart", values, "mSeq=?", new String[]{String.valueOf(mSeq)});
        db.close();
    }

    public void deleteStore(int sSeq) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("likedStore", "sSeq=?", new String[]{String.valueOf(sSeq)});
        db.close();
    }

    public boolean isInCart(int mSeq) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select * from cart where mSeq=" + mSeq;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() != 0)
            return true;
        else
            return false;
    }

    public int[] getStoreList() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select sSeq from likedStore";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        int length = cursor.getCount();
        int[] sSeq = new int[length];
        for (int i = 0; i < length; i++) {
            sSeq[i] = cursor.getInt(0);
            cursor.moveToNext();
        }
        return sSeq;

    }

    public void insertMenu(int mSeq) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("mSeq", mSeq);
        db.insert("likedMenu", null, values);
        db.close();
    }

    public void deleteMenu(int mSeq) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("likedMenu", "mSeq=?", new String[]{String.valueOf(mSeq)});
        db.close();
    }

    public boolean isLikedMenu(int mSeq) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select * from likedMenu where mSeq=" + mSeq;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() != 0)
            return true;
        else
            return false;
    }

    public int[] getMenuList() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select mSeq from cart";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        int length = cursor.getCount();
        int[] mSeq = new int[length];
        for (int i = 0; i < length; i++) {
            mSeq[i] = cursor.getInt(0);
            cursor.moveToNext();
        }
        return mSeq;
    }

    public ArrayList<Order> getCartList() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select * from cart";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();

        ArrayList<Order> orderList = new ArrayList<Order>();
        for (int i = 0; i < cursor.getCount(); i++) {
            Order item = new Order();
            item.setMenu_seq(cursor.getInt(cursor.getColumnIndex("mSeq")));
            item.setSeller_seq(cursor.getInt(cursor.getColumnIndex("sSeq")));
            item.setNum(cursor.getInt(cursor.getColumnIndex("num")));
            Log.d("item", String.valueOf(item.getMenu_seq()));
            orderList.add(item);

            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return orderList;
    }

}