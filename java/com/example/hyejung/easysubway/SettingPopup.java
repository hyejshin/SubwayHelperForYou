package com.example.hyejung.easysubway;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class SettingPopup extends Activity {
    DrawInfo info;
    String userOption;
    Map selectOption = new HashMap();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.setting_popup);

        info = new DrawInfo();
        userOption = info.getUserOption();

        selectOption.put("person", 0);
        selectOption.put("elevator", 1);
        selectOption.put("wheelchair", 2);

        final String [] opt = {"일반사용자", "엘리베이터사용", "휠체어사용자"};
        final Spinner sp = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter adapter = new ArrayAdapter(
                getApplicationContext(), R.layout.spin, opt);
        adapter.setDropDownViewResource(R.layout.spin_dropdown);
        sp.setAdapter(adapter);
        sp.setSelection((Integer)selectOption.get(userOption));

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String option = sp.getSelectedItem().toString();
                if (option.equals("일반사용자"))
                    userOption = "person";
                else if (option.equals("엘리베이터사용"))
                    userOption = "elevator";
                else if(option.equals("휠체어사용자"))
                    userOption = "wheelchair";
                storeInfo();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void storeInfo(){
        try {
            info.setUserOption(userOption);
            DBManager dbmanager = new DBManager(this);
            SQLiteDatabase sqlitedb = dbmanager.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("id", "user");
            values.put("userOption", userOption);
            values.put("visitNum", info.getVisitNum());
            sqlitedb.update("settingInfo", values, "id = ?", new String[]{"user"});
            sqlitedb.close();
            dbmanager.close();
        } catch (SQLiteException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void start(View v){
        Intent it = new Intent(this, MainActivity.class);
        startActivity(it);
        finish();
    }

}
