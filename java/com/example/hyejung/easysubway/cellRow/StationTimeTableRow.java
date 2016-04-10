package com.example.hyejung.easysubway.cellRow;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.hyejung.easysubway.R;

public class StationTimeTableRow extends PathListRow {
    private View cell_View;
    private RelativeLayout timeLayout1;
    private RelativeLayout timeLayout2;
    private TextView timeValue11;
    private TextView timeValue12;
    private TextView timeValue13;
    private TextView timeValue14;
    private TextView timeValue21;
    private TextView timeValue22;
    private TextView timeValue23;
    private TextView timeValue24;

    public StationTimeTableRow(View cell) {
        super(cell);
        this.cell_View = cell;
    }

    private void _setCellData(Bundle bundle, int type) {
        int iColor;
        switch (type == 1 ? bundle.getInt("type1") : bundle.getInt("type2")) {
            case 1:
                iColor = -2285022;
                break;
            case 2:
                iColor = -14540254;
                break;
            default:
                iColor = -5592406;
                break;
        }
        if (bundle.getString("minutes" + type).length() > 0) {
            int second = Integer.parseInt(bundle.getString("second" + type));
            getTimeValue1(type).setText(bundle.getString("minutes" + type));
            getTimeValue2(type).setText(String.format("\ubd84 %02d \ucd08", new Object[]{Integer.valueOf(second)}));
            getTimeValue1(type).setTextColor(iColor);
            getTimeValue2(type).setTextColor(iColor);
            String strData = bundle.getString("dest" + type);
            if (strData.length() > 0) {
                if (Integer.valueOf(bundle.getString("exFlag" + type)).intValue() == 1) {
                    getTimeValue3(type).setTextColor(-5622989);
                    strData = "(\uae09)" + strData;
                } else {
                    getTimeValue3(type).setTextColor(iColor);
                }
                if (strData.length() < 7 && !strData.contains("\uc21c\ud658")) {
                    strData = new StringBuilder(String.valueOf(strData)).append("\ud589").toString();
                }
                getTimeValue3(type).setText(strData);
            } else {
                getTimeValue3(type).setText("");
            }
            strData = bundle.getString("wSub" + type);
            if (strData.length() > 0) {
                int iSubData = Integer.valueOf(strData).intValue();
                if (iSubData == 88) {
                    getTimeValue4(type).setText("[\ucd9c\ubc1c\uc5ed]");
                    getTimeValue4(type).setTextColor(-5614251);
                } else if (iSubData == 99) {
                    getTimeValue4(type).setText("[\uc885\ucc29\uc5ed]");
                    getTimeValue4(type).setTextColor(-11184726);
                } else {
                    getTimeValue4(type).setText("\uc5f4\ucc28\ucd9c\ubc1c");
                    getTimeValue4(type).setTextColor(iColor);
                }
            } else {
                getTimeValue4(type).setText("");
            }
            getTimeLayout(type).setVisibility(0);
            return;
        }
        getTimeLayout(type).setVisibility(4);
    }

    public void setCellData(Bundle bundle) {
        if (bundle != null) {
            _setCellData(bundle, 1);
            _setCellData(bundle, 2);
        }
    }

    public TextView getTimeValue1(int type) {
        if (type == 1) {
            if (this.timeValue11 == null) {
                this.timeValue11 = (TextView) this.cell_View.findViewById(R.id.timeValue11);
            }
            return this.timeValue11;
        }
        if (this.timeValue21 == null) {
            this.timeValue21 = (TextView) this.cell_View.findViewById(R.id.timeValue21);
        }
        return this.timeValue21;
    }

    public TextView getTimeValue2(int type) {
        if (type == 1) {
            if (this.timeValue12 == null) {
                this.timeValue12 = (TextView) this.cell_View.findViewById(R.id.timeValue12);
            }
            return this.timeValue12;
        }
        if (this.timeValue22 == null) {
            this.timeValue22 = (TextView) this.cell_View.findViewById(R.id.timeValue22);
        }
        return this.timeValue22;
    }

    public TextView getTimeValue3(int type) {
        if (type == 1) {
            if (this.timeValue13 == null) {
                this.timeValue13 = (TextView) this.cell_View.findViewById(R.id.timeValue13);
            }
            return this.timeValue13;
        }
        if (this.timeValue23 == null) {
            this.timeValue23 = (TextView) this.cell_View.findViewById(R.id.timeValue23);
        }
        return this.timeValue23;
    }

    public TextView getTimeValue4(int type) {
        if (type == 1) {
            if (this.timeValue14 == null) {
                this.timeValue14 = (TextView) this.cell_View.findViewById(R.id.timeValue14);
            }
            return this.timeValue14;
        }
        if (this.timeValue24 == null) {
            this.timeValue24 = (TextView) this.cell_View.findViewById(R.id.timeValue24);
        }
        return this.timeValue24;
    }

    public RelativeLayout getTimeLayout(int type) {
        if (type == 1) {
            if (this.timeLayout1 == null) {
                this.timeLayout1 = (RelativeLayout) this.cell_View.findViewById(R.id.timeLayout1);
            }
            return this.timeLayout1;
        }
        if (this.timeLayout2 == null) {
            this.timeLayout2 = (RelativeLayout) this.cell_View.findViewById(R.id.timeLayout2);
        }
        return this.timeLayout2;
    }
}
