package com.example.myhealthbuddy;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register extends AppCompatActivity {



    private EditText UserEmail,UserNID, userPassword, userPassword2,userName,UserPhone;
    private Button regBtn;

    private Button login;
DatabaseReference Userref;
    private FirebaseAuth mAuth;
    private String Currentuser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        UserEmail = findViewById(R.id.email);
        userPassword = findViewById(R.id.pass);
        userPassword2 = findViewById(R.id.pass2);
        userName = findViewById(R.id.name);
        UserNID = findViewById(R.id.ID);
        UserPhone=findViewById(R.id.phone);

        regBtn = findViewById(R.id.regBtn);
        login =findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, SigninActivity.class));
            }
        });

        mAuth = FirebaseAuth.getInstance();

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String Email = UserEmail.getText().toString();
                final String Password= userPassword.getText().toString();
                final String Password2= userPassword2.getText().toString();
                final String Name =userName.getText().toString();
                final String NID = UserNID.getText().toString();
                final String phone= UserPhone.getText().toString();


                if( Email.isEmpty() || Password.isEmpty() || Password2.isEmpty() || NID.isEmpty() || NID.length()<10 || NID.length()>10 || !Password.equals(Password2)){
                    showMessage("Please Verify All Fields");

                }
                else {

                    CreateUserAccount(Email,NID,Password,Name,phone);

                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // FirebaseUser currentUser=mAuth.getCurrentUser();
        /* if (currentUser!=null)
        {

            SendUserToMainActivity();

        }*/
    }

    private void CreateUserAccount(String email,final String nid,final String password,final String name,final String phone) {

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //sendUserToSetUpActivity();

                            showMessage("registered successfully");
                            Currentuser=mAuth.getCurrentUser().getUid();
                            Userref= FirebaseDatabase.getInstance().getReference().child("Users").child(Currentuser);
                            HashMap userMap=new HashMap();
                            userMap.put("name",name);
                            userMap.put("national ID",nid);
                            userMap.put("phone",phone);
                            Userref.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {

                                    if (task.isSuccessful()){
                                        Toast.makeText(Register.this, "Account created", Toast.LENGTH_LONG).show();
                                    }

                                    else{
                                        Toast.makeText(Register.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                        }
                        else
                            showMessage("Account Creation Failed" + task.getException().getMessage());
                    }
                });



    }

    // method to show a message
    private void showMessage(String message) {

        Toast.makeText(getApplicationContext(), message,Toast.LENGTH_LONG).show();
    }
    /*private void sendUserToSetUpActivity(){
        Intent setupintent=new Intent(Register.this,SetUpActivity.class);
        setupintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupintent);
        finish();
    }*/

    private void SendUserToMainActivity(){
        Intent mainintent =new Intent(Register.this,MainActivity.class);
        mainintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainintent);
        finish();

    }


}


