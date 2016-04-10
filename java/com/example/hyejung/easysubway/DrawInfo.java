package com.example.hyejung.easysubway;

import java.util.ArrayList;

public class DrawInfo {
    static ArrayList<String> name = new ArrayList<String>();
    static ArrayList<String> id = new ArrayList<String>();
    static ArrayList<String> beforeStation = new ArrayList<String>();
    static ArrayList<String> nextStation = new ArrayList<String>();
    static ArrayList<String> beforeId = new ArrayList<String>();
    static ArrayList<String> nextId = new ArrayList<String>();
    static ArrayList<String> travelTime = new ArrayList<String>();
    static ArrayList<String> startTime = new ArrayList<String>();
    static ArrayList<String> arriveTime = new ArrayList<String>();
    static ArrayList<String> cartNo = new ArrayList<String>();
    static String pathOption;
    static String timeOption;
    static int week, hour, minute, totalStation;

    static String userOption;
    static int visitNum;

    public DrawInfo(){
    }

    public void reset(){
        name.clear();
        beforeStation.clear();
        nextStation.clear();
        beforeId.clear();
        nextId.clear();
        id.clear();
        travelTime.clear();
        cartNo.clear();
    }

    public void resetTime(){
        startTime.clear();
        arriveTime.clear();
    }

    public String getUserOption(){ return userOption; }

    public void setUserOption(String str){ userOption = str; }

    public int getVisitNum(){
        return visitNum;
    }

    public void setVisitNum(int num) { visitNum = num; }

    public int getTotalTransfer(){
        return name.size();
    }

    public void setName(String str){
        name.add(str);
    }

    public String getName(int idx){
        return name.get(idx);
    }

    public void setBeforeStation(String str){
        beforeStation.add(str);
    }

    public String getBeforeStation(int idx){
        return beforeStation.get(idx);
    }

    public void setNextStation(String str){
        nextStation.add(str);
    }

    public String getNextStation(int idx){
        return nextStation.get(idx);
    }

    public void setBeforeId(String str){
        beforeId.add(str);
    }

    public String getBeforeId(int idx){
        return beforeId.get(idx);
    }

    public void setNextId(String str){
        nextId.add(str);
    }

    public String getNextId(int idx){
        return nextId.get(idx);
    }

    public void setId(String str){
        id.add(str);
    }

    public String getId(int idx){
        return id.get(idx);
    }

    public void setTravelTime(String str){
        travelTime.add(str);
    }

    public String getTravelTime(int idx){
        return travelTime.get(idx);
    }

    public void setArriveTime(String str){
        arriveTime.add(str);
    }

    public String getArriveTime(int idx){
        return arriveTime.get(idx);
    }

    public void setStartTime(String str){
        startTime.add(str);
    }

    public String getStartTime(int idx){
        return startTime.get(idx);
    }

    public void setCartNo(String str){
        cartNo.add(str);
    }

    public String getCartNo(int idx){
        return cartNo.get(idx);
    }

    public void setPathOption(String str) { pathOption = str;}

    public String getPathOption() { return pathOption; }

    public void setTimeOption(String str) { timeOption = str;}

    public String getTimeOption() { return timeOption; }

    public void setTotalStation(int num) { totalStation = num;}

    public int getTotalStation() { return totalStation; }

    public void setWeek(int n) { week = n; }

    public int getWeek() { return week; }

    public void setHour(int n) { hour = n; }

    public int getHour() { return hour; }

    public void setMinute(int n) { minute = n; }

    public int getMinute() { return minute; }

    public String getTime() { return setTime(hour, minute); }

    public String setTime(int h, int m){
        String time = "";
        if(h > 9)
            time += h;
        else
            time += "0" + h;
        time += ":";
        if(m > 9)
            time += m;
        else
            time += "0" + m;

        return time;
    }

    public int stringToInt(String str){
        int len = str.length();
        int num = 0;
        for(int i=0; i<len; i++){
            num = num*10 + str.charAt(i)-'0';
        }
        return num;
    }
}

