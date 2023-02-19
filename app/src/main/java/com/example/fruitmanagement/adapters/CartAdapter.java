package com.example.fruitmanagement.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fruitmanagement.R;
import com.example.fruitmanagement.daos.CartDAO;
import com.example.fruitmanagement.dtos.CartItemDTO;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CartAdapter extends BaseAdapter {
    private ArrayList<CartItemDTO> cartDTOList;

    public void setCartDTOList(ArrayList<CartItemDTO> cartDTOList) {
        this.cartDTOList = cartDTOList;
    }

    public ArrayList<CartItemDTO> getCartDTOList() {
        return cartDTOList;
    }

    @Override
    public int getCount() {
        return cartDTOList.size();
    }

    @Override
    public CartItemDTO getItem(int position) {
        return cartDTOList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.cart_item, parent, false);
        }

        CartItemDTO dto = getItem(position);
        TextView txtName = convertView.findViewById(R.id.txtCartItemName);
        TextView txtPrice = convertView.findViewById(R.id.txtCartItemPrice);
        TextView txtQuantity = convertView.findViewById(R.id.txtCartItemQty);
        ImageView imgCartItem = convertView.findViewById(R.id.imgCartItemView);
        Button btnIncrease = convertView.findViewById(R.id.btnIncreaseCart);
        Button btnDecrease = convertView.findViewById(R.id.btnDecreaseCart);

        txtName.setText(dto.getName());
        txtPrice.setText(dto.getPrice() + "$");
        txtQuantity.setText(String.valueOf(dto.getQuantity()));
        try {
            Picasso.get().load(dto.getImage()).fit().into(imgCartItem);
        } catch (Exception e) {
            e.printStackTrace();
        }

        CartDAO dao = new CartDAO(convertView.getContext());

        btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Click to increase
                try {
                    boolean success = dao.updateCart(dto.getId(), dto.getQuantity() + 1);
                    if (success) {
                        getItem(position).setQuantity(dto.getQuantity() + 1);
                        notifyDataSetChanged();
                    } else {
                        Toast.makeText(v.getContext(), "Error occurred.", Toast.LENGTH_LONG);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Click to decrease
                try {
                    boolean success = false;
                    if (dto.getQuantity() == 1) {
                        success = dao.deleteItem(dto.getId());
                        cartDTOList.remove(dto);
                    } else {
                        success = dao.updateCart(dto.getId(), dto.getQuantity() - 1);
                        getItem(position).setQuantity(dto.getQuantity() - 1);
                    }
                    if (success) {
                        notifyDataSetChanged();
                    } else {
                        Toast.makeText(v.getContext(), "Error occurred.", Toast.LENGTH_LONG);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        return convertView;
    }



    private void update() {
        notifyDataSetChanged();
    }
}
