package com.example.hyejung.easysubway.cellRow;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.hyejung.easysubway.R;

import com.example.hyejung.easysubway.DBmodel.AppDataManager;
import com.example.hyejung.easysubway.DBmodel.RealTimeCellModel;
import java.util.ArrayList;

public class StationRealTimeRow02 {
    private ImageView arrowView01 = ((ImageView) this.cell_View.findViewById(R.id.arrowView01));
    private ImageView arrowView02 = ((ImageView) this.cell_View.findViewById(R.id.arrowView02));
    private View cell_View;
    private TextView cursLabel01 = ((TextView) this.cell_View.findViewById(R.id.cursLabel01));
    private TextView cursLabel02 = ((TextView) this.cell_View.findViewById(R.id.cursLabel02));
    private TextView cursLabel03 = ((TextView) this.cell_View.findViewById(R.id.cursLabel03));
    private ImageView lineView = ((ImageView) this.cell_View.findViewById(R.id.lineView));
    private ImageView lineView01 = ((ImageView) this.cell_View.findViewById(R.id.lineView01));
    private ImageView lineView02 = ((ImageView) this.cell_View.findViewById(R.id.lineView02));
    private ImageView lineView03 = ((ImageView) this.cell_View.findViewById(R.id.lineView03));
    private Context mContext;
    private ImageView pointView = ((ImageView) this.cell_View.findViewById(R.id.pointView));
    private RelativeLayout pointView01 = ((RelativeLayout) this.cell_View.findViewById(R.id.pointView01));
    private RelativeLayout pointView02 = ((RelativeLayout) this.cell_View.findViewById(R.id.pointView02));
    private RelativeLayout pointView03 = ((RelativeLayout) this.cell_View.findViewById(R.id.pointView03));
    private TextView timeLabel01 = ((TextView) this.cell_View.findViewById(R.id.timeLabel01));
    private TextView timeLabel02 = ((TextView) this.cell_View.findViewById(R.id.timeLabel02));
    private TextView timeLabel03 = ((TextView) this.cell_View.findViewById(R.id.timeLabel03));
    private TextView titleLabel = ((TextView) this.cell_View.findViewById(R.id.titleLabel));

    public StationRealTimeRow02(View cell) {
        this.cell_View = cell;
    }

    public void setModelData(Context context, RealTimeCellModel model) {
        if (model != null) {
            this.mContext = context;
            if (model.getEndFlag() == 0) {
                String title = model.getInfo();
                if (title != null && title.length() > 0) {
                    title = "\uc774\ubc88\uc5f4\ucc28 : " + title;
                }
                this.titleLabel.setText(title);
            } else {
                this.titleLabel.setText("\uc815\ubcf4\uac00 \uc5c6\uc2b5\ub2c8\ub2e4.");
            }
            int iAreaCode = AppDataManager.shared().getAreaCode();
            int iLineCode = model.getLineCode();
            this.lineView.setImageResource(this.mContext.getResources().getIdentifier("img_linecolor_" + iAreaCode + "_" + iLineCode, "drawable", this.mContext.getPackageName()));
            int resId = this.mContext.getResources().getIdentifier("img_linepoint_" + iAreaCode + "_" + iLineCode, "drawable", this.mContext.getPackageName());
            this.lineView01.setImageResource(resId);
            this.lineView02.setImageResource(resId);
            this.lineView03.setImageResource(resId);
            int iPoint = 0;
            this.pointView.setVisibility(0);
            this.pointView01.setVisibility(4);
            this.pointView02.setVisibility(4);
            this.pointView03.setVisibility(4);
            this.arrowView01.setVisibility(0);
            this.arrowView02.setVisibility(0);
            ArrayList<Bundle> arrData = model.getArrData();
            for (int i = 0; i < arrData.size(); i++) {
                Bundle bundle = (Bundle) arrData.get(i);
                if (iPoint == 0) {
                    if (bundle.getInt("curValue") == 0) {
                        this.pointView.setVisibility(4);
                        this.pointView01.setVisibility(0);
                        this.cursLabel01.setText(bundle.getString("curs"));
                        this.timeLabel01.setText(bundle.getString("time"));
                        iPoint = 1;
                    } else {
                        this.arrowView01.setVisibility(0);
                        this.pointView02.setVisibility(0);
                        this.cursLabel02.setText(bundle.getString("curs"));
                        this.timeLabel02.setText(bundle.getString("time"));
                        iPoint = 2;
                    }
                } else if (iPoint == 1) {
                    this.arrowView01.setVisibility(0);
                    this.pointView02.setVisibility(0);
                    this.cursLabel02.setText(bundle.getString("curs"));
                    this.timeLabel02.setText(bundle.getString("time"));
                    iPoint++;
                } else if (iPoint == 2) {
                    this.arrowView02.setVisibility(0);
                    this.pointView03.setVisibility(0);
                    this.cursLabel03.setText(bundle.getString("curs"));
                    this.timeLabel03.setText(bundle.getString("time"));
                    iPoint++;
                }
            }
        }
    }
}
