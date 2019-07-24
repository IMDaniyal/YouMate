package com.example.youmate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.Intent.getIntent;

public class MyHelper extends SQLiteOpenHelper {

    private static final String dbname="mydb";
    private  static final int version=1;
    public MyHelper( Context context )
    {
        super(context,dbname,null,version);
    }

    @Override
    public void onCreate( SQLiteDatabase db ) {


        db.execSQL("Create Table IF NOT EXISTS Book (ID INTEGER PRIMARY KEY AUTOINCREMENT, URL VARCHAR(255));");


    }

 protected void dataInsert( String url )
 {
     SQLiteDatabase sd=this.getWritableDatabase();
     ContentValues values= new ContentValues();
     values.put("URL",url);
     sd.insert("Book",null,values);
 }
/*
 public Cursor getData()
 {
     SQLiteDatabase sd=this.getReadableDatabase();
     Cursor cursor= sd.rawQuery("SELECT URL From Book",null);
     return cursor;
 }
*/
public ArrayList<UrlModelData> loadData()
{

    ArrayList<UrlModelData> arrayList=new ArrayList<>();
    SQLiteDatabase db=this.getReadableDatabase();

    Cursor c=db.rawQuery("SELECT * FROM BOOK",null);


    while (c.moveToNext())
    {
        String u= c.getString(1);
        UrlModelData urlModelData = new UrlModelData(u);
        arrayList.add(urlModelData);
    }

    return arrayList;
}
    @Override
    public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion ) {

    }
}
