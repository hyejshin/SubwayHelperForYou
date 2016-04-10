package com.example.hyejung.easysubway.subviews;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.hyejung.easysubway.R;
import com.example.hyejung.easysubway.DBmodel.AppDataManager;
import java.util.ArrayList;

public class StationNameSubView extends RelativeLayout implements OnClickListener {
    private static final StationNameViewListener NULL_STATION_NAME_LISTENER = new StationNameViewListener() {
        public void onSelectedTabButton(Bundle bundle) {
        }
    };
    AppDataManager appManager = null;
    ArrayList<Bundle> arrStationInfo = null;
    Bundle dicStationInfo = null;
    int iSelectedIndex = 0;
    Button lineButton1 = null;
    Button lineButton2 = null;
    Button lineButton3 = null;
    Button lineButton4 = null;
    Context mContext = null;
    LinearLayout nameSubView = null;
    TextView nextButton = null;
    TextView prevButton = null;
    protected StationNameViewListener selectedListener = NULL_STATION_NAME_LISTENER;
    TextView stationName1 = null;
    TextView stationName2 = null;
    TextView stationName3 = null;

    public interface StationNameViewListener {
        void onSelectedTabButton(Bundle bundle);
    }

    public StationNameSubView(Context context) {
        super(context);
        this.mContext = context;
        View nameView = LayoutInflater.from(context).inflate(R.layout.subview_station_name_view, null);
        addView(nameView);
        this.nameSubView = (LinearLayout) nameView.findViewById(R.id.nameSubView);
        this.stationName1 = (TextView) nameView.findViewById(R.id.stationName1);
        this.stationName2 = (TextView) nameView.findViewById(R.id.stationName2);
        this.stationName3 = (TextView) nameView.findViewById(R.id.stationName3);
        this.prevButton = (TextView) nameView.findViewById(R.id.prevButton);
        this.nextButton = (TextView) nameView.findViewById(R.id.nextButton);
        this.lineButton1 = (Button) nameView.findViewById(R.id.lineButton1);
        this.lineButton2 = (Button) nameView.findViewById(R.id.lineButton2);
        this.lineButton3 = (Button) nameView.findViewById(R.id.lineButton3);
        this.lineButton4 = (Button) nameView.findViewById(R.id.lineButton4);
        this.lineButton1.setOnClickListener(this);
        this.lineButton2.setOnClickListener(this);
        this.lineButton3.setOnClickListener(this);
        this.lineButton4.setOnClickListener(this);
        this.prevButton.setOnClickListener(this);
        this.nextButton.setOnClickListener(this);
        int widthPX = getContext().getResources().getDisplayMetrics().widthPixels;
        float scale = getContext().getResources().getDisplayMetrics().density;
        if (((float) widthPX) / scale > 400.0f) {
            widthPX = (int) (110.0f * scale);
        } else {
            widthPX = (int) (((float) ((int) (((float) widthPX) + (32.0f * scale)))) * 0.25f);
        }
        LayoutParams params = (LayoutParams) this.lineButton1.getLayoutParams();
        params.width = widthPX;
        this.lineButton1.setLayoutParams(params);
        params = (LayoutParams) this.lineButton2.getLayoutParams();
        params.width = widthPX;
        this.lineButton2.setLayoutParams(params);
        params = (LayoutParams) this.lineButton3.getLayoutParams();
        params.width = widthPX;
        this.lineButton3.setLayoutParams(params);
        params = (LayoutParams) this.lineButton4.getLayoutParams();
        params.width = widthPX;
        this.lineButton4.setLayoutParams(params);
        this.arrStationInfo = new ArrayList();
        this.appManager = AppDataManager.shared();
    }

    public void onClick(View arg0) {
        int id = arg0.getId();
        if (id == R.id.prevButton) {
            setNextStationInfo(this.dicStationInfo, 1);
        } else if (id == R.id.nextButton) {
            setNextStationInfo(this.dicStationInfo, 2);
        } else {
            int index = 0;
            if (id == R.id.lineButton1) {
                index = 0;
            } else if (id == R.id.lineButton2) {
                index = 1;
            } else if (id == R.id.lineButton3) {
                index = 2;
            } else if (id == R.id.lineButton4) {
                index = 3;
            }
            if (index != this.iSelectedIndex) {
                setLineTabView(index);
            }
        }
    }

