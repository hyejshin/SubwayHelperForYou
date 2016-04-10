package com.example.hyejung.easysubway.process;

import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.ArrayAdapter;
import com.example.hyejung.easysubway.MainActivity;
import com.example.hyejung.easysubway.DBmodel.AppDataManager;
import com.example.hyejung.easysubway.DBmodel.SearchDataModel;
import com.example.hyejung.easysubway.DBmodel.TimeDataManager;
//import com.example.hyejung.easysubway.popupviews.searchresultsdetailview.SearchResultsDetailViewActivity;
import java.util.ArrayList;
import java.util.Iterator;

public class SearchDataManager {
    static SearchDataManager _shared = null;
    private AppDataManager appManager = null;
    ArrayList<Bundle> arrTimeTableData = null;
    Boolean bEndTimeFlag;
    Boolean bSearchPathFalg;
    Boolean bSettingTimeDataFlag;
    Boolean bSettingTimeOptionFlag;
    Bundle dicSearchPointData;
    Bundle dicTimeData;
    float fTotalDistance;
    int iFinishPoint;
    int iPassPoint;
    int iStartPoint;
    private Context mContext = null;
    private ProgressDialog mProgress = null;
    private Context mSearchResultViewContext = null;
    SearchDataModel searchDataModel;
    //private SearchPathProcess searchProc = null;
    private TimeDataManager timeManager = null;

    private class DoComplecatedSearchPath extends AsyncTask<Integer, Integer, Integer> {
        private DoComplecatedSearchPath() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
            SearchDataManager.this.showProgressDialog();
        }

        protected Integer doInBackground(Integer... params) {
            SystemClock.sleep(10);
            SearchDataManager.this._startSearchPathEvent(SearchDataManager.this.dicSearchPointData);
            return Integer.valueOf(0);
        }

