package com.example.jewelrypurchase.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.jewelrypurchase.ui.home.AuctionDetail;
import com.example.jewelrypurchase.R;
import com.example.jewelrypurchase.jpWeb.Auction;

import java.util.List;

public class AuctionViewAdapter extends RecyclerView.Adapter<AuctionViewAdapter.ViewHolder> {
    // 商品
    private List<Auction> goods;
    private Context context;

    public AuctionViewAdapter(Context context, List<Auction> goods) {
        this.context = context;
        this.goods = goods;
    }

    @NonNull
    @Override
    public AuctionViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.auction_list_item, parent, false);
        return new AuctionViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 获取当前位置的数据项
        Auction auction = goods.get(position);

        // 设置数据到视图
        Glide.with(holder.itemView.getContext())
                .load(auction.getImageUrl())
                .centerCrop()
                .error(R.drawable.error_picture)
                .into(holder.goodsImgUrl);
        holder.goodsName.setText(auction.getName());
        holder.goodsEndTime.setText(auction.getEndTime()+" 结拍");
        holder.goodsInventory.setText("拍品 "+auction.getStock() +" 件");
        holder.goodsAuthor.setText(auction.getAuthor());

        holder.auctionPlayBtu.setOnClickListener(v -> {
            Intent intent = new Intent(context, AuctionDetail.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("Auction", auction);
            context.startActivity(intent);
            Toast.makeText(context, "拍卖已结束", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return goods == null ? 0 : goods.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView goodsImgUrl;
        private TextView goodsName;
        private TextView goodsEndTime;
        private TextView goodsInventory;
        private TextView goodsAuthor;
        private CardView auctionPlayBtu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            goodsImgUrl = itemView.findViewById(R.id.auction_mianImg);
            goodsName = itemView.findViewById(R.id.auction_name);
            goodsEndTime = itemView.findViewById(R.id.auction_overTime);
            goodsInventory = itemView.findViewById(R.id.auction_goodsNum);
            goodsAuthor = itemView.findViewById(R.id.auction_username);
            auctionPlayBtu = itemView.findViewById(R.id.auction_playBtu);
        }
    }
}