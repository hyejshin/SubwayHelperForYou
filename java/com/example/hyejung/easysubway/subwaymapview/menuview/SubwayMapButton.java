package com.example.hyejung.easysubway.subwaymapview.menuview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

public class SubwayMapButton extends ImageButton {
    private int buttonIndex = 0;
    private int menuIndex = 0;
    private float offsetX = 0.0f;
    private int oldOffsetX = 0;

    public SubwayMapButton(Context context) {
        super(context);
    }

    public SubwayMapButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SubwayMapButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOffsetX(float x) {
        this.offsetX = x;
    }

    public float getOffsetX() {
        return this.offsetX;
    }

    public int getOldOffsetX() {
        return this.oldOffsetX;
    }

    public void setOldOffsetX(int oldOffsetX) {
        this.oldOffsetX += oldOffsetX;
    }

    public int getMenuIndex() {
        return this.menuIndex;
    }

    public void setMenuIndex(int menuIndex) {
        this.menuIndex = menuIndex;
    }

    public int getButtonIndex() {
        return this.buttonIndex;
    }

    public void setButtonIndex(int buttonIndex) {
        this.buttonIndex = buttonIndex;
    }
}
