package com.example.myhealthbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.StrictMode;
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

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

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

                    final HashMap Sharemap=new HashMap();
                    Sharemap.put("hcp_uid",HCPuID);
                    Sharemap.put("record_id",recordId);
                    Sharemap.put("patient_id",patientID);
                    Sharemap.put("patient_uid",currentUser);
                    Sharemap.put("share_id",currentUser+HCPuID);

                    final String key=patientID+HCPID+recordId;

                    sharRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                Toast.makeText(ShareRecord.this, "لقد قمت بمشاركه احد هذه التقارير بالفعل ", Toast.LENGTH_SHORT).show();
                                return;

                            }
                            else {
                                sharRef.child(key).updateChildren(Sharemap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(ShareRecord.this, "تمت مشاركة التقارير", Toast.LENGTH_SHORT).show();
                                            sendNotification(HCPuID);
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
    private void sendNotification(final String puid) {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    String send_email;


                    //This is a Simple Logic to Send Notification different Device Programmatically....

                    send_email =puid ;


                    try {
                        String jsonResponse;

                        URL url = new URL("https://onesignal.com/api/v1/notifications");
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setUseCaches(false);
                        con.setDoOutput(true);
                        con.setDoInput(true);

                        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                        con.setRequestProperty("Authorization", "Basic MWMzMDk5YzgtMTBjMC00N2U4LTgzNjAtNjk2Yjk3NjgxOTRm");
                        con.setRequestMethod("POST");

                        String strJsonBody = "{"
                                + "\"app_id\": \"8feeee1a-0ae6-4662-af58-51550ce5b903\","

                                + "\"filters\": [{\"field\": \"tag\", \"key\": \"User_uid\", \"relation\": \"=\", \"value\": \"" + send_email + "\"}],"

                                + "\"data\": {\"foo\": \"bar\"},"
                                + "\"contents\": {\"en\": \"A patient shared a record with you\"}"
                                + "}";


                        System.out.println("strJsonBody:\n" + strJsonBody);

                        byte[] sendBytes = strJsonBody.getBytes("UTF-8");
                        con.setFixedLengthStreamingMode(sendBytes.length);

                        OutputStream outputStream = con.getOutputStream();
                        outputStream.write(sendBytes);

                        int httpResponse = con.getResponseCode();
                        System.out.println("httpResponse: " + httpResponse);

                        if (httpResponse >= HttpURLConnection.HTTP_OK
                                && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                            Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        } else {
                            Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        }
                        System.out.println("jsonResponse:\n" + jsonResponse);

                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        });
    }


}