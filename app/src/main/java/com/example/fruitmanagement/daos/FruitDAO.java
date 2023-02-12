package com.example.fruitmanagement.daos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.fruitmanagement.db.MyDatabase;
import com.example.fruitmanagement.dtos.FruitDTO;

import java.io.Serializable;
import java.util.ArrayList;

public class FruitDAO implements Serializable {
    private MyDatabase conn = null;
    private SQLiteDatabase db = null;

    public FruitDAO(Context context) {
        conn = new MyDatabase(context);
    }

    public ArrayList<FruitDTO> getFruits() throws Exception {
        ArrayList<FruitDTO> result = new ArrayList<>();
        int id, quantity;
        String name, description, image;
        double price;
        FruitDTO dto = null;

        try {
            db = conn.getReadableDatabase();
            Cursor cs = db.rawQuery("SELECT Id, Name, Description, Price, Quantity, Image " +
                    "FROM Fruit", null);

            cs.moveToFirst();
            while (!cs.isAfterLast()) {
                id = cs.getInt(0);
                name = cs.getString(1);
                description = cs.getString(2);
                price = cs.getDouble(3);
                quantity = cs.getInt(4);
                image = cs.getString(5);
                dto = new FruitDTO(id, name, description, price, quantity, image);
                result.add(dto);
                cs.moveToNext();
            }
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return result;
    }
}
