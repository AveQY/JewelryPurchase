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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jewelrypurchase.R;
import com.example.jewelrypurchase.adapter.AddressAdapter;
import com.example.jewelrypurchase.jpWeb.Address;
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

public class AddressManage extends AppCompatActivity {
    private RecyclerView addressManageRecycler;
    private SharedPreferences sharedPreferences;
    private String addressAuthor;

    private OkHttpClient okHttpClient;
    private Handler handler = new Handler(Looper.getMainLooper());
    private List<Address> addresses;
    private AddressAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_manage);

        // 隐藏标题栏
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        sharedPreferences = this.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        addressAuthor = sharedPreferences.getString("token", "");

        ImageView addressBack = findViewById(R.id.addressBack);
        addressBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ImageView addAddress = findViewById(R.id.addAddress);
        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(AddressManage.this, AddressEdit.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        addresses = new ArrayList<>();
        addressManageRecycler = findViewById(R.id.addressManageRecycler);
        getAllAddress();
    }

    private void getAllAddress() {
        String Url = new WebUrl().getBASE_URL() + "/api/search/address?userToken=" + addressAuthor;
        Request request = new Request.Builder().url(Url).build();
        okHttpClient = new OkHttpClient.Builder().build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                handler.post(() -> {
                    Log.e("Address", "加载失败: " + e.getMessage());
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e("Address", "加载失败");
                    return;
                }

                String res = response.body().string();
                Type type = new TypeToken<List<Address>>() {
                }.getType();
                List<Address> pageResponse = new Gson().fromJson(res, type);

                handler.post(() -> {
                    addresses.addAll(pageResponse);
                    if (adapter == null) {
                        adapter = new AddressAdapter(AddressManage.this, addresses);
                        addressManageRecycler.setLayoutManager(new GridLayoutManager(AddressManage.this, 1));
                        addressManageRecycler.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });


    }
}