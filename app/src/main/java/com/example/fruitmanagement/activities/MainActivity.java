package com.example.fruitmanagement.activities;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.fruitmanagement.R;
import com.example.fruitmanagement.adapters.FruitAdapter;
import com.example.fruitmanagement.constants.Constants;
import com.example.fruitmanagement.daos.CartDAO;
import com.example.fruitmanagement.daos.FruitDAO;
import com.example.fruitmanagement.dtos.FruitDTO;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Main";

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
            case R.id.MenuHome:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.menuCart:
                 intent = new Intent(this, CartActivity.class);
                startActivity(intent);
                break;
            case R.id.menuHistory:
                intent = new Intent(this, OrderHistoryActivity.class);
                startActivity(intent);
            case R.id.menuExit:
                logout();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("IDPref");
        editor.remove("EmailPref");
        editor.remove("Role");
        editor.remove("Remember");
        editor.apply();
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
            Log.e(TAG, "Error: " + e.getMessage());
        }
    }
    
    private void showNotification(Context context, String title, String message, Intent intent, int reqCode)
    {
        String notiId = "cart_exists";

        PendingIntent pendingIntent = PendingIntent.getActivity(context,reqCode, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, notiId)
                .setSmallIcon(R.drawable.shopping_cart)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setChannelId(notiId)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notify if cart exists.";// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(notiId, name, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        notificationManager.notify(reqCode, builder.build()); // 0 is the request code, it should be unique id
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
                Intent intent = new Intent(this, CartActivity.class);
                showNotification(getApplicationContext(), "Your Cart has Product", "Your Cart is not empty. Do you want to take a look?", intent, 0);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error: " + e.getMessage());
        }

    }

    public void clickToSwitchToAddress(View view) {
        Intent intent = new Intent(this, MapsMarkerActivity.class);
        startActivity(intent);
        finish();
    }
}