package com.example.hyejung.easysubway.subviews;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.hyejung.easysubway.R;
import com.example.hyejung.easysubway.apputils.SiteScanLoader;
import com.example.hyejung.easysubway.apputils.SiteScanLoader.SiteScanTimeLoaderListener;
import com.example.hyejung.easysubway.cellRow.StationRealTimeNoDataRow;
import com.example.hyejung.easysubway.cellRow.StationRealTimeRow;
import com.example.hyejung.easysubway.cellRow.StationTimeDataRow;
import com.example.hyejung.easysubway.cellRow.StationTimeViewHeaderRow;
import com.example.hyejung.easysubway.DBmodel.AppDataManager;
import com.example.hyejung.easysubway.DBmodel.PointDataManager;
import com.example.hyejung.easysubway.DBmodel.RealTimeDataModel;
import com.example.hyejung.easysubway.DBmodel.TimeDataManager;
import java.util.ArrayList;

public class StationTimeDataSubView extends RelativeLayout implements OnClickListener, SiteScanTimeLoaderListener {
    private static final TouchTimeSelectedListener NULL_TOUCH_SELECTED_LISTENER = new TouchTimeSelectedListener() {
        public void onSelectedTimeTabButton(int type) {
        }
    };
    AppDataManager appManager = null;
    ArrayList<Bundle> arrStationInfo = null;
    ArrayList<Bundle> arrayList01 = null;
    ArrayList<Bundle> arrayList02 = null;
    Bundle dicStationInfo = null;
    int iLineCode = 0;
    int iSelectedIndex = 0;
    int iStationCode = 0;
    int iTimeType = 0;
    int iViewType = 1;
    int iWeekType = 0;
    ImageView infoImage01 = null;
    ImageView infoImage02 = null;
    ImageView infoImage03 = null;
    TextView infoValue01 = null;
    TextView infoValue02 = null;
    TextView infoValue03 = null;
    Button lineButton1 = null;
    Button lineButton2 = null;
    Button lineButton3 = null;
    Button lineButton4 = null;
    ArrayAdapter<Bundle> listData01 = null;
    ArrayAdapter<Bundle> listData02 = null;
    Context mContext = null;
    LinearLayout nameSubView = null;
    TextView nextButton = null;
    int nextCode = 1;
    TextView prevButton = null;
    int prevCode = 0;
    ListView realTimeListView = null;
    protected TouchTimeSelectedListener selectedListener = NULL_TOUCH_SELECTED_LISTENER;
    SiteScanLoader siteScanLoader = null;
    TextView stationName1 = null;
    TextView stationName2 = null;
    TextView stationName3 = null;
    String strNextName = null;
    String strPrevName = null;
    String strStationName = null;
    Button tabButton1 = null;
    Button tabButton2 = null;
    RelativeLayout timeLoadingView = null;
    TimeDataManager timeManager = null;
    ListView timeTableListView = null;
    LinearLayout timeTableTabView = null;

    public interface TouchTimeSelectedListener {
        void onSelectedTimeTabButton(int i);
    }

    public class RealTimeListAdapter extends ArrayAdapter<Bundle> {
        Context context;

        public RealTimeListAdapter(Context context, int textViewResourceId, ArrayList<Bundle> objects) {
            super(context, textViewResourceId, objects);
            this.context = context;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View cell = convertView;
            Bundle bundle = (Bundle) StationTimeDataSubView.this.arrayList02.get(position);
            if (bundle.getInt("cellType") == 9) {
                StationRealTimeNoDataRow rowView;
                if (cell == null || cell.getId() != R.layout.cell_station_real_time_nodata_row) {
                    cell = LayoutInflater.from(this.context).inflate(R.layout.cell_station_real_time_nodata_row, null);
                    rowView = new StationRealTimeNoDataRow(cell);
                    cell.setTag(rowView);
                    cell.setId(R.layout.cell_station_real_time_nodata_row);
                } else {
                    rowView = (StationRealTimeNoDataRow) cell.getTag();
                }
                rowView.setTitleData(bundle.getString("title"));
            } else {
                StationRealTimeRow rowView2;
                if (cell == null || cell.getId() != R.layout.cell_station_real_time_row) {
                    cell = LayoutInflater.from(this.context).inflate(R.layout.cell_station_real_time_row, null);
                    rowView2 = new StationRealTimeRow(cell);
                    cell.setTag(rowView2);
                    cell.setId(R.layout.cell_station_real_time_row);
                } else {
                    rowView2 = (StationRealTimeRow) cell.getTag();
                }
                rowView2.setCellData(bundle);
            }
            return cell;
        }
    }

