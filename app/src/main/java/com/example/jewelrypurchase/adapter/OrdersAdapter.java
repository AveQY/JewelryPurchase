package com.example.jewelrypurchase.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.jewelrypurchase.R;
import com.example.jewelrypurchase.jpWeb.Orders;
import com.example.jewelrypurchase.jpWeb.Product;
import com.example.jewelrypurchase.jpWeb.WebUrl;
import com.example.jewelrypurchase.ui.dashboard.OrderDetails;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {

    private OkHttpClient okHttpClient;
    private Handler handler = new Handler(Looper.getMainLooper());

    private Context context;
    private List<Orders> orders;
    private List<Product> orderProduct = new ArrayList<>();

    public OrdersAdapter(Context context, List<Orders> orders) {
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrdersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrdersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Orders order = orders.get(position);

        // 异步加载订单商品数据
        String rGoodsUrl = new WebUrl().getBASE_URL() + "/api/search/product?productId=" + order.getProductId();
        Request request = new Request.Builder().url(rGoodsUrl).build();
        okHttpClient = new OkHttpClient.Builder().build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                handler.post(() -> {
                    Log.e("ordersAdapter", "加载失败: " + e.getMessage());
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                String res = response.body().string();
                Type type = new TypeToken<Product>() {
                }.getType();
                Product product = new Gson().fromJson(res, type);

                while (position > orderProduct.size()) {
                    orderProduct.add(null);
                }
                if (position == orderProduct.size()) {
                    orderProduct.add(product);
                }else {
                    orderProduct.set(position, product);
                }

                handler.post(() -> {
                    holder.orderGoodsName.setText(product.getName());
                    // 设置数据到视图
                    Glide.with(context)
                            .load(product.getImageUrl())
                            .centerCrop()
                            .error(R.drawable.error_picture)
                            .into(holder.orderItemImage);
                });
            }
        });

        // 加载订单状态
        holder.orderStatusText.setText(order.getStatus());
        // 加载订单金额
        holder.orderPrice.setText(order.getTotalAmount().toString());

        // 跳转订单详细
        holder.orderDetailsBtu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OrderDetails.class);
                intent.putExtra("ORDERID", order.getOrderId().toString());
                intent.putExtra("Status", "LOAD");
                intent.putExtra("orderImage", orderProduct.get(position).getImageUrl());
                intent.putExtra("orderPrice", orderProduct.get(position).getPrice());
                context.startActivity(intent);
            }
        });

        // 删除订单
        holder.orderDeleteBtu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteOrder(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders == null ? 0 : orders.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView orderItemImage;
        private TextView orderGoodsName;
        private TextView orderStatusText;
        private TextView orderPrice;
        private LinearLayout orderListItem;
        private CardView orderDeleteBtu;
        private CardView orderDetailsBtu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderItemImage = itemView.findViewById(R.id.orderItemImage);
            orderGoodsName = itemView.findViewById(R.id.orderGoodsName);
            orderStatusText = itemView.findViewById(R.id.orderStatusText);
            orderPrice = itemView.findViewById(R.id.orderPrice);
            orderListItem = itemView.findViewById(R.id.orderListItem);
            orderDeleteBtu = itemView.findViewById(R.id.orderDeleteBtu);
            orderDetailsBtu = itemView.findViewById(R.id.orderDetailsBtu);
        }
    }

    /**
     * 删除订单
     */
    private void deleteOrder(int p) {

        Log.e("myOrders", p + ":" + orders.size());
        Log.e("myOrdersProduct", p + ":" + orderProduct.size());

        // 创建一个AlertDialog.Builder对象
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // 点击外部是否可以取消
        builder.setCancelable(false);
        // 设置对话框的内容
        builder.setTitle("删除订单");
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_privacy_policy, null);
        TextView editTextView = dialogView.findViewById(R.id.privacy_policy_text);
        editTextView.setText("是否删除该订单：" + orderProduct.get(p).getName() + "?");
        builder.setView(dialogView);

        // 设置对话框的按钮
        builder.setPositiveButton("确定", (dialog, which) -> {
            okHttpClient = new OkHttpClient.Builder().build();
            String Url = new WebUrl().getBASE_URL() + "/api/delete/order/" + orders.get(p).getOrderId();
            Request RcarouselUrl = new Request.Builder().url(Url).delete().build();

            new Thread(() -> {
                try {
                    Response response = okHttpClient.newCall(RcarouselUrl).execute();
                    if (response.isSuccessful()) {
                        handler.post(() -> {
                            // 1. 从本地数据源移除对应项
                            orders.remove(p);
                            orderProduct.remove(p);
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
