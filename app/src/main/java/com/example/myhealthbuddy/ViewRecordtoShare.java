package com.example.myhealthbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import android.widget.Toast;


public class ViewRecordtoShare extends AppCompatActivity {

    Query RecordRef;
    DatabaseReference DoctorRef;
    DatabaseReference PatientRef;
    DatabaseReference HospitalRef;
    FirebaseAuth mAuth;
    String currentPatienid, HospitalID, HospitalName;
    RecyclerView RecordList;
    RecordAdapter mAdapter ;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<item_record> Arraylist;
    ArrayList<String> CkList;
    Button sendChk;
    SearchView searchView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_to_share);

        sendChk=(Button) findViewById(R.id.next);
        mAuth = FirebaseAuth.getInstance();
        currentPatienid = mAuth.getCurrentUser().getUid();
        RecordRef = FirebaseDatabase.getInstance().getReference().child("Records").orderByChild("pid").equalTo(currentPatienid);

        RecordList= findViewById(R.id.recordShareList);
        RecordList.setHasFixedSize(true);
        mLayoutManager=new LinearLayoutManager(this);
        RecordList.setLayoutManager(mLayoutManager);


        //Toast.makeText(this, HID, Toast.LENGTH_LONG).show();
        RecordRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Arraylist= new ArrayList<item_record>();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                    item_record r = dataSnapshot1.getValue(item_record.class);
                    r.rid= dataSnapshot1.getKey();
                   /// r.hname=GetHospitalName(r.rid);
                    Arraylist.add(r);
                }

                mAdapter= new RecordAdapter(ViewRecordtoShare.this,Arraylist);
                RecordList.setAdapter(mAdapter);
                mAdapter.setOnItemClickListener(new RecordAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(ArrayList<String> mRecorslist) {

                        CkList= mRecorslist;
                    }
                });

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ViewRecordtoShare.this, "Some thong went wrong ",Toast.LENGTH_SHORT).show();
            }
        });

        sendChk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // get selected
                Intent intent =new Intent(ViewRecordtoShare.this,ChooseDocToShareWith.class);
                if(CkList.size()>0){
                intent.putExtra("list",CkList);
                startActivity(intent); }
                else{
                    Toast.makeText(ViewRecordtoShare.this, "No record selected ",Toast.LENGTH_SHORT).show();
                }
                //sent to other Activity
            }
        });



    }


    public void Browse() {
/*
        Query DisplayInfiQuere =RecordRef.orderByChild("pid").startAt(currentPatienid).endAt(currentPatienid+"\uf8ff");

        FirebaseRecyclerAdapter<item_record, ViewAllRecord.RecordViweHolder> FirebaseRecycleAdapter
                = new FirebaseRecyclerAdapter<item_record, RecordViweHolder>
                (
                        item_record.class,
                        R.layout.record_item,
                        ViewAllRecord.RecordViweHolder.class,
                        DisplayInfiQuere
                ){
            @Override
            protected void populateViewHolder(final ViewAllRecord.RecordViweHolder recordViweHolder, final item_record module, final int i) {

                recordViweHolder.setDate(module.getDate());

                PatientRef = FirebaseDatabase.getInstance().getReference().child("Patients").child(currentPatienid);
                PatientRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) { if(dataSnapshot.exists()){
                        String  Patient_name= dataSnapshot.child("name").getValue().toString();
                        recordViweHolder.setPatientName(Patient_name);
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



                DoctorRef = FirebaseDatabase.getInstance().getReference().child("Doctors").child(module.getDid());
                DoctorRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String  Doctor_Name=dataSnapshot.child("name").getValue().toString();
                        HospitalID =dataSnapshot.child("hospital").getValue().toString();

                        recordViweHolder.setDoctorName(Doctor_Name);
                        recordViweHolder.setHospitalName( GetHospitalName());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                HospitalRef= FirebaseDatabase.getInstance().getReference().child("Hospitals").child("122");
                HospitalRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        HospitalName =dataSnapshot.child("Name").getValue().toString();
                        recordViweHolder.setHospitalName(HospitalName);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


               /* recordViweHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String HCPuId= getRef(i).getKey();
                        Intent intent = new Intent(ChooseDocToShareWith.this, ShareRecord.class);
                        intent.putExtra("HCPuID", HCPuId);
                        intent.putExtra("HCPID",HCPID);
                        startActivity(intent);
                    }
                })*/
/*
            }
        };
        RecordList.setAdapter(FirebaseRecycleAdapter);
        */

    }

    public String GetHospitalName(String rid){


        DoctorRef=  FirebaseDatabase.getInstance().getReference().child("Records").child(rid);
        DoctorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HospitalID= dataSnapshot.child("did").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


         PatientRef=FirebaseDatabase.getInstance().getReference().child("Doctors");
        PatientRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HospitalID=dataSnapshot.child(HospitalID).child("hospital").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        HospitalRef= FirebaseDatabase.getInstance().getReference().child("Hospitals").child(HospitalID);
        HospitalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 HospitalName =dataSnapshot.child("Name").getValue().toString();
               // recordViweHolder.setHospitalName(HospitalName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return HospitalName;
    }




}