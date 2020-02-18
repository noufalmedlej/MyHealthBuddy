package com.example.myhealthbuddy;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        mAuth=FirebaseAuth.getInstance();
        UsersRef= FirebaseDatabase.getInstance().getReference().child("Patients");

    }
    @Override
    protected void onStart(){
        super.onStart();
        FirebaseUser currentUser=mAuth.getCurrentUser();
        if (currentUser==null)
        {

            sendUserToStartActivity();

        }
        else
        {
            CheckUserExistence();
        }
    }

    private void sendUserToStartActivity() {
        Intent loginIntent = new Intent(MainActivity.this,Register.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    private void CheckUserExistence()
    {
        //mAuth is Firebase
        final String current_user_id=mAuth.getCurrentUser().getUid();
        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(current_user_id))
                {
                    SendUserToSetUpActivity();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void SendUserToSetUpActivity()
    {
        Toast.makeText(MainActivity.this, "User is recognised but null", Toast.LENGTH_SHORT).show();
    }
}
