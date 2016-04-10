package com.example.hyejung.easysubway.cellRow;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.hyejung.easysubway.R;

import com.example.hyejung.easysubway.DBmodel.RealTimeCellModel;

public class StationRealTimeHeaderRow {
    private View cell_View;
    private ImageView nextImageView;
    private ImageView prevImageView;
    private TextView titleView;

    public StationRealTimeHeaderRow(View cell) {
        this.cell_View = cell;
    }

    public void setModelData(RealTimeCellModel model) {
        if (model != null) {
            getTitleView().setText(model.getTitle());
            if (model.getTrainLine() == 0) {
                getPrevImageView().setVisibility(0);
                getNextImageView().setVisibility(4);
            } else if (model.getTrainLine() == 1) {
                getPrevImageView().setVisibility(4);
                getNextImageView().setVisibility(0);
            }
        }
    }

    public TextView getTitleView() {
        if (this.titleView == null) {
            this.titleView = (TextView) this.cell_View.findViewById(R.id.titleView);
        }
        return this.titleView;
    }

    public ImageView getPrevImageView() {
        if (this.prevImageView == null) {
            this.prevImageView = (ImageView) this.cell_View.findViewById(R.id.prevIcon);
        }
        return this.prevImageView;
    }

    public ImageView getNextImageView() {
        if (this.nextImageView == null) {
            this.nextImageView = (ImageView) this.cell_View.findViewById(R.id.nextIcon);
        }
        return this.nextImageView;
    }
}
