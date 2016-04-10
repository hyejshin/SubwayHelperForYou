package com.example.hyejung.easysubway.apputils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.hyejung.easysubway.DBmodel.AppDataManager;
import com.example.hyejung.easysubway.DBmodel.RealTimeDataModel;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class SiteScanLoader {
    private static final SiteScanLoaderListener NULL_SITE_SCAN_LISTENER = new SiteScanLoaderListener() {
        public void onResSiteScanData(Bundle bundle, boolean toastFlag) {
        }
    };
    private static final SiteScanTimeLoaderListener NULL_SITE_TIME_SCAN_LISTENER = new SiteScanTimeLoaderListener() {
        public void onResSiteScanTimeData(ArrayList<RealTimeDataModel> arrayList) {
        }
    };
    int _scanType = 0;
    ArrayList<RealTimeDataModel> arrTimeList = null;
    Bundle dicAppData = null;
    HttpClient httpclient = null;
    protected Context mContext;
    private final String msgHead = "|SiteScanLoader|";
    protected SiteScanLoaderListener siteScanListener = NULL_SITE_SCAN_LISTENER;
    protected SiteScanTimeLoaderListener siteTimeScanListener = NULL_SITE_TIME_SCAN_LISTENER;
    RealTimeTask timeTask = null;
    boolean toastMsgFlag;

    public interface SiteScanLoaderListener {
        void onResSiteScanData(Bundle bundle, boolean z);
    }

    public interface SiteScanTimeLoaderListener {
        void onResSiteScanTimeData(ArrayList<RealTimeDataModel> arrayList);
    }

    class DownloadingTaskDialog extends AsyncTask<String, Integer, Integer> {
        InputStream is = null;
        boolean isRunning = true;
        ProgressDialog mProgress;
        OutputStream out = null;

        DownloadingTaskDialog() {
            this.mProgress = new ProgressDialog(SiteScanLoader.this.mContext);
        }

        protected void onPreExecute() {
            if (SiteScanLoader.this.toastMsgFlag) {
                this.mProgress = ProgressDialog.show(SiteScanLoader.this.mContext, null, "\uc7a0\uc2dc\ub9cc \uae30\ub2e4\ub824 \uc8fc\uc138\uc694.", true);
            }
        }

        protected Integer doInBackground(String... strData) {
            int statusCode = -1;
            StringBuffer url_content = new StringBuffer();
            String source = "";
            try {
                SiteScanLoader.this.httpclient = new DefaultHttpClient();
                SiteScanLoader.this.httpclient.getParams().setParameter("http.protocol.expect-continue", Boolean.valueOf(false));
                SiteScanLoader.this.httpclient.getParams().setParameter("http.connect.timeout", Integer.valueOf(60000));
                SiteScanLoader.this.httpclient.getParams().setParameter("http.socket.timeout", Integer.valueOf(60000));
                HttpResponse response = SiteScanLoader.this.httpclient.execute(new HttpPost(strData[0]));
                statusCode = response.getStatusLine().getStatusCode();
                Log.i("|SiteScanLoader|statusCode: " + statusCode, "0");
                HttpEntity resEntityGet = response.getEntity();
                if (resEntityGet != null) {
                    InputStreamReader isr = new InputStreamReader(resEntityGet.getContent(), "euc-kr");
                    BufferedReader br = new BufferedReader(isr);
                    String str = "";
                    while (true) {
                        str = br.readLine();
                        if (str == null) {
                            break;
                        }
                        url_content.append(new StringBuilder(String.valueOf(str)).append("\n").toString());
                    }
                    BufferedReader in = new BufferedReader(isr);
                    ArrayList<String> inputLine = new ArrayList();
                    while (in.readLine() != null) {
                        inputLine.add(in.readLine());
                    }
                    String str2 = new String(url_content);
                    try {
                        if (SiteScanLoader.this._scanType == 1) {
                            reqAppDataParser(str2);
                            source = str2;
                        } else {
                            source = str2;
                        }
                    } catch (Exception e5) {
                        source = str2;
                        onExit(5);
                        return Integer.valueOf(statusCode);
                    }
                }
            } catch (UnknownHostException e6) {
            } catch (ClientProtocolException e7) {
            } catch (FileNotFoundException e8) {
            } catch (IOException e9) {
            } catch (Exception e10) {
            }
            return Integer.valueOf(statusCode);
        }

        protected void onCancelled() {
            super.onCancelled();
            SiteScanLoader.this.siteScanListener.onResSiteScanData(null, false);
            this.mProgress.dismiss();
            SiteScanLoader.this.clearDicAppData();
        }

        protected void onPostExecute(Integer result) {
            super.onPostExecute(null);
            SiteScanLoader.this.siteScanListener.onResSiteScanData(SiteScanLoader.this.dicAppData, SiteScanLoader.this.toastMsgFlag);
            this.mProgress.dismiss();
            SiteScanLoader.this.clearDicAppData();
        }

        protected void onProgressUpdate(Integer... values) {
            this.mProgress.setProgress(values[0].intValue());
        }

        protected void onExit(int rValue) {
            this.isRunning = false;
            if (SiteScanLoader.this.httpclient != null) {
                SiteScanLoader.this.httpclient.getConnectionManager().shutdown();
            }
            this.mProgress.dismiss();
            cancel(true);
        }

        protected String _scanString(String total, String start, String end) {
            String res = null;
            int endIndex;
            if (start == null) {
                try {
                    endIndex = total.indexOf(end);
                    if (endIndex > -1) {
                        return total.substring(0, endIndex);
                    }
                    return null;
                } catch (Exception e) {
                    Log.i("|SiteScanLoader|\ubb38\uc790 \uac80\uc0c9 \uc624\ub958","0");
                    return null;
                }
            }
            int startIndex = total.indexOf(start);
            if (startIndex > -1) {
                res = total.substring(startIndex + start.length());
            }
            if (end == null) {
                return res;
            }
            endIndex = res.indexOf(end);
            if (endIndex > -1) {
                return res.substring(0, endIndex);
            }
            return res;
        }

        protected void reqAppDataParser(String reqData) {
            try {
                if (reqData.contains("versionCode") && reqData.length() >= 1) {
                    if (SiteScanLoader.this.dicAppData != null) {
                        SiteScanLoader.this.dicAppData.clear();
                    } else {
                        SiteScanLoader.this.dicAppData = new Bundle();
                    }
                    String appVersion = _scanString(reqData, "versionCode>", "</versionCode");
                    if (appVersion != null) {
                        SiteScanLoader.this.dicAppData.putString("versionCode", appVersion);
                    }
                    String siteTime = _scanString(reqData, "siteTime>", "</siteTime");
                    if (siteTime != null) {
                        SiteScanLoader.this.dicAppData.putString("siteTime", siteTime);
                    }
                }
            } catch (Exception e) {
                SiteScanLoader.this.clearDicAppData();
            }
        }
    }

    class RealTimeTask extends AsyncTask<String, Integer, Integer> {
        boolean isRunning = true;
        ProgressDialog mProgress;

        RealTimeTask() {
            this.mProgress = new ProgressDialog(SiteScanLoader.this.mContext);
        }

        protected void onPreExecute() {
            if (SiteScanLoader.this.toastMsgFlag) {
                this.mProgress = ProgressDialog.show(SiteScanLoader.this.mContext, null, "\uc2e4\uc2dc\uac04\uc815\ubcf4\ub97c \ub85c\ub529\uc911\uc785\ub2c8\ub2e4..", true, true, new OnCancelListener() {
                    public void onCancel(DialogInterface dialog) {
                        SiteScanLoader.this.timeTask.cancel(true);
                    }
                });
            }
        }

        protected Integer doInBackground(String... strData) {
            int statusCode = -1;
            try {
                SiteScanLoader.this.httpclient = new DefaultHttpClient();
                SiteScanLoader.this.httpclient.getParams().setParameter("http.protocol.expect-continue", Boolean.valueOf(false));
                SiteScanLoader.this.httpclient.getParams().setParameter("http.connect.timeout", Integer.valueOf(60000));
                SiteScanLoader.this.httpclient.getParams().setParameter("http.socket.timeout", Integer.valueOf(60000));
                HttpResponse response = SiteScanLoader.this.httpclient.execute(new HttpPost(strData[0]));
                statusCode = response.getStatusLine().getStatusCode();
                Log.i("|SiteScanLoader|statusCode: " + statusCode, "0");
                HttpEntity resEntityGet = response.getEntity();
                if (resEntityGet != null) {
                    Gson gson = new Gson();
                    JsonObject jobj = new JsonParser().parse(EntityUtils.toString(resEntityGet)).getAsJsonObject();
                    ArrayList<RealTimeDataModel> arrayList = (ArrayList) gson.fromJson(jobj.get("resultList"), new TypeToken<List<RealTimeDataModel>>() {
                    }.getType());
                    if (arrayList != null) {
                        reqTiemDataParser(arrayList);
                    }
                }
            } catch (UnknownHostException e) {
                onExit(5);
            } catch (ClientProtocolException e2) {
                onExit(5);
            } catch (FileNotFoundException e3) {
                onExit(5);
            } catch (IOException e4) {
                onExit(5);
            } catch (Exception e5) {
                onExit(5);
            }
            return Integer.valueOf(statusCode);
        }

        protected void onCancelled() {
            super.onCancelled();
            SiteScanLoader.this.siteTimeScanListener.onResSiteScanTimeData(null);
            this.mProgress.dismiss();
            SiteScanLoader.this.clearArrTimeData();
        }

        protected void onPostExecute(Integer result) {
            super.onPostExecute(null);
            SiteScanLoader.this.siteTimeScanListener.onResSiteScanTimeData(SiteScanLoader.this.arrTimeList);
            this.mProgress.dismiss();
            SiteScanLoader.this.clearArrTimeData();
        }

        protected void onProgressUpdate(Integer... values) {
            this.mProgress.setProgress(values[0].intValue());
        }

        protected void onExit(int rValue) {
            this.isRunning = false;
            if (SiteScanLoader.this.httpclient != null) {
                SiteScanLoader.this.httpclient.getConnectionManager().shutdown();
            }
            this.mProgress.dismiss();
            cancel(true);
        }

        protected void reqTiemDataParser(ArrayList<RealTimeDataModel> arrayList) {
            if (arrayList != null) {
                if (SiteScanLoader.this.arrTimeList != null) {
                    SiteScanLoader.this.arrTimeList.clear();
                } else {
                    SiteScanLoader.this.arrTimeList = new ArrayList();
                }
                SiteScanLoader.this.arrTimeList.addAll(arrayList);
            }
        }
    }

    public SiteScanLoader(Context ctx) {
        this.mContext = ctx;
    }

    public void getSiteScanData(boolean toast) {
        clearDicAppData();
        this._scanType = 1;
        this.toastMsgFlag = toast;
    }

    public void clearDicAppData() {
        if (this.dicAppData != null) {
            this.dicAppData.clear();
            this.dicAppData = null;
        }
    }

    public void clearArrTimeData() {
        if (this.arrTimeList != null) {
            this.arrTimeList.clear();
            this.arrTimeList = null;
        }
    }

    public void setSiteScanLoaderListener(SiteScanLoaderListener listener) {
        if (listener == null) {
            this.siteScanListener = NULL_SITE_SCAN_LISTENER;
        } else {
            this.siteScanListener = listener;
        }
    }

    public void setSiteScanTimeLoaderListener(SiteScanTimeLoaderListener listener) {
        if (listener == null) {
            this.siteTimeScanListener = NULL_SITE_TIME_SCAN_LISTENER;
        } else {
            this.siteTimeScanListener = listener;
        }
    }

    public void getSiteScanTimeData(int iStationCode, Boolean toast) {
        clearArrTimeData();
        Bundle bundle = AppDataManager.shared().getArrStationSiteCode(iStationCode);
        if (bundle != null) {
            String subwayId = bundle.getString("seoulLine");
            String url = "http://m.bus.go.kr/mBus/subway/getArvlByInfo.bms?subwayId=" + subwayId + "&statnId=" + bundle.getString("seoulCode");
            this.toastMsgFlag = toast.booleanValue();
            if (this.timeTask == null) {
                this.timeTask = new RealTimeTask();
            }
            this.timeTask.execute(new String[]{url});
        }
    }

    public void cancelSiteScanTimeData() {
        if (this.timeTask != null) {
            this.timeTask.cancel(true);
        }
    }
}
