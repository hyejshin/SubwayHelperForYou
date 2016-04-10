package com.example.hyejung.easysubway.subwaymapview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Matrix.ScaleToFit;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;

import com.example.hyejung.easysubway.DBmodel.AppDataManager;
import com.example.hyejung.easysubway.DBmodel.DBHandler;
import com.example.hyejung.easysubway.subwaymapview.scroller.ScrollDelegate;
import com.example.hyejung.easysubway.subwaymapview.scroller.ScrollOver;

import java.util.ArrayList;

public class ZoomImageViewTouchable extends ZoomImageView implements OnDoubleTapListener, OnGestureListener {
    private static final OverScrollListener NULL_OVERSCROLL_LISTENER = new OverScrollListener() {
        public void onOverscrollX(float f) {
            if (ZoomImageViewTouchable.DEBUG.booleanValue()) {
                Log.v(ZoomImageViewTouchable.TAG, "OverScroll X: " + f);
            }
        }

        public void onOverscrollY(float f) {
            if (ZoomImageViewTouchable.DEBUG.booleanValue()) {
                Log.v(ZoomImageViewTouchable.TAG, "OverScroll Y: " + f);
            }
        }
    };
    private static final TouchMapListener NULL_TOUCHMAP_LISTENER = new TouchMapListener() {
        public void onTouchScale(float scaleFactor, float foxusX, float focusY) {
        }

        public void onTouch(float x, float y) {
        }

        public void onSingleTapConfirmed() {
        }

        public void onSingleTapCancelled() {
        }

        public void onDoubleTap(float eventX, float eventY) {
        }
    };
    private static final TouchStationListener NULL_TOUCH_STATION_LISTENER = new TouchStationListener() {
        public void onSelectedStation(Bundle data) {
        }
    };
    private static final String TAG = ZoomImageViewTouchable.class.getSimpleName();
    private DecelerateInterpolator decelerateInterpolator;
    private GestureDetector gestureScanner;
    private boolean mDoubleTapZoom;
    private OverScrollListener mOverScrollListener;
    private ScrollDelegate mOverScroller;
    protected TouchMapListener mapListener;
    private Matrix matrixTranslate;
    private float mflingScale;
    boolean movedX;
    boolean movedY;
    private RectF rectMap;
    private RectF rectMapUpdate;
    protected TouchStationListener stationListener;

    public interface TouchStationListener {
        void onSelectedStation(Bundle bundle);
    }

    public interface OverScrollListener {
        void onOverscrollX(float f);

        void onOverscrollY(float f);
    }

    public interface TouchMapListener {
        void onDoubleTap(float f, float f2);

        void onSingleTapCancelled();

        void onSingleTapConfirmed();

        void onTouch(float f, float f2);

        void onTouchScale(float f, float f2, float f3);
    }

    public ZoomImageViewTouchable(Context context) {
        this(context, null);
    }

    public ZoomImageViewTouchable(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomImageViewTouchable(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mapListener = NULL_TOUCHMAP_LISTENER;
        this.stationListener = NULL_TOUCH_STATION_LISTENER;
        this.mOverScrollListener = NULL_OVERSCROLL_LISTENER;
        this.rectMap = new RectF();
        this.rectMapUpdate = new RectF();
        this.matrixTranslate = new Matrix();
        this.gestureScanner = new GestureDetector(getContext(), this);
        this.gestureScanner.setOnDoubleTapListener(this);
        this.mDoubleTapZoom = true;
        this.mflingScale = 5.0f;
        this.decelerateInterpolator = new DecelerateInterpolator(this.mflingScale);
        this.mOverScroller = new ScrollOver(1.0f);
   
    }

    public void setOverScrollListener(OverScrollListener listener) {
        if (this.mOverScrollListener == null) {
            this.mOverScrollListener = NULL_OVERSCROLL_LISTENER;
        } 
        else {
            this.mOverScrollListener = listener;
        }
    }

    public void setTouchMapListener(TouchMapListener mapListener) {
        if (mapListener == null) {
            this.mapListener = NULL_TOUCHMAP_LISTENER;
        } else {
            this.mapListener = mapListener;
        }
    }

    public void setTouchStationListener(TouchStationListener listener) {
        if (listener == null) {
            this.stationListener = NULL_TOUCH_STATION_LISTENER;
        } else {
            this.stationListener = listener;
        }
    }

    public static float distance(PointF p, PointF q) {
        return distance(p.x, p.y, q.x, q.y);
    }

    public static float distance(float x0, float y0, float x1, float y1) {
        float dx = x0 - x1;
        float dy = y0 - y1;
        return (float)Math.sqrt((dx * dx) + (dy * dy));
    }

    public static boolean equalsRect(RectF rect1, RectF rect2) {
        return rect1.left == rect2.left && rect1.right == rect2.right && rect1.top == rect2.top && rect1.bottom == rect2.bottom;
    }

    public boolean onDown(MotionEvent e) {
        if (DEBUG.booleanValue()) {
            Log.v(TAG, "onDown");
        }
        this.mapListener.onTouch(e.getX(), e.getY());
        updateDiffRect();
        this.mapZoomHandler.removeMessages(1);
        return true;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == 1) {
            if (DEBUG.booleanValue()) {
                Log.v(TAG, "Action_Up");
            }
            if (this.movedX || this.movedY) {
                restoreInitialPosition();
            }
        }
        return this.gestureScanner.onTouchEvent(event);
    }

