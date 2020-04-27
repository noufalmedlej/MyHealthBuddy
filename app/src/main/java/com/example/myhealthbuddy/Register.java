package com.example.myhealthbuddy;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseError;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import com.google.firebase.database.DatabaseReference;

import java.util.regex.Pattern;


public class Register extends AppCompatActivity {



    public  EditText UserEmail,UserNID, userPassword, userPassword2,userName,UserPhone;
    public boolean flag;

    private Button regBtn;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
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
        UserPhone = findViewById(R.id.phone);
        regBtn = findViewById(R.id.regBtn);
        login = findViewById(R.id.loginn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, SigninActivity.class));
            }
        });

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String Email = UserEmail.getText().toString();
                final String Password= userPassword.getText().toString();
                final String Password2= userPassword2.getText().toString();
                final String Name =userName.getText().toString();
                final String NID = UserNID.getText().toString();
                final String phone= UserPhone.getText().toString();


//check for empty fields
                if( !notEmpty(Email,Name,NID, phone,Password, Password2)){
                    return;
                }
                // check password
                if ( !isValid(Password,Password2)){
                    return;
                }

// check phone
                if (!phoneValid(phone)) {
                    return;
                }
                // check NID
                if (!NationalIDValid(NID)){
                    return;
                }





                final String phonee="+966"+phone;
                DatabaseReference authentication= FirebaseDatabase.getInstance("https://mockup-8ca7f.firebaseio.com").getReference().child("info");
                authentication.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postsnapshot : dataSnapshot.getChildren()) {
                            String key1 = postsnapshot.getKey();
                            if((key1.equals(NID))) {
                                if ((dataSnapshot.child(key1).child("phone").getValue().equals(phonee))){
                                    flag=true;
                                    System.out.println(key1);
                                    System.out.println(dataSnapshot.child(key1).child("phone").getValue());
                                    Intent intent = new Intent(Register.this, Authenticate.class);
                                    Bundle extras = new Bundle();
                                    extras.putString("phone", phonee);
                                    extras.putString("NID", NID);
                                    extras.putString("name", Name);
                                    extras.putString("email", Email);
                                    extras.putString("p2", Password);
                                    intent.putExtras(extras);
                                    startActivity(intent);
                                    break;
                                }}
                        }
                        if (!flag)
                            showMessage("يرجى التأكد من ان بياناتك مطابقة للبيانات المسجلة في الأحوال المدنية");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        showMessage("يرجى التأكد من ان بياناتك مطابقة للبيانات المسجلة في الأحوال المدنية");
                    }
                });

            }});



    }
    @Override
    protected void onStart(){
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser=mAuth.getCurrentUser();
        // no because we want log in every time
        /*if (currentUser!=null)
        {

            SendUserToMainActivity();

        }*/

    }




    // method to show a message
    private void showMessage(String message) {

        Toast.makeText(getApplicationContext(), message,Toast.LENGTH_LONG).show();
    }
    private void sendUserToSetUpActivity(){
        Intent setupintent=new Intent(Register.this,HomePage.class);
        setupintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupintent);
        finish();
    }

    private void SendUserToMainActivity(){
        Intent mainintent =new Intent(Register.this,MainActivity.class);
        mainintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainintent);
        finish();

    }
// test password
    public boolean isValid(String passwordhere, String passwordhere2) {

        Pattern UpperCasePatten = Pattern.compile("[A-Z ]");
        Pattern lowerCasePatten = Pattern.compile("[a-z ]");
        Pattern digitCasePatten = Pattern.compile("[0-9 ]");

        boolean flag=true;


        if (!UpperCasePatten.matcher(passwordhere).find()||!lowerCasePatten.matcher(passwordhere).find()||!digitCasePatten.matcher(passwordhere).find()||passwordhere.length()<8) {
            userPassword.setError("يجب ان لا يقل الرمز السري عن 8 رموز ويحتوي على ارقام وحروف كبيرة وصغيرة ");
            userPassword.requestFocus();
            flag=false;
        }

        if (!(passwordhere.equals(passwordhere2))){
            showMessage("الرمز السري غير متطابق");
            flag=false;
        }


        return flag;

    }

    //test empty fields
    public boolean notEmpty(String Email, String Name, String NID, String phone,String password, String password2){
        boolean flag=true;
if( Email.isEmpty() || password2.isEmpty() || password.isEmpty()|| Name.isEmpty()||NID.isEmpty()||phone.isEmpty()){
        showMessage("يرجى تعبئة جميع الخانات");
flag=false;
}
        return false;
    }

    public boolean phoneValid(String phone){
        boolean flag=true;
    //length
       if ( phone.length()<9 || phone.length()>9) {
            UserPhone.setError("يحب ان يتكون رقم الجوال من 9 خانات");
            UserPhone.requestFocus();
       flag=false;
    }
       // start with 5
        if ( !phone.startsWith("5")) {
            UserPhone.setError("يحب ان يبدأ رقم الجوال ب5");
            UserPhone.requestFocus();
            flag=false;
        }
        // check for non numeric
        if ( !phone.matches("[0-9]+")) {
            UserPhone.setError("يجب ان لايحتوي رقم الجوال على رموز");
            UserPhone.requestFocus();
            flag=false;
        }
        return flag;

}
    public boolean NationalIDValid(String NID) {
        boolean flag = true;
        //length
        if (NID.length() < 10 || NID.length() > 10) {
            UserNID.setError("يحب ان يتكون رقم الهوية الوطنية من 10 خانات");
            UserNID.requestFocus();
            flag = false;
        }
        // check for non numeric
        if ( !NID.matches("[0-9]+")) {
            UserNID.setError("يجب ان لايحتوي رقم الوية الوطنية على رموز");
            UserNID.requestFocus();
            flag=false;
        }
        return flag;
    }
    }