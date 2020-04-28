package com.example.myhealthbuddy;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Calendar;
import java.util.HashMap;


public class SetUp extends AppCompatActivity {
    private Button save;
    private DatePickerDialog.OnDateSetListener mDatasetListner;


    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef;
    private TextView dat;
    String currentUserId;
    private String date,gender;
    private RadioButton male,female;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setupactivity);

        mAuth=FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser().getUid();
        UsersRef= FirebaseDatabase.getInstance().getReference().child("Patients").child(currentUserId);

        male=(RadioButton)findViewById(R.id.male);
        female=(RadioButton)findViewById(R.id.female);
        save=(Button) findViewById(R.id.editbutton);
        dat=(TextView)findViewById(R.id.date);
        dat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal=Calendar.getInstance();
                int year=cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);
                int day=cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog=new DatePickerDialog(SetUp.this,android.R.style.Theme_DeviceDefault_Dialog_MinWidth,mDatasetListner,year,month,day);
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


        save.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view){
                SaveAccountSetupInformation();

            }
        });




    }



    private void SaveAccountSetupInformation(){

        if(male.isChecked())
            gender="Male";
        else
        if(female.isChecked())
            gender="Female";
        if(TextUtils.isEmpty(date)){
            Toast.makeText(this, "الرجاء ادخال تاريخ الميلاد", Toast.LENGTH_SHORT).show();
        }
        else
        {


            HashMap userMap=new HashMap();
            userMap.put("birthdate",date);
            userMap.put("gender",gender);


            UsersRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {

                    if (task.isSuccessful()){
                        sendUserToHomePage();
                        Toast.makeText(SetUp.this, "تم حفظ المعلومات بنجاح", Toast.LENGTH_LONG).show();

                    }

                    else{
                        Toast.makeText(SetUp.this, "حدث خطأ", Toast.LENGTH_SHORT).show();

                    }

                }
            });

        }

    }
    private void sendUserToHomePage(){
        Intent mainIntent=new Intent(SetUp.this,HomePage.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }




}
