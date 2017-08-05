package sust.hackathon.helper.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import sust.hackathon.helper.model.DataTemp;

/**
 * Created by joy on 8/5/17.
 */

public class MyDBFunction extends SQLiteOpenHelper {

    private static final String DATABSE_NAME = "mydb";
    private static final String TABLE_NAME = "mytab";

    private static final String TAB_ID = "_id";
    private static final String TAB_NAME = "name";
    private static final String TAB_Details = "details";


    public MyDBFunction(Context context) {
        super(context, DATABSE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String s = "CREATE TABLE " + TABLE_NAME + " (" + TAB_ID + " INTEGER PRIMARY KEY, " + TAB_NAME + " TEXT, " + TAB_Details + " TEXT)";
        db.execSQL(s);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // ---- ---- adding data to database --- -----


    public void addingData(DataTemp dataTemp){
        SQLiteDatabase sqLiteDatabase =this.getWritableDatabase();

        ContentValues contentValues=new ContentValues();
        contentValues.put(TAB_NAME,dataTemp.getName());
        contentValues.put(TAB_Details,dataTemp.getDetails());

        sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
        sqLiteDatabase.close();
    }
    public Cursor my_data(){
        SQLiteDatabase database =this.getReadableDatabase();
        String q="SELECT * FROM "+TABLE_NAME;
        Cursor c =database.rawQuery(q,null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public int delete_data(String bday) {

        SQLiteDatabase s = this.getWritableDatabase();
        return s.delete(TABLE_NAME, TAB_ID + " = ?", new String[]{bday});

    }
}

