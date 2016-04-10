package com.example.hyejung.easysubway;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

public class CustomView extends View {
    Map line = new HashMap();
    Map lineColor = new HashMap();

    public CustomView(Context context, AttributeSet attrs){
        super(context);
        setBackgroundColor(Color.parseColor("#4C4C4C"));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        setupSubwayLine();
        setupColor();

        Paint paint = new Paint();
        paint.setTextSize(30);
        String id, color;
        int x, y, x2, y2, r, diff, count, margin = 0;

        DrawInfo info = new DrawInfo();
        count = info.getTotalTransfer();

        x = 170 + margin; y = 120; x2 = 330 + margin; y2 = 270; r = 30; diff = 200;
        for(int i=0; i<count-1; i++) {
            id = info.getId(i);
            color = lineColor.get(id).toString();
            paint.setColor(Color.parseColor(color));
            canvas.drawRoundRect(new RectF(x, y, x2, y2), r, r, paint);
            x += diff; x2 += diff;
        }

        paint.setColor(Color.WHITE);
        x = 230; y = 250; diff = 200;
        for(int i=0; i<count-2; i++){
            canvas.drawText(info.getCartNo(i), x, y, paint);
            x += diff;
        }

        x = 150 + margin; y = 200; r = 60; diff = 200;
        for(int i=0; i<count; i++){
            canvas.drawCircle(x, y, r, paint);
            x += diff;
        }

        paint.setColor(Color.BLACK);
        x = 130 + margin; y = 210; diff = 200;
        for(int i=0; i<count; i++){
            int extra = 0;
            for(int j=0; j<info.getName(i).length()-2; j++)
                extra += 10;
            canvas.drawText(info.getName(i), x-extra, y, paint);
            x += diff;
            extra = 0;
        }

        paint.setColor(Color.WHITE);
        x = 220 + margin; y = 150; diff = 200;
        for(int i=0; i<count-1; i++){
            id = info.getId(i);
            canvas.drawText(line.get(id).toString(), x, y, paint);
            x += diff;
        }

        canvas.drawRect(30, 350, 120, 400, paint);
        canvas.drawRect(30, 410, 120, 460, paint);

        paint.setColor(Color.BLACK);
        canvas.drawText("도착", 40, 390, paint);
        canvas.drawText("출발", 40, 450, paint);

        paint.setColor(Color.WHITE);
        x = 130; y = 350; x2 = 280; y2 = 400; diff = 160;

        for(int i=0; i<count; i++) {
            canvas.drawRect(x, y, x2, y2, paint);
            canvas.drawRect(x, y + 60, x2, y2 + 60, paint);
            x += diff; x2 += diff;
        }

       x = 170; y = 390; diff = 160;
        paint.setColor(Color.BLACK);
        canvas.drawText(info.getStartTime(0), x, y + 60, paint);
        x += diff;
        for(int i=1; i<count-1; i++) {
            canvas.drawText(info.getArriveTime(i - 1), x, y, paint);
            canvas.drawText(info.getStartTime(i), x, y + 60, paint);
            x += diff;
        }
        canvas.drawText(info.getArriveTime(count-2), x, y, paint);
    }

    public void setupSubwayLine(){
        line.put("1001", "1호선");
        line.put("1002", "2호선");
        line.put("1003", "3호선");
        line.put("1004", "4호선");
        line.put("1005", "5호선");
        line.put("1006", "6호선");
        line.put("1007", "7호선");
        line.put("1008", "8호선");
        line.put("1009", "9호선");
        line.put("1061", "중앙선");
        line.put("1063", "중앙선");
        line.put("1065", "공항철도");
        line.put("1077", "신분당선");
    }

    public void setupColor(){
        lineColor.put("1001", "#00498B");
        lineColor.put("1002", "#009246");
        lineColor.put("1003", "#F36630");
        lineColor.put("1004", "#00A2D1");
        lineColor.put("1005", "#A064A3");
        lineColor.put("1006", "#9E4510");
        lineColor.put("1007", "#5D6519");
        lineColor.put("1008", "#D6406A");
        lineColor.put("1009", "#A17E46");
        lineColor.put("1061", "#72C7A6");
        lineColor.put("1063", "#72C7A6");
        lineColor.put("1065", "#0065B3");
        lineColor.put("1077", "#BB1833");
    }
}
