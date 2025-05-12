package com.example.jewelrypurchase.ui.home;

import android.content.Intent;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jewelrypurchase.R;
import com.example.jewelrypurchase.adapter.CategoryAdapter;
import com.example.jewelrypurchase.adapter.GridViewAdapter;
import com.example.jewelrypurchase.jpWeb.Product;
import com.example.jewelrypurchase.jpWeb.ProductCategory;
import com.example.jewelrypurchase.jpWeb.WebUrl;
import com.example.jewelrypurchase.jpWeb.util.PageResponse;
import com.example.jewelrypurchase.jpWeb.util.StatusBar;
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

public class CategoryActivity extends AppCompatActivity {
    private CategoryAdapter categoryAdapter;
    private ImageView cBack;
    private TextView searchText;
    private TextView goodsIsNull;
    private RecyclerView categoryList;
    private RecyclerView productGrid;

    private int goodsCategoryId;

    private OkHttpClient okHttpClient;
    private Handler handler = new Handler(Looper.getMainLooper());
    private int currentPage = 0;
    private boolean isLoading = false;
    private boolean hasMore = true;
    private List<ProductCategory> goodsCategory = new ArrayList<>();
    private List<Product> goodsdata = new ArrayList<>();
    private GridViewAdapter goods_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_category);

        // 隐藏标题栏
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // 设置状态栏
        new StatusBar().defaultStyle(getWindow(),this);

        productGrid = findViewById(R.id.productGrid);
        goodsIsNull = findViewById(R.id.goodsIsNull);

        cBack = findViewById(R.id.cBack);
        cBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(0, R.anim.fade_out);
            }
        });

        searchText = findViewById(R.id.searchText);
        searchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 搜索界面
                finish();
                Intent intent = new Intent(CategoryActivity.this, SearchActivity.class);
                startActivity(intent);
                overridePendingTransition(0, R.anim.fade_out);
            }
        });

        // 初始化分类列表
        categoryList = findViewById(R.id.categoryList);
        categoryList.setLayoutManager(new LinearLayoutManager(this));

        loadCategory();
        productGrid.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager == null) return;

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();

                if (!isLoading && hasMore && (lastVisiblePosition >= totalItemCount - 1)) {
                    loadGoods();
                }
            }
        });

    }

    private void loadCategory() {
        String Url = new WebUrl().getBASE_URL() + "/api/search/all/productCategory";
        Request request = new Request.Builder().url(Url).build();

        okHttpClient = new OkHttpClient.Builder().build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                handler.post(() -> {
                    goodsIsNull.setVisibility(View.VISIBLE);
                    Log.e("Category", "加载失败: " + e.getMessage());
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    handler.post(() -> {
                        Log.e("Category", "响应异常: HTTP " + response.code());
                    });
                    return;
                }

                String res = response.body().string();
//                Log.d("Category", "响应数据: " + res);

                Type type = new TypeToken<List<ProductCategory>>() {
                }.getType();
                List<ProductCategory> productCategories = new Gson().fromJson(res, type);

                goodsCategory.addAll(productCategories);

                handler.post(() -> {
                    categoryAdapter = new CategoryAdapter(goodsCategory);
                    // 修改分类点击监听逻辑（在loadCategory()方法中）
                    categoryAdapter.setOnCategoryClickListener(position -> {
                        // 重置分页参数
                        currentPage = 0;
                        hasMore = true;

                        // 清除旧数据
                        goodsdata.clear();
                        if (goods_adapter != null) {
                            goods_adapter.notifyDataSetChanged(); // 立即清空显示
                        }

                        // 加载新分类数据
                        goodsCategoryId = goodsCategory.get(position).getCategoryId();
                        loadGoods();

                        // 更新选中状态（如果ProductCategory有选中状态字段）
                        for (int i = 0; i < goodsCategory.size(); i++) {
                            goodsCategory.get(i).setSelected(i == position);
                        }
                        categoryAdapter.notifyDataSetChanged();
                    });
                    categoryList.setAdapter(categoryAdapter);
                });
            }
        });
    }

    private void loadGoods() {
        if (isLoading || !hasMore) return;
        isLoading = true;

        String Url = new WebUrl().getBASE_URL() + "/api/search/product/category?categoryId=" + goodsCategoryId + "&page=" + currentPage + "&size=8";
        Request request = new Request.Builder().url(Url).build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                handler.post(() -> {
                    Log.e("Category", "加载失败: " + e.getMessage());
                    isLoading = false;
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    handler.post(() -> {
                        Log.e("Category", "响应异常: HTTP " + response.code());
                        isLoading = false;
                    });
                    return;
                }

                String res = response.body().string();
                Type type = new TypeToken<PageResponse<Product>>() {
                }.getType();
                PageResponse<Product> pageResponse = new Gson().fromJson(res, type);
                List<Product> newItems = pageResponse.getContent();

                handler.post(() -> {
                    if (newItems != null && !newItems.isEmpty()) {
                        int startPosition = goodsdata.size();
                        goodsdata.addAll(newItems);

                        if (goods_adapter == null) {
                            goods_adapter = new GridViewAdapter(CategoryActivity.this, goodsdata);
                            productGrid.setLayoutManager(new GridLayoutManager(CategoryActivity.this, 1));
                            productGrid.setAdapter(goods_adapter);
                        } else {
                            if (startPosition == 0) {
                                goods_adapter.notifyDataSetChanged();
                            } else {
                                goods_adapter.notifyItemRangeInserted(startPosition, newItems.size());
                            }
                        }

                        currentPage++;
                        hasMore = !pageResponse.isLast();
                    } else {
                        hasMore = false;
                    }

                    goodsIsNull.setVisibility(goodsdata.isEmpty() ? View.VISIBLE : View.GONE);
                    isLoading = false;
                });
            }
        });
    }

}