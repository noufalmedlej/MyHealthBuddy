package com.example.myhealthbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ViewRecordsNotificatins extends AppCompatActivity {

    BottomNavigationView Doctorbottomnav;
    private RecyclerView notificationList;
    String uid;
    FirebaseAuth mAuth;
    Query RecordRef;
    private DatabaseReference PatientRef,HospitalRef,Recordsref;
    View nonot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_records_notificatins);

        Doctorbottomnav=findViewById(R.id.bottom_navigation);
        Doctorbottomnav.setSelectedItemId(R.id.nav_not);
        Doctorbottomnav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                DoctorMenuSelector(menuItem);
                return false;
            }
        });


        mAuth = FirebaseAuth.getInstance();
        uid=mAuth.getCurrentUser().getUid().toString();


        notificationList=(RecyclerView)findViewById(R.id.NotificationList);
        notificationList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        notificationList.setLayoutManager(linearLayoutManager);

        nonot=findViewById(R.id.nonotif);




        RecordRef = FirebaseDatabase.getInstance().getReference().child("Records").orderByChild("pid").equalTo(uid);


        RecordRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.getChildrenCount()==0){//no notii
                    notificationList.setVisibility(View.INVISIBLE);
                    nonot.setVisibility(View.VISIBLE);
                }
                else {
                    displayNotifications(uid);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    private void displayNotifications(String currentPatient) {



        Query q=FirebaseDatabase.getInstance().getReference().child("Records").orderByChild("date_order");

        FirebaseRecyclerAdapter<item_record,NotificationsViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<item_record, NotificationsViewHolder>(item_record.class,R.layout.record_item,NotificationsViewHolder.class,q) {
                    @Override
                    protected void populateViewHolder(final NotificationsViewHolder notificationsViewHolder, final item_record records, int i) {

                        final ViewGroup.LayoutParams params = notificationsViewHolder.mView.getLayoutParams();
                        notificationsViewHolder.mView.setVisibility(View.GONE);

                        params.height = 0;
                        params.width = 0;

                        if(records.getPid().equals(uid)) {
                            notificationsViewHolder.mView.setVisibility(View.VISIBLE);
                            params.width = 1000;
                            params.height = 400;


                        notificationsViewHolder.setDoctorName(records.getDoctorName());
                        notificationsViewHolder.setRid(records.getRid());
                        notificationsViewHolder.setType(records.getType());


                        PatientRef = FirebaseDatabase.getInstance().getReference().child("Patients");
                        PatientRef.child(records.getPid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {

                                    String Patient_name = dataSnapshot.child("name").getValue().toString();

                                    notificationsViewHolder.setPatientName(Patient_name);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });

                        HospitalRef = FirebaseDatabase.getInstance().getReference().child("Hospitals");
                        HospitalRef.child(records.getHospital()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {

                                    String hospital_name = dataSnapshot.child("Name").getValue().toString();

                                    notificationsViewHolder.setHospital(hospital_name);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });


                        final String Rid = getRef(i).getKey();
                        notificationsViewHolder.setRid(Rid);

                        Recordsref = FirebaseDatabase.getInstance().getReference().child("Records");
                        Recordsref.child(Rid).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {

                                    String date = dataSnapshot.child("dateCreated").getValue().toString();

                                    notificationsViewHolder.setDateCreated(date);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });


                        notificationsViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                switch (records.getType()) {
                                    case 1:
                                        Intent intentPills = new Intent(ViewRecordsNotificatins.this, ViewPrescription.class);
                                        intentPills.putExtra("Rid", Rid);
                                        startActivity(intentPills);
                                        break;
                                    case 2:
                                        Intent intentMyBloodTests = new Intent(ViewRecordsNotificatins.this, ViewBloodTest.class);
                                        intentMyBloodTests.putExtra("Rid", Rid);
                                        startActivity(intentMyBloodTests);
                                        break;
                                    case 3:
                                        Intent intentMyx_Rays = new Intent(ViewRecordsNotificatins.this, ViewXRay.class);
                                        intentMyx_Rays.putExtra("Rid", Rid);
                                        startActivity(intentMyx_Rays);
                                        break;
                                    case 4:
                                        Intent intentMyVital = new Intent(ViewRecordsNotificatins.this, ViewVitalSigns.class);
                                        intentMyVital.putExtra("Rid", Rid);
                                        startActivity(intentMyVital);
                                        break;
                                    case 5:
                                        Intent intentRecord = new Intent(ViewRecordsNotificatins.this, ViewRecord.class);
                                        intentRecord.putExtra("Rid", Rid);
                                        startActivity(intentRecord);
                                        break;
                                }
                            }
                        });

                    }
                }

                };

        notificationList.setAdapter(firebaseRecyclerAdapter);

    }
    public static class NotificationsViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public NotificationsViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
        }
        public void setDateCreated(String date){
            TextView datecreated=(TextView)mView.findViewById(R.id.record_date);
            datecreated.setText(date);
            CheckBox chk=(CheckBox)mView.findViewById(R.id.chk);
            chk.setVisibility(View.INVISIBLE);
        }

        public void setDoctorName(String doctorName){
            TextView doctor=(TextView)mView.findViewById(R.id.d_name);
            doctor.setText(doctorName);
        }

        public void setRid(String rid){
            TextView recordid=(TextView)mView.findViewById(R.id.Rid);
            recordid.setText(rid);
        }

        public void setHospital(String hname){
            TextView hospital=(TextView)mView.findViewById(R.id.hname);
            hospital.setText(hname);
        }
        public void setPatientName(String Pname) {
            TextView name=(TextView)mView.findViewById(R.id.patient_name);
            name.setText(Pname);
        }


        public void setType(int type){
            TextView t=(TextView)mView.findViewById(R.id.Type);
            if(type==1)
                t.setText("وصفة طبية");
            if(type==2)
                t.setText(" تحليل طبي ");
            if(type==3)
                t.setText(" أشعة");
            if(type==4)
                t.setText("علامات حيوية");
            if(type==5)
                t.setText("تقرير طبي ");
        }

    }


    private void DoctorMenuSelector(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.nav_share:
                Intent intentShare = new Intent(ViewRecordsNotificatins.this, ViewRecordtoShare.class);
                startActivity(intentShare);
                break;

            case R.id.nav_person:
                Intent intentProfile = new Intent(ViewRecordsNotificatins.this, Profile.class);
                startActivity(intentProfile);
                break;

            case R.id.nav_home:
                Intent intentHome = new Intent(ViewRecordsNotificatins.this, HomePage.class);
                startActivity(intentHome);
                break;

            case R.id.nav_request:
                Intent intentRequest= new Intent(ViewRecordsNotificatins.this, ViewRequests.class);
                startActivity(intentRequest);
                break;


        }

    }
}
