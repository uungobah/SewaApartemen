package com.apartment.swamedia.sewaapartemenbandung.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.apartment.swamedia.sewaapartemenbandung.fragment.FragmentMenuEditTab1;
import com.apartment.swamedia.sewaapartemenbandung.fragment.FragmentMenuEditTab2;
import com.apartment.swamedia.sewaapartemenbandung.fragment.FragmentMenuEditTab3;
import com.apartment.swamedia.sewaapartemenbandung.fragment.FragmentTab1Verifikasi;
import com.apartment.swamedia.sewaapartemenbandung.fragment.FragmentTab2Verifikasi;

/**
 * Created by swa on 12/15/2015.
 */
public class ViewPagerAdapterVerifikasi extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created


    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapterVerifikasi(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        if (position == 0) // if the position is 0 we are returning the First tab
        {
            FragmentTab1Verifikasi tab1 = new FragmentTab1Verifikasi();
            return tab1;
        } else  {            // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
            FragmentTab2Verifikasi tab2 = new FragmentTab2Verifikasi();
            return tab2;
        }

    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }


}