package com.volokh.danylo.midpointcircle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by danylo.volokh on 12/17/2015.
 */
public class Diagram extends View {

    private static final String TAG = Diagram.class.getSimpleName();
    private List<Point> mPoints = new ArrayList<Point>();

    private int mX0;
    private int mY0;

    private final Paint mPaintGreen = new Paint();
    private final Paint mPaintRed = new Paint();
    private final Paint mPaintBlue = new Paint();
    private final Paint mPaintBlack = new Paint();
    private final Paint mPaintYellow = new Paint();
    private final Paint mPaintMagenta = new Paint();
    private final Paint mPaintCyan = new Paint();
    private final Paint mPaintDarkGrey = new Paint();

    private int mVisiblePointsCount;

    {
        mPaintGreen.setColor(Color.GREEN);
        mPaintGreen.setStrokeWidth(2);

        mPaintRed.setColor(Color.RED);
        mPaintRed.setStrokeWidth(2);

        mPaintBlue.setColor(Color.BLUE);
        mPaintBlue.setStrokeWidth(2);

        mPaintBlack.setColor(Color.BLACK);
        mPaintBlack.setStrokeWidth(2);

        mPaintYellow.setColor(Color.YELLOW);
        mPaintYellow.setStrokeWidth(2);

        mPaintMagenta.setColor(Color.MAGENTA);
        mPaintMagenta.setStrokeWidth(2);

        mPaintCyan.setColor(Color.CYAN);
        mPaintCyan.setStrokeWidth(2);

        mPaintDarkGrey.setColor(Color.DKGRAY);
        mPaintDarkGrey.setStrokeWidth(2);
    }

    public void addPoint(Point point){
        mPoints.add(point);
    }

    public void setOrigin(int x0, int y0){
        mX0 = x0;
        mY0 = y0;
    }

    public Diagram(Context context) {
        super(context);
    }

    public Diagram(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Diagram(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int index = 0;
        for(; index < mVisiblePointsCount; index++) {
            canvas.drawLine(mX0, mY0, mPoints.get(index).x, mPoints.get(index).y, mPoints.get(index).getPaint());
        }
    }

    public void setVisiblePercent(int value) {
        mVisiblePointsCount = value;
        Log.v(TAG, "setVisiblePercent mVisiblePointsCount " + mVisiblePointsCount);
    }

    public int getPointsCount() {
        Log.v(TAG, "getPointsCount " + mPoints.size());
        return mPoints.size();
    }

    public List<Point> getPointsList() {
        return mPoints;
    }
}