    private void restoreInitialPosition() {
        updateDiffRect();
        if (this.rectMap.width() < ((float) getWidth())) {
            this.rectMapUpdate.offset((-this.rectMap.centerX()) + this.rectView.centerX(), 0.0f);
        } 
        else if (this.rectMap.left > 0.0f) {
            this.rectMapUpdate.offset(-this.rectMap.left, 0.0f);
        } 
        else if (this.rectMap.right < ((float) getWidth())) {
            this.rectMapUpdate.offset(((float) getWidth()) - this.rectMap.right, 0.0f);
        }
        if (this.rectMap.height() < ((float) getHeight())) {
            this.rectMapUpdate.offset(0.0f, (-this.rectMap.centerY()) + this.rectView.centerY());
        }
        else if (this.rectMap.top > 0.0f) {
            this.rectMapUpdate.offset(0.0f, -this.rectMap.top);
        }
        else if (this.rectMap.bottom < ((float) getHeight())) {
            this.rectMapUpdate.offset(0.0f, ((float) getHeight()) - this.rectMap.bottom);
        }
        if (!equalsRect(this.rectMapUpdate, this.rectMap)) {
            this.matrixTranslate.setRectToRect(this.rectMapOrigin, this.rectMapUpdate, ScaleToFit.FILL);
            this.mapScaleAnim = new ZoomScaleAnim(this.matrix, this.matrixTranslate, 200);
            startZoomAnimation();
        }
    }

    public boolean onDoubleTap(MotionEvent e) {
        if (DEBUG.booleanValue()) {
            Log.v(TAG, "onDoubleTap");
        }
        this.mapListener.onDoubleTap(e.getX(), e.getY());
        if (this.mDoubleTapZoom) {
            zoomToggle(e.getX(), e.getY());
        }
        return false;
    }

    public boolean onDoubleTapEvent(MotionEvent e) {
        if (DEBUG.booleanValue()) {
            Log.v(TAG, "onDoubleTapEvent");
        }
        return false;
    }

    public boolean onSingleTapConfirmed(MotionEvent e) {
        Log.i(getClass().getSimpleName(), "onSingleTapConfirmed");
        this.mapListener.onSingleTapConfirmed();
        checkStationData(e.getX(), e.getY());
        return false;
    }

    private void checkStationData(float x, float y) {
        float zoomLevel = getCurrentZoomLevel();
        float tempZoomLevel = this.rectMapOrigin.right / (this.rectMapOrigin.right * zoomLevel);
        float transX2 = (getCurrentTransX() + x) * tempZoomLevel;
        float transY2 = (getCurrentTransY() + y) * tempZoomLevel;
        ArrayList<Bundle> arrList = DBHandler.shared().getArrStationPoint(transX2, transY2);
        if (arrList.size() > 0) {
            this.stationListener.onSelectedStation((Bundle) arrList.get(checkSelectedStationData(arrList, transX2, transY2)));
            return;
        }
        Log.d(TAG, "\uc5ed\uc5c6\uc74c : " + transX2 + " / " + transY2 + " / " + zoomLevel);
    }

    public void selectedStationData(Bundle bundle) {
        if (bundle != null) {
            float addOn;
            float scale = getContext().getResources().getDisplayMetrics().density;
            if (AppDataManager.shared().getMapTimeInfoFlag().booleanValue()) {
                addOn = 83.0f;
            } else {
                addOn = 11.0f;
            }
            float transX2 = Float.parseFloat(bundle.getString("pointX", "0"));
            float transY2 = Float.parseFloat(bundle.getString("pointY", "0"));
            float zoomLevel = getCurrentZoomLevel();
            selectedToggle((transX2 * zoomLevel) - getCurrentTransX(), ((transY2 + addOn) * zoomLevel) - getCurrentTransY(), transX2, transY2);
        }
    }

