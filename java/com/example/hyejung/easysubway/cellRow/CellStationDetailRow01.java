package com.example.hyejung.easysubway.cellRow;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.example.hyejung.easysubway.R;

public class CellStationDetailRow01 {
    private View cell_View;
    private TextView descView;
    private TextView titleView;

    public CellStationDetailRow01(View cell) {
        this.cell_View = cell;
    }

    public void setCellData(Bundle bundle) {
        if (bundle != null) {
            getTitleView().setText(bundle.getString("title"));
            getDescView().setText(bundle.getString("desc"));
        }
    }

    public TextView getTitleView() {
        if (this.titleView == null) {
            this.titleView = (TextView) this.cell_View.findViewById(R.id.titleView);
        }
        return this.titleView;
    }

    public TextView getDescView() {
        if (this.descView == null) {
            this.descView = (TextView) this.cell_View.findViewById(R.id.descView);
        }
        return this.descView;
    }
}
