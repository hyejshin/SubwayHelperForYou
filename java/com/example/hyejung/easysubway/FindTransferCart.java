package com.example.hyejung.easysubway;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by HyeJung on 2016-03-11.
 */
public class FindTransferCart extends Activity{
    DrawInfo info;
    Map line = new HashMap();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        info = new DrawInfo();
        setupSubwayLine();

        String beforeStation, nextStation, beforeId, nextId, beforeLine, nextLine;
        int totalTransfer = info.getTotalTransfer();
        String userOption = info.getUserOption();

        for(int i=1; i<totalTransfer-1; i++) {
            beforeStation = info.getBeforeStation(i - 1);
            nextStation = info.getNextStation(i);
            beforeId = info.getBeforeId(i - 1);
            nextId = info.getId(i);
            beforeLine = line.get(beforeId.substring(0, 4)).toString();
            nextLine = line.get(nextId.substring(0, 4)).toString();
            getCartInfo(beforeStation, nextStation, beforeLine, nextLine, userOption);
        }

        Intent it = new Intent(this, FindTime.class);
        startActivity(it);
        finish();
    }

    public void getCartInfo(String beforeStation, String nextStation, String beforeLine, String nextLine, String userOption){
        try {
            AssetDatabaseOpenHelper assetDBHelper = new AssetDatabaseOpenHelper(this);
            assetDBHelper.setDbName("TransferNavigator.db");
            SQLiteDatabase sqLiteDatabase = assetDBHelper.openDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM navigatorinfo " +
                    "Where previouskorean == \"" + beforeStation + "\" and nextkorean == \"" + nextStation + "\" and " +
                    "previousline == \"" + beforeLine + "\" and nextline == \"" + nextLine + "\";", null);

            String cart = "";
            if (cursor.moveToNext()) {
                if(userOption.equals("person"))
                    cart = cursor.getString(cursor.getColumnIndex("person_door"));
                else if(userOption.equals("elevator"))
                    cart = cursor.getString(cursor.getColumnIndex("elevator_close_door"));
                else if(userOption.equals("wheelchair"))
                    cart = cursor.getString(cursor.getColumnIndex("wheelchair_door"));
            }
            info.setCartNo(cart);

            cursor.close();
            sqLiteDatabase.close();
        } catch (SQLiteException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void setupSubwayLine(){
        line.put("1001", "1");
        line.put("1002", "2");
        line.put("1003", "3");
        line.put("1004", "4");
        line.put("1005", "5");
        line.put("1006", "6");
        line.put("1007", "7");
        line.put("1008", "8");
        line.put("1009", "9");
        line.put("1061", "중앙");
        line.put("1063", "중앙");
        line.put("1065", "공항철도");
        line.put("1077", "신분당");
        line.put("1075", "분당");
    }
}
