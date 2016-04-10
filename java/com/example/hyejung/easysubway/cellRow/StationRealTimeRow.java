package com.example.hyejung.easysubway.cellRow;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.hyejung.easysubway.R;

public class StationRealTimeRow extends PathListRow {
    private View cell_View;
    private TextView dName1;
    private TextView dName2;
    private TextView desc1;
    private TextView desc2;
    private TextView minutes11;
    private TextView minutes12;
    private TextView minutes21;
    private TextView minutes22;
    private TextView seconds12;
    private TextView seconds13;
    private TextView seconds22;
    private TextView seconds23;
    private RelativeLayout timeLayout1;
    private RelativeLayout timeLayout2;
    private LinearLayout timeType11;
    private LinearLayout timeType12;
    private LinearLayout timeType13;
    private LinearLayout timeType14;
    private LinearLayout timeType21;
    private LinearLayout timeType22;
    private LinearLayout timeType23;
    private LinearLayout timeType24;
    private TextView title14;
    private TextView title24;

    public StationRealTimeRow(View cell) {
        super(cell);
        this.cell_View = cell;
    }

    private void _setCellData(Bundle bundle, int type) {
        String strData = bundle.getString("desc" + type);
        if (strData.length() > 0) {
            int viewType;
            getDesc(type).setText(strData);
            strData = bundle.getString("dName" + type);
            if (strData.length() > 0) {
                getDName(type).setText(strData);
            }
            int iMinutes = 0;
            int iSeconds = 0;
            int iTimeData = bundle.getInt("tTime" + type);
            if (iTimeData > 100) {
                viewType = 0;
                iMinutes = (iTimeData / 60) + 1;
            } else if (iTimeData > 60) {
                viewType = 1;
                iMinutes = iTimeData / 60;
                iSeconds = iTimeData % 60;
            } 
            else if (iTimeData > 10) {
                viewType = 2;
                iSeconds = iTimeData;
            } else {
                viewType = 3;
            }
            if (viewType == 0) {
                getMinutes1(type).setText(String.valueOf(iMinutes));
                getTimeType1(type).setVisibility(0);
                getTimeType2(type).setVisibility(4);
                getTimeType3(type).setVisibility(4);
                getTimeType4(type).setVisibility(4);
            } 
            else if (viewType == 1) {
                getMinutes2(type).setText(String.valueOf(iMinutes));
                getSeconds2(type).setText(String.format("%02d", new Object[]{Integer.valueOf(iSeconds)}));
                getTimeType1(type).setVisibility(4);
                getTimeType2(type).setVisibility(0);
                getTimeType3(type).setVisibility(4);
                getTimeType4(type).setVisibility(4);
            } 
            else if (viewType == 2) {
                getSeconds3(type).setText(String.format("%02d", new Object[]{Integer.valueOf(iSeconds)}));
                getTimeType1(type).setVisibility(4);
                getTimeType2(type).setVisibility(4);
                getTimeType3(type).setVisibility(0);
                getTimeType4(type).setVisibility(4);
            }
            else {
                strData = bundle.getString("title" + type);
                if (strData.length() < 3) {
                    if (iTimeData > 10) {
                        strData = "\uc7a0\uc2dc\ud6c4 \ub3c4\ucc29\uc608\uc815\uc785\ub2c8\ub2e4";
                    } else {
                        strData = bundle.getString("desc" + type);
                    }
                }
                getTitle4(type).setText(strData);
                getTimeType1(type).setVisibility(4);
                getTimeType2(type).setVisibility(4);
                getTimeType3(type).setVisibility(4);
                getTimeType4(type).setVisibility(0);
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

    public TextView getMinutes1(int type) {
        if (type == 1) {
            if (this.minutes11 == null) {
                this.minutes11 = (TextView) this.cell_View.findViewById(R.id.minutes11);
            }
            return this.minutes11;
        }
        if (this.minutes21 == null) {
            this.minutes21 = (TextView) this.cell_View.findViewById(R.id.minutes21);
        }
        return this.minutes21;
    }

    public TextView getMinutes2(int type) {
        if (type == 1) {
            if (this.minutes12 == null) {
                this.minutes12 = (TextView) this.cell_View.findViewById(R.id.minutes12);
            }
            return this.minutes12;
        }
        if (this.minutes22 == null) {
            this.minutes22 = (TextView) this.cell_View.findViewById(R.id.minutes22);
        }
        return this.minutes22;
    }

    public TextView getSeconds2(int type) {
        if (type == 1) {
            if (this.seconds12 == null) {
                this.seconds12 = (TextView) this.cell_View.findViewById(R.id.seconds12);
            }
            return this.seconds12;
        }
        if (this.seconds22 == null) {
            this.seconds22 = (TextView) this.cell_View.findViewById(R.id.seconds22);
        }
        return this.seconds22;
    }

    public TextView getSeconds3(int type) {
        if (type == 1) {
            if (this.seconds13 == null) {
                this.seconds13 = (TextView) this.cell_View.findViewById(R.id.seconds13);
            }
            return this.seconds13;
        }
        if (this.seconds23 == null) {
            this.seconds23 = (TextView) this.cell_View.findViewById(R.id.seconds23);
        }
        return this.seconds23;
    }

    public TextView getTitle4(int type) {
        if (type == 1) {
            if (this.title14 == null) {
                this.title14 = (TextView) this.cell_View.findViewById(R.id.title14);
            }
            return this.title14;
        }
        if (this.title24 == null) {
            this.title24 = (TextView) this.cell_View.findViewById(R.id.title24);
        }
        return this.title24;
    }

    public TextView getDName(int type) {
        if (type == 1) {
            if (this.dName1 == null) {
                this.dName1 = (TextView) this.cell_View.findViewById(R.id.dName1);
            }
            return this.dName1;
        }
        if (this.dName2 == null) {
            this.dName2 = (TextView) this.cell_View.findViewById(R.id.dName2);
        }
        return this.dName2;
    }

    public TextView getDesc(int type) {
        if (type == 1) {
            if (this.desc1 == null) {
                this.desc1 = (TextView) this.cell_View.findViewById(R.id.desc1);
            }
            return this.desc1;
        }
        if (this.desc2 == null) {
            this.desc2 = (TextView) this.cell_View.findViewById(R.id.desc2);
        }
        return this.desc2;
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

    public LinearLayout getTimeType4(int type) {
        if (type == 1) {
            if (this.timeType14 == null) {
                this.timeType14 = (LinearLayout) this.cell_View.findViewById(R.id.timeType14);
            }
            return this.timeType14;
        }
        if (this.timeType24 == null) {
            this.timeType24 = (LinearLayout) this.cell_View.findViewById(R.id.timeType24);
        }
        return this.timeType24;
    }
}
