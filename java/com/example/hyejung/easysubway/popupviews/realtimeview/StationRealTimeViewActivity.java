package com.example.hyejung.easysubway.popupviews.realtimeview;

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
import com.example.hyejung.easysubway.apputils.SiteScanLoader;
import com.example.hyejung.easysubway.apputils.SiteScanLoader.SiteScanTimeLoaderListener;
import com.example.hyejung.easysubway.cellRow.StationRealTimeDescRow;
import com.example.hyejung.easysubway.cellRow.StationRealTimeHeaderRow;
import com.example.hyejung.easysubway.cellRow.StationRealTimeNoDataRow;
import com.example.hyejung.easysubway.cellRow.StationRealTimeRow01;
import com.example.hyejung.easysubway.cellRow.StationRealTimeRow02;
import com.example.hyejung.easysubway.DBmodel.AppDataManager;
import com.example.hyejung.easysubway.DBmodel.PointDataManager;
import com.example.hyejung.easysubway.DBmodel.RealTimeCellModel;
import com.example.hyejung.easysubway.DBmodel.RealTimeDataModel;
import com.example.hyejung.easysubway.subviews.StationNameSubView;
import com.example.hyejung.easysubway.subviews.StationNameSubView.StationNameViewListener;
import java.util.ArrayList;

public class StationRealTimeViewActivity extends Activity implements OnClickListener, SiteScanTimeLoaderListener, StationNameViewListener {
    AppDataManager appManager = null;
    ArrayList<RealTimeCellModel> arrTimeList = null;
    int iLineCode = 0;
    int iStationCode = 0;
    int iViewType = 0;
    int iWeekCode = 0;
    ArrayAdapter<RealTimeCellModel> listData = null;
    int nextCode = 1;
    int prevCode = 0;
    SiteScanLoader siteScanLoader = null;
    StationNameSubView stationNameView = null;
    String strNextName = null;
    String strPrevName = null;
    String strStationName = null;

    public class RealTimeListAdapter extends ArrayAdapter<RealTimeCellModel> {
        Activity context;

        public RealTimeListAdapter(Activity context, int textViewResourceId, ArrayList<RealTimeCellModel> objects) {
            super(context, textViewResourceId, objects);
            this.context = context;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View cell = convertView;
            RealTimeCellModel model = (RealTimeCellModel) StationRealTimeViewActivity.this.arrTimeList.get(position);
            if (model.getCellType() == 0) {
                StationRealTimeHeaderRow rowView;
                if (cell == null || cell.getId() != R.layout.cell_station_real_time_header_row) {
                    cell = this.context.getLayoutInflater().inflate(R.layout.cell_station_real_time_header_row, null);
                    rowView = new StationRealTimeHeaderRow(cell);
                    cell.setTag(rowView);
                    cell.setId(R.layout.cell_station_real_time_header_row);
                } else {
                    rowView = (StationRealTimeHeaderRow) cell.getTag();
                }
                rowView.setModelData(model);
            } else if (model.getCellType() == 1) {
                StationRealTimeRow01 rowView2;
                if (cell == null || cell.getId() != R.layout.cell_station_real_time_row01) {
                    cell = this.context.getLayoutInflater().inflate(R.layout.cell_station_real_time_row01, null);
                    rowView2 = new StationRealTimeRow01(cell);
                    cell.setTag(rowView2);
                    cell.setId(R.layout.cell_station_real_time_row01);
                } 
                else {
                    rowView2 = (StationRealTimeRow01) cell.getTag();
                }
                rowView2.setModelData(StationRealTimeViewActivity.this, model);
            } else if (model.getCellType() == 2) {
                StationRealTimeRow02 rowView3;
                if (cell == null || cell.getId() != R.layout.cell_station_real_time_row02) {
                    cell = this.context.getLayoutInflater().inflate(R.layout.cell_station_real_time_row02, null);
                    rowView3 = new StationRealTimeRow02(cell);
                    cell.setTag(rowView3);
                    cell.setId(R.layout.cell_station_real_time_row02);
                } else {
                    rowView3 = (StationRealTimeRow02) cell.getTag();
                }
                rowView3.setModelData(StationRealTimeViewActivity.this, model);
            } else if (model.getCellType() == 3) {
                StationRealTimeDescRow rowView4;
                if (cell == null || cell.getId() != R.layout.cell_station_real_time_desc_row) {
                    cell = this.context.getLayoutInflater().inflate(R.layout.cell_station_real_time_desc_row, null);
                    rowView4 = new StationRealTimeDescRow(cell);
                    cell.setTag(rowView4);
                    cell.setId(R.layout.cell_station_real_time_desc_row);
                } else {
                    rowView4 = (StationRealTimeDescRow) cell.getTag();
                }
                rowView4.setModelData(model);
            } else if (model.getCellType() == 9) {
                StationRealTimeNoDataRow rowView5;
                if (cell == null || cell.getId() != R.layout.cell_station_real_time_nodata_row) {
                    cell = this.context.getLayoutInflater().inflate(R.layout.cell_station_real_time_nodata_row, null);
                    rowView5 = new StationRealTimeNoDataRow(cell);
                    cell.setTag(rowView5);
                    cell.setId(R.layout.cell_station_real_time_nodata_row);
                } else {
                    rowView5 = (StationRealTimeNoDataRow) cell.getTag();
                }
                rowView5.setTitleData(model.getTitle());
            }
            return cell;
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_real_time_view);
        this.appManager = AppDataManager.shared();
        initTableBar();
        initListView();
        initStationNameView();
    }

