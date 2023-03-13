package com.example.fruitmanagement.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
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
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class AdminChatActivity extends AppCompatActivity {
    private static final String TAG = "AdminChatActivity";

    ServerSocket serverSocket;

    private TextView txtIP, txtPort, txtMsgs;
    private EditText edtMsg;
    private Button btnSend;

    private static String SERVER_IP = "";
    private static final int SERVER_PORT = 8080;
    Thread socketThread = null;

    private PrintWriter output;
    private BufferedReader input;

    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_chat);

        txtIP = findViewById(R.id.txtIP);
        txtPort = findViewById(R.id.txtPort);
        txtMsgs = findViewById(R.id.txtMsgs);
        edtMsg = findViewById(R.id.edtMsg);
        btnSend = findViewById(R.id.btnSend);

        try {
            SERVER_IP = getLocalIpAddress();
        } catch (UnknownHostException ex) {
            Log.e(TAG, "Error: " + ex.getMessage());
        }

        socketThread = new Thread(new ListeningThread());

        socketThread.start();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = edtMsg.getText().toString().trim();
                if (!message.isEmpty()) {
                    new Thread(new SendingThread(message)).start();
                }
            }
        });
    }

    private String getLocalIpAddress() throws UnknownHostException {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        assert wifiManager != null;
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipInt = wifiInfo.getIpAddress();
        return InetAddress.getByAddress(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(ipInt).array()).getHostAddress();
    }

    class ListeningThread implements Runnable {
        @Override
        public void run() {
            Socket socket;
            try {
                serverSocket = new ServerSocket(SERVER_PORT);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtMsgs.setText("Not connected");
                        txtIP.setText("IP: " + SERVER_IP);
                        txtPort.setText("Port: " + String.valueOf(SERVER_PORT));
                    }
                });

                socket = serverSocket.accept();
                output = new PrintWriter(socket.getOutputStream());
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtMsgs.setText("Connected\n");
                    }
                });
                new Thread(new ReceivingThread()).start();
            } catch (IOException ex) {
                Log.e(TAG, "Error: " + ex.getMessage());
            }

        }
    }

    private class ReceivingThread implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    final String message = input.readLine();
                    if (message != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txtMsgs.append("Client: " + message + "\n");
                            }
                        });
                    } else {
                        socketThread = new Thread(new ListeningThread());
                        socketThread.start();
                        return;
                    }
                } catch (IOException ex) {
                    Log.e(TAG, "Error: " + ex.getMessage());
                }
            }
        }
    }

    private class SendingThread implements Runnable {
        private String message;

        private SendingThread(String message) {
            this.message = message;
        }


        @Override
        public void run() {
            output.write(message);
            output.flush();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    txtMsgs.append("Server: " + message + "\n");
                    edtMsg.setText("");
                }
            });

        }
    }
}