package com.example.hyejung.easysubway.popupviews.stationdetailview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import com.example.hyejung.easysubway.R;
import com.example.hyejung.easysubway.DBmodel.AppDataManager;
import com.example.hyejung.easysubway.DBmodel.PointDataManager;
import com.example.hyejung.easysubway.popupviews.timetableview.StationTimeTableViewActivity;
import com.example.hyejung.easysubway.subviews.StationNameSubView;
import com.example.hyejung.easysubway.subviews.StationNameSubView.StationNameViewListener;

public class StationDetailViewActivity extends Activity implements OnClickListener, StationNameViewListener {
    AppDataManager appManager = null;
    StationDetailSubView01 detailSubView01 = null;
    StationDetailSubView02 detailSubView02 = null;
    StationDetailSubView03 detailSubView03 = null;
    Bundle dicStationData = null;
    int iCrossing = 0;
    int iExitDoor = 0;
    int iLineCode = 0;
    int iSelectedIndex = 0;
    int iStationCode = 0;
    int iToilet = 0;
    int iWeekCode = 0;
    StationNameSubView stationNameView = null;
    Button tabButton1 = null;
    Button tabButton2 = null;
    Button tabButton3 = null;
    ImageButton weekButton = null;

    protected void onResume() {
        super.onResume();
        int week = this.appManager.getWeekType();
        if (this.iWeekCode != week) {
            setWeeButtonAction(week);
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_detail_view);
        this.appManager = AppDataManager.shared();
        this.dicStationData = new Bundle();
        initMenuBar();
        initTabMenuBar();
        initStationNameView();
    }

    private void initMenuBar() {
        LinearLayout menuBer = (LinearLayout) findViewById(R.id.menuBer);
        View barIndicator = LayoutInflater.from(this).inflate(R.layout.bar_station_detail_menu, null);
        this.weekButton = (ImageButton) barIndicator.findViewById(R.id.navi_menubar_button1);
        this.weekButton.setOnClickListener(this);
        ((ImageButton) barIndicator.findViewById(R.id.navi_menubar_button2)).setOnClickListener(this);
        ((ImageButton) barIndicator.findViewById(R.id.navi_menubar_button4)).setOnClickListener(this);
        ((ImageButton) barIndicator.findViewById(R.id.navi_menubar_button5)).setOnClickListener(this);
        menuBer.addView(barIndicator);
        setWeeButton(this.appManager.getWeekType());
    }

    private void initTabMenuBar() {
        this.tabButton1 = (Button) findViewById(R.id.info_tab_button1);
        this.tabButton1.setOnClickListener(this);
        this.tabButton2 = (Button) findViewById(R.id.info_tab_button2);
        this.tabButton2.setOnClickListener(this);
        this.tabButton3 = (Button) findViewById(R.id.info_tab_button3);
        this.tabButton3.setOnClickListener(this);
    }

    private void initStationNameView() {
        LinearLayout nameView = (LinearLayout) findViewById(R.id.stationNameView);
        this.stationNameView = new StationNameSubView(this);
        this.stationNameView.setStationNameViewListener(this);
        nameView.addView(this.stationNameView);
        setNextStationInfo(PointDataManager.shared().getSelectedData());
    }

    public void setNextStationInfo(Bundle dicData) {
        this.stationNameView.setNextStationInfo(dicData, 0);
    }

    public void onClick(View arg0) {
        int id = arg0.getId();
        if (id == R.id.navi_menubar_button1) {
            cmdMenuBer_Button1();
        } else if (id == R.id.navi_menubar_button2) {
            cmdMenuBer_Button2();
        } else if (id == R.id.navi_menubar_button4) {
            cmdMenuBer_Button4();
        } else if (id == R.id.navi_menubar_button5) {
            cmdMenuBer_Button5();
        } else if (id == R.id.info_tab_button1) {
            setSelectedTabAction(0);
        } else if (id == R.id.info_tab_button2) {
            setSelectedTabAction(1);
        } else if (id == R.id.info_tab_button3) {
            setSelectedTabAction(2);
        }
    }

    private void cmdMenuBer_Button1() {
        setWeeButtonAction(this.appManager.cmdChangeWeekTypeEvent());
    }

