package com.example.hyejung.easysubway;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.view.Window;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class DisplayPath extends Activity {
    String pathOption, timeOption, transferStation, userOption;
    int totalTransfer;
    boolean change;

    private static final int DAY = 1, TIME = 2, START_ARRIVAL = 3, METHOD = 4;
    Button b1, b2, b3, b4;
    public int week, hour, minute;
    static final String w[] = {" ","공휴일", "평일", "평일", "평일", "평일", "평일", "토요일"};
    Map weekInt = new HashMap();
    Map line = new HashMap();
    Map transferIdx = new HashMap();
    DrawInfo info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.display_path);

        //setTitle("지하철경로");
        info = new DrawInfo();
        totalTransfer = info.getTotalTransfer();
        userOption = info.getUserOption();

        TextView tv_start = (TextView) findViewById(R.id.tv_start);
        TextView tv_arrive = (TextView) findViewById(R.id.tv_arrive);

        TextView tv_totalTime = (TextView) findViewById(R.id.tv_totalTime);
        int totalTime = calculateTime(info.getArriveTime(totalTransfer-2), info.getStartTime(0));
        tv_totalTime.setText(totalTime+"분 소요");
        TextView tv_totalStation = (TextView) findViewById(R.id.tv_totalStation);
        tv_totalStation.setText("총 "+ info.getTotalStation() +" 정거장");

        setWeekInt();
        setupSubwayLine();

        tv_start.setText(info.getName(0));
        tv_arrive.setText(info.getName(totalTransfer - 1));
        pathOption = info.getPathOption();
        timeOption = info.getTimeOption();
        week = info.getWeek();
        hour = info.getHour();
        minute = info.getMinute();

        b1 = (Button) findViewById(R.id.button1);
        b1.setText(w[week] + " ▽");
        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DAY);
            }
        });

        b2 = (Button) findViewById(R.id.button2);
        b2.setText(info.getTime() + " ▽");
        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(TIME);
            }
        });

        b3 = (Button) findViewById(R.id.button3);
        b3.setText(timeOption + " ▽");
        b3.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                showDialog(START_ARRIVAL);
            }
        });

        b4 = (Button) findViewById(R.id.button4);
        b4.setText(pathOption+" ▽");
        b4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(METHOD);
            }
        });


        String [] opt = new String[totalTransfer-2];
        for(int i=1; i<totalTransfer-1; i++) {
            opt[i - 1] = info.getName(i);
            transferIdx.put(info.getName(i), i);
        }

        final Spinner sp = (Spinner) findViewById(R.id.spinner);
        Button btn = (Button) findViewById(R.id.btn);

        if(userOption.equals("person")) {
            sp.setVisibility(View.GONE);
            btn.setVisibility(View.GONE);
        }else {
            sp.setVisibility(View.VISIBLE);
            btn.setVisibility(View.VISIBLE);
        }

        ArrayAdapter adapter = new ArrayAdapter(
                getApplicationContext(), R.layout.spin, opt);
            adapter.setDropDownViewResource(R.layout.spin_dropdown);
            sp.setAdapter(adapter);

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                transferStation = sp.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        AlertDialog alert;
        AlertDialog.Builder builder;

        switch (id) {
            case DAY:
                final String[] day = {"평일", "토요일", "공휴일"};
                change = false;
                builder = new AlertDialog.Builder(this);
                builder.setTitle("원하는 검색방법을 선택하세요");
                builder.setSingleChoiceItems(day, -1, new
                        DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                b1.setText(day[item] + " ▽");
                                dialog.dismiss();
                                int d = (Integer)weekInt.get(day[item]);
                                if (week != d) {
                                    week = d;
                                    info.setWeek(d);
                                    change = true;
                                }
                                if (change)
                                    findTime();
                            }
                        });
                alert = builder.create();
                return alert;

            case TIME:
                return new TimePickerDialog(this, mTimeSetListener, hour, minute, false);

            case START_ARRIVAL:
                final String[] start_arrival = {"출발시간", "첫차시간", "막차시간"};
                builder = new AlertDialog.Builder(this);
                builder.setTitle("원하는 검색방법을 선택하세요");
                builder.setSingleChoiceItems(start_arrival, -1, new
                        DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                b3.setText(start_arrival[item]+" ▽");
                                dialog.dismiss();
                                String option = start_arrival[item];
                                if (!timeOption.equals(option)) {
                                    timeOption = option;
                                    change = true;
                                    info.setTimeOption(timeOption);
                                }
                                if (change)
                                    findTime();
                            }
                        });
                alert = builder.create();
                return alert;

            case METHOD:
                final String method[] = {"최소환승", "최단거리"};
                change = false;
                builder = new AlertDialog.Builder(this);
                builder.setTitle("원하는 검색방법을 선택하세요");
                builder.setSingleChoiceItems(method, -1, new
                        DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                b4.setText(method[item] + " ▽");
                                dialog.dismiss();
                                String option = method[item];
                                if (!pathOption.equals(option)) {
                                    pathOption = option;
                                    change = true;
                                }
                                if (change)
                                    findPath();
                            }
                        });
                alert = builder.create();
                return alert;
        }
        return null;
    }

    private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener(){
        public void onTimeSet(TimePicker view, int hourOfDay, int min){
            hour = hourOfDay;
            minute = min;
            info.setHour(hour);
            info.setMinute(minute);
            info.setTimeOption("출발시간");
            b2.setText(info.getTime() + " ▽");

            findTime();
        }
    };

    public int calculateTime(String arrive, String start){
        int hr_a, min_a, hr_s, min_s, hr, min;
        hr_a = info.stringToInt(arrive.substring(0, 2));
        min_a = info.stringToInt(arrive.substring(3, 5));
        hr_s = info.stringToInt(start.substring(0, 2));
        min_s = info.stringToInt(start.substring(3,5));

        if(min_a >= min_s){
            hr = hr_a - hr_s;
            min = min_a - min_s;
        }else{
            hr = hr_a - hr_s - 1;
            min = min_a + 60 - min_s;
        }

        return min + 60*hr;
    }



    public void findPath(){
        Intent it = new Intent(this, FindPath.class);

        it.putExtra("it_start", info.getName(0));
        it.putExtra("it_arrive", info.getName(totalTransfer-1));
        it.putExtra("it_pathOption", pathOption);

        startActivity(it);
        finish();
    }

    public void findTime(){
        Intent it = new Intent(this, FindTime.class);
        startActivity(it);
        finish();
    }

    public void displayTransferInfo(View v){
        int idx = (Integer)transferIdx.get(transferStation);
        String beforeStation = info.getBeforeStation(idx - 1);
        String nextStation = info.getNextStation(idx);
        String beforeId = info.getBeforeId(idx - 1);
        String nextId = info.getId(idx);
        String beforeLine = line.get(beforeId.substring(0, 4)).toString();
        String nextLine = line.get(nextId.substring(0, 4)).toString();

        Intent it = new Intent(this, DisplayTransferMap.class);

        it.putExtra("beforeStation", beforeStation);
        it.putExtra("nextStation", nextStation);
        it.putExtra("beforeLine", beforeLine);
        it.putExtra("nextLine", nextLine);

        startActivity(it);
    }

    public void setWeekInt() {
        weekInt.put("평일", 2);
        weekInt.put("토요일", 7);
        weekInt.put("공휴일", 1);
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
    }
}

