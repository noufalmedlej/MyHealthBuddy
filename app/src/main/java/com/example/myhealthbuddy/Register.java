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


public class Register extends AppCompatActivity {



    public EditText UserEmail,UserNID, userPassword, userPassword2,userName,UserPhone;

    private Button regBtn;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private Button login;
    DatabaseReference Userref;
    private FirebaseAuth mAuth;
    private String Currentuser;
    boolean exists;
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



                if( Email.isEmpty() || Password.isEmpty() || Password2.isEmpty() || !Password.equals(Password2)){
                    showMessage("يرجى التأكد من بياناتك");
                    return;

                }
                if ( Password.length()<6){
                    userPassword.setError("يجب ان لا يقل الرمز السري عن 6 رموز");
                    userPassword.requestFocus();
                    return;
                }

                if (phone.isEmpty()|| phone.length()<9 || phone.length()>9) {
                    UserPhone.setError("رقم الجوال غير صحيح");
                    UserPhone.requestFocus();
                    return;
                }
                if ( NID.isEmpty() || NID.length()<10 || NID.length()>10 ){
                    UserNID.setError("رقم الهوية الوطنية غير صحيح");
                }
                // to check if the NID is already in use
                DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Patients");

                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postsnapshot : dataSnapshot.getChildren()) {
                            String key1 = postsnapshot.getKey();
                                if ((dataSnapshot.child(key1).child("national_id").getValue().equals(NID))){
                                    UserNID.setError("رقم الهوية الوطنية مستخدم");
                                    exists=true;
                                    return;
                                }}
                        }



                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                if (exists==true)
                    return;






        final String phonee="+966"+phone;
                DatabaseReference authentication= FirebaseDatabase.getInstance("https://mockup-8ca7f.firebaseio.com").getReference().child("info");
                authentication.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean flag = false;
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
                                    extras.putString("p1", Password);
                                    extras.putString("p2", Password2);
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
        if (currentUser!=null)
        {

            SendUserToMainActivity();

        }

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


}


