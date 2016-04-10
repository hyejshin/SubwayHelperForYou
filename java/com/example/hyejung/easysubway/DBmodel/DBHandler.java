package com.example.hyejung.easysubway.DBmodel;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

//데이터베이스 정보 뽑아오기-> sql 쿼리문 작성
public class DBHandler {
    static DBHandler _shared = null;
    AppDataManager appManager;
    private SQLiteDatabase db;
    private DBHelper helper;
    private Context mContext;

    public static DBHandler shared() {
        synchronized (AppDataManager.class) {
            if (_shared == null) {
                _shared = new DBHandler();
            }
        }
        return _shared;
    }

    public void initDBHandler(Context ctx) {
        this.mContext = ctx;
        this.appManager = AppDataManager.shared();
    }

    private int getAppInfoCode(int area) {
        switch (area) {
            case 1:
                return 20160223;
            
            default:
                return 0;
        }
    }

    private void DBOpen(int area) throws SQLException {
        this.helper = new DBHelper(this.mContext, getDataFileName(area));
        this.db = this.helper.getWritableDatabase();
    }

    private void DBOpen(int area, int line) throws SQLException {
        if (line > 100) {
            line = (int) (((float) line) * 0.01f);
        }
        this.helper = new DBHelper(this.mContext, getDataFileName(area, line));
        this.db = this.helper.getWritableDatabase();
    }

    private void DBOpen(String fileName) throws SQLException {
        this.helper = new DBHelper(this.mContext, fileName);
        this.db = this.helper.getWritableDatabase();
    }

    private void DBClose() {
        this.helper.close();
        this.helper = null;
    }

    public ArrayList<Bundle> getArrLineInfoAll() {
        DBOpen(this.appManager.getAreaCode());
        ArrayList<Bundle> array = new ArrayList();
        Cursor cursor = this.db.rawQuery("SELECT lineCode, lineName, latitude, longitude, latitudeDelta, longitudeDelta FROM tb_linePoint WHERE lineCode != 14 ORDER BY lineCode", null);
        while (cursor.moveToNext()) {
            Bundle bundle = new Bundle();
            bundle.putString("lineCode", cursor.getString(0));
            bundle.putString("lineName", cursor.getString(1));
            bundle.putString("latitude", cursor.getString(2));
            bundle.putString("longitude", cursor.getString(3));
            bundle.putString("latitudeDelta", cursor.getString(4));
            bundle.putString("longitudeDelta", cursor.getString(5));
            array.add(bundle);
        }
        cursor.close();
        DBClose();
        return array;
    }

    public ArrayList<Bundle> getArrStationInfoListAll() {
        DBOpen(this.appManager.getAreaCode());
        ArrayList<Bundle> array = new ArrayList();
        Cursor cursor = this.db.rawQuery("SELECT a.stationCode, a.subCode, a.areaCode, a.stationName, IFNULL(a.subName,''), a.lineCode, a.lineName, a.transfer, a.tSubCode, a.tCount, a.nStationCode1, a.nSubCode1, a.nStationName1, a.nTime1, a.nDistance1, a.nStationCode2, a.nSubCode2, a.nStationName2, a.nTime2, a.nDistance2, a.toilet, a.exitDoor, a.crossing, b.pointX, b.pointY, a.dFlag FROM tb_stationInfo a, tb_stationPoint b WHERE a.stationCode == b.stationCode ORDER BY a.subCode", null);
        while (cursor.moveToNext()) {
            Bundle bundle = new Bundle();
            bundle.putString("stationCode", cursor.getString(0));
            bundle.putString("subCode", cursor.getString(1));
            bundle.putString("areaCode", cursor.getString(2));
            bundle.putString("stationName", cursor.getString(3));
            bundle.putString("subName", cursor.getString(4));
            bundle.putString("lineCode", cursor.getString(5));
            bundle.putString("lineName", cursor.getString(6));
            bundle.putString("transfer", cursor.getString(7));
            bundle.putString("tSubCode", cursor.getString(8));
            bundle.putString("tCount", cursor.getString(9));
            bundle.putString("nStationCode1", cursor.getString(10));
            bundle.putString("nSubCode1", cursor.getString(11));
            bundle.putString("nStationName1", cursor.getString(12));
            bundle.putString("nTime1", cursor.getString(13));
            bundle.putString("nDistance1", cursor.getString(14));
            bundle.putString("nStationCode2", cursor.getString(15));
            bundle.putString("nSubCode2", cursor.getString(16));
            bundle.putString("nStationName2", cursor.getString(17));
            bundle.putString("nTime2", cursor.getString(18));
            bundle.putString("nDistance2", cursor.getString(19));
            bundle.putString("toilet", cursor.getString(20));
            bundle.putString("exitDoor", cursor.getString(21));
            bundle.putString("crossing", cursor.getString(22));
            bundle.putString("pointX", cursor.getString(23));
            bundle.putString("pointY", cursor.getString(24));
            bundle.putString("dFlag", cursor.getString(25));
            array.add(bundle);
        }
        cursor.close();
        DBClose();
        return array;
    }

    public ArrayList<Bundle> getArrStationInfoListAll01() {
        DBOpen(this.appManager.getAreaCode());
        ArrayList<Bundle> array = new ArrayList();
        Cursor cursor = this.db.rawQuery("SELECT a.stationCode, a.subCode, a.areaCode, a.stationName, IFNULL(a.subName,''), a.lineCode, a.lineName, a.transfer, a.tSubCode, a.tCount, a.nStationCode1, a.nSubCode1, a.nStationName1, a.nTime1, a.nDistance1, a.nStationCode2, a.nSubCode2, a.nStationName2, a.nTime2, a.nDistance2, a.toilet, a.exitDoor, a.crossing, b.pointX, b.pointY, a.direct, a.dSubCode, a.dCount, a.dFlag FROM tb_stationInfo a, tb_stationPoint b WHERE a.stationCode == b.stationCode ORDER BY a.subCode", null);
        while (cursor.moveToNext()) {
            Bundle bundle = new Bundle();
            bundle.putString("stationCode", cursor.getString(0));
            bundle.putString("subCode", cursor.getString(1));
            bundle.putString("areaCode", cursor.getString(2));
            bundle.putString("stationName", cursor.getString(3));
            bundle.putString("subName", cursor.getString(4));
            bundle.putString("lineCode", cursor.getString(5));
            bundle.putString("lineName", cursor.getString(6));
            bundle.putString("transfer", cursor.getString(7));
            bundle.putString("tSubCode", cursor.getString(8));
            bundle.putString("tCount", cursor.getString(9));
            bundle.putString("nStationCode1", cursor.getString(10));
            bundle.putString("nSubCode1", cursor.getString(11));
            bundle.putString("nStationName1", cursor.getString(12));
            bundle.putString("nTime1", cursor.getString(13));
            bundle.putString("nDistance1", cursor.getString(14));
            bundle.putString("nStationCode2", cursor.getString(15));
            bundle.putString("nSubCode2", cursor.getString(16));
            bundle.putString("nStationName2", cursor.getString(17));
            bundle.putString("nTime2", cursor.getString(18));
            bundle.putString("nDistance2", cursor.getString(19));
            bundle.putString("toilet", cursor.getString(20));
            bundle.putString("exitDoor", cursor.getString(21));
            bundle.putString("crossing", cursor.getString(22));
            bundle.putString("pointX", cursor.getString(23));
            bundle.putString("pointY", cursor.getString(24));
            bundle.putString("direct", cursor.getString(25));
            bundle.putString("dSubCode", cursor.getString(26));
            bundle.putString("dCount", cursor.getString(27));
            bundle.putString("dFlag", cursor.getString(28));
            array.add(bundle);
        }
        cursor.close();
        DBClose();
        return array;
    }

    public ArrayList<Bundle> getArrStationInfoList(int lineCode) {
        DBOpen(this.appManager.getAreaCode());
        ArrayList<Bundle> array = new ArrayList();
        Cursor cursor = this.db.rawQuery("SELECT stationCode, areaCode, stationName, IFNULL(subName,''), lineCode, lineName, transfer, nStationName1, nStationCode1, nStationName2, nStationCode2 FROM tb_stationInfo WHERE lineCode = " + lineCode + " AND dFlag = 0 Group BY StationName ORDER BY stationCode", null);
        while (cursor.moveToNext()) {
            Bundle bundle = new Bundle();
            bundle.putString("stationCode", cursor.getString(0));
            bundle.putString("areaCode", cursor.getString(1));
            bundle.putString("stationName", cursor.getString(2));
            bundle.putString("subName", cursor.getString(3));
            bundle.putString("lineCode", cursor.getString(4));
            bundle.putString("lineName", cursor.getString(5));
            bundle.putString("transfer", cursor.getString(6));
            bundle.putString("nStationName1", cursor.getString(7));
            bundle.putString("nStationCode1", cursor.getString(8));
            bundle.putString("nStationName2", cursor.getString(9));
            bundle.putString("nStationCode2", cursor.getString(10));
            array.add(bundle);
        }
        cursor.close();
        DBClose();
        return array;
    }

