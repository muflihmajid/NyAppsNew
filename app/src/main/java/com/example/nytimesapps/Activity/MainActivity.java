package com.example.nytimesapps.Activity;

import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.nytimesapps.Adapter.SectionsPageAdapter;
import com.example.nytimesapps.Fragment.HomeFragment;
import com.example.nytimesapps.Fragment.SavedFragment;
import com.example.nytimesapps.R;

public class MainActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener,SavedFragment.OnFragmentInteractionListener{

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);
        //Set Up tab
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setTabTextColors(getResources().getColor(R.color.Normal), getResources().getColor(R.color.Selected));
        tabLayout.setupWithViewPager(mViewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(), "Home");
        adapter.addFragment(new SavedFragment(), "Saved");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}