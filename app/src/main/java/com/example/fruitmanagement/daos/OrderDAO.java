package com.example.fruitmanagement.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.fruitmanagement.db.MyDatabase;
import com.example.fruitmanagement.dtos.CartItemDTO;
import com.example.fruitmanagement.dtos.OrderDTO;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO implements Serializable {
    private MyDatabase conn = null;
    private SQLiteDatabase db = null;

    public OrderDAO(Context context) {
        conn = new MyDatabase(context);
    }

    private void closeConnection() throws Exception {
        if (db != null) {
            db.close();
        }
    }

    public ArrayList<OrderDTO> getOrders(String userId) throws Exception {
        ArrayList<OrderDTO> result = new ArrayList<>();
        OrderDTO dto;
        int id;
        String dateStr;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            db = conn.getReadableDatabase();
            Cursor cs = db.rawQuery("SELECT id, created_time FROM [Order] WHERE user_id = ?", new String[] {userId});
            cs.moveToFirst();

            while (!cs.isAfterLast()) {
                id = cs.getInt(0);
                dateStr = cs.getString(1);
                dto = new OrderDTO(id, dateFormat.parse(dateStr));
                result.add(dto);
                cs.moveToNext();
            }
        } finally {
            closeConnection();
        }
        return result;
    }

    public long create(OrderDTO order) throws Exception {
        try {
            db = conn.getWritableDatabase();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            ContentValues values = new ContentValues();
            values.put("user_id", order.getUserId());
            values.put("created_time", dateFormat.format(order.getTime()));
            return db.insert("[order]", null, values);
        } finally {
            closeConnection();
        }
    }

    public boolean addOrderDetail(long orderId, List<CartItemDTO> cartItemDTOList) throws Exception {
        boolean success = false;
        try {
            db = conn.getWritableDatabase();
            for (int i = 0; i < cartItemDTOList.size(); i++) {
                ContentValues values = new ContentValues();
                values.put("order_id", orderId);
                values.put("fruit_id", cartItemDTOList.get(i).getId());
                values.put("quantity", cartItemDTOList.get(i).getQuantity());
                success = db.insert("order_detail", null, values) > 0;
            }
            return success;
        } finally {
            closeConnection();
        }
    }
}
