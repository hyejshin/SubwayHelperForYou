package com.example.hyejung.easysubway.cellRow;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.hyejung.easysubway.R;

public class StationTimeTableHeaderRow {
    private View cell_View;
    private ImageView indicatorIcon;
    private TextView titleView;

    public StationTimeTableHeaderRow(View cell) {
        this.cell_View = cell;
    }

    public void setTitle(String title) {
        getTitleView().setText(title);
    }

    public TextView getTitleView() {
        if (this.titleView == null) {
            this.titleView = (TextView) this.cell_View.findViewById(R.id.titleView);
        }
        return this.titleView;
    }

    public ImageView getImageView() {
        if (this.indicatorIcon == null) {
            this.indicatorIcon = (ImageView) this.cell_View.findViewById(R.id.indicatorIcon);
        }
        return this.indicatorIcon;
    }
}
