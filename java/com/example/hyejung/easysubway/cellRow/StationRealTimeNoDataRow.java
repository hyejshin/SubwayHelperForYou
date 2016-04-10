package com.example.hyejung.easysubway.cellRow;

import android.view.View;
import android.widget.TextView;

import com.example.hyejung.easysubway.R;

public class StationRealTimeNoDataRow {
    private View cell_View;
    private TextView noDataView;

    public StationRealTimeNoDataRow(View cell) {
        this.cell_View = cell;
    }

    public void setTitleData(String text) {
        if (text == null) {
            text = "\uc815\ubcf4\uac00 \uc5c6\uc2b5\ub2c8\ub2e4.";
        } else {
            getNoDataView().setText(text);
        }
    }

    public TextView getNoDataView() {
        if (this.noDataView == null) {
            this.noDataView = (TextView) this.cell_View.findViewById(R.id.noDataView);
        }
        return this.noDataView;
    }
}
