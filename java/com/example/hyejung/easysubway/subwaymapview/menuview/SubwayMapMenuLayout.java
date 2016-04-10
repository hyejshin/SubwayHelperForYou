package com.example.hyejung.easysubway.subwaymapview.menuview;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.example.hyejung.easysubway.R;
import com.example.hyejung.easysubway.DBmodel.AppDataManager;
import com.example.hyejung.easysubway.DBmodel.PointDataManager;
//import com.example.hyejung.easysubway.process.SearchDataManager;
import java.util.ArrayList;

/*전체 지도 화면에서 지하철 모양 버튼 클릭-> 나오는 메뉴
 * 각 메뉴 클릭시 발생하는 이벤트
 * 메뉴 믈릭시 구현되는 애니메이션 구현
 */
public class SubwayMapMenuLayout extends RelativeLayout implements OnClickListener {
    private static final TouchMapMenuListener NULL_TOUCH_MAPMENU_LISTENER = new TouchMapMenuListener() {
        public void onSelectedMapMenu(int index) {
        }

        public void onSelectedMapButton(int index) {
        }

        public void onSelectedPointButton(int index) {
        }
    };
    private static final int duration = 200;
    AppDataManager appManager = null;
    private int buttonCount = 0;
    private ArrayList<SubwayMapButton> buttons;
    private Button finishPointButton = null;
    private boolean isMenuOpened = false;
    private boolean isModeFlag = false;
    private int length = 300;
    Context mContext = null;
    private Button mapMenuButton = null;
    private Button mapModeButton = null;
    private Button mapQuickButton = null;
    private Button mapResetButton = null;
    private int menuAniCount = 0;
    private Boolean menuAniFlag = Boolean.valueOf(false);
    private RelativeLayout menuListLayout;
    private Button passPointButton = null;
    private ImageView passPointImage = null;
    private LinearLayout selectedDataView = null;
    protected TouchMapMenuListener selectedListener = NULL_TOUCH_MAPMENU_LISTENER;
    private Button startPointButton = null;
    private int sub_duration = duration;
    private int sub_offset = 40;
    private int sub_select_duration = 180;

    public interface TouchMapMenuListener {
        void onSelectedMapButton(int i);

        void onSelectedMapMenu(int i);

        void onSelectedPointButton(int i);
    }