    private int checkSelectedStationData(ArrayList<Bundle> array, float ptX, float ptY) {
        int index = 0;
        int iValue = 1000;
        for (int i = 0; i < array.size(); i++) {
            Bundle data = (Bundle) array.get(i);
            int x = Integer.parseInt(data.getString("pointX"));
            x -= (int) ptX;
            int y = Integer.parseInt(data.getString("pointY")) - ((int) ptY);
            if (x < 0) {
                x *= -1;
            }
            if (y < 0) {
                y *= -1;
            }
            int n = x + y;
            if (iValue > n) {
                index = i;
                iValue = n;
            }
        }
        return index;
    }

    protected void updateDiffRect() {
        this.matrix.mapRect(this.rectMap, this.rectMapOrigin);
        this.rectMapUpdate.set(this.rectMap);
    }

    protected void onAnimationEnd() {
        super.onAnimationEnd();
        updateDiffRect();
    }

    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (DEBUG.booleanValue()) {
            Log.v(TAG, "onFling veloxity x:" + velocityX + " velocity y:" + velocityY);
        }
        if (this.movedX || this.movedY) {
            float to = this.mflingScale * 4.0f;
            this.mapScaleAnim = new ZoomScaleAnim(this.matrix, 0.0f, 0.0f, velocityX / to, velocityY / to, 1.0f, 500);
            this.mapScaleAnim.setInterpolator(this.decelerateInterpolator);
            startZoomAnimation();
        }
        return false;
    }

    public void onLongPress(MotionEvent e) {
        if (DEBUG.booleanValue()) {
            Log.d(TAG, "onLongPress");
        }
    }

    public void onShowPress(MotionEvent e) {
        if (DEBUG.booleanValue()) {
            Log.d(TAG, "onShowPress");
        }
    }

    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        this.movedX = false;
        this.movedY = false;
        if (DEBUG.booleanValue()) {
            Log.v(TAG, String.format("onScroll : dist : %f, %f ", new Object[]{Float.valueOf(distanceX), Float.valueOf(distanceY)}));
        }
        if (!zooming()) {
            this.matrix.mapRect(this.rectMap, this.rectMapOrigin);
            if (this.rectView.right + distanceX >= this.rectMap.right && distanceX > 0.0f) {
                this.movedX = this.mOverScroller.onScrollX(this.rectMap, this.rectView, distanceX, this.matrix);
                this.mOverScrollListener.onOverscrollX(this.rectMap.right - Math.min(this.rectMapUpdate.right, this.rectView.right));
            } 
            else if (this.rectView.left + distanceX <= this.rectMap.left && distanceX < 0.0f) {
                this.movedX = this.mOverScroller.onScrollX(this.rectMap, this.rectView, distanceX, this.matrix);
                this.mOverScrollListener.onOverscrollX(this.rectMap.left - Math.max(this.rectMapUpdate.left, this.rectView.left));
            } 
            else if (distanceX != 0.0f) {
                this.matrix.postTranslate(-distanceX, 0.0f);
                this.movedX = true;
            }
            if (this.rectView.bottom + distanceY >= this.rectMap.bottom && distanceY > 0.0f) {
                this.movedY = this.mOverScroller.onScrollY(this.rectMap, this.rectView, distanceY, this.matrix);
                this.mOverScrollListener.onOverscrollY(this.rectMap.bottom - Math.min(this.rectMapUpdate.bottom, this.rectView.bottom));
            } 
            else if (this.rectView.top + distanceY <= this.rectMap.top && distanceY < 0.0f) {
                this.movedY = this.mOverScroller.onScrollY(this.rectMap, this.rectView, distanceY, this.matrix);
                this.mOverScrollListener.onOverscrollY(this.rectMap.top - Math.max(this.rectMapUpdate.top, this.rectView.top));
            } 
            else if (distanceY != 0.0f) {
                this.matrix.postTranslate(0.0f, -distanceY);
                this.movedY = true;
            }
            if (this.movedX || this.movedY) {
                invalidate();
                return true;
            }
        }
        return false;
    }

    boolean zooming() {
        return this.mapZoomHandler.hasMessages(1) || this.mapZoomHandler.hasMessages(0);
    }

    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
