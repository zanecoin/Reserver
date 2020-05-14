package com.application.reserver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

public class LoginActivity extends AppCompatActivity {
    EditText mEmail, mPassword;
    TextView mRegisterBtn;
    Button mLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    private static final String TAG = "Login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mRegisterBtn = findViewById(R.id.backRegister);
        mLoginBtn = findViewById(R.id.loginBtn);

        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar2);

        if (fAuth.getCurrentUser() != null){
            Toast.makeText(this, "Stahuji data uživatele.", Toast.LENGTH_SHORT).show();
            fetchUserData(fAuth.getCurrentUser());
        }

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

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

                progressBar.setVisibility(View.VISIBLE);

                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            fetchUserData(fAuth.getCurrentUser());
                        } else {
                            Log.w(TAG, "onComplete: ");
                            Toast.makeText(LoginActivity.this, "Nastala chyba." + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
        /*back to the register activity*/
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
            }
        });
    }
    private void fetchUserData(FirebaseUser user){
        UserService.getInstance().fetchUser(user);
        UserService.getInstance().setOnUserListener(new UserService.OnUserListener() {
            @Override
            public void onSuccess(User user) {
                Toast.makeText(getApplicationContext(), "Uživatel úspěšně přihlášen.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }

            @Override
            public void onFail() {
                Toast.makeText(getApplicationContext(), "Přihlášení se nezdařilo.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
