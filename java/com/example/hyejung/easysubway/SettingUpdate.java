package com.example.hyejung.easysubway;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class SettingUpdate extends Activity {
    SQLiteDatabase sqlitedb;
    DBManager dbmanager;
    TextView tv;
    LinearLayout layout;
    int totalSubwayStation;
    int totalFrCode = 0;
    String STATIONS[] ={"서울", "숙대입구", "낙성대", "사당", "강남", "잠실", "신도림", "남영", "총신대입구(이수)", "이촌", "용산"};
    String FR_CODE[] = new String[300];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_update);

        setTitle("지하철 정보");

        tv = (TextView) findViewById(R.id.tv);
        layout = (LinearLayout) findViewById(R.id.view);
        totalSubwayStation = STATIONS.length;

    }

    public void requestAndParsing(View v) {
        String serviceURI, stationName, frCode, strUrl;
        for(int i=0; i<totalSubwayStation; i++) {
            serviceURI = "http://openapi.seoul.go.kr:8088/694367775373686937314b4b4d437a/xml/SearchInfoBySubwayNameService/1/5/";
            stationName = STATIONS[i];
            strUrl = serviceURI + URLEncoder.encode(stationName);

            new DownloadWebpageTask1().execute(strUrl);
        }

        getFrCode();

        for(int i=0; i<totalFrCode; i++) {
            serviceURI = "http://openapi.seoul.go.kr:8088/694367775373686937314b4b4d437a/xml/SearchSTNInfoByFRCodeService/1/5/";
            frCode = FR_CODE[i];
            strUrl = serviceURI + frCode;

            new DownloadWebpageTask2().execute(strUrl);
        }
    }

    public void getFrCode(){
        try {
            dbmanager = new DBManager(this);
            sqlitedb = dbmanager.getReadableDatabase();
            Cursor cursor = sqlitedb.query("subwayStationInfo", null, "stationCode is not null", null, null, null, null);

            int i = 0;
            while(cursor.moveToNext()) {
                String frCode = cursor.getString(cursor.getColumnIndex("frCode"));
                FR_CODE[i] = frCode;
                i++;
            }
            totalFrCode = i;

            cursor.close();
            sqlitedb.close();
            dbmanager.close();

        } catch(SQLiteException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private class DownloadWebpageTask1 extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                return (String) downloadUrl((String) urls[0]);
            } catch (IOException e) {
                return "다운로드 실패";
            }
        }

        protected void onPostExecute(String result) {
            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();

                xpp.setInput(new StringReader(result));
                int eventType = xpp.getEventType();
                boolean bSet = false;
                String field = "";
                String stationCode = "", frCode = "", stationName = "", lineNum = "";

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_DOCUMENT) {
                        ;
                    } else if (eventType == XmlPullParser.START_TAG) {
                        String tag_name = xpp.getName();
                        if (tag_name.equals("STATION_CD") || tag_name.equals("STATION_NM") ||
                                tag_name.equals("LINE_NUM") || tag_name.equals("FR_CODE")) {
                            field = tag_name;
                            bSet = true;
                        }
                    } else if (eventType == XmlPullParser.TEXT) {
                        if (bSet) {
                            String content = xpp.getText();
                            if(field.equals("STATION_CD"))
                                stationCode = content;
                            else if(field.equals("STATION_NM"))
                                stationName = content;
                            else if(field.equals("LINE_NUM"))
                                lineNum = content;
                            else if(field.equals("FR_CODE")) {
                                frCode = content;
                                if(checkExistData("subwayStationInfo", stationCode))
                                    updateStationInfo(stationCode, frCode, stationName, lineNum);
                                else
                                    storeStationInfo(stationCode, frCode, stationName, lineNum);
                            }
                            bSet = false;
                        }
                    } else if (eventType == XmlPullParser.END_TAG) {
                        ;
                    }
                    eventType = xpp.next();
                }
            } catch (Exception e) {
                tv.setText(e.getMessage());
            }
        }

        private String downloadUrl(String myurl) throws IOException {

            HttpURLConnection conn = null;
            try{
                URL url = new URL(myurl);
                conn = (HttpURLConnection) url.openConnection();
                BufferedInputStream buf = new BufferedInputStream(conn.getInputStream());
                BufferedReader bufreader = new BufferedReader(new InputStreamReader(buf, "utf-8"));
                String line = null;
                String page = "";
                while((line = bufreader.readLine()) != null) {
                    page += line;
                }

                return page;
            } finally {
                conn.disconnect();
            }
        }
    }

    private class DownloadWebpageTask2 extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                return (String) downloadUrl((String) urls[0]);
            } catch (IOException e) {
                return "다운로드 실패";
            }
        }

        protected void onPostExecute(String result) {
            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();

                xpp.setInput(new StringReader(result));
                int eventType = xpp.getEventType();
                boolean bSet = false;
                String field = "";
                String stationCode = "", stationName = "", englishName = "", lineNum = "", tel = "", nursing = "", elevator = "", wheelchair = "";

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_DOCUMENT) {
                        ;
                    } else if (eventType == XmlPullParser.START_TAG) {
                        String tag_name = xpp.getName();
                        if (tag_name.equals("STATION_CD") || tag_name.equals("STATION_NM") || tag_name.equals("STATION_NM_ENG") ||
                                tag_name.equals("LINE_NUM") || tag_name.equals("TEL") || tag_name.equals("NURSING") ||
                                tag_name.equals("ELEVATOR") || tag_name.equals("WHEELCHAIR")) {
                            field = tag_name;
                            bSet = true;
                        }
                    } else if (eventType == XmlPullParser.TEXT) {
                        if (bSet) {
                            String content = xpp.getText();
                            if(field.equals("STATION_CD"))
                                stationCode = content;
                            else if(field.equals("STATION_NM"))
                                stationName = content;
                            else if(field.equals("STATION_NM_ENG"))
                                englishName = content;
                            else if(field.equals("LINE_NUM"))
                                lineNum = content;
                            else if(field.equals("TEL"))
                                tel = content;
                            else if(field.equals("NURSING"))
                                nursing = content;
                            else if(field.equals("ELEVATOR"))
                                elevator = content;
                            else if(field.equals("WHEELCHAIR")) {
                                wheelchair = content;
                                if(checkExistData("subwayFacilityInfo", stationCode))
                                    updateFacilityInfo(stationCode, stationName, englishName, lineNum, tel, nursing, elevator, wheelchair);
                                else
                                    storeFacilityInfo(stationCode, stationName, englishName, lineNum, tel, nursing, elevator, wheelchair);
                            }
                            bSet = false;
                        }
                    } else if (eventType == XmlPullParser.END_TAG) {
                        ;
                    }
                    eventType = xpp.next();
                }
            } catch (Exception e) {
                tv.setText(e.getMessage());
            }
        }

        private String downloadUrl(String myurl) throws IOException {

            HttpURLConnection conn = null;
            try{
                URL url = new URL(myurl);
                conn = (HttpURLConnection) url.openConnection();
                BufferedInputStream buf = new BufferedInputStream(conn.getInputStream());
                BufferedReader bufreader = new BufferedReader(new InputStreamReader(buf, "utf-8"));
                String line = null;
                String page = "";
                while((line = bufreader.readLine()) != null) {
                    page += line;
                }

                return page;
            } finally {
                conn.disconnect();
            }
        }
    }

    public boolean checkExistData(String tableName, String stationCode){
        SQLiteDatabase sqlitedb;
        DBManager dbmanager;
        boolean exist = true;
        Cursor cursor;

        try {
            dbmanager = new DBManager(this);
            sqlitedb = dbmanager.getReadableDatabase();
            String query = "Select * FROM " + tableName + " WHERE stationCode =  \"" + stationCode + "\"";
            cursor = sqlitedb.rawQuery(query, null);

            if(cursor.moveToNext())
                exist = true;
            else
                exist = false;

        } catch(SQLiteException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        if(exist)
            return true;
        else
            return false;
    }

    public void storeStationInfo(String stationCode, String frCode, String stationName, String lineNum){
        tv.setText("success");
        try {
            dbmanager = new DBManager(this);
            sqlitedb = dbmanager.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("stationCode", stationCode);
            values.put("frCode", frCode);
            values.put("stationName", stationName);
            values.put("lineNum", lineNum);

            sqlitedb.insert("subwayStationInfo", null, values);
            sqlitedb.close();
            dbmanager.close();
        } catch (SQLiteException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void updateStationInfo(String stationCode, String frCode, String stationName, String lineNum){
        tv.setText("updated");
        try {
            dbmanager = new DBManager(this);
            sqlitedb = dbmanager.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("stationCode", stationCode);
            values.put("frCode", frCode);
            values.put("stationName", stationName);
            values.put("lineNum", lineNum);

            sqlitedb.update("subwayStationInfo", values, "stationCode = ?", new String[]{stationName});
            sqlitedb.close();
            dbmanager.close();
        } catch (SQLiteException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void storeFacilityInfo(String stationCode, String stationName, String englishName, String lineNum, String tel, String nursing, String elevator, String wheelchair){
        tv.setText("success");
        try {
            dbmanager = new DBManager(this);
            sqlitedb = dbmanager.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("stationCode", stationCode);
            values.put("stationName", stationName);
            values.put("lineNum", lineNum);
            values.put("tel", tel);
            values.put("nursing", nursing);
            values.put("elevator", elevator);
            values.put("wheelchair", wheelchair);

            sqlitedb.insert("subwayFacilityInfo", null, values);
            sqlitedb.close();
            dbmanager.close();
        } catch (SQLiteException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        storeEnglishName(stationName, englishName);
    }

    public void storeEnglishName(String stationName, String englishName){
        try {
            dbmanager = new DBManager(this);
            sqlitedb = dbmanager.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("stationName", stationName);
            values.put("englishName", englishName);

            sqlitedb.insert("stationEnglishName", null, values);
            sqlitedb.close();
            dbmanager.close();
        } catch (SQLiteException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void updateFacilityInfo(String stationCode, String stationName, String englishName, String lineNum, String tel, String nursing, String elevator, String wheelchair){
        tv.setText("updated");
        try {
            dbmanager = new DBManager(this);
            sqlitedb = dbmanager.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("stationCode", stationCode);
            values.put("stationName", stationName);
            values.put("lineNum", lineNum);
            values.put("tel", tel);
            values.put("nursing", nursing);
            values.put("elevator", elevator);
            values.put("wheelchair", wheelchair);

            sqlitedb.update("subwayFacilityInfo", values, "stationCode = ?", new String[]{stationName});
            sqlitedb.close();
            dbmanager.close();
        } catch (SQLiteException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        updateEnglishName(stationName, englishName);
    }

    public void updateEnglishName(String stationName, String englishName){
        try {
            dbmanager = new DBManager(this);
            sqlitedb = dbmanager.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("stationName", stationName);
            values.put("englishName", englishName);

            sqlitedb.update("stationEnglishName", values, "stationName = ?", new String[]{stationName});
            sqlitedb.close();
            dbmanager.close();
        } catch (SQLiteException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void displayStationInfo(View v){
        try {
            dbmanager = new DBManager(this);
            sqlitedb = dbmanager.getReadableDatabase();
            Cursor cursor = sqlitedb.query("subwayStationInfo", null, "stationCode is not null", null, null, null, null);

            layout.removeAllViews();
            int count = 0;
            while (cursor.moveToNext()) {
                count++;
                String stationCode = cursor.getString(0);
                String frCode = cursor.getString(1);
                String stationName = cursor.getString(2);
                String lineNum = cursor.getString(3);

                LinearLayout layout_list = new LinearLayout(this);
                layout_list.setOrientation(LinearLayout.VERTICAL);
                layout_list.setPadding(20, 10, 20, 10);

                TextView tv_list = new TextView(this);
                tv_list.setText(stationCode);
                tv_list.setTextSize(20);
                layout_list.addView(tv_list);

                TextView tv_list2 = new TextView(this);
                tv_list2.setText(lineNum + "호선\n");
                tv_list2.append(stationName + "역\n");
                tv_list2.append("역외부코드: " + frCode + "\n");
                tv_list2.append("count: " + count + "\n");

                layout_list.addView(tv_list2);

                layout.addView(layout_list);
            }

            cursor.close();
            sqlitedb.close();
            dbmanager.close();

        } catch (SQLiteException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void displayFacilityInfo(View v){
        try {
            dbmanager = new DBManager(this);
            sqlitedb = dbmanager.getReadableDatabase();
            Cursor cursor = sqlitedb.query("subwayFacilityInfo", null, "stationName is not null", null, null, null, null);

            layout.removeAllViews();
            int count = 0;
            while (cursor.moveToNext()) {
                count++;
                String stationCode = cursor.getString(cursor.getColumnIndex("stationCode"));
                String stationName = cursor.getString(cursor.getColumnIndex("stationName"));
                String lineNum = cursor.getString(cursor.getColumnIndex("lineNum"));
                String tel = cursor.getString(cursor.getColumnIndex("tel"));
                String nursing = cursor.getString(cursor.getColumnIndex("nursing"));
                String elevator = cursor.getString(cursor.getColumnIndex("elevator"));
                String wheelchair = cursor.getString(cursor.getColumnIndex("wheelchair"));

                LinearLayout layout_list = new LinearLayout(this);
                layout_list.setOrientation(LinearLayout.VERTICAL);
                layout_list.setPadding(20, 10, 20, 10);

                TextView tv_list = new TextView(this);
                tv_list.setText(stationCode);
                tv_list.setTextSize(20);
                layout_list.addView(tv_list);

                TextView tv_list2 = new TextView(this);
                tv_list2.setText(stationName + "역\n");
                tv_list2.append(lineNum + "호선\n");
                tv_list2.append("역무실번호: " + tel + "\n");
                tv_list2.append("수유실: " + nursing + "\n");
                tv_list2.append("엘레베이터: " + elevator + "\n");
                tv_list2.append("휠체어: " + wheelchair + "\n");
                tv_list2.append("count: " + count + "\n");

                layout_list.addView(tv_list2);

                layout.addView(layout_list);
            }

            cursor.close();
            sqlitedb.close();
            dbmanager.close();

        } catch (SQLiteException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void displayEnglishName(View v){
        try {
            dbmanager = new DBManager(this);
            sqlitedb = dbmanager.getReadableDatabase();
            Cursor cursor = sqlitedb.query("stationEnglishName", null, "stationName is not null", null, null, null, null);

            layout.removeAllViews();
            int count = 0;
            while (cursor.moveToNext()) {
                count++;
                String stationName = cursor.getString(cursor.getColumnIndex("stationName"));
                String englishName = cursor.getString(cursor.getColumnIndex("englishName"));

                LinearLayout layout_list = new LinearLayout(this);
                layout_list.setOrientation(LinearLayout.VERTICAL);
                layout_list.setPadding(20, 10, 20, 10);

                TextView tv_list = new TextView(this);
                tv_list.setText(stationName);
                tv_list.setTextSize(20);
                layout_list.addView(tv_list);

                TextView tv_list2 = new TextView(this);
                tv_list2.setText(englishName + "\n");
                tv_list2.append("count: " + count + "\n");

                layout_list.addView(tv_list2);

                layout.addView(layout_list);
            }

            cursor.close();
            sqlitedb.close();
            dbmanager.close();

        } catch (SQLiteException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}