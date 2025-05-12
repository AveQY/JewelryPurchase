package com.example.jewelrypurchase.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.jewelrypurchase.R;
import com.example.jewelrypurchase.jpWeb.Product;
import com.example.jewelrypurchase.ui.home.GoodDetails;

import java.util.List;

public class GridViewAdapter extends RecyclerView.Adapter<GridViewAdapter.ViewHolder> {
    // 商品
    private List<Product> goods;
    private Context context;

    public GridViewAdapter(Context context, List<Product> goods) {
        this.context = context;
        this.goods = goods;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.goods_grid_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // 获取当前位置的数据项
        Product item = goods.get(position);

        // 设置数据到视图
        Glide.with(holder.itemView.getContext())
                .load(item.getImageUrl())
                .centerCrop()
                .error(R.drawable.error_picture)
                .into(holder.goodsImgUrl);
        holder.goodsTextView.setText(item.getName());
        holder.goodsmoney.setText(item.getPrice());
        holder.goodsInventory.setText(item.getStock());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, GoodDetails.class);
            intent.putExtra("goodsID", item.getId());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return goods == null ? 0 : goods.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView goodsImgUrl;
        private TextView goodsTextView;
        private TextView goodsmoney;
        private TextView goodsInventory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            goodsImgUrl = itemView.findViewById(R.id.goodsGridImage);
            goodsTextView = itemView.findViewById(R.id.GridGoodstext);
            goodsmoney = itemView.findViewById(R.id.goodsmoney);
            goodsInventory = itemView.findViewById(R.id.GridGoodsQuantity);
        }
    }
}
