package com.marcinmejner.uberclone;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DriverLoginActivity extends AppCompatActivity {
    private static final String TAG = "DriverLoginActivity";

    @BindView(R.id.email_driver_edt)
    EditText email;
    @BindView(R.id.password_driver_edt)
    EditText password;
    @BindView(R.id.login_driver_btn)
    Button login;
    @BindView(R.id.register_driver_btn)
    Button registration;

    /* FireBase */
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener firebaseAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_login);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                if (user != null) {
//                    Intent intent = new Intent(DriverLoginActivity.this, MapActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
            }
        };

        /*
        * Rejestrowanie nowego kierowcy
        * */
        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String emailText = email.getText().toString();
                final String passwordText = password.getText().toString();
                if (email.getText().toString().equals("") && password.getText().toString().equals("")) {
                    Toast.makeText(DriverLoginActivity.this, "Pola musza mieć wiecej znaków", Toast.LENGTH_SHORT).show();
                } else {

                    mAuth.createUserWithEmailAndPassword(emailText, passwordText).addOnCompleteListener(DriverLoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (!task.isSuccessful()) {
                                Toast.makeText(DriverLoginActivity.this, "Wystąpił błąd, spróbuj ponownie", Toast.LENGTH_LONG).show();
                                Log.d(TAG, "onComplete: Rejestracja nieudana");
                            }
                            /* Rejestracja udana*/
                            else {
                                String userId = mAuth.getCurrentUser().getUid();
                                DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Riders").child(userId);
                                current_user_db.setValue(true);
                                Log.d(TAG, "onComplete: SUKCES");
                                Toast.makeText(DriverLoginActivity.this, "Rejestracja udana!", Toast.LENGTH_LONG).show();

                            }

                        }
                    });
                }
            }
        });

        /*
        * Logowanie Kierowcy
        * */
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String emailText = email.getText().toString();
                final String passwordText = password.getText().toString();
                if (email.getText().toString().equals("") && password.getText().toString().equals("")) {
                    Toast.makeText(DriverLoginActivity.this, "Pola musza mieć wiecej znaków", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.signInWithEmailAndPassword(emailText, passwordText).addOnCompleteListener(DriverLoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(DriverLoginActivity.this, "Wystąpił błąd w czasie logowania, spróbuj ponownie", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(DriverLoginActivity.this, "Udane Logowanie", Toast.LENGTH_LONG).show();
                                Log.d(TAG, "onComplete: LOGOWANIE - SUKCES");
                            }


                        }
                    });
                }


            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);

    }
}













