package com.example.myhealthbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ViewAllRecord extends AppCompatActivity {

    DatabaseReference RecordRef ,DoctorRef, PatientRef ,HospitalRef;
    FirebaseAuth mAuth;
    String currentPatienid, HospitalID, HospitalName;
    RecyclerView RecordList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_record);

        RecordRef = FirebaseDatabase.getInstance().getReference().child("Records");
        mAuth = FirebaseAuth.getInstance();
        currentPatienid = mAuth.getCurrentUser().getUid();

        RecordList= findViewById(R.id.recordlist);
        RecordList.setHasFixedSize(true);
        RecyclerView myRecycler =  findViewById(R.id.recordlist);
        myRecycler.setLayoutManager(new LinearLayoutManager(this));
        RecordList.setLayoutManager(new LinearLayoutManager(this));
        //Toast.makeText(this, HID, Toast.LENGTH_LONG).show();

        Browse();
    }

    public void Browse() {

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
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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

                recordViweHolder.setCk(true);



            }
        };
        RecordList.setAdapter(FirebaseRecycleAdapter);
    }


    public static class RecordViweHolder extends RecyclerView.ViewHolder {
        View mViwe;


        //defolt constroctor
        public RecordViweHolder(@NonNull View itemView) {
            super(itemView);
            mViwe = itemView;
        }

        public void setDoctorName(String DName) {
            TextView MyName= (TextView)mViwe.findViewById(R.id.d_name);
            MyName.setText(DName);
        }
        public void setHospitalName(String HName) {
            TextView MyName= (TextView)mViwe.findViewById(R.id.hname);
            MyName.setText(HName);
        }
        public void setPatientName(String Pname) {
            TextView myID=(TextView)mViwe.findViewById(R.id.patient_name);
            myID.setText(Pname);
        }

        public void setDate(String Date) {
            TextView myDate=(TextView) mViwe.findViewById(R.id.record_date);
            myDate.setText(Date);
        }

        public void setCk(boolean ck) {
            CheckBox b =(CheckBox) mViwe.findViewById(R.id.chk);
            b.setVisibility(View.INVISIBLE);
            b.setEnabled(false);
        }
        public void setRid(String rid) {
            TextView myRid=(TextView) mViwe.findViewById(R.id.Rid);
            myRid.setText(rid);
        }
        public void setType(int type) {
            TextView mytybe=(TextView) mViwe.findViewById(R.id.Type);
            if(type==2)
                mytybe.setText("تحالييل المختبر ");
        }

    }

    public String GetHospitalName(){

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
