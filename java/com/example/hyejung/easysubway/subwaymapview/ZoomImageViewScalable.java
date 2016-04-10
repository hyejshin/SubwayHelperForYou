package com.example.hyejung.easysubway.subwaymapview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.OvershootInterpolator;
import com.example.hyejung.easysubway.subwaymapview.ScaleHandler.ScaleListener;

@TargetApi(4)
public class ZoomImageViewScalable extends ZoomImageViewTouchable implements ScaleListener {
    private static final String TAG = ZoomImageViewScalable.class.getSimpleName();
    private static final OvershootInterpolator overshootInterpolator = new OvershootInterpolator();
    private float fMaxZoomLevel;
    private float fMinZoomLevel;
    private ScaleHandler mScaleHandler;
    private float[] matrixOriginValues;
    private float[] matrixValues;
    Matrix savedMatrix;

    public ZoomImageViewScalable(Context context) {
        this(context, null, 0);
    }

    public ZoomImageViewScalable(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomImageViewScalable(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.matrixValues = new float[9];
        this.matrixOriginValues = new float[9];
        this.savedMatrix = new Matrix();
        this.fMinZoomLevel = 0.5f;
        this.fMaxZoomLevel = 2.0f;
        init();
    }

    private void init() {
        this.mScaleHandler = ScaleHandler.getInstance(getContext(), this);
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        this.matrixOrigin.getValues(this.matrixOriginValues);
    }

    public boolean onTouchEvent(MotionEvent event) {
        this.mScaleHandler.handleTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (Boolean.FALSE.equals(this.scaling)) {
            this.scaling = null;
            return false;
        } 
        else if (this.scaling == null) {
            return super.onScroll(e1, e2, distanceX, distanceY);
        } else {
            return false;
        }
    }

    public boolean onScale(float scaleFactor, float focusX, float focusY) {
        if (!zooming()) {
            this.savedMatrix.set(this.matrix);
            this.matrix.postScale(scaleFactor, scaleFactor, focusX, focusY);
            this.matrix.getValues(this.matrixValues);
            float maxValue = this.matrixOriginValues[0] * (this.fMaxZoomLevel + 1.0f);
            if (this.matrixValues[0] < this.matrixOriginValues[0] * (this.fMinZoomLevel * 0.6f) || this.matrixValues[0] > maxValue) {
                this.matrix.set(this.savedMatrix);
            } 
            else {
                invalidate();
            }
        }
        return true;
    }

    public boolean onScaleBegin(float scaleFactor, float focusX, float focusY) {
        if (DEBUG.booleanValue()) {
            Log.d(TAG, "Scale Begin");
        }
        this.mapListener.onSingleTapCancelled();
        this.mapListener.onTouchScale(scaleFactor, focusX, focusY);
        this.scaling = Boolean.valueOf(true);
        return true;
    }

    public void onScaleEnd(float scaleFactor, float focusX, float focusY) {
        if (DEBUG.booleanValue()) {
            Log.d(TAG, "Scale End");
        }
        updateDiffRect();
        this.matrix.getValues(this.matrixValues);
        this.scaling = Boolean.valueOf(false);
        if (this.matrixValues[0] < this.matrixOriginValues[0] * this.fMinZoomLevel) {
            this.mapScaleAnim = new ZoomScaleAnim(this.matrix, (float) (getWidth() / 2), (float) (getHeight() / 2), (float) (getWidth() / 2), (float) (getHeight() / 2), (this.matrixOriginValues[0] / this.matrixValues[0]) * this.fMinZoomLevel, (int) getZoomDuration());
        } else if (this.matrixValues[0] > this.matrixOriginValues[0] * this.fMaxZoomLevel) {
            this.mapScaleAnim = new ZoomScaleAnim(this.matrix, (float) (getWidth() / 2), (float) (getHeight() / 2), (float) (getWidth() / 2), (float) (getHeight() / 2), (this.matrixOriginValues[0] / this.matrixValues[0]) * this.fMaxZoomLevel, (int) getZoomDuration());
        } else {
            return;
        }
        this.mapScaleAnim.setInterpolator(overshootInterpolator);
        startZoomAnimation();
    }
}
