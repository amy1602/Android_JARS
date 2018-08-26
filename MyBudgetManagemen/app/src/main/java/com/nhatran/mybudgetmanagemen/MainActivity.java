package com.nhatran.mybudgetmanagemen;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.nhatran.mybudgetmanagemen.db.PaymentDBHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(), "Home");
        adapter.addFragment(new StatisticFragment(), "Statistic");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_timeline_24dp);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0){
                    FragmentLifeCycle fragmentToHide = (FragmentLifeCycle) adapter.getItem(1);
                    fragmentToHide.onPauseFragment();

                    FragmentLifeCycle fragmentToShow = (FragmentLifeCycle) adapter.getItem(0);
                    fragmentToShow.onResumeFragment();
                }else {
                    FragmentLifeCycle fragmentToHide = (FragmentLifeCycle) adapter.getItem(0);
                    fragmentToHide.onPauseFragment();

                    FragmentLifeCycle fragmentToShow = (FragmentLifeCycle) adapter.getItem(1);
                    fragmentToShow.onResumeFragment();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