    public class TimeTableListAdapter extends ArrayAdapter<Bundle> {
        Context context;

        public TimeTableListAdapter(Context context, int textViewResourceId, ArrayList<Bundle> objects) {
            super(context, textViewResourceId, objects);
            this.context = context;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View cell = convertView;
            Bundle bundle = (Bundle) StationTimeDataSubView.this.arrayList01.get(position);
            if (bundle.getInt("cellType") == 2) {
                StationTimeViewHeaderRow rowView;
                if (cell == null || cell.getId() != R.layout.cell_station_time_view_header_row) {
                    cell = LayoutInflater.from(this.context).inflate(R.layout.cell_station_time_view_header_row, null);
                    rowView = new StationTimeViewHeaderRow(cell);
                    cell.setTag(rowView);
                    cell.setId(R.layout.cell_station_time_view_header_row);
                } 
                else {
                    rowView = (StationTimeViewHeaderRow) cell.getTag();
                }
                rowView.setTimeView(Boolean.valueOf(false));
            } 
            else {
                StationTimeDataRow rowView2;
                if (cell == null || cell.getId() != R.layout.cell_station_time_data_row) {
                    cell = LayoutInflater.from(this.context).inflate(R.layout.cell_station_time_data_row, null);
                    rowView2 = new StationTimeDataRow(cell);
                    cell.setTag(rowView2);
                    cell.setId(R.layout.cell_station_time_data_row);
                } else {
                    rowView2 = (StationTimeDataRow) cell.getTag();
                }
                rowView2.setCellData(bundle);
            }
            return cell;
        }
    }

    public StationTimeDataSubView(Context context) {
        super(context);
        this.mContext = context;
        View nameView = LayoutInflater.from(context).inflate(R.layout.subview_station_time_view, null);
        addView(nameView);
        this.nameSubView = (LinearLayout) nameView.findViewById(R.id.nameSubView);
        this.stationName1 = (TextView) nameView.findViewById(R.id.stationName1);
        this.stationName2 = (TextView) nameView.findViewById(R.id.stationName2);
        this.stationName3 = (TextView) nameView.findViewById(R.id.stationName3);
        this.prevButton = (TextView) nameView.findViewById(R.id.prevButton);
        this.nextButton = (TextView) nameView.findViewById(R.id.nextButton);
        this.lineButton1 = (Button) nameView.findViewById(R.id.lineButton1);
        this.lineButton2 = (Button) nameView.findViewById(R.id.lineButton2);
        this.lineButton3 = (Button) nameView.findViewById(R.id.lineButton3);
        this.lineButton4 = (Button) nameView.findViewById(R.id.lineButton4);
        this.lineButton1.setOnClickListener(this);
        this.lineButton2.setOnClickListener(this);
        this.lineButton3.setOnClickListener(this);
        this.lineButton4.setOnClickListener(this);
        int widthPX = getContext().getResources().getDisplayMetrics().widthPixels;
        float scale = getContext().getResources().getDisplayMetrics().density;
        if (((float) widthPX) / scale > 400.0f) {
            widthPX = (int) (110.0f * scale);
        } else {
            widthPX = (int) (((float) ((int) (((float) widthPX) + (32.0f * scale)))) * 0.25f);
        }
        LayoutParams params = (LayoutParams) this.lineButton1.getLayoutParams();
        params.width = widthPX;
        this.lineButton1.setLayoutParams(params);
        params = (LayoutParams) this.lineButton2.getLayoutParams();
        params.width = widthPX;
        this.lineButton2.setLayoutParams(params);
        params = (LayoutParams) this.lineButton3.getLayoutParams();
        params.width = widthPX;
        this.lineButton3.setLayoutParams(params);
        params = (LayoutParams) this.lineButton4.getLayoutParams();
        params.width = widthPX;
        this.lineButton4.setLayoutParams(params);
        this.infoValue01 = (TextView) findViewById(R.id.infoValue01);
        this.infoImage01 = (ImageView) findViewById(R.id.infoImage01);
        this.infoValue02 = (TextView) findViewById(R.id.infoValue02);
        this.infoImage02 = (ImageView) findViewById(R.id.infoImage02);
        this.infoValue03 = (TextView) findViewById(R.id.infoValue03);
        this.infoImage03 = (ImageView) findViewById(R.id.infoImage03);
        this.timeLoadingView = (RelativeLayout) findViewById(R.id.timeLoadingView);
        initListView01();
        initListView02();
        this.arrStationInfo = new ArrayList();
        this.appManager = AppDataManager.shared();
        this.timeManager = TimeDataManager.shared();
        initTimeTabView();
    }

