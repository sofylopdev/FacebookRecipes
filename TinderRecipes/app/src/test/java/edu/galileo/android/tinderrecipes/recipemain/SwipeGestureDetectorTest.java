package edu.galileo.android.tinderrecipes.recipemain;

import android.view.MotionEvent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import edu.galileo.android.tinderrecipes.BaseTest;
import edu.galileo.android.tinderrecipes.BuildConfig;
import edu.galileo.android.tinderrecipes.recipemain.ui.SwipeGestureDetector;
import edu.galileo.android.tinderrecipes.recipemain.ui.SwipeGestureListener;

import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 27)
public class SwipeGestureDetectorTest extends BaseTest {

    @Mock
    private SwipeGestureListener listener;

    private SwipeGestureDetector detector;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        detector = new SwipeGestureDetector(listener);
    }

    @Test
    public void testSwipeRight_ShouldCallKeepOnListener() {
        long downTime = 0;
        long moveTime = downTime + 500;
        long upTime = downTime + 1000;
        float xStart = 200;
        float yStart = 200;
        float xEnd = 500;
        float yEnd = 250;

        MotionEvent motionEventStart = MotionEvent.obtain(downTime, moveTime, MotionEvent.ACTION_MOVE, xStart, yStart, 0);
        MotionEvent motionEventEnd = MotionEvent.obtain(downTime, upTime, MotionEvent.ACTION_UP, xEnd, yEnd, 0);

        float velocityX = 120;

        detector.onFling(motionEventStart, motionEventEnd, velocityX, 0);
        verify(listener).onKeep();
    }

    @Test
    public void testSwipeLeft_ShouldCallDismissOnListener() {
        long downTime = 0;
        long moveTime = downTime + 500;
        long upTime = downTime + 1000;
        float xStart = 200;
        float yStart = 200;
        float xEnd = -500;
        float yEnd = 250;

        MotionEvent motionEventStart = MotionEvent.obtain(downTime, moveTime, MotionEvent.ACTION_MOVE, xStart, yStart, 0);
        MotionEvent motionEventEnd = MotionEvent.obtain(downTime, upTime, MotionEvent.ACTION_UP, xEnd, yEnd, 0);

        float velocityX = 120;

        detector.onFling(motionEventStart, motionEventEnd, velocityX, 0);
        verify(listener).onDismiss();
    }
}
