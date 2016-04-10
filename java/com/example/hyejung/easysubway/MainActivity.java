package com.example.hyejung.easysubway;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.hyejung.easysubway.DBmodel.AppDataManager;
import com.example.hyejung.easysubway.DBmodel.PointDataManager;
import com.example.hyejung.easysubway.apputils.SiteScanLoader;
import com.example.hyejung.easysubway.apputils.SiteScanLoader.SiteScanLoaderListener;
import com.example.hyejung.easysubway.popupviews.favoriteslistview.FavoritesListActivity;
import com.example.hyejung.easysubway.popupviews.mylocationview.MyLocationListActivity;
import com.example.hyejung.easysubway.popupviews.realtimeview.StationRealTimeViewActivity;
import com.example.hyejung.easysubway.popupviews.stationdetailview.StationDetailViewActivity;
import com.example.hyejung.easysubway.popupviews.subwaylistview.SubwayLineListActivity;
import com.example.hyejung.easysubway.popupviews.timetableview.StationTimeTableViewActivity;
import com.example.hyejung.easysubway.process.SearchDataManager;
import com.example.hyejung.easysubway.process.SearchLocationManager;
import com.example.hyejung.easysubway.process.SearchLocationManager.SearchLocationListener;
import com.example.hyejung.easysubway.subwaymapview.ZoomImageViewScalable;
import com.example.hyejung.easysubway.subwaymapview.ZoomImageViewTouchable.TouchStationListener;
import com.example.hyejung.easysubway.subwaymapview.menuview.SubwayMapMenuLayout;
import com.example.hyejung.easysubway.subwaymapview.menuview.SubwayMapMenuLayout.TouchMapMenuListener;
import com.example.hyejung.easysubway.subwaymapview.menuview.SubwaySelectedLayout;
import com.example.hyejung.easysubway.subwaymapview.menuview.SubwaySelectedLayout.TouchSelectedListener;
import com.example.hyejung.easysubway.R;
import java.util.ArrayList;

public class MainActivity extends Activity implements SiteScanLoaderListener,TouchStationListener, TouchMapMenuListener, TouchSelectedListener {
	private AppDataManager appManager = null;
	private int iAniSpeedWeight = 0;
	private long iEndTime = 0;
	private long iStartTime = 0;
	private Boolean isAniFlag = Boolean.valueOf(false);
	private Boolean isPressedBackButton = Boolean.valueOf(false);
	SearchLocationManager locationManager = null;
	ZoomImageViewScalable mZoomView = null;
	RelativeLayout mainLayout = null;
	RelativeLayout mapImageLayout = null;
	SubwayMapMenuLayout mapMenuLayout = null;
	RelativeLayout mapSubLayout = null;
	private PointDataManager pointManager = null;
	RelativeLayout searchInfoLayout = null;
	private SearchDataManager searchManager = null;
	RelativeLayout selectedLayout = null;
	SubwaySelectedLayout selectedMenuLayout = null;
	private SiteScanLoader siteScanLoader = null;

	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode != -1) {
			return;
		}
		if (requestCode == 100) {
			int resCode = intent.getIntExtra("resFlag", 0);
			if (resCode == 2) {
				this.pointManager.setSelectedData(this.appManager.getArrOenStationData(intent.getStringExtra("stationCode")));
				selectedStation();
			} 
			else if (resCode == 3) {
				//
			}
		} 
		else if (requestCode == 900) {
			this.mapMenuLayout.resetMapQuickButton();
			if (this.appManager.getRefleshFlag().booleanValue()) {
				try {
					reloadAppData();
				} catch (Throwable throwable) {
					throwable.printStackTrace();
				}
				this.appManager.setRefleshFlag(Boolean.valueOf(false));
			}
		} 
		else if (requestCode == 901 && Boolean.valueOf(intent.getBooleanExtra("resetFlag", false)).booleanValue()) {
			
		}
	}

	public void onDestroy() {
		super.onDestroy();

	}

	SharedPreferences setOverlay;
	boolean showOverlay;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setOverlay = PreferenceManager.getDefaultSharedPreferences(this);
