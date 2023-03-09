package com.example.fruitmanagement.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.fruitmanagement.R;
public class BillingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);
        setTitle("Billing Screen");

    }
    public void clickToOK(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
}