package com.example.hyejung.easysubway.popupviews.stationdetailview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.hyejung.easysubway.R;
import com.example.hyejung.easysubway.cellRow.StationTimeDataRow;
import com.example.hyejung.easysubway.DBmodel.AppDataManager;
import com.example.hyejung.easysubway.DBmodel.TimeDataManager;
import java.util.ArrayList;

//역 클릭 이벤트 구현-> 상세정보 버튼-> 기본정보 : 시각표. 첫차.막차. 외부 웹사이트 연결 
public class StationDetailSubView01 extends RelativeLayout implements OnClickListener {
    AppDataManager appManager = null;
    ArrayList<Bundle> arrayList = null;
    Bundle dicStationData = null;
    TextView firstData1 = null;
    TextView firstData2 = null;
    TextView firstTime1 = null;
    TextView firstTime2 = null;
    int iCrossing = 0;
    int iExitDoor = 0;
    int iLineCode = 0;
    int iStationCode = 0;
    int iToilet = 0;
    int iWeekCode = 0;
    ImageView infoImage01 = null;
    ImageView infoImage02 = null;
    ImageView infoImage03 = null;
    TextView infoValue01 = null;
    TextView infoValue02 = null;
    TextView infoValue03 = null;
    TextView lastData1 = null;
    TextView lastData2 = null;
    TextView lastTime1 = null;
    TextView lastTime2 = null;
    ArrayAdapter<Bundle> listData = null;
    ListView listView = null;
    Context mContext = null;
    TimeDataManager timeManager = null;

    public class arrayListAdapter extends ArrayAdapter<Bundle> {
        Context context;

        public arrayListAdapter(Context context, int textViewResourceId, ArrayList<Bundle> objects) {
            super(context, textViewResourceId, objects);
            this.context = context;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            StationTimeDataRow rowView;
            View cell = convertView;
            if (cell == null || cell.getId() != R.layout.cell_station_time_data_row) {
                cell = LayoutInflater.from(this.context).inflate(R.layout.cell_station_time_data_row, null);
                rowView = new StationTimeDataRow(cell);
                cell.setTag(rowView);
                cell.setId(R.layout.cell_station_time_data_row);
            } else {
                rowView = (StationTimeDataRow) cell.getTag();
            }
            rowView.setCellData((Bundle) StationDetailSubView01.this.arrayList.get(position));
            return cell;
        }
    }

    public StationDetailSubView01(Context context) {
        super(context);
        this.mContext = context;
        this.appManager = AppDataManager.shared();
        this.timeManager = TimeDataManager.shared();
        addView(LayoutInflater.from(context).inflate(R.layout.subview_station_detail_view01, null));
        ((Button) findViewById(R.id.goto_site_naver)).setOnClickListener(this);
        ((Button) findViewById(R.id.goto_site_daum)).setOnClickListener(this);
        initTextView();
        initListView();
    }

    private void initTextView() {
        this.firstTime1 = (TextView) findViewById(R.id.firstTime1);
        this.firstData1 = (TextView) findViewById(R.id.firstData1);
        this.firstTime2 = (TextView) findViewById(R.id.firstTime2);
        this.firstData2 = (TextView) findViewById(R.id.firstData2);
        this.lastTime1 = (TextView) findViewById(R.id.lastTime1);
        this.lastData1 = (TextView) findViewById(R.id.lastData1);
        this.lastTime2 = (TextView) findViewById(R.id.lastTime2);
        this.lastData2 = (TextView) findViewById(R.id.lastData2);
        this.infoValue01 = (TextView) findViewById(R.id.infoValue01);
        this.infoImage01 = (ImageView) findViewById(R.id.infoImage01);
        this.infoValue02 = (TextView) findViewById(R.id.infoValue02);
        this.infoImage02 = (ImageView) findViewById(R.id.infoImage02);
        this.infoValue03 = (TextView) findViewById(R.id.infoValue03);
        this.infoImage03 = (ImageView) findViewById(R.id.infoImage03);
    }

    private void initListView() {
        if (this.arrayList == null) {
            this.arrayList = new ArrayList();
        } else {
            this.arrayList.clear();
        }
        this.listData = new arrayListAdapter(this.mContext, 0, this.arrayList);
        this.listView = (ListView) findViewById(R.id.listView);
        this.listView.setAdapter(this.listData);
        this.listView.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                StationDetailSubView01.this.listView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }

    public void setStationData(Bundle bundle, int week) {
        if (!bundle.equals(this.dicStationData) || this.iWeekCode != week) {
            this.dicStationData = bundle;
            this.iWeekCode = week;
        }
    }

    public void setStationCode(int code, int lCode, int week) {
        if (this.iStationCode != code || this.iWeekCode != week) {
            this.iStationCode = code;
            this.iLineCode = lCode;
            this.iWeekCode = week;
            _getStationTimeInfo();
            _getArrStationTimeTable();
        }
    }

    public void setStationInfo(int toilet, int exitDoor, int crossing) {
        this.iToilet = toilet;
        this.iExitDoor = exitDoor;
        this.iCrossing = crossing;
        _setInfoData();
    }

