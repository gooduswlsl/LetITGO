package com.sook.cs.letitgo.customer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sook.cs.letitgo.item.Order;

import java.util.ArrayList;

public class DBHelperCart extends SQLiteOpenHelper {

    public DBHelperCart(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE cart(" +
                "seq INTEGER PRIMARY KEY AUTOINCREMENT," +
                "mSeq INTEGER NOT NULL," +
                "sSeq INTEGER NOT NULL," +
                "num INTEGER NOT NULL,"+
                "tTime VARCHAR,"+
                "msg VARCHAR)";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

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

    public void deleteCart(int mSeq) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("cart", "mSeq=?", new String[]{String.valueOf(mSeq)});
        db.close();
    }

    public void updateNum(int mSeq, int num){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("num", num);
        db.update("cart", values, "mSeq=?", new String[]{String.valueOf(mSeq)});
        db.close();
    }

    public void updateMsg(int mSeq, String msg){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("msg", msg);
        db.update("cart", values, "mSeq=?", new String[]{String.valueOf(mSeq)});
        db.close();
    }

    public void updateTime(int mSeq, String tTime){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tTime", tTime);
        db.update("cart", values, "mSeq=?", new String[]{String.valueOf(mSeq)});
        db.close();
    }


    public ArrayList<Order> getCartList() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select * from cart";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();

        ArrayList<Order> orderList = new ArrayList<>();
        for (int i = 0; i < cursor.getCount(); i++) {
            Order item = new Order();
            item.setMenu_seq(cursor.getInt(cursor.getColumnIndex("mSeq")));
            item.setSeller_seq(cursor.getInt(cursor.getColumnIndex("sSeq")));
            item.setNum(cursor.getInt(cursor.getColumnIndex("num")));
            item.setMessage(cursor.getString(cursor.getColumnIndex("msg")));
            item.setTime_take(cursor.getString(cursor.getColumnIndex("tTime")));

            orderList.add(item);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return orderList;
    }

    public void flushDB(){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql_drop = "DROP TABLE Cart";

        String sql_create = "CREATE TABLE cart(" +
                "seq INTEGER PRIMARY KEY AUTOINCREMENT," +
                "mSeq INTEGER NOT NULL," +
                "sSeq INTEGER NOT NULL," +
                "num INTEGER NOT NULL,"+
                "tTime VARCHAR,"+
                "msg VARCHAR)";

        db.execSQL(sql_drop);
        db.execSQL(sql_create);
    }
}