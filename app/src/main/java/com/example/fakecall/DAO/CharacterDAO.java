package com.example.fakecall.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import com.example.fakecall.CovertIMG;
import com.example.fakecall.Database.SQL;
import com.example.fakecall.R;

import java.util.ArrayList;

public class CharacterDAO {
    static SQL db ;
    SQLiteDatabase sqLiteDatabase ;
    public static Context context ;

    public CharacterDAO(Context context) {
        db = new SQL(context);
        this.context= context ;
        //Check();
    }
    public static void Check()
    {
        SharedPreferences preferencess =context.getSharedPreferences("USER_FILE",context.MODE_PRIVATE);
        Boolean check = true ;
        //check = preferencess.getBoolean("chekkk" , false) ;
        if (check==true)
        {
            insertData() ;
            SharedPreferences preferences =context. getSharedPreferences("USER_FILE", context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("chekkk", false);
            check = preferencess.getBoolean("chekkk" , false) ;
        }
    }
    public long inserCharacter (ModelCharacter modelCharacter)
    {
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase() ;
        ContentValues values = new ContentValues( );
        //
        values.put("name" , modelCharacter.getName() );
        values.put("numberPhone" , modelCharacter.getSdt() );
        values.put("imageView" , modelCharacter.getHinhanh() );
        long kq = sqLiteDatabase.insert("character", null , values) ;
        return kq ;
    }
    public int update(ModelCharacter modelCharacter)
    {
        sqLiteDatabase = db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", modelCharacter.getName());
        values.put("imageView", modelCharacter.getHinhanh());
        int kq = sqLiteDatabase.update("character",values, "numberPhone=?" , new String[]{String.valueOf(modelCharacter.getSdt())});
        return kq;
    }
    public static long insertData()
    {
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase() ;
        ContentValues values = new ContentValues( );
        Bitmap bitmap1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.backgroundsamsaung , null);
        values.put("name","Bùi văn chung ");
        values.put("numberPhone" ,"0355789456");
        values.put("imageView" ,CovertIMG.getBytes(bitmap1));
        long kq = sqLiteDatabase.insert("character", null , values) ;

        return kq ;
    }
    public ArrayList<ModelCharacter>getdata (String sql,String ...a)
    {
        sqLiteDatabase = db.getWritableDatabase() ;
        ArrayList<ModelCharacter> list = new ArrayList<>() ;
        Cursor cursor = sqLiteDatabase.rawQuery(sql , a);
        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            String name = cursor.getString(0) ;
            String sdt = cursor.getString(1) ;
            byte [] hinhanh = cursor.getBlob(2);
            ModelCharacter character = new ModelCharacter( name , sdt , hinhanh) ;
            list.add(character) ;
            cursor.moveToNext() ;
        }
        cursor.close();
        sqLiteDatabase.close();
        return list ;
    }
    public ArrayList<ModelCharacter> getAll(){
        String sql=" SELECT * FROM character ";
        return getdata(sql);
    }
    public int delete ( )
    {
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
        return sqLiteDatabase.delete("character" ,null  , null);
    }
    public int  getsdt( String sdt){
        String sql=" SELECT * FROM character WHERE numberPhone =? ";
       ArrayList<ModelCharacter> list = getdata(sql , sdt) ;
        Log.e(" list ","  "+ list) ;
       if (list.size()==0)
       {
           return -1 ;
       }
       return 1;
    }
}
