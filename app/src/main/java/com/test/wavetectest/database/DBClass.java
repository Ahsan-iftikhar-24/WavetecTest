package com.test.wavetectest.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.test.wavetectest.models.ImageDBModel;
import com.test.wavetectest.models.ImagesModel;

import java.util.ArrayList;
import java.util.List;

public class DBClass extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "wave.db";  // Database Name
    public static final int VERSION = 1;                   // Database VERSION
    public static final String IMAGE_TABLE = "imagesTable";


    public DBClass(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Create Type Table
        String createTableImage = "CREATE TABLE IF NOT EXISTS " + IMAGE_TABLE +"(imgID INTEGER PRIMARY KEY,photographerName TEXT, width TEXT, height TEXT, url TEXT, pUrl TEXT, imgMed TEXT, imgLarge TEXT)";
        db.execSQL(createTableImage);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + IMAGE_TABLE);
        onCreate(db);
    }

    // Insert values in FORM Table
    public boolean insertUserData(ImageDBModel imageDBModel){

        SQLiteDatabase db = this.getWritableDatabase();

        //Content value used to hold data like hash map against table columns
        ContentValues cv = new ContentValues();
        cv.put("imgID",imageDBModel.getImgID());
        cv.put("photographerName",imageDBModel.getPhotographerName());
        cv.put("width",imageDBModel.getWidth());
        cv.put("height",imageDBModel.getHeight());
        cv.put("url",imageDBModel.getUrl());
        cv.put("pUrl",imageDBModel.getpUrl());
        cv.put("imgMed",imageDBModel.getImgMed());
        cv.put("imgLarge",imageDBModel.getImgLarge());



        // Insert in SQLlite DB
        db.insertWithOnConflict(IMAGE_TABLE,null,cv,SQLiteDatabase.CONFLICT_REPLACE);

        db.close();

        return true;
    }

    // GET all POST DOCUMENT DATA
    public List<ImageDBModel> getAllData(){

        List<ImageDBModel> returnList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "Select * FROM "+IMAGE_TABLE;

        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()){

            do{

                int imgID = cursor.getInt(0);
                String photographerName = cursor.getString(1);
                String width = cursor.getString(2);
                String height = cursor.getString(3);
                String url = cursor.getString(4);
                String pUrl = cursor.getString(5);
                String imgMed = cursor.getString(6);
                String imgLarge = cursor.getString(7);
                ImageDBModel imageDBModel = new ImageDBModel(imgID,photographerName,width,height,url,pUrl,imgMed,imgLarge);

                returnList.add(imageDBModel);


            }while (cursor.moveToNext());

        }

        else {

            //ADD nothing in the list
        }

        cursor.close();
        db.close();


        return returnList;
    }

    public boolean deleteImageRow(int imgID){

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(IMAGE_TABLE,"imgID = ?" ,
                new String[]{String.valueOf(imgID)});
        db.close();
        return true;
    }


}
