package com.brandontate.BTGridPager;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;


/**
 * BTGridPager
 *
 * The grid pager works by maintaining a view port that moves around a virtual grid.  The views to show in this viewport will be provided by the BTGridPagerAdapter.
 * When the a new page is selected the view port is updated to hold only the views surrounding it.
 *
 * @author Brandon Tate
 */
public class BTFragmentGridPager extends ViewPager {

    /** Bit flag for no wrapping. */
    public static final int GRID_WRAP_NONE = 0;

    /** Bit flag for left side wrapping. */
    public static final int GRID_WRAP_LEFT = 1 << 1;

    /** Bit flag for right side wrapping. */
    public static final int GRID_WRAP_RIGHT = 1 << 2;

    /** Bit flag for top side wrapping. */
    public static final int GRID_WRAP_UP = 1 << 3;

    /** Bit flag for bottom side wrapping. */
    public static final int GRID_WRAP_DOWN = 1 << 4;

    /** Wrapping flag. */
    private int mWrappingFlags = (GRID_WRAP_DOWN | GRID_WRAP_UP | GRID_WRAP_RIGHT);

    /** Bit flag for no resets. */
    public static final int GRID_RESET_NONE = 0;

    /** Bit flag for resetting to first column on row change. */
    public static final int GRID_RESET_ROW = 1 << 1;

    /** Bit flag for resetting to first row on column change. */
    public static final int GRID_RESET_COL = 1 << 2;

    /** Reset Flag. */
    private int mResetFlags = GRID_RESET_NONE;


    /** Current grid index. */
    public GridIndex mCurrentIndex = new GridIndex(0, 0);

    /** The fragment grid pager adapter for retrieving views. */
    private FragmentGridPagerAdapter mGridPagerAdapter;

    /** How many views to load on each side of the central view. */
    private int mGridPadding = 1;

    public BTFragmentGridPager(Context context) {
        super(context);
        init();
    }

    /**
     * Used to inflate the Workspace from XML.
     *
     * @param context
     *            The application's context.
     * @param attrs
     *            The attribtues set containing the Workspace's customization values.
     */
    public BTFragmentGridPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init(){
        resetAdapter();
    }

    //*****************************************************
    //*
    //*			Helpers
    //*
    //*****************************************************

    private boolean canScrollLeft(){
        return !(mCurrentIndex.getCol() == 0 && (0 == (mWrappingFlags & GRID_WRAP_LEFT)));
    }

    private boolean canScrollRight(){
        return !(mCurrentIndex.getCol() == (mGridPagerAdapter.columnCount(mCurrentIndex.getRow()) - 1) && (0 == (mWrappingFlags & GRID_WRAP_RIGHT)));
    }

    private boolean canScrollUp(){
        return !(mCurrentIndex.getRow() == 0 && (0 == (mWrappingFlags & GRID_WRAP_UP)));
    }

    private boolean canScrollDown(){
        return !(mCurrentIndex.getRow() == (mGridPagerAdapter.rowCount() - 1) && (0 == (mWrappingFlags & GRID_WRAP_DOWN)));
    }

    private GridIndex wrapGridIndex(GridIndex index){

        int newRow = index.getRow();

        if (newRow < 0)
            newRow = mGridPagerAdapter.rowCount() + newRow;
        else if(newRow >= mGridPagerAdapter.rowCount())
            newRow = (mGridPagerAdapter.rowCount() - newRow);


        int newCol = index.getCol();

        if (newCol < 0)
            newCol = mGridPagerAdapter.columnCount(newRow) + newCol;
        else if (newCol >= mGridPagerAdapter.columnCount(newRow))
            newCol = (mGridPagerAdapter.columnCount(newRow) - newCol);

        return new GridIndex(newRow, newCol);

    }

    /**
     *   Completely resets the adapter with updated current grid index.
     */
    private void resetAdapter(){

        // Clear old on page change listener to avoid unwanted actions
        setOnPageChangeListener(null);
        setAdapter(new FragmentGridHorizontalPagerAdapter( ((FragmentActivity) getContext()).getSupportFragmentManager()) );

        if (!canScrollLeft())
            setCurrentItem(0, false);
        else if(!canScrollRight())
            setCurrentItem(getAdapter().getCount() - 1);
        else
            setCurrentItem(mGridPadding, false);

        setOnPageChangeListener(new SimpleOnPageChangeListener(){

            @Override
            public void onPageSelected(int i) {

                // Default to wrapping index
                int newCol = mCurrentIndex.getCol() + ( -1 * (mGridPadding - i) );

                // Adjust for limited wrapping
                if (!canScrollLeft())
                    newCol = mCurrentIndex.getCol() + i;
                else if(!canScrollRight())
                    newCol--;

                mCurrentIndex = wrapGridIndex( new GridIndex( (0 != (mResetFlags & GRID_RESET_COL) ) ? 0 : mCurrentIndex.getRow() ,  newCol) );

                resetAdapter();
            }
        });
    }

    //*****************************************************
    //*
    //*			Getters/Setters
    //*
    //*****************************************************

    public FragmentGridPagerAdapter getGridPagerAdapter() {
        return mGridPagerAdapter;
    }

