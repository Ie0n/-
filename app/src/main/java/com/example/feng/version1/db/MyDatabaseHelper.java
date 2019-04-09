package com.example.feng.version1.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by feng on 2019/3/22.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private Context monet;
    public static  final String CREATE_TOTA = "create table TOTA ("
            +"id integer primary key autoincrement, "
            +"device text , "
            +"meter text , "
            +"top real , "
            +"low real )";
//    public static  final String CREATE_DEURL = "create table DEURL ("
//            +"id integer primary key autoincrement, "
//            +"url text , "
//            +"dev text )";
    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        monet = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TOTA);
//        db.execSQL(CREATE_DEURL);
       // Toast.makeText(monet,"Creat succeeded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
