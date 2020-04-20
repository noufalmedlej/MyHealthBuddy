package com.example.myhealthbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ShareRecord extends AppCompatActivity {

    private Button share,cancel;
    private String HCPuID ,HCPID,HCPName,allIds="";
    private String currentUser;
    ArrayList<String> CkList;


    private TextView Ids,HCPIDText,hcpName;

    private DatabaseReference patienstRef,sharRef,recordRef;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_record);



        CkList =(ArrayList<String>)getIntent().getExtras().get("list");

        HCPuID =getIntent().getExtras().get("HCPuID").toString();
        HCPID= getIntent().getExtras().get("HCPID").toString();
        HCPName=getIntent().getExtras().get("HCPName").toString();


        Ids= findViewById(R.id.RecordsID);
        HCPIDText=findViewById(R.id.HCPID);
        hcpName=findViewById(R.id.hcpName);

        // viewing list of record ids before share
        for(String tmp: CkList){
            allIds= allIds+"\n"+tmp+"\n";
        }
        Ids.setText(allIds);
        HCPIDText.setText(HCPID);
        hcpName.setText(HCPName);



        //database Ref
        patienstRef= FirebaseDatabase.getInstance().getReference().child("Patients");
        sharRef= FirebaseDatabase.getInstance().getReference().child("Share");
        recordRef= FirebaseDatabase.getInstance().getReference().child("Records");
        mAuth= FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser().getUid();



        //share and Cancel button
        share= findViewById(R.id.sharebtn);
        cancel=findViewById(R.id.CancelShare);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(String tmp: CkList){
                    validate(tmp);
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToMainActivity();
            }
        });



    }

    private void validate(final String recordId ){

            Query query = recordRef.child(recordId);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        Share(HCPID,currentUser,recordId);

                    }
                    else {
                        Toast.makeText(getApplicationContext(),"حدثت مشكله  ",Toast.LENGTH_SHORT).show();

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
                                Toast.makeText(ShareRecord.this, "لقد قمت بمشاركه احد هذه التقارير بالفعل ", Toast.LENGTH_SHORT).show();
                                return;

                            }
                            else {
                                sharRef.child(key).updateChildren(reqmap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(ShareRecord.this, "تمت مشاركة التقارير", Toast.LENGTH_SHORT).show();
                                            sendUserToMainActivity();
                                        }
                                        else{
                                            Toast.makeText(ShareRecord.this, "حدثت مشكلة  ", Toast.LENGTH_SHORT).show();
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


}