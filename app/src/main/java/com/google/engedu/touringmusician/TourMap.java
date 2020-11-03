/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.touringmusician;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class TourMap extends View {

    private Bitmap mapImage;
    private CircularLinkedList list = new CircularLinkedList();
    private CircularLinkedList BeginningList = new CircularLinkedList();
    private CircularLinkedList ClosestList = new CircularLinkedList();
    private CircularLinkedList SmallestList = new CircularLinkedList();
    private String insertMode = "Add";

    public TourMap(Context context) {
        super(context);
        mapImage = BitmapFactory.decodeResource(
                getResources(),
                R.drawable.map);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mapImage, 0, 0, null);
        Paint pointPaint = new Paint();
        pointPaint.setColor(Color.RED);
        Paint linePaint  = new Paint();
        linePaint.setColor(Color.BLACK);
        linePaint.setStrokeWidth((float) 3.5);
        Point startPoint = null;
        Point prevPoint = null;
        for (Point p : list) {
            if (startPoint==null) {startPoint=p;}
            canvas.drawCircle(p.x, p.y, 30, pointPaint);
            if (prevPoint!=null) {
                canvas.drawLine(prevPoint.x,prevPoint.y,p.x,p.y,linePaint);
            }
            prevPoint = p;
        }
        Paint beginningPaint = new Paint();
        beginningPaint.setColor(Color.BLUE);
        beginningPaint.setStrokeWidth(20);
        drawLinesInList(canvas,BeginningList,beginningPaint);
        Paint closestPaint = new Paint();
        closestPaint.setColor(Color.MAGENTA);
        closestPaint.setStrokeWidth((float) 10);
        drawLinesInList(canvas,ClosestList,closestPaint);
        Paint smallestPaint = new Paint();
        smallestPaint.setColor(Color.YELLOW);
        smallestPaint.setStrokeWidth(5);
        drawLinesInList(canvas,SmallestList,smallestPaint);
        /*
        if (startPoint!= null && prevPoint != null){
            canvas.drawLine(prevPoint.x,prevPoint.y,startPoint.x,startPoint.y,linePaint);
        }
        */
    }

    private void drawLinesInList(Canvas canvas,CircularLinkedList list,Paint linePaint){
        Point prevPoint = null;
        for (Point p : list) {
            if (prevPoint!=null) {
                canvas.drawLine(prevPoint.x,prevPoint.y,p.x,p.y,linePaint);
            }
            prevPoint = p;
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Point p = new Point((int) event.getX(), (int)event.getY());
                if (insertMode.equals("Closest")) {
                    list.insertNearest(p);
                } else if (insertMode.equals("Smallest")) {
                    list.insertSmallest(p);
                } else {
                    list.insertBeginning(p);
                }
                BeginningList.insertBeginning(p);
                ClosestList.insertNearest(p);
                SmallestList.insertSmallest(p);
                updateCostMessage(list,R.id.game_status,insertMode);
                updateCostMessage(BeginningList,R.id.BeginningCost,"beginning");
                updateCostMessage(ClosestList,R.id.ClosestCost, "closest");
                updateCostMessage(SmallestList,R.id.SmallestCost, "smallest");
                invalidate();
                return true;
        }
        return super.onTouchEvent(event);
    }
    private void updateCostMessage(CircularLinkedList list,int TextViewId,String tourType ){
        TextView message = (TextView) ((Activity) getContext()).findViewById(TextViewId);
        if (message != null) {
            message.setText(String.format("Tour length using %s insert mode is now %.2f", tourType,list.totalDistance()));
        }
    }

    public void reset() {
        list.reset();
        BeginningList.reset();
        ClosestList.reset();
        SmallestList.reset();
        invalidate();
    }

    public void setInsertMode(String mode) {
        insertMode = mode;
    }
}
