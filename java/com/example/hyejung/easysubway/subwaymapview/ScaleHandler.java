package com.example.hyejung.easysubway.subwaymapview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;

@TargetApi(4)
public abstract class ScaleHandler {

    public static class ScaleHandlerDonut extends ScaleHandler {
        public boolean handleTouchEvent(MotionEvent event) {
            return false;
        }
    }

    @TargetApi(8)
    public static class ScaleHandlerFroyo extends ScaleHandler {
        private ScaleGestureDetector mScaleDetector;
        private final ScaleListener mScaleListener;

        public ScaleHandlerFroyo(Context c, ScaleListener listener) {
            this.mScaleListener = listener;
            this.mScaleDetector = new ScaleGestureDetector(c, new OnScaleGestureListener() {
                public void onScaleEnd(ScaleGestureDetector detector) {
                    ScaleHandlerFroyo.this.mScaleListener.onScaleEnd(detector.getScaleFactor(), detector.getFocusX(), detector.getFocusY());
                }

                public boolean onScaleBegin(ScaleGestureDetector detector) {
                    return ScaleHandlerFroyo.this.mScaleListener.onScaleBegin(detector.getScaleFactor(), detector.getFocusX(), detector.getFocusY());
                }

                public boolean onScale(ScaleGestureDetector detector) {
                    return ScaleHandlerFroyo.this.mScaleListener.onScale(detector.getScaleFactor(), detector.getFocusX(), detector.getFocusY());
                }
            });
        }

        public boolean handleTouchEvent(MotionEvent event) {
            return this.mScaleDetector.onTouchEvent(event);
        }
    }

    public interface ScaleListener {
        boolean onScale(float f, float f2, float f3);

        boolean onScaleBegin(float f, float f2, float f3);

        void onScaleEnd(float f, float f2, float f3);
    }

    public abstract boolean handleTouchEvent(MotionEvent motionEvent);

    public static ScaleHandler getInstance(Context c, ScaleListener listener) {
        if (VERSION.SDK_INT >= 14) {
            return new ScaleHandlerFroyo(c, listener);
        }
        return new ScaleHandlerDonut();
    }
}
