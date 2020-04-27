package com.example.myhealthbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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

public class Profile extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);


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


        CardView set = findViewById(R.id.textView5);
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intt = new Intent(Profile.this, Settings.class);
                startActivity(intt);
            }
        });
        ImageButton edit= findViewById(R.id.editbut);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toedit = new Intent(Profile.this, EditProfile.class);
                startActivity(toedit);
            }
        });
        final TextView name= findViewById(R.id.nameee);
        final TextView mail= findViewById(R.id.mailview);
        final TextView phone= findViewById(R.id.phoneview);
        final TextView nid= findViewById(R.id.nid);
        final TextView date= findViewById(R.id.dateview);
        final TextView gender= findViewById(R.id.genderview);
        String current= FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Patients").child(current);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name.setText(dataSnapshot.child("name").getValue().toString());
                mail.setText(dataSnapshot.child("email").getValue().toString());
                phone.setText(dataSnapshot.child("phone").getValue().toString());
                nid.setText(dataSnapshot.child("national_id").getValue().toString());
                date.setText(dataSnapshot.child("birthdate").getValue().toString());
                if (dataSnapshot.child("gender").getValue().toString().equals("Male"))
                gender.setText("ذكر");
                if (dataSnapshot.child("gender").getValue().toString().equals("Female"))
                    gender.setText("أنثى");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void UserMenuSelector(MenuItem item) {
        switch (item.getItemId()){

            case R.id.nav_share:
                Intent intent = new Intent(Profile.this, ViewRecordtoShare.class);
                startActivity(intent);
                break;

            case R.id.nav_request:
                Intent intentrequest=new Intent(Profile.this, ViewRequests.class);
                startActivity(intentrequest);
                break;

            case R.id.nav_home:
                Intent intentsearch=new Intent(Profile.this, HomePage.class);
                startActivity(intentsearch);
                break;
        }
    }
}
