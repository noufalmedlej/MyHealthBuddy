package com.example.myhealthbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewRecord extends AppCompatActivity {

    String recordID,hid,pid;
    TextView doctorNameT,doctorsSpecialtyT, patientNameT,hospitalNameT,creationDate,creationTime,patientN,patientID,patientG;
    DatabaseReference recordRef, patientRef ,hospitalRef;
    TextView testDateT,noteT;
    Button attachmentView,done;
    BottomNavigationView bottomnav;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_record);

        bottomnav =  findViewById(R.id.bottom_navigation);
        bottomnav.setSelectedItemId(R.id.nav_home);
        bottomnav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                UserMenuSelector(menuItem);
                return false;
            }
        });

        recordID = getIntent().getExtras().get("Rid").toString();

        doctorNameT=findViewById(R.id.doctorName);
        doctorsSpecialtyT=findViewById(R.id.doctorsSpecialty);
        patientNameT=findViewById(R.id.patientName);
        hospitalNameT=findViewById(R.id.hospitalName);
        creationDate=findViewById(R.id.creationDate);
        creationTime=findViewById(R.id.creationTime);
        patientN=findViewById(R.id.patientName);
        patientID=findViewById(R.id.patientID);
        patientG=findViewById(R.id.gender);

        done=findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        testDateT=findViewById(R.id.testDate);
        noteT=findViewById(R.id.note);


        recordRef = FirebaseDatabase.getInstance().getReference().child("Records").child(recordID);
        recordRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //record info
                String medication,impression,note,tdate;


                if(dataSnapshot.hasChild("note")){
                    note=dataSnapshot.child("note").getValue().toString();
                    noteT.setText(note);
                }else{
                    noteT.setVisibility(View.GONE);
                    findViewById(R.id.noteL).setVisibility(View.GONE);
                }
                if(dataSnapshot.hasChild("testDate")){
                    tdate=dataSnapshot.child("testDate").getValue().toString();
                    testDateT.setText(tdate);
                }else{
                    testDateT.setVisibility(View.GONE);
                    findViewById(R.id.testDateL).setVisibility(View.GONE);
                }
                if(dataSnapshot.hasChild("testDate")){
                    tdate=dataSnapshot.child("testDate").getValue().toString();
                    testDateT.setText(tdate);
                }else{
                    testDateT.setVisibility(View.GONE);
                    findViewById(R.id.testDateL).setVisibility(View.GONE);
                }

                //doctor who wrote this record
                String doctorName, doctorsSpecialty;

                doctorName=dataSnapshot.child("doctorName").getValue().toString();
                doctorNameT.setText(doctorName);

                doctorsSpecialty=dataSnapshot.child("doctorSpeciality").getValue().toString();
                doctorsSpecialtyT.setText(doctorsSpecialty);

                creationDate.setText(dataSnapshot.child("dateCreated").getValue().toString());
                creationTime.setText(dataSnapshot.child("timeCreated").getValue().toString());
                hid=dataSnapshot.child("hospital").getValue().toString();
                pid=dataSnapshot.child("pid").getValue().toString();


                //patient
                patientRef = FirebaseDatabase.getInstance().getReference().child("Patients").child(pid);
                patientRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        patientN.setText(dataSnapshot.child("name").getValue().toString());
                        patientID.setText(dataSnapshot.child("national_id").getValue().toString());
                        patientG.setText(dataSnapshot.child("gender").getValue().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                //hospital
                hospitalRef = FirebaseDatabase.getInstance().getReference().child("Hospitals").child(hid);
                hospitalRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        hospitalNameT.setText(dataSnapshot.child("Name").getValue().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



                //pdf
                if(dataSnapshot.hasChild("file")){
                    attachmentView=findViewById(R.id.attachmentView);
                    attachmentView.setVisibility(View.VISIBLE);
                    final String url = dataSnapshot.child("file").getValue().toString();
                    attachmentView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //pdfviewer
                            Intent redirect = new Intent(ViewRecord.this,ReadActivity.class);
                            redirect.putExtra("url", url);
                            redirect.putExtra("recordID", recordID);
                            startActivity(redirect);
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void UserMenuSelector(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_share:
                Intent intent = new Intent(ViewRecord.this, ViewRecordtoShare.class);
                startActivity(intent);
                break;

            case R.id.nav_request:
                Intent intentrequest=new Intent(ViewRecord.this, ViewRequests.class);
                startActivity(intentrequest);
                break;

            case R.id.nav_person:
                Intent intentsearch=new Intent(ViewRecord.this, Profile.class);
                startActivity(intentsearch);
                break;
        }
    }


}
