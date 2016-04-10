package com.example.hyejung.easysubway.DBmodel;

import android.os.Bundle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class TimeDataManager {
    static TimeDataManager _shared = null;
    AppDataManager appManager = null;
    int iNowHour = 0;
    int iNowMinutes = 0;
    int iNowSeconds = 0;
    int iTimeValue = 0;
    int iWeekType;

    public static TimeDataManager shared() {
        synchronized (TimeDataManager.class) {
            if (_shared == null) {
                _shared = new TimeDataManager();
            }
        }
        return _shared;
    }

    public TimeDataManager() {
        _initNowTimeData();
    }

    private void _initNowTimeData() {
        this.appManager = AppDataManager.shared();
        this.iNowHour = 0;
        this.iNowMinutes = 0;
        this.iNowSeconds = 0;
        this.iTimeValue = 0;
        this.iWeekType = 0;
    }

    public void releaseTimeData() {
        this.iNowHour = 0;
        this.iNowMinutes = 0;
        this.iNowSeconds = 0;
        this.iTimeValue = 0;
        this.iWeekType = 0;
    }

    public Bundle getNowTimeData(int weekType) {
        String strTime;
        getNowTimeDate();
        if (this.appManager.getSearchTimeType() == 1) {
            strTime = String.format("%02d:%02d:%02d", new Object[]{Integer.valueOf(this.iNowHour), Integer.valueOf(this.iNowMinutes), Integer.valueOf(this.iNowSeconds)});
            if (this.iNowSeconds >= 30) {
                this.iNowSeconds -= 30;
            } else if (this.iNowMinutes >= 1) {
                this.iNowSeconds += 30;
                this.iNowMinutes--;
            } else {
                this.iNowHour--;
                this.iNowMinutes = 59;
                this.iNowSeconds += 30;
            }
        } else {
            strTime = String.format("%02d:%02d:%02d", new Object[]{Integer.valueOf(this.iNowHour), Integer.valueOf(this.iNowMinutes), Integer.valueOf(this.iNowSeconds)});
        }
        Bundle bundle = new Bundle();
        bundle.putInt("hour", this.iNowHour);
        bundle.putInt("minutes", this.iNowMinutes);
        bundle.putInt("seconds", this.iNowSeconds);
        bundle.putInt("wSub", 30);
        bundle.putInt("week", weekType);
        bundle.putString("startTimeData", strTime);
        this.iWeekType = weekType;
        return bundle;
    }

    private void getNowTimeDate() {
        Calendar c = Calendar.getInstance();
        this.iNowHour = c.get(11);
        this.iNowMinutes = c.get(12);
        this.iNowSeconds = c.get(13);
    }

    private void _getNowTimeData() {
        getNowTimeDate();
        if (this.iNowHour < 3) {
            this.iNowHour += 24;
        }
        this.iTimeValue = this.iNowHour * 60;
        this.iTimeValue = (this.iTimeValue + this.iNowMinutes) * 60;
        this.iTimeValue += this.iNowSeconds;
    }

    public ArrayList<Bundle> getStationTimeData(Bundle bundle, int weekCode, int limit) {
        if (bundle == null) {
            return null;
        }
        ArrayList<Bundle> arrTime;
        int iStationCode = Integer.valueOf(bundle.getString("stationCode")).intValue();
        int iLineCode = Integer.valueOf(bundle.getString("lineCode")).intValue();
        ArrayList<Bundle> arrTimeData1 = null;
        ArrayList<Bundle> arrTimeData2 = null;
        _getNowTimeData();
        int _iNowHour = this.iNowHour;
        int _iNowMinutes = this.iNowMinutes - 1;
        if (_iNowMinutes < 0) {
            _iNowHour--;
            _iNowMinutes += 60;
        }
        Bundle dicTime = new Bundle();
        dicTime.putInt("hour", _iNowHour);
        dicTime.putInt("minutes", _iNowMinutes);
        dicTime.putInt("week", weekCode);
        dicTime.putInt("limit", limit);
        int iTimeCount = this.appManager.getArrStationTimeDataToCheck(iStationCode, weekCode, iLineCode);
        if (Integer.valueOf(bundle.getString("nStationCode1")).intValue() != 9999 && iTimeCount > 2) {
            arrTime = this.appManager.getArrStationTimeDataToNowTime(dicTime, iStationCode, 1, iLineCode);
            if (arrTime.size() > 0) {
                arrTimeData1 = _setTimeOperation(arrTime);
            }
        }
        if (Integer.valueOf(bundle.getString("nStationCode2")).intValue() != 9999 && iTimeCount > 2) {
            arrTime = this.appManager.getArrStationTimeDataToNowTime(dicTime, iStationCode, 2, iLineCode);
            if (arrTime.size() > 0) {
                arrTimeData2 = _setTimeOperation(arrTime);
            }
        }
        ArrayList<Bundle> arrTimeData = new ArrayList();
        int iRowCount = 0;
        if (arrTimeData1 != null) {
            iRowCount = arrTimeData1.size();
        }
        if (arrTimeData2 != null && iRowCount < arrTimeData2.size()) {
            iRowCount = arrTimeData2.size();
        }
        if (iRowCount <= 0) {
            return arrTimeData;
        }
        Bundle object = null;
        Bundle bundle1 = null;
        Bundle bundle2 = null;
        for (int i = 0; i < iRowCount; i++) {
            if (arrTimeData1 != null) {
                if (arrTimeData1.size() > i) {
                    bundle1 = (Bundle) arrTimeData1.get(i);
                } else {
                    bundle1 = null;
                }
            }
            if (arrTimeData2 != null) {
                if (arrTimeData2.size() > i) {
                    bundle2 = (Bundle) arrTimeData2.get(i);
                } else {
                    bundle2 = null;
                }
            }
            if (bundle1 != null && bundle2 != null) {
                object = new Bundle();
                object.putInt("cellType", 1);
                object.putString("exFlag1", bundle1.getString("exFlag"));
                object.putString("timeTable1", bundle1.getString("timeTable"));
                object.putString("dName1", bundle1.getString("dName"));
                object.putInt("minutes1", bundle1.getInt("minutes"));
                object.putInt("seconds1", bundle1.getInt("seconds"));
                object.putString("exFlag2", bundle2.getString("exFlag"));
                object.putString("timeTable2", bundle2.getString("timeTable"));
                object.putString("dName2", bundle2.getString("dName"));
                object.putInt("minutes2", bundle2.getInt("minutes"));
                object.putInt("seconds2", bundle2.getInt("seconds"));
            } else if (bundle1 != null) {
                object = new Bundle();
                object.putInt("cellType", 1);
                object.putString("exFlag1", bundle1.getString("exFlag"));
                object.putString("timeTable1", bundle1.getString("timeTable"));
                object.putString("dName1", bundle1.getString("dName"));
                object.putInt("minutes1", bundle1.getInt("minutes"));
                object.putInt("seconds1", bundle1.getInt("seconds"));
                object.putString("timeTable2", "");
            } else if (bundle2 != null) {
                object = new Bundle();
                object.putInt("cellType", 1);
                object.putString("exFlag2", bundle2.getString("exFlag"));
                object.putString("timeTable2", bundle2.getString("timeTable"));
                object.putString("dName2", bundle2.getString("dName"));
                object.putInt("minutes2", bundle2.getInt("minutes"));
                object.putInt("seconds2", bundle2.getInt("seconds"));
                object.putString("timeTable1", "");
            }
            if (object != null) {
                arrTimeData.add(object);
            }
            object = null;
        }
        return arrTimeData;
    }

    private ArrayList<Bundle> _setTimeOperation(ArrayList<Bundle> arr) {
        ArrayList<Bundle> arrTimeTableData = new ArrayList();
        Iterator it = arr.iterator();
        while (it.hasNext()) {
            String timeTable;
            Bundle bundle = (Bundle) it.next();
            int hour = Integer.valueOf(bundle.getString("hour")).intValue();
            int minutes = Integer.valueOf(bundle.getString("tData")).intValue();
            int seconds = Integer.valueOf(bundle.getString("tSub")).intValue();
            int waiting = Integer.valueOf(bundle.getString("wSub")).intValue();
            int iTime = ((((hour * 60) + minutes) * 60) + seconds) - this.iTimeValue;
            int iMinutes = iTime / 60;
            int iSeconds = iTime % 60;
            if (iSeconds > 54) {
                iMinutes++;
                iSeconds = 0;
            } else if (iSeconds > 44) {
                iSeconds = 50;
            } else if (iSeconds > 34) {
                iSeconds = 40;
            } else if (iSeconds > 24) {
                iSeconds = 30;
            } else if (iSeconds > 14) {
                iSeconds = 20;
            } else if (iSeconds > 4) {
                iSeconds = 10;
            } else {
                iSeconds = 0;
            }
            String dName = bundle.getString("dName");
            if (dName == null) {
                dName = "";
            }
            if (waiting == 88) {
                timeTable = String.format("[%d:%02d:%02d%s]", new Object[]{Integer.valueOf(hour), Integer.valueOf(minutes), Integer.valueOf(seconds), "\ucd9c\ubc1c / \ucd9c\ubc1c\uc9c0"});
            } else if (waiting > 0) {
                timeTable = String.format(" \ub3c4\ucc29 / %d\ucd08 \uc815\ucc28", new Object[]{Integer.valueOf(waiting)});
                timeTable = String.format("[%d:%02d:%02d%s]", new Object[]{Integer.valueOf(hour), Integer.valueOf(minutes), Integer.valueOf(seconds), timeTable});
            } else {
                timeTable = String.format("[\uc2dc\uac01\ud45c : %d:%02d:%02d]", new Object[]{Integer.valueOf(hour), Integer.valueOf(minutes), Integer.valueOf(seconds)});
            }
            Bundle dicData = new Bundle();
            dicData.putString("exFlag", bundle.getString("exFlag"));
            dicData.putString("timeTable", timeTable);
            dicData.putString("dName", dName);
            dicData.putInt("minutes", iMinutes);
            dicData.putInt("seconds", iSeconds);
            arrTimeTableData.add(dicData);
        }
        return arrTimeTableData;
    }
}
