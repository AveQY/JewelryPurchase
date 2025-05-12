package com.example.jewelrypurchase.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jewelrypurchase.R;
import com.example.jewelrypurchase.adapter.GridViewAdapter;
import com.example.jewelrypurchase.jpWeb.Product;
import com.example.jewelrypurchase.jpWeb.WebUrl;
import com.example.jewelrypurchase.jpWeb.util.PageResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchResultActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private OkHttpClient okHttpClient;
    private Handler handler = new Handler(Looper.getMainLooper());
    private String query;
    private List<Product> searchGoodsdata = new ArrayList<>();
    private int currentPage = 0;
    private boolean isLoading = false;
    private boolean hasMore = true;
    private GridViewAdapter adapter;

    private LinearLayout search_top_Linear;
    private TextView searchResultIsNull;
    private TextView searchResultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result_page);

        search_top_Linear = findViewById(R.id.search_top_Linear);
        searchResultIsNull = findViewById(R.id.searchResultIsNull);
        searchResultText = findViewById(R.id.searchResultText);
        recyclerView = findViewById(R.id.searchResultsRecyclerView);

        // 隐藏标题栏
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // 点击返回
        search_top_Linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(0, R.anim.fade_out);
            }
        });

        // 获取传递过来的Intent
        Intent intent = getIntent();
        query = intent.getStringExtra("query");

        searchResultText.setText(query);

        loadNextPage();

        recyclerView.setLayoutManager(new GridLayoutManager(getBaseContext(), 2));
        adapter = new GridViewAdapter(this, searchGoodsdata);
        recyclerView.setAdapter(adapter);

        // 添加滚动监听器
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager == null) return;

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();

                if (!isLoading && hasMore && (lastVisiblePosition >= totalItemCount - 1)) {
//                    Log.d("ScrollDebug", "触发加载下一页，当前页码: " + currentPage);
                    loadNextPage();
                }
            }
        });

    }

    private void loadNextPage() {
        if (isLoading || !hasMore) return;

        isLoading = true;
        okHttpClient = new OkHttpClient.Builder().build();
        String url = new WebUrl().getBASE_URL() + "/api/search/productName?keyword=" + query
                + "&page=" + currentPage + "&size=8"; // 根据API调整size
        Request request = new Request.Builder().url(url).build();

        new Thread(() -> {
            try (Response response = okHttpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) return;

                String res = response.body().string();
                Type type = new TypeToken<PageResponse<Product>>() {
                }.getType();
                PageResponse<Product> pageResponse = new Gson().fromJson(res, type);
                List<Product> newProducts = pageResponse.getContent();

                hasMore = pageResponse.hasMore();

                handler.post(() -> {

                    if (newProducts != null && !newProducts.isEmpty()) {
                        int startPosition = searchGoodsdata.size();
                        searchGoodsdata.addAll(newProducts);

                        if (searchGoodsdata.isEmpty()) {
                            searchResultIsNull.setVisibility(View.VISIBLE);
                        }
                        if (startPosition == 0) {
                            adapter.notifyDataSetChanged();
                        } else {
                            adapter.notifyItemRangeInserted(startPosition, newProducts.size());
                        }

                        currentPage++;
                        hasMore = !pageResponse.isLast();
                    } else {
                        hasMore = false; // 无更多数据
                    }

                    isLoading = false;
                });

            } catch (IOException e) {
                e.printStackTrace();
                handler.post(() -> isLoading = false);
            }
        }).start();
    }

}
