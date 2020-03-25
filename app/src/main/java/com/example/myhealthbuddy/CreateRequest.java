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

public class CreateRequest extends AppCompatActivity {
    private Button sendreq;
    private EditText docId,not;
    private TextView dat;
    private RadioGroup radioGroup;
    private DatePickerDialog.OnDateSetListener mDatasetListner;
    private String doctorid,note,date;
    private String currentUser;
    BottomNavigationView bottomnav;


    private DatabaseReference patientRef,docsref,reqRef;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_request);

        sendreq=(Button)findViewById(R.id.sendreq);
        docId=(EditText)findViewById(R.id.DocId);
        dat=(TextView)findViewById(R.id.Date);
        not=(EditText)findViewById(R.id.Notes);

        bottomnav =  findViewById(R.id.bottom_navigation);
        bottomnav.setSelectedItemId(R.id.nav_request);
        bottomnav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                UserMenuSelector(menuItem);
                return false;
            }
        });

        dat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal=Calendar.getInstance();
                int year=cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);
                int day=cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog=new DatePickerDialog(CreateRequest.this,android.R.style.Theme_DeviceDefault_Dialog_MinWidth,mDatasetListner,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();


            }
        });
        mDatasetListner=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                date=dayOfMonth+"/"+month+"/"+year;
                dat.setText(date);
            }
        };

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //databaseref
        patientRef= FirebaseDatabase.getInstance().getReference().child("Patients");
        docsref= FirebaseDatabase.getInstance().getReference().child("Doctors");
        reqRef= FirebaseDatabase.getInstance().getReference().child("Requests");
        mAuth= FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser().getUid();



        sendreq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validaterequest();
            }
        });

    }

    private void validaterequest(){
        doctorid=docId.getText().toString();
        note=not.getText().toString();
        date=dat.getText().toString();
        if(doctorid.isEmpty()||date.isEmpty()) {
            Toast.makeText(getApplicationContext(),"Please enter doctor's ID and date of the session",Toast.LENGTH_SHORT).show();
        }

        else {

            Query query = docsref.orderByChild("id").equalTo(doctorid);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        createreq(doctorid,date,note);

                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Doctor was not found, please enter another ID...",Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    private void createreq(final String doctorid, final String date, final String note){

        patientRef.child(currentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //choose rad
                    RadioButton vsighn=(RadioButton)findViewById(R.id.vsigns);
                    RadioButton pres=(RadioButton)findViewById(R.id.pres);
                    RadioButton radreports=(RadioButton)findViewById(R.id.radreports);
                    RadioButton labreport=(RadioButton)findViewById(R.id.labreports);
                    RadioButton mreports=(RadioButton)findViewById(R.id.mreport);
                    String type="";
                    if(vsighn.isChecked())
                        type="vitalsigns";
                    else
                    if(pres.isChecked())
                        type="prescription";
                    else
                    if(radreports.isChecked())
                        type="Radiologyreport";
                    else
                    if(labreport.isChecked())
                        type="labreport";
                    else
                    if(mreports.isChecked())
                        type="medicalreport";


                    String patientID=dataSnapshot.child("national_id").getValue().toString();

                    // check if there is a request before

                    HashMap reqmap=new HashMap();
                    reqmap.put("patient_id",patientID);
                    reqmap.put("date",date);
                    reqmap.put("doctor_id",doctorid);
                    if(!note.isEmpty())
                    reqmap.put("notes",note);
                    reqmap.put("type",type);



                    String key=patientID+doctorid+date.replace("/","")+type;

                    reqRef.child(key).updateChildren(reqmap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){
                                Toast.makeText(CreateRequest.this, "Request sent", Toast.LENGTH_SHORT).show();
                                sendUserToMainActivity();
                            }
                            else{
                                Toast.makeText(CreateRequest.this, "Error", Toast.LENGTH_SHORT).show();
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

    private void sendUserToMainActivity(){
        Intent mainIntent=new Intent(CreateRequest.this,HomePage.class);
        startActivity(mainIntent);
    }

    private void UserMenuSelector(MenuItem item) {
        switch (item.getItemId()){

            case R.id.nav_home:
                Intent intenthome=new Intent(CreateRequest.this, HomePage.class);
                startActivity(intenthome);
                break;

            case R.id.nav_person:
                Intent intentsearch=new Intent(CreateRequest.this, Profile.class);
                startActivity(intentsearch);
                break;
        }
    }


}
