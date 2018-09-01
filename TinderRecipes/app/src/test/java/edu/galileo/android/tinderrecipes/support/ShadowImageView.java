package edu.galileo.android.tinderrecipes.support;

import android.view.MotionEvent;
import android.widget.ImageView;

import org.robolectric.annotation.Implements;
import org.robolectric.annotation.RealObject;
import org.robolectric.shadows.ShadowView;

@Implements(ImageView.class)
public class ShadowImageView extends ShadowView{

    @RealObject
    private ImageView realImageView;

    public void performSwipe(float xStart, float yStart, float xEnd, float yEnd, long duration){
        long downTime = 0;
        long moveTime = downTime + duration/2;
        long upTime = downTime + duration;

        MotionEvent eventDown = MotionEvent.obtain(downTime, downTime, MotionEvent.ACTION_DOWN, xStart, yStart, 0);
        MotionEvent eventMove = MotionEvent.obtain(downTime, moveTime, MotionEvent.ACTION_MOVE, xEnd/2, yEnd/2, 0);
        MotionEvent eventUp = MotionEvent.obtain(downTime, upTime, MotionEvent.ACTION_UP, xEnd, yEnd, 0);

        realImageView.dispatchTouchEvent(eventDown);
        realImageView.dispatchTouchEvent(eventMove);
        realImageView.dispatchTouchEvent(eventMove);
        realImageView.dispatchTouchEvent(eventUp);

    }
}