    public void setNextStationInfo(Bundle dic, int type) {
        if (dic != null) {
            ArrayList<Bundle> arr;
            String strKey1 = null;
            String strKey2 = null;
            int iTransfer = 0;
            switch (type) {
                case 0:
                    strKey1 = "stationName";
                    strKey2 = "stationCode";
                    iTransfer = Integer.valueOf(dic.getString("transfer")).intValue();
                    break;
                case 1:
                    strKey1 = "nStationName1";
                    strKey2 = "nStationCode1";
                    iTransfer = checkTransferData(dic.getString(strKey2));
                    break;
                case 2:
                    strKey1 = "nStationName2";
                    strKey2 = "nStationCode2";
                    iTransfer = checkTransferData(dic.getString(strKey2));
                    break;
            }
            if (iTransfer > 0) {
                arr = this.appManager.getArrStationInfoName(dic.getString(strKey1));
            } else {
                arr = this.appManager.getArrStationDataCode(dic.getString(strKey2));
            }
            if (arr.size() > 0) {
                this.arrStationInfo.clear();
                this.arrStationInfo.addAll(arr);
                this.iSelectedIndex = 0;
                for (int i = 0; i < this.arrStationInfo.size(); i++) {
                    if (Integer.valueOf(dic.getString("lineCode")) == Integer.valueOf(((Bundle) this.arrStationInfo.get(i)).getString("lineCode"))) {
                        this.iSelectedIndex = i;
                        setLineTabView(this.iSelectedIndex);
                    }
                }
                setLineTabView(this.iSelectedIndex);
            }
        }
    }

    private int checkTransferData(String strCode) {
        ArrayList<Bundle> arr = this.appManager.getArrStationDataCode(strCode);
        if (arr.size() > 0) {
            return ((Bundle) arr.get(0)).getInt("transfer");
        }
        return 0;
    }

    private void setLineTabView(int index) {
        this.iSelectedIndex = index;
        this.dicStationInfo = (Bundle) this.arrStationInfo.get(this.iSelectedIndex);
        String strName = this.dicStationInfo.getString("stationName");
        String strSubName = this.dicStationInfo.getString("subName");
        if (strSubName.length() > 0) {
            this.stationName1.setVisibility(4);
            this.stationName2.setVisibility(0);
            this.stationName3.setVisibility(0);
            this.stationName1.setText("");
            this.stationName2.setText(strName);
            this.stationName3.setText("(" + strSubName + ")");
        } else {
            this.stationName1.setVisibility(0);
            this.stationName2.setVisibility(4);
            this.stationName3.setVisibility(4);
            this.stationName1.setText(strName);
            this.stationName2.setText("");
            this.stationName3.setText("");
            if (strName.length() > 7) {
                this.stationName1.setTextSize(1, 20.0f);
            } else {
                this.stationName1.setTextSize(1, 23.0f);
            }
        }
        String strData = this.dicStationInfo.getString("nStationName1");
        if (strData == null) {
            this.prevButton.setText("\uc5c6\uc74c");
        } else if (strData.equals("0")) {
            this.prevButton.setText("\uc5c6\uc74c");
        } else {
            this.prevButton.setText("<" + strData);
        }
        strData = this.dicStationInfo.getString("nStationName2");
        if (strData == null) {
            this.nextButton.setText("\uc5c6\uc74c");
        } else if (strData.equals("0")) {
            this.nextButton.setText("\uc5c6\uc74c");
        } else {
            this.nextButton.setText(new StringBuilder(String.valueOf(strData)).append(">").toString());
        }
        this.lineButton1.setVisibility(4);
        this.lineButton2.setVisibility(4);
        this.lineButton3.setVisibility(4);
        this.lineButton4.setVisibility(4);
        Button button = null;
        for (int i = 0; i < this.arrStationInfo.size(); i++) {
            Bundle bundle = (Bundle) this.arrStationInfo.get(i);
            switch (i) {
                case 0:
                    button = this.lineButton1;
                    break;
                case 1:
                    button = this.lineButton2;
                    break;
                case 2:
                    button = this.lineButton3;
                    break;
                case 3:
                    button = this.lineButton4;
                    break;
            }
            button.setText(bundle.getString("lineName"));
            button.setVisibility(0);
            int iLineCode = Integer.valueOf(bundle.getString("lineCode")).intValue();
            if (this.iSelectedIndex == i) {
                button.setSelected(true);
                this.nameSubView.setBackgroundResource(this.appManager.getStationNameImage(iLineCode));
                button.bringToFront();
                button.invalidate();
                int textColor = this.appManager.getStationNameColor(iLineCode);
                this.prevButton.setTextColor(textColor);
                this.nextButton.setTextColor(textColor);
            } else {
                button.setSelected(false);
            }
            button.setBackgroundResource(this.appManager.getLineTabImage(iLineCode));
        }
        this.selectedListener.onSelectedTabButton(this.dicStationInfo);
    }

    public void setStationNameViewListener(StationNameViewListener listener) {
        if (listener == null) {
            this.selectedListener = NULL_STATION_NAME_LISTENER;
        } else {
            this.selectedListener = listener;
        }
    }
}
