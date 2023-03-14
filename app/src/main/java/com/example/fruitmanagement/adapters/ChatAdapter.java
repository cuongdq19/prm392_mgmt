package com.example.fruitmanagement.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.fruitmanagement.R;
import com.example.fruitmanagement.models.Message;

import java.util.ArrayList;

public class ChatAdapter extends BaseAdapter {
    private ArrayList<Message> messages = new ArrayList<>();
    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Message getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void addMessage(Message message) {
        messages.add(message);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.message, parent, false);
        }

        TextView txtMsgContent = convertView.findViewById(R.id.txtMsgContent);

        Message currentMsg = getItem(position);
        txtMsgContent.setText(currentMsg.getUsername() + ": " + currentMsg.getContent());

        return convertView;
    }
}
