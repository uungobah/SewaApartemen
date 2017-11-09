package com.apartment.swamedia.sewaapartemenbandung.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.apartment.swamedia.sewaapartemenbandung.R;
import com.apartment.swamedia.sewaapartemenbandung.fragment.FragmentTab1Messaging;
import com.apartment.swamedia.sewaapartemenbandung.fragment.FragmentTab1PengajuanSewa;
import com.apartment.swamedia.sewaapartemenbandung.fragment.FragmentTab2Messaging;
import com.apartment.swamedia.sewaapartemenbandung.fragment.FragmentTab2PengajuanSewa;

/**
 * Created by swa on 1/20/2016.
 */
public class ViewPagerAdapterMessaging extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs;
    Context mContext;
     // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created


    private int[] imageResId = {
            R.drawable.ic_file_download_black_24dp,
            R.drawable.ic_file_upload_black_24dp,

    };

//    public View getTabView(int position) {
//        // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
//        View v = LayoutInflater.from(mContext).inflate(R.layout.custom_tab, null);
//        TextView tv = (TextView) v.findViewById(R.id.tabtext);
//        tv.setText(Titles[position]);
//        ImageView img = (ImageView) v.findViewById(R.id.tabimage);
//        img.setImageResource(imageResId[position]);
//        return v;
//    }

    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapterMessaging(Context context,FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);
        this.mContext = context;
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        if (position == 0) // if the position is 0 we are returning the First tab
        {
            FragmentTab1Messaging tab1 = new FragmentTab1Messaging();
            return tab1;
        } else  {            // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
            FragmentTab2Messaging tab2 = new FragmentTab2Messaging();
            return tab2;
        }

    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
//        return Titles[position];
        Drawable image = mContext.getResources().getDrawable(imageResId[position]);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        SpannableString sb = new SpannableString(" "+"\n"+Titles[position]);
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
//        Drawable tab_image = mContext.getResources().getDrawable(imageResId[position]);
//        tab_image.setBounds(0, 0, 40, 40);  //Setting up the resolution for image
//        ImageSpan imageSpanresource = new ImageSpan(tab_image);
////Notice the additional space at the end of the String
//        String resourcesstring = "Tab1 ";
////here we are setting up the position to display image..
//        SpannableString viewpager_tab_title = new SpannableString(resourcesstring );
//        viewpager_tab_title.setSpan(imageSpanresource,0,1,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        return viewpager_tab_title;
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}
