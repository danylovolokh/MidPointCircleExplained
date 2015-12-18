package com.volokh.danylo.midpointcircle.mirror_helper;

import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.volokh.danylo.midpointcircle.Point;

import java.util.List;

/**
 * This class is a helper for FirstQuadrantCirclePointsCreator
 *
 * It can create a full circle points using points from 1st octant. It is mirroring existing points to the points in other circle sectors.
 *
 */
public class FirstQuadrantCircleMirrorHelper implements CircleMirrorHelper {

    private static final boolean SHOW_LOGS = false;
    private static final String TAG = FirstQuadrantCircleMirrorHelper.class.getSimpleName();

    private final int mX0;
    private final int mY0;

    private final Paint _2_octant_paint = new Paint();
    private final Paint _2_quadrant_paint = new Paint();
    private final Paint _2_semicircle_paint = new Paint();

    {
        _2_octant_paint.setColor(Color.RED);
        _2_octant_paint.setStrokeWidth(2);

        _2_quadrant_paint.setColor(Color.BLACK);
        _2_quadrant_paint.setStrokeWidth(2);

        _2_semicircle_paint.setColor(Color.BLUE);
        _2_semicircle_paint.setStrokeWidth(2);
    }


    public FirstQuadrantCircleMirrorHelper(int x0, int y0){
        mX0 = x0;
        mY0 = y0;
    }

    enum Action{
        MIRROR_2ND_OCTANT,
        MIRROR_2ND_QUADRANT,
        MIRROR_2ND_SEMICIRCLE
    }

    /**
     * This method takes the points from 1st octant and mirror them to the 2nd octant
     *
     *                     ^                  /
     *                  +y | 2nd octant    /
     *                     |             /
     *                     |_____      /
     *                     |     --_ /
     *                     |       / *_  <-- this s the point from where we start
     *                     |     /     |                  \
     *                     |   /       | 1st Octant        | We are going through points in this direction
     *                     | /         |                   V
     *      ---------------|--------------->      V
     *        |            |
     *        |            |
     *        |_           |
     *          \_         |
     *            --_______|
     *                     |
     *                     |
     */
    @Override
    public void mirror_2nd_Octant(
            List<Point> circlePoints
    ) {

        int countOfPointsIn_1st_octant = circlePoints.size();
        if(SHOW_LOGS) Log.v(TAG, "mirror_2nd_Octant, countOfPointsIn_1st_octant " + countOfPointsIn_1st_octant);

        for(int pointIndex = countOfPointsIn_1st_octant - 1;
            pointIndex >= 0;
            pointIndex-- ){

            createMirroredPoint(Action.MIRROR_2ND_OCTANT, pointIndex, circlePoints);
        }
    }

    @Override
    public void mirror_2nd_Quadrant(
            List<Point> circlePoints
    ) {

        int countOfPointsIn_1st_quadrant = circlePoints.size();
        if(SHOW_LOGS) Log.v(TAG, "mirror_2nd_Quadrant, countOfPointsIn_1st_quadrant " + countOfPointsIn_1st_quadrant);

        for(int pointIndex = countOfPointsIn_1st_quadrant
                - 1 // last point
                - 1 // previous to the last because last point is already in the list (x0, radius + y0). It is a point on Y axis
            ;
            pointIndex >= 0;
            pointIndex-- ){

            createMirroredPoint(Action.MIRROR_2ND_QUADRANT, pointIndex, circlePoints);
        }
    }

    @Override
    public void mirror_2nd_Semicircle(
            List<Point> circlePoints
    ) {

        int countOfPointsIn_1st_semicircle = circlePoints.size();
        if(SHOW_LOGS) Log.v(TAG, "mirror_2nd_Semicircle, countOfPointsIn_1st_semicircle " + countOfPointsIn_1st_semicircle);

        for(int pointIndex = countOfPointsIn_1st_semicircle - 2; // don't count (-radius, 0) because it already in the list
            pointIndex > 0; // don't count (radius, 0) because it already in the list
            pointIndex-- ){

            createMirroredPoint(Action.MIRROR_2ND_SEMICIRCLE, pointIndex, circlePoints);

        }

    }

    private void createMirroredPoint(
            Action action,
            int pointIndex,
            List<Point> circlePoints
    ) {

        Point pointAtIndex = circlePoints.get(pointIndex);

        Point mirroredPoint;
        switch (action) {
            case MIRROR_2ND_OCTANT:
                mirroredPoint = mirror_2nd_OctantPoint(pointAtIndex, _2_octant_paint);
                break;
            case MIRROR_2ND_QUADRANT:
                mirroredPoint = mirror_2nd_QuadrantPoint(pointAtIndex, _2_quadrant_paint);
                break;
            case MIRROR_2ND_SEMICIRCLE:
                mirroredPoint = mirror_2nd_SemicirclePoint(pointAtIndex, _2_semicircle_paint);
                break;
            default:
                throw new RuntimeException("Not handled action " + action);
        }

        if (mirroredPoint != null) {
            circlePoints.add(mirroredPoint);
        } else {
            if (SHOW_LOGS)
                Log.i(TAG, "createMirroredPoint, found a point that should not be mirrored, pointAtIndex " + pointAtIndex + ", action " + action);
            if (SHOW_LOGS)
                Log.i(TAG, "createMirroredPoint, this point is already created. Skip it");

        }
    }