    public ArrayList<Bundle> getArrStationInfoName(String strName) {
        DBOpen(this.appManager.getAreaCode());
        ArrayList<Bundle> array = new ArrayList();
        Cursor cursor = this.db.rawQuery("SELECT stationCode, areaCode, stationName, IFNULL(subName,''), lineCode, lineName, transfer, nStationName1, nStationCode1, nStationName2, nStationCode2, toilet, exitDoor, crossing FROM tb_stationInfo WHERE stationName = '" + strName + "' AND dFlag = 0 ORDER BY lineCode", null);
        while (cursor.moveToNext()) {
            Bundle bundle = new Bundle();
            bundle.putString("stationCode", cursor.getString(0));
            bundle.putString("areaCode", cursor.getString(1));
            bundle.putString("stationName", cursor.getString(2));
            bundle.putString("subName", cursor.getString(3));
            bundle.putString("lineCode", cursor.getString(4));
            bundle.putString("lineName", cursor.getString(5));
            bundle.putString("transfer", cursor.getString(6));
            bundle.putString("nStationName1", cursor.getString(7));
            bundle.putString("nStationCode1", cursor.getString(8));
            bundle.putString("nStationName2", cursor.getString(9));
            bundle.putString("nStationCode2", cursor.getString(10));
            bundle.putString("toilet", cursor.getString(11));
            bundle.putString("exitDoor", cursor.getString(12));
            bundle.putString("crossing", cursor.getString(13));
            array.add(bundle);
        }
        cursor.close();
        DBClose();
        return array;
    }

    public ArrayList<Bundle> getSreachStationInfoList(String strName) {
        DBOpen(this.appManager.getAreaCode());
        ArrayList<Bundle> array = new ArrayList();
        Cursor cursor = this.db.rawQuery("SELECT stationCode, areaCode, stationName, IFNULL(subName,''), lineCode, lineName, transfer, nStationName1, nStationCode1, nStationName2, nStationCode2 FROM tb_stationInfo WHERE ( stationName like '%" + strName + "%'" + " OR nameChosung like '%" + strName + "%'" + " OR subName like '%" + strName + "%'" + " ) AND dFlag = 0 ORDER BY lineCode", null);
        while (cursor.moveToNext()) {
            Bundle bundle = new Bundle();
            bundle.putString("stationCode", cursor.getString(0));
            bundle.putString("areaCode", cursor.getString(1));
            bundle.putString("stationName", cursor.getString(2));
            bundle.putString("subName", cursor.getString(3));
            bundle.putString("lineCode", cursor.getString(4));
            bundle.putString("lineName", cursor.getString(5));
            bundle.putString("transfer", cursor.getString(6));
            bundle.putString("nStationName1", cursor.getString(7));
            bundle.putString("nStationCode1", cursor.getString(8));
            bundle.putString("nStationName2", cursor.getString(9));
            bundle.putString("nStationCode2", cursor.getString(10));
            array.add(bundle);
        }
        cursor.close();
        DBClose();
        return array;
    }

    public ArrayList<Bundle> getArrSubwayAllLocation() {
        DBOpen(this.appManager.getAreaCode());
        ArrayList<Bundle> array = new ArrayList();
        Cursor cursor = this.db.rawQuery("SELECT a.stationCode, a.stationName, IFNULL(a.subName,''), a.lineCode, a.lineName, a.transfer, b.latitude, b.longitude FROM tb_stationInfo a, tb_stationPoint b WHERE a.stationCode == b.stationCode AND a.dFlag = 0", null);
        while (cursor.moveToNext()) {
            Bundle bundle = new Bundle();
            bundle.putString("stationCode", cursor.getString(0));
            bundle.putString("stationName", cursor.getString(1));
            bundle.putString("subName", cursor.getString(2));
            bundle.putString("lineCode", cursor.getString(3));
            bundle.putString("lineName", cursor.getString(4));
            bundle.putString("transfer", cursor.getString(5));
            bundle.putString("latitude", cursor.getString(6));
            bundle.putString("longitude", cursor.getString(7));
            array.add(bundle);
        }
        cursor.close();
        DBClose();
        return array;
    }

    public ArrayList<Bundle> getArrSubwayLineLocation(int lineCode) {
        DBOpen(this.appManager.getAreaCode());
        ArrayList<Bundle> array = new ArrayList();
        Cursor cursor = this.db.rawQuery("SELECT a.stationCode, a.stationName, IFNULL(a.subName,''), a.lineCode, a.lineName, a.transfer, b.latitude, b.longitude FROM tb_stationInfo a, tb_stationPoint b WHERE a.stationCode == b.stationCode AND a.lineCode = " + lineCode + " AND a.dFlag = 0", null);
        while (cursor.moveToNext()) {
            Bundle bundle = new Bundle();
            bundle.putString("stationCode", cursor.getString(0));
            bundle.putString("stationName", cursor.getString(1));
            bundle.putString("subName", cursor.getString(2));
            bundle.putString("lineCode", cursor.getString(3));
            bundle.putString("lineName", cursor.getString(4));
            bundle.putString("transfer", cursor.getString(5));
            bundle.putString("latitude", cursor.getString(6));
            bundle.putString("longitude", cursor.getString(7));
            array.add(bundle);
        }
        cursor.close();
        DBClose();
        return array;
    }

    public ArrayList<Bundle> getArrOenStationLocation(int stationCode) {
        DBOpen(this.appManager.getAreaCode());
        ArrayList<Bundle> array = new ArrayList();
        Cursor cursor = this.db.rawQuery("SELECT a.stationCode, a.stationName, IFNULL(a.subName,''), a.lineCode, a.lineName, a.transfer, b.latitude, b.longitude FROM tb_stationInfo a, tb_stationPoint b WHERE a.stationCode == b.stationCode AND a.stationCode = " + stationCode + " AND a.dFlag = 0", null);
        while (cursor.moveToNext()) {
            Bundle bundle = new Bundle();
            bundle.putString("stationCode", cursor.getString(0));
            bundle.putString("stationName", cursor.getString(1));
            bundle.putString("subName", cursor.getString(2));
            bundle.putString("lineCode", cursor.getString(3));
            bundle.putString("lineName", cursor.getString(4));
            bundle.putString("transfer", cursor.getString(5));
            bundle.putString("latitude", cursor.getString(6));
            bundle.putString("longitude", cursor.getString(7));
            array.add(bundle);
        }
        cursor.close();
        DBClose();
        return array;
    }

