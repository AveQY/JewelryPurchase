package com.example.jewelrypurchase.ui.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.example.jewelrypurchase.adapter.ShoppingCartAdapter;
import com.example.jewelrypurchase.databinding.FragmentDashboardBinding;
import com.example.jewelrypurchase.jpWeb.Dashboard;
import com.example.jewelrypurchase.jpWeb.Product;
import com.example.jewelrypurchase.jpWeb.WebUrl;
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
 * 购物车
 */

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    private List<Product> items;
    private RecyclerView dashboardGoodsView;
    private ShoppingCartAdapter shoppingCartAdapter;

    private OkHttpClient okHttpClient;
    private Handler handler = new Handler(Looper.getMainLooper());

    private List<Dashboard> goodsId;
    private String username;
    private CardView dashboardAllPay;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        dashboardGoodsView = root.findViewById(R.id.list_shopping_cart);
        dashboardAllPay = root.findViewById(R.id.dashboardAllPay);

        TextView myShoppingCartAllMoney = root.findViewById(R.id.myShoppingCartAllMoney);

        // 获取 SharedPreferences 对象
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        username = sharedPreferences.getString("username", "");

        // 购物车商品列表
        items = new ArrayList<>();
        // 加载购物车
        goodsId = new ArrayList<>();
        okHttpClient = new OkHttpClient.Builder().build();
        String dashboardUrl = new WebUrl().getBASE_URL() + "/api/dashboard?buyer=" + username;
        Request RcarouselUrl = new Request.Builder()
                .url(dashboardUrl)
                .build();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = okHttpClient.newCall(RcarouselUrl).execute();
                    String res = response.body().string();

                    // Log.e("Dashboard", res);

                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<Dashboard>>() {
                    }.getType();
                    List<Dashboard> itemList = gson.fromJson(res, listType);

                    goodsId.addAll(itemList);

                    handler.post(() -> {
                        // 设置Adapter
                        shoppingCartAdapter = new ShoppingCartAdapter(getActivity(), goodsId, myShoppingCartAllMoney);
                        dashboardGoodsView.setAdapter(shoppingCartAdapter);
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // 商品adapter
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1); // 每行显示2列
        dashboardGoodsView.setLayoutManager(layoutManager);

        // 结算 跳转到商品界面用户自行选购 (暂时隐藏)
        dashboardAllPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // to do...
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}