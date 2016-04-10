package com.example.hyejung.easysubway.cellRow;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.hyejung.easysubway.R;

public class StationTimeDataRow extends PathListRow {
    private View cell_View;
    private TextView timeData10;
    private TextView timeData11;
    private TextView timeData12;
    private TextView timeData13;
    private TextView timeData14;
    private TextView timeData20;
    private TextView timeData21;
    private TextView timeData22;
    private TextView timeData23;
    private TextView timeData24;
    private RelativeLayout timeLayout1;
    private RelativeLayout timeLayout2;
    private LinearLayout timeType11;
    private LinearLayout timeType12;
    private LinearLayout timeType13;
    private LinearLayout timeType21;
    private LinearLayout timeType22;
    private LinearLayout timeType23;

    public StationTimeDataRow(View cell) {
        super(cell);
        this.cell_View = cell;
    }

    private void _setCellData(Bundle bundle, int type) {
        String strData = bundle.getString("timeTable" + type);
        if (strData.length() > 0) {
            getTimeData4(type).setText(strData);
            int minutes = bundle.getInt("minutes" + type);
            if (minutes > 1) {
                getTimeData0(type).setText(String.valueOf(minutes));//minutes);
                getTimeType1(type).setVisibility(0);
                getTimeType2(type).setVisibility(4);
                getTimeType3(type).setVisibility(4);
            } else {
                int seconds = bundle.getInt("seconds" + type);
                if (minutes > 0 || (minutes == 0 && seconds > 49)) {
                    getTimeData1(type).setText(String.valueOf(minutes));//minutes);
                    getTimeData2(type).setText(String.format("%02d", new Object[]{Integer.valueOf(seconds)}));
                    getTimeType1(type).setVisibility(4);
                    getTimeType2(type).setVisibility(0);
                    getTimeType3(type).setVisibility(4);
                } 
                else {
                    seconds += minutes * 60;
                    TextView _textView = getTimeDesc(type);
                    if (seconds > 0) {
                        _textView.setText(R.string.time_sec_30);
                    } else {
                        _textView.setText(R.string.time_sec_0);
                    }
                    getTimeType1(type).setVisibility(4);
                    getTimeType2(type).setVisibility(4);
                    getTimeType3(type).setVisibility(0);
                }
            }
            TextView timeData3 = getTimeData3(type);
            strData = bundle.getString("dName" + type);
            if (strData.length() > 0) {
                if (Integer.valueOf(bundle.getString("exFlag" + type)).intValue() == 1) {
                    timeData3.setTextColor(-5622989);
                    strData = "(\uae09)" + strData;
                } else {
                    timeData3.setTextColor(-7829368);
                }
                if (strData.length() < 7 && !strData.contains("\uc21c\ud658")) {
                    strData = new StringBuilder(String.valueOf(strData)).append("\ud589").toString();
                }
                timeData3.setText(strData);
            } else {
                timeData3.setText("");
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

    public TextView getTimeData0(int type) {
        if (type == 1) {
            if (this.timeData10 == null) {
                this.timeData10 = (TextView) this.cell_View.findViewById(R.id.timeData10);
            }
            return this.timeData10;
        }
        if (this.timeData20 == null) {
            this.timeData20 = (TextView) this.cell_View.findViewById(R.id.timeData20);
        }
        return this.timeData20;
    }

    public TextView getTimeData1(int type) {
        if (type == 1) {
            if (this.timeData11 == null) {
                this.timeData11 = (TextView) this.cell_View.findViewById(R.id.timeData11);
            }
            return this.timeData11;
        }
        if (this.timeData21 == null) {
            this.timeData21 = (TextView) this.cell_View.findViewById(R.id.timeData21);
        }
        return this.timeData21;
    }

    public TextView getTimeData2(int type) {
        if (type == 1) {
            if (this.timeData12 == null) {
                this.timeData12 = (TextView) this.cell_View.findViewById(R.id.timeData12);
            }
            return this.timeData12;
        }
        if (this.timeData22 == null) {
            this.timeData22 = (TextView) this.cell_View.findViewById(R.id.timeData22);
        }
        return this.timeData22;
    }

    public TextView getTimeData3(int type) {
        if (type == 1) {
            if (this.timeData13 == null) {
                this.timeData13 = (TextView) this.cell_View.findViewById(R.id.timeData13);
            }
            return this.timeData13;
        }
        if (this.timeData23 == null) {
            this.timeData23 = (TextView) this.cell_View.findViewById(R.id.timeData23);
        }
        return this.timeData23;
    }

    public TextView getTimeData4(int type) {
        if (type == 1) {
            if (this.timeData14 == null) {
                this.timeData14 = (TextView) this.cell_View.findViewById(R.id.timeData14);
            }
            return this.timeData14;
        }
        if (this.timeData24 == null) {
            this.timeData24 = (TextView) this.cell_View.findViewById(R.id.timeData24);
        }
        return this.timeData24;
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

    public LinearLayout getTimeType1(int type) {
        if (type == 1) {
            if (this.timeType11 == null) {
                this.timeType11 = (LinearLayout) this.cell_View.findViewById(R.id.timeType11);
            }
            return this.timeType11;
        }
        if (this.timeType21 == null) {
            this.timeType21 = (LinearLayout) this.cell_View.findViewById(R.id.timeType21);
        }
        return this.timeType21;
    }

    public LinearLayout getTimeType2(int type) {
        if (type == 1) {
            if (this.timeType12 == null) {
                this.timeType12 = (LinearLayout) this.cell_View.findViewById(R.id.timeType12);
            }
            return this.timeType12;
        }
        if (this.timeType22 == null) {
            this.timeType22 = (LinearLayout) this.cell_View.findViewById(R.id.timeType22);
        }
        return this.timeType22;
    }

    public LinearLayout getTimeType3(int type) {
        if (type == 1) {
            if (this.timeType13 == null) {
                this.timeType13 = (LinearLayout) this.cell_View.findViewById(R.id.timeType13);
            }
            return this.timeType13;
        }
        if (this.timeType23 == null) {
            this.timeType23 = (LinearLayout) this.cell_View.findViewById(R.id.timeType23);
        }
        return this.timeType23;
    }

    public TextView getTimeDesc(int type) {
        if (type == 1) {
            return (TextView) getTimeType3(type).findViewById(R.id.timeDesc13);
        }
        return (TextView) getTimeType3(type).findViewById(R.id.timeDesc23);
    }
}
