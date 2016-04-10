package com.example.hyejung.easysubway.subwaymapview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Matrix.ScaleToFit;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.widget.ImageView;

import com.example.hyejung.easysubway.DBmodel.AppDataManager;
import com.example.hyejung.easysubway.DBmodel.PointDataManager;
import com.example.hyejung.easysubway.R;
import java.io.IOException;



public class ZoomImageView extends ImageView implements Callback {
    protected static final int ANIM_CONTINUE = 1;
    protected static final int ANIM_START = 0;
    protected static final int ANIM_STOP = 2;
    protected static Boolean DEBUG = Boolean.valueOf(false);
    protected static final String TAG = ZoomImageView.class.getSimpleName();
    private Bitmap finishImage;
    private float mMaxZoomLevel;
    private float mMidZoomLevel;
    private float mMinZoomLevel;
    private float mOidZoomLevel;
    private int mZoomDuration;
    private Bitmap map;
    private Paint mapPaint;
    protected Animation mapScaleAnim;
    Handler mapZoomHandler;
    protected Matrix matrix;
    protected Matrix matrixOrigin;
    private float[] matrixOriginValues;
    private float[] matrixValues;
    private Bitmap passImage;
    private Bitmap pathImage1;
    private float pointImageSize;
    PointDataManager pointManager;
    protected RectF rectMapOrigin;
    protected RectF rectView;
    protected Boolean scaling;
    private Bitmap selectedImage;
    private float selectedImageSize;
    private Bitmap startImage;
    private RectF tmpRect;
    private Transformation transform;

    public ZoomImageView(Context context) {
        this(context, null);
    }

    public ZoomImageView(Context context, AttributeSet attr) {
        this(context, attr, ANIM_START);
    }

