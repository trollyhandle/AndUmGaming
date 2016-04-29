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
        switch(i)
        {
            case 0: return new TutorialImageFragment().newInstance(R.id.page_zero);
            case 1: return new TutorialImageFragment().newInstance(R.id.page_one);
            case 2: return new TutorialImageFragment().newInstance(R.id.page_two);
            case 3: return new TutorialImageFragment().newInstance(R.id.page_three);
            case 4: return new TutorialImageFragment().newInstance(R.id.page_four);
            case 5: return new TutorialImageFragment().newInstance(R.id.page_five);
            case 6: return new TutorialImageFragment().newInstance(R.id.page_six);
            case 7: return new TutorialImageFragment().newInstance(R.id.page_seven);
            case 8: return new TutorialImageFragment().newInstance(R.id.page_eight);
            case 9: return new TutorialImageFragment().newInstance(R.id.page_nine);
            default: return new TutorialImageFragment().newInstance(R.id.page_zero);
        }
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
