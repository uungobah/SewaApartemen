package com.apartment.swamedia.sewaapartemenbandung.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apartment.swamedia.sewaapartemenbandung.R;
import com.apartment.swamedia.sewaapartemenbandung.adapter.ViewPagerAdapterPermintaanSewa;
import com.apartment.swamedia.sewaapartemenbandung.adapter.ViewPagerAdapterVerifikasi;
import com.apartment.swamedia.sewaapartemenbandung.tabmenu.SlidingTabLayout;

/**
 * Created by swa on 1/4/2016.
 */
public class FragmentPermintaanSewa extends Fragment {

    Context ctx;
    View v;

    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapterPermintaanSewa adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Permintaan Sewa Baru","Status Pembayaran","Daftar Transaksi"};
    int Numboftabs =3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        v = inflater.inflate(R.layout.fragmenteditprofile, container, false);
        v = inflater.inflate(R.layout.fragment_permintaan_sewa, container, false);

        ctx = v.getContext();

        adapter =  new ViewPagerAdapterPermintaanSewa(getFragmentManager(),Titles,Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) v.findViewById(R.id.pager_permintaan_sewa);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) v.findViewById(R.id.tabs_permintaan_sewa);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.colorAccent);
//                return Color.WHITE;
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);


        return v;
    }
}