    public ArrayList<Bundle> getArrStationLocation(float latitude, float longitude, Boolean group) {
        String sql;
        DBOpen(this.appManager.getAreaCode());
        ArrayList<Bundle> array = new ArrayList();
        float strLatitude1 = latitude - 0.08f;
        float strLatitude2 = latitude + 0.08f;
        float strLongitude1 = longitude - 0.08f;
        float strLongitude2 = longitude + 0.08f;
        if (group.booleanValue()) {
            sql = "SELECT a.stationCode, a.stationName, IFNULL(a.subName,''), a.lineCode, a.lineName, a.transfer, b.latitude, b.longitude FROM tb_stationInfo a, tb_stationPoint b WHERE a.stationCode == b.stationCode AND b.latitude > " + strLatitude1 + " AND b.latitude < " + strLatitude2 + " AND b.longitude > " + strLongitude1 + " AND b.longitude  < " + strLongitude2 + " AND a.dFlag = 0 Group BY a.StationName";
        } else {
            sql = "SELECT a.stationCode, a.stationName, IFNULL(a.subName,''), a.lineCode, a.lineName, a.transfer, b.latitude, b.longitude FROM tb_stationInfo a, tb_stationPoint b WHERE a.stationCode == b.stationCode AND b.latitude > " + strLatitude1 + " AND b.latitude < " + strLatitude2 + " AND b.longitude > " + strLongitude1 + " AND b.longitude  < " + strLongitude2 + " AND a.dFlag = 0";
        }
        Cursor cursor = this.db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            Bundle bundle = new Bundle();
            bundle.putString("stationCode", cursor.getString(0));
            bundle.putString("stationName", cursor.getString(1));
            bundle.putString("subName", cursor.getString(2));
            bundle.putString("lineCode", cursor.getString(3));
            bundle.putString("lineName", cursor.getString(4));
            bundle.putString("transfer", cursor.getString(5));
            bundle.putString("latitude", cursor.getString(6));
            bundle.putString("longitude", cursor.getString(7));
            array.add(bundle);
        }
        cursor.close();
        DBClose();
        return array;
    }

    public ArrayList<Bundle> getArrStationPoint(float ptX, float ptY) {
        DBOpen(this.appManager.getAreaCode());
        ArrayList<Bundle> array = new ArrayList();
        int iValue2 = (int) (((float) 60) + ptX);
        int iValue3 = (int) (ptY - ((float) 60));
        int iValue4 = (int) (((float) 60) + ptY);
        Cursor cursor = this.db.rawQuery("SELECT a.stationCode, a.subCode, a.areaCode, a.stationName, a.transfer, a.lineCode, b.pointX, b.pointY FROM tb_stationInfo a, tb_stationPoint b  WHERE a.stationCode == b.stationCode AND b.pointX > " + ((int) (ptX - ((float) 60))) + " AND b.pointX < " + iValue2 + " AND b.pointY > " + iValue3 + " AND b.pointY < " + iValue4 + " AND a.dFlag = 0 ORDER BY a.transfer DESC", null);
        while (cursor.moveToNext()) {
            Bundle bundle = new Bundle();
            bundle.putString("stationCode", cursor.getString(0));
            bundle.putString("subCode", cursor.getString(1));
            bundle.putString("areaCode", cursor.getString(2));
            bundle.putString("stationName", cursor.getString(3));
            bundle.putString("transfer", cursor.getString(4));
            bundle.putString("lineCode", cursor.getString(5));
            bundle.putString("pointX", cursor.getString(6));
            bundle.putString("pointY", cursor.getString(7));
            array.add(bundle);
        }
        cursor.close();
        DBClose();
        return array;
    }

    public ArrayList<Bundle> getArrStationPoint02(String stationCode) {
        DBOpen(this.appManager.getAreaCode());
        ArrayList<Bundle> array = new ArrayList();
        Cursor cursor = this.db.rawQuery("SELECT a.stationCode, a.subCode, a.areaCode, a.stationName, a.lineCode, b.pointX, b.pointY FROM tb_stationInfo a, tb_stationPoint b WHERE a.stationCode == b.stationCode AND a.stationCode = " + stationCode, null);
        while (cursor.moveToNext()) {
            Bundle bundle = new Bundle();
            bundle.putString("stationCode", cursor.getString(0));
            bundle.putString("subCode", cursor.getString(1));
            bundle.putString("areaCode", cursor.getString(2));
            bundle.putString("stationName", cursor.getString(3));
            bundle.putString("lineCode", cursor.getString(4));
            bundle.putString("pointX", cursor.getString(5));
            bundle.putString("pointY", cursor.getString(6));
            array.add(bundle);
        }
        cursor.close();
        DBClose();
        return array;
    }

    public ArrayList<Bundle> getArrAreaCodeToStationCode(Bundle data) {
        DBOpen(this.appManager.getAreaCode());
        ArrayList<Bundle> array = new ArrayList();
        Cursor cursor = this.db.rawQuery("SELECT stationCode FROM tb_stationInfo WHERE areaCode = ?" + data.getString("nSubCode") + " AND lineCode = ?" + data.getString("lineCode"), null);
        while (cursor.moveToNext()) {
            Bundle bundle = new Bundle();
            bundle.putString("stationCode", cursor.getString(0));
            array.add(bundle);
        }
        cursor.close();
        DBClose();
        return array;
    }

    public ArrayList<Bundle> getArrStationSubCodeToAreaCode(int areaCode) {
        DBOpen(this.appManager.getAreaCode());
        ArrayList<Bundle> array = new ArrayList();
        Cursor cursor = this.db.rawQuery("SELECT subCode, dFlag, lineCode FROM tb_stationInfo WHERE areacode = " + areaCode + " AND dFlag = 0", null);
        while (cursor.moveToNext()) {
            Bundle bundle = new Bundle();
            bundle.putString("subCode", cursor.getString(0));
            bundle.putString("dFlag", cursor.getString(1));
            bundle.putString("lineCode", cursor.getString(2));
            array.add(bundle);
        }
        cursor.close();
        DBClose();
        return array;
    }

    public ArrayList<Bundle> getArrStationSubCodeToAreaCode01(int areaCode) {
        DBOpen(this.appManager.getAreaCode());
        ArrayList<Bundle> array = new ArrayList();
        Cursor cursor = this.db.rawQuery("SELECT subCode, dFlag, lineCode FROM tb_stationInfo WHERE areacode = " + areaCode, null);
        while (cursor.moveToNext()) {
            Bundle bundle = new Bundle();
            bundle.putString("subCode", cursor.getString(0));
            bundle.putString("dFlag", cursor.getString(1));
            bundle.putString("lineCode", cursor.getString(2));
            array.add(bundle);
        }
        cursor.close();
        DBClose();
        return array;
    }

    public ArrayList<Bundle> getArrStationSubCodeToCode(String code) {
        DBOpen(this.appManager.getAreaCode());
        ArrayList<Bundle> array = new ArrayList();
        Cursor cursor = this.db.rawQuery("SELECT subCode FROM tb_stationInfo WHERE stationCode = " + code, null);
        while (cursor.moveToNext()) {
            Bundle bundle = new Bundle();
            bundle.putString("subCode", cursor.getString(0));
            array.add(bundle);
        }
        cursor.close();
        DBClose();
        return array;
    }

    public ArrayList<Bundle> getArrStationDataCode(String stationCode) {
        DBOpen(this.appManager.getAreaCode());
        ArrayList<Bundle> array = new ArrayList();
        Cursor cursor = this.db.rawQuery("SELECT stationCode, areaCode, stationName, IFNULL(subName,''), lineCode, lineName, transfer, nStationName1, nStationCode1, nStationName2, nStationCode2, toilet, exitDoor, crossing FROM tb_stationInfo WHERE stationCode = " + stationCode + " AND dFlag = 0 ORDER BY lineCode", null);
        while (cursor.moveToNext()) {
            Bundle bundle = new Bundle();
            bundle.putString("stationCode", cursor.getString(0));
            bundle.putString("areaCode", cursor.getString(1));
            bundle.putString("stationName", cursor.getString(2));
            bundle.putString("subName", cursor.getString(3));
            bundle.putString("lineCode", cursor.getString(4));
            bundle.putString("lineName", cursor.getString(5));
            bundle.putString("transfer", cursor.getString(6));
            bundle.putString("nStationName1", cursor.getString(7));
            bundle.putString("nStationCode1", cursor.getString(8));
            bundle.putString("nStationName2", cursor.getString(9));
            bundle.putString("nStationCode2", cursor.getString(10));
            bundle.putString("toilet", cursor.getString(11));
            bundle.putString("exitDoor", cursor.getString(12));
            bundle.putString("crossing", cursor.getString(13));
            array.add(bundle);
        }
        cursor.close();
        DBClose();
        return array;
    }

    public Bundle getArrOenStationData(String stationCode) {
        DBOpen(this.appManager.getAreaCode());
        Bundle bundle = null;
        Cursor cursor = this.db.rawQuery("SELECT a.stationCode, a.subCode, a.areaCode, a.stationName, a.lineCode, a.lineName, a.transfer, a.nStationCode1, a.nSubCode1, a.nStationName1, a.nTime1, a.nDistance1, a.nStationCode2, a.nSubCode2, a.nStationName2 ,a.nTime2 , a.nDistance2, b.pointX, b.pointY FROM tb_stationInfo a, tb_stationPoint b WHERE a.stationCode == b.stationCode AND a.stationCode = " + stationCode + " AND a.dFlag = 0", null);
        if (cursor.moveToFirst()) {
            bundle = new Bundle();
            bundle.putString("stationCode", cursor.getString(0));
            bundle.putString("subCode", cursor.getString(1));
            bundle.putString("areaCode", cursor.getString(2));
            bundle.putString("stationName", cursor.getString(3));
            bundle.putString("lineCode", cursor.getString(4));
            bundle.putString("lineName", cursor.getString(5));
            bundle.putString("transfer", cursor.getString(6));
            bundle.putString("nStationCode1", cursor.getString(7));
            bundle.putString("nSubCode2", cursor.getString(8));
            bundle.putString("nStationName1", cursor.getString(9));
            bundle.putString("nTime1", cursor.getString(10));
            bundle.putString("nDistance1", cursor.getString(11));
            bundle.putString("nStationCode2", cursor.getString(12));
            bundle.putString("nSubCode2", cursor.getString(13));
            bundle.putString("nStationName2", cursor.getString(14));
            bundle.putString("nTime2", cursor.getString(15));
            bundle.putString("nDistance2", cursor.getString(16));
            bundle.putString("pointX", cursor.getString(17));
            bundle.putString("pointY", cursor.getString(18));
        }
        cursor.close();
        DBClose();
        return bundle;
    }

    public ArrayList<Bundle> getArrStationNameToSubData(String stationName) {
        DBOpen(this.appManager.getAreaCode());
        ArrayList<Bundle> array = new ArrayList();
        Cursor cursor = this.db.rawQuery("SELECT subCode, lineCode, transfer, stationCode FROM tb_stationInfo WHERE stationName == " + stationName + " AND a.dFlag = 0", null);
        while (cursor.moveToNext()) {
            Bundle bundle = new Bundle();
            bundle.putString("subCode", cursor.getString(0));
            bundle.putString("lineCode", cursor.getString(1));
            bundle.putString("transfer", cursor.getString(2));
            bundle.putString("stationCode", cursor.getString(3));
            array.add(bundle);
        }
        cursor.close();
        DBClose();
        return array;
    }

    public ArrayList<Bundle> getHistoryData(int areaCode) {
        DBOpen(getUserFileName());
        ArrayList<Bundle> array = new ArrayList();
        Cursor cursor = this.db.rawQuery("SELECT rowid, startCode, startName, startLineCode, startAreaCode, finishCode, finishName, finishLineCode, finishAreaCode, passCode, passName, passLineCode, passAreaCode, areaCode, dateTime  FROM tb_pathHistory  WHERE areaCode = " + areaCode + " AND startCode NOT NULL AND finishCode NOT NULL ORDER BY dateTime DESC", null);
        while (cursor.moveToNext()) {
            Bundle bundle = new Bundle();
            bundle.putInt("rowid", cursor.getInt(0));
            bundle.putString("startCode", cursor.getString(1));
            bundle.putString("startName", cursor.getString(2));
            bundle.putString("startLineCode", cursor.getString(3));
            bundle.putString("startAreaCode", cursor.getString(4));
            bundle.putString("finishCode", cursor.getString(5));
            bundle.putString("finishName", cursor.getString(6));
            bundle.putString("finishLineCode", cursor.getString(7));
            bundle.putString("finishAreaCode", cursor.getString(8));
            bundle.putString("passCode", cursor.getString(9));
            bundle.putString("passName", cursor.getString(10));
            bundle.putString("passLineCode", cursor.getString(11));
            bundle.putString("passAreaCode", cursor.getString(12));
            bundle.putString("areaCode", cursor.getString(13));
            bundle.putString("dateTime", cursor.getString(14));
            array.add(bundle);
        }
        cursor.close();
        DBClose();
        return array;
    }

    public Boolean insertHistoryData(Bundle data) {
        String sql;
        Boolean flag = Boolean.valueOf(true);
        DBOpen(getUserFileName());
        int areaCode = this.appManager.getAreaCode();
        int iCount = 0;
        Cursor cursor = this.db.rawQuery("SELECT COUNT(rowid) FROM tb_pathHistory WHERE areaCode = " + areaCode, null);
        if (cursor.moveToFirst()) {
            iCount = cursor.getInt(0);
        }
        cursor.close();
        if (iCount > 100) {
            for (int i = iCount; i > 100; i--) {
                this.db.execSQL("DELETE FROM tb_pathHistory WHERE rowid = (SELECT rowid FROM tb_pathHistory LIMIT 1) AND areaCode = " + areaCode);
            }
        }
        if (data.getString("passCode").length() > 0) {
            sql = "DELETE FROM tb_pathHistory  WHERE startCode = " + data.getString("startCode") + " AND finishCode = " + data.getString("finishCode") + " AND passCode = " + data.getString("passCode") + " AND areaCode = " + areaCode;
        } else {
            sql = "DELETE FROM tb_pathHistory  WHERE startCode = " + data.getString("startCode") + " AND finishCode = " + data.getString("finishCode") + " AND passCode = '' " + " AND areaCode = " + areaCode;
        }
        this.db.execSQL(sql);
        ContentValues values = new ContentValues();
        values.put("startCode", data.getString("startCode"));
        values.put("startName", data.getString("startName"));
        values.put("startLineCode", data.getString("startLineCode"));
        values.put("startAreaCode", data.getString("startAreaCode"));
        values.put("finishCode", data.getString("finishCode"));
        values.put("finishName", data.getString("finishName"));
        values.put("finishLineCode", data.getString("finishLineCode"));
        values.put("finishAreaCode", data.getString("finishAreaCode"));
        values.put("passCode", data.getString("passCode"));
        values.put("passName", data.getString("passName"));
        values.put("passLineCode", data.getString("passLineCode"));
        values.put("passAreaCode", data.getString("passAreaCode"));
        values.put("areaCode", areaCode);
        values.put("dateTime", getNowTimeData());
        if (this.db.insert("tb_pathHistory ", null, values) != 0) {
            flag = Boolean.valueOf(false);
        }
        DBClose();
        return flag;
    }

    public Boolean deleteHistoryDataAll() {
        Boolean flag = Boolean.valueOf(true);
        DBOpen(getUserFileName());
        this.db.execSQL("DELETE FROM tb_pathHistory WHERE areaCode =" + this.appManager.getAreaCode());
        DBClose();
        return flag;
    }

    public Boolean deleteHistoryData(int row) {
        Boolean flag = Boolean.valueOf(true);
        DBOpen(getUserFileName());
        this.db.execSQL("DELETE FROM tb_pathHistory WHERE rowid = " + row + " AND areaCode = " + this.appManager.getAreaCode());
        DBClose();
        return flag;
    }

    public ArrayList<Bundle> getFavoritesStation() {
        DBOpen(getUserFileName());
        ArrayList<Bundle> array = new ArrayList();
        Cursor cursor = this.db.rawQuery("SELECT rowid, stationCode, stationName, lineCode FROM tb_favoritesStation WHERE areaCode = " + this.appManager.getAreaCode() + "  ORDER BY dateTime DESC", null);
        while (cursor.moveToNext()) {
            Bundle bundle = new Bundle();
            bundle.putInt("rowid", cursor.getInt(0));
            bundle.putString("stationCode", cursor.getString(1));
            bundle.putString("stationName", cursor.getString(2));
            bundle.putString("lineCode", cursor.getString(3));
            array.add(bundle);
        }
        cursor.close();
        DBClose();
        return array;
    }

    public long insertFavoritesStation(Bundle data) {
        long res;
        DBOpen(getUserFileName());
        int iCount = 0;
        Cursor cursor = this.db.rawQuery("SELECT COUNT(rowid) FROM tb_favoritesStation WHERE stationCode = " + data.getString("stationCode"), null);
        if (cursor.moveToFirst()) {
            iCount = Integer.parseInt(cursor.getString(0));
        }
        cursor.close();
        if (iCount == 0) {
            ContentValues values = new ContentValues();
            values.put("stationCode", data.getString("stationCode"));
            values.put("stationName", data.getString("stationName"));
            values.put("lineCode", data.getString("lineCode"));
            values.put("areaCode", Integer.valueOf(this.appManager.getAreaCode()));
            res = this.db.insert("tb_favoritesStation ", null, values);
            if (res >= 0) {
                res = 0;
            }
        } else {
            res = (long) iCount;
        }
        DBClose();
        return res;
    }

    public Boolean deleteFavoritesStationAll() {
        Boolean flag = Boolean.valueOf(true);
        DBOpen(getUserFileName());
        this.db.execSQL("DELETE FROM tb_favoritesStation WHERE areaCode =" + this.appManager.getAreaCode());
        DBClose();
        return flag;
    }

    public Boolean deleteFavoritesStation(int row) {
        Boolean flag = Boolean.valueOf(true);
        DBOpen(getUserFileName());
        this.db.execSQL("DELETE FROM tb_favoritesStation WHERE rowid = " + row + " AND areaCode = " + this.appManager.getAreaCode());
        DBClose();
        return flag;
    }

    public ArrayList<Bundle> getFavoritesPath() {
        DBOpen(getUserFileName());
        ArrayList<Bundle> array = new ArrayList();
        Cursor cursor = this.db.rawQuery("SELECT rowid, startCode, startName, startLineCode, startAreaCode, finishCode, finishName, finishLineCode, finishAreaCode, passCode, passName, passLineCode, passAreaCode, dateTime FROM tb_favoritesPath  WHERE areaCode = " + this.appManager.getAreaCode() + "  ORDER BY dateTime DESC", null);
        while (cursor.moveToNext()) {
            Bundle bundle = new Bundle();
            bundle.putInt("rowid", cursor.getInt(0));
            bundle.putString("startCode", cursor.getString(1));
            bundle.putString("startName", cursor.getString(2));
            bundle.putString("startLineCode", cursor.getString(3));
            bundle.putString("startAreaCode", cursor.getString(4));
            bundle.putString("finishCode", cursor.getString(5));
            bundle.putString("finishName", cursor.getString(6));
            bundle.putString("finishLineCode", cursor.getString(7));
            bundle.putString("finishAreaCode", cursor.getString(8));
            bundle.putString("passCode", cursor.getString(9));
            bundle.putString("passName", cursor.getString(10));
            bundle.putString("passLineCode", cursor.getString(11));
            bundle.putString("passAreaCode", cursor.getString(12));
            bundle.putString("dateTime", cursor.getString(13));
            array.add(bundle);
        }
        cursor.close();
        DBClose();
        return array;
    }

    public long insertFavoritesPath(Bundle data) {
        String _passCode2;
        long res;
        DBOpen(getUserFileName());
        int iCount = 0;
        String _startCode = data.getString("startCode");
        String _finishCode = data.getString("finishCode");
        String _passCode = data.getString("passCode");
        if (_passCode.length() > 0) {
            _passCode2 = _passCode;
        } else {
            _passCode2 = "''";
        }
        Cursor cursor = this.db.rawQuery("SELECT COUNT(rowid) FROM tb_favoritesPath WHERE startCode = " + _startCode + " AND finishCode = " + _finishCode + " AND passCode = " + _passCode2, null);
        if (cursor.moveToFirst()) {
            iCount = Integer.parseInt(cursor.getString(0));
        }
        cursor.close();
        if (iCount == 0) {
            ContentValues values = new ContentValues();
            values.put("startCode", _startCode);
            values.put("startName", data.getString("startName"));
            values.put("startLineCode", data.getString("startLineCode"));
            values.put("startAreaCode", data.getString("startAreaCode"));
            values.put("finishCode", _finishCode);
            values.put("finishName", data.getString("finishName"));
            values.put("finishLineCode", data.getString("finishLineCode"));
            values.put("finishAreaCode", data.getString("finishAreaCode"));
            values.put("passCode", _passCode);
            values.put("passName", data.getString("passName"));
            values.put("passLineCode", data.getString("passLineCode"));
            values.put("passAreaCode", data.getString("passAreaCode"));
            values.put("areaCode", Integer.valueOf(this.appManager.getAreaCode()));
            res = this.db.insert("tb_favoritesPath ", null, values);
            if (res >= 0) {
                res = 0;
            }
        } else {
            res = (long) iCount;
        }
        DBClose();
        return res;
    }

    public Boolean deleteFavoritesPathAll() {
        Boolean flag = Boolean.valueOf(true);
        DBOpen(getUserFileName());
        this.db.execSQL("DELETE FROM tb_favoritesPath WHERE areaCode =" + this.appManager.getAreaCode());
        DBClose();
        return flag;
    }

    public Boolean deleteFavoritesPath(int row) {
        Boolean flag = Boolean.valueOf(true);
        DBOpen(getUserFileName());
        this.db.execSQL("DELETE FROM tb_favoritesPath WHERE rowid = " + row + " AND areaCode = " + this.appManager.getAreaCode());
        DBClose();
        return flag;
    }

    public ArrayList<Bundle> getArrStationAddOnInfo(int stationCode) {
        DBOpen(this.appManager.getAreaCode());
        ArrayList<Bundle> array = new ArrayList();
        Cursor cursor = this.db.rawQuery("SELECT stationCode, stationName, lineCode, address, telNumber, platform, meetingPlace, toilet, exitDoor, crossing, office, IFNULL(amenities,'\u5360\uc3d9\uc619\u5360\uc3d9\uc619') FROM tb_stationSubInfo WHERE stationCode = " + stationCode, null);
        while (cursor.moveToNext()) {
            Bundle bundle = new Bundle();
            bundle.putString("stationCode", cursor.getString(0));
            bundle.putString("stationName", cursor.getString(1));
            bundle.putString("lineCode", cursor.getString(2));
            bundle.putString("address", cursor.getString(3));
            bundle.putString("telNumber", cursor.getString(4));
            bundle.putString("platform", cursor.getString(5));
            bundle.putString("meetingPlace", cursor.getString(6));
            bundle.putString("toilet", cursor.getString(7));
            bundle.putString("exitDoor", cursor.getString(8));
            bundle.putString("crossing", cursor.getString(9));
            bundle.putString("office", cursor.getString(10));
            bundle.putString("amenities", cursor.getString(11));
            array.add(bundle);
        }
        cursor.close();
        DBClose();
        return array;
    }

    public ArrayList<Bundle> getArrTransferInfoCode(String stationCode) {
        DBOpen(this.appManager.getAreaCode());
        ArrayList<Bundle> array = new ArrayList();
        Cursor cursor = this.db.rawQuery("SELECT stationCode, stationName, lineCode, lineName, destination, dSubCode1, tStationCode, tLineCode, tLineName, tDestination, dSubCode2, transferInfo, transferTime1 FROM tb_transferInfo WHERE stationCode = " + stationCode, null);
        while (cursor.moveToNext()) {
            Bundle bundle = new Bundle();
            bundle.putString("stationCode", cursor.getString(0));
            bundle.putString("stationName", cursor.getString(1));
            bundle.putString("lineCode", cursor.getString(2));
            bundle.putString("lineName", cursor.getString(3));
            bundle.putString("destination", cursor.getString(4));
            bundle.putString("dSubCode1", cursor.getString(5));
            bundle.putString("tStationCode", cursor.getString(6));
            bundle.putString("tLineCode", cursor.getString(7));
            bundle.putString("tLineName", cursor.getString(8));
            bundle.putString("tDestination", cursor.getString(9));
            bundle.putString("dSubCode2", cursor.getString(10));
            bundle.putString("transferInfo", cursor.getString(11));
            bundle.putString("transferTime1", cursor.getString(12));
            array.add(bundle);
        }
        cursor.close();
        DBClose();
        return array;
    }

    public ArrayList<Bundle> getArrStationExitInfo(int stationCode) {
        DBOpen(this.appManager.getAreaCode());
        ArrayList<Bundle> array = new ArrayList();
        Cursor cursor = this.db.rawQuery("SELECT gateNumber, gateInfo FROM tb_stationGateInfo WHERE stationCode = " + stationCode + " ORDER BY gateNumber", null);
        while (cursor.moveToNext()) {
            Bundle bundle = new Bundle();
            bundle.putString("gateNumber", cursor.getString(0));
            bundle.putString("gateInfo", cursor.getString(1));
            array.add(bundle);
        }
        cursor.close();
        DBClose();
        return array;
    }

    public ArrayList<Bundle> getArrTransferSubCodeDataAll() {
        DBOpen(this.appManager.getAreaCode());
        ArrayList<Bundle> array = new ArrayList();
        Cursor cursor = this.db.rawQuery("SELECT a.subCode, b.dSubCode1, b.dSubCode2, b.transferTime1, b.transferTime2, b.tLineCode FROM tb_stationInfo a, tb_transferInfo b WHERE a.stationCode = b.tStationCode AND a.lineCode = b.tLineCode Group BY b.tSubCode ORDER BY b.tSubCode", null);
        while (cursor.moveToNext()) {
            Bundle bundle = new Bundle();
            bundle.putString("subCode", cursor.getString(0));
            bundle.putString("dSubCode1", cursor.getString(1));
            bundle.putString("dSubCode2", cursor.getString(2));
            bundle.putString("transferTime1", cursor.getString(3));
            bundle.putString("transferTime2", cursor.getString(4));
            bundle.putString("tLineCode", cursor.getString(5));
            array.add(bundle);
        }
        cursor.close();
        DBClose();
        return array;
    }

    public ArrayList<Bundle> getArrDirectSubCodeDataAll() {
        DBOpen(this.appManager.getAreaCode());
        ArrayList<Bundle> array = new ArrayList();
        Cursor cursor = this.db.rawQuery("SELECT a.subCode, b.dSubCode1, b.dSubCode2, b.transferTime1, b.transferTime2, b.tLineCode FROM tb_stationInfo a, tb_directInfo b WHERE a.stationCode = b.tStationCode AND a.lineCode = b.tLineCode ORDER BY b.tSubCode", null);
        while (cursor.moveToNext()) {
            Bundle bundle = new Bundle();
            bundle.putString("subCode", cursor.getString(0));
            bundle.putString("dSubCode1", cursor.getString(1));
            bundle.putString("dSubCode2", cursor.getString(2));
            bundle.putString("transferTime1", cursor.getString(3));
            bundle.putString("transferTime2", cursor.getString(4));
            bundle.putString("tLineCode", cursor.getString(5));
            array.add(bundle);
        }
        cursor.close();
        DBClose();
        return array;
    }

    public ArrayList<Bundle> getArrStationCodeInfo(int stationCode) {
        DBOpen(this.appManager.getAreaCode());
        ArrayList<Bundle> array = new ArrayList();
        Cursor cursor = this.db.rawQuery("SELECT stationCode, stationName, lineCode, metroLine, metroCode, naverLine, naverCode, daumCode FROM tb_stationCode WHERE stationCode = " + stationCode, null);
        while (cursor.moveToNext()) {
            Bundle bundle = new Bundle();
            bundle.putString("stationCode", cursor.getString(0));
            bundle.putString("stationName", cursor.getString(1));
            bundle.putString("lineCode", cursor.getString(2));
            bundle.putString("metroLine", cursor.getString(3));
            bundle.putString("metroCode", cursor.getString(4));
            bundle.putString("naverLine", cursor.getString(5));
            bundle.putString("naverCode", cursor.getString(6));
            bundle.putString("daumCode", cursor.getString(7));
            array.add(bundle);
        }
        cursor.close();
        DBClose();
        return array;
    }

    public Bundle getArrStationSiteCode(int stationCode) {
        int iAreaCode = this.appManager.getAreaCode();
        if (iAreaCode != 1) {
            return null;
        }
        DBOpen(iAreaCode);
        Bundle bundle = null;
        Cursor cursor = this.db.rawQuery("SELECT seoulLine, seoulCode FROM tb_stationCode WHERE stationCode = " + stationCode, null);
        if (cursor.moveToFirst()) {
            bundle = new Bundle();
            bundle.putString("seoulLine", cursor.getString(0));
            bundle.putString("seoulCode", cursor.getString(1));
        }
        cursor.close();
        DBClose();
        return bundle;
    }

    public ArrayList<Bundle> getArrStationTimeDataToWeek(int code, int week, int lCode) {
        DBOpen(this.appManager.getAreaCode(), lCode);
        ArrayList<Bundle> array = new ArrayList();
        Cursor cursor = this.db.rawQuery("SELECT hour, tData1, tSub1, wSub1, tCode1, tData2, tSub2, wSub2, tCode2, week FROM tb_tTimeData WHERE stationCode = " + code + " AND week = " + week, null);
        while (cursor.moveToNext()) {
            Bundle bundle = new Bundle();
            bundle.putString("hour", cursor.getString(0));
            bundle.putString("tData1", cursor.getString(1));
            bundle.putString("tSub1", cursor.getString(2));
            bundle.putString("wSub1", cursor.getString(3));
            bundle.putString("tCode1", cursor.getString(4));
            bundle.putString("tData2", cursor.getString(5));
            bundle.putString("tSub2", cursor.getString(6));
            bundle.putString("wSub2", cursor.getString(7));
            bundle.putString("tCode2", cursor.getString(8));
            bundle.putString("week", cursor.getString(9));
            array.add(bundle);
        }
        cursor.close();
        DBClose();
        return array;
    }

    public ArrayList<Bundle> getArrStationTimeDataToPicker(int code, int week, int lCode, int direction) {
        String sql;
        DBOpen(this.appManager.getAreaCode(), lCode);
        ArrayList<Bundle> array = new ArrayList();
        if (direction == 1) {
            sql = "SELECT a.hour, a.tData1, a.tSub1, a.wSub1, b.dName, b.exFlag FROM tb_tTimeData a, tb_trainData b WHERE stationCode = " + code + " AND week = " + week + " AND a.tCode1 = b.tCode";
        } else {
            sql = "SELECT a.hour, a.tData2, a.tSub2, a.wSub2, b.dName, b.exFlag FROM tb_tTimeData a, tb_trainData b WHERE stationCode = " + code + " AND week = " + week + " AND a.tCode2 = b.tCode";
        }
        Cursor cursor = this.db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            Bundle bundle = new Bundle();
            bundle.putString("hour", cursor.getString(0));
            bundle.putString("tData", cursor.getString(1));
            bundle.putString("tSub", cursor.getString(2));
            bundle.putString("wSub", cursor.getString(3));
            bundle.putString("dName", cursor.getString(4));
            bundle.putString("exFlag", cursor.getString(5));
            array.add(bundle);
        }
        cursor.close();
        DBClose();
        return array;
    }

    public ArrayList<Bundle> getArrStationTimeDataToNowTime(Bundle data, int code, int type, int lCode) {
        String sql;
        DBOpen(this.appManager.getAreaCode(), lCode);
        ArrayList<Bundle> array = new ArrayList();
        if (type == 1) {
            sql = "SELECT a.hour, a.tData1, a.tSub1, a.wSub1, b.dName, b.exFlag FROM tb_tTimeData a, tb_trainData b WHERE a.stationCode = " + code + " AND (a.hour > " + data.getInt("hour") + " OR (a.hour == " + data.getInt("hour") + " AND a.tData1 >= " + data.getInt("minutes") + ")) AND  a.week = " + data.getInt("week") + " AND a.tCode1 != 0 AND a.wSub1 != 99" + " AND a.tCode1 = b.tCode LIMIT " + data.getInt("limit");
        } else {
            sql = "SELECT a.hour, a.tData2, a.tSub2, a.wSub2, b.dName, b.exFlag FROM tb_tTimeData a, tb_trainData b WHERE a.stationCode = " + code + " AND (a.hour > " + data.getInt("hour") + " OR (a.hour == " + data.getInt("hour") + " AND a.tData2 >= " + data.getInt("minutes") + ")) AND  a.week = " + data.getInt("week") + " AND a.tCode2 != 0 AND a.wSub2 != 99" + " AND a.tCode2 = b.tCode LIMIT " + data.getInt("limit");
        }
        Cursor cursor = this.db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            Bundle bundle = new Bundle();
            bundle.putString("hour", cursor.getString(0));
            bundle.putString("tData", cursor.getString(1));
            bundle.putString("tSub", cursor.getString(2));
            bundle.putString("wSub", cursor.getString(3));
            bundle.putString("dName", cursor.getString(4));
            bundle.putString("exFlag", cursor.getString(5));
            array.add(bundle);
        }
        cursor.close();
        DBClose();
        return array;
    }

    public ArrayList<Bundle> getArrStationTimeDataToType(int code, int week, int lCode, int type) {
        String sql;
        DBOpen(this.appManager.getAreaCode(), lCode);
        ArrayList<Bundle> array = new ArrayList();
        if (type == 1) {
            sql = "SELECT a.hour, a.tData1, a.tSub1, a.wSub1, b.dName, b.exFlag FROM tb_tTimeData a, tb_trainData b WHERE a.stationCode = " + code + " AND  a.week = " + week + " AND a.tCode1 != 0 AND a.wSub1 != 99 AND a.tCode1 = b.tCode";
        } else {
            sql = "SELECT a.hour, a.tData2, a.tSub2, a.wSub2, b.dName, b.exFlag FROM tb_tTimeData a, tb_trainData b WHERE a.stationCode = " + code + " AND  a.week = " + week + " AND a.tCode2 != 0 AND a.wSub2 != 99 AND a.tCode2 = b.tCode";
        }
        Cursor cursor = this.db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            Bundle bundle = new Bundle();
            bundle.putString("hour", cursor.getString(0));
            bundle.putString("tData", cursor.getString(1));
            bundle.putString("tSub", cursor.getString(2));
            bundle.putString("wSub", cursor.getString(3));
            bundle.putString("dName", cursor.getString(4));
            bundle.putString("exFlag", cursor.getString(5));
            array.add(bundle);
        }
        cursor.close();
        DBClose();
        return array;
    }

    public ArrayList<Bundle> getArrTrainDataAll(int lCode) {
        DBOpen(this.appManager.getAreaCode(), lCode);
        ArrayList<Bundle> array = new ArrayList();
        Cursor cursor = this.db.rawQuery("SELECT tName, dName, exFlag FROM tb_trainData", null);
        while (cursor.moveToNext()) {
            Bundle bundle = new Bundle();
            bundle.putString("tName", cursor.getString(0));
            bundle.putString("dName", cursor.getString(1));
            bundle.putString("exFlag", cursor.getString(2));
            array.add(bundle);
        }
        cursor.close();
        DBClose();
        return array;
    }

    public ArrayList<Bundle> getArrSearchTrainPathDataToTime(Bundle data, String sCode1, String tCode, int week, int lCode, int type, int exFlag) {
        String sql;
        DBOpen(this.appManager.getAreaCode(), lCode);
        ArrayList<Bundle> array = new ArrayList();
        int hour = data.getInt("hour");
        int tData = data.getInt("minutes");
        int time = ((hour * 3600) + (tData * 60)) + data.getInt("seconds");
        if (tCode == null || tCode.length() <= 0) {
            tCode = "''";
        }
        if (type == 1) {
            sql = "SELECT a.stationCode, a.hour, a.tData1, a.tSub1, a.wSub1, b.exFlag, b.ioType, a.tCode1, b.dName FROM tb_tTimeData a, tb_trainData b WHERE a.stationCode <= " + sCode1 + " AND a.tCode1 = (SELECT c.tCode1 FROM tb_tTimeData c, tb_trainData d " + "WHERE c.stationCode = " + sCode1 + " AND c.tCode1 != " + tCode + " AND c.week = " + week + " AND d.exFlag >= " + exFlag + " AND c.tCode1 = d.tCode " + " AND ((c.hour * 3600) + (c.tData1 * 60) + tSub1 +" + " (CASE WHEN (wSub1 != 88 AND wSub1 != 99)  THEN wSub1 ELSE 0 END)) >= " + time + " limit 1 ) AND a.tCode1 = b.tCode" + " AND a.week = " + week + " ORDER BY a.hour, a.tData1";
        } else {
            sql = "SELECT a.stationCode, a.hour, a.tData2, a.tSub2, a.wSub2, b.exFlag, b.ioType, a.tCode2, b.dName FROM tb_tTimeData a, tb_trainData b WHERE a.stationCode >= " + sCode1 + " AND a.tCode2 = (SELECT c.tCode2 FROM tb_tTimeData c, tb_trainData d " + "WHERE c.stationCode = " + sCode1 + " AND c.tCode2 != " + tCode + " AND c.week = " + week + " AND d.exFlag >= " + exFlag + " AND c.tCode2 = d.tCode " + " AND ((c.hour * 3600) + (c.tData2 * 60) + tSub2 +" + " (CASE WHEN (wSub2 != 88 AND wSub2 != 99)  THEN wSub2 ELSE 0 END)) >= " + time + " limit 1 ) AND a.tCode2 = b.tCode" + " AND a.week = " + week + " ORDER BY a.hour, a.tData2";
        }
        Cursor cursor = this.db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            Bundle bundle = new Bundle();
            bundle.putString("stationCode", cursor.getString(0));
            bundle.putString("hour", cursor.getString(1));
            bundle.putString("tData", cursor.getString(2));
            bundle.putString("tSub", cursor.getString(3));
            bundle.putString("wSub", cursor.getString(4));
            bundle.putString("exFlag", cursor.getString(5));
            bundle.putString("ioType", cursor.getString(6));
            bundle.putString("tCode", cursor.getString(7));
            bundle.putString("dName", cursor.getString(8));
            array.add(bundle);
        }
        cursor.close();
        DBClose();
        return array;
    }

    public ArrayList<Bundle> getArrSearchTrainPathDataToTime(Bundle data, String sCode1, String sCode2, String tCode, int week, int lCode, int type, int exFlag) {
        String sql;
        DBOpen(this.appManager.getAreaCode(), lCode);
        ArrayList<Bundle> array = new ArrayList();
        int hour = data.getInt("hour");
        int tData = data.getInt("minutes");
        int time = ((hour * 3600) + (tData * 60)) + data.getInt("seconds");
        if (tCode == null) {
            tCode = "''";
        }
        if (type == 1) {
            sql = "SELECT a.stationCode, a.hour, a.tData1, a.tSub1, a.wSub1, b.exFlag, b.ioType, a.tCode1, b.dName FROM tb_tTimeData a, tb_trainData b WHERE a.stationCode <= " + sCode1 + " AND a.tCode1 = (SELECT c.tCode1 FROM tb_tTimeData c, tb_trainData d " + "WHERE c.stationCode = " + sCode1 + " AND c.tCode1 in (SELECT c.tCode1 FROM tb_tTimeData c, tb_trainData d " + "WHERE c.stationCode = " + sCode2 + " AND c.tCode1 != " + tCode + " AND c.week = " + week + " AND c.tCode1 = d.tCode ) " + " AND c.week = " + week + " AND d.exFlag >= " + exFlag + " AND c.tCode1 = d.tCode " + " AND ((c.hour * 3600) + (c.tData1 * 60) + tSub1 +" + " (CASE WHEN (wSub1 != 88 AND wSub1 != 99)  THEN wSub1 ELSE 0 END)) >= " + time + " limit 1 ) AND a.tCode1 = b.tCode" + " AND a.week = " + week + " ORDER BY a.hour, a.tData1";
        } else {
            sql = "SELECT a.stationCode, a.hour, a.tData2, a.tSub2, a.wSub2, b.exFlag, b.ioType, a.tCode2, b.dName FROM tb_tTimeData a, tb_trainData b WHERE a.stationCode >= " + sCode1 + " AND a.tCode2 = (SELECT c.tCode2 FROM tb_tTimeData c, tb_trainData d " + "WHERE c.stationCode = " + sCode1 + " AND c.tCode2 in (SELECT c.tCode2 FROM tb_tTimeData c, tb_trainData d " + "WHERE c.stationCode = " + sCode2 + " AND c.tCode2 != " + tCode + " AND c.week = " + week + " AND c.tCode2 = d.tCode ) " + " AND c.week = " + week + " AND d.exFlag >= " + exFlag + " AND c.tCode2 = d.tCode " + " AND ((c.hour * 3600) + (c.tData2 * 60) + tSub2 +" + " (CASE WHEN (wSub2 != 88 AND wSub2 != 99)  THEN wSub2 ELSE 0 END)) >= " + time + " limit 1 ) AND a.tCode2 = b.tCode" + " AND a.week = " + week + " ORDER BY a.hour, a.tData2";
        }
        Cursor cursor = this.db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            Bundle bundle = new Bundle();
            bundle.putString("stationCode", cursor.getString(0));
            bundle.putString("hour", cursor.getString(1));
            bundle.putString("tData", cursor.getString(2));
            bundle.putString("tSub", cursor.getString(3));
            bundle.putString("wSub", cursor.getString(4));
            bundle.putString("exFlag", cursor.getString(5));
            bundle.putString("ioType", cursor.getString(6));
            bundle.putString("tCode", cursor.getString(7));
            bundle.putString("dName", cursor.getString(8));
            array.add(bundle);
        }
        cursor.close();
        DBClose();
        return array;
    }

    public ArrayList<Bundle> getArrSearchTrainPathDataToLine2(Bundle data, String code, String tCode, int week, int lCode, int type) {
        String sql;
        DBOpen(this.appManager.getAreaCode(), lCode);
        ArrayList<Bundle> array = new ArrayList();
        int hour = data.getInt("hour");
        int tData = data.getInt("minutes");
        int time = ((hour * 3600) + (tData * 60)) + data.getInt("seconds");
        if (tCode == null) {
            tCode = "''";
        }
        if (type == 1) {
            sql = "SELECT a.stationCode, a.hour, a.tData1, a.tSub1, a.wSub1, b.exFlag, b.ioType, a.tCode1, b.dName FROM tb_tTimeData a, tb_trainData b WHERE a.tCode1 = (SELECT c.tCode1 FROM tb_tTimeData c, tb_trainData d WHERE c.stationCode = " + code + " AND c.tCode1 != " + tCode + " AND c.week = " + week + " AND c.tCode1 = d.tCode " + " AND ((c.hour * 3600) + (c.tData1 * 60) + tSub1 +" + " (CASE WHEN (wSub1 != 88 AND wSub1 != 99)  THEN wSub1 ELSE 0 END)) >= " + time + " limit 1 ) AND a.tCode1 = b.tCode" + " AND a.week = " + week + " ORDER BY a.hour, a.tData1";
        } else {
            sql = "SELECT a.stationCode, a.hour, a.tData2, a.tSub2, a.wSub2, b.exFlag, b.ioType, a.tCode2, b.dName FROM tb_tTimeData a, tb_trainData b WHERE a.tCode2 = (SELECT c.tCode2 FROM tb_tTimeData c, tb_trainData d WHERE c.stationCode = " + code + " AND c.tCode2 != " + tCode + " AND c.week = " + week + " AND c.tCode2 = d.tCode " + " AND ((c.hour * 3600) + (c.tData2 * 60) + tSub2 +" + " (CASE WHEN (wSub2 != 88 AND wSub2 != 99)  THEN wSub2 ELSE 0 END)) >= " + time + " limit 1 ) AND a.tCode2 = b.tCode" + " AND a.week = " + week + " ORDER BY a.hour, a.tData2";
        }
        Cursor cursor = this.db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            Bundle bundle = new Bundle();
            bundle.putString("stationCode", cursor.getString(0));
            bundle.putString("hour", cursor.getString(1));
            bundle.putString("tData", cursor.getString(2));
            bundle.putString("tSub", cursor.getString(3));
            bundle.putString("wSub", cursor.getString(4));
            bundle.putString("exFlag", cursor.getString(5));
            bundle.putString("ioType", cursor.getString(6));
            bundle.putString("tCode", cursor.getString(7));
            bundle.putString("dName", cursor.getString(8));
            array.add(bundle);
        }
        cursor.close();
        DBClose();
        return array;
    }

    public ArrayList<Bundle> getArrStationFirstLastTimeData(String code, int week, int lCode, int type, int tType) {
        String sql;
        DBOpen(this.appManager.getAreaCode(), lCode);
        ArrayList<Bundle> array = new ArrayList();
        if (type == 1) {
            if (tType == 1) {
                sql = "SELECT a.hour, a.tData1, a.tSub1, a.wSub1, a.tCode1, b.exFlag FROM tb_tTimeData a, tb_trainData b WHERE a.stationCode = " + code + " AND a.week = " + week + " AND a.tCode1 = b.tCode " + " AND a.tCode1 != 0 ORDER BY a.hour, a.tData1";
            } else {
                sql = "SELECT a.hour, a.tData1, a.tSub1, a.wSub1, a.tCode1, b.exFlag FROM tb_tTimeData a, tb_trainData b WHERE a.stationCode = " + code + " AND a.week = " + week + " AND a.tCode1 = b.tCode " + " AND a.tCode1 != 0 ORDER BY a.hour DESC, a.tData1 DESC";
            }
        } else if (tType == 1) {
            sql = "SELECT a.hour, a.tData2, a.tSub2, a.wSub2, a.tCode2, b.exFlag FROM tb_tTimeData a, tb_trainData b WHERE a.stationCode = " + code + " AND a.week = " + week + " AND a.tCode2 = b.tCode " + " AND tCode2 != 0 ORDER BY hour, tData2";
        } else {
            sql = "SELECT a.hour, a.tData2, a.tSub2, a.wSub2, a.tCode2, b.exFlag FROM tb_tTimeData a, tb_trainData b WHERE a.stationCode = " + code + " AND a.week = " + week + " AND a.tCode2 = b.tCode " + " AND tCode2 != 0 ORDER BY hour DESC, tData2 DESC";
        }
        Cursor cursor = this.db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            Bundle bundle = new Bundle();
            bundle.putString("hour", cursor.getString(0));
            bundle.putString("tData", cursor.getString(1));
            bundle.putString("tSub", cursor.getString(2));
            bundle.putString("wSub", cursor.getString(3));
            bundle.putString("tCode", cursor.getString(4));
            bundle.putString("exFlag", cursor.getString(5));
            array.add(bundle);
        }
        cursor.close();
        DBClose();
        return array;
    }

    public ArrayList<Bundle> getArrStationUserTimeData(String code, int hour, int tData, int tSub, int week, int lCode, int type, int seType) {
        String sql;
        DBOpen(this.appManager.getAreaCode(), lCode);
        ArrayList<Bundle> array = new ArrayList();
        if (type == 1) {
            sql = "SELECT a.hour, a.tData1, a.tSub1, a.wSub1, a.tCode1, b.exFlag FROM tb_tTimeData a, tb_trainData b WHERE a.stationCode = " + code + " AND (a.hour < " + hour + " OR (a.hour == " + hour + " AND a.tData1 < " + tData + ") OR (a.hour = " + hour + " AND a.tData1 = " + tData + " AND a.tSub1 <= " + tSub + ")) AND a.week = " + week + " AND a.tCode1 != 0 AND a.wSub1 != " + seType + " AND a.tCode1 = b.tCode " + " ORDER BY a.hour DESC, a.tData1 DESC";
        } else {
            sql = "SELECT a.hour, a.tData2, a.tSub2, a.wSub2, a.tCode2, b.exFlag FROM tb_tTimeData a, tb_trainData b WHERE a.stationCode = " + code + " AND (a.hour < " + hour + " OR (a.hour == " + hour + " AND a.tData2 < " + tData + ") OR (a.hour = " + hour + " AND a.tData2 = " + tData + " AND a.tSub2 <= " + tSub + ")) AND a.week = " + week + " AND a.tCode2 != 0 AND a.wSub2 != " + seType + " AND a.tCode2 = b.tCode " + " ORDER BY a.hour DESC, a.tData2 DESC";
        }
        Cursor cursor = this.db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            Bundle bundle = new Bundle();
            bundle.putString("hour", cursor.getString(0));
            bundle.putString("tData", cursor.getString(1));
            bundle.putString("tSub", cursor.getString(2));
            bundle.putString("wSub", cursor.getString(3));
            bundle.putString("tCode", cursor.getString(4));
            bundle.putString("exFlag", cursor.getString(5));
            array.add(bundle);
        }
        cursor.close();
        DBClose();
        return array;
    }

    public int getArrStationTimeDataToCheck(int code, int week, int lCode) {
        int iCount = 0;
        DBOpen(this.appManager.getAreaCode(), lCode);
        Cursor cursor = this.db.rawQuery("SELECT COUNT(rowid) FROM tb_tTimeData WHERE stationCode = " + code + " AND week = " + week, null);
        if (cursor.moveToFirst()) {
            iCount = Integer.parseInt(cursor.getString(0));
        }
        cursor.close();
        DBClose();
        return iCount;
    }

    public ArrayList<Bundle> getArrConvInfoListAll() {
        DBOpen(this.appManager.getAreaCode());
        ArrayList<Bundle> array = new ArrayList();
        Cursor cursor = this.db.rawQuery("SELECT cCode, cName FROM tb_convInfoData GROUP BY cCode ORDER BY cCode", null);
        while (cursor.moveToNext()) {
            Bundle bundle = new Bundle();
            bundle.putString("cCode", cursor.getString(0));
            bundle.putString("cName", cursor.getString(1));
            array.add(bundle);
        }
        cursor.close();
        DBClose();
        return array;
    }

    public ArrayList<Bundle> getArrConvDetailList(int code) {
        DBOpen(this.appManager.getAreaCode());
        ArrayList<Bundle> array = new ArrayList();
        Cursor cursor = this.db.rawQuery("SELECT b.stationCode, b.stationName, b.lineCode, b.lineName, IFNULL(a.tel,''), b.transfer FROM tb_convInfoData a, tb_stationInfo b WHERE a.stationCode == b.stationCode AND a.lineCode == b.lineCode AND cCode = " + code + " GROUP BY b.stationCode ORDER BY b.lineCode, b.stationCode", null);
        while (cursor.moveToNext()) {
            Bundle bundle = new Bundle();
            bundle.putString("stationCode", cursor.getString(0));
            bundle.putString("stationName", cursor.getString(1));
            bundle.putString("lineCode", cursor.getString(2));
            bundle.putString("lineName", cursor.getString(3));
            bundle.putString("tel", cursor.getString(4));
            bundle.putString("transfer", cursor.getString(5));
            array.add(bundle);
        }
        cursor.close();
        DBClose();
        return array;
    }

    public int getFileInfo(String fileName) {
        int res = 0;
        DBOpen(fileName);
        Cursor cursor = this.db.rawQuery("SELECT date FROM tb_dataInfo LIMIT 1", null);
        if (cursor.moveToFirst()) {
            res = cursor.getInt(0);
        }
        cursor.close();
        DBClose();
        return res;
    }

    public static String getNowTimeData() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA).format(new Date(System.currentTimeMillis()));
    }

    private int getLineCount(int area) {
        switch (area) {
            case 1:
                return 19;
            default:
                return 0;
        }
    }

    private String getDataFileName(int area) {
        return String.format(Locale.US, "SubwayData0%d.sqlite", new Object[]{Integer.valueOf(area)});
    }

    private String getDataFileName(int area, int line) {
        return String.format(Locale.US, "SubwayTime0%d_%02d.sqlite", new Object[]{Integer.valueOf(area), Integer.valueOf(line)});
    }

    private String getUserFileName() {
        return "SubwayUserData.sqlite";
    }

    public int initDBFile(int fileCode) throws Throwable {
        String fileName = getDataFileName(this.appManager.getAreaCode());
        String dirName = "databases/";
        int defCode = getAppInfoCode(this.appManager.getAreaCode());
        checkDBFile(dirName, fileName, defCode, fileCode);
        int lineCount = getLineCount(this.appManager.getAreaCode());
        for (int i = 1; i <= lineCount; i++) {
            checkDBFile(dirName, getDataFileName(this.appManager.getAreaCode(), i), defCode, fileCode);
        }
        checkUserFile(dirName, getUserFileName());
        return defCode;
    }

    public int initSubwayDBFile(int fileCode) throws Throwable {
        String fileName = getDataFileName(this.appManager.getAreaCode());
        String dirName = "databases/";
        int defCode = getAppInfoCode(this.appManager.getAreaCode());
        checkDBFile(dirName, fileName, defCode, fileCode);
        int lineCount = getLineCount(this.appManager.getAreaCode());
        for (int i = 1; i <= lineCount; i++) {
            checkDBFile(dirName, getDataFileName(this.appManager.getAreaCode(), i), defCode, fileCode);
        }
        return defCode;
    }

    public int removeSubwayDBFile(int areaCode) {
        String fileName = getDataFileName(areaCode);
        String dirName = "databases/";
        int defCode = getAppInfoCode(this.appManager.getAreaCode());
        removeDBFile(dirName, fileName);
        int lineCount = getLineCount(areaCode);
        for (int i = 1; i <= lineCount; i++) {
            removeDBFile(dirName, getDataFileName(areaCode, i));
        }
        return defCode;
    }

    @SuppressLint({"SdCardPath"})
    public void checkDBFile(String dirName, String fileName, int defCode, int fileCode) throws Throwable {
        File dir = new File("/data/data/" + this.mContext.getPackageName() + "/" + dirName);
        dir.mkdir();
        Boolean bFlag = Boolean.valueOf(false);
        File file = new File(dir, fileName);
        if (!file.exists()) {
            bFlag = Boolean.valueOf(true);
        } else if (defCode > fileCode) {
            file.delete();
            bFlag = Boolean.valueOf(true);
        }
        if (bFlag.booleanValue()) {
            copeDBFile(file, dirName, fileName);
        }
    }

    public void checkUserFile(String dirName, String fileName) throws Throwable {
        File dir = new File("/data/data/" + this.mContext.getPackageName() + "/" + dirName);
        dir.mkdir();
        File file = new File(dir, fileName);
        if (!file.exists()) {
            copeDBFile(file, dirName, fileName);
        }
    }

    public void copeDBFile(File file, String dirName, String fileName) throws Throwable {
        IOException e;
        Throwable th;
        BufferedOutputStream br = null;
        BufferedInputStream bi = null;
        BufferedOutputStream br2 = null;
        try {
            //BufferedOutputStream br2;
            file.createNewFile();
            BufferedInputStream bi2 = new BufferedInputStream(this.mContext.getAssets().open(new StringBuilder(String.valueOf(dirName)).append(fileName).toString()));
            try {
                br2 = new BufferedOutputStream(new FileOutputStream(file));
            } catch (IOException e2) {
                e = e2;
                bi = bi2;
                try {
                    Log.e("IO", "File Output Error");
                    e.printStackTrace();
                    if (bi != null) {
                        try {
                            bi.close();
                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                            return;
                        }
                    }
                    if (br == null) {
                        br.close();
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (bi != null) {
                        try {
                            bi.close();
                        } catch (IOException ioe2) {
                            ioe2.printStackTrace();
                            throw th;
                        }
                    }
                    if (br != null) {
                        br.close();
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                bi = bi2;
                if (bi != null) {
                    bi.close();
                }
                if (br != null) {
                    br.close();
                }
                throw th;
            }
            try {
                byte[] msg = new byte[1024];
                int i = bi2.read(msg);
                do {
                    br2.write(msg);
                } while (bi2.read(msg) != -1);
                if (bi2 != null) {
                    try {
                        bi2.close();
                    } catch (IOException ioe22) {
                        ioe22.printStackTrace();
                    }
                }
                if (br2 != null) {
                    br2.close();
                    bi = bi2;
                    br = br2;
                    return;
                }
                bi = bi2;
                br = br2;
            } catch (IOException e3) {
                e = e3;
                bi = bi2;
                br = br2;
                Log.e("IO", "File Output Error");
                e.printStackTrace();
                if (bi != null) {
                    bi.close();
                }
                if (br == null) {
                    br.close();
                }
            } catch (Throwable th4) {
                th = th4;
                bi = bi2;
                br = br2;
                if (bi != null) {
                    bi.close();
                }
                if (br != null) {
                    br.close();
                }
                throw th;
            }
        } catch (IOException e4) {
            e = e4;
            Log.e("IO", "File Output Error");
            e.printStackTrace();
            if (bi != null) {
                bi.close();
            }
            if (br == null) {
                br.close();
            }
        }
    }

    @SuppressLint({"SdCardPath"})
    public void removeDBFile(String dirName, String fileName) {
        File dir = new File("/data/data/" + this.mContext.getPackageName() + "/" + dirName);
        dir.mkdir();
        File file = new File(dir, fileName);
        if (file.exists()) {
            file.delete();
        }
    }
}
