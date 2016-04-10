package com.example.hyejung.easysubway.subwaymapview.menuview;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.FloatMath;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.example.hyejung.easysubway.R;
import com.example.hyejung.easysubway.DBmodel.AppDataManager;
import com.example.hyejung.easysubway.DBmodel.PointDataManager;
import com.example.hyejung.easysubway.subviews.StationTimeDataSubView;
import com.example.hyejung.easysubway.subviews.StationTimeDataSubView.TouchTimeSelectedListener;
import java.util.ArrayList;

public class SubwaySelectedLayout extends RelativeLayout implements OnClickListener, TouchTimeSelectedListener {
    private static final TouchSelectedListener NULL_TOUCH_SELECTED_LISTENER = new TouchSelectedListener() {
        public void onSelectedMenu(int index) {
        }

        public void onSelectedWeekButton() {
        }

        public void onSelectedTimeButton() {
        }
    };
    AppDataManager appManager = null;
    Boolean bTimeInfoFlag = Boolean.valueOf(false);
    private int buttonCount = 0;
    private ArrayList<SubwaySelectedButton> buttons;
    SubwaySelectedButton closeButton = null;
    private double endAngle = 6.283185307179586d;
    int iTimeType = 0;
    private boolean isOpened = false;
    private int length = 200;
    Context mContext = null;
    private int menuAniCount = 0;
    private Boolean menuAniFlag = Boolean.valueOf(false);
    private Button menuTimeButton = null;
    private Button menuWeekButton = null;
    PointDataManager pointManager = null;
    protected TouchSelectedListener selectedListener = NULL_TOUCH_SELECTED_LISTENER;
    StationTimeDataSubView stationTimeView = null;
    private int sub_duration = 100;
    private int sub_duration2 = 100;
    private int sub_offset = 1;
    private int sub_offset2 = 1;
    private float sub_rotationAngle = 360.0f;
    private float sub_rotationAngle2 = 360.0f;
    private int sub_select_duration = 200;

    public interface TouchSelectedListener {
        void onSelectedMenu(int i);

        void onSelectedTimeButton();

        void onSelectedWeekButton();
    }

