package com.example.hyejung.easysubway;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import uk.co.senab.photoview.PhotoViewAttacher;

public class DisplayTransferMap extends Activity {

    Map lineColor = new HashMap();
    Map lineName = new HashMap();
    DrawInfo info;
    PhotoViewAttacher mAttacher;
    String userOption;
    String transferkorean ="", transferenglish = "",previouskorean ="", previousenglish = "", previousline ="";
    String  nextkorean ="", nextenglish = "",nextline = "", explainstep ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_route);

        if(setIntent()) {
            setupColor();
            setupLineName();
            set_headLayer_color();

            setMapImage();

            ImageView img = (ImageView) findViewById(R.id.img_viewpager_childimage);

            mAttacher = new PhotoViewAttacher(img);

            String transfer_subway_name = previousenglish + "_" + nextenglish;
            String line_number_image = "@drawable/" + transfer_subway_name;
            int transfer_subwayID = getResources().getIdentifier(line_number_image, "drawable", getPackageName());

            img.setImageResource(transfer_subwayID);

            TextView text = (TextView) findViewById(R.id.stepExplain);
            text.setText(explainstep);
        }
    }

    public boolean setIntent(){
        Intent intent = getIntent();
        String previous_subway = intent.getStringExtra("beforeStation");
        String next_subway = intent.getStringExtra("nextStation");
        String previous_subwayline = intent.getStringExtra("beforeLine");
        String next_subwayline = intent.getStringExtra("nextLine");

        info = new DrawInfo();
        userOption = info.getUserOption();

        try{
            AssetDatabaseOpenHelper assetDBHelper = new AssetDatabaseOpenHelper(this);
            assetDBHelper.setDbName("TransferNavigator.db");
            SQLiteDatabase sqLiteDatabase = assetDBHelper.openDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM navigatorinfo where previouskorean = ? AND nextkorean =? AND previousline = ? AND nextline = ?",new String[]{previous_subway,next_subway, previous_subwayline, next_subwayline});
            //Log.d("MyApp", "cnt: " + cursor.getCount());

            if(cursor.getCount() != 0) {
                cursor.moveToNext();
                transferkorean = cursor.getString(cursor.getColumnIndex("transferkorean"));
                transferenglish = cursor.getString(cursor.getColumnIndex("transferenglish"));
                previouskorean = cursor.getString(cursor.getColumnIndex("previouskorean"));
                previousenglish = cursor.getString(cursor.getColumnIndex("previousenglish"));
                previousline = cursor.getString(cursor.getColumnIndex("previousline"));
                nextkorean = cursor.getString(cursor.getColumnIndex("nextkorean"));
                nextenglish = cursor.getString(cursor.getColumnIndex("nextenglish"));
                nextline = cursor.getString(cursor.getColumnIndex("nextline"));
                if(userOption.equals("elevator"))
                    explainstep = cursor.getString(cursor.getColumnIndex("explainstep2"));
                else
                    explainstep = cursor.getString(cursor.getColumnIndex("explainstep"));

            }
            else{
                Toast.makeText(this, "해당 환승정보가 없습니다.", Toast.LENGTH_SHORT).show();
                return false;
            }
            cursor.close();
            sqLiteDatabase.close();
        }catch(SQLiteException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    public void set_headLayer_color(){
        //레이아웃 바탕 호선 색으로 설정
        String color;
        color = lineColor.get(previousline).toString();
        LinearLayout layout = (LinearLayout)findViewById(R.id.previous_subway_layout);
        layout.setBackgroundColor(Color.parseColor(color));

        color = lineColor.get(nextline).toString();
        layout = (LinearLayout)findViewById(R.id.next_subway_layout);
        layout.setBackgroundColor(Color.parseColor(color));

        //호선 번호 심볼 넣기
        String strlineName;
        strlineName = lineName.get(previousline).toString();
        String line_number_image = "@drawable/line" + strlineName;
        int line_numberID = getResources().getIdentifier(line_number_image,"drawable",getPackageName());

        ImageView imageView = (ImageView)findViewById(R.id.previous_line_symbol);
        imageView.setImageResource(line_numberID);

        strlineName = lineName.get(nextline).toString();
        line_number_image = "@drawable/line" + strlineName;
        line_numberID = getResources().getIdentifier(line_number_image, "drawable", getPackageName());

        imageView = (ImageView)findViewById(R.id.next_line_symbol);
        imageView.setImageResource(line_numberID);

        //전, 후역 이름 넣기
        TextView subway_name = (TextView)findViewById(R.id.previous_subway_name);
        subway_name.setText(previouskorean);

        subway_name = (TextView)findViewById(R.id.transfer_subway_name);
        subway_name.setText(transferkorean);

        subway_name = (TextView)findViewById(R.id.next_subway_name);
        subway_name.setText(nextkorean);
    }

    public void setMapImage(){
        ImageView img= (ImageView)findViewById(R.id.img_viewpager_childimage);

        mAttacher = new PhotoViewAttacher(img);

        String transfer_subway_name = previousenglish + "_" + nextenglish;
        String line_number_image = "@drawable/" + transfer_subway_name;
        int transfer_subwayID = getResources().getIdentifier(line_number_image, "drawable", getPackageName());

        img.setImageResource(transfer_subwayID);
    }

    public void setupColor(){
        lineColor.put("1", "#00498B");
        lineColor.put("2", "#009246");
        lineColor.put("3", "#F36630");
        lineColor.put("4", "#00A2D1");
        lineColor.put("5", "#A064A3");
        lineColor.put("6", "#9E4510");
        lineColor.put("7", "#5D6519");
        lineColor.put("8", "#D6406A");
        lineColor.put("9", "#8E764B");
        lineColor.put("중앙", "#72C7A6");
        lineColor.put("공항철도", "#006D9D");
        lineColor.put("신분당", "#BB1833");
    }

    //호선 심볼 가지고 오기
    public void setupLineName(){
        lineName.put("1", "1");
        lineName.put("2", "2");
        lineName.put("3", "3");
        lineName.put("4", "4");
        lineName.put("5", "5");
        lineName.put("6", "6");
        lineName.put("7", "7");
        lineName.put("8", "8");
        lineName.put("9", "9");
        lineName.put("중앙", "joongang");
        lineName.put("공항철도", "gonghang");
        lineName.put("신분당", "newbundang");
    }
}

