package com.example.fruitmanagement.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fruitmanagement.R;
import com.example.fruitmanagement.adapters.FruitAdapter;
import com.example.fruitmanagement.constants.Constants;
import com.example.fruitmanagement.daos.CartDAO;
import com.example.fruitmanagement.daos.FruitDAO;
import com.example.fruitmanagement.dtos.FruitDTO;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView listFruitView;
    private ArrayList<FruitDTO> fruitDTOList;
    private FruitAdapter adapter;
    private AlertDialog.Builder alertDialog;

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
                break;
            case R.id.menuExit:
                logout();
                break;
            case R.id.menuHistory:
                intent = new Intent(this, OrderHistoryActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("IDPref");
        editor.remove("EmailPref");
        editor.remove("Role");
        CartDAO dao = new CartDAO(this);
        try {
            boolean success = dao.clearCart();
            if (success) {
                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Cannot logout.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Fruits");

        listFruitView = findViewById(R.id.listFruitView);
        adapter = new FruitAdapter();
        FruitDAO dao = new FruitDAO(MainActivity.this);
        try {
            fruitDTOList = dao.getFruits();
            adapter.setFruitDTOList(fruitDTOList);
            listFruitView.setAdapter(adapter);

            CartDAO cartDAO = new CartDAO(this);
            boolean hasCart = cartDAO.getCartItems().size() > 0;

            if (hasCart) {
                buildAlertDialog();
                alertDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void buildAlertDialog() {
        alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("You have items inside your Cart. Do you want to check?");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

    }


}