package com.example.hyejung.easysubway.process;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import com.example.hyejung.easysubway.DBmodel.AppDataManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import com.example.hyejung.easysubway.R;

public class SearchLocationManager {
    private static final SearchLocationListener NULL_SEARCH_LOCATION_LISTENER = new SearchLocationListener() {
        public void onSearchLocationListener(ArrayList<Bundle> arrayList) {
        }
    };
    private static final Comparator<Bundle> myComparator = new Comparator<Bundle>() {
        public int compare(Bundle arg0, Bundle arg1) {
            if (arg0.getFloat("distance") < arg1.getFloat("distance")) {
                return -1;
            }
            return arg0.getFloat("distance") > arg1.getFloat("distance") ? 1 : 0;
        }
    };
    protected SearchLocationListener locationListener = NULL_SEARCH_LOCATION_LISTENER;
    private Context mContext = null;
    private ProgressDialog mProgress = null;
    private LocationManager manager = null;
    private MyLocationListener mylistener = null;
    private int resCount = 0;
    private Bundle object;

    public interface SearchLocationListener {
        void onSearchLocationListener(ArrayList<Bundle> arrayList);
    }

    class MyLocationListener implements LocationListener {
        MyLocationListener() {
        }

        public void onLocationChanged(Location location) {
            ArrayList<Bundle> arrData = SearchLocationManager.this.getAroundStationData(location, Boolean.valueOf(true), SearchLocationManager.this.resCount);
            SearchLocationManager.this.manager.removeUpdates(SearchLocationManager.this.mylistener);
            SearchLocationManager.this.closeProgressDialog();
            if (SearchLocationManager.this.locationListener != SearchLocationManager.NULL_SEARCH_LOCATION_LISTENER) {
                SearchLocationManager.this.locationListener.onSearchLocationListener(arrData);
            }
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    public void initLocation(Context context) {
        this.mContext = context;
        if (this.manager == null) {
            this.manager = (LocationManager) this.mContext.getSystemService("location");
        }
        if (this.mylistener == null) {
            this.mylistener = new MyLocationListener();
        }
    }

    public void searchLocationUpdates(int count) {
        this.resCount = count;
        this.manager.requestLocationUpdates("network", 1000, 10.0f, this.mylistener);
        this.manager.requestLocationUpdates("gps", 1000, 10.0f, this.mylistener);
        showProgressDialog();
    }

    public ArrayList<Bundle> getAroundStationData(Location location, Boolean group, int count) {
        ArrayList<Bundle> arr = AppDataManager.shared().getArrStationLocation((float) location.getLatitude(), (float) location.getLongitude(), group);
        ArrayList<Bundle> arrList = new ArrayList();
        ArrayList<Bundle> arrAroundList = new ArrayList();
        Iterator it = arr.iterator();
        while (it.hasNext()) {
            Bundle dic1 = (Bundle) it.next();
            Location loc = new Location("point");
            loc.setLatitude(Double.valueOf(dic1.getString("latitude")).doubleValue());
            loc.setLongitude(Double.valueOf(dic1.getString("longitude")).doubleValue());
            float distance = location.distanceTo(loc);
            Bundle object = new Bundle();
            object.putAll(dic1);
            object.putFloat("distance", distance);
            arrList.add(object);
        }
        Collections.sort(arrList, myComparator);
        int i = 0;
        it = arrList.iterator();
        while (it.hasNext()) {
            Bundle dic2 = (Bundle) it.next();
            if (i < count) {
                String strData = null;
                float fLocation = dic2.getFloat("distance");
                if (fLocation < 10000.0f) {
                    if (fLocation >= 1000.0f) {
                        strData = String.format("%.01fkm", new Object[]{Float.valueOf(fLocation * 0.001f)});
                    } else {
                        strData = String.format("%.0fm", new Object[]{Float.valueOf(fLocation)});
                    }
                }
                object = new Bundle();
                object.putAll(dic2);
                object.putString("distance", strData);
                arrAroundList.add(object);
            }
            i++;
        }
        arrList.clear();
        return arrAroundList;
    }

    private void showProgressDialog() {
        if (this.mProgress == null) {
            this.mProgress = new ProgressDialog(this.mContext);
        }
        this.mProgress = ProgressDialog.show(this.mContext, null, "\uc7a0\uc2dc\ub9cc \uae30\ub2e4\ub824 \uc8fc\uc138\uc694.", true);
        this.mProgress.setCancelable(true);
        this.mProgress.setOnCancelListener(new OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                if (SearchLocationManager.this.manager != null) {
                    SearchLocationManager.this.manager.removeUpdates(SearchLocationManager.this.mylistener);
                    SearchLocationManager.this.closeProgressDialog();
                }
            }
        });
    }

    private void closeProgressDialog() {
        if (this.mProgress != null) {
            this.mProgress.dismiss();
        }
    }

    public void setSearchLocationListener(SearchLocationListener listener) {
        if (listener == null) {
            this.locationListener = NULL_SEARCH_LOCATION_LISTENER;
        } else {
            this.locationListener = listener;
        }
    }
}
