package com.demo.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import com.brandontate.BTGridPager.BTFragmentGridPager;
import com.example.BTGridPager.R;

public class Demo extends FragmentActivity {

    private BTFragmentGridPager.FragmentGridPagerAdapter mFragmentGridPagerAdapter;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final BTFragmentGridPager mFragmentGridPager = (BTFragmentGridPager) findViewById(R.id.fragmentGridPager);

        mFragmentGridPagerAdapter = new BTFragmentGridPager.FragmentGridPagerAdapter() {
            @Override
            public int rowCount() {
                return 10;
            }

            @Override
            public int columnCount(int row) {
                return 10;
            }

            @Override
            public Fragment getItem(BTFragmentGridPager.GridIndex index) {
                DemoFragment panelFrag1 = new DemoFragment();
                panelFrag1.setGridIndex(index);

                return panelFrag1;
            }
        };

        mFragmentGridPager.setGridPagerAdapter(mFragmentGridPagerAdapter);
    }
}
