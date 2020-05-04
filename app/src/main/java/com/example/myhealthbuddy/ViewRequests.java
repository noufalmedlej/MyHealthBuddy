package com.example.myhealthbuddy;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.myhealthbuddy.ui.main.SectionsPagerAdapter;

public class ViewRequests extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_requests);

        BottomNavigationView bottomnav;
        bottomnav = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomnav.setSelectedItemId(R.id.nav_request);
        bottomnav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                UserMenuSelector(menuItem);
                return false;
            }
        });


        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter( getSupportFragmentManager());

        ViewPager viewPager = findViewById(R.id.view_pager);
        setUpViewPager(viewPager);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);




        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentrequest=new Intent(ViewRequests.this, CreateRequest.class);
                startActivity(intentrequest);
            }
        });
    }
    private void setUpViewPager(ViewPager viewPager){
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter( getSupportFragmentManager());
        sectionsPagerAdapter.addFragment(new ActiveRequests(),"نشطة");
        sectionsPagerAdapter.addFragment(new CanceledRequests(),"مرفوضة");
        sectionsPagerAdapter.addFragment(new ClosedRequests(),"مغلقة");
        viewPager.setAdapter(sectionsPagerAdapter);
    }

    private void UserMenuSelector(MenuItem item) {
        switch (item.getItemId()){

            case R.id.nav_share:
                Intent intentshare = new Intent(ViewRequests.this, ViewRecordtoShare.class);
                startActivity(intentshare);
                break;

            case R.id.nav_home:
                Intent intentrequest=new Intent(ViewRequests.this, HomePage.class);
                startActivity(intentrequest);
                break;

            case R.id.nav_person:
                Intent intentsearch=new Intent(ViewRequests.this, Profile.class);
                startActivity(intentsearch);
                break;

            case R.id.nav_not:
                Intent intentnot = new Intent(ViewRequests.this, ViewRecordsNotificatins.class);
                startActivity(intentnot);
                break;
        }
    }
}