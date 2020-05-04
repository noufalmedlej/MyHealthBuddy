package com.example.myhealthbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class Settings extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        BottomNavigationView bottomnav;
        bottomnav = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomnav.setSelectedItemId(R.id.nav_person);
        bottomnav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                UserMenuSelector(menuItem);
                return false;
            }
        });
       CardView logout = findViewById(R.id.logout);
       CardView reset = findViewById(R.id.reset1);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent logout = new Intent(Settings.this, SigninActivity.class);
                logout.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(logout);
                finish();

            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reset = new Intent(Settings.this, ResetPassword.class);
                startActivity(reset);
}});}

    private void UserMenuSelector(MenuItem item) {
        switch (item.getItemId()) {


            case R.id.nav_share:
                Intent intent = new Intent(Settings.this, ViewRecordtoShare.class);
                startActivity(intent);
                break;

            case R.id.nav_request:
                Intent intentrequest = new Intent(Settings.this, ViewRequests.class);
                startActivity(intentrequest);
                break;

            case R.id.nav_home:
                Intent intentsearch = new Intent(Settings.this, HomePage.class);
                startActivity(intentsearch);
                break;

            case R.id.nav_not:
                Intent intentnot = new Intent(Settings.this, ViewRecordsNotificatins.class);
                startActivity(intentnot);
                break;
        }

    }
}

