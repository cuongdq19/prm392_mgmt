package com.example.fruitmanagement.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fruitmanagement.R;
import com.example.fruitmanagement.adapters.FruitAdapter;
import com.example.fruitmanagement.daos.FruitDAO;
import com.example.fruitmanagement.db.MyDatabase;
import com.example.fruitmanagement.dtos.FruitDTO;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView listFruitView;
    private ArrayList<FruitDTO> fruitDTOList;
    private FruitAdapter adapter;

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
            case R.id.menuExit:
                intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new MyDatabase(this);

        listFruitView = findViewById(R.id.listFruitView);
        adapter = new FruitAdapter();
        FruitDAO dao = new FruitDAO(MainActivity.this);
        try {
            fruitDTOList = dao.getFruits();
            adapter.setFruitDTOList(fruitDTOList);
            listFruitView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}