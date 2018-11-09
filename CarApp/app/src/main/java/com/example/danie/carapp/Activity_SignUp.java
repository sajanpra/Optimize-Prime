package com.example.danie.carapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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


public class Activity_SignUp extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static final String TAG = "Activity_SignUp";
    private EditText editEmail, editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__sign_up);

        mAuth = FirebaseAuth.getInstance();


        //##################   REGISTER   ####################

        Button register = (Button) findViewById(R.id.createaccount_button);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){

                editEmail = findViewById(R.id.EmailTxt);
                editPassword = findViewById(R.id.PasswordTxt);
                String password = editPassword.getText().toString();
                String email = editEmail.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Activity_SignUp.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(Activity_SignUp.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(Activity_SignUp.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    startActivity(new Intent(Activity_SignUp.this, Activity_MainMenu.class));
                                    finish();
                                }
                            }
                        });






            }
        });


    }

    private void updateUI(FirebaseUser user) {
        //hideProgressDialog();
        if (user != null) {


            final String _User = user.getUid();

            Intent myIntent = new Intent(Activity_SignUp.this, Activity_Profile.class);
            startActivity(myIntent);

            findViewById(R.id.login_button).setVisibility(View.GONE);
        } else {

            findViewById(R.id.login_button).setVisibility(View.VISIBLE);
        }
    }


}
