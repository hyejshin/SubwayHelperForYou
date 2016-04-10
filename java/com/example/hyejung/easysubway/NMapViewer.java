/*
 * NMapViewer.java $version 2010. 1. 1
 *
 * Copyright 2010 NHN Corp. All rights Reserved.
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.example.hyejung.easysubway;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapCompassManager;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapMyLocationOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;

import java.util.HashMap;
import java.util.Map;

public class NMapViewer extends NMapActivity {
	private static final String LOG_TAG = "NMapViewer";
	private static final boolean DEBUG = false;

	// set your Client ID which is registered for NMapViewer library.
	private static final String CLIENT_ID = "rGAgQHuSBw1BRIDcDgt7";

	private MapContainerView mMapContainerView;

	private NMapView mMapView;
	private NMapController mMapController;

//	private static final NGeoPoint NMAP_LOCATION_DEFAULT = new NGeoPoint(126.978371, 37.5666091);37.529740, 126.984641

//	private static final NGeoPoint NMAP_LOCATION_DEFAULT = new NGeoPoint(127.0632587, 37.493623);

	private static final int NMAP_VIEW_MODE_DEFAULT = NMapView.VIEW_MODE_VECTOR;
	private static final boolean NMAP_TRAFFIC_MODE_DEFAULT = false;
	private static final boolean NMAP_BICYCLE_MODE_DEFAULT = false;

	private static final String KEY_ZOOM_LEVEL = "NMapViewer.zoomLevel";
	private static final String KEY_CENTER_LONGITUDE = "NMapViewer.centerLongitudeE6";
	private static final String KEY_CENTER_LATITUDE = "NMapViewer.centerLatitudeE6";
	private static final String KEY_VIEW_MODE = "NMapViewer.viewMode";
	private static final String KEY_TRAFFIC_MODE = "NMapViewer.trafficMode";
	private static final String KEY_BICYCLE_MODE = "NMapViewer.bicycleMode";

	private SharedPreferences mPreferences;

	private NMapOverlayManager mOverlayManager;

	private NMapMyLocationOverlay mMyLocationOverlay;
	private NMapLocationManager mMapLocationManager;
	private NMapCompassManager mMapCompassManager;

	private NMapViewerResourceProvider mMapViewerResourceProvider;

	private NMapPOIdataOverlay mFloatingPOIdataOverlay;
	private NMapPOIitem mFloatingPOIitem;


	private NGeoPoint map_center_point;
	private NGeoPoint elevator_closet_point;
	private static final int NMAP_ZOOMLEVEL = 13;

	private double map_center_longitude;
	private double map_center_latitude;
	String subwayName;
	String[] subwayLine;
	String[] elevator_longitudes;
	String[] elevator_latitudes;
	int elevator_num, line_num;

	Map lineColor = new HashMap();
	Map lineName = new HashMap();
	String color[] = {"#CCCCCC","#00498B","#009246","#F36630","#00A2D1",
			"#A064A3", "#9E4510", "#5D6519", "#D6406A", "#8E764B"};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		storeIntentValue();
		setContentView(R.layout.map_main);

		setupColor();
		setupLineName();
		setMapLabel();
		setMapView();
	}

	public void storeIntentValue() {
		Intent intent = getIntent();
		subwayName = intent.getStringExtra("subwayName");
		subwayLine = intent.getStringArrayExtra("subwayLine");
		String str_longitude = intent.getStringExtra("longitude");
		String str_latitude = intent.getStringExtra("latitude");
		elevator_longitudes = intent.getStringArrayExtra("elevator_longitude");
		elevator_latitudes = intent.getStringArrayExtra("elevator_latitude");
		elevator_num =  intent.getExtras().getInt("elevator_num");
		line_num =  intent.getExtras().getInt("line_num");

		map_center_longitude = Double.parseDouble(str_longitude);
		map_center_latitude = Double.parseDouble(str_latitude);

		map_center_point = new NGeoPoint(map_center_longitude, map_center_latitude);
	}

	public void setMapLabel(){
		String color;
		color = lineColor.get(subwayLine[0]).toString();
		LinearLayout map_label = (LinearLayout)findViewById(R.id.subwaylayout);
		map_label.setBackgroundColor(Color.parseColor(color));
		LinearLayout symbol_layout = (LinearLayout)findViewById(R.id.subway_symbol_layout);

		ImageView iv[] = new ImageView[line_num];
		LinearLayout layout = new LinearLayout(this);

		for(int i = 0; i < line_num;i++){
			iv[i] = new ImageView(this);
			iv[i].setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

			String strlineName = lineName.get(subwayLine[i]).toString();
			String line_number_image = "@drawable/line" + strlineName;
			int line_numberID = getResources().getIdentifier(line_number_image, "drawable", getPackageName());

			iv[i].setImageResource(line_numberID);
			layout.addView(iv[i]);
		}
		symbol_layout.addView(layout);
		TextView subway_title = (TextView)findViewById(R.id.subwayinfo);
		subway_title.setText(subwayName);
		subway_title.setTextColor(Color.WHITE);
	}

	public void setupColor(){
		lineColor.put("1", "#00498B");
		lineColor.put("2", "#009246");
		lineColor.put("3", "#F36630");
		lineColor.put("4", "#00A2D1");
		lineColor.put("5", "#A064A3");
		lineColor.put("6", "#9E4510");
		lineColor.put("7", "#5D6519");
		lineColor.put("8", "#D6406A");
		lineColor.put("9", "#8E764B");
		lineColor.put("중앙", "#72C7A6");
		lineColor.put("공항철도", "#006D9D");
		lineColor.put("신분당", "#BB1833");
	}

	public void setupLineName(){
		lineName.put("1", "1");
		lineName.put("2", "2");
		lineName.put("3", "3");
		lineName.put("4", "4");
		lineName.put("5", "5");
		lineName.put("6", "6");
		lineName.put("7", "7");
		lineName.put("8", "8");
		lineName.put("9", "9");
		lineName.put("중앙", "joongang");
		lineName.put("공항철도", "gonghang");
		lineName.put("신분당", "newbundang");
	}

	public void setMapView(){
		mMapView = (NMapView)findViewById(R.id.mapView);

		// set a registered API key for Open MapViewer Library
		mMapView.setApiKey(CLIENT_ID);

		// initialize map view
		mMapView.setClickable(true);
		mMapView.setEnabled(true);
		mMapView.setFocusable(true);
		mMapView.setFocusableInTouchMode(true);
		mMapView.requestFocus();

		// register listener for map state changes
		mMapView.setOnMapStateChangeListener(onMapViewStateChangeListener);
		mMapView.setOnMapViewTouchEventListener(onMapViewTouchEventListener);

		// use map controller to zoom in/out, pan and set map center, zoom level etc.
		mMapController = mMapView.getMapController();

		// use built in zoom controls
		NMapView.LayoutParams lp = new NMapView.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, NMapView.LayoutParams.BOTTOM_CENTER);
		mMapView.setBuiltInZoomControls(true, lp);


		// create resource provider
		mMapViewerResourceProvider = new NMapViewerResourceProvider(this);

		// create overlay manager
		mOverlayManager = new NMapOverlayManager(this, mMapView, mMapViewerResourceProvider);

//		testPOIdataOverlay();
// 		openOptionsMenu();

		// location manager
		mMapLocationManager = new NMapLocationManager(this);
		mMapLocationManager.setOnLocationChangeListener(onMyLocationChangeListener);

		// compass manager
		mMapCompassManager = new NMapCompassManager(this);

		// create my location overlay
		mMyLocationOverlay = mOverlayManager.createMyLocationOverlay(mMapLocationManager, mMapCompassManager);

	}

	public void displaySubwayLocation(View v){
		stopMyLocation();
		mMapController.setMapCenter(map_center_point, NMAP_ZOOMLEVEL);
	}

	public void displayMyLocation(View v){
		stopMyLocation();
		startMyLocation();
	}

	/* Test Functions */

	private void startMyLocation() {
//		Toast.makeText(NMapViewer.this, "마이로케이션 내위치에 들어옴! ", Toast.LENGTH_SHORT).show();
		if (mMyLocationOverlay != null) {
			if (!mOverlayManager.hasOverlay(mMyLocationOverlay)) {
				mOverlayManager.addOverlay(mMyLocationOverlay);
			}

			if (mMapLocationManager.isMyLocationEnabled()) {
				if (!mMapView.isAutoRotateEnabled()) {
					mMyLocationOverlay.setCompassHeadingVisible(true);

					mMapCompassManager.enableCompass();

					mMapView.setAutoRotateEnabled(true, false);

					mMapContainerView.requestLayout();
				} else {
					stopMyLocation();
				}

				mMapView.postInvalidate();
			} else {  //Toast.makeText(NMapViewer.this, "start로케이션 else!! ", Toast.LENGTH_SHORT).show();
				boolean isMyLocationEnabled = mMapLocationManager.enableMyLocation(true);

				if (!isMyLocationEnabled) {
					Toast.makeText(NMapViewer.this, "Please enable a My Location source in system settings",
						Toast.LENGTH_LONG).show();

					Intent goToSettings = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					startActivity(goToSettings);

					return;
				}
			}
		}
	}



	private void stopMyLocation() {
		if (mMyLocationOverlay != null) {
			mMapLocationManager.disableMyLocation();

			if (mMapView.isAutoRotateEnabled()) {
				mMyLocationOverlay.setCompassHeadingVisible(false);

				mMapCompassManager.disableCompass();

				mMapView.setAutoRotateEnabled(false, false);

				mMapContainerView.requestLayout();
			}
		}
	}


	/*	private void testPOIdataOverlay() {

		// Markers for POI item
		int markerId = NMapPOIflagType.PIN;

		// set POI data
		NMapPOIdata poiData = new NMapPOIdata(2, mMapViewerResourceProvider);
		poiData.beginPOIdata(2);
//		NMapPOIitem item = poiData.addPOIitem(127.0630205, 37.5091300, "Pizza 777-111", markerId, 0);
		NMapPOIitem item = poiData.addPOIitem(longitude, latitude, subwayName, markerId, 0);
		item.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
///		poiData.addPOIitem(126.972078, 37.544587, "Pizza 123-456", markerId, 0);
		poiData.endPOIdata();

		// create POI data overlay
		NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);

		// set event listener to the overlay
		poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);

		// select an item
		poiDataOverlay.selectPOIitem(0, true);

		// show all POI data
		//poiDataOverlay.showAllPOIdata(0);
	} */
