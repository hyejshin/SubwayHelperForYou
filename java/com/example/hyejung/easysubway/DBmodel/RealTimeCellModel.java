package com.example.hyejung.easysubway.DBmodel;

import android.os.Bundle;
import java.util.ArrayList;

public class RealTimeCellModel {
    ArrayList<Bundle> arrData = null;
    int cellType = 0;
    int endFlag = 0;
    String info = null;
    int lineCode = 0;
    String stationName = null;
    String subTitle = null;
    String title = null;
    int trainLine = 0;

    public int getCellType() {
        return this.cellType;
    }

    public void setCellType(int cellType) {
        this.cellType = cellType;
    }

    public int getTrainLine() {
        return this.trainLine;
    }

    public void setTrainLine(int trainLine) {
        this.trainLine = trainLine;
    }

    public int getEndFlag() {
        return this.endFlag;
    }

    public void setEndFlag(int endFlag) {
        this.endFlag = endFlag;
    }

    public int getLineCode() {
        return this.lineCode;
    }

    public void setLineCode(int lineCode) {
        this.lineCode = lineCode;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return this.subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getInfo() {
        return this.info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getStationName() {
        return this.stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public ArrayList<Bundle> getArrData() {
        return this.arrData;
    }

    public void setArrData(ArrayList<Bundle> arrData) {
        this.arrData = arrData;
    }
}
