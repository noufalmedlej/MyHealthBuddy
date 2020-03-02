package com.example.myhealthbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {
    private EditText mail;
    private Button Reset;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resetpassword);

        BottomNavigationView bottomnav;
        bottomnav = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomnav.setSelectedItemId(R.id.nav_person);
        bottomnav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                UserMenuSelector(menuItem);
                return false;
            }
        });
        mail = findViewById(R.id.mailll);
        Reset = findViewById(R.id.reset);

        mAuth = FirebaseAuth.getInstance();

        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email =mail.getText().toString();

                if (Email.isEmpty()){
                    Toast.makeText(getApplicationContext(),"ادخل بريدك الإلكتروني",Toast.LENGTH_LONG).show();
                }
                else{
                    mAuth.sendPasswordResetEmail(Email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),"لقد تم ارسال رسالة تغيير كلمة المرور إلى بريدك الإلكتروني",Toast.LENGTH_LONG).show();
                                startActivity(new Intent(ResetPassword.this,SigninActivity.class));

                            }
                            else {
                                String Error = task.getException().getMessage();
                                Toast.makeText(getApplicationContext(),Error,Toast.LENGTH_LONG).show();

                            }
                        }
                    });
                }
            }
        });
    }
    private void UserMenuSelector(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_person:
                Intent intentsearchh=new Intent(ResetPassword.this, Profile.class);
                startActivity(intentsearchh);
                break;
            case R.id.nav_home:
                Intent intentsearch=new Intent(ResetPassword.this, HomePage.class);
                startActivity(intentsearch);
                break;}}
}

