package com.example.hyejung.easysubway.cellRow;

import android.view.View;
import android.widget.TextView;
import com.example.hyejung.easysubway.R;

public class StationTimeViewHeaderRow {
    private View backView;
    private View cell_View;
    private TextView titleView;

    public StationTimeViewHeaderRow(View cell) {
        this.cell_View = cell;
    }

    public void setTimeView(Boolean flag) {
        if (flag.booleanValue()) {
            getTitleView().setText("\uc2e4\uc2dc\uac04\ub3c4\ucc29 \uc815\ubcf4\uc785\ub2c8\ub2e4");
            getBackView().setBackgroundColor(-1722954522);
            return;
        }
        getTitleView().setText("\uc2dc\uac01\ud45c \uacc4\uc0b0 \uc815\ubcf4\uc785\ub2c8\ub2e4");
        getBackView().setBackgroundColor(-1719631744);
    }

    public View getBackView() {
        if (this.backView == null) {
            this.backView = this.cell_View.findViewById(R.id.backView);
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
