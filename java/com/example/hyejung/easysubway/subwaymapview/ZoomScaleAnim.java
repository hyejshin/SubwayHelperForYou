package com.example.hyejung.easysubway.subwaymapview;

import android.graphics.Matrix;
import android.graphics.RectF;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

public class ZoomScaleAnim extends AnimationSet {
    private float[] iMatrixs;
    private final int mDuration;
    private final Matrix mTargetMatrix;
    private ScaleAnimation scaleAnimation;
    private float[] tMatrixs;
    private TranslateAnimation translateAnimation;

    private void set(float fromScale, float toScale, float fromX, float toX, float fromY, float toY) {
        if (!getAnimations().isEmpty()) {
            getAnimations().clear();
            reset();
        }
        this.scaleAnimation = new ScaleAnimation(fromScale, toScale, fromScale, toScale);
        this.scaleAnimation.setDuration((long) this.mDuration);
        addAnimation(this.scaleAnimation);
        this.translateAnimation = new TranslateAnimation(fromX, toX, fromY, toY);
        this.translateAnimation.setDuration((long) this.mDuration);
        addAnimation(this.translateAnimation);
        setFillAfter(true);
    }

    public ZoomScaleAnim(Matrix start, Matrix target, int duration) {
        super(true);
        this.iMatrixs = new float[9];
        this.tMatrixs = new float[9];
        this.mTargetMatrix = target;
        this.mDuration = duration;
        start.getValues(this.iMatrixs);
        target.getValues(this.tMatrixs);
    }

    public ZoomScaleAnim(Matrix start, float x, float y, float scale, int duration) {
        super(true);
        this.iMatrixs = new float[9];
        this.tMatrixs = new float[9];
        this.mDuration = duration;
        start.getValues(this.iMatrixs);
        translateAxis(2, x, x, scale);
        translateAxis(5, y, y, scale);
        this.mTargetMatrix = new Matrix();
        this.mTargetMatrix.setScale(this.iMatrixs[0] * scale, this.iMatrixs[4] * scale);
        this.mTargetMatrix.postTranslate(this.tMatrixs[2], this.tMatrixs[5]);
    }

    public ZoomScaleAnim(Matrix start, float x, float y, float toX, float toY, float scale, int duration) {
        super(true);
        this.iMatrixs = new float[9];
        this.tMatrixs = new float[9];
        this.mDuration = duration;
        start.getValues(this.iMatrixs);
        translateAxis(2, x, toX, scale);
        translateAxis(5, y, toY, scale);
        this.mTargetMatrix = new Matrix();
        this.mTargetMatrix.setScale(this.iMatrixs[0] * scale, this.iMatrixs[4] * scale);
        this.mTargetMatrix.postTranslate(this.tMatrixs[2], this.tMatrixs[5]);
    }

    private void translateAxis(int direction, float from, float to, float scale) {
        this.tMatrixs[direction] = (scale * this.iMatrixs[direction]) + (to - (from * scale));
    }

    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        RectF mapRect = new RectF(0.0f, 0.0f, (float) width, (float) height);
        this.mTargetMatrix.mapRect(mapRect);
        if (mapRect.width() <= ((float) parentWidth)) {
            this.mTargetMatrix.postTranslate((((float) (parentWidth / 2)) - mapRect.left) - (mapRect.width() / 2.0f), 0.0f);
        } 
        else if (mapRect.left > 0.0f) {
            this.mTargetMatrix.postTranslate(-mapRect.left, 0.0f);
        } 
        else if (mapRect.right < ((float) parentWidth)) {
            this.mTargetMatrix.postTranslate(((float) parentWidth) - mapRect.right, 0.0f);
        }
        if (mapRect.height() <= ((float) parentHeight)) {
            this.mTargetMatrix.postTranslate(0.0f, (((float) (parentHeight / 2)) - mapRect.top) - (mapRect.height() / 2.0f));
        } 
        else if (mapRect.top > 0.0f) {
            this.mTargetMatrix.postTranslate(0.0f, -mapRect.top);
        } 
        else if (mapRect.bottom < ((float) parentHeight)) {
            this.mTargetMatrix.postTranslate(0.0f, ((float) parentHeight) - mapRect.bottom);
        }
        this.mTargetMatrix.getValues(this.tMatrixs);
        set(this.iMatrixs[0], this.tMatrixs[0], this.iMatrixs[2], this.tMatrixs[2], this.iMatrixs[5], this.tMatrixs[5]);
        super.initialize(width, height, parentWidth, parentHeight);
    }
}
