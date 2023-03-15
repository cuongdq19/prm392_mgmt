package com.example.fruitmanagement.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.fruitmanagement.R;
import com.example.fruitmanagement.adapters.OrderAdapter;
import com.example.fruitmanagement.constants.Constants;
import com.example.fruitmanagement.daos.OrderDAO;
import com.example.fruitmanagement.dtos.OrderDTO;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {
    private static final String TAG = "Cart";

    ListView listOrderHistoryView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        listOrderHistoryView = findViewById(R.id.listOrderHistoryView);

        OrderAdapter adapter = new OrderAdapter();
        OrderDAO dao = new OrderDAO(this);
        try {
            SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
            String userId = sharedPreferences.getString("IDPref", null);
            ArrayList<OrderDTO> orders = dao.getOrders(userId);
            adapter.setOrderDTOArrayList(orders);
            listOrderHistoryView.setAdapter(adapter);
        } catch (Exception e) {
            Log.e(TAG, "Error: " + e.getMessage());
        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.MenuHome:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.menuCart:
                intent = new Intent(this, CartActivity.class);
                startActivity(intent);
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}