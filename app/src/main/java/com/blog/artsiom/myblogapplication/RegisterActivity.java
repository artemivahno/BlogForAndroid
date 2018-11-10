package com.blog.artsiom.myblogapplication;

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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText reg_email_field;
    private EditText reg_pass_field;
    private EditText reg_confirm_pass_field;
    private EditText setupName;
    private EditText setupSurname;
    private Button reg_btn;
    private Button reg_login_btn;
    private Button modyfy_btn;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registry);

        mAuth = FirebaseAuth.getInstance();



        reg_email_field = findViewById(R.id.email);
        reg_pass_field = findViewById(R.id.password);
        reg_confirm_pass_field = findViewById(R.id.confirm_password);
        reg_btn = findViewById(R.id.email_registry_button);
        modyfy_btn = findViewById(R.id.modyfy_btn);



        reg_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                String email = reg_email_field.getText().toString();
                String pass = reg_pass_field.getText().toString();
                String confirm_pass = reg_confirm_pass_field.getText().toString();
                final String user_name = setupName.getText().toString();
                final String user_surname = setupSurname.getText().toString();
                FirebaseUser currentUser = mAuth.getCurrentUser();


                if(currentUser != null) {

                    reg_btn.setEnabled(false);
                    modyfy_btn.setEnabled(true);
                }
                else {

                    if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(confirm_pass) && !TextUtils.isEmpty(user_name) && !TextUtils.isEmpty(user_surname)) {

                        if (pass.equals(confirm_pass)) {

                            mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {

                                        Intent setupIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(setupIntent);
                                        storeFirestore(user_name, user_surname);
                                        finish();

                                    } else {

                                        String errorMessage = task.getException().getMessage();
                                        Toast.makeText(RegisterActivity.this, "Error : " + errorMessage, Toast.LENGTH_LONG).show();

                                    }

                                }
                            });

                        } else {

                            Toast.makeText(RegisterActivity.this, "Confirm Password and Password Field doesn't match.", Toast.LENGTH_LONG).show();

                        }
                    }

                }

            }
        });


    }

    private void storeFirestore(String user_name, String user_surname) {

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("name", user_name);
        userMap.put("surname", user_surname);

    }


    @Override
    protected void onStart() {
        super.onStart();
        modyfy_btn = findViewById(R.id.modyfy_btn);
        modyfy_btn.setEnabled(false);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        /*if(currentUser != null){

            sendToMain();

        }*/

    }


    private void sendToMain() {

        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();

    }
}