    public void onClick(View arg0) {
        int id = arg0.getId();
        int index = -1;
        if (id == R.id.lineButton1) {
            index = 0;
        } else if (id == R.id.lineButton2) {
            index = 1;
        } else if (id == R.id.lineButton3) {
            index = 2;
        } else if (id == R.id.lineButton4) {
            index = 3;
        } else if (id == R.id.time_tab_button1) {
            this.selectedListener.onSelectedTimeTabButton(1);
        } else if (id == R.id.time_tab_button2) {
            this.selectedListener.onSelectedTimeTabButton(0);
        }
        if (index >= 0) {
            setLineTabView(index);
            showTimeListView();
        }
    }

    private void setSelectedTabAction(int index) {
        if (index == 1) {
            this.tabButton1.setSelected(true);
            this.tabButton2.setSelected(false);
            setTabButtonParam(9, 10);
        } else {
            this.tabButton1.setSelected(false);
            this.tabButton2.setSelected(true);
            setTabButtonParam(10, 9);
        }
        if (PointDataManager.shared().checkRealTimeFlag()) {
            this.tabButton1.setVisibility(0);
        } else {
            this.tabButton1.setVisibility(8);
        }
        this.timeTableTabView.setVisibility(0);
    }

    void setTabButtonParam(int left, int right) {
        LinearLayout.LayoutParams lparam = (LinearLayout.LayoutParams) this.tabButton1.getLayoutParams();
        lparam.weight = (float) left;
        this.tabButton1.setLayoutParams(lparam);
        LinearLayout.LayoutParams rparam = (LinearLayout.LayoutParams) this.tabButton2.getLayoutParams();
        rparam.weight = (float) right;
        this.tabButton2.setLayoutParams(rparam);
    }

    public void resetStationTimeData() {
        removeSiteScanLoader();
        if (this.arrayList01 != null) {
            this.arrayList01.clear();
        }
        if (this.arrayList02 != null) {
            this.arrayList02.clear();
        }
        if (this.listData02 != null) {
            this.listData02.notifyDataSetChanged();
        }
        if (this.listData01 != null) {
            this.listData01.notifyDataSetChanged();
        }
        this.arrStationInfo.clear();
        this.dicStationInfo = null;
        this.timeTableTabView.setVisibility(8);
        setVisibility(4);
    }

    public void showStationTimeView(Bundle dic, int type) {
        if (dic == null) {
            resetStationTimeData();
            return;
        }
        ArrayList<Bundle> arr;
        setVisibility(0);
        String strKey1 = null;
        String strKey2 = null;
        int iTransfer = 0;
        switch (type) {
            case 0:
                strKey1 = "stationName";
                strKey2 = "stationCode";
                iTransfer = Integer.valueOf(dic.getString("transfer")).intValue();
                break;
            case 1:
                strKey1 = "nStationName1";
                strKey2 = "nStationCode1";
                iTransfer = checkTransferData(dic.getString(strKey2));
                break;
            case 2:
                strKey1 = "nStationName2";
                strKey2 = "nStationCode2";
                iTransfer = checkTransferData(dic.getString(strKey2));
                break;
        }
        if (iTransfer > 0) {
            arr = this.appManager.getArrStationInfoName(dic.getString(strKey1));
        } else {
            arr = this.appManager.getArrStationDataCode(dic.getString(strKey2));
        }
        if (arr.size() > 0) {
            this.arrStationInfo.clear();
            this.arrStationInfo.addAll(arr);
            this.iSelectedIndex = 0;
            for (int i = 0; i < this.arrStationInfo.size(); i++) {
                if (Integer.valueOf(dic.getString("lineCode")) == Integer.valueOf(((Bundle) this.arrStationInfo.get(i)).getString("lineCode"))) {
                    this.iSelectedIndex = i;
                    setLineTabView(this.iSelectedIndex);
                }
            }
            setLineTabView(this.iSelectedIndex);
        }
    }