//		showOverlay = setOverlay.getBoolean("overlaypref", true);
//		if (showOverlay == true) {
//			showActivityOverlay();
//		}
		setContentView(R.layout.activity_main);

		try {
			initAppData();                //어플리케이션 데이터 설정확인
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
		initMainView();                  //메인메뉴 초기화
		if (this.appManager.checkVersionInfo().booleanValue()) {
			getSiteScanData(Boolean.valueOf(false));
		}
	}


//	private void showActivityOverlay() {
//		final Dialog dialog = new Dialog(this,
//				android.R.style.Theme_Translucent_NoTitleBar);
//		dialog.setContentView(R.layout.overlay_activity);
//		LinearLayout layout = (LinearLayout) dialog.findViewById(R.id.overlay_activity);
//
//		layout.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				dialog.dismiss();
//				SharedPreferences.Editor editor = setOverlay.edit();
//				editor.putBoolean("overlaypref", false);
//				editor.commit();
//			}
//		});
//		dialog.show();
//	}

	private void reloadAppData() throws Throwable {
		this.appManager.resetDBHandler();
		this.searchManager.resetData();
		this.mZoomView.resetMapImage(this);
		this.pointManager.resetStationData();
		this.mapMenuLayout.showResetButton(Boolean.valueOf(false));
	}

	private void initAppData() throws Throwable {
		this.isPressedBackButton = Boolean.valueOf(false);
		this.iStartTime = System.currentTimeMillis();
		this.appManager = AppDataManager.shared();
		this.appManager.initAppDataManager(this);
		this.pointManager = PointDataManager.shared();
		this.pointManager.initPointManager(this);
		this.searchManager = SearchDataManager.shared();
		this.searchManager.initSearchDataManager(this);
	}

	private void initMainView() {
		this.mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);
		this.selectedLayout = (RelativeLayout) findViewById(R.id.selectedLayout);
		this.mapImageLayout = (RelativeLayout) findViewById(R.id.mapImageLayout);
		this.mapSubLayout = (RelativeLayout) findViewById(R.id.mapSubLayout);
		this.mZoomView = new ZoomImageViewScalable(this);
		this.mZoomView.setLayoutParams(new LayoutParams(-1, -1));
		this.mZoomView.setTouchStationListener(this);
		this.mapImageLayout.addView(this.mZoomView);
		this.selectedMenuLayout = new SubwaySelectedLayout(this);
		this.selectedLayout.addView(this.selectedMenuLayout);
		this.selectedMenuLayout.setTouchSelectedListener(this);
		this.mapMenuLayout = new SubwayMapMenuLayout(this);
		this.mapSubLayout.addView(this.mapMenuLayout);
		this.mapMenuLayout.setTouchMapMenuListener(this);
	}

	public void closeSearchInfoLayout() {
		if (this.searchInfoLayout == null) {
			this.searchInfoLayout = (RelativeLayout) findViewById(R.id.searchInfoLayout);
		}
		this.searchInfoLayout.setVisibility(8);
		this.searchInfoLayout.removeAllViews();
		this.mapMenuLayout.showMapMenuButton(Boolean.valueOf(true));
		this.mZoomView.oidZoomLevel();
	}

	public void closeMenuAtcion() {
		this.mZoomView.closeMenuAtcion();
	}

	public void onBackPressed() {
		if (this.selectedMenuLayout.isMenuOpened().booleanValue()) {
			this.selectedMenuLayout.closeSelectedMenu(0);
		} 
		else if (this.mapMenuLayout.isMenuOpened().booleanValue()) {
			this.mapMenuLayout.closeSelectedMenu(0);
		} 
		else {
			this.iEndTime = System.currentTimeMillis();
			if (this.iEndTime - this.iStartTime > 2000) {
				this.isPressedBackButton = Boolean.valueOf(false);
			}
			if (this.isPressedBackButton.booleanValue()) {
				finish();
				System.exit(0);
				Process.killProcess(Process.myPid());
				super.onBackPressed();
				return;
			}
			this.isPressedBackButton = Boolean.valueOf(true);
			this.iStartTime = System.currentTimeMillis();
			Toast.makeText(this, "'\ub4a4\ub85c\uac00\uae30' \ubc84\ud2bc\uc744 \ud55c\ubc88 \ub354 \ub204\ub974\uc2dc\uba74 \uc885\ub8cc\ub429\ub2c8\ub2e4.", 0).show();
		}
	}

	public void onSelectedStation(Bundle bundle) {
		if (bundle != null) {
			this.pointManager.setSelectedData(bundle);
			if (!this.appManager.getMapSearchModeFlag().booleanValue() || this.searchManager.isSearchPathFalg().booleanValue()) {
				
				selectedStation();
				return;
			}
			addSearchPathEvent();
			this.pointManager.clearSelectedData();
			this.mZoomView.mapReDraw();
		}
	}

	Bundle box = new Bundle();
	Bundle box2 = new Bundle();
	String stationname;
	public void selectedStation() {
		new Handler().postDelayed(new Runnable() {
			public void run() {
				MainActivity.this.mZoomView.selectedStationData(MainActivity.this.pointManager.getSelectedData());
				//Log.d("name", stationname);
				MainActivity.this.selectedMenuLayout.showSelectedMenu();
				box=MainActivity.this.pointManager.getSelectedData();
			}
		}, 10);
		this.mZoomView.mapReDraw();
	}

	Intent mapinfo;
	public void onSelectedMenu(int index) {                //해당 역에 대한 클릭 이벤트 메뉴
		switch (index) {
			case 1:
				showStationDetailViewActivity();               //해당 역 상세정보 
				break;

			case 2:
				//setSearchPathEvent(3);
//				mapinfo = new Intent(MainActivity.this, SubwayList.class);
//				stationname = box.getString("stationName");
//				if(stationname.equals("서울역"))
//					stationname = "서울";
//				box2.putString("subwayname", stationname);
//				mapinfo.putExtras(box2);
//				startActivity(mapinfo);
				showStationMap();
				break;

			case 3:
				setSearchPathEvent(2);
				break;
			case 4:
				setSearchPathEvent(1);
				break;
			case 5:
				setFavoritesStation();                               ///즐겨찾기 추가->해당역 추가 
				break;
			case 6:
				showStationTimeTableViewActivity();       //해당 역에 대한 전철 시각표 
				break;
		}
		if (index != 1 && index != 6) {
			closeMenuAtcion();
			this.pointManager.clearSelectedData();
		}
	}

	public void showStationMap(){
		mapinfo = new Intent(MainActivity.this, SubwayList.class);
		stationname = box.getString("stationName");
		if(stationname.equals("서울역"))
			stationname = "서울";
		box2.putString("subwayname", stationname);
		mapinfo.putExtras(box2);
		startActivity(mapinfo);
	}

	public void onSelectedWeekButton() {
		this.selectedMenuLayout.setWeekButtonAction(this.appManager.cmdChangeWeekTypeEvent());
	}

	public void onSelectedTimeButton() {
		if (!this.pointManager.checkRealTimeFlag()) {
			Toast.makeText(this, "\ub3c4\ucc29\uc815\ubcf4\uac00 \uc81c\uacf5\ud558\uc9c0 \uc54a\ub294 \uc5ed\uc785\ub2c8\ub2e4.", 0).show();
		}
		else if (!this.appManager.getMapTimeInfoFlag().booleanValue()) {
			onSelectedRealTimeButton();
		}
	}

	public void onSelectedRealTimeButton() {                          //실시간 정보 
		startActivityForResult(new Intent(this, StationRealTimeViewActivity.class), 300);
	}

	private void setFavoritesStation() {                                     //해당 역을 즐겨찾기에 setting
		this.appManager.setFavoritesStation(this.pointManager.getSelectedData());
	}

	private void setFavoritesPath() {                                        //해당 경로를 즐겨찾기에 setting
		this.appManager.setFavoritesPath(this.pointManager.getSelectedPoint());
	}

	private void setSearchPathEvent(int type) {
		this.pointManager.setSelectedStation(type);
		if (this.pointManager.getStartData() == null && this.pointManager.getFinishData() == null && this.pointManager.getPassData() == null) {
			this.mapMenuLayout.showResetButton(Boolean.valueOf(false));
			this.mapMenuLayout.showSelectedPointView(Boolean.valueOf(false));
			return;
		}
		
		this.mapMenuLayout.showResetButton(Boolean.valueOf(true));
		this.mapMenuLayout.showSelectedPointView(Boolean.valueOf(true));
	}

	//출발역과 도착지역 역코드 받아오는 function
	public void cmdStartSearchPathEvent(Boolean history) {
        Bundle bundle = this.pointManager.getSelectedPoint();
        //bundle.putInt("areaCode", this.appManager.getAreaCode());
        if(history) {
			String stationName1 = bundle.getString("startName");
			String stationName2 = bundle.getString("finishName");
			if(stationName1.equals("서울역"))
				stationName1="서울";

			Bundle transfer = new Bundle();
			Intent intent = new Intent(MainActivity.this, TransferActivity.class);
			transfer.putString("start", stationName1 );
			transfer.putString("finish", stationName2);
			intent.putExtras(transfer);
			startActivity(intent);
        }
        
        //this.searchManager.startSearchPathEvent(bundle, history, Boolean.valueOf(true));
    }


	private void addSearchPathEvent() {
		if (this.pointManager.checkSelectedData().booleanValue()) {
			Toast.makeText(this, "\uc774\ubbf8 \uc120\ud0dd\ub41c \uc5ed\uc785\ub2c8\ub2e4.", 0).show();
		} 
		else if (this.pointManager.getStartData() == null) {
			setSearchPathEvent(1);
		} 
		else if (this.pointManager.getFinishData() == null) {
			setSearchPathEvent(2);
		}
	}

	public void onSelectedMapMenu(int index) {    //전체화면에서 보여지는 옵션메뉴
		switch (index) {
			case 1:
				showSubwayLineListActivity();             
				return;
			case 2:
				showMyLocationListActivity();
				return;
			case 3:
				showFavoritesListActivity();
				return;
			case 8:
				showAppConfigurationActivity();
				return;
			default:
				return;
		}
	}

	public void onSelectedMapButton(int index) {
		switch (index) {
			case 100:
				onMapQuickButtonAction();                  //모드 버튼 메뉴
				return;
			case 101:
				onResetButtonAction();                         
				return;
			default:
				return;
		}
	}

	public void onSelectedPointButton(int index) {
		Bundle bundle = null;
		if (index == 1) {
			bundle = new Bundle();
			bundle.putAll(this.pointManager.getStartData());
		}
		else if (index == 2) {
			bundle = new Bundle();
			bundle.putAll(this.pointManager.getFinishData());
		}
		else if (index == 3) {
			bundle = new Bundle();
			bundle.putAll(this.pointManager.getPassData());
		}
		if (bundle != null) {
			this.pointManager.setSelectedData(bundle);
			selectedStation();
		}
	}

	private void onResetButtonAction() {
		this.pointManager.resetStationData();
		this.mZoomView.mapReDraw();
		this.mapMenuLayout.showResetButton(Boolean.valueOf(false));
	}

	private void onClearSearchDataAction() {
		if (this.searchManager.isSearchPathFalg().booleanValue()) {
			closeSearchInfoLayout();
			this.searchManager.clearData();
			onResetButtonAction();
		}
	}

	private void myLocationUpdates() {
		if (this.locationManager == null) {
			this.locationManager = new SearchLocationManager();
			this.locationManager.initLocation(this);
		}
		this.locationManager.setSearchLocationListener(new SearchLocationListener() {
			public void onSearchLocationListener(ArrayList<Bundle> resData) {
				if (resData == null || resData.size() <= 0) {
					MainActivity.this.appManager.showToastMsg("\uc8fc\ubcc0\uc5d0 \uac00\uae4c\uc6b4 \uc5ed\uc774 \uc5c6\uc2b5\ub2c8\ub2e4.");
				} else {
					Bundle bundle = MainActivity.this.appManager.getArrOenStationData(((Bundle) resData.get(0)).getString("stationCode"));
					if (bundle != null) {
						MainActivity.this.pointManager.setSelectedData(bundle);
						MainActivity.this.selectedStation();
					}
				}
			}
		});
		this.locationManager.searchLocationUpdates(1);
	}

	private void onMapQuickButtonAction() {
		switch (this.appManager.getUserMenuType01()) {
			case 0:
				myLocationUpdates();
				return;
			case 1:
				showSubwayLineListActivity();
				return;
			case 2:
				showFavoritesListActivity();
				return;
			case 4:
				showAppConfigurationActivity();
				return;
			default:
				return;
		}
	}

	private void showSubwayLineListActivity() {
		startActivityForResult(new Intent(this, SubwayLineListActivity.class), 100);
	}

	private void showMyLocationListActivity() {
		startActivityForResult(new Intent(this, MyLocationListActivity.class), 100);
	}

	private void showFavoritesListActivity() {
		startActivityForResult(new Intent(this, FavoritesListActivity.class), 100);
	}
	
	private void showAppConfigurationActivity() {
		startActivityForResult(new Intent(this, SettingPage.class), 900);
	}
	
	private void showStationDetailViewActivity() {
		startActivityForResult(new Intent(this, StationDetailViewActivity.class), 200);
	}

	private void showStationTimeTableViewActivity() {
		startActivityForResult(new Intent(this, StationTimeTableViewActivity.class), 200);
	}

	public void onStartSearchPathEvent(boolean bFalg) {
		if (bFalg) {
			cmdStartSearchPathEvent(Boolean.valueOf(true));
		}
		else if (this.searchManager.isSearchPathFalg().booleanValue()) {
			closeSearchInfoLayout();
			this.searchManager.clearData();
			this.mapMenuLayout.showSelectedPointView(Boolean.valueOf(true));
		}
	}

	private void _searchTimeTypeToData(int type) {
		Bundle bundle = this.pointManager.getSelectedPoint();
		bundle.putInt("areaCode", this.appManager.getAreaCode());
		if (type == 1) {
			this.searchManager.startSearchPathEvent(bundle, Boolean.valueOf(false), Boolean.valueOf(false));
		} else if (this.searchManager.isSearchPathFalg().booleanValue()) {
			this.searchManager.reStartSearchInfoDataProc();
		} else {
			this.searchManager.getNewNowTimeData();
			this.searchManager.startSearchPathEvent(bundle, Boolean.valueOf(true), Boolean.valueOf(true));
		}
	}
		public void onStartSearchTime() {
		_searchTimeTypeToData(1);
	}

	/*실시간 정보 업데이트 -> 실시간 정보 site 초기화
	 * 실시간 시간 정보 삭제. 얻기
	*/
	public void getSiteScanData(Boolean flag) {
		initSiteScanLoader();
	}

	private void initSiteScanLoader() {
		if (this.siteScanLoader == null) {
			this.siteScanLoader = new SiteScanLoader(this);
			this.siteScanLoader.setSiteScanLoaderListener(this);
		}
	}

	private void removeSiteScanLoader() {
		if (this.siteScanLoader != null) {
			this.siteScanLoader.clearDicAppData();
			this.siteScanLoader.setSiteScanLoaderListener(null);
			this.siteScanLoader = null;
		}
	}

	public void onResSiteScanData(Bundle bundle, boolean toastFlag) {
		if (bundle != null) {
			int appCode;
			if (bundle.getString("siteTime") != null) {
				this.appManager.setRealTimeInfoFlag(Boolean.valueOf(Boolean.parseBoolean(bundle.getString("siteTime"))));
			}
			int newCode = 0;
			if (toastFlag) {
				appCode = this.appManager.getAppVersionCode();
			} else {
				appCode = this.appManager.getAppDataFileCode();
			}
			if (bundle.getString("versionCode") != null) {
				newCode = Integer.parseInt(bundle.getString("versionCode"));
			}
			if (newCode > appCode) {
				this.appManager.setAppDataFileCode(newCode);
			} 
			else if (toastFlag) {
				Toast.makeText(this, "\ucd5c\uc2e0 \uc571\ubc84\uc804 \uc0ac\uc6a9\uc911\uc785\ub2c8\ub2e4.", 0).show();
			}
		}
		this.appManager.checkVersionInfo();
		removeSiteScanLoader();
	}
}
