package com.demo.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.brandontate.BTGridPager.BTFragmentGridPager;
import com.example.BTGridPager.R;

/**
 * BTGridPager
 *
 * @author Brandon Tate
 */
public class DemoFragment extends Fragment {

    TextView mLabel;

    BTFragmentGridPager.GridIndex mGridIndex;


    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.demo_fragment, container, false);
        mLabel = (TextView) layout.findViewById(R.id.label);
        setTxtRow(mGridIndex);

        return layout;

    }


    public void setTxtRow(BTFragmentGridPager.GridIndex gridIndex) {
        mLabel.setText("(" + gridIndex.getRow() + ", " + gridIndex.getCol() + ")");
    }

    public void setGridIndex(BTFragmentGridPager.GridIndex gridIndex){
        mGridIndex = gridIndex;
    }

}
