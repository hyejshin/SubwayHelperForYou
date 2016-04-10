package com.example.hyejung.easysubway.DBmodel;

import android.content.Context;
import android.os.Bundle;
import com.example.hyejung.easysubway.MainActivity;

public class PointDataManager {
    static PointDataManager _shared = null;
    Bundle dicFinishData;
    Bundle dicPassData;
    Bundle dicSelectedData;
    Bundle dicSelectedPoint;
    Bundle dicStartData;
    Context mContext = null;

    public static PointDataManager shared() {
        synchronized (PointDataManager.class) {
            if (_shared == null) {
                _shared = new PointDataManager();
            }
        }
        return _shared;
    }

    public void initPointManager(Context context) {
        this.mContext = context;
    }

    public boolean checkRealTimeFlag() {
        if (this.dicSelectedData != null && AppDataManager.shared().getAreaCode() == 1) {
            String seoulCode = AppDataManager.shared().getArrStationSiteCode(Integer.valueOf(this.dicSelectedData.getString("stationCode")).intValue()).getString("seoulCode");
            if (seoulCode != null && seoulCode.length() > 0) {
                return true;
            }
        }
        return false;
    }

    public Bundle getSelectedData() {
        return this.dicSelectedData;
    }

    public void setSelectedData(Bundle bundle) {
        this.dicSelectedData = bundle;
        _setLastSelectedPoint(bundle, 1);
    }

    public void clearSelectedData() {
        if (this.dicSelectedData != null) {
            this.dicSelectedData.clear();
            this.dicSelectedData = null;
        }
    }

    private void _setLastSelectedPoint(Bundle bundle, int type) {
        if (bundle != null && AppDataManager.shared().getMapStartPointType() == type) {
            int areaCode = AppDataManager.shared().getAreaCode();
            AppDataManager.shared().setMySharedPreferences("LastSelectedPointX" + areaCode, Float.valueOf(bundle.getString("pointX")));
            AppDataManager.shared().setMySharedPreferences("LastSelectedPointY" + areaCode, Float.valueOf(bundle.getString("pointY")));
        }
    }

    public Boolean checkSelectedData() {
        if (checkStartData() == 1) {
            return Boolean.valueOf(true);
        }
        if (checkFinishData() == 1) {
            return Boolean.valueOf(true);
        }
        if (checkPassData() == 1) {
            return Boolean.valueOf(true);
        }
        return Boolean.valueOf(false);
    }

    public int checkStartData() {
        if (this.dicStartData == null || this.dicSelectedData == null) {
            return 0;
        }
        if (this.dicStartData.getString("stationCode").equals(this.dicSelectedData.getString("stationCode"))) {
            return 1;
        }
        return 2;
    }

    public Bundle getStartData() {
        return this.dicStartData;
    }

    public void setStartData(Bundle bundle) {
        if (bundle != null) {
            clearStartData();
            this.dicStartData = new Bundle();
            this.dicStartData.putAll(bundle);
            _setLastSelectedPoint(bundle, 2);
            if (checkPointData(this.dicStartData, this.dicFinishData).booleanValue()) {
                clearFinishData();
            } else if (checkPointData(this.dicStartData, this.dicPassData).booleanValue()) {
                clearPassData();
            }
        }
    }

    public void clearStartData() {
        if (this.dicStartData != null) {
            this.dicStartData.clear();
            this.dicStartData = null;
        }
    }

    public int checkFinishData() {
        if (this.dicFinishData == null || this.dicSelectedData == null) {
            return 0;
        }
        if (this.dicFinishData.getString("stationCode").equals(this.dicSelectedData.getString("stationCode"))) {
            return 1;
        }
        return 2;
    }

    public Bundle getFinishData() {
        return this.dicFinishData;
    }

    public void setFinishData(Bundle bundle) {
        if (bundle != null) {
            clearFinishData();
            this.dicFinishData = new Bundle();
            this.dicFinishData.putAll(bundle);
            _setLastSelectedPoint(bundle, 3);
            if (checkPointData(this.dicFinishData, this.dicStartData).booleanValue()) {
                clearStartData();
            } else if (checkPointData(this.dicFinishData, this.dicPassData).booleanValue()) {
                clearPassData();
            }
        }
    }

    public void clearFinishData() {
        if (this.dicFinishData != null) {
            this.dicFinishData.clear();
            this.dicFinishData = null;
        }
    }

