package com.example.hyejung.easysubway.subwaymapview.scroller;

import android.graphics.Matrix;
import android.graphics.RectF;

public class ScrollStop implements ScrollDelegate {
    public boolean onScrollX(RectF content, RectF container, float distance, Matrix matrix) {
        if (content.width() > container.width()) {
            if (distance < 0.0f) {
                distance = content.left - container.left;
            } else {
                distance = content.right - container.right;
            }
            if (distance != 0.0f) {
                matrix.postTranslate(-distance, 0.0f);
                return true;
            }
        }
        return false;
    }

    public boolean onScrollY(RectF content, RectF container, float distance, Matrix matrix) {
        if (content.height() > container.height()) {
            if (distance < 0.0f) {
                distance = content.top - container.top;
            } else {
                distance = content.bottom - container.bottom;
            }
            if (distance != 0.0f) {
                matrix.postTranslate(0.0f, -distance);
                return true;
            }
        }
        return false;
    }
}
