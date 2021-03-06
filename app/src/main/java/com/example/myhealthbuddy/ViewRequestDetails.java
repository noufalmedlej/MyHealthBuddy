package com.example.myhealthbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ViewRequestDetails extends AppCompatActivity {
    Toolbar mtoolbar;
    TextView DoctorNameText,DoctorIDText,RequestTypeText,AppointmentDateText,NoteText,RDate;
    Button WriteRecordbtn,CancelRequestBtn;
    ImageView RequestPic;
    String RequestKey,Type,reqdate;
    DatabaseReference Request,PatientsRef,declinedRequest,DocRef;
    BottomNavigationView bottomnav;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_request_details);





        //Page Fields
        DoctorNameText=(TextView) findViewById(R.id.page_request_name1);
        DoctorIDText=(TextView) findViewById(R.id.page_request_ID);
        RequestTypeText=(TextView)findViewById(R.id.page_request_type);
        RequestPic=(ImageView) findViewById(R.id.page_request_pic);
        AppointmentDateText=(TextView)findViewById(R.id.page_request_apDate);
        NoteText=(TextView)findViewById(R.id.page_request_note);
        RDate=(TextView)findViewById(R.id.page_request_requestdate);
        CancelRequestBtn=(Button)findViewById(R.id.page_request_cancelanddelete);

        NoteText.setMovementMethod(new ScrollingMovementMethod());

        Type=getIntent().getExtras().get("Type").toString();
        RequestKey = getIntent().getExtras().get("RequestKey").toString();


        bottomnav =  findViewById(R.id.bottom_navigation);
        bottomnav.setSelectedItemId(R.id.nav_request);
        bottomnav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                UserMenuSelector(menuItem);
                return false;
            }
        });


        Request= FirebaseDatabase.getInstance().getReference().child("Requests").child(Type).child(RequestKey);
        DocRef=FirebaseDatabase.getInstance().getReference().child("Doctors");


        Request.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String did=dataSnapshot.child("doctor_id").getValue().toString();
                    DoctorIDText.setText(did);
                    if (Type.equals("PendingRequests"))
                        reqdate=dataSnapshot.child("request_date").getValue().toString();
                    else if(Type.equals("DeclinedRequests"))
                        reqdate=dataSnapshot.child("declined_date").getValue().toString();

                    ////////// here change date

                    RDate.setText(reqdate);
                    String type=dataSnapshot.child("type").getValue().toString();
                    String apDate=dataSnapshot.child("date").getValue().toString();
                    AppointmentDateText.setText(apDate);
                    if(dataSnapshot.hasChild("notes")){
                        String note=dataSnapshot.child("notes").getValue().toString();
                        NoteText.setText(note);}
                    else
                        NoteText.setText("None");




                    if(type.equals("Other")){
                        RequestPic.setImageResource(R.drawable.medicalhistory);
                        RequestTypeText.setText("تقرير");
                    }
                    if(type.equals("X-Ray")){
                        RequestPic.setImageResource(R.drawable.skeleton);
                        RequestTypeText.setText("تقرير");
                    }
                    if(type.equals("Prescription")){
                        RequestPic.setImageResource(R.drawable.pills1);
                        RequestTypeText.setText("وصفة طبية");
                    }
                    if(type.equals("Vital Signs")){
                        RequestPic.setImageResource(R.drawable.heartbeat);
                        RequestTypeText.setText("مؤشرات حيوية");
                    }
                    if(type.equals("Blood Test")){
                        RequestPic.setImageResource(R.drawable.blooddonation);
                        RequestTypeText.setText("تحليل مختبر");

                    }


                    DocRef.child(dataSnapshot.child("doctor_uid").getValue().toString()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String dname =dataSnapshot.child("name").getValue().toString();
                            DoctorNameText.setText(dname);
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


        CancelRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteRequest();
            }
        });
    }
    private void DeleteRequest(){
        AlertDialog.Builder altb= new AlertDialog.Builder(ViewRequestDetails.this);
        altb.setMessage("هل متأكد من حذف الطلب؟").setCancelable(false)
                .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                                            Request.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    finish();
                                                    Toast.makeText(getApplicationContext(), "تم الحذف", Toast.LENGTH_LONG).show();
                                                }
                                            });


                    }
                })
                .setNegativeButton("لا", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                    }
                });

        AlertDialog alertDialog=altb.create();
        alertDialog.setTitle("حذف الطلب");
        alertDialog.show();


    }
    private void UserMenuSelector(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_share:
                Intent intent = new Intent(ViewRequestDetails.this, ViewRecordtoShare.class);
                startActivity(intent);
                break;

            case R.id.nav_request:
                Intent intentrequest=new Intent(ViewRequestDetails.this, ViewRequests.class);
                startActivity(intentrequest);
                break;

            case R.id.nav_person:
                Intent intentsearch=new Intent(ViewRequestDetails.this, Profile.class);
                startActivity(intentsearch);
                break;
            case R.id.nav_home:
                Intent intenthome=new Intent(ViewRequestDetails.this, HomePage.class);
                startActivity(intenthome);
                break;

            case R.id.nav_not:
                Intent intentnot=new Intent(ViewRequestDetails.this, ViewRecordsNotificatins.class);
                startActivity(intentnot);
                break;
        }
    }
}
