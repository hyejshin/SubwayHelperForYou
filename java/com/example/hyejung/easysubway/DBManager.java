package com.example.hyejung.easysubway;

/**
 * Created by HyeJung on 2016-01-27.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBManager extends SQLiteOpenHelper {

    public DBManager(Context context) {
        super(context, "easySubway", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE subwayStationInfo (stationCode text, frCode text, stationName text, lineNum text);");
        db.execSQL("CREATE TABLE subwayFacilityInfo (stationCode text, stationName text, lineNum text, tel text, " +
                "nursing text, elevator text, wheelchair text);");
        db.execSQL("CREATE TABLE stationEnglishName (stationName text, englishName text);");
        db.execSQL("CREATE TABLE settingInfo (id text, userOption text, visitNum integer);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS subwayStationInfo");
        db.execSQL("DROP TABLE IF EXISTS create table subwayFacilityInfo");
        db.execSQL("DROP TABLE IF EXISTS stationEnglishName");
        db.execSQL("DROP TABLE IF EXISTS settingInfo");

        onCreate(db);
    }
}