    private int checkTransferData(String strCode) {
        ArrayList<Bundle> arr = this.appManager.getArrStationDataCode(strCode);
        if (arr.size() > 0) {
            return ((Bundle) arr.get(0)).getInt("transfer");
        }
        return 0;
    }

    private void setLineTabView(int index) {
        this.iSelectedIndex = index;
        this.dicStationInfo = (Bundle) this.arrStationInfo.get(this.iSelectedIndex);
        String strName = this.dicStationInfo.getString("stationName");
        String strSubName = this.dicStationInfo.getString("subName");
        if (strSubName.length() > 0) {
            this.stationName1.setVisibility(4);
            this.stationName2.setVisibility(0);
            this.stationName3.setVisibility(0);
            this.stationName1.setText("");
            this.stationName2.setText(strName);
            this.stationName3.setText("(" + strSubName + ")");
        } else {
            this.stationName1.setVisibility(0);
            this.stationName2.setVisibility(4);
            this.stationName3.setVisibility(4);
            this.stationName1.setText(strName);
            this.stationName2.setText("");
            this.stationName3.setText("");
            if (strName.length() > 7) {
                this.stationName1.setTextSize(1, 20.0f);
            } else {
                this.stationName1.setTextSize(1, 23.0f);
            }
        }
        String strData = this.dicStationInfo.getString("nStationName1");
        if (strData == null) {
            this.prevButton.setText("\uc5c6\uc74c");
        } else if (strData.equals("0")) {
            this.prevButton.setText("\uc5c6\uc74c");
        } else {
            this.prevButton.setText(" " + strData);
        }
        strData = this.dicStationInfo.getString("nStationName2");
        if (strData == null) {
            this.nextButton.setText("\uc5c6\uc74c");
        } else if (strData.equals("0")) {
            this.nextButton.setText("\uc5c6\uc74c");
        } else {
            this.nextButton.setText(new StringBuilder(String.valueOf(strData)).append(" ").toString());
        }
        this.lineButton1.setVisibility(4);
        this.lineButton2.setVisibility(4);
        this.lineButton3.setVisibility(4);
        this.lineButton4.setVisibility(4);
        Button button = null;
        for (int i = 0; i < this.arrStationInfo.size(); i++) {
            Bundle bundle = (Bundle) this.arrStationInfo.get(i);
            switch (i) {
                case 0:
                    button = this.lineButton1;
                    break;
                case 1:
                    button = this.lineButton2;
                    break;
                case 2:
                    button = this.lineButton3;
                    break;
                case 3:
                    button = this.lineButton4;
                    break;
            }
            button.setText(bundle.getString("lineName"));
            button.setVisibility(0);
            int iLineCode = Integer.valueOf(bundle.getString("lineCode")).intValue();
            if (this.iSelectedIndex == i) {
                button.setSelected(true);
                this.nameSubView.setBackgroundResource(this.appManager.getStationNameImage(iLineCode));
                button.bringToFront();
                button.invalidate();
                int textColor = this.appManager.getStationNameColor(iLineCode);
                this.prevButton.setTextColor(textColor);
                this.nextButton.setTextColor(textColor);
            } else {
                button.setSelected(false);
            }
            button.setBackgroundResource(this.appManager.getLineTabImage(iLineCode));
        }
        _setInfoData();
    }

    public void setWeekType(int week) {
        this.iTimeType = this.appManager.getMapRealTimeType();
        if (this.iTimeType == 1 && !PointDataManager.shared().checkRealTimeFlag()) {
            this.iTimeType = 0;
        }
        this.iWeekType = week;
        showTimeListView();
    }

    public void setRealTimeType(int type) {
        this.iTimeType = type;
        showTimeListView();
    }

    private void initTimeTabView() {
        this.timeTableTabView = (LinearLayout) findViewById(R.id.timeTableTabView);
        this.timeTableTabView.setVisibility(8);
        this.tabButton1 = (Button) findViewById(R.id.time_tab_button1);
        this.tabButton1.setOnClickListener(this);
        this.tabButton2 = (Button) findViewById(R.id.time_tab_button2);
        this.tabButton2.setOnClickListener(this);
    }

