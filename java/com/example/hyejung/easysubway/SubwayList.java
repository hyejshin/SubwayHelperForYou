package com.example.hyejung.easysubway;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class SubwayList extends Activity {

    Bundle bundle = new Bundle();
    String subwayName;
    String str_subwayName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = this.getIntent().getExtras();

        str_subwayName = bundle.getString("subwayname");
        getSubwayName();
    }


    public void getSubwayName(){
        String subwayName = "", subwayLine = "", it_name = "", str_it_line = "";
        double sum_latitude = 0, sum_longitude = 0;
        double average_latitude = 0.0, average_longitude = 0.0;
        String ele_longitude = "";
        String ele_latitude = "";

        String [] all_elevator_longitudes = new String[20];
        String [] all_elevator_latitudes = new String[20];
        String [] it_line = new String[10];
        int elevator_num = 0 ,line_num = 0;

        try{
            AssetDatabaseOpenHelper assetDBHelper = new AssetDatabaseOpenHelper(this);
            assetDBHelper.setDB_name("SubwayLocationMap.db");
            SQLiteDatabase sqLiteDatabase = assetDBHelper.openDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM subwayPosition where subwayName = ?", new String[]{str_subwayName});
            Log.d("MyApp", "cnt: " + cursor.getCount());

            int i=0;
            if(cursor.getCount() == 0){
                Toast.makeText(this,str_subwayName + " 해당 DB가 없습니다.",Toast.LENGTH_SHORT).show();
            }
            else {
                while (cursor.moveToNext()) {
                    subwayName = cursor.getString(cursor.getColumnIndex("subwayName"));

                    if(subwayLine != "") {
                        subwayLine += ",";
                    }
                    subwayLine += cursor.getString(cursor.getColumnIndex("subwayLine"));

                    if(ele_longitude != "" && ele_latitude != "") {
                        ele_longitude += ",";
                        ele_latitude += ",";
                    }
                    ele_longitude += cursor.getString(cursor.getColumnIndex("elevatorLongitude"));
                    ele_latitude += cursor.getString(cursor.getColumnIndex("elevatorLatitude"));

                    it_name = subwayName;
                    double longitude = cursor.getDouble(cursor.getColumnIndex("longitude"));
                    double latitude = cursor.getDouble(cursor.getColumnIndex("latitude"));

                    sum_longitude += longitude;
                    sum_latitude += latitude;
                   i++;
                }

                average_latitude = sum_latitude / i;
                average_longitude = sum_longitude / i;

                String[] elevator_longitude = ele_longitude.split(",");
                String[] elevator_latitude = ele_latitude.split(",");
                String[] subway_line = subwayLine.split(",");

                for(int j = 0; j < elevator_longitude.length; j++, elevator_num++) {
                    all_elevator_longitudes[elevator_num] = elevator_longitude[elevator_num];
                    all_elevator_latitudes[elevator_num] = elevator_latitude[elevator_num];
                }
                for(int j = 0; j < subway_line.length; j++, line_num++) {
                    it_line[line_num] = subway_line[line_num];
                }
            }
            cursor.close();
            sqLiteDatabase.close();
        }catch(SQLiteException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        if(!subwayName.equals("")) {

            Intent intent = new Intent(this, NMapViewer.class);
            intent.putExtra("subwayName", it_name);
            intent.putExtra("subwayLine", it_line);
            intent.putExtra("longitude", Double.toString(average_longitude));
            intent.putExtra("latitude", Double.toString(average_latitude));
            intent.putExtra("elevator_longitude", all_elevator_longitudes);
            intent.putExtra("elevator_latitude", all_elevator_latitudes);
            intent.putExtra("elevator_num", elevator_num);
            intent.putExtra("line_num", line_num);
            startActivity(intent);
        }
        else
            Toast.makeText(this, "해당역 정보가 없습니다.", Toast.LENGTH_LONG).show();

        finish();
    }

}
