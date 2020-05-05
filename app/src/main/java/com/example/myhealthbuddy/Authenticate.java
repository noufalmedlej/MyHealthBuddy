package com.example.myhealthbuddy;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import androidx.appcompat.app.AppCompatActivity;
public class Authenticate extends AppCompatActivity{

    private String verificationid;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private EditText Code;
    private Button check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authentication);
        mAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progressbar);
        check= findViewById(R.id.reset);
        Code = findViewById(R.id.mailll);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        // String username = extras.getString("name");
        // String password = extras.getString("p1");
        // String password2 = extras.getString("p2");
        //  String email = extras.getString("email");
        String phone = extras.getString("phone");
        // String nid = extras.getString("NID");

        sendVerificationCode(phone);

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String code = Code.getText().toString().trim();

                if ((code.isEmpty() || code.length() < 6)){

                    Code.setError("رمز التحقق غير صحيح");
                    Code.requestFocus();
                    return;
                }
                verifyCode(code);

            }
        });
    }

    private void verifyCode(String code){
        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationid, code);
      //  String ActCode = credential.getSmsCode();

  /*      if (ActCode==code){
            Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            String username = extras.getString("name");
            String password = extras.getString("p1");
            String password2 = extras.getString("p2");
            String email = extras.getString("email");
            String phone = extras.getString("phone");
            String nid = extras.getString("NID");
            CreateUserAccount(email,nid,password,username,phone);
        }
*/
        signInWithCredential(credential);
    }
    private void signInWithCredential(PhoneAuthCredential credential) {
        progressBar.setVisibility(View.INVISIBLE);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = getIntent();
                            Bundle extras = intent.getExtras();
                            String username = extras.getString("name");
                            String password2 = extras.getString("p2");
                            String email = extras.getString("email");
                            String phone = extras.getString("phone");
                            String nid = extras.getString("NID");
                            CreateUserAccount(email,nid,password2,username,phone);

                        } else {
                            Toast.makeText(Authenticate.this, "رمز التحقق غير صحيح", Toast.LENGTH_LONG).show();
                            // The verification code entered was invalid
                        }
                    }

                });
    }

    private void sendVerificationCode(String number){

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationid = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null){
                progressBar.setVisibility(View.VISIBLE);
                Code.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(Authenticate.this, e.getMessage(),Toast.LENGTH_LONG).show();

        }
    };

    private void CreateUserAccount(final String email,final String nid,final String password,final String name,final String phone) {

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //sendUserToSetUpActivity();

                            showMessage("تم انشاء حسابك");
                            String Currentuser=mAuth.getCurrentUser().getUid();
                            DatabaseReference Userref= FirebaseDatabase.getInstance().getReference().child("Patients").child(Currentuser);
                            HashMap userMap=new HashMap();
                            userMap.put("email",email);
                            userMap.put("name",name);
                            userMap.put("national_id",nid);
                            userMap.put("phone",phone);
                            Userref.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {

                                    if (task.isSuccessful()){
                                        Toast.makeText(Authenticate.this, "تم تسجيل بياناتك", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(Authenticate.this, SetUp.class);// go to set up
                                        startActivity(intent);
                                    }

                                    else{

                                            String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                                            showMessage(errorCode);

                                            switch (errorCode) {

                                                case "ERROR_EMAIL_ALREADY_IN_USE":
                                                    showMessage("البريد الإلكتروني مسجل مسبقًا.");
                                                    break;

                                                case "ERROR_USER_MISMATCH":
                                                    showMessage("البريد الإلكتروني مسجل مسبقًا.");
                                                    break;

                                                case "ERROR_INVALID_EMAIL":
                                                    showMessage("الرجاء إدخال بريد إلكتروني صحيح.");
                                                    break;

                                                case "ERROR_WEAK_PASSWORD":
                                                    showMessage("يجب أن تتكون كلمة المرور من ٦ أحرف على الأقل.");
                                                    break;


                                            }
                                    }

                                }
                            });
                        }
                        else
                            showMessage("فشلت عملية التسجيل" + task.getException().getMessage());
                    }
                });
    }
    private void showMessage(String message) {

        Toast.makeText(getApplicationContext(), message,Toast.LENGTH_LONG).show();
    }

}
