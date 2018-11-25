package com.example.danie.carapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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


public class Activity_SignUp extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText editEmail, editPassword, editName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__sign_up);

        mAuth = FirebaseAuth.getInstance();




        //##################   REGISTER   ####################

        Button register = findViewById(R.id.createaccount_button);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){

                editName = findViewById(R.id.NameTxt);
                editEmail = findViewById(R.id.EmailTxt);
                editPassword = findViewById(R.id.PasswordTxt);
                final String username = editName.getText().toString().trim();
                final String email = editEmail.getText().toString().trim();
                final String password = editPassword.getText().toString();

                boolean hasUppercase = !password.equals(password.toLowerCase());
                boolean hasLowercase = !password.equals(password.toUpperCase());
                boolean hasSpecial   = password.matches(".*[!@#$%^&*].*");  //Checks at least one char is not alpha numeric
                boolean noConditions = !(password.contains("AND") || password.contains("NOT")); //Check that it doesn't contain AND or NOT



                // Make sure all parameters are filled before even checking input
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (TextUtils.isEmpty(username)) {
                    Toast.makeText(getApplicationContext(), "Enter name!", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (password.length() < 8) {
                    Toast.makeText(getApplicationContext(), "Password must be at least 8 characters long", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (!hasUppercase) {
                    Toast.makeText(getApplicationContext(), "Password must have at least one upper case character", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (!hasLowercase) {
                    Toast.makeText(getApplicationContext(), "Password must have at least one lower case character", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (!hasSpecial) {
                    Toast.makeText(getApplicationContext(), "Password must have at least one special character", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (!noConditions) {
                    Toast.makeText(getApplicationContext(), "Invalid password input of AND or NOT", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (!isValidEmailAddress(email)) {
                    Toast.makeText(getApplicationContext(), "Please enter a valid buffalo.edu email address!", Toast.LENGTH_SHORT).show();
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
                                    String exceptionString = task.getException().toString();
                                    int indextotrim = exceptionString.indexOf(":");
                                    String ExceptionInfo = exceptionString.substring(indextotrim+2, exceptionString.length());
                                    Toast.makeText(Activity_SignUp.this, "Authentication failed! " + ExceptionInfo,
                                            Toast.LENGTH_SHORT).show();
                                }
                                else {

                                    FirebaseUser user = mAuth.getCurrentUser();
                                    String _User = user.getUid();

                                    FirebaseDatabase database = FirebaseDatabase.getInstance();

                                    DatabaseReference myRef = database.getReference("users").child(_User).child("Email");
                                    myRef.setValue(email);
                                    myRef = database.getReference("users").child(_User).child("Password");
                                    myRef.setValue(password);
                                    myRef = database.getReference("users").child(_User).child("Name");
                                    myRef.setValue(username);

                                    Intent myIntent = new Intent(Activity_SignUp.this, Activity_MainMenu.class);
                                    startActivity(myIntent);
                                    finish();
                                }
                            }
                        });


            }
        });


    }


    public boolean isValidEmailAddress(String email) {
        //String ePattern = "^[a-zA-z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        String ePattern = "^[a-zA-z0-9.!#$%&'*+/=?^_`{|}~-]+@(buffalo.edu)$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }


}
