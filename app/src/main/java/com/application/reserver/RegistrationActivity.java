package com.application.reserver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistrationActivity extends AppCompatActivity {
    /* variables for user input or some other interactive actions */
    EditText mFullName, mEmail, mPassword, mPhone;
    Button mRegisterBtn; //actual button
    /* variables for text, service, animation */
    TextView mLoginBtn; //text converted to button
    FirebaseAuth fAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* set activity */
        setContentView(R.layout.activity_registration);
        /* assign widget to corresponding variable */
        mFullName = findViewById(R.id.name);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mPhone = findViewById(R.id.phone);
        mRegisterBtn = findViewById(R.id.registerBtn);
        mLoginBtn = findViewById(R.id.backLogin);
        progressBar = findViewById(R.id.progressBar);
        /* connect to firebase */
        fAuth = FirebaseAuth.getInstance();

        /* actions being executed after clicking on register btn */
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* get input from user */
                String email = mEmail.getText().toString().trim();
                final String password = mPassword.getText().toString().trim();

                /* check if input is correct */
                if (TextUtils.isEmpty(email)){
                    mEmail.setError("Zadejte prosím svůj e-mail.");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    mPassword.setError("Zadejte prosím své heslo.");
                    return;
                }
                if (password.length() < 8){
                    mPassword.setError("Heslo musí mít alespoň 8 znaků.");
                    return;
                }

                /* show progress bar */
                progressBar.setVisibility(View.VISIBLE);

                /* registration process */
                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(RegistrationActivity.this, "Účet byl úspěšně vytvořen.", Toast.LENGTH_LONG).show();

                            /* user in database that has just been created */
                            FirebaseUser firebaseUser = fAuth.getCurrentUser();

                            /* new variable named user, class User */
                            User user = new User(
                                    firebaseUser.getUid(),
                                    firebaseUser.getEmail(),
                                    password,
                                    mFullName.getText().toString(),
                                    mPhone.getText().toString()
                            );

                            /* save user to Fire store database collection*/
                            UserService.getInstance().setUser(user);

                            /* jump main activity */
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            /* error handling */
                            Toast.makeText(RegistrationActivity.this, "Nastala chyba." + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
        /* switch to login activity */
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }
}
