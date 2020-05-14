package com.application.reserver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        User user = UserService.getInstance().getUser();
        if (user != null) {
            TextView nameView = (TextView) findViewById(R.id.nameShow);
            TextView emailView = (TextView) findViewById(R.id.emailShow);
            TextView phoneView = (TextView) findViewById(R.id.phoneShow);
            nameView.setText(user.getName());
            emailView.setText(user.getEmail());
            phoneView.setText(user.getPhone());
        }
    }

    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

}
