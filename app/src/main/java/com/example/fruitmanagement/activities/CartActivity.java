package com.example.fruitmanagement.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fruitmanagement.R;
import com.example.fruitmanagement.adapters.CartAdapter;
import com.example.fruitmanagement.constants.Constants;
import com.example.fruitmanagement.daos.CartDAO;
import com.example.fruitmanagement.daos.OrderDAO;
import com.example.fruitmanagement.db.MyDatabase;
import com.example.fruitmanagement.dtos.CartItemDTO;
import com.example.fruitmanagement.dtos.OrderDTO;

import java.util.ArrayList;
import java.util.Date;

public class CartActivity extends AppCompatActivity {
    private static final String TAG = "Cart";
    private TextView txtTotalPrice;
    private ListView listCartView;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        setTitle("Your Cart");
        txtTotalPrice = findViewById(R.id.txtTotalPrice);
        listCartView = findViewById(R.id.listCartView);
        CartAdapter adapter = new CartAdapter();
        CartDAO dao = new CartDAO(this);
        try {
            ArrayList<CartItemDTO> cartItems = dao.getCartItems();
            adapter.setCartDTOList(cartItems);
            adapter.registerDataSetObserver(new DataSetObserver() {
                @Override
                public void onChanged() {
                    txtTotalPrice.setText(getTotal(adapter.getCartDTOList()) + "$");
                }
            });

            listCartView.setAdapter(adapter);
            txtTotalPrice.setText(getTotal(cartItems) + "$");

        } catch (Exception e) {
            Log.e(TAG, "Error: " + e.getMessage());
        }
    }

    public void clickToCheckout(View view) {
        try {
            CartAdapter adapter = (CartAdapter) listCartView.getAdapter();
            ArrayList<CartItemDTO> list = adapter.getCartDTOList();
            OrderDAO dao = new OrderDAO(this);

            SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
            String userId = sharedPreferences.getString("IDPref", null);
            OrderDTO dto = new OrderDTO(new Date(), userId);
            long orderId = dao.create(dto);
            if (orderId < 0) {
                Toast.makeText(this, "Failed to create order!", Toast.LENGTH_SHORT).show();
                return;
            }
            boolean success = dao.addOrderDetail(orderId, list);
            if (success) {
                CartDAO cartDAO = new CartDAO(this);
                cartDAO.clearCart();

                Intent intent = new Intent(this, BillingActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error: " + e.getMessage());
        }
    }


    private double getTotal(ArrayList<CartItemDTO> cartDTOList) {
        double total = 0;
        for (int i = 0; i < cartDTOList.size(); i++) {
            total += cartDTOList.get(i).getPrice() * cartDTOList.get(i).getQuantity();
        }

        return total;
    }
}