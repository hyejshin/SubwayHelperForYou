package com.example.hyejung.easysubway.cellRow;

import android.view.View;
import android.widget.TextView;
import com.example.hyejung.easysubway.R;
import com.example.hyejung.easysubway.DBmodel.RealTimeCellModel;

public class StationRealTimeDescRow {
    private View cell_View;
    private TextView subTitleView;
    private TextView titleView;

    public StationRealTimeDescRow(View cell) {
        this.cell_View = cell;
    }

    public void setModelData(RealTimeCellModel model) {
        if (model != null) {
            getTitleView().setText(model.getTitle());
            getSubTitleView().setText(model.getSubTitle());
        }
    }

    public TextView getTitleView() {
        if (this.titleView == null) {
            this.titleView = (TextView) this.cell_View.findViewById(R.id.titleView);
        }
        return this.titleView;
    }

    public TextView getSubTitleView() {
        if (this.subTitleView == null) {
            this.subTitleView = (TextView) this.cell_View.findViewById(R.id.subTitleView);
        }
        return this.subTitleView;
    }
}
