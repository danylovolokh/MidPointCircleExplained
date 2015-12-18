package com.volokh.danylo.midpointcircle;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.volokh.danylo.midpointcircle.circle_points_creator.CirclePointsCreator;
import com.volokh.danylo.midpointcircle.circle_points_creator.FirstQuadrantCirclePointsCreator;

public class MidPointCircleActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MidPointCircleActivity.class.getSimpleName();
    private static final int ANIMATION_DURATION = 5000;

    // paints to draw octants on the circle
    private final Paint mPaintGreen = new Paint();
    private final Paint mPaintRed = new Paint();
    private final Paint mPaintBlue = new Paint();
    private final Paint mPaintBlack = new Paint();
    private final Paint mPaintYellow = new Paint();
    private final Paint mPaintMagenta = new Paint();
    private final Paint mPaintCyan = new Paint();
    private final Paint mPaintDarkGrey = new Paint();

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

    private int radius;

    private int mX0;
    private int mY0;

    private Diagram mMidPointCircleDiagram;
    private Diagram mMidPointCircleModifiedDiagram;

    private Button mReloadMidPointCircleButton;
    private Button mReloadMidPointCircleModifiedButton;

    private CirclePointsCreator mCirclePointsCreator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mMidPointCircleDiagram = (Diagram) findViewById(R.id.mid_point_circle);
        mMidPointCircleModifiedDiagram = (Diagram) findViewById(R.id.mid_point_circle_modified);
        mReloadMidPointCircleButton = (Button) findViewById(R.id.reload_mid_point_circle);
        mReloadMidPointCircleModifiedButton = (Button) findViewById(R.id.reload_mid_point_circle_modified);

        mReloadMidPointCircleButton.setOnClickListener(this);
        mReloadMidPointCircleModifiedButton.setOnClickListener(this);

        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;

        radius = screenWidth / 2;

        mX0 = screenWidth/2;
        mY0 = screenHeight/2;

        mMidPointCircleDiagram.setOrigin(mX0, mY0);
        mMidPointCircleModifiedDiagram.setOrigin(mX0, mY0);


        // create points for "mid point circle"
        createCirclePoints(mX0, mY0, radius);

        // create points for "mid point circle" modified
        mCirclePointsCreator = new FirstQuadrantCirclePointsCreator(radius, mX0, mY0);
        mCirclePointsCreator.fillCirclePoints(mMidPointCircleModifiedDiagram.getPointsList());

        startAnimation(mMidPointCircleDiagram, mMidPointCircleModifiedDiagram.getPointsCount());
    }

    private void startAnimation(final Diagram diagram, int pointsCount) {

        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, pointsCount);
        valueAnimator.setDuration(ANIMATION_DURATION);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                Log.v(TAG, "onAnimationUpdate value " + value);

                diagram.setVisiblePercent(value);
                diagram.invalidate();
            }
        });
        valueAnimator.start();
    }

    void createCirclePoints(int x0, int y0, int radius)
    {
        int x = radius;
        int y = 0;
        int decisionOver2 = 1 - x;   // Decision criterion divided by 2 evaluated at x=r, y=0

        while( y <= x )
        {
            mMidPointCircleDiagram.addPoint(new Point(x + x0, y + y0, mPaintBlack)); // Octant 1

            mMidPointCircleDiagram.addPoint(new Point(y + x0, x + y0, mPaintBlue));// Octant 2

            mMidPointCircleDiagram.addPoint(new Point(-x + x0, y + y0, mPaintCyan));// Octant 4

            mMidPointCircleDiagram.addPoint(new Point(-y + x0, x + y0, mPaintDarkGrey));// Octant 3

            mMidPointCircleDiagram.addPoint(new Point(-x + x0, -y + y0, mPaintGreen));// Octant 5

            mMidPointCircleDiagram.addPoint(new Point(-y + x0, -x + y0, mPaintMagenta));// Octant 6

            mMidPointCircleDiagram.addPoint(new Point(x + x0, -y + y0, mPaintRed));// Octant 8

            mMidPointCircleDiagram.addPoint(new Point(y + x0, -x + y0, mPaintYellow));// Octant 7

            y++;
            if (decisionOver2<=0)
            {
                decisionOver2 += 2 * y + 1;   // Change in decision criterion for y -> y+1
            }
            else
            {
                x--;
                decisionOver2 += 2 * (y - x) + 1;   // Change for y -> y+1, x -> x-1
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.reload_mid_point_circle:

                mMidPointCircleModifiedDiagram.setVisibility(View.GONE);
                mMidPointCircleDiagram.setVisibility(View.VISIBLE);

                startAnimation(mMidPointCircleDiagram, mMidPointCircleDiagram.getPointsCount());
                break;
            case R.id.reload_mid_point_circle_modified:

                mMidPointCircleModifiedDiagram.setVisibility(View.VISIBLE);
                mMidPointCircleDiagram.setVisibility(View.GONE);

                startAnimation(mMidPointCircleModifiedDiagram, mMidPointCircleModifiedDiagram.getPointsCount());
                break;
        }
    }
}
