package com.brandontate.BTGridPager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * BTGridPager
 *
 * @author Brandon Tate
 */
public class BTVerticalPagerFragment extends Fragment {

    /** Pager adapter. */
    private FragmentStatePagerAdapter mVerticalPagerAdapter;

    /** Page change listener. */
    private ViewPager.OnPageChangeListener mPageChangeListener;

    /** The actual pager. */
    private BTVerticalPager mPager;

    /** The start page when the pager is loaded. */
    private int mStartPage = 0;

    private BTFragmentGridPager mGridPager;

    public BTVerticalPagerFragment(){

    }

//    public BTVerticalPagerFragment (BTFragmentGridPager gridPager) {
//
//        mGridPager = gridPager;
//    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        LinearLayout view = new LinearLayout(getActivity());
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        view.setId(1);

        if (mGridPager != null) {
            mPager = new BTVerticalPager(getActivity(), mGridPager);
            mPager.setId(2);
            mPager.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            mPager.setAdapter(mVerticalPagerAdapter);
            mPager.setCurrentItem(mStartPage);
            mPager.setOnPageChangeListener(mPageChangeListener);

            view.addView(mPager);
        }

        return view;
    }

    public FragmentStatePagerAdapter getVerticalPagerAdapter() {
        return mVerticalPagerAdapter;
    }

    public void setGridPager(BTFragmentGridPager gridPager){
        mGridPager = gridPager;
    }

    public void setVerticalPagerAdapter(FragmentStatePagerAdapter verticalPagerAdapter) {
        this.mVerticalPagerAdapter = verticalPagerAdapter;

        if (mPager != null)
            mPager.setAdapter(mVerticalPagerAdapter);
    }

    public void setPageChangeListener(ViewPager.OnPageChangeListener pageChangeListener) {
        this.mPageChangeListener = pageChangeListener;

        if (mPager != null)
            mPager.setOnPageChangeListener(mPageChangeListener);
    }

    public BTVerticalPager getPager() {
        return mPager;
    }

    public void setStartPage(int startPage) {
        this.mStartPage = startPage;
    }

}
