package com.volokh.danylo.midpointcircle;


import android.graphics.Paint;

/**
 * Created by danylo.volokh on 12/18/2015.
 */
public class Point extends android.graphics.Point{

    private final Paint paint;

    public Point(int x, int y, Paint paint){
        super(x, y);
        this.paint = paint;
    }

    public Paint getPaint(){
        return paint;
    }
}
