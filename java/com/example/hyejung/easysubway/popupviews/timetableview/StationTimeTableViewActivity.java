package com.example.hyejung.easysubway.popupviews.timetableview;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.example.hyejung.easysubway.R;
import com.example.hyejung.easysubway.cellRow.StationTimeTableHeaderRow;
import com.example.hyejung.easysubway.cellRow.StationTimeTableRow;
import com.example.hyejung.easysubway.DBmodel.AppDataManager;
import com.example.hyejung.easysubway.DBmodel.PointDataManager;
import com.example.hyejung.easysubway.subviews.StationNameSubView;
import com.example.hyejung.easysubway.subviews.StationNameSubView.StationNameViewListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class StationTimeTableViewActivity extends Activity implements OnClickListener, StationNameViewListener {
    private static final Comparator<Bundle> myComparator1 = new Comparator<Bundle>() {
        public int compare(Bundle arg0, Bundle arg1) {
            if (arg0.getInt("hour") < arg1.getInt("hour")) {
                return -1;
            }
            return arg0.getInt("min") < arg1.getInt("min") ? 1 : 0;
        }
    };
    AppDataManager appManager = null;
    int iHourValue = 0;
    int iItemRow = 0;
    int iLineCode = 0;
    int iMinutesValue = 0;
    int iStationCode = 0;
    int iWeekCode = 0;
    ArrayAdapter<Bundle> listData = null;
    StationNameSubView stationNameView = null;
    ArrayList<Bundle> timeTableList = null;
    Button weekButton = null;

    public class StationListAdapter extends ArrayAdapter<Bundle> {
        Activity context;

        public StationListAdapter(Activity context, int textViewResourceId, ArrayList<Bundle> objects) {
            super(context, textViewResourceId, objects);
            this.context = context;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View cell = convertView;
            Bundle bundle = (Bundle) StationTimeTableViewActivity.this.timeTableList.get(position);
            if (bundle.getInt("cellType") == 1) {
                StationTimeTableHeaderRow rowView;
                if (cell == null || cell.getId() != R.layout.cell_station_time_table_header_row) {
                    cell = this.context.getLayoutInflater().inflate(R.layout.cell_station_time_table_header_row, null);
                    rowView = new StationTimeTableHeaderRow(cell);
                    cell.setTag(rowView);
                    cell.setId(R.layout.cell_station_time_table_header_row);
                } else {
                    rowView = (StationTimeTableHeaderRow) cell.getTag();
                }
                rowView.setTitle(bundle.getString("title"));
            } else {
                StationTimeTableRow rowView2;
                if (cell == null || cell.getId() != R.layout.cell_station_time_table_row) {
                    cell = this.context.getLayoutInflater().inflate(R.layout.cell_station_time_table_row, null);
                    rowView2 = new StationTimeTableRow(cell);
                    cell.setTag(rowView2);
                    cell.setId(R.layout.cell_station_time_table_row);
                } else {
                    rowView2 = (StationTimeTableRow) cell.getTag();
                }
                rowView2.setCellData(bundle);
            }
            return cell;
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_time_table_view);
        this.appManager = AppDataManager.shared();
        initTableBar();
        initListView();
        initStationNameView();
    }

    private void initTableBar() {
        LinearLayout titlebar = (LinearLayout) findViewById(R.id.titleBer);
        View barIndicator = LayoutInflater.from(this).inflate(R.layout.bar_titlebar_2button, null);
        ((TextView) barIndicator.findViewById(R.id.title)).setText("\uc2dc\uac01\ud45c");
        this.weekButton = (Button) barIndicator.findViewById(R.id.navi_titlebar_2button1);
        this.weekButton.setOnClickListener(this);
        Button button2 = (Button) barIndicator.findViewById(R.id.navi_titlebar_2button2);
        button2.setText("\ub2eb\uae30");
        button2.setOnClickListener(this);
        titlebar.addView(barIndicator);
        setWeeButton(this.appManager.getWeekType());
    }

    private void initStationNameView() {
        LinearLayout nameView = (LinearLayout) findViewById(R.id.stationNameView);
        this.stationNameView = new StationNameSubView(this);
        this.stationNameView.setStationNameViewListener(this);
        nameView.addView(this.stationNameView);
        setNextStationInfo(PointDataManager.shared().getSelectedData());
    }

    public void setNextStationInfo(Bundle dicData) {
        this.stationNameView.setNextStationInfo(dicData, 0);
    }

    private void initListView() {
        this.timeTableList = new ArrayList();
        this.listData = new StationListAdapter(this, R.layout.cell_subway_station_list_row, this.timeTableList);
        ((ListView) findViewById(R.id.listView)).setAdapter(this.listData);
    }

    public void onClick(View arg0) {
        int id = arg0.getId();
        if (id == R.id.navi_titlebar_2button1) {
            cmdTableBer_Button1();
        } else if (id == R.id.navi_titlebar_2button2) {
            cmdTableBer_Button2();
        }
    }

    private void cmdTableBer_Button1() {
        setWeeButton(this.appManager.cmdChangeWeekTypeEvent());
        _getArrStationTimeTable();
    }

    private void cmdTableBer_Button2() {
        finish();
    }

    private void setWeeButton(int week) {
        this.iWeekCode = week;
        if (this.iWeekCode == 1) {
            this.weekButton.setText("\ud1a0\uc694\uc77c");
        } else if (this.iWeekCode == 2) {
            this.weekButton.setText("\uc77c\uc694\uc77c");
        } else {
            this.weekButton.setText("\ud3c9\uc77c");
        }
    }

    public void onSelectedTabButton(Bundle bundle) {
        this.iStationCode = Integer.valueOf(bundle.getString("stationCode")).intValue();
        this.iLineCode = Integer.valueOf(bundle.getString("lineCode")).intValue();
        _getArrStationTimeTable();
    }

    private void _getArrStationTimeTable() {
        ArrayList<Bundle> arr = this.appManager.getArrStationTimeDataToWeek(this.iStationCode, this.iWeekCode + 1, this.iLineCode);
        this.iItemRow = 0;
        if (arr.size() > 0) {
            _initTimeTableData(arr);
            this.listData.notifyDataSetChanged();
            if (this.iItemRow - 3 > 0) {
                this.iItemRow -= 3;
            }
            ((ListView) findViewById(R.id.listView)).setSelection(this.iItemRow);
        } else {
            this.timeTableList.clear();
            this.listData.notifyDataSetChanged();
        }
        arr.clear();
    }

    public void getNowTimeData() {
        Calendar c = Calendar.getInstance();
        this.iHourValue = c.get(11);
        this.iMinutesValue = c.get(12);
    }

    private void _initTimeTableData(ArrayList<Bundle> arr) {
        this.timeTableList.clear();
        ArrayList<Bundle> arrData = this.appManager.getArrTrainDataAll(this.iLineCode);
        getNowTimeData();
        int iNowHour = this.iHourValue;
        int iNowMinutes = this.iMinutesValue;
        int iHourType = 0;
        if (iNowHour < 4) {
            iNowHour += 24;
        }
        int iOldHour = 0;
        int iNewHour = 0;
        int iNewHour1 = 0;
        int iNewHour2 = 0;
        int iType1 = 0;
        int iType2 = 0;
        String strMinutes1 = null;
        String strMinutes2 = null;
        String strSecond1 = null;
        String strSecond2 = null;
        String strWSub1 = null;
        String strWSub2 = null;
        String strDest1 = null;
        String strDest2 = null;
        String strExFlag1 = null;
        String strExFlag2 = null;
        if (arr.size() < 3) {
            iType1 = 2;
            iType2 = 2;
        }
        if (arrData.size() > 0) {
            Bundle object;
            ArrayList<Bundle> arrTemp1 = new ArrayList();
            ArrayList<Bundle> arrTemp2 = new ArrayList();
            Iterator it = arr.iterator();
            while (it.hasNext()) {
                Bundle bundle = (Bundle) it.next();
                bundle.getString("hour");
                iNewHour2 = Integer.valueOf(bundle.getString("hour")).intValue();
                iNewHour1 = iNewHour2;
                int iDest1 = Integer.valueOf(bundle.getString("tCode1")).intValue();
                if (iDest1 > 0) {
                    strMinutes1 = bundle.getString("tData1");
                    if (strMinutes1.length() > 0) {
                        iDest1--;
                        if (iDest1 < arrData.size()) {
                            strDest1 = ((Bundle) arrData.get(iDest1)).getString("dName");
                            strExFlag1 = ((Bundle) arrData.get(iDest1)).getString("exFlag");
                        } else {
                            strDest1 = "";
                            strExFlag1 = "0";
                        }
                        int iMinutes1 = Integer.valueOf(strMinutes1).intValue();
                        int iSecond1 = Integer.valueOf(bundle.getString("tSub1")).intValue();
                        strWSub1 = bundle.getString("wSub1");
                        int iWSub1 = Integer.valueOf(strWSub1).intValue();
                        if (!(iWSub1 == 88 || iWSub1 == 99 || iWSub1 <= 0)) {
                            iSecond1 += iWSub1;
                            if (iSecond1 >= 60) {
                                iMinutes1 += iSecond1 / 60;
                                iSecond1 %= 60;
                                if (iMinutes1 >= 60) {
                                    iMinutes1 -= 60;
                                    iNewHour1++;
                                }
                            }
                        }
                        strMinutes1 = String.valueOf(iMinutes1);
                        strSecond1 = String.valueOf(iSecond1);
                        object = new Bundle();
                        object.putInt("hour", iNewHour1);
                        object.putInt("min", iMinutes1);
                        object.putInt("type", iType1);
                        object.putString("minutes", strMinutes1);
                        object.putString("second", strSecond1);
                        object.putString("wSub", strWSub1);
                        object.putString("dest", strDest1);
                        object.putString("exFlag", strExFlag1);
                        arrTemp1.add(object);
                    }
                }
                int iDest2 = Integer.valueOf(bundle.getString("tCode2")).intValue();
                if (iDest2 > 0) {
                    strMinutes2 = bundle.getString("tData2");
                    if (strMinutes2.length() > 0) {
                        iDest2--;
                        if (iDest2 < arrData.size()) {
                            strDest2 = ((Bundle) arrData.get(iDest2)).getString("dName");
                            strExFlag2 = ((Bundle) arrData.get(iDest2)).getString("exFlag");
                        } else {
                            strDest2 = "";
                            strExFlag2 = "0";
                        }
                        int iMinutes2 = Integer.valueOf(strMinutes2).intValue();
                        int iSecond2 = Integer.valueOf(bundle.getString("tSub2")).intValue();
                        strWSub2 = bundle.getString("wSub2");
                        int iWSub2 = Integer.valueOf(strWSub2).intValue();
                        if (!(iWSub2 == 88 || iWSub2 == 99 || iWSub2 <= 0)) {
                            iSecond2 += iWSub2;
                            if (iSecond2 >= 60) {
                                iMinutes2 += iSecond2 / 60;
                                iSecond2 %= 60;
                                if (iMinutes2 >= 60) {
                                    iMinutes2 -= 60;
                                    iNewHour2++;
                                }
                            }
                        }
                        strMinutes2 = String.valueOf(iMinutes2);
                        strSecond2 = String.valueOf(iSecond2);
                        object = new Bundle();
                        object.putInt("hour", iNewHour2);
                        object.putInt("min", iMinutes2);
                        object.putInt("type", iType2);
                        object.putString("minutes", strMinutes2);
                        object.putString("second", strSecond2);
                        object.putString("wSub", strWSub2);
                        object.putString("dest", strDest2);
                        object.putString("exFlag", strExFlag2);
                        arrTemp2.add(object);
                    }
                }
            }
            Boolean bFlag1 = Boolean.valueOf(true);
            Boolean bFlag2 = Boolean.valueOf(true);
            int iIndex1 = 0;
            int iIndex2 = 0;
            Bundle dic1 = null;
            Bundle dic2 = null;
            Collections.sort(arrTemp1, myComparator1);
            Collections.sort(arrTemp2, myComparator1);
            while (true) {
                if (bFlag1.booleanValue()) {
                    if (arrTemp1.size() > iIndex1) {
                        dic1 = (Bundle) arrTemp1.get(iIndex1);
                        iNewHour1 = dic1.getInt("hour");
                    } else {
                        bFlag1 = Boolean.valueOf(false);
                        iNewHour1 = 99;
                    }
                }
                if (bFlag2.booleanValue()) {
                    if (arrTemp2.size() > iIndex2) {
                        dic2 = (Bundle) arrTemp2.get(iIndex2);
                        iNewHour2 = dic2.getInt("hour");
                    } else {
                        bFlag2 = Boolean.valueOf(false);
                        iNewHour2 = 99;
                    }
                }
                if (iNewHour != iNewHour1 && iNewHour != iNewHour2) {
                    if (!bFlag1.booleanValue() && !bFlag2.booleanValue()) {
                        break;
                    }
                    iNewHour = iNewHour1 < iNewHour2 ? iNewHour1 : iNewHour2;
                    if (iNewHour != iOldHour) {
                        String strTime;
                        iOldHour = iNewHour;
                        if (iNewHour < 12) {
                            strTime = String.format("\uc624\uc804 %d\uc2dc", new Object[]{Integer.valueOf(iNewHour)});
                        } else if (iNewHour == 12) {
                            strTime = "\uc624\ud6c4 12\uc2dc";
                        } else if (iNewHour < 24) {
                            strTime = String.format("\uc624\ud6c4 %d\uc2dc", new Object[]{Integer.valueOf(iNewHour - 12)});
                        } else if (iNewHour == 24) {
                            strTime = "\uc624\uc804 12\uc2dc";
                        } else {
                            strTime = String.format("\uc624\uc804 %d\uc2dc", new Object[]{Integer.valueOf(iNewHour - 24)});
                        }
                        object = new Bundle();
                        object.putInt("cellType", 1);
                        object.putString("title", strTime);
                        this.timeTableList.add(object);
                    }
                    if (iNewHour == iNowHour) {
                        iHourType = 1;
                    } else if (iNewHour > iNowHour) {
                        iHourType = 2;
                    } else {
                        iHourType = 0;
                        iType1 = 0;
                        iType2 = 0;
                    }
                } else {
                    if (iNewHour1 == iNewHour2) {
                        strMinutes1 = dic1.getString("minutes");
                        strSecond1 = dic1.getString("second");
                        strWSub1 = dic1.getString("wSub");
                        strDest1 = dic1.getString("dest");
                        strExFlag1 = dic1.getString("exFlag");
                        strMinutes2 = dic2.getString("minutes");
                        strSecond2 = dic2.getString("second");
                        strWSub2 = dic2.getString("wSub");
                        strDest2 = dic2.getString("dest");
                        strExFlag2 = dic2.getString("exFlag");
                        iIndex1++;
                        iIndex2++;
                        iNewHour = iNewHour1;
                    } else if (iNewHour1 < iNewHour2) {
                        strMinutes1 = dic1.getString("minutes");
                        strSecond1 = dic1.getString("second");
                        strWSub1 = dic1.getString("wSub");
                        strDest1 = dic1.getString("dest");
                        strExFlag1 = dic1.getString("exFlag");
                        strMinutes2 = "";
                        strSecond2 = "";
                        strWSub2 = "0";
                        strDest2 = "";
                        strExFlag2 = "0";
                        iIndex1++;
                        iNewHour = iNewHour1;
                    } else if (iNewHour1 > iNewHour2) {
                        strMinutes1 = "";
                        strSecond1 = "";
                        strWSub1 = "0";
                        strDest1 = "";
                        strExFlag1 = "0";
                        strMinutes2 = dic2.getString("minutes");
                        strSecond2 = dic2.getString("second");
                        strWSub2 = dic2.getString("wSub");
                        strDest2 = dic2.getString("dest");
                        strExFlag2 = dic2.getString("exFlag");
                        iIndex2++;
                        iNewHour = iNewHour2;
                    }
                    if (iType1 >= 1) {
                        iType1 = 2;
                    } else if (iHourType == 1) {
                        if (strMinutes1.length() > 0 && Integer.valueOf(strMinutes1).intValue() >= iNowMinutes) {
                            iType1 = 1;
                            if (this.iItemRow == 0) {
                                this.iItemRow = this.timeTableList.size();
                            }
                        }
                    } else if (iHourType == 2) {
                        iType1 = 1;
                        if (this.iItemRow == 0) {
                            this.iItemRow = this.timeTableList.size();
                        }
                    }
                    if (iType2 >= 1) {
                        iType2 = 2;
                    } else if (iHourType == 1) {
                        if (strMinutes2.length() > 0 && Integer.valueOf(strMinutes2).intValue() >= iNowMinutes) {
                            iType2 = 1;
                            if (this.iItemRow == 0) {
                                this.iItemRow = this.timeTableList.size();
                            }
                        }
                    } else if (iHourType == 2) {
                        iType2 = 1;
                        if (this.iItemRow == 0) {
                            this.iItemRow = this.timeTableList.size();
                        }
                    }
                    if (bFlag1.booleanValue() || bFlag2.booleanValue()) {
                        object = new Bundle();
                        object.putInt("cellType", 2);
                        object.putInt("type1", iType1);
                        object.putInt("type2", iType2);
                        object.putString("minutes1", strMinutes1);
                        object.putString("second1", strSecond1);
                        object.putString("wSub1", strWSub1);
                        object.putString("dest1", strDest1);
                        object.putString("exFlag1", strExFlag1);
                        object.putString("minutes2", strMinutes2);
                        object.putString("second2", strSecond2);
                        object.putString("wSub2", strWSub2);
                        object.putString("dest2", strDest2);
                        object.putString("exFlag2", strExFlag2);
                        this.timeTableList.add(object);
                    }
                }
            }
        }
    }
}
