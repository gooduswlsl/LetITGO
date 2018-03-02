package com.sook.cs.letitgo.customer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelperLiked extends SQLiteOpenHelper {

    public DBHelperLiked(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql_store = "CREATE TABLE likedStore(" +
                "seq INTEGER PRIMARY KEY AUTOINCREMENT," +
                "sSeq INTEGER NOT NULL)";
        String sql_menu = "CREATE TABLE likedMenu(" +
                "seq INTEGER PRIMARY KEY AUTOINCREMENT," +
                "mSeq INTEGER NOT NULL)";

        db.execSQL(sql_store);
        db.execSQL(sql_menu);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertStore(int sSeq) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("sSeq", sSeq);
        db.insert("likedStore", null, values);
        db.close();
    }

    public void deleteStore(int sSeq) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("likedStore", "sSeq=?", new String[]{String.valueOf(sSeq)});
        db.close();
    }

    public boolean isLikedStore(int sSeq) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select * from likedStore where sSeq=" + sSeq;
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
        String sql = "select mSeq from likedMenu";
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

}