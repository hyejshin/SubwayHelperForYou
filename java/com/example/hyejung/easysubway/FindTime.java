package com.example.hyejung.easysubway;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class FindTime extends Activity {
    TextView tv;
    Map line = new HashMap();
    Map stationCode = new HashMap();
    Map nextStation = new HashMap();
    Map alpha = new HashMap();

    int week, hour, min;
    int w[] = {0, 3, 1, 1, 1, 1, 1, 2};
    DrawInfo info;
    String timeOption, userOption;
    int totalTransfer;
    boolean error = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupSubwayLine();
        setupStationCode();
        setupNextStation();

        info = new DrawInfo();
        info.resetTime();
        week = info.getWeek();
        hour = info.getHour();
        min = info.getMinute();
        timeOption = info.getTimeOption();
        totalTransfer = info.getTotalTransfer();
        userOption = info.getUserOption();

        alpha.put("person", 5);
        alpha.put("elevator", 5);
        alpha.put("wheelchair", 10);

        if(timeOption.equals("첫차시간")){
            hour = 5;
            min = 00;
        }

        for(int i=0; i<totalTransfer-1; i++) {
            String name = info.getName(i);
            String id = info.getId(i);
            String L = line.get(id).toString();
            String code = "";

            if(stationCode.containsKey(name+L)){
                code = stationCode.get(name+L).toString();
            }
            else {
                error = true;
                break;
            }

            int direction = getDirection(name + L, info.getNextStation(i));
            findTime(code, w[week], hour, min, direction, L);

            int travelTime = info.stringToInt(info.getTravelTime(i));
            String time = info.getStartTime(i);
            hour = info.stringToInt(time.substring(0, 2));
            min = info.stringToInt(time.substring(3, 5));

            addTime(travelTime);
            info.setArriveTime(info.setTime(hour, min));
            addTime((Integer) alpha.get(userOption));
        }

        if(error) {
            Toast.makeText(this, "선택한 역 정보가 없습니다.", Toast.LENGTH_LONG).show();
            finish();
        }
        else
            nextActivity();
    }

    public void findTime(String code, int week, int hour, int min, int dir, String line){
        int h=0, m=0;
        String dbName = "SubwayTime"+line+".sqlite";
        String str = "";
        if(dir==1)
            str = " and ((hour == "+hour+" and tData1 >= "+min+") or hour > "+hour+")";
        else
            str = " and ((hour == "+hour+" and tData2 >= "+min+") or hour > "+hour+")";
        try {
            AssetDatabaseOpenHelper assetDBHelper = new AssetDatabaseOpenHelper(this);
            assetDBHelper.setDbName(dbName);
            SQLiteDatabase sqLiteDatabase = assetDBHelper.openDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM tb_tTimeData " +
                    "Where stationCode == "+code+" and week == "+week+
                    str+";", null);

            int count = 0;
            while (cursor.moveToNext()) {
                count++;
                String stationCode = cursor.getString(cursor.getColumnIndex("stationCode"));

                h = cursor.getInt(cursor.getColumnIndex("hour"));
                if(dir==1)
                    m = cursor.getInt(cursor.getColumnIndex("tData1"));
                else
                    m = cursor.getInt(cursor.getColumnIndex("tData2"));

                if(count == 1 && !timeOption.equals("막차시간"))
                    break;
            }

            info.setStartTime(info.setTime(h, m));

            cursor.close();
            sqLiteDatabase.close();

        } catch (SQLiteException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void nextActivity() {
        Intent it = new Intent(this, DisplayPath.class);
        startActivity(it);
        finish();
    }

    public int getDirection(String key, String next){
        String station = nextStation.get(key).toString();
        if(station.equals(next))
            return 1;
        else
            return 2;
    }

    public void addTime(int m){
        if(min+m >= 60){
            min = min+m-60;
            hour += 1;
        }else
            min += m;
    }

    public void setupSubwayLine(){
        line.put("1001", "01");
        line.put("1002", "02");
        line.put("1003", "03");
        line.put("1004", "04");
        line.put("1005", "05");
        line.put("1006", "06");
        line.put("1007", "07");
        line.put("1008", "08");
        line.put("1009", "09");
        line.put("1061", "10");
        line.put("1063", "10");
        line.put("1065", "공항철도");
        line.put("1077", "신분당선");
        line.put("1075", "분당");
    }

    /*
잠실 2070
총신대입구(이수) 4025
동작 4026*/
    public void setupStationCode() {
        stationCode.put("서울01", "1034");
        stationCode.put("서울04", "4030");
        stationCode.put("숙대입구04", "4031");
        stationCode.put("사당02", "2060");
        stationCode.put("사당04", "4024");
        stationCode.put("강남02", "2022");
        stationCode.put("낙성대02", "2059");
        stationCode.put("남영01", "1034");
        stationCode.put("이촌04", "4027");
        stationCode.put("이촌10", "10306");
        stationCode.put("용산01", "1036");
        stationCode.put("용산10", "10305");
        stationCode.put("신도림01", "1041");
        stationCode.put("신도림02", "2052");
        stationCode.put("잠실02", "2070");
        stationCode.put("총신대입구", "4052");
        stationCode.put("동작", "4026");
        stationCode.put("동대문역사문화공원04", "4035");
        stationCode.put("동대문역사문화공원02", "2081");
    }

    public void setupNextStation() {
        //direction: 시계방향(외선), 상행
        nextStation.put("서울01", "시청");
        nextStation.put("서울04", "회현");
        nextStation.put("숙대입구04", "서울");
        nextStation.put("낙성대02", "서울대입구");
        nextStation.put("사당04", "총신대입구(이수)");
        nextStation.put("사당02", "낙성대");
        nextStation.put("강남02", "교대");
        nextStation.put("잠실02", "신천");
        nextStation.put("남영01", "서울");
        nextStation.put("신도림01", "영등포");
        nextStation.put("신도림02", "문래");
        nextStation.put("총신대입구(이수)04", "동작");
        nextStation.put("이촌04", "신용산");
        nextStation.put("이촌10", "효창");
        nextStation.put("용산01", "남영");
        nextStation.put("용산10", "이촌");
        nextStation.put("동대문역사문화공원04", "동대문");
        nextStation.put("동대문역사문화공원02", "을지로4가");

    }
}
