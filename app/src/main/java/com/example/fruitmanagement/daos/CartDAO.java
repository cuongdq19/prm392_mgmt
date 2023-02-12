package com.example.fruitmanagement.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.fruitmanagement.db.MyDatabase;
import com.example.fruitmanagement.dtos.CartItemDTO;

import java.util.ArrayList;

public class CartDAO {
    private MyDatabase conn = null;
    private SQLiteDatabase db = null;

    public CartDAO(Context context) {
        conn = new MyDatabase(context);
    }

    private void closeConnection() throws Exception {
        if (db != null) {
            db.close();
        }
    }

    public ArrayList<CartItemDTO> getCartItems() throws Exception {
        ArrayList<CartItemDTO> result = new ArrayList<>();
        CartItemDTO dto = null;
        int id, quantity;
        String name, image;
        double price;
        try {
            db = conn.getReadableDatabase();
            Cursor cs = db.rawQuery("SELECT fruit_id, fruit_name, quantity, price, image FROM Cart", null);
            cs.moveToFirst();
            while (!cs.isAfterLast()) {
                id = cs.getInt(0);
                name = cs.getString(1);
                quantity = cs.getInt(2);
                price = cs.getDouble(3);
                image = cs.getString(4);
                dto = new CartItemDTO(id, name,quantity, price, image);
                result.add(dto);
                cs.moveToNext();
            }
        } finally {
            closeConnection();
        }

        return result;
    }

    public CartItemDTO getCartItem(int id) throws Exception {
        CartItemDTO dto = null;
        int quantity;
        String name, image;
        double price;
        try {
            db = conn.getReadableDatabase();
            Cursor cs = db.rawQuery("SELECT fruit_name, quantity, price, image FROM Cart WHERE fruit_id = ?", new String[] {String.valueOf(id)});
            cs.moveToFirst();
            if (cs.isLast()) {
                name = cs.getString(0);
                quantity = cs.getInt(1);
                price = cs.getDouble(2);
                image = cs.getString(3);
                dto = new CartItemDTO(id, name, quantity, price, image);
            }
        } finally {
            closeConnection();
        }

        return dto;
    }

    public boolean addToCart(CartItemDTO dto) throws Exception {
        try {
            db = conn.getWritableDatabase();
            ContentValues values =new ContentValues();
            values.put("fruit_id", dto.getId());
            values.put("fruit_name", dto.getName());
            values.put("image", dto.getImage());
            values.put("price", dto.getPrice());
            values.put("quantity", dto.getQuantity());
            return db.insert("cart", null, values) > 0;
        } finally {
            closeConnection();
        }
    }

    public boolean updateCart(int id, int quantity) throws Exception {
        try {
            db = conn.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("quantity", quantity);

            return db.update("cart", values, "fruit_id = ?", new String[]{ String.valueOf(id) }) > 0;
        } finally {
            closeConnection();
        }
    }

    public boolean deleteItem(int id) throws Exception {
        try {
            db = conn.getWritableDatabase();
            return db.delete("Cart", "fruit_id = ?", new String[] {String.valueOf(id)}) > 0;
        } finally {
            closeConnection();
        }
    }

}
