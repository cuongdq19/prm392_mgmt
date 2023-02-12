package com.example.fruitmanagement.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.fruitmanagement.db.MyDatabase;
import com.example.fruitmanagement.dtos.UserDTO;

import java.io.Serializable;
import java.util.ArrayList;

public class UserDAO implements Serializable {
    private MyDatabase conn = null;
    private SQLiteDatabase db = null;

    public UserDAO(Context context) {
        conn = new MyDatabase(context);
    }

    private void closeConnection() throws Exception {
        if (db != null) {
            db.close();
        }
    }

    public boolean register(UserDTO dto) throws Exception {
        try {
            db = conn.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("username", dto.getUsername());
            values.put("password", dto.getPassword());
            values.put("email", dto.getEmail());
            values.put("role", dto.getRole());
            return db.insert("user", null, values) > 0;
        } finally {
            closeConnection();
        }
    }

    public ArrayList<UserDTO> getUsers() throws Exception {
        ArrayList<UserDTO> result = new ArrayList<>();
        String username, password, role, email;
        UserDTO dto = null;

        try {
            db = conn.getReadableDatabase();
            Cursor cs = db.rawQuery("SELECT * FROM User", null);

            cs.moveToFirst();
            while (!cs.isAfterLast()) {
                username = cs.getString(0);
                password = cs.getString(1);
                role = cs.getString(2);
                email = cs.getString(3);
                dto = new UserDTO(username, password, role, email);
                result.add(dto);
                cs.moveToNext();
            }
        } finally {
            closeConnection();
        }

        return result;
    }

    public UserDTO login(String username, String password) throws Exception {
        String email = "", role = "";
        UserDTO dto = null;

        try {
            db = conn.getReadableDatabase();
            Cursor cs = db.rawQuery("SELECT Email, Role FROM User WHERE Username = ? and Password = ?", new String[] { username, password });
            cs.moveToFirst();

            if (cs.isLast()) {
                email = cs.getString(0);
                role = cs.getString(1);
                dto = new UserDTO(username, email, role);
            }

        } finally {
            closeConnection();
        }
        return dto;
    }
}
