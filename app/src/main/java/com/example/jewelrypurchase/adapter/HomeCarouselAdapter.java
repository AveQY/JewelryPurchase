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

public class HomeCarouselAdapter extends RecyclerView.Adapter<HomeCarouselAdapter.ViewHolder> {
    private List<String> images; // 假设你用图片的资源ID列表

    public HomeCarouselAdapter(List<String> images) {
        this.images = images;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carousel_home_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.carouselImageView);
        }
    }
}
