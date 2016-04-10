package com.example.hyejung.easysubway.DBmodel;

import android.os.Bundle;
import java.util.ArrayList;

public class SearchDataModel {
    int aniCurrentPoint = 0;
    int aniTotalPoint = 0;
    ArrayList<ArrayList<Bundle>> arrPathData = null;
    ArrayList<Bundle> arrPathIcon = null;
    ArrayList<Bundle> arrSearchPathData = null;
    ArrayList<Bundle> arrTransferData = null;
    Bundle dicInfoData = null;
    float distance = 0.0f;
    Bundle searchData = null;
    int searchType = 0;
    int totalTime = 0;
    int transferCount = 0;
    int transferTime1 = 0;
    int transferTime2 = 0;

    public SearchDataModel() {
        clear();
    }

    public void clear() {
        this.aniTotalPoint = 0;
        this.aniCurrentPoint = 0;
        this.transferCount = 0;
        this.transferTime1 = 0;
        this.transferTime2 = 0;
        this.totalTime = 0;
        this.searchType = 0;
        this.distance = 0.0f;
        if (this.searchData != null) {
            this.searchData.clear();
            this.searchData = null;
        }
        if (this.dicInfoData != null) {
            this.dicInfoData.clear();
            this.dicInfoData = null;
        }
        if (this.arrPathData != null) {
            this.arrPathData.clear();
            this.arrPathData = null;
        }
        if (this.arrTransferData != null) {
            this.arrTransferData.clear();
            this.arrTransferData = null;
        }
        if (this.arrPathIcon != null) {
            this.arrPathIcon.clear();
            this.arrPathIcon = null;
        }
        if (this.arrSearchPathData != null) {
            this.arrSearchPathData.clear();
            this.arrSearchPathData = null;
        }
    }

    public void clearPathIcon() {
        if (this.arrPathIcon != null) {
            this.arrPathIcon.clear();
            this.arrPathIcon = null;
        }
    }

    public int getTransferCount() {
        return this.transferCount;
    }

    public void setTransferCount(int transferCount) {
        this.transferCount = transferCount;
    }

    public int getTransferTime1() {
        return this.transferTime1;
    }

    public void setTransferTime1(int transferTime1) {
        this.transferTime1 = transferTime1;
    }

    public int getTransferTime2() {
        return this.transferTime2;
    }

    public void setTransferTime2(int transferTime2) {
        this.transferTime2 = transferTime2;
    }

    public int getTotalTime() {
        return this.totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public int getSearchType() {
        return this.searchType;
    }

    public void setSearchType(int searchType) {
        this.searchType = searchType;
    }

    public float getDistance() {
        return this.distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public Boolean getAniPointFlag() {
        return Boolean.valueOf(this.aniCurrentPoint < this.aniTotalPoint);
    }

    public int getAniCurrentPoint() {
        return this.aniCurrentPoint;
    }

    public void setAniCurrentPoint(int aniCurrentPoint) {
        this.aniCurrentPoint = aniCurrentPoint;
        if (aniCurrentPoint >= this.aniTotalPoint) {
            aniCurrentPoint = this.aniTotalPoint;
        }
    }

    public int getAniTotalPoint() {
        return this.aniTotalPoint;
    }

    public Bundle getCurrentPoint() {
        return (Bundle) this.arrSearchPathData.get(this.aniCurrentPoint);
    }

    public void addCurrentPoint() {
        this.aniCurrentPoint++;
    }

    public void stopCurrentPoint() {
        this.aniCurrentPoint = this.aniTotalPoint;
    }

    public Bundle getSearchData() {
        return this.searchData;
    }

    public void setSearchData(Bundle object) {
        if (object == null) {
            return;
        }
        if (this.searchData != null) {
            this.searchData.clear();
            this.searchData.putAll(object);
            return;
        }
        this.searchData = new Bundle(object);
    }

    public ArrayList<ArrayList<Bundle>> getArrPathData() {
        return this.arrPathData;
    }

    public void setArrPathData(ArrayList<ArrayList<Bundle>> object) {
        if (object == null) {
            return;
        }
        if (this.arrPathData != null) {
            this.arrPathData.clear();
            this.arrPathData.addAll(object);
            return;
        }
        this.arrPathData = new ArrayList(object);
    }

    public ArrayList<Bundle> getArrSearchPathData() {
        return this.arrSearchPathData;
    }

    public void setArrSearchPathData(ArrayList<Bundle> object) {
        if (object != null) {
            if (this.arrSearchPathData != null) {
                this.arrSearchPathData.clear();
                this.arrSearchPathData.addAll(object);
            } else {
                this.arrSearchPathData = new ArrayList(object);
            }
            this.aniTotalPoint = this.arrSearchPathData.size();
            return;
        }
        this.aniTotalPoint = 0;
    }

    public ArrayList<Bundle> getArrTransferData() {
        return this.arrTransferData;
    }

    public void setArrTransferData(ArrayList<Bundle> object) {
        if (object == null) {
            return;
        }
        if (this.arrTransferData != null) {
            this.arrTransferData.clear();
            this.arrTransferData.addAll(object);
            return;
        }
        this.arrTransferData = new ArrayList(object);
    }

    public Bundle getDicInfoData() {
        return this.dicInfoData;
    }

    public void setDicInfoData(Bundle object) {
        if (object == null) {
            return;
        }
        if (this.dicInfoData != null) {
            this.dicInfoData.clear();
            this.dicInfoData.putAll(object);
            return;
        }
        this.dicInfoData = new Bundle(object);
    }

    public ArrayList<Bundle> getArrPathIcon() {
        return this.arrPathIcon;
    }

    public void setArrPathIcon(ArrayList<Bundle> object) {
        if (object == null) {
            return;
        }
        if (this.arrPathIcon != null) {
            this.arrPathIcon.clear();
            this.arrPathIcon.addAll(object);
            return;
        }
        this.arrPathIcon = new ArrayList(object);
    }
}
