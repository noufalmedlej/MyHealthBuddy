package com.example.myhealthbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomePage extends AppCompatActivity {

    ImageView img;
    BottomNavigationView bottomnav;
    CardView Mypills,MyDoc,MyBloodTests,Myx_Rays,MyVital,Myreports;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    setContentView(R.layout.homepage);


    final TextView name= findViewById(R.id.name);


    bottomnav =  findViewById(R.id.bottom_navigation);
    bottomnav.setSelectedItemId(R.id.nav_home);
    bottomnav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                UserMenuSelector(menuItem);
                return false;
            }
    });



    String current= FirebaseAuth.getInstance().getCurrentUser().getUid();
   DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Patients").child(current).child("name");
   ref.addValueEventListener(new ValueEventListener() {
       @Override
       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
           String n = dataSnapshot.getValue().toString();
           name.setText(n);
       }

       @Override
       public void onCancelled(@NonNull DatabaseError databaseError) {

       }
   });

        MyDoc = findViewById(R.id.doctorcard);
        MyDoc.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {
                                         Intent intentMyDoc = new Intent(HomePage.this, MyHCPs.class);
                                         startActivity(intentMyDoc);
                                     }
                                 }
        );

        Mypills = findViewById(R.id.pillscard);
        Mypills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intentPills=new Intent(HomePage.this, ViewAllBloodTest.class);
                intentPills.putExtra("type",1);
               startActivity(intentPills);
            }
        });


        MyBloodTests= findViewById(R.id.blodcard);
        MyBloodTests.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {
                                         Intent intentMyBloodTests = new Intent(HomePage.this, ViewAllBloodTest.class);
                                         intentMyBloodTests.putExtra("type",2);
                                         startActivity(intentMyBloodTests);
                                     }
                                 }
        );
        Myx_Rays= findViewById(R.id.Xraycard);
        Myx_Rays.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                              Intent intentMyx_Rays = new Intent(HomePage.this, ViewAllBloodTest.class);
                                              intentMyx_Rays.putExtra("type",3);
                                              startActivity(intentMyx_Rays);
                                            }
                                        }
        );
        MyVital= findViewById(R.id.cardiocard);
        MyVital.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                             Intent intentMyVital = new Intent(HomePage.this, ViewAllBloodTest.class);
                                            intentMyVital.putExtra("type",4);
                                            startActivity(intentMyVital);
                                        }
                                    }
        );
        Myreports = findViewById(R.id.reporcard);
        Myreports.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           Intent intentRecord = new Intent(HomePage.this, ViewAllBloodTest.class);
                                           intentRecord.putExtra("type",5);
                                           startActivity(intentRecord);
                                       }
                                   }
        );


}
    private void UserMenuSelector(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_share:
                Intent intent = new Intent(HomePage.this, ViewRecordtoShare.class);
                startActivity(intent);
                break;

            case R.id.nav_request:
                Intent intentrequest=new Intent(HomePage.this, ViewRequests.class);
                startActivity(intentrequest);
                break;

            case R.id.nav_person:
                Intent intentsearch=new Intent(HomePage.this, Profile.class);
                startActivity(intentsearch);
                break;
        }
    }
}