    public int checkPassData() {
        if (this.dicPassData == null || this.dicSelectedData == null) {
            return 0;
        }
        if (this.dicPassData.getString("stationCode").equals(this.dicSelectedData.getString("stationCode"))) {
            return 1;
        }
        return 2;
    }

    public Bundle getPassData() {
        return this.dicPassData;
    }

    public void setPassData(Bundle bundle) {
        if (bundle != null) {
            clearPassData();
            this.dicPassData = new Bundle();
            this.dicPassData.putAll(bundle);
            if (checkPointData(this.dicPassData, this.dicStartData).booleanValue()) {
                clearStartData();
            } else if (checkPointData(this.dicPassData, this.dicFinishData).booleanValue()) {
                clearFinishData();
            }
        }
    }

    public void clearPassData() {
        if (this.dicPassData != null) {
            this.dicPassData.clear();
            this.dicPassData = null;
        }
    }

    public Boolean checkPointData(Bundle bundle1, Bundle bundle2) {
        if (bundle1 == null || bundle2 == null || !bundle1.getString("stationCode").equals(bundle2.getString("stationCode"))) {
            return Boolean.valueOf(false);
        }
        return Boolean.valueOf(true);
    }

    public void setSelectedStation(int type) {
        boolean bFalg = true;
        if (this.dicSelectedData != null) {
            if (type == 1) {
                if (checkStartData() == 1) {
                    clearStartData();
                } 
                else {
                    setStartData(this.dicSelectedData);
                }
            } 
            else if (type == 2) {
                if (checkFinishData() == 1) {
                    clearFinishData();
                } 
                else {
                    setFinishData(this.dicSelectedData);
                }
            } 
            else if (type == 3) {
                if (checkPassData() == 1) {
                    clearPassData();
                } 
                else {
                    setPassData(this.dicSelectedData);
                }
            }
            if (this.mContext != null) {
                if (this.dicStartData == null || this.dicFinishData == null) {
                    bFalg = false;
                }
                ((MainActivity) this.mContext).onStartSearchPathEvent(bFalg);
            }
        }
    }

    public void resetStationData() {
        if (this.dicStartData != null) {
            this.dicStartData.clear();
            this.dicStartData = null;
        }
        if (this.dicFinishData != null) {
            this.dicFinishData.clear();
            this.dicFinishData = null;
        }
        if (this.dicPassData != null) {
            this.dicPassData.clear();
            this.dicPassData = null;
        }
        if (this.dicSelectedPoint != null) {
            this.dicSelectedPoint.clear();
            this.dicSelectedPoint = null;
        }
        clearSelectedData();
    }

    public Boolean setSelectedPoint(Bundle bundle) {
        resetStationData();
        String startCode = bundle.getString("startCode");
        String finishCode = bundle.getString("finishCode");
        if (startCode == null || finishCode == null) {
            return Boolean.valueOf(false);
        }
        Bundle object = AppDataManager.shared().getArrOenStationData(startCode);
        if (object != null && object.size() > 0) {
            this.dicStartData = new Bundle(object);
        }
        object = AppDataManager.shared().getArrOenStationData(finishCode);
        if (object != null && object.size() > 0) {
            this.dicFinishData = new Bundle(object);
        }
        String passCode = bundle.getString("passCode");
        if (passCode != null && passCode.length() > 0) {
            object = AppDataManager.shared().getArrOenStationData(finishCode);
            if (object != null && object.size() > 0) {
                this.dicPassData = new Bundle(object);
            }
        }
        return Boolean.valueOf(true);
    }

    public Bundle getSelectedPoint() {
        if (this.dicStartData == null || this.dicFinishData == null) {
            return null;
        }
        Bundle bundle = new Bundle();
        bundle.putString("startCode", this.dicStartData.getString("stationCode"));
        bundle.putString("startName", this.dicStartData.getString("stationName"));
        bundle.putString("startLineCode", this.dicStartData.getString("lineCode"));
        bundle.putString("finishCode", this.dicFinishData.getString("stationCode"));
        bundle.putString("finishName", this.dicFinishData.getString("stationName"));
        bundle.putString("finishLineCode", this.dicFinishData.getString("lineCode"));
        if (this.dicPassData != null) {
            bundle.putString("passCode", this.dicPassData.getString("stationCode"));
            bundle.putString("passName", this.dicPassData.getString("stationName"));
            bundle.putString("passLineCode", this.dicPassData.getString("lineCode"));
            return bundle;
        }
        bundle.putString("passCode", "");
        bundle.putString("passName", "");
        bundle.putString("passLineCode", "0");
        return bundle;
    }
}
