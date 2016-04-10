package com.example.hyejung.easysubway.subwaymapview.menuview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

public class SubwaySelectedButton extends ImageButton {
    private int menuIndex = 0;
    private float offsetX = 0.0f;
    private float offsetY = 0.0f;
    private int oldOffsetX = 0;
    private int oldOffsetY = 0;

    public SubwaySelectedButton(Context context) {
        super(context);
    }

    public SubwaySelectedButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SubwaySelectedButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOffset(double x, double y) {
        this.offsetX = (float)x;
        this.offsetY = (float)y;
    }

    public float getOffsetX() {
        return this.offsetX;
    }

    public float getOffsetY() {
        return this.offsetY;
    }

    public int getOldOffsetX() {
        return this.oldOffsetX;
    }

    public void setOldOffsetX(int oldOffsetX) {
        this.oldOffsetX += oldOffsetX;
    }

    public int getOldOffsetY() {
        return this.oldOffsetY;
    }

    public void setOldOffsetY(int oldOffsetY) {
        this.oldOffsetY += oldOffsetY;
    }

    public int getMenuIndex() {
        return this.menuIndex;
    }

    public void setMenuIndex(int menuIndex) {
        this.menuIndex = menuIndex;
    }
}
