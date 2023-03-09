package com.example.fruitmanagement.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fruitmanagement.R;
import com.example.fruitmanagement.daos.UserDAO;
import com.example.fruitmanagement.dtos.UserDTO;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "Signup";

    private EditText edtUsername, edtPassword, edtEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtEmail = findViewById(R.id.edtEmail);
    }

    public void clickToSignup(View view) {
        String username = edtUsername.getText().toString();
        String password = edtPassword.getText().toString();
        String email = edtEmail.getText().toString();
        String role = "User";

        UserDTO dto = new UserDTO(username, password, email, role);
        UserDAO dao = new UserDAO(this);
        try {
            boolean success = dao.register(dto);
            if (success) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Cannot create user.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error: " + e.getMessage());
        }
    }

    public void clickToSwitchToLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}