    public SubwayMapMenuLayout(Context context) {
        super(context);
        this.mContext = context;
        this.appManager = AppDataManager.shared();
        LayoutInflater.from(context).inflate(R.layout.map_menu_layout, this);
        this.menuListLayout = (RelativeLayout) findViewById(R.id.menuListLayout);
        this.menuListLayout.setVisibility(4);
        this.menuListLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SubwayMapMenuLayout.this.startMenuAnimation(Boolean.valueOf(false));
            }
        });
        initMenuButton();
        initPointView();
    }

    public void resetMapQuickButton() {
        if (this.appManager.getUserMenuType01() < 5) {
            this.mapQuickButton.setVisibility(0);
        } else {
            this.mapQuickButton.setVisibility(4);
        }
        switch (this.appManager.getUserMenuType01()) {
            case 0:
                this.mapQuickButton.setBackgroundResource(R.drawable.img_usermenu_icon01);
                return;
            case 1:
                this.mapQuickButton.setBackgroundResource(R.drawable.img_usermenu_icon03);
                return;
            case 2:
                this.mapQuickButton.setBackgroundResource(R.drawable.img_usermenu_icon02);
                return;
            case 3:
                this.mapQuickButton.setBackgroundResource(R.drawable.img_usermenu_icon06);
                return;
            default:
                return;
        }
    }

    private void initMenuButton() {
        this.mapQuickButton = (Button) findViewById(R.id.mapQuickButton);
        resetMapQuickButton();
        this.mapQuickButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SubwayMapMenuLayout.this.selectedListener.onSelectedMapButton(100);
            }
        });
        this.mapMenuButton = (Button) findViewById(R.id.mapMenuButton);
        this.mapMenuButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	SubwayMapMenuLayout.this.startMenuAnimation(Boolean.valueOf(!SubwayMapMenuLayout.this.isMenuOpened));
             
            }
        });
        this.mapModeButton = (Button) findViewById(R.id.mapModeButton);
        startModeButtonAnimation(this.appManager.getMapSearchModeFlag(), Boolean.valueOf(false));
        this.mapModeButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                boolean z;
                SubwayMapMenuLayout subwayMapMenuLayout = SubwayMapMenuLayout.this;
                if (SubwayMapMenuLayout.this.isModeFlag) {
                    z = false;
                } else {
                    z = true;
                }
                subwayMapMenuLayout.startModeButtonAnimation(Boolean.valueOf(z), Boolean.valueOf(true));
            }
        });
        this.mapResetButton = (Button) findViewById(R.id.mapResetButton);
        this.mapResetButton.setVisibility(4);
        this.mapResetButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SubwayMapMenuLayout.this.selectedListener.onSelectedMapButton(101);
            }
        });
        this.buttons = new ArrayList();
        SubwayMapButton button = (SubwayMapButton) findViewById(R.id.mapMenu08);
        button.setMenuIndex(8);
        this.buttons.add(button);
        button = (SubwayMapButton) findViewById(R.id.mapMenu03);
        button.setMenuIndex(3);
        this.buttons.add(button);
        button = (SubwayMapButton) findViewById(R.id.mapMenu02);
        button.setMenuIndex(2);
        this.buttons.add(button);
        button = (SubwayMapButton) findViewById(R.id.mapMenu01);
        button.setMenuIndex(1);
        this.buttons.add(button);
        this.length = (int) (((float) this.length) * getContext().getResources().getDisplayMetrics().density);
        this.buttonCount = this.buttons.size();
        for (int i = 0; i < this.buttonCount; i++) {
            button = (SubwayMapButton) this.buttons.get(i);
            button.setOnClickListener(this);
            button.setEnabled(false);
            button.setButtonIndex(i + 1);
            button.setOffsetX((float) this.length);
            button.setOldOffsetX(this.length);
        }
    }

    private void initPointView() {
        this.selectedDataView = (LinearLayout) findViewById(R.id.selectedDataView);
        this.selectedDataView.setVisibility(8);
        this.startPointButton = (Button) findViewById(R.id.startPointButton);
        this.startPointButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SubwayMapMenuLayout.this.selectedListener.onSelectedPointButton(1);
            }
        });
        this.finishPointButton = (Button) findViewById(R.id.finishPointButton);
        this.finishPointButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SubwayMapMenuLayout.this.selectedListener.onSelectedPointButton(2);
            }
        });
        this.passPointButton = (Button) findViewById(R.id.passPointButton);
        this.passPointButton.setVisibility(8);
        this.passPointButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SubwayMapMenuLayout.this.selectedListener.onSelectedPointButton(3);
            }
        });
        this.passPointImage = (ImageView) findViewById(R.id.passPointImage);
        this.passPointImage.setVisibility(8);
    }

    public void showResetButton(Boolean flag) {
        if (flag.booleanValue()) {
            this.mapResetButton.setVisibility(0);
            return;
        }
        this.mapResetButton.setVisibility(8);
        showSelectedPointView(flag);
    }

    public void showSelectedPointView(Boolean flag) {
        if (flag.booleanValue()) {
            Bundle bundle = PointDataManager.shared().getStartData();
            if (bundle != null) {
                this.startPointButton.setText(bundle.getString("stationName"));
                this.startPointButton.setVisibility(0);
            } 
            else {
                this.startPointButton.setText("");
                this.startPointButton.setVisibility(4);
            }
            bundle = PointDataManager.shared().getFinishData();
            if (bundle != null) {
                this.finishPointButton.setText(bundle.getString("stationName"));
                this.finishPointButton.setVisibility(0);
            } 
            else {
                this.finishPointButton.setText("");
                this.finishPointButton.setVisibility(4);
            }
            bundle = PointDataManager.shared().getPassData();
            if (bundle != null) {
                this.passPointButton.setText(bundle.getString("stationName"));
                this.passPointButton.setVisibility(0);
                this.passPointImage.setVisibility(0);
            } 
            else {
                this.passPointButton.setText("");
                this.passPointButton.setVisibility(8);
                this.passPointImage.setVisibility(8);
            }
            this.selectedDataView.setVisibility(0);
            return;
        }
        clearSelectedPointView();
    }

    public void clearSelectedPointView() {
        this.selectedDataView.setVisibility(8);
        this.startPointButton.setText("");
        this.finishPointButton.setText("");
        this.passPointButton.setText("");
        this.startPointButton.setVisibility(4);
        this.finishPointButton.setVisibility(4);
        this.passPointButton.setVisibility(8);
        this.passPointImage.setVisibility(8);
    }

    public void showMapMenuButton(Boolean flag) {
        if (flag.booleanValue()) {
            this.mapMenuButton.setVisibility(0);
            this.mapModeButton.setVisibility(0);
            if (this.appManager.getUserMenuType01() < 5) {
                this.mapQuickButton.setVisibility(0);
                return;
            } else {
                this.mapQuickButton.setVisibility(8);
                return;
            }
        }
        this.mapMenuButton.setVisibility(8);
        this.mapModeButton.setVisibility(8);
        this.mapQuickButton.setVisibility(8);
        this.mapResetButton.setVisibility(8);
        this.selectedDataView.setVisibility(8);
    }

    private void startModeButtonAnimation(Boolean mode, Boolean bToast) {
        this.isModeFlag = mode.booleanValue();
        this.appManager.setMapSearchModeFlag(Boolean.valueOf(this.isModeFlag));
        if (this.isModeFlag) {
            this.mapModeButton.setBackgroundResource(R.drawable.img_map_main_menu_mode02);
            if (bToast.booleanValue()) {
                Toast.makeText(this.mContext, "\ud035\ubaa8\ub4dc\ub85c \uc124\uc815\ub418\uc5c8\uc2b5\ub2c8\ub2e4.", 0).show();
                return;
            }
            return;
        }
        this.mapModeButton.setBackgroundResource(R.drawable.img_map_main_menu_mode01);
        if (bToast.booleanValue()) {
            Toast.makeText(this.mContext, "\uba54\ub274\ubaa8\ub4dc\ub85c \uc124\uc815\ub418\uc5c8\uc2b5\ub2c8\ub2e4.", 0).show();
        }
    }

    private void startMenuAnimation(Boolean open) {
        this.isMenuOpened = open.booleanValue();
        if (open.booleanValue()) {
            openMenuList();
        } else {
            closeSelectedMenu(0);
        }
    }

    public Boolean isMenuOpened() {
        return Boolean.valueOf(this.isMenuOpened);
    }

    public void openMenuList() {
        if (!this.menuAniFlag.booleanValue()) {
            Animation rotate = new RotateAnimation(0.0f, 45.0f, 1, 0.5f, 1, 0.5f);
            rotate.setInterpolator(AnimationUtils.loadInterpolator(this.mContext, 17432585));
            rotate.setFillAfter(true);
            rotate.setDuration(200);
            this.mapMenuButton.startAnimation(rotate);
            this.isMenuOpened = true;
            this.menuAniFlag = Boolean.valueOf(true);
            this.menuAniCount = 0;
            this.menuListLayout.setVisibility(0);
            for (int i = 0; i < this.buttonCount; i++) {
                clearMoveAnimationButton(i);
                showMenuAnimation(this.buttons, i);
            }
        }
    }

    public void closeSelectedMenu(int index) {
        if (!this.menuAniFlag.booleanValue()) {
            Animation rotate = new RotateAnimation(-45.0f, 0.0f, 1, 0.5f, 1, 0.5f);
            rotate.setInterpolator(AnimationUtils.loadInterpolator(this.mContext, 17432585));
            rotate.setFillAfter(false);
            rotate.setDuration(200);
            this.mapMenuButton.startAnimation(rotate);
            this.isMenuOpened = false;
            this.menuAniFlag = Boolean.valueOf(true);
            this.menuAniCount = 0;
            if (index > 0) {
                int idx = index - 1;
                startSelectedMenu(this.buttons);
                this.selectedListener.onSelectedMapMenu(((SubwayMapButton) this.buttons.get(idx)).getMenuIndex());
                return;
            }
            setButtonEnabled(Boolean.valueOf(this.isMenuOpened));
            for (int i = 0; i < this.buttonCount; i++) {
                closeMenuAnimation(this.buttons, i);
            }
        }
    }

    public void setTouchMapMenuListener(TouchMapMenuListener listener) {
        if (listener == null) {
            this.selectedListener = NULL_TOUCH_MAPMENU_LISTENER;
        } else {
            this.selectedListener = listener;
        }
    }

    public void setButtonEnabled(Boolean flag) {
        for (int i = 0; i < this.buttonCount; i++) {
            ((SubwayMapButton) this.buttons.get(i)).setEnabled(flag.booleanValue());
        }
    }

    public void checkMenuAnimation() {
        if (this.menuAniFlag.booleanValue()) {
            this.menuAniCount++;
            if (this.buttonCount <= this.menuAniCount) {
                if (!this.isMenuOpened) {
                    this.menuListLayout.setVisibility(4);
                }
                setButtonEnabled(Boolean.valueOf(this.isMenuOpened));
                this.menuAniFlag = Boolean.valueOf(false);
            }
        }
    }

    public void onClick(View v) {
        closeSelectedMenu(((SubwayMapButton) v).getButtonIndex());
    }

    public void showMenuAnimation(ArrayList<SubwayMapButton> btns, final int index) {
        final SubwayMapButton button = (SubwayMapButton) btns.get(index);
        float endX = button.getOffsetX();
        AnimationSet animation = new AnimationSet(false);
        Animation translate = new TranslateAnimation(0.0f, endX, 0.0f, 0.0f);
        translate.setDuration((long) this.sub_duration);
        translate.setInterpolator(AnimationUtils.loadInterpolator(this.mContext, 17432584));
        translate.setStartOffset((long) (this.sub_offset * index));
        animation.setFillAfter(false);
        animation.addAnimation(translate);
        animation.setAnimationListener(new AnimationListener() {
            public void onAnimationStart(Animation animation) {
                button.setVisibility(0);
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                Handler mHandler = new Handler();
                final int i = index;
                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        SubwayMapMenuLayout.this.moveAnimationButton(SubwayMapMenuLayout.this.buttons, i, true);
                    }
                }, 5);
                SubwayMapMenuLayout.this.checkMenuAnimation();
            }
        });
        button.startAnimation(animation);
    }

    public void closeMenuAnimation(ArrayList<SubwayMapButton> btns, final int index) {
        SubwayMapButton button = (SubwayMapButton) btns.get(index);
        float endX = button.getOffsetX();
        AnimationSet animation = new AnimationSet(false);
        Animation translate = new TranslateAnimation(0.0f, -endX, 0.0f, 0.0f);
        translate.setDuration((long) this.sub_duration);
        translate.setInterpolator(AnimationUtils.loadInterpolator(this.mContext, 17432583));
        translate.setStartOffset((long) (this.sub_offset * index));
        animation.setFillAfter(true);
        animation.addAnimation(translate);
        animation.setAnimationListener(new AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                Handler mHandler = new Handler();
                final int i = index;
                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        SubwayMapMenuLayout.this.moveAnimationButton(SubwayMapMenuLayout.this.buttons, i, false);
                    }
                }, 5);
                SubwayMapMenuLayout.this.checkMenuAnimation();
            }
        });
        button.startAnimation(animation);
    }

    public void startSelectedMenuAnimation(ArrayList<SubwayMapButton> buttons, int index) {
        this.menuAniCount = 0;
        for (int i = 0; i < this.buttonCount; i++) {
            Animation scale;
            Animation alpha;
            SubwayMapButton view = (SubwayMapButton) buttons.get(i);
            AnimationSet animation = new AnimationSet(false);
            if (index == i) {
                scale = new ScaleAnimation(1.0f, 1.3f, 1.0f, 1.3f, 1, 0.5f, 1, 0.5f);
                scale.setDuration((long) this.sub_select_duration);
                alpha = new AlphaAnimation(1.0f, 0.0f);
                alpha.setDuration((long) this.sub_select_duration);
            } else {
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
                    SubwayMapMenuLayout.this.checkMenuAnimation();
                }
            });
            view.startAnimation(animation);
        }
        this.isMenuOpened = false;
    }

    public void startSelectedMenu(ArrayList<SubwayMapButton> arrayList) {
        this.menuAniCount = 0;
        for (int i = 0; i < this.buttonCount; i++) {
            checkMenuAnimation();
        }
        this.isMenuOpened = false;
    }

    private void moveAnimationButton(ArrayList<SubwayMapButton> btns, int index, boolean open) {
        SubwayMapButton curBtn = (SubwayMapButton) btns.get(index);
        int offsetX = (int) curBtn.getOffsetX();
        if (open) {
            curBtn.setOldOffsetX(offsetX);
            curBtn.offsetLeftAndRight(offsetX);
            return;
        }
        curBtn.setOldOffsetX(-offsetX);
        curBtn.offsetLeftAndRight(-offsetX);
    }

    private void clearMoveAnimationButton(int index) {
        SubwayMapButton curBtn = (SubwayMapButton) this.buttons.get(index);
        int offsetX = curBtn.getOldOffsetX();
        if (offsetX != 0) {
            offsetX *= -1;
            curBtn.offsetLeftAndRight(offsetX);
            curBtn.setOldOffsetX(offsetX);
        }
    }
}
