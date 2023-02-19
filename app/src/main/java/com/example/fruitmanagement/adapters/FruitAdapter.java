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
import com.example.fruitmanagement.dtos.FruitDTO;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FruitAdapter extends BaseAdapter {
    private ArrayList<FruitDTO> fruitDTOList;

    public void setFruitDTOList(ArrayList<FruitDTO> fruitDTOList) {
        this.fruitDTOList = fruitDTOList;
    }

    @Override
    public int getCount() {
        return fruitDTOList.size();
    }

    @Override
    public Object getItem(int position) {
        return fruitDTOList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return fruitDTOList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.fruit, parent, false);
        }

        FruitDTO dto = (FruitDTO) getItem(position);
        TextView txtName = convertView.findViewById(R.id.txtName);
        TextView txtPrice = convertView.findViewById(R.id.txtPrice);
        ImageView imgView = convertView.findViewById(R.id.imgFruitView);
        Button btnAddToCart = convertView.findViewById(R.id.btnAddToCart);

        txtName.setText(dto.getName());
        txtPrice.setText(dto.getPrice() + "$");
        try {
            Picasso.get().load(dto.getImage()).fit().into(imgView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartDAO dao = new CartDAO(v.getContext());
                try {
                    CartItemDTO cartItem = dao.getCartItem(dto.getId());
                    if (cartItem == null) {
                        cartItem = new CartItemDTO(dto.getId(), dto.getName(), 1, dto.getPrice(), dto.getImage());
                        dao.addToCart(cartItem);
                    } else {
                        int quantity = cartItem.getQuantity() + 1;
                        dao.updateCart(dto.getId(), quantity);
                    }
                    Toast.makeText(v.getContext(), "Added to Cart", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        return convertView;
    }
}
