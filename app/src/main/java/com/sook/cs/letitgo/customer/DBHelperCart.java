package com.sook.cs.letitgo.customer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.sook.cs.letitgo.MyApp;
import com.sook.cs.letitgo.item.Order;
import com.sook.cs.letitgo.item.Seller;
import com.sook.cs.letitgo.remote.RemoteService;
import com.sook.cs.letitgo.remote.ServiceGenerator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                "num INTEGER NOT NULL," +
                "mPrice INTEGER NOT NULL," +
                "tTime VARCHAR," +
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

    public void insertCart(int mSeq, int sSeq, int num, int mPrice) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("mSeq", mSeq);
        values.put("sSeq", sSeq);
        values.put("num", num);
        values.put("mPrice", mPrice);

        db.insert("cart", null, values);
        db.close();
    }

    public void updateCart(int mSeq, int num) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "select num from cart where mSeq=" + mSeq;
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();

        ContentValues values = new ContentValues();
        values.put("num", num + cursor.getInt(cursor.getColumnIndex("num")));
        db.update("cart", values, "mSeq=?", new String[]{String.valueOf(mSeq)});
        db.close();
    }

    public void deleteCart(int mSeq) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("cart", "mSeq=?", new String[]{String.valueOf(mSeq)});
        db.close();
    }

    public void updateNum(int mSeq, int num) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("num", num);
        db.update("cart", values, "mSeq=?", new String[]{String.valueOf(mSeq)});
        db.close();
    }

    public void updateMsg(int sSeq, String msg) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("msg", msg);
        db.update("cart", values, "sSeq=?", new String[]{String.valueOf(sSeq)});
        db.close();
    }

    public void updateTime(int sSeq, String tTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tTime", tTime);
        db.update("cart", values, "mSeq=?", new String[]{String.valueOf(sSeq)});
        db.close();
    }

    public ArrayList<Order> getCartgList() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select * from cart group by sSeq";
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();

        ArrayList<Order> orderList = new ArrayList<>();
        for (int i = 0; i < cursor.getCount(); i++) {
            Order item = new Order();
            item.setSeller_seq(cursor.getInt(cursor.getColumnIndex("sSeq")));
            item.setMessage(cursor.getString(cursor.getColumnIndex("msg")));
            item.setTime_take(cursor.getString(cursor.getColumnIndex("tTime")));

            orderList.add(item);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return orderList;
    }

    public ArrayList<Order> getCartList(int sSeq, int cSeq) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select * from cart ";
        if (sSeq != -1)
            sql += "where sSeq = " + sSeq;

        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();

        ArrayList<Order> orderList = new ArrayList<>();
        for (int i = 0; i < cursor.getCount(); i++) {
            Order item = new Order();
            item.setCust_seq(cSeq);
            item.setMenu_seq(cursor.getInt(cursor.getColumnIndex("mSeq")));
            item.setSeller_seq(cursor.getInt(cursor.getColumnIndex("sSeq")));
            item.setNum(cursor.getInt(cursor.getColumnIndex("num")));
            item.setPrice(cursor.getInt(cursor.getColumnIndex("mPrice")));
            item.setMessage(cursor.getString(cursor.getColumnIndex("msg")));
            item.setTime_take(cursor.getString(cursor.getColumnIndex("tTime")));

            orderList.add(item);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return orderList;
    }

    public void flushDB() {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql_delete = "DELETE FROM cart";

        db.execSQL(sql_delete);
        db.close();
    }
}