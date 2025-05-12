package com.example.jewelrypurchase.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.jewelrypurchase.R;
import com.example.jewelrypurchase.jpWeb.Dashboard;
import com.example.jewelrypurchase.jpWeb.Product;
import com.example.jewelrypurchase.jpWeb.WebUrl;
import com.example.jewelrypurchase.ui.home.GoodDetails;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 购物车Adapter
 */

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.ViewHolder> {
    private Context context;
    private List<Dashboard> myDashboardGoods;
    private TextView myShoppingCartAllMoney;
    private List<Product> itemsPrice = new ArrayList<>();

    private OkHttpClient okHttpClient;
    private Handler handler = new Handler(Looper.getMainLooper());

    // 构造函数，接收上下文和数据列表
    public ShoppingCartAdapter(Context context, List<Dashboard> myDashboardGoods, TextView myShoppingCartAllMoney) {
        this.context = context;
        this.myDashboardGoods = myDashboardGoods;
        this.myShoppingCartAllMoney = myShoppingCartAllMoney;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shoppingcart_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // 获取当前位置的数据项
        String Url = new WebUrl().getBASE_URL() + "/api/search/product?productId=" + myDashboardGoods.get(position).getProductId();
        Request RcarouselUrl = new Request.Builder()
                .url(Url)
                .build();

        okHttpClient = new OkHttpClient.Builder().build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = okHttpClient.newCall(RcarouselUrl).execute();
                    String res = response.body().string();

                    // Log.e("dashboard", res);

                    Gson gson = new Gson();
                    Type listType = new TypeToken<Product>() {
                    }.getType();

                    Product productItem = gson.fromJson(res, listType);

                    while (position > itemsPrice.size()) {
                        itemsPrice.add(null);
                    }
                    if (position == itemsPrice.size()) {
                        itemsPrice.add(productItem);
                    }else {
                        itemsPrice.set(position, productItem);
                    }

                    handler.post(() -> {
                        // 设置数据到视图
                        Glide.with(holder.itemView.getContext())
                                .load(productItem.getImageUrl())
                                .centerCrop()
                                .error(R.drawable.error_picture)
                                .into(holder.dashboardGoodsImg);
                        holder.goodsName.setText(productItem.getName());
                        holder.dashboardType.setText(productItem.getAnthor());
                        holder.dashboardGoodsPrice.setText(productItem.getPrice());
                        holder.dashboardGoodsNumber.setText("×" + myDashboardGoods.get(position).getGoodsNum());
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // 禁用dashboardGoodsCheck控件的交互
//        holder.dashboardGoodsCheck.setClickable(false);
//        holder.dashboardGoodsCheck.setFocusable(false);

        // 计算商品价格之和
        holder.dashboardGoodsCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Log.e("dashboard", String.valueOf(position));
                double startPrice = Double.parseDouble(myShoppingCartAllMoney.getText().toString());
                double price = Double.parseDouble(itemsPrice.get(position).getPrice()) * myDashboardGoods.get(position).getGoodsNum();
                if (!holder.dashboardGoodsCheck.isChecked()) {
                    double newPrice = startPrice - price;
                    myShoppingCartAllMoney.setText(String.format("%.2f", newPrice)); // 保留两位小数
                } else {
                    double newPrice = startPrice + price;
                    myShoppingCartAllMoney.setText(String.format("%.2f", newPrice)); // 保留两位小数
                }
            }
        });

        // 跳转商品界面
        holder.dashboardList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, GoodDetails.class);
                intent.putExtra("goodsID", String.valueOf(myDashboardGoods.get(position).getProductId()));
                context.startActivity(intent);
            }
        });

        // 删除购物车商品
        holder.dashboardList.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // 添加position参数
                deleteDashboardGoodsDialog(position);
                return false;
            }
        });
    }

    // 返回特定位置的数据项
    @Override
    public int getItemCount() {
        return myDashboardGoods == null ? 0 : myDashboardGoods.size();
    }

    // 返回特定位置的项ID
    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView dashboardGoodsImg;
        private TextView goodsName;
        private TextView dashboardGoodsPrice;
        private TextView dashboardType;
        private TextView dashboardGoodsNumber;
        private LinearLayout dashboardList;
        private CheckBox dashboardGoodsCheck;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dashboardGoodsImg = itemView.findViewById(R.id.dashboardGoodsImg);
            goodsName = itemView.findViewById(R.id.doshboardGoodsName);
            dashboardType = itemView.findViewById(R.id.dashboardType);
            dashboardGoodsPrice = itemView.findViewById(R.id.dashboardGoodsPrice);
            dashboardGoodsNumber = itemView.findViewById(R.id.dashboardGoodsNumber);
            dashboardList = itemView.findViewById(R.id.dashboardList);
            dashboardGoodsCheck = itemView.findViewById(R.id.dashboardGoodsCheck);
        }
    }

    // 删除购物车商品框
    public void deleteDashboardGoodsDialog(int position) {
        // 创建一个AlertDialog.Builder对象
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // 点击外部是否可以取消
        builder.setCancelable(false);
        // 设置对话框的内容
        builder.setTitle("删除商品");
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_privacy_policy, null);
        TextView editTextView = dialogView.findViewById(R.id.privacy_policy_text);
        editTextView.setText("是否删除 "+ itemsPrice.get(position).getName() +"?");
        builder.setView(dialogView);

        // 设置对话框的按钮
        builder.setPositiveButton("确定", (dialog, which) -> {
            okHttpClient = new OkHttpClient.Builder().build();
            String Url = new WebUrl().getBASE_URL() + "/api/delete/dashboard/" + myDashboardGoods.get(position).getDashboardId();
            Request RcarouselUrl = new Request.Builder()
                    .url(Url)
                    .delete()
                    .build();

            new Thread(() -> {
                try {
                    Response response = okHttpClient.newCall(RcarouselUrl).execute();
                    if (response.isSuccessful()) {
                        handler.post(() -> {
                            // 1. 从本地数据源移除对应项
                            itemsPrice.remove(position);
                            myDashboardGoods.remove(position);
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
