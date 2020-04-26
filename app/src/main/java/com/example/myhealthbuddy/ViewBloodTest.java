package com.example.myhealthbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class ViewBloodTest extends AppCompatActivity {

    String recordID,hid,pid;
    TextView doctorNameT,doctorsSpecialtyT, patientNameT,hospitalNameT,creationDate,creationTime,patientN,patientID,patientG;
    DatabaseReference recordRef, patientRef ,hospitalRef;
    TextView testDateT,noteT;
    TextView findingsT,impressionT,durationT;
    Button attachmentView,done;
    BottomNavigationView bottomnav;
    RecyclerView recyclerV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_blood_test);




        bottomnav = findViewById(R.id.bottom_navigation);
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
                    String note;

                    if(dataSnapshot.hasChild("note")){
                        note=dataSnapshot.child("note").getValue().toString();
                        noteT.setText(note);
                    }else{
                        noteT.setVisibility(View.GONE);
                        findViewById(R.id.noteL).setVisibility(View.GONE);
                    }


                    //doctor who wrote this record
                    String doctorName, doctorsSpecialty;

                    doctorName=dataSnapshot.child("doctorName").getValue().toString();
                    doctorNameT.setText(doctorName);

                    doctorsSpecialty=dataSnapshot.child("doctorSpeciality").getValue().toString();
                    doctorsSpecialtyT.setText(doctorsSpecialty);

                    creationDate.setText(dataSnapshot.child("dateCreated").getValue().toString()+" at ");
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
                            if(dataSnapshot.child("gender").getValue().toString().equals("female"))
                            patientG.setText("أنثى");
                            else patientG.setText("ذكر");

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
                                Intent redirect = new Intent(ViewBloodTest.this,ReadActivity.class);
                                redirect.putExtra("url", url);
                                redirect.putExtra("recordID", recordID);
                                startActivity(redirect);
                            }
                        });
                    }
                    //tests
                    if(dataSnapshot.hasChild("BloodTest")){
                        //recycler view
                        recyclerV=findViewById(R.id.recy);
                        recyclerV.setHasFixedSize(true);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());////?
                        linearLayoutManager.setReverseLayout(true);
                        linearLayoutManager.setStackFromEnd(true);
                        recyclerV.setLayoutManager(linearLayoutManager);
                        displayBloodTests();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }//onc

        private void displayBloodTests() {
            final Query query = recordRef.child(recordID).child("BloodTest");
            FirebaseRecyclerAdapter<bt_info,viewHolder> firebaseRecyclerAdapter =
                    new FirebaseRecyclerAdapter<bt_info, viewHolder>(
                            bt_info.class,
                            R.layout.bt_item,
                            viewHolder.class,
                            query
                    ) {
                        @Override
                        protected void populateViewHolder(viewHolder viewHolder, bt_info btinfo, final int i) {
                            //set information in each row
                            Double min, max,res;
                            max=btinfo.getNormalMax();
                            min=btinfo.getNormalMin();
                            res=btinfo.getResult();


                            viewHolder.setTest(btinfo.getTest()+"("+btinfo.getUnit()+")");
                            viewHolder.setNormalMax(max);
                            viewHolder.setNormalMin(min);
                            viewHolder.setResult(res);


                            //in between
                            if(res>=min && res<=max){
                                //green
                                viewHolder.colorbtn.setBackgroundColor(Color.parseColor("#4CAF50"));

                            }else {//less or greater
                                //red
                                viewHolder.colorbtn.setBackgroundColor(Color.parseColor("#CA0000"));
                            }


                            viewHolder.del.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                                    builder.setTitle("Delete Test!");
                                    builder.setMessage("Are you sure?");




                                    // Set click listener for alert dialog buttons
                                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch(which){
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    // User clicked the yes button
                                                    deleteItem(i);
                                                    break;

                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // User clicked the no button
                                                    break;
                                            }
                                        }
                                    };
                                    // Set the alert dialog yes button click listener
                                    builder.setPositiveButton("Yes", dialogClickListener);

                                    // Set the alert dialog no button click listener
                                    builder.setNegativeButton("No",dialogClickListener);

                                    AlertDialog dialog = builder.create();
                                    // Display the alert dialog on interface
                                    dialog.show();
                                }

                            });//delete onclicklistener
                        }

                        public void deleteItem(int position){
                            String key = getRef(position).getKey();
                            recordRef.child(recordID).child("BloodTest").child(key)
                                    .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(ViewBloodTest.this, "Test deleted.", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(ViewBloodTest.this, "Test not deleted.", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            });
                        }
                    };


            recyclerV.setAdapter(firebaseRecyclerAdapter);

        }

    public static class viewHolder extends RecyclerView.ViewHolder{
        View mView;
        ImageButton del= itemView.findViewById(R.id.deleteBT);
        Button colorbtn= itemView.findViewById(R.id.button3);

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setTest(String tn){
            TextView title = mView.findViewById(R.id.test);
            title.setText(tn);
        }

        public void setResult(Double tn){
            TextView title = mView.findViewById(R.id.result);
            title.setText(tn+"");
        }
        public void setNormalMin(Double tn){
            TextView title = mView.findViewById(R.id.normalMin);
            title.setText(tn+"");
        }
        public void setNormalMax(Double tn){
            TextView title = mView.findViewById(R.id.normalMax);
            title.setText(tn+"");
        }
    }

    private void UserMenuSelector(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_share:
                Intent intent = new Intent(ViewBloodTest.this, ViewRecordtoShare.class);
                startActivity(intent);
                break;

            case R.id.nav_request:
                Intent intentrequest=new Intent(ViewBloodTest.this, ViewRequests.class);
                startActivity(intentrequest);
                break;

            case R.id.nav_person:
                Intent intentsearch=new Intent(ViewBloodTest.this, Profile.class);
                startActivity(intentsearch);
                break;
        }
    }
}
