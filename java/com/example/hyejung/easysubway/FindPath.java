package com.example.hyejung.easysubway;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class FindPath extends Activity {
    TextView tv;
    String start;
    String arrive;
    String pathOption;

    String stationID[] = new String[50];
    String stationName[] = new String[50];
    String transferStation[] = new String[5];
    String transferStationID[] = new String[5];
    String travelTime[] = new String[5];
    int totalStation = 0;
    int totalTransfer = 0;
    int totalTime = 0;
    int transferCount = 0;
    int flag = 0;
    DrawInfo info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tv = (TextView) findViewById(R.id.tv);
        Intent it = getIntent();
        start = it.getStringExtra("it_start");
        arrive = it.getStringExtra("it_arrive");
        pathOption = it.getStringExtra("it_pathOption");

        info = new DrawInfo();
        info.reset();
        info.resetTime();
        getPassByStation(start, arrive);
    }

    public void getPassByStation(String start, String arrive){
        String serviceURI = "http://swopenAPI.seoul.go.kr/api/subway/5a6851595968696b373747544e5a79/xml/shortestRoute/0/1/";
        String strUrl = serviceURI + URLEncoder.encode(start) +"/"+ URLEncoder.encode(arrive);

        new DownloadWebpageTask().execute(strUrl);
    }

    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                return (String) downloadUrl((String) urls[0]);
            } catch (IOException e) {
                return "다운로드 실패";
            }
        }

        protected void onPostExecute(String result) {
            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();

                xpp.setInput(new StringReader(result));
                int eventType = xpp.getEventType();
                boolean bSet = false;
                String field = "";
                String shortTimePassBystationID = "";
                String shortTimePassBystationName = "";
                String minTransferPassBystationID = "";
                String minTransferPassBystationName = "";

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_DOCUMENT) {
                        ;
                    } else if (eventType == XmlPullParser.START_TAG) {
                        String tag_name = xpp.getName();
                        if (tag_name.equals("shtStatnId") || tag_name.equals("shtStatnNm") ||
                                tag_name.equals("minStatnId") || tag_name.equals("minStatnNm")){
                            field = tag_name;
                            bSet = true;
                        }
                    } else if (eventType == XmlPullParser.TEXT) {
                        if (bSet) {
                            String content = xpp.getText();
                            if(field.equals("shtStatnId"))
                                shortTimePassBystationID = content;
                            else if(field.equals("shtStatnNm"))
                                shortTimePassBystationName = content;
                            else if(field.equals("minStatnId"))
                                minTransferPassBystationID = content;
                            else if(field.equals("minStatnNm")) {
                                minTransferPassBystationName = content;

                                if(pathOption.equals("최단거리")) {
                                    parseStationInfo(shortTimePassBystationID, stationID);
                                    parseStationInfo(shortTimePassBystationName, stationName);
                                }
                                else {
                                    parseStationInfo(minTransferPassBystationID, stationID);
                                    parseStationInfo(minTransferPassBystationName, stationName);
                                }
                            }
                            bSet = false;
                        }
                    } else if (eventType == XmlPullParser.END_TAG) {
                        ;
                    }
                    eventType = xpp.next();
                }
            } catch (Exception e) {
                //tv.setText("오류");
            }
        }

        private String downloadUrl(String myurl) throws IOException {

            HttpURLConnection conn = null;
            try{
                URL url = new URL(myurl);
                conn = (HttpURLConnection) url.openConnection();
                BufferedInputStream buf = new BufferedInputStream(conn.getInputStream());
                BufferedReader bufreader = new BufferedReader(new InputStreamReader(buf, "utf-8"));
                String line = null;
                String page = "";
                while((line = bufreader.readLine()) != null) {
                    page += line;
                }

                return page;
            } finally {
                conn.disconnect();
            }
        }
    }

    public void parseStationInfo(String passBystation, String station[]){
        int len = passBystation.length();
        int idx = 0;
        char s[] = passBystation.toCharArray();
        String text = "";

        for(int i=0; i<len; i++){
            if(s[i]==' ')
                continue;
            else if(s[i]==','){
                station[idx] = text;
                idx++;
                text = "";
            }else {
                text += s[i];
            }
        }
        totalStation = idx;
        flag++;

        if(flag==2) {
            flag = 0;
            getTransferSubway();
        }
    }

    public void getTransferSubway(){
        String prev = stationID[0].substring(0, 4);
        String now = "";
        int idx = 0;

        transferStation[idx] = start;
        transferStationID[idx++] = prev;
        info.setNextStation(stationName[idx]);
        info.setNextId(stationID[idx]);

        int i;
        for(i=1; i<totalStation; i++){
            now = stationID[i].substring(0, 4);
            if(!prev.equals(now)){
                transferStation[idx] = stationName[i];
                transferStationID[idx++] = now;
                info.setNextStation(stationName[i + 1]);
                info.setBeforeStation(stationName[i - 1]);
                info.setNextId(stationID[i + 1]);
                info.setBeforeId(stationID[i - 1]);
            }
            prev = now;
        }
        info.setBeforeStation(stationName[i - 2]);
        info.setBeforeId(stationID[i - 2]);
        transferStation[idx] = arrive;
        transferStationID[idx++] = now;
        totalTransfer = idx;

        getTravelingTime();
    }

    public void getTravelingTime(){
        String serviceURI = "http://swopenAPI.seoul.go.kr/api/subway/5a6851595968696b373747544e5a79/xml/shortestRoute/0/1/";
        String strUrl;

        for(int i=0; i<totalTransfer-1; i++) {
            strUrl = serviceURI + URLEncoder.encode(transferStation[i]) + "/" + URLEncoder.encode(transferStation[i + 1]);
            new DownloadWebpageTask2().execute(strUrl);
        }
    }

    public void parseTravelMsg(String message, String[] travelTime){
        String time = "";
        for(int i=0; i<message.length(); i++){
            if(message.charAt(i) >= '0' && message.charAt(i) <= '9'){
                time += message.charAt(i);
            }else{
                break;
            }
        }
        travelTime[totalTime++] = time;

        if(totalTime == totalTransfer-1){
            storeInformation();
            nextActivity();
        }
    }

    public void nextActivity(){
        //Intent it = new Intent(this, DisplayPath.class);
        Intent it = new Intent(this, FindTransferCart.class);
        startActivity(it);
        finish();
    }

    private class DownloadWebpageTask2 extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                return (String) downloadUrl((String) urls[0]);
            } catch (IOException e) {
                return "다운로드 실패";
            }
        }

        protected void onPostExecute(String result) {
            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();

                xpp.setInput(new StringReader(result));
                int eventType = xpp.getEventType();
                boolean bSet = false;
                String field = "";
                String shortTravelMsg = "";
                String minTravelMsg = "";

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_DOCUMENT) {
                        ;
                    } else if (eventType == XmlPullParser.START_TAG) {
                        String tag_name = xpp.getName();
                        if (tag_name.equals("shtTravelMsg") || tag_name.equals("minTravelMsg")){
                            field = tag_name;
                            bSet = true;
                        }
                    } else if (eventType == XmlPullParser.TEXT) {
                        if (bSet) {
                            String content = xpp.getText();
                            if(field.equals("shtTravelMsg"))
                                shortTravelMsg = content;
                            else if(field.equals("minTravelMsg")) {
                                minTravelMsg = content;

                                if(pathOption.equals("최단거리")) {
                                    parseTravelMsg(shortTravelMsg, travelTime);
                                }else {
                                    parseTravelMsg(minTravelMsg, travelTime);
                                }
                            }
                            bSet = false;
                        }
                    } else if (eventType == XmlPullParser.END_TAG) {
                        ;
                    }
                    eventType = xpp.next();
                }
            } catch (Exception e) {
                //tv.setText("오류");
            }
        }

        private String downloadUrl(String myurl) throws IOException {

            HttpURLConnection conn = null;
            try{
                URL url = new URL(myurl);
                conn = (HttpURLConnection) url.openConnection();
                BufferedInputStream buf = new BufferedInputStream(conn.getInputStream());
                BufferedReader bufreader = new BufferedReader(new InputStreamReader(buf, "utf-8"));
                String line = null;
                String page = "";
                while((line = bufreader.readLine()) != null) {
                    page += line;
                }

                return page;
            } finally {
                conn.disconnect();
            }
        }
    }

    public void storeInformation() {
        info.setTotalStation(totalStation);
        for (int i = 0; i < totalTransfer; i++) {
            info.setName(transferStation[i]);
            info.setId(transferStationID[i]);
            info.setTravelTime(travelTime[i]);
            info.setPathOption(pathOption);
        }
    }
}