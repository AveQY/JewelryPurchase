package com.example.jewelrypurchase.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.jewelrypurchase.R;

import java.util.List;

public class GoodsImgAdaper extends RecyclerView.Adapter<HomeCarouselAdapter.ViewHolder> {
    private List<String> images; // 假设你用图片的资源ID列表

    public GoodsImgAdaper(List<String> images) {
        this.images = images;
    }

    @NonNull
    @Override
    public HomeCarouselAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.goods_show_img, parent, false);
        return new HomeCarouselAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeCarouselAdapter.ViewHolder holder, int position) {
        String imageUrl = images.get(position % images.size());
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .error(R.drawable.error_picture)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return images == null ? 0 : images.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.carouselImageView);
        }
    }
}
