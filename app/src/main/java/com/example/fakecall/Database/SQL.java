package com.example.fakecall.Database;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.Nullable;

import com.example.fakecall.DAO.CharacterDAO;
import com.example.fakecall.R;
public class SQL extends SQLiteOpenHelper {

    public SQL(@Nullable Context context) {

        super(context, "FakeCallData",null ,2 );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String TABLECHARACTER ="CREATE TABLE character (name  TEXT NOT NULL ,numberPhone  TEXT NOT NULL , imageView BLOB )";
//        String tk= " INSERT INTO character VALUES ('Bui van chung ','1111111', ";
//        db.execSQL(tk);
        db.execSQL(TABLECHARACTER);
     }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String TABLECHARACTER ="drop table if exists thuthu";
        db.execSQL(TABLECHARACTER);
    }
}
