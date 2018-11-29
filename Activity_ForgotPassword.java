package com.example.danie.carapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Activity_ForgotPassword extends AppCompatActivity {

    private EditText editEmail;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__forgot_password);


        Button forgotpasswordEmailbutton = findViewById(R.id.forgotpassword_button);
        forgotpasswordEmailbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editEmail = findViewById(R.id.fp_txt);
                String email = editEmail.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    mAuth = FirebaseAuth.getInstance();
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(Activity_ForgotPassword.this, "Password reset email sent!", Toast.LENGTH_SHORT).show();
                                //finish();
                            }
                            else {
                                Toast.makeText(Activity_ForgotPassword.this, "Error! Password reset email failed to send!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });

    }
}