/*
	private void testFloatingPOIdataOverlay() {
		// Markers for POI item
		int marker1 = NMapPOIflagType.PIN;

		// set POI data
		NMapPOIdata poiData = new NMapPOIdata(1, mMapViewerResourceProvider);
		poiData.beginPOIdata(1);
		NMapPOIitem item = poiData.addPOIitem(null, "Touch & Drag to Move", marker1, 0);
		if (item != null) {
			// initialize location to the center of the map view.
			item.setPoint(mMapController.getMapCenter());
			// set floating mode
			item.setFloatingMode(NMapPOIitem.FLOATING_TOUCH | NMapPOIitem.FLOATING_DRAG);
			// show right button on callout
			item.setRightButton(true);

			mFloatingPOIitem = item;
		}
		poiData.endPOIdata();

		// create POI data overlay
		NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
		if (poiDataOverlay != null) {
			poiDataOverlay.setOnFloatingItemChangeListener(onPOIdataFloatingItemChangeListener);

			// set event listener to the overlay
			poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);

			poiDataOverlay.selectPOIitem(0, false);

			mFloatingPOIdataOverlay = poiDataOverlay;
		}
	}
*/
	/* NMapDataProvider Listener */
/*	private final OnDataProviderListener onDataProviderListener = new OnDataProviderListener() {

		@Override
		public void onReverseGeocoderResponse(NMapPlacemark placeMark, NMapError errInfo) {

			if (DEBUG) {
				Log.i(LOG_TAG, "onReverseGeocoderResponse: placeMark="
					+ ((placeMark != null) ? placeMark.toString() : null));
			}

			if (errInfo != null) {
				Log.e(LOG_TAG, "Failed to findPlacemarkAtLocation: error=" + errInfo.toString());

				Toast.makeText(NMapViewer.this, errInfo.toString(), Toast.LENGTH_LONG).show();
				return;
			}

			if (mFloatingPOIitem != null && mFloatingPOIdataOverlay != null) {
				mFloatingPOIdataOverlay.deselectFocusedPOIitem();

				if (placeMark != null) {
					mFloatingPOIitem.setTitle(placeMark.toString());
				}
				mFloatingPOIdataOverlay.selectPOIitemBy(mFloatingPOIitem.getId(), false);
			}
		}

	};
*/

	public void calculateClosetElevator(NGeoPoint myLocation){
		double my_longitude = myLocation.getLongitude(), my_latitude = myLocation.getLatitude();
		double ele_longitude, ele_latitude;
		double closet_longitude = 0.0;
		double closet_latitude = 0.0;
		double distance = 0.0;
		double closet_distance = 999999999.9999999999999;

		for(int i = 0; i < elevator_num; i++) {
			ele_longitude = Double.parseDouble(elevator_longitudes[i]);
			ele_latitude =  Double.parseDouble(elevator_latitudes[i]);

			if(checkEmptyPoint(ele_longitude, ele_latitude) == true)
				continue;

			distance = calDistance(my_latitude, my_longitude, ele_latitude, ele_longitude);
			if(distance < closet_distance){
				closet_distance = distance;
				closet_longitude = ele_longitude;
				closet_latitude = ele_latitude;
			}
		}
		if(closet_longitude == 0 && closet_latitude == 0) {
			Toast.makeText(NMapViewer.this, subwayName + "역은 엘리베이터가 없습니다", Toast.LENGTH_SHORT).show();
			stopMyLocation();
		}
		else
			markClosetElevator(closet_longitude, closet_latitude);
	}

	public boolean checkEmptyPoint(double empty_longitude, double empty_latitude){
		if(empty_latitude == 0 && empty_latitude == 0)
			return true;
		else
			return false;
	}

	public double calDistance(double lat1, double lon1, double lat2, double lon2){

		double theta, dist;
		theta = lon1 - lon2;
		dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1))
				* Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);

		dist = dist * 60 * 1.1515;
		dist = dist * 1.609344;    // 단위 mile 에서 km 변환.
		dist = dist * 1000.0;      // 단위  km 에서 m 로 변환

		return dist;
	}

	private double deg2rad(double deg){
		return (double)(deg * Math.PI / (double)180d);
	}

	// 주어진 라디언(radian) 값을 도(degree) 값으로 변환
	private double rad2deg(double rad){
		return (double)(rad * (double)180d / Math.PI);
	}

	private void markClosetElevator(double closet_longitude, double closet_latitude) {
		// Markers for POI item
		int markerId = NMapPOIflagType.PIN;

		// set POI data
		NMapPOIdata poiData = new NMapPOIdata(1, mMapViewerResourceProvider);
		poiData.beginPOIdata(1);
		NMapPOIitem item = poiData.addPOIitem(closet_longitude, closet_latitude, "현재 위치에서 가장 가까운 "+subwayName+"역 엘리베이터", markerId, 0);
		poiData.endPOIdata();

		// create POI data overlay
		NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);

		// set event listener to the overlay
