package com.example.andumgaming.g370.views.tutorial;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.andumgaming.g370.R;

// Since this is an object collection, use a FragmentStatePagerAdapter,
// and NOT a FragmentPagerAdapter.
public class TutorialPagerAdapter extends FragmentStatePagerAdapter {
    public TutorialPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        TutorialImageFragment fragment = new TutorialImageFragment();
        switch(i)
        {
            case 0: fragment.setTutorialImage(R.id.page_zero);
            case 1: fragment.setTutorialImage(R.id.page_one);
            case 2: fragment.setTutorialImage(R.id.page_two);
            case 3: fragment.setTutorialImage(R.id.page_three);
            case 4: fragment.setTutorialImage(R.id.page_four);
            case 5: fragment.setTutorialImage(R.id.page_five);
            case 6: fragment.setTutorialImage(R.id.page_six);
            case 7: fragment.setTutorialImage(R.id.page_seven);
            case 8: fragment.setTutorialImage(R.id.page_eight);
            case 9: fragment.setTutorialImage(R.id.page_nine);
            default: fragment.setTutorialImage(R.id.page_zero);
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "OBJECT " + (position + 1);
    }
}