    private void initListView01() {
        if (this.arrayList01 == null) {
            this.arrayList01 = new ArrayList();
        } else {
            this.arrayList01.clear();
        }
        this.timeTableListView = (ListView) findViewById(R.id.timeTableListView);
        if (this.listData01 == null) {
            this.listData01 = new TimeTableListAdapter(this.mContext, 0, this.arrayList01);
            this.timeTableListView.setAdapter(this.listData01);
        }
    }

    private void initListView02() {
        if (this.arrayList02 == null) {
            this.arrayList02 = new ArrayList();
        } else {
            this.arrayList02.clear();
        }
        this.realTimeListView = (ListView) findViewById(R.id.realTimeListView);
        if (this.listData02 == null) {
            this.listData02 = new RealTimeListAdapter(this.mContext, 0, this.arrayList02);
            this.realTimeListView.setAdapter(this.listData02);
        }
    }

    private void showTimeListView() {
        if (this.iTimeType == 1) {
            this.timeTableListView.setVisibility(4);
            this.realTimeListView.setVisibility(0);
        } else {
            this.realTimeListView.setVisibility(4);
            this.timeTableListView.setVisibility(0);
        }
        setSelectedTabAction(this.iTimeType);
        this.timeLoadingView.setVisibility(4);
        _getArrStationTimeTable();
    }

    private void _getArrStationTimeTable() {
        removeSiteScanLoader();
        if (this.iTimeType == 1) {
            this.arrayList02.clear();
            if (this.dicStationInfo != null) {
                this.iStationCode = Integer.valueOf(this.dicStationInfo.getString("stationCode")).intValue();
                this.iLineCode = Integer.valueOf(this.dicStationInfo.getString("lineCode")).intValue();
                this.strStationName = this.dicStationInfo.getString("stationName");
                this.strPrevName = this.dicStationInfo.getString("nStationName1");
                this.strNextName = this.dicStationInfo.getString("nStationName2");
                checkPrevNextCode(this.iLineCode);
                getSiteScanData();
            }
            this.listData02.notifyDataSetChanged();
            return;
        }
        this.arrayList01.clear();
        if (this.dicStationInfo != null) {
            ArrayList<Bundle> arrData = this.timeManager.getStationTimeData(this.dicStationInfo, this.iWeekType + 1, 7);
            if (arrData.size() > 0) {
                this.arrayList01.addAll(arrData);
            }
        }
        this.listData01.notifyDataSetChanged();
    }

