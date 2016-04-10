package com.example.hyejung.easysubway.subwaymapview.scroller;

import android.graphics.Matrix;
import android.graphics.RectF;

public class ScrollOver implements ScrollDelegate {
    private final float mOverScrollRate;

    public ScrollOver(float overScrollRate) {
        this.mOverScrollRate = overScrollRate;
    }

    public boolean onScrollX(RectF rectMap, RectF rectView, float distance, Matrix matrix) {
        matrix.postTranslate(-(distance * this.mOverScrollRate), 0.0f);
        return true;
    }

    public boolean onScrollY(RectF rectMap, RectF rectView, float distance, Matrix matrix) {
        matrix.postTranslate(0.0f, -(distance * this.mOverScrollRate));
        return true;
    }
}
