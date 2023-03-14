package com.example.fruitmanagement.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fruitmanagement.R;
import com.example.fruitmanagement.constants.Constants;
import com.example.fruitmanagement.daos.CartDAO;
import com.example.fruitmanagement.daos.UserDAO;
import com.example.fruitmanagement.dtos.UserDTO;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "Login";

    private EditText edtUsername, edtPassword;
    private CheckBox cboRmb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        boolean remember = sharedPreferences.getBoolean("Remember", false);
        if (remember) {
            String role = sharedPreferences.getString("Role", "");

            Intent intent = new Intent(this, role.equals("User") ? ChatActivity.class : ChatActivity.class);
            startActivity(intent);
            finish();
            return;
        } else {
            saveToPreference(null);
            CartDAO dao = new CartDAO(this);
            try {
                dao.clearCart();
            } catch (Exception e) {
                Log.e(TAG, "Error: " + e.getMessage());
            }
        }

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        cboRmb = findViewById(R.id.cboRemember);
    }


    public void clickToLogin(View view) throws Exception {
        String username = edtUsername.getText().toString();
        String password = edtPassword.getText().toString();
        boolean isRemember = cboRmb.isChecked();

        UserDAO dao = new UserDAO(this);
        UserDTO dto = dao.login(username, password);
        if (dto == null) {
            Toast.makeText(this, "Invalid username or password.", Toast.LENGTH_SHORT).show();
            return;
        }
        String role = dto.getRole();
        if(role.equals("User")) {
            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra("DTO", dto);
            saveToPreference(dto);
            saveToPreference(isRemember);
            startActivity(intent);
        } else if(role.equals("Admin")) {
            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra("DTO", dto);
            saveToPreference(dto);
            saveToPreference(isRemember);
            startActivity(intent);
        }
        finish();

    }

    public void clickToSwitchToSignup(View view) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
        finish();
    }

    private void saveToPreference(boolean isRemember) {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean("Remember", isRemember);
        editor.commit();

    }

    private void saveToPreference(UserDTO dto) {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (dto == null) {
            // Remove dto from SharedPreferences
            editor.remove("IDPref");
            editor.remove("EmailPref");
            editor.remove("Role");

        } else {
            editor.putString("IDPref", dto.getUsername());
            editor.putString("EmailPref", dto.getEmail());
            editor.putString("Role", dto.getRole());
        }

        editor.commit();
    }
}