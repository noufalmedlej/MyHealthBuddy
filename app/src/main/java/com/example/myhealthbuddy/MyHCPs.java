package com.example.myhealthbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;


public class MyHCPs extends AppCompatActivity {

    private RecyclerView HCPsList;
    private DatabaseReference allSharesRef,DoctorRef,RecordsRef;
    private FirebaseAuth mAuth;
    BottomNavigationView bottomnav;
    private String currentPatient_uid;
    HCPAdapter mAdapter ;
    TextView Noresult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_hcps);

        mAuth = FirebaseAuth.getInstance();
        currentPatient_uid= mAuth.getCurrentUser().getUid();
        allSharesRef = FirebaseDatabase.getInstance().getReference().child("Share");
        DoctorRef = FirebaseDatabase.getInstance().getReference().child("Doctors");
        RecordsRef = FirebaseDatabase.getInstance().getReference().child("Records");



        bottomnav =  findViewById(R.id.bottom_navigation);
        //bottomnav.setSelectedItemId(R.id.nav_home);
        bottomnav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                UserMenuSelector(menuItem);
                return false;
            }
        });




        // RecyclerView
        HCPsList =findViewById(R.id.MyHCPsresult);
        HCPsList.setHasFixedSize(true);
        HCPsList.setLayoutManager(new LinearLayoutManager(this));


        Noresult =findViewById(R.id.Noresult);


        Browse();

    }

    public void Browse() {

        final ArrayList<HCPsResult> HCPArrayList= new ArrayList<>();
        final ArrayList<String> Ids= new ArrayList<>();


        DoctorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot2) {
                if (dataSnapshot2.exists()) {

                    Query PatientShare= allSharesRef.orderByChild("patient_uid").equalTo(currentPatient_uid + "\uf8ff");
                    PatientShare.addValueEventListener(new ValueEventListener() {


                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                            if (dataSnapshot1.exists()) {
                                String id;
                                for (DataSnapshot share : dataSnapshot1.getChildren()) {
                                    for (DataSnapshot ds : dataSnapshot2.getChildren()) {
                                        HCPsResult HCP;
                                        if (ds.getKey().equals(share.child("hcp_uid").getValue().toString())) {
                                            HCP = ds.getValue(HCPsResult.class);
                                            id = ds.getKey();
                                            if (!Ids.contains(id)) {
                                                Ids.add(id);
                                                //GetHospitalName(record.getKey());
                                                HCPArrayList.add(HCP);
                                            }
                                        }
                                    }

                                }

                            }
                            mAdapter = new HCPAdapter(MyHCPs.this, HCPArrayList);
                            HCPsList.setAdapter(mAdapter);
                            if (HCPArrayList.size() == 0) {
                                Noresult.setVisibility(View.VISIBLE);

                            } else Noresult.setVisibility(View.INVISIBLE);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                    // from recods

                   Query paitenrRecords =RecordsRef.orderByChild("pid").startAt(currentPatient_uid).endAt(currentPatient_uid + "\uf8ff");
                    paitenrRecords.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {

                            if (dataSnapshot1.exists()) {
                                String id;
                                for (DataSnapshot records : dataSnapshot1.getChildren()) {

                                    for (DataSnapshot doc : dataSnapshot2.getChildren()) {
                                        HCPsResult HCP;
                                        if (doc.getKey().equals(records.child("did").getValue().toString())) {
                                            HCP = doc.getValue(HCPsResult.class);
                                            id = doc.getKey();
                                            if (!Ids.contains(id)) {
                                                Ids.add(id);
                                                //GetHospitalName(record.getKey());
                                                HCPArrayList.add(HCP);
                                            }
                                        }
                                    }

                                }


                            }

                            mAdapter = new HCPAdapter(MyHCPs.this, HCPArrayList);
                            HCPsList.setAdapter(mAdapter);
                            if (HCPArrayList.size() == 0) {
                                Noresult.setVisibility(View.VISIBLE);

                            } else Noresult.setVisibility(View.INVISIBLE);


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }
            }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }

});
        }

    private void sendUserToMainActivity(){
        Intent mainIntent=new Intent(MyHCPs.this,HomePage.class);
        startActivity(mainIntent);
    }

    private void UserMenuSelector(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_share:
                Intent intent = new Intent(MyHCPs.this, ViewRecordtoShare.class);
                startActivity(intent);
                break;

            case R.id.nav_request:
                Intent intentrequest=new Intent(MyHCPs.this, ViewRequests.class);
                startActivity(intentrequest);
                break;

            case R.id.nav_person:
                Intent intentsearch=new Intent(MyHCPs.this, Profile.class);
                startActivity(intentsearch);
                break;
        }
    }
}
