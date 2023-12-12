package com.example.groupassignment;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLiteAdapter {
    public static final String MYDATABASE_TABLE = "SHOPPING_LIST_TABLE";
    public static final String MYDATABSE_NAME = "SHOPPING_LIST_TABLE";
    public static final int MYDATASE_VERSION = 1;
    public static final String KEY_CONTENT = "Item";

    public static final String SCRIPT_CREATE_DATABASE = "create table "+ MYDATABASE_TABLE+
            " (id INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_CONTENT +" text not null);";

    private Context context;
    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase sqlLiteDatabase;

    //constructor
    public SQLiteAdapter(Context c){
        context = c;
    }

    //write data
    public SQLiteAdapter openToWrite() throws android.database.SQLException{
        sqLiteHelper = new SQLiteHelper(context, MYDATABSE_NAME, null, MYDATASE_VERSION);

        sqlLiteDatabase = sqLiteHelper.getWritableDatabase();
        return this;
    }

    //read data
    public SQLiteAdapter openToRead() throws android.database.SQLException{
        sqLiteHelper = new SQLiteHelper(context, MYDATABSE_NAME, null, MYDATASE_VERSION);
        sqlLiteDatabase = sqLiteHelper.getReadableDatabase();

        return this;
    }

    //insertion into ONE column table
    public long insert(String content){
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_CONTENT, content);

        return sqlLiteDatabase.insert(MYDATABASE_TABLE, null, contentValues);
    }

    public Cursor getCursor() {
        String[] columns = new String[]{KEY_CONTENT};
        return sqlLiteDatabase.query(MYDATABASE_TABLE, columns, null, null, null, null, null);
    }

    public int deleteItem(String content) {
        return sqlLiteDatabase.delete(MYDATABASE_TABLE,
                KEY_CONTENT + "=?", new String[]{content});
    }

    public void close(){
        sqLiteHelper.close();
    }

    public int deleteAll(){
        return sqlLiteDatabase.delete(MYDATABASE_TABLE, null, null);
    }

    public class SQLiteHelper extends SQLiteOpenHelper {


        public SQLiteHelper(@Nullable Context context, @Nullable String name,
                            @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        //create database
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SCRIPT_CREATE_DATABASE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            sqlLiteDatabase.execSQL(SCRIPT_CREATE_DATABASE);
        }
    }
}
