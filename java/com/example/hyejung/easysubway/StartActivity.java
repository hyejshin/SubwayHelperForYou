package com.example.hyejung.easysubway;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

public class StartActivity  extends Activity {
    SQLiteDatabase sqlitedb;
    DBManager dbmanager;

    DrawInfo info;
    int visitNum = 0;
    String userOption = "person";
    boolean firstVisit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

//        startActivity(new Intent(this, SplashActivity.class));

        info = new DrawInfo();

        try {
            dbmanager = new DBManager(this);
            sqlitedb = dbmanager.getReadableDatabase();
            String query = "Select * FROM settingInfo WHERE id = \"user\"";
            Cursor cursor = sqlitedb.rawQuery(query, null);

            if(cursor.moveToNext()) {
                userOption = cursor.getString(cursor.getColumnIndex("userOption"));
                visitNum = cursor.getInt(cursor.getColumnIndex("visitNum"));
            }else{
                firstVisit = true;
            }

            cursor.close();
            sqlitedb.close();
            dbmanager.close();
        } catch(SQLiteException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        storeVisitInfo();
        /*if(visitNum > 0) {
            info.setUserOption(userOption);
            Intent it = new Intent(this, MainActivity.class);
            startActivity(it);
        }
        else {
            Intent it = new Intent(this, SettingPage.class);
            startActivity(it);
        }*/

        Intent it = new Intent(this, SettingPage.class);
        startActivity(it);
    }

    public void storeVisitInfo(){
        info.setUserOption(userOption);
        info.setVisitNum(visitNum + 1);

        try {
            dbmanager = new DBManager(this);
            sqlitedb = dbmanager.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("id", "user");
            values.put("userOption", userOption);
            values.put("visitNum", visitNum + 1);

            if(firstVisit){
                sqlitedb.insert("settingInfo", null, values);
            }else {
                sqlitedb.update("settingInfo", values, "id = ?", new String[]{"user"});
            }

            sqlitedb.close();
            dbmanager.close();
        } catch (SQLiteException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}