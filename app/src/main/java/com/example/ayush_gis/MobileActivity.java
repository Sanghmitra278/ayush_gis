package com.example.ayush_gis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MobileActivity extends AppCompatActivity {
    TextView textDummyHintphone;
    EditText editPhone;
    //These are the objects needed
    //It is the verification id that will be sent to the user
    private String mVerificationId;
    Button btn_verify;
    //The edittext to input the code
    private TextView editTextCode;
    ProgressBar mProgressbar;
    //firebase auth object
    private FirebaseAuth mAuth;
    private TextView get_mob_no;
    String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile);
        textDummyHintphone = (TextView) findViewById(R.id.text_dummy_hint_phone);
        mAuth = FirebaseAuth.getInstance();

        editTextCode = findViewById(R.id.TextCode);
        btn_verify = (Button)findViewById(R.id.btn_verify);
        get_mob_no = findViewById(R.id.mob_no);
        editPhone = (EditText) findViewById(R.id.edit_phone);
        mProgressbar = (ProgressBar)findViewById(R.id.progressBar);
        editPhone.addTextChangedListener(new MobileActivity.TextChange(editPhone));

        findViewById(R.id.buttonContinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mobile = editPhone.getText().toString();
//                Intent intent = new Intent(MobileActivity.this,DashboardActivity.class);
//                intent.putExtra("mobile", mobile);
//                startActivity(intent);
                get_mob_no.setText(str);
//                String mobile = intent.getStringExtra("mobile");
                sendVerificationCode(mobile);
                mProgressbar.setVisibility(View.VISIBLE);
            }
        });
        editPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            // Show white background behind floating label
                            textDummyHintphone.setVisibility(View.VISIBLE);
                        }
                    }, 100);
                } else {
                    // Required to show/hide white background behind floating label during focus change
                    if (editPhone.getText().length() > 0)
                        textDummyHintphone.setVisibility(View.VISIBLE);
                    else
                        textDummyHintphone.setVisibility(View.INVISIBLE);
                }

            }
        });

        //if the automatic sms detection did not work, user can also enter the code manually
        //so adding a click listener to the button
        findViewById(R.id.btn_verify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = editTextCode.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    editTextCode.setError("Enter valid code");
                    editTextCode.requestFocus();
                    return;
                }
                //verifying the code entered manually
                verifyVerificationCode(code);
            }
        });
    }

    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(MobileActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //verification successful we will start the profile activity
                            String Data = get_mob_no.getText().toString();

                            Intent intent = new Intent(MobileActivity.this, DashboardActivity.class);
                            intent.putExtra("mobile",Data);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            Toast.makeText(  MobileActivity.this, "OTP Verified successfully", Toast.LENGTH_SHORT).show();
                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }

                            Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG);
                            snackbar.setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                            snackbar.show();
                        }
                    }
                });
    }
    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }
    //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                editTextCode.setText(code);
                //verifying the code
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(MobileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };
    private class TextChange implements TextWatcher {

        View view;

        private TextChange(View v) {
            view = v;
        }//end constructor

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            String mobile = editPhone.getText().toString();

            if(mobile.isEmpty() || mobile.length() < 10){
                editPhone.requestFocus();
                editPhone.setError("Enter a valid Mobile Number");

                return;

            }
        }
    }
}