    public void setGridPagerAdapter(FragmentGridPagerAdapter gridPagerAdapter) {
        this.mGridPagerAdapter = gridPagerAdapter;
        resetAdapter();
    }

    public int getResetFlags() {
        return mResetFlags;
    }

    public void setResetFlags(int resetFlags) {
        this.mResetFlags = resetFlags;
    }

    public int getWrappingFlags() {
        return mWrappingFlags;
    }

    public void setWrappingFlags(int wrappingFlags) {
        this.mWrappingFlags = wrappingFlags;
    }


    //*****************************************************
    //*
    //*			Adapters
    //*
    //*****************************************************

    public class FragmentGridHorizontalPagerAdapter extends FragmentStatePagerAdapter{

        FragmentManager fm;

        public FragmentGridHorizontalPagerAdapter(FragmentManager fm) {
            super(fm);

            this.fm = fm;
        }

        @Override
        public Fragment getItem(int i) {

            if (mGridPagerAdapter == null)
                return new Fragment();

            // Figure out if this is our vertical pager
            boolean vpFragFlag = false;

            if (!canScrollLeft() &&  i == 0)
                vpFragFlag = true;
            else if(!canScrollRight() && i == (getAdapter().getCount() - 1))
                vpFragFlag = true;
            else if(canScrollLeft() && canScrollRight())
                vpFragFlag = (i == mGridPadding);

            if (vpFragFlag){

                BTVerticalPagerFragment vpFragment = new BTVerticalPagerFragment();

                vpFragment.setVerticalPagerAdapter(new FragmentStatePagerAdapter(fm){
                    @Override
                    public Fragment getItem(int i) {

                        if (mGridPagerAdapter == null)
                            return new Fragment();

                        int newCol = mCurrentIndex.getCol();

                        // If it's not the middle one and we need a row reset
                        if (i != mGridPadding && (0 != (mResetFlags & GRID_RESET_ROW) ))
                            newCol = 0;


                        // Default my view index wrapped
                        GridIndex viewIndex = wrapGridIndex(wrapGridIndex( new GridIndex( mCurrentIndex.getRow() + ( -1 * (mGridPadding - i) ), newCol ) ) );

                        // Adjust for limited wrapping
                        if (!canScrollUp() || !canScrollDown()) {
                            if (i == this.getCount() - 1)
                                viewIndex.setRow(mGridPagerAdapter.rowCount() - 1);
                            else
                                viewIndex.setRow(mCurrentIndex.getRow() + i);
                        }

                        return mGridPagerAdapter.getItem(viewIndex);
                    }

                    @Override
                    public int getCount() {
                        return (mGridPadding * 2) + 1;
                    }

                    public int getItemPosition(Object object){
                        return POSITION_NONE;
                    }
                });


                if (!canScrollUp())
                    vpFragment.setStartPage(0);
                else if(!canScrollDown())
                    vpFragment.setStartPage(vpFragment.getVerticalPagerAdapter().getCount() - 1);
                else
                vpFragment.setStartPage(mGridPadding);

                vpFragment.setPageChangeListener(new SimpleOnPageChangeListener() {

                    @Override
                    public void onPageSelected(int i) {
                        int newRow = mCurrentIndex.getRow() + ( -1 * (mGridPadding - i) );

                        if (!canScrollUp())
                            newRow = mCurrentIndex.getRow() + i;
                        else if(!canScrollDown())
                            newRow--;

                        int newCol = mCurrentIndex.getCol();

                        // If it's not the middle one and we need a row reset
                        if (i != mGridPadding && (0 != (mResetFlags & GRID_RESET_ROW) ))
                            newCol = 0;

                        mCurrentIndex = wrapGridIndex(new GridIndex(newRow, newCol));

                        resetAdapter();

                    }

                });

                return vpFragment;
            }
            else{

                // Default my view index wrapped
                GridIndex viewIndex = wrapGridIndex(new GridIndex((0 != (mResetFlags & GRID_RESET_COL) ) ? 0 : mCurrentIndex.getRow(), mCurrentIndex.getCol() + ( -1 * (mGridPadding - i) ) ) );

                // Adjust for limited wrapping
                if (!canScrollLeft() || !canScrollRight())
                    viewIndex.setCol(mCurrentIndex.getCol() + i);

                return mGridPagerAdapter.getItem(viewIndex);
            }
        }

        public int getItemPosition(Object object){
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return (mGridPadding * 2) + 1;
        }

    }

    public interface FragmentGridPagerAdapter{

        public int rowCount();
        public int columnCount(int row);
        public Fragment getItem(GridIndex index);

    }

    //*****************************************************
    //*
    //*			Data Types
    //*
    //*****************************************************

    public class GridIndex{

        private int mRow = 0, mCol = 0;

        public GridIndex(int row, int col){

            mRow = row;
            mCol = col;

        }

        public int getRow() {
            return mRow;
        }

        public void setRow(int row) {
            this.mRow = row;
        }

        public int getCol() {
            return mCol;
        }

        public void setCol(int col) {
            this.mCol = col;
        }

        @Override
        public String toString() {
            return "GridIndex{" +
                    "mRow=" + mRow +
                    ", mCol=" + mCol +
                    '}';
        }
    }
}
