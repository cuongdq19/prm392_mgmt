package com.example.fruitmanagement.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.fruitmanagement.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class UserChatActivity extends AppCompatActivity {
    private static final String TAG = "UserChatActivity";

    private TextView txtMsgs;
    private EditText edtMsg, edtIP, edtPort;
    private Button btnSend, btnConnect;
    private String SERVER_IP;
    private int SERVER_PORT;

    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_chat);

        txtMsgs = findViewById(R.id.txtMsgs);
        edtIP = findViewById(R.id.edtIP);
        edtMsg = findViewById(R.id.edtMsg);
        edtPort = findViewById(R.id.edtPort);
        btnConnect = findViewById(R.id.btnConnect);
        btnSend = findViewById(R.id.btnSend);

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtMsgs.setText("");
                SERVER_IP = edtIP.getText().toString().trim();
                SERVER_PORT = Integer.parseInt(edtPort.getText().toString().trim());
                thread = new Thread(new Thread1());
                thread.start();

            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = edtMsg.getText().toString().trim();
                if (!message.isEmpty()) {
                    new Thread(new Thread3(message)).start();
                }
            }
        });
    }

    private PrintWriter output;
    private BufferedReader input;
    class Thread1 implements Runnable {
        public void run() {
            Socket socket;
            try {
                socket = new Socket(SERVER_IP, SERVER_PORT);
                output = new PrintWriter(socket.getOutputStream());
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtMsgs.setText("Connected\n");
                    }
                });
                new Thread(new Thread2()).start();
            } catch (IOException e) {
                Log.e(TAG, "Error: " + e.getMessage());
            }
        }
    }

    class Thread2 implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    final String message = input.readLine();
                    if (message != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txtMsgs.append("Server: " + message + "\n");
                            }
                        });
                    } else {
                        thread = new Thread(new Thread1());
                        thread.start();
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class Thread3 implements Runnable {
        private String message;
        Thread3(String message) {
            this.message = message;
        }
        @Override
        public void run() {
            output.write(message);
            output.flush();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    txtMsgs.append("Client: " + message + "\n");
                    edtMsg.setText("");
                }
            });
        }
    }
}