    private void _setInfoData() {
        this.infoValue01.setText("");
        this.infoValue02.setText("");
        this.infoValue03.setText("");
        this.infoImage01.setImageDrawable(null);
        this.infoImage02.setImageDrawable(null);
        this.infoImage03.setImageDrawable(null);
        if (this.dicStationInfo != null) {
            int iToilet = Integer.valueOf(this.dicStationInfo.getString("toilet")).intValue();
            int iExitDoor = Integer.valueOf(this.dicStationInfo.getString("exitDoor")).intValue();
            int iCrossing = Integer.valueOf(this.dicStationInfo.getString("crossing")).intValue();
            switch (iToilet) {
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
            switch (iExitDoor) {
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
            switch (iCrossing) {
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
    }

    public void checkPrevNextCode(int lineCode) {
        switch (lineCode) {
            case 2:
            case 4:
            case 7:
            case 12:
                this.prevCode = 1;
                this.nextCode = 0;
                return;
            default:
                this.prevCode = 0;
                this.nextCode = 1;
                return;
        }
    }

    public void getSiteScanData() {
        if (PointDataManager.shared().checkRealTimeFlag()) {
            this.timeLoadingView.setVisibility(0);
            this.iTimeType = 0;
            initSiteScanLoader();
            this.siteScanLoader.getSiteScanTimeData(this.iStationCode, Boolean.valueOf(false));
            return;
        }
        Bundle object = new Bundle();
        object.putInt("cellType", 9);
        object.putString("title", "\ub3c4\ucc29\uc815\ubcf4\ub97c \uc81c\uacf5\ud558\uc9c0 \uc54a\ub294 \uc5ed\uc785\ub2c8\ub2e4");
        this.arrayList02.add(object);
    }

    private void initSiteScanLoader() {
        if (this.siteScanLoader == null) {
            this.siteScanLoader = new SiteScanLoader(this.mContext);
            this.siteScanLoader.setSiteScanTimeLoaderListener(this);
        }
    }

    private void removeSiteScanLoader() {
        if (this.siteScanLoader != null) {
            this.siteScanLoader.cancelSiteScanTimeData();
            this.siteScanLoader.clearDicAppData();
            this.siteScanLoader.setSiteScanTimeLoaderListener(null);
            this.siteScanLoader = null;
        }
    }

    public void onResSiteScanTimeData(ArrayList<RealTimeDataModel> arrayList) {
        Bundle object;
        this.arrayList02.clear();
        if (arrayList != null) {
            int i;
            ArrayList<Bundle> arrData01 = new ArrayList();
            ArrayList<Bundle> arrData02 = new ArrayList();
            boolean bPrevFlag = Integer.valueOf(this.dicStationInfo.getString("nStationCode1")).intValue() != 9999;
            boolean z = Integer.valueOf(this.dicStationInfo.getString("nStationCode2")).intValue() != 9999;
            int time01 = 0;
            int time02 = 0;
            int stationCode = Integer.valueOf(this.dicStationInfo.getString("stationCode")).intValue();
            if (stationCode == 6001 || stationCode == 6007) {
                bPrevFlag = true;
                z = true;
            }
            for (i = 0; i < arrayList.size(); i++) {
                RealTimeDataModel model = (RealTimeDataModel) arrayList.get(i);
                int tTime = 0;
                String title = null;
                String dName = model.getbStatnNm().replace(")\ud589", ")");
                if (dName.contains("11\ud589")) {
                    dName = "\uad11\uad50\ud589";
                }
                int curs = Integer.valueOf(model.getCurstatnsn()).intValue();
                if (curs < 11) {
                    String desc;
                    if (curs > 0) {
                        tTime = Integer.valueOf(model.getbArvlTm()).intValue();
                    }
                    if (tTime == 0 && model.getArvlMsg2() != null) {
                        title = model.getArvlMsg2().replace("[", "").replace("]", "");
                        if (title.contains("\ubc88\uc9f8 \uc804\uc5ed")) {
                            title = title.replace("\ubc88\uc9f8 \uc804\uc5ed", "\uc804").replace(")", ")\uc785\ub2c8\ub2e4").replace(" (", "(");
                        } else if (title.contains("(") && title.contains(")")) {
                            title = (title.substring(0, title.indexOf("(")) + title.substring(title.indexOf(")"))).replace(")", "");
                        }
                        if (title.length() < 9) {
                            if (title.contains("\uc9c4\uc785")) {
                                title = new StringBuilder(String.valueOf(title)).append("\uc911\uc785\ub2c8\ub2e4").toString();
                            } else if (title.contains("\ucd9c\ubc1c")) {
                                title = new StringBuilder(String.valueOf(title)).append("\uc911\uc785\ub2c8\ub2e4").toString();
                            } else if (title.contains("\ub3c4\ucc29")) {
                                title = new StringBuilder(String.valueOf(title)).append("\ud558\uc600\uc2b5\ub2c8\ub2e4.").toString();
                            }
                        }
                    }
                    if (title == null) {
                        title = "";
                    }
                    if (curs > 0) {
                        desc = new StringBuilder(String.valueOf(curs)).append("\uac1c\uc5ed \uc804\uc785\ub2c8\ub2e4.").toString();
                    } else {
                        desc = model.getArvlMsg3();
                        if (desc != null) {
                            if (desc.contains("(")) {
                                desc = desc.substring(0, desc.indexOf("("));
                            }
                            desc = new StringBuilder(String.valueOf(desc)).append("\uc5ed").toString();
                        } else {
                            desc = title;
                        }
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString("title", title);
                    bundle.putString("desc", desc);
                    bundle.putString("dName", dName);
                    bundle.putInt("curs", curs);
                    bundle.putInt("tTime", tTime);
                    boolean bFlag = true;
                    if (bPrevFlag && this.prevCode == Integer.valueOf(model.getTrainLine()).intValue()) {
                        if (stationCode == 10201 && curs > 1) {
                            bFlag = false;
                        }
                        if (bFlag && ((time01 == 0 || tTime > time01) && (curs <= 1 || tTime <= 0 || tTime >= 50))) {
                            if (stationCode == 6001) {
                                arrData02.add(bundle);
                                time02 = tTime;
                            } else if (stationCode != 6007) {
                                arrData01.add(bundle);
                                time01 = tTime;
                            }
                        }
                    } else if (z && this.nextCode == Integer.valueOf(model.getTrainLine()).intValue()) {
                        if (stationCode == 10201 && model.getbStatnNm().contains("\uc11c\uc6b8")) {
                            bFlag = false;
                        }
                        if (bFlag && ((time02 == 0 || tTime > time02) && ((curs <= 1 || tTime <= 0 || tTime >= 50) && stationCode != 6001))) {
                            arrData02.add(bundle);
                            time02 = tTime;
                        }
                    }
                }
            }
            int iRowCount = arrData01.size() > arrData02.size() ? arrData01.size() : arrData02.size();
            if (iRowCount > 0) {
                object = null;
                Bundle bundle1 = null;
                Bundle bundle2 = null;
                for (i = 0; i < iRowCount; i++) {
                    if (arrData01 != null) {
                        if (arrData01.size() > i) {
                            bundle1 = (Bundle) arrData01.get(i);
                        } else {
                            bundle1 = null;
                        }
                    }
                    if (arrData02 != null) {
                        if (arrData02.size() > i) {
                            bundle2 = (Bundle) arrData02.get(i);
                        } else {
                            bundle2 = null;
                        }
                    }
                    if (bundle1 != null && bundle2 != null) {
                        object = new Bundle();
                        object.putInt("cellType", 1);
                        object.putString("title1", bundle1.getString("title"));
                        object.putString("desc1", bundle1.getString("desc"));
                        object.putString("dName1", bundle1.getString("dName"));
                        object.putInt("curs1", bundle1.getInt("curs"));
                        object.putInt("tTime1", bundle1.getInt("tTime"));
                        object.putString("title2", bundle2.getString("title"));
                        object.putString("desc2", bundle2.getString("desc"));
                        object.putString("dName2", bundle2.getString("dName"));
                        object.putInt("curs2", bundle2.getInt("curs"));
                        object.putInt("tTime2", bundle2.getInt("tTime"));
                    } else if (bundle1 != null) {
                        object = new Bundle();
                        object.putInt("cellType", 1);
                        object.putString("title1", bundle1.getString("title"));
                        object.putString("desc1", bundle1.getString("desc"));
                        object.putString("dName1", bundle1.getString("dName"));
                        object.putInt("curs1", bundle1.getInt("curs"));
                        object.putInt("tTime1", bundle1.getInt("tTime"));
                        object.putString("desc2", "");
                    } else if (bundle2 != null) {
                        object = new Bundle();
                        object.putInt("cellType", 1);
                        object.putString("title2", bundle2.getString("title"));
                        object.putString("desc2", bundle2.getString("desc"));
                        object.putString("dName2", bundle2.getString("dName"));
                        object.putInt("curs2", bundle2.getInt("curs"));
                        object.putInt("tTime2", bundle2.getInt("tTime"));
                        object.putString("desc1", "");
                    }
                    if (object != null) {
                        this.arrayList02.add(object);
                    }
                    object = null;
                }
            }
        }
        if (this.arrayList02.size() == 0) {
            object = new Bundle();
            object.putInt("cellType", 9);
            object.putString("title", "\ub3c4\ucc29\uc815\ubcf4\ub97c \ubc1b\uc544\uc624\uc9c0 \ubabb\ud588\uc2b5\ub2c8\ub2e4.\n\uc7a0\uc2dc\ud6c4 \ub2e4\uc2dc \uc774\uc6a9\ud574 \uc8fc\uc2dc\uae30 \ubc14\ub78d\ub2c8\ub2e4.");
            this.arrayList02.add(object);
        }
        this.listData02.notifyDataSetChanged();
        removeSiteScanLoader();
        this.timeLoadingView.setVisibility(4);
    }

    public void setTouchTimeSelectedListener(TouchTimeSelectedListener listener) {
        if (listener == null) {
            this.selectedListener = NULL_TOUCH_SELECTED_LISTENER;
        } else {
            this.selectedListener = listener;
        }
    }
}