    public SubwaySelectedLayout(Context context) {
        super(context);
        this.mContext = context;
        this.appManager = AppDataManager.shared();
        this.pointManager = PointDataManager.shared();
        this.bTimeInfoFlag = this.appManager.getMapTimeInfoFlag();
        this.iTimeType = this.appManager.getMapRealTimeType();
        LayoutInflater.from(context).inflate(R.layout.selected_layout, this);
        RelativeLayout selectedLayout = (RelativeLayout) findViewById(R.id.selectedMenuLayout);
        initMenuButton();
        initStationView();
        setbTimeInfoFlag();
        setVisibility(4);
        selectedLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SubwaySelectedLayout.this.closeSelectedMenu(0);
            }
        });
    }

    private void setbTimeInfoFlag() {
        float screenSize = (float) getContext().getResources().getDisplayMetrics().heightPixels;
        float scale = getContext().getResources().getDisplayMetrics().density;
        if (screenSize / scale > 700.0f) {
            this.length = (int) (130.0f * scale);
        } else {
            if (this.bTimeInfoFlag.booleanValue()) {
                screenSize -= 300.0f * scale;
            } else {
                screenSize = (float) getContext().getResources().getDisplayMetrics().widthPixels;
                if (screenSize / scale > 440.0f) {
                    this.length = (int) (130.0f * scale);
                } else {
                    screenSize -= 30.0f * scale;
                }
            }
            this.length = (int) (screenSize / 3.0f);
        }
        this.buttonCount = this.buttons.size();
        for (int i = 0; i < this.buttonCount; i++) {
            SubwaySelectedButton button = (SubwaySelectedButton) this.buttons.get(i);
            button.setOnClickListener(this);
            button.setEnabled(false);
            button.setMenuIndex(i + 1);
            button.setOffset(((float) this.length) * Math.sin((float) ((this.endAngle * ((double) i)) / ((double) this.buttonCount))), (((float) this.length) * Math.cos((float) ((this.endAngle * ((double) i)) / ((double) this.buttonCount)))) * -1.0f);
        }
        LinearLayout stationLayout = (LinearLayout) findViewById(R.id.stationLayout);
        LinearLayout marginLayout = (LinearLayout) findViewById(R.id.marginLayout);
        if (this.bTimeInfoFlag.booleanValue()) {
            marginLayout.setVisibility(8);
            stationLayout.setVisibility(0);
            return;
        }
        marginLayout.setVisibility(4);
        stationLayout.setVisibility(8);
    }

    private void setWeekButton(int week) {
        switch (week) {
            case 0:
                this.menuWeekButton.setBackgroundResource(R.drawable.img_week_icon01);
                return;
            case 1:
                this.menuWeekButton.setBackgroundResource(R.drawable.img_week_icon02);
                return;
            case 2:
                this.menuWeekButton.setBackgroundResource(R.drawable.img_week_icon03);
                return;
            default:
                return;
        }
    }

    public void setWeekButtonAction(int week) {
        setWeekButton(week);
        this.stationTimeView.setWeekType(week);
    }

    private void setRealTimeButton(int type) {
        if (type == 0) {
            this.menuWeekButton.setVisibility(0);
        } else {
            this.menuWeekButton.setVisibility(4);
        }
    }

    private void initMenuButton() {
        this.buttons = new ArrayList();
        this.buttons.add((SubwaySelectedButton) findViewById(R.id.menu04));
        this.buttons.add((SubwaySelectedButton) findViewById(R.id.menu05));
        this.buttons.add((SubwaySelectedButton) findViewById(R.id.menu07));
        this.buttons.add((SubwaySelectedButton) findViewById(R.id.menu06));
        this.buttons.add((SubwaySelectedButton) findViewById(R.id.menu03));
        this.buttons.add((SubwaySelectedButton) findViewById(R.id.menu01));
        this.closeButton = (SubwaySelectedButton) findViewById(R.id.menu00);
        this.closeButton.setOnClickListener(this);
        this.closeButton.setEnabled(false);
        this.menuWeekButton = (Button) findViewById(R.id.menuWeekButton);
        setWeekButton(this.appManager.getWeekType());
        this.menuWeekButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SubwaySelectedLayout.this.selectedListener.onSelectedWeekButton();
            }
        });
        this.menuTimeButton = (Button) findViewById(R.id.menuTimeButton);
        this.menuTimeButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SubwaySelectedLayout.this.selectedListener.onSelectedTimeButton();
            }
        });
    }

    public void setTouchSelectedListener(TouchSelectedListener listener) {
        if (listener == null) {
            this.selectedListener = NULL_TOUCH_SELECTED_LISTENER;
        } else {
            this.selectedListener = listener;
        }
    }

    public Boolean isMenuOpened() {
        return Boolean.valueOf(this.isOpened);
    }

    public void showSelectedMenu() {
        if (!this.menuAniFlag.booleanValue() && !this.isOpened) {
            this.menuAniFlag = Boolean.valueOf(true);
            setVisibility(0);
            this.isOpened = true;
            this.menuAniCount = 0;
            this.closeButton.setVisibility(0);
            for (int i = 0; i < this.buttonCount; i++) {
                clearMoveAnimationButton(i);
                showMenuAnimation(this.buttons, i);
            }
            if (this.bTimeInfoFlag != this.appManager.getMapTimeInfoFlag()) {
                this.bTimeInfoFlag = this.appManager.getMapTimeInfoFlag();
                setbTimeInfoFlag();
            }
            if (this.bTimeInfoFlag.booleanValue()) {
                showStationTimeView();
                this.menuTimeButton.setVisibility(8);
            } else if (this.appManager.getRealTimeInfoFlag().booleanValue() && !this.bTimeInfoFlag.booleanValue()) {
                if (this.pointManager.checkRealTimeFlag()) {
                    this.menuTimeButton.setVisibility(0);
                } else {
                    this.menuTimeButton.setVisibility(8);
                }
            }
        }
    }

    public void closeSelectedMenu(int index) {
        if (!this.menuAniFlag.booleanValue() && this.isOpened) {
            this.menuAniFlag = Boolean.valueOf(true);
            this.isOpened = false;
            this.menuAniCount = 0;
            if (index > 0) {
                int idx = index - 1;
                startSelectedMenuAnimation(this.buttons, (SubwaySelectedButton) this.buttons.get(idx), idx);
                this.closeButton.setVisibility(4);
            } else {
                setButtonEnabled(Boolean.valueOf(this.isOpened));
                for (int i = 0; i < this.buttonCount; i++) {
                    closeMenuAnimation(this.buttons, i);
                }
            }
            closeStationTimeView();
            this.selectedListener.onSelectedMenu(index);
        }
    }

    public void checkMenuAnimation() {
        if (this.menuAniFlag.booleanValue()) {
            this.menuAniCount++;
            if (this.buttonCount <= this.menuAniCount) {
                if (this.isOpened) {
                    setTimeViewToWeek();
                } else {
                    setVisibility(4);
                }
                setButtonEnabled(Boolean.valueOf(this.isOpened));
                this.menuAniFlag = Boolean.valueOf(false);
            }
        }
    }

    public void setButtonEnabled(Boolean flag) {
        for (int i = 0; i < this.buttonCount; i++) {
            ((SubwaySelectedButton) this.buttons.get(i)).setEnabled(flag.booleanValue());
        }
        this.closeButton.setEnabled(flag.booleanValue());
    }

    public void onClick(View v) {
        SubwaySelectedButton button = (SubwaySelectedButton) v;
        int menuIndex = button.getMenuIndex();
        if (menuIndex == 1 || menuIndex == 6) {
            this.selectedListener.onSelectedMenu(menuIndex);
        } else {
            closeSelectedMenu(button.getMenuIndex());
        }
    }

    private void checkSearchButton(SubwaySelectedButton button) {
        switch (button.getId()) {
            case R.id.menu05 /*2131296412*/:
                switch (this.pointManager.checkPassData()) {
                    case 1:
                        button.setBackgroundResource(R.drawable.img_map_menu_15);
                        return;
                    case 2:
                        button.setBackgroundResource(R.drawable.img_map_menu_25);
                        return;
                    default:
                        button.setBackgroundResource(R.drawable.img_map_menu_05);
                        return;
                }
            case R.id.menu06 /*2131296413*/:
                switch (this.pointManager.checkStartData()) {
                    case 1:
                        button.setBackgroundResource(R.drawable.img_map_menu_16);
                        return;
                    case 2:
                        button.setBackgroundResource(R.drawable.img_map_menu_26);
                        return;
                    default:
                        button.setBackgroundResource(R.drawable.img_map_menu_06);
                        return;
                }
            case R.id.menu07 /*2131296414*/:
                switch (this.pointManager.checkFinishData()) {
                    case 1:
                        button.setBackgroundResource(R.drawable.img_map_menu_17);
                        return;
                    case 2:
                        button.setBackgroundResource(R.drawable.img_map_menu_27);
                        return;
                    default:
                        button.setBackgroundResource(R.drawable.img_map_menu_07);
                        return;
                }
            default:
                return;
        }
    }

    public void showMenuAnimation(ArrayList<SubwaySelectedButton> btns, int index) {
        final SubwaySelectedButton button = (SubwaySelectedButton) btns.get(index);
        checkSearchButton(button);
        float endX = button.getOffsetX();
        float endY = button.getOffsetY();
        AnimationSet animation = new AnimationSet(false);
        Animation rotate = new RotateAnimation(0.0f, this.sub_rotationAngle, 1, 0.5f, 1, 0.5f);
        rotate.setDuration((long) this.sub_duration);
        rotate.setRepeatCount(0);
        Animation scale = new ScaleAnimation(0.8f, 1.0f, 0.8f, 1.0f, 1, 0.5f, 1, 0.5f);
        scale.setDuration((long) this.sub_duration);
        Animation translateAnimation = new TranslateAnimation(0.0f, endX, 0.0f, endY);
        translateAnimation.setDuration((long) this.sub_duration);
        translateAnimation.setStartOffset((long) (this.sub_offset * index));
        animation.setFillAfter(false);
        animation.addAnimation(rotate);
        animation.addAnimation(scale);
        animation.addAnimation(translateAnimation);
        final int i = index;
        animation.setAnimationListener(new AnimationListener() {
            public void onAnimationStart(Animation animation) {
                SubwaySelectedLayout.this.setWeekButton(SubwaySelectedLayout.this.appManager.getWeekType());
                SubwaySelectedLayout.this.setRealTimeButton(SubwaySelectedLayout.this.iTimeType);
                button.setVisibility(0);
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                Handler mHandler = new Handler();
                final int j = i;
                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        SubwaySelectedLayout.this.moveAnimationButton(SubwaySelectedLayout.this.buttons, j, true);
                    }
                }, 10);
                SubwaySelectedLayout.this.checkMenuAnimation();
            }
        });
        button.startAnimation(animation);
    }

    public void closeMenuAnimation(ArrayList<SubwaySelectedButton> btns, int index) {
        final SubwaySelectedButton button = (SubwaySelectedButton) btns.get(index);
        float endX = button.getOffsetX();
        float endY = button.getOffsetY();
        AnimationSet animation = new AnimationSet(false);
        Animation rotate = new RotateAnimation(0.0f, this.sub_rotationAngle2, 1, 0.5f, 1, 0.5f);
        rotate.setDuration((long) this.sub_duration2);
        rotate.setRepeatCount(0);
        Animation scale = new ScaleAnimation(1.0f, 0.8f, 1.0f, 0.8f, 1, 0.5f, 1, 0.5f);
        scale.setDuration((long) this.sub_duration2);
        Animation translateAnimation = new TranslateAnimation(0.0f, -endX, 0.0f, -endY);
        translateAnimation.setDuration((long) this.sub_duration2);
        translateAnimation.setStartOffset((long) (this.sub_offset2 * (btns.size() - (index + 1))));
        animation.setFillAfter(false);
        animation.addAnimation(rotate);
        animation.addAnimation(scale);
        animation.addAnimation(translateAnimation);
        final int i = index;
        animation.setAnimationListener(new AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                Handler mHandler = new Handler();
                final int j = i;
                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        SubwaySelectedLayout.this.moveAnimationButton(SubwaySelectedLayout.this.buttons, j, false);
                    }
                }, 10);
                button.setVisibility(4);
                SubwaySelectedLayout.this.checkMenuAnimation();
            }
        });
        button.startAnimation(animation);
    }

    public void startSelectedMenuAnimation(ArrayList<SubwaySelectedButton> buttons, View mainBtn, int index) {
        this.menuAniCount = 0;
        for (int i = 0; i < this.buttonCount; i++) {
            Animation scale;
            Animation alpha;
            final SubwaySelectedButton view = (SubwaySelectedButton) buttons.get(i);
            AnimationSet animation = new AnimationSet(false);
            if (index == i) {
                scale = new ScaleAnimation(1.0f, 1.3f, 1.0f, 1.3f, 1, 0.5f, 1, 0.5f);
                scale.setDuration((long) this.sub_select_duration);
                alpha = new AlphaAnimation(1.0f, 0.0f);
                alpha.setDuration((long) this.sub_select_duration);
            } 
            else {
                scale = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f, 1, 0.5f, 1, 0.5f);
                scale.setDuration((long) this.sub_select_duration);
                alpha = new AlphaAnimation(1.0f, 0.0f);
                alpha.setDuration((long) this.sub_select_duration);
            }
            animation.addAnimation(scale);
            animation.addAnimation(alpha);
            animation.setAnimationListener(new AnimationListener() {
                public void onAnimationStart(Animation animation) {
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    view.setVisibility(4);
                    SubwaySelectedLayout.this.checkMenuAnimation();
                }
            });
            view.startAnimation(animation);
        }
        this.isOpened = false;
    }

    public void startSelectedMenu(ArrayList<SubwaySelectedButton> arrayList) {
        this.menuAniCount = 0;
        for (int i = 0; i < this.buttonCount; i++) {
            checkMenuAnimation();
        }
        this.isOpened = false;
    }

    private void moveAnimationButton(ArrayList<SubwaySelectedButton> btns, int index, boolean open) {
        SubwaySelectedButton curBtn = (SubwaySelectedButton) btns.get(index);
        int offsetX = (int) curBtn.getOffsetX();
        int offsetY = (int) curBtn.getOffsetY();
        if (open) {
            curBtn.setOldOffsetX(offsetX);
            curBtn.setOldOffsetY(offsetY);
            curBtn.offsetLeftAndRight(offsetX);
            curBtn.offsetTopAndBottom(offsetY);
            return;
        }
        curBtn.setOldOffsetX(-offsetX);
        curBtn.setOldOffsetY(-offsetY);
        curBtn.offsetLeftAndRight(-offsetX);
        curBtn.offsetTopAndBottom(-offsetY);
    }

    private void clearMoveAnimationButton(int index) {
        SubwaySelectedButton curBtn = (SubwaySelectedButton) this.buttons.get(index);
        int offsetX = curBtn.getOldOffsetX();
        if (offsetX != 0) {
            offsetX *= -1;
            curBtn.offsetLeftAndRight(offsetX);
            curBtn.setOldOffsetX(offsetX);
        }
        int offsetY = curBtn.getOldOffsetY();
        if (offsetY != 0) {
            offsetY *= -1;
            curBtn.offsetTopAndBottom(offsetY);
            curBtn.setOldOffsetY(offsetY);
        }
    }

    private void initStationView() {
        LinearLayout stationLayout = (LinearLayout) findViewById(R.id.stationLayout);
        this.stationTimeView = new StationTimeDataSubView(this.mContext);
        this.stationTimeView.setTouchTimeSelectedListener(this);
        stationLayout.addView(this.stationTimeView);
        this.stationTimeView.setVisibility(4);
    }

    public void showStationTimeView() {
        Bundle dicData = PointDataManager.shared().getSelectedData();
        if (dicData != null) {
            this.stationTimeView.showStationTimeView(dicData, 0);
        }
    }

    public void setTimeViewToWeek() {
        if (this.appManager.getMapTimeInfoFlag().booleanValue()) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    SubwaySelectedLayout.this.stationTimeView.setWeekType(SubwaySelectedLayout.this.appManager.getWeekType());
                }
            }, 10);
        }
    }

    public void closeStationTimeView() {
        this.stationTimeView.resetStationTimeData();
    }

    public void onSelectedTimeTabButton(int type) {
        this.iTimeType = type;
        setRealTimeButton(type);
        this.appManager.setMapRealTimeType(type);
        this.stationTimeView.setRealTimeType(type);
    }
}
