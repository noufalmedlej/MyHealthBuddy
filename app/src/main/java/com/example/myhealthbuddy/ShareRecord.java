package com.example.myhealthbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;

public class ShareRecord extends AppCompatActivity {

    private Button share;

    private DatePickerDialog.OnDateSetListener mDatasetListner;
    private String recordId, HCPuID ,HCPID;
    private String currentUser;
    BottomNavigationView bottomnav;
    private TextView t1;

    private DatabaseReference patienstRef,docsRef,sharRef,recordRef;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_record);

        bottomnav = findViewById(R.id.bottom_navigation);
        bottomnav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                UserMenuSelector(menuItem);
                return false;
            }
        });



        HCPuID =getIntent().getExtras().get("HCPuID").toString();
        HCPID= getIntent().getExtras().get("HCPID").toString();
        t1= findViewById(R.id.textView);
        t1.setText(HCPID);


        //database Ref
        patienstRef= FirebaseDatabase.getInstance().getReference().child("Patients");
        docsRef= FirebaseDatabase.getInstance().getReference().child("Doctors");
        sharRef= FirebaseDatabase.getInstance().getReference().child("Share");
        recordRef= FirebaseDatabase.getInstance().getReference().child("Records");
        mAuth= FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser().getUid();


        //share button
        share= findViewById(R.id.sharebtn);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               validate();
            }
        });



    }

    private void validate(){
       // HCPID ,currentUser,
       recordId="114717951";

        /*
        if(HCPID.isEmpty()||date.equals("SelectDate")) {
            Toast.makeText(getApplicationContext(),"Please enter doctor's ID and date of the session",Toast.LENGTH_SHORT).show();
        }

        else {
*/
            Query query = recordRef.child(recordId);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        Share(HCPID,currentUser,recordId);

                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Record was not found, please enter another ID...",Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        //}
    }

    private void Share(final String HCPID, final String currentUser, final String recordId){

        patienstRef.child(currentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){


                    String patientID=dataSnapshot.child("national_id").getValue().toString();

                    // check if the record is shared before

                    final HashMap reqmap=new HashMap();
                    reqmap.put("hcp_uid",HCPuID);
                    reqmap.put("record_id",recordId);
                    reqmap.put("patient_id",patientID);
                    reqmap.put("patient_uid",currentUser);

                    final String key=patientID+HCPID+recordId;

                    sharRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                Toast.makeText(ShareRecord.this, "You already shared this record ...", Toast.LENGTH_SHORT).show();
                                return;

                            }
                            else {
                                sharRef.child(key).updateChildren(reqmap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(ShareRecord.this, "Record shared ", Toast.LENGTH_SHORT).show();
                                            sendUserToMainActivity();
                                        }
                                        else{
                                            Toast.makeText(ShareRecord.this, "Error", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            }
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
        Intent mainIntent=new Intent(ShareRecord.this,HomePage.class);
        startActivity(mainIntent);
    }

    private void UserMenuSelector(MenuItem item) {
        switch (item.getItemId()){

            case R.id.nav_home:
                Intent intenthome=new Intent(ShareRecord.this, HomePage.class);
                startActivity(intenthome);
                break;

            case R.id.nav_person:
                Intent intentsearch=new Intent(ShareRecord.this, Profile.class);
                startActivity(intentsearch);
                break;
        }
    }


}