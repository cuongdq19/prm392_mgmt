package com.example.fruitmanagement.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.fruitmanagement.R;
import com.example.fruitmanagement.dtos.OrderDTO;

import java.util.ArrayList;

public class OrderAdapter extends BaseAdapter {
    ArrayList<OrderDTO> orderDTOArrayList;

    public void setOrderDTOArrayList(ArrayList<OrderDTO> orderDTOArrayList) {
        this.orderDTOArrayList = orderDTOArrayList;
    }

    @Override
    public int getCount() {
        return orderDTOArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return orderDTOArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return orderDTOArrayList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.order, parent, false);
        };

        OrderDTO order =  (OrderDTO) getItem(position);
        TextView txtOrderId = convertView.findViewById(R.id.txtOrderId);
        txtOrderId.setText("Order #" + order.getId() + " - " + order.getTime().toString());

        return convertView;
    }
}
