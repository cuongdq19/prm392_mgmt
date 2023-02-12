package com.example.fruitmanagement.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fruitmanagement.R;
import com.example.fruitmanagement.daos.UserDAO;
import com.example.fruitmanagement.db.MyDatabase;
import com.example.fruitmanagement.dtos.UserDTO;

public class LoginActivity extends AppCompatActivity {
    private EditText edtUsername, edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
    }


    public void clickToLogin(View view) throws Exception {
        String username = edtUsername.getText().toString();
        String password = edtPassword.getText().toString();

        UserDAO dao = new UserDAO(this);
        UserDTO dto = dao.login(username, password);
        if (dto == null) {
            Toast.makeText(this, "Invalid username or password.", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("DTO", dto);
        startActivity(intent);
        finish();

    }
}