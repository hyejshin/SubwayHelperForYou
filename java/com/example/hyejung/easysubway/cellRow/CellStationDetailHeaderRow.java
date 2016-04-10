package com.example.hyejung.easysubway.cellRow;

import android.view.View;
import android.widget.TextView;
import com.example.hyejung.easysubway.R;

public class CellStationDetailHeaderRow {
    private View backView;
    private View cell_View;
    private TextView titleView;

    public CellStationDetailHeaderRow(View cell) {
        this.cell_View = cell;
    }

    public void setTitle(String title) {
        getTitleView().setText(title);
    }
    //뒤로가기
    public View getBackView() {
        if (this.backView == null) {
            this.backView = (TextView) this.cell_View.findViewById(R.id.backView);
        }
        return this.backView;
    }
    
    public TextView getTitleView() {
        if (this.titleView == null) {
            this.titleView = (TextView) this.cell_View.findViewById(R.id.titleView);
        }
        return this.titleView;
    }
}
