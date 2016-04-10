package com.example.hyejung.easysubway.DBmodel;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AppDataManager {
    static AppDataManager _shared = null;
    int appVersionCode;
    Boolean bCheckUpdateFlag;
    Boolean bDetailInfoFlag;
    Boolean bMapSearchModeFlag;
    Boolean bMapTimeAutoFlag;
    Boolean bMapTimeInfoFlag;
    Boolean bNowPointInfoFlag;
    Boolean bPathAniFlag;
    Boolean bRealTimeInfoFlag;
    Boolean bRefleshFlag;
    Boolean bStartWaitTimeFlag;
    private DBHandler dbHandler = null;
    float fAniScaleWeight;
    float fAniScaleWeight2;
    int iAniSpeedWeight;
    int iAreaCode;
    int iDetailInfoFlag;
    int iMapStartPointType;
    int iMapTimeInfoType;
    int iSearchPathType;
    int iSearchTimeType;
    int iSearchWaitTimeType;
    int iSelectedMenuAniType;
    int iTransferTimeWeight;
    int iUserMenuType01;
    int iWeekType;
    Context mContext = null;
    private SharedPreferences mPreference;
    int oldAreaCode;
    private String preName = "app_config";

    public static AppDataManager shared() {
        synchronized (AppDataManager.class) {
            if (_shared == null) {
                _shared = new AppDataManager();
            }
        }
        return _shared;
    }

    public void initAppDataManager(Context context) throws Throwable {
        this.mContext = context;
        this.bRefleshFlag = Boolean.valueOf(false);
        this.iSearchPathType = 0;
        this.iUserMenuType01 = 0;
        this.iMapStartPointType = 1;
        this.iMapTimeInfoType = 0;
        this.iAreaCode = 1;
        this.bPathAniFlag = Boolean.valueOf(true);
        this.bDetailInfoFlag = Boolean.valueOf(false);
        this.bMapTimeInfoFlag = Boolean.valueOf(false);
        this.bMapTimeAutoFlag = Boolean.valueOf(false);
        this.bRealTimeInfoFlag = Boolean.valueOf(false);
        this.bNowPointInfoFlag = Boolean.valueOf(false);
        this.bCheckUpdateFlag = Boolean.valueOf(false);
        this.bStartWaitTimeFlag = Boolean.valueOf(false);
        this.iSelectedMenuAniType = 0;
        this.fAniScaleWeight = 0.5f;
        this.fAniScaleWeight2 = 0.4f;
        this.iAniSpeedWeight = 300;
        this.iSearchWaitTimeType = 0;
        this.iSearchTimeType = getMySharedPreferences("searchTimeType", 0);
        this.bNowPointInfoFlag = getMySharedPreferences("nowPointInfoFlag", Boolean.valueOf(true));
        _getAppUserData();
        settingWeekType();
        _initDBHandler();
    }

    private void _initDBHandler() throws Throwable {
        this.dbHandler = DBHandler.shared();
        this.dbHandler.initDBHandler(this.mContext);
        String strKey = "appDataFileCode" + this.iAreaCode;
        int fileCode = getMySharedPreferences(strKey, 0);
        int defCode = this.dbHandler.initDBFile(fileCode);
        if (defCode > fileCode) {
            setMySharedPreferences(strKey, defCode);
        }
    }

    public void resetDBHandler() throws Throwable {
        String strKey = "appDataFileCode" + this.iAreaCode;
        int fileCode = getMySharedPreferences(strKey, 0);
        int defCode = this.dbHandler.initDBFile(fileCode);
        if (defCode > fileCode) {
            setMySharedPreferences(strKey, defCode);
        }
        if (this.oldAreaCode > 0) {
            this.dbHandler.removeSubwayDBFile(this.oldAreaCode);
        }
        this.oldAreaCode = 0;
    }

    public int getAppDataFileCode() {
        int code1 = getMySharedPreferences("appDataFileCode", 0);
        int code2 = getAppVersionCode();
        return code1 > code2 ? code1 : code2;
    }

    public void setAppDataFileCode(int code) {
        this.appVersionCode = code;
    }

    public void settingAppVersionCode() {
        setMySharedPreferences("appDataFileCode", this.appVersionCode);
    }

    public int getAppVersionCode() {
        int appCode = 0;
        try {
            return this.mContext.getPackageManager().getPackageInfo(this.mContext.getPackageName(), 128).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return appCode;
        }
    }

    public void showToastMsg(String msg) {
        showToastMsg(msg, 0);
    }

    public void showToastMsg(String msg, int length) {
        Toast.makeText(this.mContext, msg, length).show();
    }

    public Boolean checkVersionInfo() {
        Boolean bFlag = Boolean.valueOf(false);
        String nowTime = new SimpleDateFormat("yyMMddHHmm", Locale.KOREA).format(new Date(System.currentTimeMillis()));
        if (nowTime.equals(getMySharedPreferences("checkVersionTime", "0"))) {
            return bFlag;
        }
        setMySharedPreferences("checkVersionTime", nowTime);
        return Boolean.valueOf(true);
    }

    private void _saveUserData() {
        setMySharedPreferences("areaCode", this.iAreaCode);
        setMySharedPreferences("bPathAniFlag", this.bPathAniFlag);
        setMySharedPreferences("mapTimeInfoFlag", this.bMapTimeInfoFlag);
        setMySharedPreferences("mapTimeAutoFlag", this.bMapTimeAutoFlag);
        setMySharedPreferences("mapSearchModeFlag", this.bMapSearchModeFlag);
        setMySharedPreferences("transferTimeWeight", this.iTransferTimeWeight);
        setMySharedPreferences("selectedMenuAniType", this.iSelectedMenuAniType);
        setMySharedPreferences("userMenuType01", this.iUserMenuType01);
        setMySharedPreferences("mapStartPointType", this.iMapStartPointType);
        setMySharedPreferences("mapTimeInfoType", this.iMapTimeInfoType);
        setMySharedPreferences("aniScaleWeight", Float.valueOf(this.fAniScaleWeight));
        setMySharedPreferences("aniScaleWeight2", Float.valueOf(this.fAniScaleWeight2));
        setMySharedPreferences("aniSpeedWeight", this.iAniSpeedWeight);
        setMySharedPreferences("checkUpdateFlag", this.bCheckUpdateFlag);
        setMySharedPreferences("startWaitTimeFlag", this.bStartWaitTimeFlag);
    }

    public void setSearchPathType(int type) {
        this.iSearchPathType = type;
    }

    public int getSearchPathType() {
        return this.iSearchPathType;
    }

    public void setSearchTimeType(int type) {
        this.iSearchTimeType = type;
        this.iSearchWaitTimeType = 0;
    }

    public int getSearchTimeType() {
        return this.iSearchTimeType + 1;
    }

    public void setSearchTimeTypeDefaults(int type) {
        if (this.iSearchTimeType != type) {
            this.iSearchTimeType = type;
            this.iSearchWaitTimeType = 0;
            setMySharedPreferences("searchTimeType", this.iSearchTimeType);
        }
    }

    public void resetSearchTimeTypeDefaults() {
        this.iSearchTimeType = getSearchTimeTypeDefaults();
        this.iSearchWaitTimeType = 0;
    }

    public int getSearchTimeTypeDefaults() {
        return getMySharedPreferences("searchTimeType", 0);
    }

    public Boolean getNowPointInfoFlag() {
        if (this.iSearchTimeType == 0) {
            return this.bNowPointInfoFlag;
        }
        return Boolean.valueOf(false);
    }

    public void setNowPointInfoFlag(Boolean flag) {
        this.bNowPointInfoFlag = flag;
    }

    public void setNowPointInfoDefaults(Boolean type) {
        if (this.bNowPointInfoFlag != type) {
            this.bNowPointInfoFlag = type;
            setMySharedPreferences("nowPointInfoFlag", this.bNowPointInfoFlag);
        }
    }

    public Boolean getNowPointInfoDefaults() {
        return getMySharedPreferences("nowPointInfoFlag", Boolean.valueOf(true));
    }

    public void resetNowPointInfoFlagDefaults() {
        this.bNowPointInfoFlag = getNowPointInfoDefaults();
    }

    private void _initDetailInfoFlag() {
        if (isMySharedPreferences("nowPointInfoFlag").booleanValue()) {
            this.iDetailInfoFlag = getMySharedPreferences("iDetailFlag", 1);
        } else {
            this.iDetailInfoFlag = 1;
            setMySharedPreferences("iDetailFlag", this.iDetailInfoFlag);
            if (isMySharedPreferences("bDetailFlag").booleanValue()) {
                this.bDetailInfoFlag = getMySharedPreferences("bDetailFlag", Boolean.valueOf(false));
            } else {
                this.bDetailInfoFlag = Boolean.valueOf(false);
                setMySharedPreferences("bDetailFlag", this.iDetailInfoFlag);
            }
        }
        switch (this.iDetailInfoFlag) {
            case 0:
                this.bDetailInfoFlag = Boolean.valueOf(true);
                return;
            case 1:
                this.bDetailInfoFlag = Boolean.valueOf(false);
                return;
            default:
                return;
        }
    }

    private void _saveDetailInfoFlag() {
        setMySharedPreferences("bDetailFlag", this.bDetailInfoFlag);
    }

    public void setIDetailInfoFlag(int flag) {
        if (this.iDetailInfoFlag != flag) {
            this.iDetailInfoFlag = flag;
            setMySharedPreferences("iDetailFlag", this.iDetailInfoFlag);
            _initDetailInfoFlag();
        }
    }

    public int iDetailInfoFlag() {
        return this.iDetailInfoFlag;
    }

    public void setBDetailInfoFlag(Boolean flag) {
        if (this.iDetailInfoFlag == 2) {
            this.bDetailInfoFlag = flag;
        }
    }

    public Boolean bDetailInfoFlag() {
        switch (this.iDetailInfoFlag) {
            case 0:
                this.bDetailInfoFlag = Boolean.valueOf(true);
                break;
            case 1:
                this.bDetailInfoFlag = Boolean.valueOf(false);
                break;
        }
        return this.bDetailInfoFlag;
    }

    public void setSearchWaitTimeType(int type) {
        this.iSearchWaitTimeType = type;
    }

    public int getSearchWaitTimeType() {
        return this.iSearchWaitTimeType;
    }

    public void setSaveAreaCode(int code) {
        this.iAreaCode = code;
        _saveUserData();
    }

    public void setAreaCode(int code) {
        this.bRefleshFlag = Boolean.valueOf(false);
        if (this.iAreaCode != code) {
            this.oldAreaCode = this.iAreaCode;
            this.iAreaCode = code;
            this.bRefleshFlag = Boolean.valueOf(true);
            setMySharedPreferences("areaCode", this.iAreaCode);
        }
    }

    public int getAreaCode() {
        return this.iAreaCode;
    }

    public void saveUserData() {
        _saveUserData();
    }

    public void saveAppUserData() {
        _saveUserData();
        _saveDetailInfoFlag();
    }

    private void _getAppUserData() {
        _initDetailInfoFlag();
        this.iAreaCode = getMySharedPreferences("areaCode", 1);
        this.bPathAniFlag = getMySharedPreferences("bPathAniFlag", Boolean.valueOf(true));
        this.bMapTimeInfoFlag = getMySharedPreferences("mapTimeInfoFlag", Boolean.valueOf(true));
        this.bMapTimeAutoFlag = getMySharedPreferences("bMapTimeAutoFlag", Boolean.valueOf(true));
        this.bRealTimeInfoFlag = getMySharedPreferences("realTimeInfoFlag", Boolean.valueOf(false));
        this.bCheckUpdateFlag = getMySharedPreferences("checkUpdateFlag", Boolean.valueOf(true));
        this.bMapSearchModeFlag = getMySharedPreferences("mapSearchModeFlag", Boolean.valueOf(false));
        this.iTransferTimeWeight = getMySharedPreferences("transferTimeWeight", 0);
        this.iSelectedMenuAniType = getMySharedPreferences("selectedMenuAniType", 0);
        this.iUserMenuType01 = getMySharedPreferences("userMenuType01", 0);
        this.iMapStartPointType = getMySharedPreferences("mapStartPointType", 1);
        this.iMapTimeInfoType = getMySharedPreferences("mapTimeInfoType", 0);
        this.fAniScaleWeight = getMySharedPreferences("aniScaleWeight", 1.0f);
        this.fAniScaleWeight2 = getMySharedPreferences("aniScaleWeight2", 1.0f);
        this.iAniSpeedWeight = getMySharedPreferences("aniSpeedWeight", 300);
        this.bStartWaitTimeFlag = getMySharedPreferences("startWaitTimeFlag", Boolean.valueOf(false));
    }

    public void settingWeekType() {
        int weekType = 0;
        String format = new SimpleDateFormat("yyMMdd", Locale.KOREA).format(new Date(System.currentTimeMillis()));
        Calendar c = Calendar.getInstance();
        int iNowHour = c.get(11);
        int iTypeCode = _cmdCheckHoliday(Integer.parseInt("160110"));
        int week;
        if (iTypeCode == 1) {
            weekType = iNowHour > 2 ? 2 : 0;
        } else if (iTypeCode == 2) {
            weekType = iNowHour > 2 ? 2 : 1;
        } else if (iTypeCode == 3) {
            weekType = 2;
        } else if (iTypeCode != 9) {
            week = c.get(7);
            if (week == 1) {
                weekType = iNowHour > 2 ? 2 : 1;
            } else if (week == 7) {
                if (iNowHour > 2) {
                    weekType = 1;
                }
            } else if (week == 2 && iNowHour <= 2) {
                weekType = 2;
            }
        } else if (iNowHour > 2) {
            weekType = 2;
        } else {
            week = c.get(7);
            weekType = week == 1 ? 2 : week == 7 ? 1 : 0;
        }
        if (this.iWeekType != weekType) {
            this.iWeekType = weekType;
        }
    }

    public int getWeekType() {
        return this.iWeekType;
    }

    public void setWeekType(int iWeekType) {
        this.iWeekType = iWeekType;
    }

    public int cmdChangeWeekTypeEvent() {
        if (this.iWeekType > 1) {
            this.iWeekType = 0;
        } else {
            this.iWeekType++;
        }
        return this.iWeekType;
    }

    private int _cmdCheckHoliday(int date) {
        switch (date) {
            case 160101:
                return 1;
            case 160102:
                return 9;
            case 160207:
                return 2;
            case 160208:
            case 160209:
            case 160210:
                return 3;
            case 160211:
                return 9;
            case 160301:
                return 1;
            case 160302:
                return 9;
            case 160413:
                return 1;
            case 160414:
                return 9;
            case 160505:
                return 1;
            case 160506:
                return 9;
            case 160507:
                return 9;
            case 160514:
                return 1;
            case 160606:
                return 3;
            case 160815:
                return 3;
            case 160816:
                return 9;
            case 160817:
                return 9;
            case 160914:
                return 1;
            case 160915:
            case 160916:
                return 3;
            case 161003:
                return 3;
            case 161004:
                return 9;
            case 161009:
                return 2;
            case 161010:
                return 9;
            case 161225:
                return 2;
            case 161226:
                return 9;
            default:
                return 0;
        }
    }

    public void setFavoritesStation(Bundle bundle) {
        if (bundle == null) {
            showToastMsg("\uc990\uaca8\ucc3e\uae30 \ub4f1\ub85d\uc774 \uc2e4\ud328\ud558\uc600\uc2b5\ub2c8\ub2e4.", 0);
            return;
        }
        long resFlag = insertFavoritesStation(bundle);
        if (resFlag == 0) {
            setMySharedPreferences("FavoritesListMode", 0);
            showToastMsg("\uc990\uaca8\ucc3e\uae30\uac00 \ub4f1\ub85d\ub418\uc5c8\uc2b5\ub2c8\ub2e4.", 0);
        } else if (resFlag > 0) {
            showToastMsg("\uc774\ubbf8 \uc990\uaca8\ucc3e\uae30\uc5d0 \ub4f1\ub85d\ub418\uc788\uc2b5\ub2c8\ub2e4.", 1);
        } else {
            showToastMsg("\uc990\uaca8\ucc3e\uae30 \ub4f1\ub85d\uc774 \uc2e4\ud328\ud558\uc600\uc2b5\ub2c8\ub2e4.", 1);
        }
    }

    public void setFavoritesPath(Bundle bundle) {
        if (bundle == null) {
            showToastMsg("\uc990\uaca8\ucc3e\uae30 \ub4f1\ub85d\uc774 \uc2e4\ud328\ud558\uc600\uc2b5\ub2c8\ub2e4.", 0);
            return;
        }
        long resFlag = insertFavoritesPath(bundle);
        if (resFlag == 0) {
            setMySharedPreferences("FavoritesListMode", 1);
            showToastMsg("\uc990\uaca8\ucc3e\uae30\uac00 \ub4f1\ub85d\ub418\uc5c8\uc2b5\ub2c8\ub2e4.", 0);
        } else if (resFlag > 0) {
            showToastMsg("\uc774\ubbf8 \uc990\uaca8\ucc3e\uae30\uc5d0 \ub4f1\ub85d\ub418\uc788\uc2b5\ub2c8\ub2e4.", 1);
        } else {
            showToastMsg("\uc990\uaca8\ucc3e\uae30 \ub4f1\ub85d\uc774 \uc2e4\ud328\ud558\uc600\uc2b5\ub2c8\ub2e4.", 1);
        }
    }

    public ArrayList<Bundle> getHistoryData() {
        return this.dbHandler.getHistoryData(getAreaCode());
    }

    public Boolean insertHistoryData(Bundle dic) {
        return this.dbHandler.insertHistoryData(dic);
    }

    public Boolean deleteHistoryDataAll() {
        return this.dbHandler.deleteHistoryDataAll();
    }

    public Boolean deleteHistoryData(int row) {
        return this.dbHandler.deleteHistoryData(row);
    }

    public ArrayList<Bundle> getFavoritesStation() {
        return this.dbHandler.getFavoritesStation();
    }

    public long insertFavoritesStation(Bundle data) {
        return this.dbHandler.insertFavoritesStation(data);
    }

    public Boolean deleteFavoritesStationAll() {
        return this.dbHandler.deleteFavoritesStationAll();
    }

    public Boolean deleteFavoritesStation(int row) {
        return this.dbHandler.deleteFavoritesStation(row);
    }

    public ArrayList<Bundle> getFavoritesPath() {
        return this.dbHandler.getFavoritesPath();
    }

    public long insertFavoritesPath(Bundle data) {
        return this.dbHandler.insertFavoritesPath(data);
    }

    public Boolean deleteFavoritesPathAll() {
        return this.dbHandler.deleteFavoritesPathAll();
    }

    public Boolean deleteFavoritesPath(int row) {
        return this.dbHandler.deleteFavoritesPath(row);
    }

    public ArrayList<Bundle> getArrLineInfoAll() {
        this.dbHandler = DBHandler.shared();
        return this.dbHandler.getArrLineInfoAll();
    }

    public ArrayList<Bundle> getArrStationInfoListAll() {
        if (this.iAreaCode == 1) {
            return this.dbHandler.getArrStationInfoListAll01();
        }
        return this.dbHandler.getArrStationInfoListAll();
    }

    public ArrayList<Bundle> getArrTransferSubCodeDataAll() {
        return this.dbHandler.getArrTransferSubCodeDataAll();
    }

    public ArrayList<Bundle> getArrDirectSubCodeDataAll() {
        return this.dbHandler.getArrDirectSubCodeDataAll();
    }

    public ArrayList<Bundle> getArrStationInfoList(int lineCode) {
        this.dbHandler = DBHandler.shared();
        return this.dbHandler.getArrStationInfoList(lineCode);
    }

    public ArrayList<Bundle> getArrStationInfoName(String strName) {
        return this.dbHandler.getArrStationInfoName(strName);
    }

    public ArrayList<Bundle> getSreachStationInfoList(String strName) {
        return this.dbHandler.getSreachStationInfoList(strName);
    }

    public ArrayList<Bundle> getArrStationSubCodeToAreaCode(int areaCode, Boolean dFlag) {
        if (dFlag.booleanValue()) {
            return this.dbHandler.getArrStationSubCodeToAreaCode01(areaCode);
        }
        return this.dbHandler.getArrStationSubCodeToAreaCode(areaCode);
    }

    public ArrayList<Bundle> getArrSubwayAllLocation() {
        return this.dbHandler.getArrSubwayAllLocation();
    }

    public ArrayList<Bundle> getArrSubwayLineLocation(int lineCode) {
        return this.dbHandler.getArrSubwayLineLocation(lineCode);
    }

    public ArrayList<Bundle> getArrOenStationLocation(int stationCode) {
        return this.dbHandler.getArrOenStationLocation(stationCode);
    }

    public ArrayList<Bundle> getArrStationLocation(float latitude, float longitude, Boolean group) {
        return this.dbHandler.getArrStationLocation(latitude, longitude, group);
    }

    public ArrayList<Bundle> getArrStationPoint(float ptX, float ptY) {
        return this.dbHandler.getArrStationPoint(ptX, ptY);
    }

    public ArrayList<Bundle> getArrStationPoint02(String stationCode) {
        return this.dbHandler.getArrStationPoint02(stationCode);
    }

    public Bundle getArrOenStationData(String stationCode) {
        return this.dbHandler.getArrOenStationData(stationCode);
    }

    public ArrayList<Bundle> getArrStationDataCode(String stationCode) {
        return this.dbHandler.getArrStationDataCode(stationCode);
    }

    public ArrayList<Bundle> getArrStationNameToSubData(String stationName) {
        return this.dbHandler.getArrStationNameToSubData(stationName);
    }
    
    public ArrayList<Bundle> getArrAreaCodeToStationCode(Bundle dic) {
        return this.dbHandler.getArrAreaCodeToStationCode(dic);
    }

    public ArrayList<Bundle> getArrStationAddOnInfo(int stationCode) {
        return this.dbHandler.getArrStationAddOnInfo(stationCode);
    }

    public ArrayList<Bundle> getArrTransferInfoCode(String stationCode) {
        return this.dbHandler.getArrTransferInfoCode(stationCode);
    }

    public ArrayList<Bundle> getArrStationExitInfo(int stationCode) {
        return this.dbHandler.getArrStationExitInfo(stationCode);
    }

    public ArrayList<Bundle> getArrStationCodeInfo(int stationCode) {
        return this.dbHandler.getArrStationCodeInfo(stationCode);
    }

    public Bundle getArrStationSiteCode(int stationCode) {
        return this.dbHandler.getArrStationSiteCode(stationCode);
    }

    public ArrayList<Bundle> getArrStationTimeDataToWeek(int code, int week, int lCode) {
        return this.dbHandler.getArrStationTimeDataToWeek(code, week, lCode);
    }

    public ArrayList<Bundle> getArrStationTimeDataToPicker(int code, int week, int lCode, int direction) {
        return this.dbHandler.getArrStationTimeDataToPicker(code, week, lCode, direction);
    }

    public ArrayList<Bundle> getArrStationTimeDataToNowTime(Bundle dic, int code, int type, int lCode) {
        return this.dbHandler.getArrStationTimeDataToNowTime(dic, code, type, lCode);
    }

    public ArrayList<Bundle> getArrStationTimeDataToType(int code, int week, int lCode, int type) {
        return this.dbHandler.getArrStationTimeDataToType(code, week, lCode, type);
    }

    public ArrayList<Bundle> getArrTrainDataAll(int lCode) {
        return this.dbHandler.getArrTrainDataAll(lCode);
    }

    public ArrayList<Bundle> getArrSearchTrainPathDataToTime(Bundle dic, String sCode1, String sCode2, String tCode, int week, int lCode, int type, int exFlag) {
        if (sCode2 == null) {
            return this.dbHandler.getArrSearchTrainPathDataToTime(dic, sCode1, tCode, week, lCode, type, exFlag);
        }
        return this.dbHandler.getArrSearchTrainPathDataToTime(dic, sCode1, sCode2, tCode, week, lCode, type, exFlag);
    }

    public ArrayList<Bundle> getArrSearchTrainPathDataToLine2(Bundle dic, String code, String tCode, int week, int lCode, int type) {
        return this.dbHandler.getArrSearchTrainPathDataToLine2(dic, code, tCode, week, lCode, type);
    }

    public ArrayList<Bundle> getArrStationFirstLastTimeData(String code, int week, int lCode, int type, int tType) {
        return this.dbHandler.getArrStationFirstLastTimeData(code, week, lCode, type, tType);
    }

    public ArrayList<Bundle> getArrStationUserTimeData(String code, int hour, int tData, int tSub, int week, int lCode, int type, int seType) {
        return this.dbHandler.getArrStationUserTimeData(code, hour, tData, tSub, week, lCode, type, seType);
    }

    public int getArrStationTimeDataToCheck(int code, int week, int lCode) {
        return this.dbHandler.getArrStationTimeDataToCheck(code, week, lCode);
    }

    public ArrayList<Bundle> getArrConvInfoListAll() {
        return this.dbHandler.getArrConvInfoListAll();
    }

    public ArrayList<Bundle> getArrConvDetailList(int code) {
        return this.dbHandler.getArrConvDetailList(code);
    }

    public Boolean getRefleshFlag() {
        return this.bRefleshFlag;
    }

    public void setRefleshFlag(Boolean bRefleshFlag) {
        this.bRefleshFlag = bRefleshFlag;
    }

    public Boolean getBDetailInfoFlag() {
        return this.bDetailInfoFlag;
    }

    public int getIDetailInfoFlag() {
        return this.iDetailInfoFlag;
    }

    public Boolean getPathAniFlag() {
        return this.bPathAniFlag;
    }

    public void setPathAniFlag(Boolean bPathAniFlag) {
        if (this.bPathAniFlag != bPathAniFlag) {
            this.bPathAniFlag = bPathAniFlag;
            setMySharedPreferences("bPathAniFlag", bPathAniFlag);
        }
    }

    public Boolean getMapTimeInfoFlag() {
        return this.bMapTimeInfoFlag;
    }

    public void setMapTimeInfoFlag(Boolean bMapTimeInfoFlag) {
        if (this.bMapTimeInfoFlag != bMapTimeInfoFlag) {
            this.bMapTimeInfoFlag = bMapTimeInfoFlag;
            setMySharedPreferences("mapTimeInfoFlag", bMapTimeInfoFlag);
        }
    }

    public Boolean getMapTimeAutoFlag() {
        return this.bMapTimeAutoFlag;
    }

    public void setMapTimeAutoFlag(Boolean bMapTimeAutoFlag) {
        this.bMapTimeAutoFlag = bMapTimeAutoFlag;
    }

    public Boolean getRealTimeInfoFlag() {
        return this.iAreaCode == 1 ? Boolean.valueOf(true) : Boolean.valueOf(false);
    }

    public void setRealTimeInfoFlag(Boolean bRealTimeInfoFlag) {
        this.bRealTimeInfoFlag = bRealTimeInfoFlag;
        setMySharedPreferences("realTimeInfoFlag", bRealTimeInfoFlag);
    }

    public Boolean getCheckUpdateFlag() {
        return this.bCheckUpdateFlag;
    }

    public void setCheckUpdateFlag(Boolean bCheckUpdateFlag) {
        this.bCheckUpdateFlag = bCheckUpdateFlag;
    }

    public Boolean getStartWaitTimeFlag() {
        return this.bStartWaitTimeFlag;
    }

    public void setStartWaitTimeFlag(Boolean bStartWaitTimeFlag) {
        if (this.bStartWaitTimeFlag != bStartWaitTimeFlag) {
            this.bStartWaitTimeFlag = bStartWaitTimeFlag;
            setMySharedPreferences("startWaitTimeFlag", bStartWaitTimeFlag);
        }
    }

    public Boolean getMapSearchModeFlag() {
        return this.bMapSearchModeFlag;
    }

    public void setMapSearchModeFlag(Boolean bMapSearchModeFlag) {
        if (this.bMapSearchModeFlag != bMapSearchModeFlag) {
            this.bMapSearchModeFlag = bMapSearchModeFlag;
            setMySharedPreferences("mapSearchModeFlag", bMapSearchModeFlag);
        }
    }

    public int getTransferTimeWeight() {
        return this.iTransferTimeWeight;
    }

    public void setTransferTimeWeight(int iTransferTimeWeight) {
        this.iTransferTimeWeight = iTransferTimeWeight;
    }

    public int getSelectedMenuAniType() {
        return this.iSelectedMenuAniType;
    }

    public void setSelectedMenuAniType(int iSelectedMenuAniType) {
        this.iSelectedMenuAniType = iSelectedMenuAniType;
    }

    public float getfAniScaleWeight() {
        return this.fAniScaleWeight;
    }

    public void setfAniScaleWeight(float fAniScaleWeight) {
        this.fAniScaleWeight = fAniScaleWeight;
    }

    public float getfAniScaleWeight2() {
        return this.fAniScaleWeight2;
    }

    public void setfAniScaleWeight2(float fAniScaleWeight2) {
        this.fAniScaleWeight2 = fAniScaleWeight2;
    }

    public float getAniSpeedWeight() {
        return (float) this.iAniSpeedWeight;
    }

    public void setAniSpeedWeight(int _aniSpeedWeight) {
        this.iAniSpeedWeight = _aniSpeedWeight;
    }

    public int getUserMenuType01() {
        return this.iUserMenuType01;
    }

    public void setUserMenuType01(int iUserMenuType01) {
        if (this.iUserMenuType01 != iUserMenuType01) {
            this.iUserMenuType01 = iUserMenuType01;
            setMySharedPreferences("userMenuType01", iUserMenuType01);
        }
    }

    public int getMapStartPointType() {
        return this.iMapStartPointType;
    }

    public void setMapStartPointType(int iMapStartPointType) {
        if (this.iMapStartPointType != iMapStartPointType) {
            this.iMapStartPointType = iMapStartPointType;
            setMySharedPreferences("mapStartPointType", iMapStartPointType);
        }
    }

    public int getMapTimeInfoType() {
        return this.iMapTimeInfoType;
    }

    public void setMapTimeInfoType(int iMapTimeInfoType) {
        if (this.iMapTimeInfoType != iMapTimeInfoType) {
            this.iMapTimeInfoType = iMapTimeInfoType;
            setMySharedPreferences("mapTimeInfoType", iMapTimeInfoType);
        }
    }

    public void setMapRealTimeType(int type) {
        setMySharedPreferences("bMapRealTimeType", type);
    }

    public int getMapRealTimeType() {
        if (this.iAreaCode == 1 && this.bMapTimeInfoFlag.booleanValue()) {
            return getMySharedPreferences("bMapRealTimeType", 1);
        }
        return 0;
    }

    public int getStationNameColor(int lineCode) {
        if (this.iAreaCode != 1) {
            return this.iAreaCode != 2 ? -16777216 : -16777216;
        } else {
            switch (lineCode) {
                case 1:
                    return -1;
                default:
                    return -16777216;
            }
        }
    }

    public String getMapImage() {
        return "img_map" + this.iAreaCode + "_all.gif";
    }

    public String getLineColorImage(int lineCode) {
        switch (this.iAreaCode) {
            case 1:
                if ((lineCode > 0 && lineCode < 30) || lineCode == 101 || lineCode == 102 || lineCode == 901 || lineCode == 1001 || lineCode == 1201) {
                    return "drawable/img_linecolor_1_" + lineCode;
                }
        }
        return "drawable/img_linecolor";
    }

    public String getLineIconImage(int lineCode) {
        switch (this.iAreaCode) {
            case 1:
                if ((lineCode > 0 && lineCode < 30) || lineCode == 101 || lineCode == 102 || lineCode == 901 || lineCode == 1001 || lineCode == 1201) {
                    return "drawable/img_lineicon_1_" + lineCode;
                }
        }
        return "drawable/img_lineicon";
    }

    public String getLinePointIconImage(int lineCode) {
        switch (this.iAreaCode) {
            case 1:
                if ((lineCode > 0 && lineCode < 30) || lineCode == 101 || lineCode == 102 || lineCode == 901 || lineCode == 1001 || lineCode == 1201) {
                    return "drawable/img_linepoint_1_" + lineCode;
                }
        }
        return "drawable/img_linepoint";
    }

    public int getLineTabImage(int lineCode) {
        String mDrawableName = null;
        switch (this.iAreaCode) {
            case 1:
                if ((lineCode > 0 && lineCode < 30) || lineCode == 101 || lineCode == 102 || lineCode == 901 || lineCode == 1001 || lineCode == 1201) {
                    if (lineCode > 100) {
                        lineCode /= 100;
                    }
                    mDrawableName = "line_tab_button1_" + lineCode;
                    break;
                }
        }
        if (mDrawableName != null) {
            return this.mContext.getResources().getIdentifier(mDrawableName, "drawable", this.mContext.getPackageName());
        }
        return this.mContext.getResources().getIdentifier("img_linetab", "drawable", this.mContext.getPackageName());
    }

    public String getMapPinImage(int lineCode) {
        switch (this.iAreaCode) {
            case 1:
                if ((lineCode > 0 && lineCode < 30) || lineCode == 101 || lineCode == 102 || lineCode == 901 || lineCode == 1001 || lineCode == 1201) {
                    if (lineCode > 100) {
                        lineCode /= 100;
                    }
                    return "drawable/img_mappin_1_" + lineCode;
                }
        }
        return "drawable/img_mappin";
    }

    public int getStationNameImage(int lineCode) {
        String mDrawableName = null;
        switch (this.iAreaCode) {
            case 1:
                if ((lineCode > 0 && lineCode < 30) || lineCode == 101 || lineCode == 102 || lineCode == 901 || lineCode == 1001 || lineCode == 1201) {
                    if (lineCode > 100) {
                        lineCode /= 100;
                    }
                    mDrawableName = "img_station_name_1_" + lineCode;
                    break;
                }
        }
        if (mDrawableName != null) {
            return this.mContext.getResources().getIdentifier(mDrawableName, "drawable", this.mContext.getPackageName());
        }
        return this.mContext.getResources().getIdentifier("img_station_name", "drawable", this.mContext.getPackageName());
    }

    public String getLineName(int code) {
        switch (code) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                return code + "\ud638\uc120";
            case 10:
                return "\uacbd\uc758\uc120";
            case 11:
                return "\uc778\ucc9c1\ud638\uc120";
            case 12:
                return "\ubd84\ub2f9\uc120";
            case 13:
                return "\uacf5\ud56d\ucca0\ub3c4";
            case 14:
                return "\uc911\uc559\uc120";
            case 15:
                return "\uacbd\ucd98\uc120";
            case 16:
                return "\uc2e0\ubd84\ub2f9\uc120";
            case 17:
                return "\uc218\uc778\uc120";
            case 18:
                return "\uc758\uc815\ubd80\uacbd\uc804\ucca0";
            case 19:
                return "\uc5d0\ubc84\ub77c\uc778";
            case 101 /*101*/:
            case 102 /*102*/:
                return "1\ud638\uc120\uae09\ud589";
            case 901:
                return "9\ud638\uc120\uae09\ud589";
            case 1001:
                return "\uacbd\uc758\uc911\uc559\uae09\ud589";
            case 1201:
                return "\ubd84\ub2f9\uc120\uae09\ud589";
            default:
                return null;
        }
    }

    public Boolean isMySharedPreferences(String _key) {
        if (this.mPreference == null) {
            this.mPreference = this.mContext.getSharedPreferences(this.preName, 0);
        }
        try {
            return Boolean.valueOf(this.mPreference.contains(_key));
        } catch (Exception e) {
            return Boolean.valueOf(false);
        }
    }

    public int getMySharedPreferences(String _key, int _dftValue) {
        if (this.mPreference == null) {
            this.mPreference = this.mContext.getSharedPreferences(this.preName, 0);
        }
        try {
            _dftValue = this.mPreference.getInt(_key, _dftValue);
        } catch (Exception e) {
        }
        return _dftValue;
    }

    public float getMySharedPreferences(String _key, float _dftValue) {
        if (this.mPreference == null) {
            this.mPreference = this.mContext.getSharedPreferences(this.preName, 0);
        }
        try {
            _dftValue = this.mPreference.getFloat(_key, _dftValue);
        } catch (Exception e) {
        }
        return _dftValue;
    }

    public Boolean getMySharedPreferences(String _key, Boolean _dftValue) {
        if (this.mPreference == null) {
            this.mPreference = this.mContext.getSharedPreferences(this.preName, 0);
        }
        try {
            _dftValue = Boolean.valueOf(this.mPreference.getBoolean(_key, _dftValue.booleanValue()));
        } catch (Exception e) {
        }
        return _dftValue;
    }

    public String getMySharedPreferences(String _key, String _dftValue) {
        if (this.mPreference == null) {
            this.mPreference = this.mContext.getSharedPreferences(this.preName, 0);
        }
        try {
            _dftValue = this.mPreference.getString(_key, _dftValue);
        } catch (Exception e) {
        }
        return _dftValue;
    }

    public void setMySharedPreferences(String _key, int _value) {
        if (this.mPreference == null) {
            this.mPreference = this.mContext.getSharedPreferences(this.preName, 0);
        }
        Editor editor = this.mPreference.edit();
        editor.putInt(_key, _value);
        editor.commit();
    }

    public void setMySharedPreferences(String _key, Float _value) {
        if (this.mPreference == null) {
            this.mPreference = this.mContext.getSharedPreferences(this.preName, 0);
        }
        Editor editor = this.mPreference.edit();
        editor.putFloat(_key, _value.floatValue());
        editor.commit();
    }

    public void setMySharedPreferences(String _key, Boolean _value) {
        if (this.mPreference == null) {
            this.mPreference = this.mContext.getSharedPreferences(this.preName, 0);
        }
        Editor editor = this.mPreference.edit();
        editor.putBoolean(_key, _value.booleanValue());
        editor.commit();
    }

    public void setMySharedPreferences(String _key, String _value) {
        if (this.mPreference == null) {
            this.mPreference = this.mContext.getSharedPreferences(this.preName, 0);
        }
        Editor editor = this.mPreference.edit();
        editor.putString(_key, _value);
        editor.commit();
    }

    private void deleteMySharedPreferences(String _key) {
        if (this.mPreference == null) {
            this.mPreference = this.mContext.getSharedPreferences(this.preName, 0);
        }
        Editor editor = this.mPreference.edit();
        editor.remove(_key);
        editor.commit();
    }

    private void deleteMySharedAllPreferences() {
        if (this.mPreference == null) {
            this.mPreference = this.mContext.getSharedPreferences(this.preName, 0);
        }
        Editor editor = this.mPreference.edit();
        editor.clear();
        editor.commit();
    }
}