    private void initTableBar() {
        LinearLayout titlebar = (LinearLayout) findViewById(R.id.titleBer);
        View barIndicator = LayoutInflater.from(this).inflate(R.layout.bar_titlebar_3button, null);
        ((TextView) barIndicator.findViewById(R.id.title)).setText("\ub3c4\ucc29\uc815\ubcf4");
        Button button = (Button) barIndicator.findViewById(R.id.navi_titlebar_3button1);
        button.setText("\ub2eb\uae30");
        button.setOnClickListener(this);
        titlebar.addView(barIndicator);
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
        this.arrTimeList = new ArrayList();
        this.listData = new RealTimeListAdapter(this, R.layout.cell_subway_station_list_row, this.arrTimeList);
        ((ListView) findViewById(R.id.listView)).setAdapter(this.listData);
    }

    public void onClick(View arg0) {
        if (arg0.getId() == R.id.navi_titlebar_3button1) {
            cmdTableBer_Button3();
        }
    }

    private void cmdTableBer_Button3() {
        finish();
    }

    public void onSelectedTabButton(Bundle bundle) {
        this.iStationCode = Integer.valueOf(bundle.getString("stationCode")).intValue();
        this.iLineCode = Integer.valueOf(bundle.getString("lineCode")).intValue();
        this.strStationName = bundle.getString("stationName");
        this.strPrevName = bundle.getString("nStationName1");
        this.strNextName = bundle.getString("nStationName2");
        checkPrevNextCode(this.iLineCode);
        getSiteScanData();
    }

