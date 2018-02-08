package com.sook.cs.letitgo.customer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelpers extends SQLiteOpenHelper {


    public MyDBHelpers(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE likedSeller(" +
                "seq INTEGER PRIMARY KEY AUTOINCREMENT," +
                "sSeq INTEGER NOT NULL)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertStore(int sSeq) {
        SQLiteDatabase db = this.getWritableDatabase();
//        db.beginTransaction();
//        try {
//            String sql = "insert into likedSeller (sSeq) values(" + sSeq + ")";
//            db.execSQL(sql);
//            db.setTransactionSuccessful();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            db.endTransaction();
//        }
        ContentValues values = new ContentValues();
        values.put("sSeq", sSeq);

        db.insert("likedSeller", null, values);
        db.close();
    }

    public void deleteStore(int sSeq) {
        SQLiteDatabase db = this.getWritableDatabase();
//        db.beginTransaction();
//        try {
//            String sql = "delete from likedSeller where sSeq =" + sSeq + ")";
//            db.execSQL(sql);
//            db.setTransactionSuccessful();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            db.endTransaction();
//        }
        db.delete("likedSeller", "sSeq=?", new String[]{String.valueOf(sSeq)});
        db.close();
    }

    public boolean isLiked(int sSeq){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select * from likedSeller where sSeq="+sSeq;
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.getCount()!=0)
            return true;
        else
            return false;


    }

}