package com.example.jewelrypurchase.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.jewelrypurchase.R;
import com.example.jewelrypurchase.jpWeb.Product;
import com.example.jewelrypurchase.jpWeb.Result;
import com.example.jewelrypurchase.jpWeb.WebUrl;
import com.example.jewelrypurchase.ui.inventory.InventoryEdit;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 库存Adapter
 */

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ViewHolder> {
    private List<Product> items;
    private Context context;
    private OkHttpClient okHttpClient;
    private Handler handler = new Handler(Looper.getMainLooper());

    // 构造函数，接收上下文和数据列表
    public InventoryAdapter(Context context, List<Product> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inventory_list_item, parent, false);
        return new InventoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // 获取当前位置的数据项
        Product item = items.get(position);

        // 设置数据到视图
        Glide.with(holder.itemView.getContext())
                .load(item.getImageUrl())
                .centerCrop()
                .error(R.drawable.error_picture)
                .into(holder.inventoryItemImage);

        holder.inventoryItemTitle.setText(item.getName());
        holder.inventoryItemPrice.setText(item.getPrice());
        holder.inventoryItemI.setText("库存：" + item.getStock());

        // 判断按钮显示
        if (Objects.equals(item.getIsSell(), "0")) {
            holder.inventoryItemUp.setText("上架");
            holder.inventoryItemUp.setBackgroundColor(Color.parseColor("#FFDF00"));
        } else {
            holder.inventoryItemUp.setText("下架");
            holder.inventoryItemUp.setBackgroundColor(Color.parseColor("#FF7F3C"));
        }

        /**
         *点击事件
         */
        // 跳转编辑页面
        holder.inventoryItemE.setOnClickListener(v -> {
            Intent intent = new Intent(context, InventoryEdit.class);
            intent.putExtra("inventoryId", item.getId());
            context.startActivity(intent);
        });

        // 上架&&下架
        holder.inventoryItemUp.setOnClickListener(v -> {
            okHttpClient = new OkHttpClient.Builder().build();

            String inventoryUrl = new WebUrl().getBASE_URL() + "/api/isSell?id=" + item.getId();
            String inventoryItemUpText = holder.inventoryItemUp.getText().toString();
            if (inventoryItemUpText == "上架") {
                inventoryUrl += "&isSell=1";
                item.setIsSell("1");
            } else {
                inventoryUrl += "&isSell=0";
                item.setIsSell("0");
            }

            Request RcarouselUrl = new Request.Builder()
                    .url(inventoryUrl)
                    .build();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Response response = okHttpClient.newCall(RcarouselUrl).execute();
                        String res = response.body().string();

                        Gson gson = new Gson();
                        Type listType = new TypeToken<Result>() {
                        }.getType();
                        Result result = gson.fromJson(res, listType);

                        // 在主线程中更新UI
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (result.getCode().equals("200")) {
                                    if (inventoryItemUpText == "上架") {
                                        holder.inventoryItemUp.setText("下架");
                                        holder.inventoryItemUp.setBackgroundColor(Color.parseColor("#FF7F3C"));
                                    } else {
                                        holder.inventoryItemUp.setText("上架");
                                        holder.inventoryItemUp.setBackgroundColor(Color.parseColor("#FFDF00"));
                                    }
                                }else if(result.getCode().equals("402")){
                                    Toast.makeText(context, "库存不足", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "服务器错误", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        });


        holder.inventoryListItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                deleteInventoryDialog(item.getName(), item.getId(), position);

                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView inventoryItemImage;
        private TextView inventoryItemTitle;
        public TextView inventoryItemPrice;
        public TextView inventoryItemI;
        public TextView inventoryItemE;
        public TextView inventoryItemUp;
        private LinearLayout inventoryListItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            inventoryItemImage = itemView.findViewById(R.id.inventoryItemImage);
            inventoryItemTitle = itemView.findViewById(R.id.inventoryItemTitle);
            inventoryItemPrice = itemView.findViewById(R.id.inventoryItemPrice);
            inventoryItemI = itemView.findViewById(R.id.inventoryItemI);
            inventoryItemE = itemView.findViewById(R.id.inventoryItemE);
            inventoryItemUp = itemView.findViewById(R.id.inventoryItemUp);
            inventoryListItem = itemView.findViewById(R.id.inventoryListItem);
        }
    }

    // 删除库存框
    public void deleteInventoryDialog(String name, String inventoryId, int position) {
        // 创建一个AlertDialog.Builder对象
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // 点击外部是否可以取消
        builder.setCancelable(false);
        // 设置对话框的内容
        builder.setTitle("删除库存");
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_privacy_policy, null);
        TextView editTextView = dialogView.findViewById(R.id.privacy_policy_text);
        editTextView.setText("是否删除 " + name + "?");
        builder.setView(dialogView);

        // 设置对话框的按钮
        builder.setPositiveButton("确定", (dialog, which) -> {
            SharedPreferences sharedPreferences = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
            String goodsAuthor = sharedPreferences.getString("token", "");

            okHttpClient = new OkHttpClient.Builder().build();
            String Url = new WebUrl().getBASE_URL() + "/api/delete/inventory/" + inventoryId + "?author=" + goodsAuthor;
            Request RcarouselUrl = new Request.Builder()
                    .url(Url)
                    .delete()
                    .build();

            new Thread(() -> {
                try {
                    Response response = okHttpClient.newCall(RcarouselUrl).execute();
                    String res = response.body().string();
                    System.out.println(res);
                    Gson gson = new Gson();
                    Type listType = new TypeToken<Result>() {
                    }.getType();
                    Result result = gson.fromJson(res, listType);

                    if (Objects.equals(result.getCode(), "200")) {
                        handler.post(() -> {
                            // 1. 从本地数据源移除对应项
                            items.remove(position);
                            // 2. 通知适配器数据变化
                            notifyDataSetChanged();
                            // 3. 可选：若删除后列表为空，更新空视图
                            if (getItemCount() == 0) {
                                // 触发空列表UI逻辑（需自行实现）
                            }
                            Toast.makeText(context, "删除成功！", Toast.LENGTH_SHORT).show();
                        });
                    }
                } catch (IOException e) {
                    handler.post(() ->
                            Toast.makeText(context, "删除失败！", Toast.LENGTH_SHORT).show()
                    );
                    e.printStackTrace();
                }
            }).start();
        });
        builder.setNegativeButton("取消", (dialog, which) -> {

        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
