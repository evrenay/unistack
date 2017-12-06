package com.example.evren.unistack.chat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by EVREN on 6.12.2017.
 */

public class Database extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "message_database";//database adı

    private static final String TABLE_NAME = "messages";
    private static String Message = "message";
    private static String Message_ID = "id";
    private static String Gonderen = "gonderen";
    private static String Gonderilen_Oda = "gonderilen";
    private static String Tarih = "tarih";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) { // Databesi oluşturuyoruz.Bu methodu biz çağırmıyoruz. Databese de obje oluşturduğumuzda otamatik çağırılıyor.
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + Message_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Message + " TEXT,"
                + Gonderen + " TEXT,"
                + Gonderilen_Oda + " TEXT,"
                + Tarih + " NUMERIC" + ")";
        db.execSQL(CREATE_TABLE);

    }
    public void DeleteMessage(int id){ //id si belli olan row u silmek için

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, Message_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }
    public void AddMessage(String message, String gonderen,String gonderilen_Oda,String tarih) {
        //addmessage methodu ise  Databese veri eklemek için
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Message, message);
        values.put(Gonderen, gonderen);
        values.put(Gonderilen_Oda, gonderilen_Oda);
        values.put(Tarih, tarih);

        db.insert(TABLE_NAME, null, values);
        db.close(); //Database Bağlantısını kapattık*/
    }
    public HashMap<String, String> MesajDetay(int id){
        //Databeseden id si belli olan row u çekmek için.
        //Bu methodda sadece tek row değerleri alınır.
        //HashMap bir çift boyutlu arraydir.anahtar-değer ikililerini bir arada tutmak için tasarlanmıştır.
        //map.put("x","300"); mesala burda anahtar x değeri 300.

        HashMap<String,String> message = new HashMap<String,String>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME+ " WHERE id="+id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            message.put(Message, cursor.getString(1));
            message.put(Gonderen, cursor.getString(2));
            message.put(Gonderilen_Oda, cursor.getString(3));
            message.put(Tarih, cursor.getString(4));
        }
        cursor.close();
        db.close();
        // return message
        return message;
    }
    public ArrayList<HashMap<String, String>> ArrayMessages(){

        //Bu methodda ise tablodaki tüm değerleri alıyoruz
        //ArrayList adı üstünde Array lerin listelendiği bir Array.Burda hashmapleri listeleyeceğiz
        //Herbir satırı değer ve value ile hashmap a atıyoruz. Her bir satır 1 tane hashmap arrayı demek.
        //olusturdugumuz tüm hashmapleri ArrayList e atıp geri dönüyoruz(return).

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> messagelist = new ArrayList<HashMap<String, String>>();
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }

                messagelist.add(map);
            } while (cursor.moveToNext());
        }
        db.close();
        // return kitap liste
        return messagelist;
    }
    public int getRowCount() {
        // Tablodaki row sayısını geri döner.
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();
        // return row count
        return rowCount;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
