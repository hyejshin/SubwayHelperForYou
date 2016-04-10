package com.example.hyejung.easysubway.subwaymapview.scroller;

import android.graphics.Matrix;
import android.graphics.RectF;

public interface ScrollDelegate {
    boolean onScrollX(RectF rectF, RectF rectF2, float f, Matrix matrix);

    boolean onScrollY(RectF rectF, RectF rectF2, float f, Matrix matrix);
}
