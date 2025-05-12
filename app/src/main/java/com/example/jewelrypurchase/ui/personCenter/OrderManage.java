package com.example.jewelrypurchase.ui.personCenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jewelrypurchase.R;
import com.example.jewelrypurchase.adapter.OrdersAdapter;
import com.example.jewelrypurchase.jpWeb.Orders;
import com.example.jewelrypurchase.jpWeb.Result;
import com.example.jewelrypurchase.jpWeb.User;
import com.example.jewelrypurchase.jpWeb.WebUrl;
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

public class OrderManage extends AppCompatActivity {

    private RecyclerView orderManageRecycler;
    private OkHttpClient okHttpClient;
    private OrdersAdapter adapter;
    private Handler handler = new Handler(Looper.getMainLooper());

    private int userId;
    private String username;
    private List<Orders> orders = new ArrayList<>();

    private TextView orderIsNull;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_manage);

        ImageView orderManageBack = findViewById(R.id.orderManageBack);
        orderManageRecycler = findViewById(R.id.orderManageRecycler);
        orderIsNull = findViewById(R.id.orderIsNull);

        // 隐藏标题栏
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // 返回
        orderManageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        // 获取 SharedPreferences 对象
        SharedPreferences sharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        loadUserId(() -> {
            // 确保userId已赋值后，再执行后续逻辑
            loadAllOrder();
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        recreate();
    }

    private void loadAllOrder() {
        String url = new WebUrl().getBASE_URL() + "/api/orderList?userId=" + userId;
        Log.e("OrderLoadAllOrderUrl", "url: " + url);

        Request request = new Request.Builder().url(url).build();
        okHttpClient = new OkHttpClient.Builder().build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                handler.post(() -> {
                    Log.e("OrderLoadAllOrder", "加载失败: " + e.getMessage());
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String res = response.body().string();

//                Log.e("OrderLoadAllOrder", ": " + res);

                Type type = new TypeToken<Result<List<Orders>>>() {
                }.getType();
                Result<List<Orders>> ordersList = new Gson().fromJson(res, type);

                handler.post(() -> {
                    if (!ordersList.getCode().equals("200")) {
                        orderIsNull.setVisibility(View.VISIBLE);
                    } else {
                        orders.addAll(ordersList.getData());
                    }
                    if (adapter == null) {
                        adapter = new OrdersAdapter(OrderManage.this, orders);
                        orderManageRecycler.setLayoutManager(new GridLayoutManager(OrderManage.this, 1));
                        orderManageRecycler.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        orders = new ArrayList<>();
        loadUserId(() -> {
            // 确保userId已赋值后，再执行后续逻辑
            loadAllOrder();
        });
    }

    private void loadUserId(OrderManage.OnUserIdLoadedListener listener) {
        String Url = new WebUrl().getBASE_URL() + "/api/search_user?username=" + username;
        Request request = new Request.Builder().url(Url).build();
        okHttpClient = new OkHttpClient.Builder().build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                handler.post(() -> {
                    Log.e("OrderGetUsername", "加载失败: " + e.getMessage());
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e("OrderGetUsername", "加载失败");
                    return;
                }

                String res = response.body().string();
                Type type = new TypeToken<User>() {
                }.getType();
                User user = new Gson().fromJson(res, type);

                handler.post(() -> {
                    userId = user.getUserId();
                    // 回调通知userId已加载
                    if (listener != null) {
                        listener.onUserIdLoaded();
                    }
                });
            }
        });
    }

    // 在类顶部添加回调接口
    private interface OnUserIdLoadedListener {
        void onUserIdLoaded();
    }

}