    /**
     * This method takes a single point from 1st octant and mirror it to the 2nd octant
     *
     *                     ^
     *                  +y |   2nd octant
     *                     |                           /
     *                     |                         /
     *                     |                       /
     *                     |                     /
     *                     |                   /
     *                     |     * (x1*, y1*)  /
     *                     |         * (x2*, y2*)
     *                     |             /
     *                     |           /    * (x2, y2)
     *                     |         /
     *                     |       /          * (x1, y1)
     *                     |     /
     *                     |   /
     *                     | /                       1st octant
     *                     |------------------------------------->
     *
     *   How to get a mirrored point (x1*, y1*) when we have it's mirror (x1, y1)?
     *   x1 = y1
     *   y1 = x1
     ************
     *   How to get a mirrored point (x2*, y2*) when we have it's mirror (x2, y2)?
     *   x2* = y2
     *   y2* = x2
     *
     * Here is the explanation of the implementation.
     * This is how 1st and 2nd octant is drawn in "Mid point circle" algorithm
     *
     * DrawPixel( x + x0,  y + y0); // Octant 1
     * DrawPixel( y + x0,  x + y0); // Octant 2
     *
     * To mirror second point using "firstOctantPoint" we have to know original x and y;
     *
     * Get original x, y from "firstOctantPoint":
     * firstOctant_X = x + x0; -> x = firstOctant_X - x0;
     * firstOctant_Y = y + y0; -> y = firstOctant_Y - y0;
     *
     * Get "secondOctantPoint" from original x, y
     * secondOctant_X = y + x0; -> firstOctant_Y - y0 + x0;
     * secondOctant_Y = x + y0; -> firstOctant_X - x0 + y0;
     */
    private Point mirror_2nd_OctantPoint(Point firstOctantPoint, Paint paint) {
        int correctedX = firstOctantPoint.x - mX0;
        int correctedY = firstOctantPoint.y - mY0;
        return correctedX != correctedY ? new Point(correctedY + mX0, correctedX + mY0, paint)
                : null; // null means that the mirror of this point is going to be the same. (24; 24) -> mirrored (24:24)
    }

    /**
     * This method takes a single point from 1st octant and mirror it to the 2nd octant
     *
     *  ^ +y                                ^
     *  |                  2nd Quadrant  +y |     1st Quadrant
     *  |                                   |
     *  |                                   |
     *  |                    (x3*; y3*) *<--|---* (x3; y3)
     *  |             (x2*; y2*) *<---------|----------*(x2; y2)
     *  |                                   |
     *  |                                   |
     *  |                                   |
     *  |                                   |
     *  |                                   |
     *  |                                   |
     *  |                                   |
     *  |        (x1*; y1*) *<--------------|---------------* (x1; y1)
     *  |                                   |
     *  |                                   |                                      +x
     *--|-----------------------------------|------------------------------------->
     *
     *   How to get a mirrored point (x1*, y1*) when we have it's mirror (x1, y1)?
     *   x1* = x0 - (x1 - x0) = 2*x0 - x1
     *   y1* = y1
     ************
     *   How to get a mirrored point (x2*, y2*) when we have it's mirror (x2, y2)?
     *   x2* = x0 - (x2 - x0) = 2*x0 - x2
     *   y2* = y2
     ************
     *   How to get a mirrored point (x3*, y3*) when we have it's mirror (x3, y3)?
     *   x3* = x0 - (x3 - x0) = 2*x0 - x3
     *   y3* = y3
     */
    private Point mirror_2nd_QuadrantPoint(Point secondQuadrantPoint, Paint paint) {
        return new Point(-secondQuadrantPoint.x + 2 * mX0, secondQuadrantPoint.y, paint);
    }

    /**
     * This method takes a single point from 1st octant and mirror it to the 2nd octant
     *
     *                                     ^
     *                    2nd Quadrant  +y |     1st Quadrant
     *                                     |
     *                                     |
     *                      (x3; y3)   *   |
     *        (x4; y4) *               |   |
     *                 |               |   |
     *                 |               |   |
     *                 |               |   |
     *                 |               |   |
     *                 |               |   |                              * (x2; y2)
     *                 |               |   |                              |
     *                 |               |   |                              |
     *                 |               |   |                              |  * (x1; y1)
     *                 |               |   |                              |  |
     *                 |               |   |                              |  |     +x
     *-------------------------------------|------------------------------------->
     *                 |               |   |                              |  |
     *                 |               |   |                              |  V
     *                 |               |   |                              |  * (x1*; y1*)
     *                 |               |   |                              |
     *                 |               |   |                              V
     *                 |               |   |                              * (x2*; y2*)
     *                 |               |   |
     *                 |               |   |
     *                 |               |   |
     *                 V               |   |
     *        (x4; y4) *               V   |
     *                      (x3*; y3*) *   |
     *                                     |
     *                                     |
     *   How to get a mirrored point (x1*, y1*) when we have it's mirror (x1, y1)?
     *   x1* = x1
     *   y1* = y0 - (y1 - y0) = 2 * y0 - y1
     ************
     *   How to get a mirrored point (x2*, y2*) when we have it's mirror (x2, y2)?
     *   x2* = x2
     *   y2* = y0 - (y2 - y0) = 2 * y0 - y2
     ************
     *   How to get a mirrored point (x3*, y3*) when we have it's mirror (x3, y3)?
     *   x3* = x3
     *   y3* = y0 - (y3 - y0) = 2 * y0 - y3
     ************
     *   How to get a mirrored point (x4*, y4*) when we have it's mirror (x4, y4)?
     *   x4* = x4
     *   y4* = y0 - (y3 - y0) = 2 * y0 - y3
     */
    private Point mirror_2nd_SemicirclePoint(Point firstSemicirclePoint, Paint paint) {
        /** TODO: use the same logic as {@link #mirror_2nd_OctantPoint}*/
        return new Point(firstSemicirclePoint.x, -firstSemicirclePoint.y + 2 * mY0, paint);
    }

}
