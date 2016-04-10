package com.example.hyejung.easysubway.DBmodel;

public class StationDataModel {
    float _distanceData = 0.0f;
    float[] _floatData = new float[3];
    int[] _intData = new int[7];
    int[] _pathData = new int[4];

    public StationDataModel() {
        resetData();
    }

    public void setIntData(int key, int value) {
        this._intData[key] = value;
    }

    public int getIntData(int key) {
        return this._intData[key];
    }

    public void setFloatData(int key, float value) {
        this._floatData[key] = value;
    }

    public float getFloatData(int key) {
        return this._floatData[key];
    }

    public void setPathData(int key, int value) {
        this._pathData[key] = value;
    }

    public int getPathData(int key) {
        return this._pathData[key];
    }

    public float getDistanceData() {
        return this._distanceData;
    }

    public void setDistanceData(float distance) {
        this._distanceData = distance;
    }

    public void resetData() {
        int i;
        for (i = 0; i < this._intData.length; i++) {
            this._intData[i] = 0;
        }
        for (i = 0; i < this._floatData.length; i++) {
            this._floatData[i] = 0.0f;
        }
        resetPathData();
    }

    public void resetPathData() {
        this._pathData[0] = 0;
        this._pathData[1] = 0;
        this._pathData[2] = 0;
        this._pathData[3] = 0;
        this._distanceData = 0.0f;
    }
}
