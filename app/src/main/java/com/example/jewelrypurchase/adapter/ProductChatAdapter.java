package com.example.jewelrypurchase.adapter;

import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jewelrypurchase.R;
import com.example.jewelrypurchase.jpWeb.ProductChatItem;
import com.example.jewelrypurchase.ui.chat.ChatDemails;

import java.sql.Date;
import java.util.List;
import java.util.Locale;

public class ProductChatAdapter extends RecyclerView.Adapter<ProductChatAdapter.ViewHolder> {

    private final List<ProductChatItem> items;
    private Context context;

    public ProductChatAdapter(Context context, List<ProductChatItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductChatItem item = items.get(position);

        // 设置商品信息
        holder.tvProductName.setText(item.getProductName());
        holder.ivProductImage.setImageResource(item.getProductImageResId());

        // 设置最后一条消息（发送者: 消息内容）
        String formattedMessage = item.getLastSender() + ": " + item.getLastMessage();
        holder.tvLastMessage.setText(formattedMessage);

        // 设置时间
        holder.tvTime.setText(formatTime(item.getTimestamp()));

        // 设置未读消息
        if (item.getUnreadCount() > 0) {
            holder.tvUnread.setVisibility(View.VISIBLE);
            holder.tvUnread.setText(String.valueOf(item.getUnreadCount()));
        } else {
            holder.tvUnread.setVisibility(View.INVISIBLE);
        }

        holder.chatList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatDemails.class);
                intent.putExtra("id", item.getProductId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private String formatTime(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return format.format(date);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout chatList;
        private ImageView ivProductImage;
        private TextView tvProductName;
        private TextView tvLastMessage;
        private TextView tvTime;
        private TextView tvUnread;

        public ViewHolder(View view) {
            super(view);
            chatList = view.findViewById(R.id.chatList);
            ivProductImage = view.findViewById(R.id.iv_product_image);
            tvProductName = view.findViewById(R.id.tv_product_name);
            tvLastMessage = view.findViewById(R.id.tv_last_message);
            tvTime = view.findViewById(R.id.tv_time);
            tvUnread = view.findViewById(R.id.tv_unread);
        }
    }
}