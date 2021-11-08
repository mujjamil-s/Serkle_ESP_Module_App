package com.almaharapvtltd.serkle;

import android.os.Bundle;

import com.almaharapvtltd.serkle.ui.main.AllDevicesFragment;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.almaharapvtltd.serkle.ui.main.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    ViewPager2 viewPager2;
    TabLayout tabLayout;
    View tabIndicator;
    int indiacatorWidth;
    AppBarLayout appBarLayout;

     AllDevicesFragment.DataFragAdapter adapter;
     FragmentActivity activity = this;
    public ArrayList<AllDevicesFragment.Items> roomItem, kitchenItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
      //  AllDevicesFragment.DataFragAdapter sectionsPagerAdapter = new AllDevicesFragment.DataFragAdapter(this, getSupportFragmentManager());

        //viewPager.setAdapter(sectionsPagerAdapter);c
       // TabLayout tabs = findViewById(R.id.tabs_room);
        //tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);
        tabLayout = findViewById(R.id.tabs_room);
        viewPager2 = findViewById(R.id.view_pager_items);
        tabIndicator = findViewById(R.id.indicator);
        appBarLayout =(AppBarLayout) findViewById(R.id.appBarLayout);

        appBarLayout.setElevation(0);
        AddToList();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter = new AllDevicesFragment.DataFragAdapter(activity,roomItem,kitchenItem);

                viewPager2.setAdapter(adapter);

        new TabLayoutMediator(tabLayout,viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setText("All Devices");

                        break;
                    case  1:
                        tab.setText("Bed Room");

                        break;


                }
            }
        }).attach();
            }
        });

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                indiacatorWidth = tabLayout.getWidth()/tabLayout.getTabCount();

                FrameLayout.LayoutParams indicatorParams =(FrameLayout.LayoutParams) tabIndicator.getLayoutParams();
                indicatorParams.width = indiacatorWidth;
                tabIndicator.setLayoutParams(indicatorParams);
            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) tabIndicator.getLayoutParams();

                float translationOffset = (positionOffset + position) * indiacatorWidth;
                params.leftMargin = (int) translationOffset;
                tabIndicator.setLayoutParams(params);

            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void AddToList(){
        roomItem =new ArrayList<>();
        kitchenItem = new ArrayList<>();
        AllDevicesFragment.Items bulb1 = new AllDevicesFragment.Items("Bulb 1",false),
                bulb2 = new AllDevicesFragment.Items("Bulb 2",true),
                bulb3 = new AllDevicesFragment.Items("Bulb 3",false);
        for(int i = 0; i<8; i++){
        roomItem.add(bulb3);
        }
        roomItem.add(bulb3);
        roomItem.add(bulb2);
        kitchenItem.add(bulb1);


    }
}