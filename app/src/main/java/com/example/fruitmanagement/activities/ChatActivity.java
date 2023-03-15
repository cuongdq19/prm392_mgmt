package com.example.fruitmanagement.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fruitmanagement.R;
import com.example.fruitmanagement.adapters.ChatAdapter;
import com.example.fruitmanagement.constants.Constants;
import com.example.fruitmanagement.daos.CartDAO;
import com.example.fruitmanagement.models.Message;
import com.google.gson.Gson;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ChatActivity extends AppCompatActivity {
    private static final String TAG = "ChatActivity";
    
    private Socket socket;
    private Button btnSend;
    private TextView txtMsgs;
    private String username;
    private String role;
    private String roomName = "admin";

    private Gson gson = new Gson();
    private EditText edtMsg;
    private ListView listMessages;
    private ChatAdapter adapter;

    private void addMessage(Message message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.addMessage(message);
                edtMsg.setText("");
                listMessages.smoothScrollToPosition(listMessages.getChildCount());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (role.equals("Admin")) {
            getMenuInflater().inflate(R.menu.admin_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuCart:
                Intent intent = new Intent(this, CartActivity.class);
                startActivity(intent);
                break;
            case R.id.menuHistory:
                intent = new Intent(this, OrderHistoryActivity.class);
                startActivity(intent);
                break;
            case R.id.menuExit:
                logout();
                break;
            case android.R.id.home:
                if (role.equals("User")) {
                    finish();
                } else {
                    logout();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        username = sharedPreferences.getString("IDPref", "");
        role = sharedPreferences.getString("Role", "");

        setTitle(username + " Chat Room");

        btnSend = findViewById(R.id.btnSend);
        txtMsgs = findViewById(R.id.txtMsgs);
        edtMsg = findViewById(R.id.edtMsg);
        listMessages = findViewById(R.id.listChat);
        adapter = new ChatAdapter();

        listMessages.setAdapter(adapter);

        try {
            socket = IO.socket(Constants.SOCKET_URL);
            System.out.println("Socket ID = " + socket.id());
        } catch (Exception e) {
            Log.e(TAG, "Error: " + e.getMessage());
        }

        socket.connect();

        Emitter.Listener onConnect = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String jsonData = gson.toJson(new Message(username, roomName));
                socket.emit("subscribe", jsonData);
            }
        };
        socket.on(Socket.EVENT_CONNECT, onConnect);

        Emitter.Listener onNewUser = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String jsonData = (String) args[0];
                Message message = gson.fromJson(jsonData, Message.class);
                System.out.println(message.getUsername() + " has joined the chat.");
            }
        };
        socket.on("newUserToChatRoom", onNewUser);

        Emitter.Listener onUpdateChat = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Message chat = gson.fromJson((String) args[0], Message.class);
                addMessage(chat);
            }
        };
        socket.on("updateChat", onUpdateChat);

        Emitter.Listener onUserLeft = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("User leaved.");
            }
        };
        socket.on("userLeftChatRoom", onUserLeft);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = edtMsg.getText().toString();
                Message messageToSend = new Message(username, content, roomName);
                socket.emit("newMessage", gson.toJson(messageToSend));

                // Add to view
                addMessage(messageToSend);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Message chat = new Message(username, roomName);
        socket.emit("unsubscribe", gson.toJson(chat));
        socket.disconnect();

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
}