    public void _getStationTimeInfo() {
        ArrayList<Bundle> arrData = this.appManager.getArrStationTimeDataToType(this.iStationCode, this.iWeekCode + 1, this.iLineCode, 1);
        if (arrData.size() > 1) {
            setTimeTextData((Bundle) arrData.get(0), this.firstTime1, this.firstData1);
            setTimeTextData((Bundle) arrData.get(arrData.size() - 1), this.lastTime1, this.lastData1);
        } else {
            this.firstTime1.setText("");
            this.firstData1.setText("");
            this.lastTime1.setText("");
            this.lastData1.setText("");
        }
        arrData = this.appManager.getArrStationTimeDataToType(this.iStationCode, this.iWeekCode + 1, this.iLineCode, 2);
        if (arrData.size() > 1) {
            setTimeTextData((Bundle) arrData.get(0), this.firstTime2, this.firstData2);
            setTimeTextData((Bundle) arrData.get(arrData.size() - 1), this.lastTime2, this.lastData2);
            return;
        }
        this.firstTime2.setText("");
        this.firstData2.setText("");
        this.lastTime2.setText("");
        this.lastData2.setText("");
    }

    private void setTimeTextData(Bundle bundle, TextView timeText, TextView dataText) {
        timeText.setText(String.format("%02d:%02d:%02d", new Object[]{Integer.valueOf(bundle.getString("hour")), Integer.valueOf(bundle.getString("tData")), Integer.valueOf(bundle.getString("tSub"))}));
        String strData = bundle.getString("dName");
        if (strData.length() > 0) {
            if (Integer.valueOf(bundle.getString("exFlag")).intValue() == 1) {
                dataText.setTextColor(-5622989);
                strData = "(\uae09)" + strData;
            } else {
                dataText.setTextColor(-14540254);
            }
            if (strData.length() < 7 && !strData.contains("\uc21c\ud658")) {
                strData = new StringBuilder(String.valueOf(strData)).append("\ud589").toString();
            }
            dataText.setText(strData);
            return;
        }
        dataText.setText("");
    }

    private void _setInfoData() {
        switch (this.iToilet) {
            case 0:
                this.infoValue01.setText("\uc5c6\uc74c");
                this.infoImage01.setImageDrawable(getResources().getDrawable(R.drawable.img_addon_info_icon1_2));
                break;
            case 1:
                this.infoValue01.setText("\ub0b4\ubd80");
                this.infoImage01.setImageDrawable(getResources().getDrawable(R.drawable.img_addon_info_icon1_1));
                break;
            case 2:
                this.infoValue01.setText("\uc678\ubd80");
                this.infoImage01.setImageDrawable(getResources().getDrawable(R.drawable.img_addon_info_icon1_2));
                break;
            case 3:
                this.infoValue01.setText("\ub0b4\ubd80,\uc678\ubd80");
                this.infoImage01.setImageDrawable(getResources().getDrawable(R.drawable.img_addon_info_icon1_1));
                break;
        }
        switch (this.iExitDoor) {
            case 0:
                this.infoValue02.setText("\uc624\ub978\ucabd");
                this.infoImage02.setImageDrawable(getResources().getDrawable(R.drawable.img_addon_info_icon2_0));
                break;
            case 1:
                this.infoValue02.setText("\uc67c\ucabd");
                this.infoImage02.setImageDrawable(getResources().getDrawable(R.drawable.img_addon_info_icon2_1));
                break;
            case 2:
                this.infoValue02.setText("\uc591\ucabd");
                this.infoImage02.setImageDrawable(getResources().getDrawable(R.drawable.img_addon_info_icon2_2));
                break;
        }
        switch (this.iCrossing) {
            case 0:
                this.infoValue03.setText("\ud6a1\ub2e8\uac00\ub2a5");
                this.infoImage03.setImageDrawable(getResources().getDrawable(R.drawable.img_addon_info_icon3_0));
                return;
            case 1:
                this.infoValue03.setText("\ubd88\uac00\ub2a5");
                this.infoImage03.setImageDrawable(getResources().getDrawable(R.drawable.img_addon_info_icon3_1));
                return;
            case 2:
                this.infoValue03.setText("\ud658\uc2b9\uc5f0\uacb0");
                this.infoImage03.setImageDrawable(getResources().getDrawable(R.drawable.img_addon_info_icon3_2));
                return;
            default:
                return;
        }
    }

    public void onClick(View arg0) {
        int id = arg0.getId();
        if (id == R.id.goto_site_naver) {
            cmdWebViewToSiteIndo(1);
        } else if (id == R.id.goto_site_daum) {
            cmdWebViewToSiteIndo(2);
        }
    }

    private void cmdWebViewToSiteIndo(int type) {
        ArrayList<Bundle> arrData = this.appManager.getArrStationCodeInfo(this.iStationCode);
        if (arrData.size() > 0) {
            Bundle bundle = (Bundle) arrData.get(0);
            String strCode;
            if (type == 1) {
                strCode = bundle.getString("naverCode");
                this.mContext.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(String.format("http://map.naver.com/local/siteview.nhn?stationId=%s", new Object[]{strCode}))));
                return;
            }
            strCode = bundle.getString("daumCode");
            this.mContext.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(String.format("http://map.daum.net/subway/subwayStationInfo?stationId=%s", new Object[]{strCode}))));
        }
    }

    private void _getArrStationTimeTable() {
        this.arrayList.clear();
        ArrayList<Bundle> arrData = this.timeManager.getStationTimeData(this.dicStationData, this.iWeekCode + 1, 12);
        if (arrData.size() > 0) {
            this.arrayList.addAll(arrData);
        }
        this.listData.notifyDataSetChanged();
    }
}