    private void cmdMenuBer_Button2() {
        showStationTimeTableViewActivity();
    }

    private void cmdMenuBer_Button4() {
        this.appManager.setFavoritesStation(this.dicStationData);
    }

    private void cmdMenuBer_Button5() {
        finish();
    }

    private void setWeeButtonAction(int week) {
        setWeeButton(week);
        if (this.iSelectedIndex == 0) {
            this.detailSubView01.setStationCode(this.iStationCode, this.iLineCode, this.iWeekCode);
        }
    }

    private void setWeeButton(int week) {
        this.iWeekCode = week;
        if (this.iWeekCode == 1) {
            this.weekButton.setImageResource(R.drawable.img_bar_menu_week2);
        } else if (this.iWeekCode == 2) {
            this.weekButton.setImageResource(R.drawable.img_bar_menu_week3);
        } else {
            this.weekButton.setImageResource(R.drawable.img_bar_menu_week1);
        }
    }

    private void setSelectedTabAction(int index) {
        setSelectedIndex(index);
        if (this.iSelectedIndex == 0) {
            showDetailSubView1();
        } else if (this.iSelectedIndex == 1) {
            showDetailSubView2();
        } else if (this.iSelectedIndex == 2) {
            showDetailSubView3();
        }
    }

    private void setSelectedIndex(int index) {
        this.iSelectedIndex = index;
        if (this.iSelectedIndex == 0) {
            this.tabButton1.setSelected(true);
            this.tabButton2.setSelected(false);
            this.tabButton3.setSelected(false);
        } else if (this.iSelectedIndex == 1) {
            this.tabButton1.setSelected(false);
            this.tabButton2.setSelected(true);
            this.tabButton3.setSelected(false);
        } else if (this.iSelectedIndex == 2) {
            this.tabButton1.setSelected(false);
            this.tabButton2.setSelected(false);
            this.tabButton3.setSelected(true);
        }
    }

    public void onSelectedTabButton(Bundle bundle) {
        this.dicStationData.clear();
        this.dicStationData.putAll(bundle);
        this.iStationCode = Integer.valueOf(bundle.getString("stationCode")).intValue();
        this.iLineCode = Integer.valueOf(bundle.getString("lineCode")).intValue();
        this.iToilet = Integer.valueOf(bundle.getString("toilet")).intValue();
        this.iExitDoor = Integer.valueOf(bundle.getString("exitDoor")).intValue();
        this.iCrossing = Integer.valueOf(bundle.getString("crossing")).intValue();
        setSelectedTabAction(this.iSelectedIndex);
    }

    private void showDetailSubView1() {
        LinearLayout nameView = (LinearLayout) findViewById(R.id.contentView);
        if (this.detailSubView01 == null) {
            this.detailSubView01 = new StationDetailSubView01(this);
        }
        nameView.removeAllViews();
        nameView.addView(this.detailSubView01);
        this.detailSubView01.setStationData(this.dicStationData, this.iWeekCode);
        this.detailSubView01.setStationCode(this.iStationCode, this.iLineCode, this.iWeekCode);
        this.detailSubView01.setStationInfo(this.iToilet, this.iExitDoor, this.iCrossing);
    }

    private void showDetailSubView2() {
        LinearLayout nameView = (LinearLayout) findViewById(R.id.contentView);
        if (this.detailSubView02 == null) {
            this.detailSubView02 = new StationDetailSubView02(this);
        }
        nameView.removeAllViews();
        nameView.addView(this.detailSubView02);
        this.detailSubView02.setStationCode(this.iStationCode);
    }

    private void showDetailSubView3() {
        LinearLayout nameView = (LinearLayout) findViewById(R.id.contentView);
        if (this.detailSubView03 == null) {
            this.detailSubView03 = new StationDetailSubView03(this);
        }
        nameView.removeAllViews();
        nameView.addView(this.detailSubView03);
        this.detailSubView03.setStationCode(this.iStationCode);
    }

    private void showStationTimeTableViewActivity() {
        startActivityForResult(new Intent(this, StationTimeTableViewActivity.class), 200);
    }
}