    public void checkPrevNextCode(int lineCode) {
        switch (lineCode) {
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
        initSiteScanLoader();
        this.siteScanLoader.getSiteScanTimeData(this.iStationCode, Boolean.valueOf(true));
    }

    private void initSiteScanLoader() {
        if (this.siteScanLoader == null) {
            this.siteScanLoader = new SiteScanLoader(this);
            this.siteScanLoader.setSiteScanTimeLoaderListener(this);
        }
    }

    private void removeSiteScanLoader() {
        if (this.siteScanLoader != null) {
            this.siteScanLoader.clearDicAppData();
            this.siteScanLoader.setSiteScanTimeLoaderListener(null);
            this.siteScanLoader = null;
        }
    }

    public void onResSiteScanTimeData(ArrayList<RealTimeDataModel> arrayList) {
        RealTimeCellModel cellModel;
        this.arrTimeList.clear();
        if (arrayList != null) {
            String title;
            String strInfo;
            ArrayList<RealTimeCellModel> arrData01 = new ArrayList();
            ArrayList<RealTimeCellModel> arrData02 = new ArrayList();
            ArrayList<Bundle> arrSubData01 = new ArrayList();
            ArrayList<Bundle> arrSubData02 = new ArrayList();
            boolean bFlag = Integer.valueOf(PointDataManager.shared().getSelectedData().getString("stationCode")).intValue() == 10201;
            for (int i = 0; i < arrayList.size(); i++) {
                RealTimeDataModel model = (RealTimeDataModel) arrayList.get(i);
                String subTime = null;
                title = model.getbStatnNm().replace(")\ud589", ")");
                int curs = Integer.valueOf(model.getCurstatnsn()).intValue();
                if (curs < 11) {
                    String subTitle;
                    String subDesc;
                    String strCurs;
                    if (curs > 0) {
                        subTitle = model.getCurstatnsn() + "\uac1c\uc5ed \uc804";
                        subDesc = subTitle;
                        int time = Integer.valueOf(model.getbArvlTm()).intValue();
                        if (time > 0) {
                            int min = (time % 3600) / 60;
                            int sec = (time % 3600) % 60;
                            if (min > 0) {
                                subTitle = new StringBuilder(String.valueOf(subTitle)).append(String.format(" (%d\ubd84 %02d\ucd08\ud6c4 \ub3c4\ucc29\uc608\uc815)", new Object[]{Integer.valueOf(min), Integer.valueOf(sec)})).toString();
                                if (sec > 30) {
                                    min++;
                                }
                                subTime = String.format("\uc57d%d\ubd84\ud6c4", new Object[]{Integer.valueOf(min)});
                            } else {
                                subTitle = new StringBuilder(String.valueOf(subTitle)).append(String.format(" (%02d\ucd08\ud6c4 \ub3c4\ucc29\uc608\uc815)", new Object[]{Integer.valueOf(sec)})).toString();
                                subTime = String.format("\uc57d%d\ucd08\ud6c4", new Object[]{Integer.valueOf(sec)});
                            }
                        } else {
                            String strData = model.getArvlMsg3();
                            if (strData != null && strData.length() > 0) {
                                subTime = strData;
                                subTitle = new StringBuilder(String.valueOf(subTitle)).append(" (").append(strData).append(")").toString();
                            }
                        }
                    } else {
                        subTitle = model.getArvlMsg2();
                        subDesc = subTitle;
                    }
                    if (subTitle == null) {
                        subTitle = "";
                    }
                    cellModel = new RealTimeCellModel();
                    cellModel.setCellType(3);
                    cellModel.setTitle(title);
                    cellModel.setSubTitle(subTitle);
                    cellModel.setEndFlag(0);
                    subDesc = new StringBuilder(String.valueOf(title)).append(" (").append(subDesc).append(")").toString();
                    if (this.prevCode != Integer.valueOf(model.getTrainLine()).intValue() || arrData01 == null) {
                        if (this.nextCode == Integer.valueOf(model.getTrainLine()).intValue() && arrData02 != null && (!bFlag || model.getbStatnNm().contains("\uc11c\uc6b8"))) {
                            arrData02.add(cellModel);
                        }
                    } else {
                        arrData01.add(cellModel);
                    }
                    if (subTime == null) {
                        subTime = title;
                    }
                    if (curs > 0) {
                        strCurs = String.format("%d\uc804", new Object[]{Integer.valueOf(curs)});
                    } else if (subTitle.contains("\uc9c4\uc785")) {
                        strCurs = "\uc9c4\uc785";
                    } else if (subTitle.contains("\ucd9c\ubc1c")) {
                        strCurs = "\ucd9c\ubc1c";
                    } else {
                        strCurs = "\ub3c4\ucc29";
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString("time", subTime);
                    bundle.putString("curs", strCurs);
                    bundle.putString("desc", subDesc);
                    bundle.putInt("curValue", curs);
                    if (this.prevCode != Integer.valueOf(model.getTrainLine()).intValue() || arrSubData01 == null) {
                        if (this.nextCode == Integer.valueOf(model.getTrainLine()).intValue() && arrSubData02 != null && (!bFlag || model.getbStatnNm().contains("\uc11c\uc6b8"))) {
                            arrSubData02.add(bundle);
                        }
                    } else {
                        arrSubData01.add(bundle);
                    }
                }
            }
            if (this.strPrevName != null) {
                if (this.strPrevName.length() > 1) {
                    title = this.strPrevName + " \ubc29\ud5a5";
                } else {
                    title = "\uc885\ucc29\uc5ed";
                }
                if (arrData01.size() > 0) {
                    strInfo = ((Bundle) arrSubData01.get(0)).getString("desc");
                    if (strInfo == null) {
                        strInfo = "";
                    }
                } else {
                    strInfo = "\ub3c4\ucc29\uc815\ubcf4 \uc5c6\uc74c";
                }
                cellModel = new RealTimeCellModel();
                cellModel.setCellType(1);
                cellModel.setInfo(strInfo);
                cellModel.setArrData(arrSubData01);
                cellModel.setLineCode(this.iLineCode);
                cellModel.setStationName(this.strStationName);
                if (this.iViewType == 1) {
                    this.arrTimeList.add(cellModel);
                } else {
                    arrData01.add(0, cellModel);
                    cellModel = new RealTimeCellModel();
                    cellModel.setCellType(0);
                    cellModel.setTitle(title);
                    cellModel.setTrainLine(0);
                    arrData01.add(0, cellModel);
                    this.arrTimeList.addAll(arrData01);
                    if (arrData01.size() <= 1) {
                        cellModel = new RealTimeCellModel();
                        cellModel.setCellType(3);
                        cellModel.setEndFlag(1);
                        this.arrTimeList.add(cellModel);
                    }
                }
            }
            if (this.strNextName != null) {
                if (this.strNextName.length() > 1) {
                    title = this.strNextName + " \ubc29\ud5a5";
                } else {
                    title = "\uc885\ucc29\uc5ed";
                }
                if (arrData02.size() > 0) {
                    strInfo = ((Bundle) arrSubData02.get(0)).getString("desc");
                    if (strInfo == null) {
                        strInfo = "";
                    }
                } 
                else {
                    strInfo = "\ub3c4\ucc29\uc815\ubcf4 \uc5c6\uc74c";
                }
                cellModel = new RealTimeCellModel();
                cellModel.setCellType(2);
                cellModel.setInfo(strInfo);
                cellModel.setArrData(arrSubData02);
                cellModel.setLineCode(this.iLineCode);
                cellModel.setStationName(this.strStationName);
                if (this.iViewType == 1) {
                    this.arrTimeList.add(cellModel);
                } else {
                    arrData02.add(0, cellModel);
                    cellModel = new RealTimeCellModel();
                    cellModel.setCellType(0);
                    cellModel.setTitle(title);
                    cellModel.setTrainLine(1);
                    arrData02.add(0, cellModel);
                    this.arrTimeList.addAll(arrData02);
                    if (arrData02.size() <= 1) {
                        cellModel = new RealTimeCellModel();
                        cellModel.setCellType(3);
                        cellModel.setEndFlag(1);
                        this.arrTimeList.add(cellModel);
                    }
                }
            }
        }
        if (this.arrTimeList.size() == 0) {
            cellModel = new RealTimeCellModel();
            cellModel.setCellType(9);
            cellModel.setTitle("\ub3c4\ucc29\uc815\ubcf4\ub97c \ubc1b\uc544\uc624\uc9c0 \ubabb\ud588\uc2b5\ub2c8\ub2e4.\n\uc7a0\uc2dc\ud6c4 \ub2e4\uc2dc \uc774\uc6a9\ud574 \uc8fc\uc2dc\uae30 \ubc14\ub78d\ub2c8\ub2e4.");
            this.arrTimeList.add(cellModel);
        }
        this.listData.notifyDataSetChanged();
        removeSiteScanLoader();
    }
}
