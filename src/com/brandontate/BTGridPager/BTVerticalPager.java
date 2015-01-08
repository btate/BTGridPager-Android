package com.brandontate.BTGridPager;


import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * BTGridPager
 *
 * @author Brandon Tate
 */
public class BTVerticalPager extends ViewPager {

    BTFragmentGridPager mGridPager;

    public BTVerticalPager(Context context, BTFragmentGridPager gridPager) {
        super(context);
        init();

        mGridPager = gridPager;
    }

    public BTVerticalPager(Context context, AttributeSet attrs, BTFragmentGridPager gridPager) {
        super(context, attrs);
        init();

        mGridPager = gridPager;
    }

    private void init(){

        // The majority of the magic happens here
        setPageTransformer(true, new VerticalPageTransformer());
        // The easiest way to get rid of the overscroll drawing that happens on the left and right
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    @Override
    protected void onPageScrolled(int position, float offset, int offsetPixels) {
        super.onPageScrolled(position, offset, offsetPixels);

        if (offset == 0  && mGridPager.shouldReset) {
            getRootView().post(new Runnable() {
                @Override
                public void run() {
                    mGridPager.shouldReset = false;
                    mGridPager.resetAdapter();
                }
            });
        }
    }

    private class VerticalPageTransformer implements ViewPager.PageTransformer {

        @Override
        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            view.setAlpha(1);
//
//            if (position < -1 && !mGridPager.shouldReset) { // [-Infinity,-1)
//                // This page is way off-screen to the left.
//                view.setAlpha(0);
//
//            }
            if (position >= -1 && position <= 1) { // [-1,1]
                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);

                //set Y position to swipe in from top
                float yPosition = position * pageHeight;
                view.setTranslationY(yPosition);
            }
//            } else if (!mGridPager.shouldReset) { // (1,+Infinity]
//                // This page is way off-screen to the right.
//                view.setAlpha(0);
//            }
        }
    }

    /**
     * Swaps the X and Y coordinates of your touch event
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //swap the x and y coords of the touch event
        ev.setLocation(ev.getY(), ev.getX());

        return super.onTouchEvent(ev);
    }
}
