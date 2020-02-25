package com.example.myhealthbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SigninActivity  extends AppCompatActivity {
    private EditText uid,Pass;
    private Button regBtn;
    private Button login;
    DatabaseReference Userref;
     FirebaseAuth mAuth;
 String Currentuser;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        uid = findViewById(R.id.uid);
        Pass = findViewById(R.id.password);
        regBtn = findViewById(R.id.reg);
        login = findViewById(R.id.login);
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SigninActivity.this, Register.class));
}});
    mAuth = FirebaseAuth.getInstance();
        login.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            final String ID =uid.getText().toString();
            final String Password = Pass.getText().toString();

            if( ID.isEmpty() || Password.isEmpty()||ID.length()!=10){
                showMessage("Please Verify All Field");
            }
            else{
                signin(ID,Password);
            }

        }
    });
}

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser=mAuth.getCurrentUser();
        if (currentUser!=null)
        {

            SendUserToMainActivity();


        }
    }

    public void signin(final String ID, final String password) {
        //first we find the email by looking into the user info

DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("Patients");
dbref.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
String email="";
        for (DataSnapshot postsnapshot : dataSnapshot.getChildren()) {
            String key = postsnapshot.getKey();
            if((dataSnapshot.child(key).child("national ID").getValue().equals(ID))){
             email = dataSnapshot.child(key).child("email").getValue().toString();
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){
                        showMessage("logged in succefully");
                        SendUserToMainActivity();
                    }
                    else {
                        showMessage("something went wrong");
                        // showMessage(task.getException().getMessage());
                    }

                }
            });
    break;}}}

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});



    }

    private void SendUserToMainActivity(){
       /* Intent mainintent =new Intent(SigninActivity.this,MainActivity.class);
        mainintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainintent);
        finish();*/

    }

    private void showMessage(String text) {
        Toast.makeText(getApplicationContext(), text,Toast.LENGTH_LONG).show();
    }
}