//			poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);

		// select an item
		poiDataOverlay.selectPOIitem(0, true);

			stopMyLocation();
	}

	/* MyLocation Listener */
	private final NMapLocationManager.OnLocationChangeListener onMyLocationChangeListener = new NMapLocationManager.OnLocationChangeListener() {

		@Override
		public boolean onLocationChanged(NMapLocationManager locationManager, NGeoPoint myLocation) {

			if (mMapController != null) {
				//내 위치로 화면 전환
//				mMapController.animateTo(myLocation);

//				Toast.makeText(NMapViewer.this, "현재 위치로 이동: 위도:" + myLocation.getLatitude() + ",경도 "+ myLocation.getLongitude(), Toast.LENGTH_SHORT).show();
				Toast.makeText(NMapViewer.this, "엘리베이터 위치 찾는 중", Toast.LENGTH_SHORT).show();
				calculateClosetElevator(myLocation);
			}
			return true;
		}

		@Override
		public void onLocationUpdateTimeout(NMapLocationManager locationManager) {

			// stop location updating
			//			Runnable runnable = new Runnable() {
			//				public void run() {
			//					stopMyLocation();
			//				}
			//			};
			//			runnable.run();

//			Toast.makeText(NMapViewer.this, "Your current location is temporarily unavailable.", Toast.LENGTH_LONG).show();
			Toast.makeText(NMapViewer.this, "현재 위치를 찾기 어렵습니다. 다시 시도해주세요.", Toast.LENGTH_LONG).show();
			stopMyLocation();
		}

		@Override
		public void onLocationUnavailableArea(NMapLocationManager locationManager, NGeoPoint myLocation) {

			Toast.makeText(NMapViewer.this, "Your current location is unavailable area.", Toast.LENGTH_LONG).show();

			stopMyLocation();
		}
	};



	/* MapView State Change Listener*/
	private final NMapView.OnMapStateChangeListener onMapViewStateChangeListener = new NMapView.OnMapStateChangeListener() {

		@Override
		public void onMapInitHandler(NMapView mapView, NMapError errorInfo) {

			if (errorInfo == null) { // success
				// restore map view state such as map center position and zoom level.
		//		restoreInstanceState();

				//화면 옮겨서 위치 보여주기!!
				mMapController.setMapCenter(map_center_point, NMAP_ZOOMLEVEL);
			} else { // fail
				Log.e(LOG_TAG, "onFailedToInitializeWithError: " + errorInfo.toString());

				Toast.makeText(NMapViewer.this, errorInfo.toString(), Toast.LENGTH_LONG).show();
			}
		}

		@Override
		public void onAnimationStateChange(NMapView mapView, int animType, int animState) {
			if (DEBUG) {
				Log.i(LOG_TAG, "onAnimationStateChange: animType=" + animType + ", animState=" + animState);
			}
		}

		@Override
		public void onMapCenterChange(NMapView mapView, NGeoPoint center) {
			if (DEBUG) {
				Log.i(LOG_TAG, "onMapCenterChange: center=" + center.toString());
			}
		}

		@Override
		public void onZoomLevelChange(NMapView mapView, int level) {
			if (DEBUG) {
				Log.i(LOG_TAG, "onZoomLevelChange: level=" + level);
			}
		}

		@Override
		public void onMapCenterChangeFine(NMapView mapView) {

		}
	};

	private final NMapView.OnMapViewTouchEventListener onMapViewTouchEventListener = new NMapView.OnMapViewTouchEventListener() {

		@Override
		public void onLongPress(NMapView mapView, MotionEvent ev) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onLongPressCanceled(NMapView mapView) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onSingleTapUp(NMapView mapView, MotionEvent ev) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTouchDown(NMapView mapView, MotionEvent ev) {

		}

		@Override
		public void onScroll(NMapView mapView, MotionEvent e1, MotionEvent e2) {
		}

		@Override
		public void onTouchUp(NMapView mapView, MotionEvent ev) {
			// TODO Auto-generated method stub

		}

	};

	private final NMapView.OnMapViewDelegate onMapViewTouchDelegate = new NMapView.OnMapViewDelegate() {

		@Override
		public boolean isLocationTracking() {
			if (mMapLocationManager != null) {
				if (mMapLocationManager.isMyLocationEnabled()) {
					return mMapLocationManager.isMyLocationFixed();
				}
			}
			return false;
		}

	};


	/* POI data State Change Listener*/
	private final NMapPOIdataOverlay.OnStateChangeListener onPOIdataStateChangeListener = new NMapPOIdataOverlay.OnStateChangeListener() {

		@Override
		public void onCalloutClick(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {
			if (DEBUG) {
				Log.i(LOG_TAG, "onCalloutClick: title=" + item.getTitle());
			}

			// [[TEMP]] handle a click event of the callout
			Toast.makeText(NMapViewer.this, "onCalloutClick: " + item.getTitle(), Toast.LENGTH_LONG).show();
		}

		@Override
		public void onFocusChanged(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {
			if (DEBUG) {
				if (item != null) {
					Log.i(LOG_TAG, "onFocusChanged: " + item.toString());
				} else {
					Log.i(LOG_TAG, "onFocusChanged: ");
				}
			}
		}
	};
	/*
        private final NMapPOIdataOverlay.OnFloatingItemChangeListener onPOIdataFloatingItemChangeListener = new NMapPOIdataOverlay.OnFloatingItemChangeListener() {

            @Override
            public void onPointChanged(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {
                NGeoPoint point = item.getPoint();

                if (DEBUG) {
                    Log.i(LOG_TAG, "onPointChanged: point=" + point.toString());
                }

                findPlacemarkAtLocation(point.longitude, point.latitude);

                item.setTitle(null);

            }
        };

        private final NMapOverlayManager.OnCalloutOverlayListener onCalloutOverlayListener = new NMapOverlayManager.OnCalloutOverlayListener() {

            @Override
            public NMapCalloutOverlay onCreateCalloutOverlay(NMapOverlay itemOverlay, NMapOverlayItem overlayItem,
                Rect itemBounds) {

                // handle overlapped items
                if (itemOverlay instanceof NMapPOIdataOverlay) {
                    NMapPOIdataOverlay poiDataOverlay = (NMapPOIdataOverlay)itemOverlay;

                    // check if it is selected by touch event
                    if (!poiDataOverlay.isFocusedBySelectItem()) {
                        int countOfOverlappedItems = 1;

                        NMapPOIdata poiData = poiDataOverlay.getPOIdata();
                        for (int i = 0; i < poiData.count(); i++) {
                            NMapPOIitem poiItem = poiData.getPOIitem(i);

                            // skip selected item
                            if (poiItem == overlayItem) {
                                continue;
                            }

                            // check if overlapped or not
                            if (Rect.intersects(poiItem.getBoundsInScreen(), overlayItem.getBoundsInScreen())) {
                                countOfOverlappedItems++;
                            }
                        }

                        if (countOfOverlappedItems > 1) {
                            String text = countOfOverlappedItems + " overlapped items for " + overlayItem.getTitle();
                            Toast.makeText(NMapViewer.this, text, Toast.LENGTH_LONG).show();
                            return null;
                        }
                    }
                }

                // use custom old callout overlay
                if (overlayItem instanceof NMapPOIitem) {
                    NMapPOIitem poiItem = (NMapPOIitem)overlayItem;

                    if (poiItem.showRightButton()) {
                        return new NMapCalloutCustomOldOverlay(itemOverlay, overlayItem, itemBounds,
                            mMapViewerResourceProvider);
                    }
                }

                // use custom callout overlay
                return new NMapCalloutCustomOverlay(itemOverlay, overlayItem, itemBounds, mMapViewerResourceProvider);

                // set basic callout overlay
                //return new NMapCalloutBasicOverlay(itemOverlay, overlayItem, itemBounds);
            }

        };

        private final NMapOverlayManager.OnCalloutOverlayViewListener onCalloutOverlayViewListener = new NMapOverlayManager.OnCalloutOverlayViewListener() {

            @Override
            public View onCreateCalloutOverlayView(NMapOverlay itemOverlay, NMapOverlayItem overlayItem, Rect itemBounds) {

                if (overlayItem != null) {
                    // [TEST] 말풍선 오버레이를 뷰로 설정함
                    String title = overlayItem.getTitle();
                    if (title != null && title.length() > 5) {
                        return new NMapCalloutCustomOverlayView(NMapViewer.this, itemOverlay, overlayItem, itemBounds);
                    }
                }

                // null을 반환하면 말풍선 오버레이를 표시하지 않음
                return null;
            }

        };
    */
	/* Local Functions */
	private static boolean mIsMapEnlared = false;
/*
	private void restoreInstanceState() {
		mPreferences = getPreferences(MODE_PRIVATE);

		int longitudeE6 = mPreferences.getInt(KEY_CENTER_LONGITUDE, NMAP_LOCATION_DEFAULT.getLongitudeE6());
		int latitudeE6 = mPreferences.getInt(KEY_CENTER_LATITUDE, NMAP_LOCATION_DEFAULT.getLatitudeE6());
		int level = mPreferences.getInt(KEY_ZOOM_LEVEL, NMAP_ZOOMLEVEL_DEFAULT);
		int viewMode = mPreferences.getInt(KEY_VIEW_MODE, NMAP_VIEW_MODE_DEFAULT);
		boolean trafficMode = mPreferences.getBoolean(KEY_TRAFFIC_MODE, NMAP_TRAFFIC_MODE_DEFAULT);
		boolean bicycleMode = mPreferences.getBoolean(KEY_BICYCLE_MODE, NMAP_BICYCLE_MODE_DEFAULT);

		mMapController.setMapViewMode(viewMode);
		mMapController.setMapViewTrafficMode(trafficMode);
		mMapController.setMapViewBicycleMode(bicycleMode);
//		mMapController.setMapCenter(new NGeoPoint(longitudeE6, latitudeE6), level);

		mMapController.setMapCenter(NMAP_LOCATION_DEFAULT, level);

		if (mIsMapEnlared) {
			mMapView.setScalingFactor(2.0F);
		} else {
			mMapView.setScalingFactor(1.0F);
		}
	}
*/

	/*
	private void saveInstanceState() {
		if (mPreferences == null) {
			return;
		}

		NGeoPoint center = mMapController.getMapCenter();
		int level = mMapController.getZoomLevel();
		int viewMode = mMapController.getMapViewMode();
		boolean trafficMode = mMapController.getMapViewTrafficMode();
		boolean bicycleMode = mMapController.getMapViewBicycleMode();

		SharedPreferences.Editor edit = mPreferences.edit();

		edit.putInt(KEY_CENTER_LONGITUDE, center.getLongitudeE6());
		edit.putInt(KEY_CENTER_LATITUDE, center.getLatitudeE6());
		edit.putInt(KEY_ZOOM_LEVEL, level);
		edit.putInt(KEY_VIEW_MODE, viewMode);
		edit.putBoolean(KEY_TRAFFIC_MODE, trafficMode);
		edit.putBoolean(KEY_BICYCLE_MODE, bicycleMode);

		edit.commit();

	}   */

	/* Menus */
	private static final int MENU_ITEM_CLEAR_MAP = 10;
	private static final int MENU_ITEM_MAP_MODE = 20;
	private static final int MENU_ITEM_MAP_MODE_SUB_VECTOR = MENU_ITEM_MAP_MODE + 1;
	private static final int MENU_ITEM_MAP_MODE_SUB_SATELLITE = MENU_ITEM_MAP_MODE + 2;
	private static final int MENU_ITEM_MAP_MODE_SUB_TRAFFIC = MENU_ITEM_MAP_MODE + 3;
	private static final int MENU_ITEM_MAP_MODE_SUB_BICYCLE = MENU_ITEM_MAP_MODE + 4;
	private static final int MENU_ITEM_ZOOM_CONTROLS = 30;
	private static final int MENU_ITEM_MY_LOCATION = 40;

	private static final int MENU_ITEM_TEST_MODE = 50;
	private static final int MENU_ITEM_TEST_POI_DATA = MENU_ITEM_TEST_MODE + 1;
	private static final int MENU_ITEM_TEST_PATH_DATA = MENU_ITEM_TEST_MODE + 2;
	private static final int MENU_ITEM_TEST_FLOATING_DATA = MENU_ITEM_TEST_MODE + 3;
	private static final int MENU_ITEM_TEST_AUTO_ROTATE = MENU_ITEM_TEST_MODE + 4;
	private static final int MENU_ITEM_TEST_SCALING_FACTOR = MENU_ITEM_TEST_MODE + 5;
	private static final int MENU_ITEM_TEST_NEW_ACTIVITY = MENU_ITEM_TEST_MODE + 7;
	private static final int MENU_ITEM_TEST_VISIBLE_BOUNDS = MENU_ITEM_TEST_MODE + 8;

	/**
	 * Invoked during init to give the Activity a chance to set up its Menu.
	 *
	 * @param menu the Menu to which entries may be added
	 * @return true
	 */


//메뉴바 만들기
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		MenuItem menuItem = null;
		SubMenu subMenu = null;

		menuItem = menu.add(Menu.NONE, MENU_ITEM_CLEAR_MAP, Menu.CATEGORY_SECONDARY, "초기화");
		menuItem.setAlphabeticShortcut('c');
		menuItem.setIcon(android.R.drawable.ic_menu_revert);

		subMenu = menu.addSubMenu(Menu.NONE, MENU_ITEM_MAP_MODE, Menu.CATEGORY_SECONDARY, "지도보기");
		subMenu.setIcon(android.R.drawable.ic_menu_mapmode);

		menuItem = subMenu.add(0, MENU_ITEM_MAP_MODE_SUB_VECTOR, Menu.NONE, "일반지도");
		menuItem.setAlphabeticShortcut('m');
		menuItem.setCheckable(true);
		menuItem.setChecked(false);

		menuItem = subMenu.add(0, MENU_ITEM_MAP_MODE_SUB_SATELLITE, Menu.NONE, "위성지도");
		menuItem.setAlphabeticShortcut('s');
		menuItem.setCheckable(true);
		menuItem.setChecked(false);

		menuItem = subMenu.add(0, MENU_ITEM_MAP_MODE_SUB_TRAFFIC, Menu.NONE, "실시간교통");
		menuItem.setAlphabeticShortcut('t');
		menuItem.setCheckable(true);
		menuItem.setChecked(false);

		menuItem = subMenu.add(0, MENU_ITEM_MAP_MODE_SUB_BICYCLE, Menu.NONE, "자전거지도");
		menuItem.setAlphabeticShortcut('b');
		menuItem.setCheckable(true);
		menuItem.setChecked(false);

		menuItem = menu.add(0, MENU_ITEM_ZOOM_CONTROLS, Menu.CATEGORY_SECONDARY, "Zoom Controls");
		menuItem.setAlphabeticShortcut('z');
		menuItem.setIcon(android.R.drawable.ic_menu_zoom);

		menuItem = menu.add(0, MENU_ITEM_MY_LOCATION, Menu.CATEGORY_SECONDARY, "내위치");
		menuItem.setAlphabeticShortcut('l');
		menuItem.setIcon(android.R.drawable.ic_menu_mylocation);

		subMenu = menu.addSubMenu(Menu.NONE, MENU_ITEM_TEST_MODE, Menu.CATEGORY_SECONDARY, "테스트");
		subMenu.setIcon(android.R.drawable.ic_menu_more);

		menuItem = subMenu.add(0, MENU_ITEM_TEST_NEW_ACTIVITY, Menu.NONE, "New Activity");
		menuItem.setAlphabeticShortcut('n');

		menuItem = subMenu.add(0, MENU_ITEM_TEST_VISIBLE_BOUNDS, Menu.NONE, "Test Visible Bounds");
		menuItem.setAlphabeticShortcut('v');

		menuItem = subMenu.add(0, MENU_ITEM_TEST_POI_DATA, Menu.NONE, "마커 표시");
		menuItem.setAlphabeticShortcut('p');

		menuItem = subMenu.add(0, MENU_ITEM_TEST_PATH_DATA, Menu.NONE, "경로선 표시");
		menuItem.setAlphabeticShortcut('t');

		menuItem = subMenu.add(0, MENU_ITEM_TEST_FLOATING_DATA, Menu.NONE, "직접 지정");
		menuItem.setAlphabeticShortcut('f');

		menuItem = subMenu.add(0, MENU_ITEM_TEST_SCALING_FACTOR, Menu.NONE, "지도 크게보기");
		menuItem.setAlphabeticShortcut('s');

		menuItem = subMenu.add(0, MENU_ITEM_TEST_AUTO_ROTATE, Menu.NONE, "지도 회전");
		menuItem.setAlphabeticShortcut('a');



//		MenuInflater menuInflater = getMenuInflater();   이거 지우기!!!!
//		menuInflater.inflate(R.menu.menubar, menu);

//		getMenuInflater().inflate(R.menu.menubar, menu);
	//	return true;


		return true;
	}



	@Override
	public boolean onPrepareOptionsMenu(Menu pMenu) {
		super.onPrepareOptionsMenu(pMenu);

		int viewMode = mMapController.getMapViewMode();
		boolean isTraffic = mMapController.getMapViewTrafficMode();
		boolean isBicycle = mMapController.getMapViewBicycleMode();

		pMenu.findItem(MENU_ITEM_CLEAR_MAP).setEnabled(
				(viewMode != NMapView.VIEW_MODE_VECTOR) || isTraffic || mOverlayManager.sizeofOverlays() > 0);
		pMenu.findItem(MENU_ITEM_MAP_MODE_SUB_VECTOR).setChecked(viewMode == NMapView.VIEW_MODE_VECTOR);
		pMenu.findItem(MENU_ITEM_MAP_MODE_SUB_SATELLITE).setChecked(viewMode == NMapView.VIEW_MODE_HYBRID);
		pMenu.findItem(MENU_ITEM_MAP_MODE_SUB_TRAFFIC).setChecked(isTraffic);
		pMenu.findItem(MENU_ITEM_MAP_MODE_SUB_BICYCLE).setChecked(isBicycle);

		if (mMyLocationOverlay == null) {
			pMenu.findItem(MENU_ITEM_MY_LOCATION).setEnabled(false);
		}

		return true;
	}

	/**
	 * Invoked when the user selects an item from the Menu.
	 *
	 * @param item the Menu entry which was selected
	 * @return true if the Menu item was legit (and we consumed it), false
	 *         otherwise
	 */

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//
//		switch (item.getItemId()) {
//			case MENU_ITEM_CLEAR_MAP:
//				if (mMyLocationOverlay != null) {
////					stopMyLocation();
//					mOverlayManager.removeOverlay(mMyLocationOverlay);
//				}
//
//				mMapController.setMapViewMode(NMapView.VIEW_MODE_VECTOR);
//				mMapController.setMapViewTrafficMode(false);
//				mMapController.setMapViewBicycleMode(false);
//
//				mOverlayManager.clearOverlays();
//
//				return true;
//
//			case MENU_ITEM_MAP_MODE_SUB_VECTOR:
//				mMapController.setMapViewMode(NMapView.VIEW_MODE_VECTOR);
//				return true;
//
//			case MENU_ITEM_MAP_MODE_SUB_SATELLITE:
//				mMapController.setMapViewMode(NMapView.VIEW_MODE_HYBRID);
//				return true;
//
//			case MENU_ITEM_MAP_MODE_SUB_TRAFFIC:
//				mMapController.setMapViewTrafficMode(!mMapController.getMapViewTrafficMode());
//				return true;
//
//			case MENU_ITEM_MAP_MODE_SUB_BICYCLE:
//				mMapController.setMapViewBicycleMode(!mMapController.getMapViewBicycleMode());
//				return true;
//
//			case MENU_ITEM_ZOOM_CONTROLS:
//				mMapView.displayZoomControls(true);
//				return true;
//
//			case MENU_ITEM_MY_LOCATION:
////				startMyLocation();
//				return true;
//
//			case MENU_ITEM_TEST_POI_DATA:
//				mOverlayManager.clearOverlays();
//
//				// add POI data overlay
////				testPOIdataOverlay();
//				return true;
//
//			case MENU_ITEM_TEST_PATH_DATA:
//				mOverlayManager.clearOverlays();
//
//				// add path data overlay
////				testPathDataOverlay();
//
//				// add path POI data overlay
////				testPathPOIdataOverlay();
//				return true;
//
//			case MENU_ITEM_TEST_FLOATING_DATA:
//				mOverlayManager.clearOverlays();
////				testFloatingPOIdataOverlay();
//				return true;
//
//			case MENU_ITEM_TEST_NEW_ACTIVITY:
////				Intent intent = new Intent(this, FragmentActivity.class);
////				startActivity(intent);
//				return true;
//
//			case MENU_ITEM_TEST_VISIBLE_BOUNDS:
//				// test visible bounds
//				Rect viewFrame = mMapView.getMapController().getViewFrameVisible();
//				mMapController.setBoundsVisible(0, 0, viewFrame.width(), viewFrame.height() - 200);
//
//				// add POI data overlay
//				mOverlayManager.clearOverlays();
//
////				testPathDataOverlay();
//				return true;
//
//			case MENU_ITEM_TEST_SCALING_FACTOR:
//				if (mMapView.getMapProjection().isProjectionScaled()) {
//					if (mMapView.getMapProjection().isMapHD()) {
//						mMapView.setScalingFactor(2.0F, false);
//					} else {
//						mMapView.setScalingFactor(1.0F, false);
//					}
//				} else {
//					mMapView.setScalingFactor(2.0F, true);
//				}
//				mIsMapEnlared = mMapView.getMapProjection().isProjectionScaled();
//				return true;
//
//			case MENU_ITEM_TEST_AUTO_ROTATE:
//				if (mMapView.isAutoRotateEnabled()) {
//					mMapView.setAutoRotateEnabled(false, false);
//
//					mMapContainerView.requestLayout();
//
//					mHnadler.removeCallbacks(mTestAutoRotation);
//				} else {
//
//					mMapView.setAutoRotateEnabled(true, false);
//
//					mMapView.setRotateAngle(30);
//					mHnadler.postDelayed(mTestAutoRotation, AUTO_ROTATE_INTERVAL);
//
//					mMapContainerView.requestLayout();
//				}
//				return true;
//		}
//
//		return super.onOptionsItemSelected(item);
//	}

	private static final long AUTO_ROTATE_INTERVAL = 2000;
	private final Handler mHnadler = new Handler();
	private final Runnable mTestAutoRotation = new Runnable() {
		@Override
		public void run() {
//        	if (mMapView.isAutoRotateEnabled()) {
//    			float degree = (float)Math.random()*360;
//
//    			degree = mMapView.getRoateAngle() + 30;
//
//    			mMapView.setRotateAngle(degree);
//
//            	mHnadler.postDelayed(mTestAutoRotation, AUTO_ROTATE_INTERVAL);
//        	}
		}
	};

	/**
	 * Container view class to rotate map view.
	 */
	private class MapContainerView extends ViewGroup {

		public MapContainerView(Context context) {
			super(context);
		}

		@Override
		protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
			final int width = getWidth();
			final int height = getHeight();
			final int count = getChildCount();
			for (int i = 0; i < count; i++) {
				final View view = getChildAt(i);
				final int childWidth = view.getMeasuredWidth();
				final int childHeight = view.getMeasuredHeight();
				final int childLeft = (width - childWidth) / 2;
				final int childTop = (height - childHeight) / 2;
				view.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
			}

			if (changed) {
				mOverlayManager.onSizeChanged(width, height);
			}
		}

		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			int w = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
			int h = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
			int sizeSpecWidth = widthMeasureSpec;
			int sizeSpecHeight = heightMeasureSpec;

			final int count = getChildCount();
			for (int i = 0; i < count; i++) {
				final View view = getChildAt(i);

				if (view instanceof NMapView) {
					if (mMapView.isAutoRotateEnabled()) {
						int diag = (((int)(Math.sqrt(w * w + h * h)) + 1) / 2 * 2);
						sizeSpecWidth = MeasureSpec.makeMeasureSpec(diag, MeasureSpec.EXACTLY);
						sizeSpecHeight = sizeSpecWidth;
					}
				}

				view.measure(sizeSpecWidth, sizeSpecHeight);
			}
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	}
}
