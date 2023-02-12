package com.example.fruitmanagement.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.fruitmanagement.R;
import com.example.fruitmanagement.adapters.CartAdapter;
import com.example.fruitmanagement.daos.CartDAO;
import com.example.fruitmanagement.db.MyDatabase;
import com.example.fruitmanagement.dtos.CartItemDTO;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private ListView listCartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        listCartView = findViewById(R.id.listCartView);
        CartAdapter adapter = new CartAdapter();
        CartDAO dao = new CartDAO(this);
        try {
            ArrayList<CartItemDTO> cartItems = dao.getCartItems();
            adapter.setCartDTOList(cartItems);
            listCartView.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}