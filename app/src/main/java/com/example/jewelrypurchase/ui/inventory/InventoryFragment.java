package com.example.jewelrypurchase.ui.inventory;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jewelrypurchase.R;
import com.example.jewelrypurchase.adapter.InventoryAdapter;
import com.example.jewelrypurchase.databinding.FragmentInventoryBinding;
import com.example.jewelrypurchase.jpWeb.Product;
import com.example.jewelrypurchase.jpWeb.Result;
import com.example.jewelrypurchase.jpWeb.WebUrl;
import com.example.jewelrypurchase.models.ScrollHideHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class InventoryFragment extends Fragment {

    private FragmentInventoryBinding binding;
    private List<Product> items;
    private TextView nullInventory;
    private String goodsAuthor;
    // 库存列表
    private RecyclerView inventoryList;
    private InventoryAdapter InventoryAdapter;
    private CardView addInventory;
    private OkHttpClient okHttpClient;
    private SharedPreferences sharedPreferences;
    private Handler handler = new Handler(Looper.getMainLooper());
    private ScrollHideHelper scrollHideHelper;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentInventoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        nullInventory = root.findViewById(R.id.nullInventory);
        inventoryList = root.findViewById(R.id.listInventory);
        addInventory = root.findViewById(R.id.addInventory);

        // 点击编辑
        addInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), InventoryEdit.class);
                startActivity(intent);
            }
        });

        // 初始化数据
        items = new ArrayList<>();

        sharedPreferences = requireActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        goodsAuthor = sharedPreferences.getString("token", "");

        okHttpClient = new OkHttpClient.Builder().build();

        String inventoryUrl = new WebUrl().getBASE_URL() + "/api/inventory?author=" + goodsAuthor;
        Request RcarouselUrl = new Request.Builder()
                .url(inventoryUrl)
                .build();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = okHttpClient.newCall(RcarouselUrl).execute();
                    String res = response.body().string();

                    Log.e("Inventory", res);

                    if (res != "") {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<Result<List<Product>>>() {
                        }.getType();
                        Result<List<Product>> result = gson.fromJson(res, listType);

                        // 在主线程中更新UI
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (result.getCode().equals("200")) {
                                    items.addAll(result.getData());
                                    nullInventory.setVisibility(View.GONE);
                                    InventoryAdapter.notifyDataSetChanged();
                                }
                            }
                        });
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // 设置网格布局管理器
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1); // 每行显示2列
        inventoryList.setLayoutManager(layoutManager);

        // 创建并设置Adapter
        InventoryAdapter = new InventoryAdapter(getActivity(), items);
        inventoryList.setAdapter(InventoryAdapter);

        // 动画效果
        scrollHideHelper = new ScrollHideHelper(inventoryList, addInventory, 1);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
