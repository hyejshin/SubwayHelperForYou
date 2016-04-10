package com.example.hyejung.easysubway;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

public class TransferActivity extends Activity {
    String pathOption;
    String start;
    String arrive;

    private int mWeek, mHour, mMinute;
    Bundle bundle = new Bundle();

    String startStation;
    String arriveStation;
    TextView et_start;
    TextView et_arrive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.transfer_main);

        //setTitle("환승경로 구하기");

        et_start = (TextView)findViewById(R.id.editText1);
        et_arrive = (TextView)findViewById(R.id.editText2);

        bundle = this.getIntent().getExtras();
        start = (bundle.getString("start"));
        arrive = (bundle.getString("finish"));

        et_start.setText(start);
        startStation = start;    //et_start.getText().toString();
        et_arrive.setText(arrive);
        arriveStation = et_arrive.getText().toString();


        final String [] opt = {"최소환승", "최단거리"};
        final Spinner sp = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter adapter = new ArrayAdapter(
                getApplicationContext(), R.layout.spin, opt);
        adapter.setDropDownViewResource(R.layout.spin_dropdown);
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pathOption = sp.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

//    public void settingUpdate(View v){
//        Intent it = new Intent(this, SettingUpdate.class);
//        startActivity(it);
//        finish();
//    }

    public void findPath(View v){

        DrawInfo info = new DrawInfo();
        DatePickerDialogActivity();
        info.setWeek(mWeek);
        info.setHour(mHour);
        info.setMinute(mMinute);
        info.setTimeOption("출발시간");

        Intent it = new Intent(TransferActivity.this, FindPath.class);
        it.putExtra("it_start", startStation);
        it.putExtra("it_arrive", arriveStation);
        it.putExtra("it_pathOption", pathOption);
        startActivity(it);
        finish();
    }

    public void DatePickerDialogActivity(){
        final Calendar c = Calendar.getInstance();
        mWeek = c.get(Calendar.DAY_OF_WEEK);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
    }
}
