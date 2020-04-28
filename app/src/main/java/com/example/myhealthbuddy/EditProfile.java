package com.example.myhealthbuddy;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class EditProfile extends AppCompatActivity {
    private EditText editprofilename,editbirthdate;
    private Button editcancelbutton, editbutton;
    private DatabaseReference editprofRef,userRef;
    private FirebaseAuth mAuth;
    private String currentUser;
    private RadioButton male,female;
    FirebaseUser currentuser;
    TextView birthdate;

    private ImageView chosedate;
    private DatePickerDialog.OnDateSetListener mDatasetListner;
    private String date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editprofile);
        BottomNavigationView bottomnav;
        bottomnav = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomnav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                UserMenuSelector(menuItem);
                return false;
            }
        });
        currentuser = FirebaseAuth.getInstance().getCurrentUser();
        editprofilename = (EditText) findViewById(R.id.editname);

        birthdate=findViewById(R.id.editdate);
        male = (RadioButton) findViewById(R.id.male);
        female=(RadioButton) findViewById(R.id.female);

        editbutton = (Button) findViewById(R.id.editbutton);

        currentUser = currentuser.getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Patients");
        editprofRef = userRef.child(currentUser);

        editprofRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.hasChild("name")) {
                        String name = dataSnapshot.child("name").getValue().toString();
                        String date = dataSnapshot.child("birthdate").getValue().toString();
                        String gender = dataSnapshot.child("gender").getValue().toString();


                        editprofilename.setText(name);
                        birthdate.setText(date);

                        if (gender.equals("Male")){
                            male.setChecked(true);
                        }
                        if (gender.equals("Female")){
                            female.setChecked(true);
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        chosedate=(ImageView)findViewById(R.id.choosedatebtn);
        chosedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal=Calendar.getInstance();
                int year=cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);
                int day=cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog=new DatePickerDialog(EditProfile.this,android.R.style.Theme_DeviceDefault_Dialog_MinWidth,mDatasetListner,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });

        mDatasetListner=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                date=dayOfMonth+"/"+month+"/"+year;
                birthdate.setText(date);
            }
        };


        editbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    savingInformation();
                }
        });





    }

    private void savingInformation() {
        FirebaseDatabase.getInstance().getReference().child("Hospitals").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = editprofilename.getText().toString();
                    String gender="";
                    if (male.isChecked())
                        gender="Male";
                    if (female.isChecked())
                        gender="Female";
                    String date=editbirthdate.getText().toString();
                    HashMap hivesmap = new HashMap();
                    hivesmap.put("name", name);
                    hivesmap.put("birthdate", date);
                    hivesmap.put("gender", gender);

                    editprofRef.updateChildren(hivesmap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(EditProfile.this, "تم تعديل معلومات الحساب بنجاح...", Toast.LENGTH_SHORT).show();
                                //Intent intent = new Intent(Intent.ACTION_MAIN);
                                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                //startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(EditProfile.this, "حدث خطأ...", Toast.LENGTH_SHORT).show();

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
    private void UserMenuSelector(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_person:
                Intent intentsearchh=new Intent(EditProfile.this, Profile.class);
                startActivity(intentsearchh);
                break;
            case R.id.nav_home:
                Intent intentsearch=new Intent(EditProfile.this, HomePage.class);
                startActivity(intentsearch);
                break;}}
}