    public ZoomImageView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
        this.rectMapOrigin = new RectF();
        this.rectView = new RectF();
        this.matrixOrigin = new Matrix();
        this.matrix = new Matrix();
        this.scaling = null;
        this.tmpRect = new RectF();
        this.matrixOriginValues = new float[9];
        this.matrixValues = new float[9];
        this.pathImage1 = null;
        this.selectedImage = null;
        this.startImage = null;
        this.finishImage = null;
        this.passImage = null;
        this.selectedImageSize = 0.0f;
        this.pointImageSize = 25.0f;
        this.mapZoomHandler = new Handler(this);
        super.setScaleType(ScaleType.MATRIX);
        this.pointManager = PointDataManager.shared();
        this.mapPaint = new Paint();
        this.mapPaint.setFilterBitmap(true);
        this.transform = new Transformation();
        float scale = getContext().getResources().getDisplayMetrics().density;
        this.mMaxZoomLevel = 1.4f * scale;
        this.mMidZoomLevel = 0.75f * scale;
        this.mMinZoomLevel = 0.3f * scale;
        this.mZoomDuration = 200;
        this.mOidZoomLevel = this.mMidZoomLevel;
        setMapImage(context);
        this.selectedImage = BitmapFactory.decodeResource(getResources(), R.drawable.img_mapicon_selected);
        this.selectedImage = Bitmap.createScaledBitmap(this.selectedImage, 54, 54, true);
        this.startImage = BitmapFactory.decodeResource(getResources(), R.drawable.img_mapicon_start);
        this.startImage = Bitmap.createScaledBitmap(this.startImage, 50, 50, true);
        this.finishImage = BitmapFactory.decodeResource(getResources(), R.drawable.img_mapicon_finish);
        this.finishImage = Bitmap.createScaledBitmap(this.finishImage, 50, 50, true);
        this.passImage = BitmapFactory.decodeResource(getResources(), R.drawable.img_mapicon_pass);
        this.passImage = Bitmap.createScaledBitmap(this.passImage, 50, 50, true);
        this.pathImage1 = BitmapFactory.decodeResource(getResources(), R.drawable.img_mapicon_path01);
        this.pathImage1 = Bitmap.createScaledBitmap(this.pathImage1, 48, 48, true);
        this.selectedImageSize = ((float) this.selectedImage.getWidth()) * 0.5f;
    }

    private void setMapImage(Context context) {
        try {
            Options options = new Options();
            options.inPreferredConfig = Config.ARGB_8888;
            setMap(BitmapFactory.decodeStream(context.getAssets().open(AppDataManager.shared().getMapImage()), null, options));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void resetMapImage(Context context) {
        setMapImage(context);
        int areaCode = AppDataManager.shared().getAreaCode();
        float pointX = AppDataManager.shared().getMySharedPreferences("LastSelectedPointX" + areaCode, 0.0f);
        float pointY = AppDataManager.shared().getMySharedPreferences("LastSelectedPointY" + areaCode, 0.0f);
        if (pointX <= 0.0f) {
            pointX = this.rectMapOrigin.right * 0.5f;
            pointY = this.rectMapOrigin.bottom * 0.5f;
        }
        moveMapPointAction(pointX, pointY, 0.0f, this.mMidZoomLevel, 300);
    }

    public void setMap(Bitmap bmp) {
        this.map = bmp;
        this.rectMapOrigin.set(0.0f, 0.0f, (float) this.map.getWidth(), (float) this.map.getHeight());
        requestLayout();
    }

    public boolean hasMap() {
        return this.map != null;
    }

    public void zoomOnScreen(float x, float y) {
        float targetZoomLevel;
        if (this.mMaxZoomLevel <= getCurrentZoomLevel() + 0.5f || this.mMidZoomLevel >= getCurrentZoomLevel() + 0.3f) {
            targetZoomLevel = this.mMidZoomLevel / getCurrentZoomLevel();
        } else {
            targetZoomLevel = this.mMaxZoomLevel / getCurrentZoomLevel();
        }
        this.mapScaleAnim = new ZoomScaleAnim(this.matrix, x, y, (float) (getWidth() / ANIM_STOP), (float) (getHeight() / ANIM_STOP), targetZoomLevel, this.mZoomDuration);
        startZoomAnimation();
    }

    protected void startZoomAnimation() {
        this.mapScaleAnim.initialize((int) this.rectMapOrigin.width(), (int) this.rectMapOrigin.height(), getWidth(), getHeight());
        this.mapScaleAnim.startNow();
        Message.obtain(this.mapZoomHandler, ANIM_START).sendToTarget();
    }

    public void zoomDefault() {
        zoomDefault(this.mZoomDuration);
    }

    public void zoomDefault(int duration) {
        this.mapScaleAnim = new ZoomScaleAnim(this.matrix, this.matrixOrigin, duration);
        startZoomAnimation();
    }

    public void zoomToggle(float onX, float onY) {
        zoomOnScreen(onX, onY);
    }

    public void selectedToggle(float onX, float onY, float ptX, float ptY) {
        float currentZoomLevel = getCurrentZoomLevel();
        if (this.mOidZoomLevel != currentZoomLevel && this.mMaxZoomLevel > 0.1f + currentZoomLevel) {
            this.mOidZoomLevel = currentZoomLevel;
        }
        float f = onX;
        float f2 = onY;
        this.mapScaleAnim = new ZoomScaleAnim(this.matrix, f, f2, (float) (getWidth() / ANIM_STOP), (float) (getHeight() / ANIM_STOP), this.mMaxZoomLevel / currentZoomLevel, this.mZoomDuration);
        startZoomAnimation();
    }

    public void closeMenuAtcion() {
        this.mapScaleAnim = new ZoomScaleAnim(this.matrix, (float) (getWidth() / ANIM_STOP), (float) (getHeight() / ANIM_STOP), this.mOidZoomLevel / getCurrentZoomLevel(), this.mZoomDuration);
        startZoomAnimation();
    }

    public void moveMapPointAction(float ptX, float ptY, float zoomLevel) {
        moveMapPointAction(ptX, ptY, 83.0f, zoomLevel, this.mZoomDuration);
    }

    public void moveMapPointZoomAction(float ptX, float ptY, float zoomScale) {
        moveMapPointAction(ptX, ptY, 83.0f, getContext().getResources().getDisplayMetrics().density * zoomScale, this.mZoomDuration);
    }

    public void moveMapPointAction(float ptX, float ptY, float addOn, float zoomLevel, int duration) {
        if (this.mMaxZoomLevel < zoomLevel) {
            zoomLevel = this.mMaxZoomLevel;
        } else if (this.mMinZoomLevel > zoomLevel) {
            zoomLevel = this.mMinZoomLevel;
        }
        float currentZoomLevel = getCurrentZoomLevel();
        this.mapScaleAnim = new ZoomScaleAnim(this.matrix, (ptX * currentZoomLevel) - getCurrentTransX(), ((ptY - addOn) * currentZoomLevel) - getCurrentTransY(), (float) (getWidth() / ANIM_STOP), (float) (getHeight() / ANIM_STOP), zoomLevel / currentZoomLevel, duration);
        startZoomAnimation();
    }

    public float getCurrentZoomLevel() {
        this.matrix.getValues(this.matrixValues);
        return this.matrixValues[ANIM_START];
    }

    public float getCurrentTransX() {
        this.matrix.getValues(this.matrixValues);
        return this.matrixValues[ANIM_STOP] * -1.0f;
    }

    public float getCurrentTransY() {
        this.matrix.getValues(this.matrixValues);
        return this.matrixValues[5] * -1.0f;
    }

    protected float getZoomDuration() {
        return (float) this.mZoomDuration;
    }

    protected float getMinZoomLevel() {
        return this.mMinZoomLevel;
    }

    protected float getMidZoomLevel() {
        return this.mMidZoomLevel;
    }

    protected float getMaxZoomLevel() {
        return this.mMaxZoomLevel;
    }

    public void setZoomLevelAnimation(float zoomLevel) {
        this.mapScaleAnim = new ZoomScaleAnim(this.matrix, (float) (getWidth() / ANIM_STOP), (float) (getHeight() / ANIM_STOP), zoomLevel, this.mZoomDuration);
        startZoomAnimation();
    }

    public void oidZoomLevel() {
        this.mapScaleAnim = new ZoomScaleAnim(this.matrix, (float) (getWidth() / ANIM_STOP), (float) (getHeight() / ANIM_STOP), this.mOidZoomLevel / getCurrentZoomLevel(), this.mZoomDuration);
        startZoomAnimation();
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (this.map != null) {
            if (this.mapScaleAnim != null && changed) {
                this.mapZoomHandler.sendMessageAtFrontOfQueue(Message.obtain(this.mapZoomHandler, ANIM_STOP));
            }
            this.tmpRect.set(this.rectView);
            this.rectView.set(0.0f, 0.0f, (float) (right - left), (float) (bottom - top));
            resetOrigin();
            if (this.matrix.isIdentity() || this.tmpRect.isEmpty()) {
                resetPosition();
                return;
            }
            this.matrix.getValues(this.matrixValues);
            float[] fArr = this.matrixValues;
            fArr[ANIM_STOP] = fArr[ANIM_STOP] + ((this.rectView.width() - this.tmpRect.width()) / 2.0f);
            fArr = this.matrixValues;
            fArr[5] = fArr[5] + ((this.rectView.height() - this.tmpRect.height()) / 2.0f);
            this.matrix.setValues(this.matrixValues);
        }
    }

    private void resetOrigin() {
        if (this.rectView.isEmpty()) {
            this.matrixOrigin.reset();
            return;
        }
        float pointX = 0.0f;
        float pointY = 0.0f;
        if (AppDataManager.shared().getMapStartPointType() != 0) {
            int areaCode = AppDataManager.shared().getAreaCode();
            pointX = AppDataManager.shared().getMySharedPreferences("LastSelectedPointX" + areaCode, 0.0f);
            pointY = AppDataManager.shared().getMySharedPreferences("LastSelectedPointY" + areaCode, 0.0f);
        }
        if (pointX > 0.0f) {
            pointX = this.rectView.centerX() - pointX;
            pointY = this.rectView.centerY() - pointY;
        } else {
            pointX = this.rectView.centerX() - (this.rectMapOrigin.right * 0.5f);
            pointY = this.rectView.centerY() - (this.rectMapOrigin.bottom * 0.5f);
        }
        this.matrixOrigin.setRectToRect(this.rectMapOrigin, this.rectMapOrigin, ScaleToFit.CENTER);
        this.matrixOrigin.getValues(this.matrixOriginValues);
        this.matrixOrigin.setScale(1.0f, 1.0f);
        this.matrixOrigin.setTranslate(pointX, pointY);
        float px = this.rectView.centerX() * (this.mMidZoomLevel - 1.0f);
        float py = this.rectView.centerY() * (this.mMidZoomLevel - 1.0f);
        this.matrixOrigin.postScale(this.mMidZoomLevel, this.mMidZoomLevel);
        this.matrixOrigin.postTranslate(-px, -py);
    }

    public void resetPosition() {
        this.matrix.set(this.matrixOrigin);
    }

    public void mapReDraw() {
        invalidate();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.concat(this.matrix);
        if (this.map != null) {
            Bundle bundle;
            float pointX;
            float pointY;
            canvas.drawBitmap(this.map, 0.0f, 0.0f, this.mapPaint);
            if (this.pointManager.getSelectedData() != null) {
                bundle = this.pointManager.getSelectedData();
                pointX = Float.parseFloat(bundle.getString("pointX", "0")) - this.selectedImageSize;
                pointY = Float.parseFloat(bundle.getString("pointY", "0")) - this.selectedImageSize;
                canvas.drawBitmap(this.selectedImage, pointX, pointY, this.mapPaint);
            }
            if (this.pointManager.getStartData() != null) {
                bundle = this.pointManager.getStartData();
                pointX = Float.parseFloat(bundle.getString("pointX", "0")) - this.pointImageSize;
                pointY = Float.parseFloat(bundle.getString("pointY", "0")) - this.pointImageSize;
                canvas.drawBitmap(this.startImage, pointX, pointY, this.mapPaint);
            }
            if (this.pointManager.getFinishData() != null) {
                bundle = this.pointManager.getFinishData();
                pointX = Float.parseFloat(bundle.getString("pointX", "0")) - this.pointImageSize;
                pointY = Float.parseFloat(bundle.getString("pointY", "0")) - this.pointImageSize;
                canvas.drawBitmap(this.finishImage, pointX, pointY, this.mapPaint);
            }
            if (this.pointManager.getPassData() != null) {
                bundle = this.pointManager.getPassData();
                pointX = Float.parseFloat(bundle.getString("pointX", "0")) - this.pointImageSize;
                pointY = Float.parseFloat(bundle.getString("pointY", "0")) - this.pointImageSize;
                canvas.drawBitmap(this.passImage, pointX, pointY, this.mapPaint);
            }

            canvas.restore();
        }
    }

    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case ANIM_START /*0*/:
                if (DEBUG.booleanValue()) {
                    Log.d(TAG, "Animation Start");
                }
                msg.getTarget().removeMessages(ANIM_START);
                msg.getTarget().removeMessages(ANIM_CONTINUE);
                onAnimationStart();
                break;
            case ANIM_CONTINUE /*1*/:
                break;
            case ANIM_STOP /*2*/:
                msg.getTarget().removeMessages(ANIM_CONTINUE);
                onAnimationEnd();
                if (DEBUG.booleanValue()) {
                    Log.d(TAG, "Animation End");
                    invalidate();
                    break;
                }
                break;
        }
        msg.getTarget().sendEmptyMessage(this.mapScaleAnim.hasEnded() ? ANIM_STOP : ANIM_CONTINUE);
        this.mapScaleAnim.getTransformation(AnimationUtils.currentAnimationTimeMillis(), this.transform);
        this.matrix.set(this.transform.getMatrix());
        invalidate();
        return true;
    }
}
