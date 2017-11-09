package com.apartment.swamedia.sewaapartemenbandung.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.apartment.swamedia.sewaapartemenbandung.fragment.FragmentTab1PengajuanSewa;
import com.apartment.swamedia.sewaapartemenbandung.fragment.FragmentTab1PermintaanSewa;
import com.apartment.swamedia.sewaapartemenbandung.fragment.FragmentTab2PengajuanSewa;
import com.apartment.swamedia.sewaapartemenbandung.fragment.FragmentTab2PermintaanSewa;
import com.apartment.swamedia.sewaapartemenbandung.fragment.FragmentTab3PengajuanSewa;

/**
 * Created by swa on 1/4/2016.
 */
public class ViewPagerAdapterPengajuanSewa extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created


    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapterPengajuanSewa(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        if (position == 0) // if the position is 0 we are returning the First tab
        {
            FragmentTab1PengajuanSewa tab1 = new FragmentTab1PengajuanSewa();
            return tab1;
        } else if (position == 1) // if the position is 0 we are returning the First tab
        {
            FragmentTab2PengajuanSewa tab2 = new FragmentTab2PengajuanSewa();
            return tab2;
        }  {            // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
            FragmentTab3PengajuanSewa tab3 = new FragmentTab3PengajuanSewa();
            return tab3;
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
