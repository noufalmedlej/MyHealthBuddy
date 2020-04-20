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
    Button reqbtn;
  //  ImageView img;
   // Button selectbtn;
    ImageView img;
    BottomNavigationView bottomnav;
    Button btn ;
    CardView img1,MyDoc;


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

    img1 = findViewById(R.id.pillscard);
    img1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Intent intentRecord=new Intent(HomePage.this, ViewAllRecord.class);
            startActivity(intentRecord);
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
                                         Intent intent = new Intent(HomePage.this, MyHCPs.class);
                                         startActivity(intent);
                                     }
                                 }
        );





        /*selectbtn =(Button)findViewById(R.id.selectbtn);
        selectbtn.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       Intent intent = new Intent(HomePage.this, ViewRecordtoShare.class);
                                       startActivity(intent);
                                   }
                               }
        );


        /*reqbtn=findViewById(R.id.Requestbtn);
        reqbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentreq=new Intent(HomePage.this, CreateRequest.class);
                startActivity(intentreq);

            }
        });*/

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
