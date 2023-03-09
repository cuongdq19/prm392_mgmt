package com.example.fruitmanagement.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fruitmanagement.R;
import com.example.fruitmanagement.daos.CartDAO;
import com.example.fruitmanagement.dtos.CartItemDTO;
import com.example.fruitmanagement.dtos.FruitDTO;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = "Detail";

    private TextView txtName , txtPrice, txtDesc;
    private ImageView imgFruitView;
    private Button btnAddToCart;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuCart:
                Intent intent = new Intent(this, CartActivity.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        FruitDTO dto  = (FruitDTO) getIntent().getSerializableExtra("DTO");
        txtName = findViewById(R.id.txtName);
        txtPrice = findViewById(R.id.txtPrice);
        txtDesc = findViewById(R.id.txtDesc);
        imgFruitView = findViewById(R.id.imgFruitView);
        btnAddToCart = findViewById(R.id.btnAddToCart);

        txtName.setText(dto.getName());
        txtPrice.setText(dto.getPrice() + "");
        txtDesc.setText(dto.getDescription());
        Picasso.get().load(dto.getImage()).into(imgFruitView);
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartDAO dao = new CartDAO(v.getContext());
                try {
                    CartItemDTO cartItem = dao.getCartItem(dto.getId());
                    if (cartItem == null) {
                        cartItem = new CartItemDTO(dto.getId(), dto.getName(), 1, dto.getPrice(), dto.getImage());
                        dao.addToCart(cartItem);
                    } else {
                        int quantity = cartItem.getQuantity() + 1;
                        dao.updateCart(dto.getId(), quantity);
                    }
                    Toast.makeText(v.getContext(), "Added to Cart", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Log.e(TAG, "Error: " + e.getMessage());
                }

            }
        });


    }
}