        protected void onPostExecute(Integer result) {
            SearchDataManager.this.closeProgressDialog();
            if (SearchDataManager.this.mContext != null) {
                //((MainActivity) SearchDataManager.this.mContext).onResSearchPathData(SearchDataManager.this.bSearchPathFalg);
            }
            if (SearchDataManager.this.mSearchResultViewContext != null) {
            
            }
        }
    }

    private class DoComplecatedSearchPathPotin extends AsyncTask<Integer, Integer, Integer> {
        private DoComplecatedSearchPathPotin() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
            SearchDataManager.this.showProgressDialog();
        }

        protected Integer doInBackground(Integer... params) {
            SystemClock.sleep(10);
            SearchDataManager.this._startSearchInfoDataProc();
            return Integer.valueOf(0);
        }

        protected void onPostExecute(Integer result) {
            SearchDataManager.this.closeProgressDialog();
            if (SearchDataManager.this.mContext != null) {
                //((MainActivity) SearchDataManager.this.mContext).onResSearchPathData(SearchDataManager.this.bSearchPathFalg);
            }
            if (SearchDataManager.this.mSearchResultViewContext != null) {
             }
        }
    }

    public static SearchDataManager shared() {
        synchronized (SearchDataManager.class) {
            if (_shared == null) {
                _shared = new SearchDataManager();
            }
        }
        return _shared;
    }

    public void initSearchDataManager(Context context) {
        this.mContext = context;
        this.appManager = AppDataManager.shared();
        this.timeManager = TimeDataManager.shared();
        /*this.searchProc = SearchPathProcess.shared();
        this.searchProc.initSearchProcess();*/
        clearData();
    }

    public void setSearchResultViewContext(Context context) {
        this.mSearchResultViewContext = context;
    }

    public void resetData() {
        //this.searchProc.resetData();
        clearData();
    }

    public Boolean isSearchPathFalg() {
        return this.bSearchPathFalg;
    }

    public void clearData() {
        this.bSearchPathFalg = Boolean.valueOf(false);
        this.fTotalDistance = 0.0f;
        this.bEndTimeFlag = Boolean.valueOf(false);
        this.bSettingTimeDataFlag = Boolean.valueOf(false);
        this.bSettingTimeOptionFlag = Boolean.valueOf(false);
        this.fTotalDistance = 0.0f;
        this.iStartPoint = 0;
        this.iFinishPoint = 0;
        this.iPassPoint = 0;
        this.timeManager.releaseTimeData();
        if (this.dicTimeData != null) {
            this.dicTimeData.clear();
            this.dicTimeData = null;
        }
        if (this.dicSearchPointData != null) {
            this.dicSearchPointData.clear();
            this.dicSearchPointData = null;
        }
        //this.searchProc.clearData();
        clearSearchPathModel();
    }

    public SearchDataModel getSearchPathModel() {
        return this.searchDataModel;
    }

    public void clearSearchPathModel() {
        if (this.searchDataModel == null) {
            this.searchDataModel = new SearchDataModel();
        } else {
            this.searchDataModel.clear();
        }
    }

    public void startSearchPathAnmation() {
        if (this.mContext == null) {
            return;
        }
        if (this.searchDataModel.getAniCurrentPoint() < this.searchDataModel.getAniTotalPoint()) {
            //((MainActivity) this.mContext).onSearchPathAnmation(this.searchDataModel.getCurrentPoint());
            return;
        }
        //((MainActivity) this.mContext).onStopSearchPathAnmation(null);
    }

    public float getfTotalDistance() {
        return this.fTotalDistance;
    }

    public void setfTotalDistance(float fTotalDistance) {
        this.fTotalDistance = fTotalDistance;
    }

    public int getStartPoint() {
        return this.iStartPoint;
    }

    public void setStartPoint(int iStartPoint) {
        this.iStartPoint = iStartPoint;
    }

    public int getFinishPoint() {
        return this.iFinishPoint;
    }

    public void setFinishPoint(int iFinishPoint) {
        this.iFinishPoint = iFinishPoint;
    }

    public int getPassPoint() {
        return this.iPassPoint;
    }

    public void setPassPoint(int iPassPoint) {
        this.iPassPoint = iPassPoint;
    }

    public Boolean getEndTimeFlag() {
        return this.bEndTimeFlag;
    }

    public void setEndTimeFlag(Boolean bEndTimeFlag) {
        this.bEndTimeFlag = bEndTimeFlag;
    }

    public Bundle getNowTimeData() {
        return this.timeManager.getNowTimeData(this.appManager.getWeekType());
    }

    public Bundle getNewNowTimeData() {
        if (this.dicTimeData != null) {
            this.dicTimeData.clear();
            this.dicTimeData = null;
        }
        Bundle dic = this.timeManager.getNowTimeData(this.appManager.getWeekType());
        if (dic.size() > 0) {
            this.dicTimeData = new Bundle(dic);
        }
        dic.clear();
        return this.dicTimeData;
    }

    public void setNewNowTimeData(String strTime) {
        this.dicTimeData.clear();
        String[] arrivalArray = strTime.split(":");
        if (arrivalArray.length > 2) {
            this.dicTimeData = new Bundle();
            this.dicTimeData.putInt("hour", Integer.valueOf(arrivalArray[0]).intValue());
            this.dicTimeData.putInt("minutes", Integer.valueOf(arrivalArray[1]).intValue());
            this.dicTimeData.putInt("seconds", Integer.valueOf(arrivalArray[2]).intValue());
            this.dicTimeData.putInt("week", this.appManager.getWeekType());
            this.dicTimeData.putString("startTimeData", strTime);
        }
    }

    public void setNewTimeData(Bundle bundle) {
        if (bundle != null) {
            this.dicTimeData.clear();
            this.dicTimeData = new Bundle(bundle);
            this.dicTimeData.putInt("week", this.appManager.getWeekType());
            String timeData = bundle.getString("timeData");
            if (timeData == null) {
                return;
            }
            if (timeData.contains("(\uae09")) {
                if (this.appManager.getSearchPathType() != 2) {
                    this.appManager.setSearchPathType(2);
                }
            } else if (this.appManager.getSearchPathType() == 2) {
                this.appManager.setSearchPathType(0);
            }
        }
    }

    public Bundle getOldNowTimeData() {
        if (this.dicTimeData == null) {
            getNewNowTimeData();
        }
        return this.dicTimeData;
    }

    public Bundle getSearchPointData() {
        return this.dicSearchPointData;
    }

    public void setSearchPointData(Bundle bundle) {
        if (bundle == null) {
            this.dicSearchPointData.clear();
        }
        if (this.dicSearchPointData == null) {
            this.dicSearchPointData = new Bundle(bundle);
        } else if (!this.dicSearchPointData.equals(bundle)) {
            this.dicSearchPointData.clear();
            this.dicSearchPointData = new Bundle(bundle);
        }
    }

    public void startSearchPathEvent(Bundle dicData, Boolean history, Boolean reset) {
        if (dicData != null && dicData.size() > 0) {
            this.bEndTimeFlag = Boolean.valueOf(false);
            if (reset.booleanValue()) {
                this.appManager.resetSearchTimeTypeDefaults();
                this.appManager.resetNowPointInfoFlagDefaults();
                this.appManager.setSearchPathType(0);
                this.fTotalDistance = 0.0f;
            }
            setSearchPointData(dicData);
            if (history.booleanValue()) {
                this.appManager.insertHistoryData(dicData);
            }
            new DoComplecatedSearchPath().execute(new Integer[]{Integer.valueOf(0)});
        }
    }

    public void reStartSearchInfoDataProc() {
        this.searchDataModel.clearPathIcon();
        new DoComplecatedSearchPathPotin().execute(new Integer[]{Integer.valueOf(0)});
    }

    public void _reStartSearchPathEvent(Bundle dicData) {
        if (dicData != null && dicData.size() > 0) {
            setSearchPointData(dicData);
            _startSearchPathEvent(this.dicSearchPointData);
        }
    }

    public void reSettingSearchPathEvent(Bundle dic, Boolean bStart, Boolean history) {
        if (this.dicSearchPointData != null) {
            ArrayList<Bundle> arr = this.appManager.getArrStationPoint02(dic.getString("stationCode"));
            if (arr.size() > 0) {
                Bundle dicStartData = (Bundle) arr.get(0);
                Bundle dicData = new Bundle();
                dicData.putString("startCode", dicStartData.getString("stationCode"));
                dicData.putString("stationName", dicStartData.getString("stationName"));
                dicData.putString("lineCode", dicStartData.getString("lineCode"));
                dicData.putString("areaCode", dicStartData.getString("areaCode"));
                dicData.putString("subCode", dicStartData.getString("subCode"));
                dicData.putString("finishCode", dicStartData.getString("finishCode"));
                dicData.putString("finishName", dicStartData.getString("finishName"));
                dicData.putString("finishLineCode", dicStartData.getString("finishLineCode"));
                dicData.putString("finishAreaCode", dicStartData.getString("finishAreaCode"));
                dicData.putString("finishSubCode", dicStartData.getString("finishSubCode"));
                dicData.putString("passCode", "");
                dicData.putString("passName", "");
                dicData.putString("passLineCode", "");
                dicData.putString("passAreaCode", "");
                dicData.putString("passSubCode", "");
                dicData.putInt("areaCode", this.appManager.getAreaCode());
                if (history.booleanValue()) {
                    this.appManager.insertHistoryData(dicData);
                }
                _reStartSearchPathEvent(dicData);
            }
            arr.clear();
        }
    }

    private void _startSearchPathEvent(Bundle dicData) {
        clearSearchPathModel();
        /*if (this.searchProc.startSearchPathEvent(dicData).booleanValue()) {
            _startSearchInfoDataProc();
        }*/
    }

    private void _startSearchInfoDataProc() {
        getSearchPathListData(this.appManager.getSearchTimeType());
    }

    private void showProgressDialog() {
        if (this.mProgress == null) {
            this.mProgress = new ProgressDialog(this.mContext);
        }
        if (this.mSearchResultViewContext != null) {
            this.mProgress = ProgressDialog.show(this.mSearchResultViewContext, null, "\uc7a0\uc2dc\ub9cc \uae30\ub2e4\ub824 \uc8fc\uc138\uc694.", true);
        } else {
            this.mProgress = ProgressDialog.show(this.mContext, null, "\uc7a0\uc2dc\ub9cc \uae30\ub2e4\ub824 \uc8fc\uc138\uc694.", true);
        }
    }

    private void closeProgressDialog() {
        if (this.mProgress != null) {
            this.mProgress.dismiss();
        }
    }

    public Boolean getSearchInfoToTimeTable(int type) {
        Boolean bFlag = Boolean.valueOf(true);
        if (this.searchDataModel.getArrSearchPathData() == null) {
            bFlag = Boolean.valueOf(false);
            if (this.appManager.getSearchTimeType() == 1 && type == 0) {
                bFlag = getSearchPathListData(1);
            }
            else {
             
            }
            if (bFlag.booleanValue()) {
               
            }
        }
        return bFlag;
    }

    public Boolean getSearchPathListData(int type) {
        Boolean bFlag = Boolean.valueOf(false);
        if (this.searchDataModel.getArrPathIcon() == null) {
            
        }
        return bFlag;
    }

    public void checkSubwayStartTime(Bundle bundle) {
        setNewTimeData(bundle);
        if (this.mContext != null) {
            ((MainActivity) this.mContext).onStartSearchTime();
        }
    }

    public void checkSubwayFinishTime(Bundle bundle) {
        //checkSubwayFinishTime(bundle.getString("startTimeData"));
        if (this.mContext != null) {
            ((MainActivity) this.mContext).onStartSearchTime();
        }
    }

    public void checkSubwayFirstTime() {
        //checkSubwayFitstTime();
        if (this.mContext != null) {
            ((MainActivity) this.mContext).onStartSearchTime();
        }
    }

    public void checkSubwayLastTime() {
        //checkSubwayLastTime();
        if (this.mContext != null) {
            ((MainActivity) this.mContext).onStartSearchTime();
        }
    }

    public void showTimeTableDataDialog(Context context, int type, Boolean buttonFlag) {
        if (getSearchInfoToTimeTable(0).booleanValue()) {
            ArrayList<Bundle> arrData;
            Bundle dicData;
            if (this.arrTimeTableData == null) {
                this.arrTimeTableData = new ArrayList();
            } else {
                this.arrTimeTableData.clear();
            }
            if (type == 2) {
                arrData = this.searchDataModel.getArrSearchPathData();
                dicData = (Bundle) arrData.get(arrData.size() - 1);
            } else {
                dicData = (Bundle) this.searchDataModel.getArrSearchPathData().get(0);
            }
            int lCode = Integer.valueOf(dicData.getString("lineCode")).intValue();
            int iTime1 = 0;
            int iTime2 = 0;
            int code = Integer.valueOf(dicData.getString("stationCode")).intValue();
            int direction = dicData.getInt("direction");
            int week = this.appManager.getWeekType() + 1;
            String timeData = dicData.getString("nowTime");
            if (!(timeData == null || timeData.contains("\uc6b4\ud589\uc885\ub8cc"))) {
                String[] strArray = timeData.split(":");
                if (strArray.length > 1) {
                    iTime1 = Integer.valueOf(strArray[0]).intValue();
                    iTime2 = Integer.valueOf(strArray[1]).intValue();
                }
            }
            arrData = this.appManager.getArrStationTimeDataToPicker(code, week, lCode, direction);
            if (arrData.size() > 0) {
                _showTimeTableData(type, buttonFlag, arrData, iTime1, iTime2, context);
            } else {
                this.appManager.showToastMsg("\uc2dc\uac04\uc815\ubcf4\uac00 \uc5c6\uc2b5\ub2c8\ub2e4.");
            }
        }
    }

    private void _showTimeTableData(int viewType, Boolean buttonFlag, ArrayList<Bundle> arrData, int iTime1, int iTime2, Context context) {
        String strTitle;
        String strButton;
        final int i;// = viewType;
        int iSelectedIndex = 0;
        int iRowCount = 0;
        Boolean bFlag = Boolean.valueOf(true);
        ArrayAdapter<String> items = new ArrayAdapter(context, 17367058);
        Iterator it = arrData.iterator();
        while (it.hasNext()) {
            Bundle data = (Bundle) it.next();
            String dName = data.getString("dName");
            if (Integer.valueOf(data.getString("exFlag")).intValue() == 1) {
                dName = "(\uae09)" + dName;
            }
            if (dName.length() < 7 && !dName.contains("\uc21c\ud658")) {
                dName = new StringBuilder(String.valueOf(dName)).append("\ud589").toString();
            }
            int hour = Integer.valueOf(data.getString("hour")).intValue();
            int tData = Integer.valueOf(data.getString("tData")).intValue();
            int tSub = Integer.valueOf(data.getString("tSub")).intValue();
            int wSub = Integer.valueOf(data.getString("wSub")).intValue();
            if (!(wSub == 88 || wSub == 99)) {
                tSub += wSub;
            }
            if (tSub > 59) {
                tData += tSub / 60;
                tSub %= 60;
                if (tData > 59) {
                    tData -= 60;
                    hour++;
                }
            }
            String strData2 = String.format("%02d:%02d:%02d", new Object[]{Integer.valueOf(hour), Integer.valueOf(tData), Integer.valueOf(tSub)});
            String strData = new StringBuilder(String.valueOf(strData2)).append(" - ").append(dName).toString();
            items.add(strData);
            Bundle object = new Bundle();
            object.putInt("hour", Integer.valueOf(data.getString("hour")).intValue());
            object.putInt("minutes", Integer.valueOf(data.getString("tData")).intValue());
            object.putInt("seconds", Integer.valueOf(data.getString("tSub")).intValue());
            object.putString("timeData", strData);
            object.putString("startTimeData", strData2);
            this.arrTimeTableData.add(object);
            if (bFlag.booleanValue()) {
                if (hour > iTime1) {
                    bFlag = Boolean.valueOf(false);
                    iSelectedIndex = iRowCount;
                } else if (hour == iTime1 && tData >= iTime2) {
                    bFlag = Boolean.valueOf(false);
                    iSelectedIndex = iRowCount;
                }
                iRowCount++;
            }
        }
        if (viewType == 2) {
            strTitle = "\ub3c4\ucc29\uc2dc\uac04 \ubcc0\uacbd";
            strButton = "\ucd9c\ubc1c\uc2dc\uac04 \ubcc0\uacbd";
        } else {
            strTitle = "\ucd9c\ubc1c\uc2dc\uac04 \ubcc0\uacbd";
            strButton = "\ub3c4\ucc29\uc2dc\uac04 \ubcc0\uacbd";
        }
        Builder builder = new Builder(context);
        builder.setTitle(strTitle);
        if (buttonFlag.booleanValue()) {
            i = viewType;
            builder.setNeutralButton(strButton, new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    int type = 2;
                    if (i == 2) {
                        type = 1;
                    }
                    SearchDataManager.this.showTimeTableDataDialog(SearchDataManager.this.mContext, type, Boolean.valueOf(true));
                }
            });
        }
        final int j = viewType;
        builder.setSingleChoiceItems(items, iSelectedIndex, new OnClickListener() {
            public void onClick(DialogInterface dialog, int index) {
                SearchDataManager.this.appManager.setSearchTimeType(0);
                Bundle bundle = (Bundle) SearchDataManager.this.arrTimeTableData.get(index);
                if (j == 2) {
                    SearchDataManager.this.checkSubwayFinishTime(bundle);
                } else {
                    SearchDataManager.this.checkSubwayStartTime(bundle);
                }
                dialog.dismiss();
            }
        }).setPositiveButton("\uccab\ucc28\uacc4\uc0b0", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SearchDataManager.this.appManager.setSearchTimeType(0);
                SearchDataManager.this.checkSubwayFirstTime();
            }
        }).setNegativeButton("\ub9c9\ucc28\uacc4\uc0b0", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SearchDataManager.this.appManager.setSearchTimeType(0);
                SearchDataManager.this.checkSubwayLastTime();
            }
        });
        builder.create().show();
    }
}
