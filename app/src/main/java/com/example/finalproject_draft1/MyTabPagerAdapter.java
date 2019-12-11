package com.example.finalproject_draft1;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.finalproject_draft1.Fragments.RecordFragment;
import com.example.finalproject_draft1.Fragments.SavedFragment;


/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class MyTabPagerAdapter extends FragmentPagerAdapter {

  @StringRes
  private static final int[] TAB_TITLES = new int[]{R.string.recording_tab, R.string.saved_tab};
  private final Context mContext;

  public MyTabPagerAdapter(Context context, FragmentManager fm) {
    super(fm);
    mContext = context;
  }

  @Override
  public Fragment getItem(int position) {
    // getItem is called to instantiate the fragment for the given page.
    // Return a PlaceholderFragment (defined as a static inner class below).
    Fragment fragment = null;

    switch (position){
      case 0:
        fragment = RecordFragment.newInstance();
        break;
      case 1:
        fragment = SavedFragment.newInstance();
        break;
    }

    return fragment;
  }

  @Nullable
  @Override
  public CharSequence getPageTitle(int position) {
    return mContext.getResources().getString(TAB_TITLES[position]);
  }

  @Override
  public int getCount() {
    // Show 2 total pages.
    return TAB_TITLES.length;
  }
}