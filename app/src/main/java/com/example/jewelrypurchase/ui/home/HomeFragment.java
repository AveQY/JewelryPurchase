package com.example.jewelrypurchase.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.example.jewelrypurchase.R;
import com.example.jewelrypurchase.adapter.GridViewAdapter;
import com.example.jewelrypurchase.adapter.HomeCarouselAdapter;
import com.example.jewelrypurchase.databinding.FragmentHomeBinding;
import com.example.jewelrypurchase.jpWeb.Carousel;
import com.example.jewelrypurchase.jpWeb.Product;
import com.example.jewelrypurchase.jpWeb.WebUrl;
import com.example.jewelrypurchase.jpWeb.util.PageResponse;
import com.example.jewelrypurchase.models.ScrollViewHideHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;

    private ViewPager2 viewPager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Product> goodsdata = new ArrayList<>();
    private RecyclerView homeGoodsView;
    private GridViewAdapter homeGoods_adapter;
    private NestedScrollView nestedScrollView;
    private int currentPage = 0;
    private boolean isLoading = false;
    private boolean hasMore = true;

    private OkHttpClient okHttpClient;
    private Handler handler = new Handler(Looper.getMainLooper());
    private CardView homeSearchItme;
    private TextView homeGoodsIsNull;
    private LinearLayout homeTopItem;
    private LinearLayout homeTopSearchItme;
    private ScrollViewHideHelper scrollHideHelper;

    // 在HomeFragment类中添加以下成员变量
    private Runnable carouselRunnable;
    private final long CAROUSEL_DELAY = 3000; // 3秒切换间隔


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        /**
         * 轮播图
         */
        viewPager = root.findViewById(R.id.viewPager);

        /**
         * 商品列表（网格）
         */
        swipeRefreshLayout = root.findViewById(R.id.homeRefresh);
        homeGoodsView = root.findViewById(R.id.home_goods_recyclerView);
        homeSearchItme = root.findViewById(R.id.homeSearchItme);
        homeGoodsIsNull = root.findViewById(R.id.homeGoodsIsNull);
        nestedScrollView = root.findViewById(R.id.nestedScrollView);
        homeTopItem = root.findViewById(R.id.homeTopItem);
        homeTopSearchItme = root.findViewById(R.id.homeTopSearchItme);

        // 搜索
        homeSearchItme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 搜索界面
                Intent intent = new Intent(requireActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // 获取 NestedScrollView 的子视图（通常是内部的 LinearLayout/RecyclerView 等）
                View child = v.getChildAt(0);
                if (child == null) return;

                // 判断是否滚动到底部
                if (scrollY >= (child.getHeight() - v.getHeight())) {
                    // 触发加载更多逻辑
                    loadGoods();
                }
            }
        });

        // 处理用户交互：滑动时重置定时器
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // 用户滑动后重置定时器
                handler.removeCallbacks(carouselRunnable);
                handler.postDelayed(carouselRunnable, CAROUSEL_DELAY);
            }
        });

        refreshData();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopAutoCarousel(); // 停止定时任务
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 轮播图
     */
    // 修改loadviewPager方法，添加自动轮播逻辑
    private void loadviewPager() {
        List<String> images = new ArrayList<>(Arrays.asList());
        okHttpClient = new OkHttpClient.Builder().build();
        String carouselUrl = new WebUrl().getBASE_URL() + "/api/carousel";
        Request RcarouselUrl = new Request.Builder()
                .url(carouselUrl)
                .build();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = okHttpClient.newCall(RcarouselUrl).execute();
                    String res = response.body().string();

                    Type listType = new TypeToken<List<Carousel>>() {}.getType();
                    List<Carousel> imageItemList = new Gson().fromJson(res, listType);

                    for (Carousel carousel : imageItemList) {
                        images.add(carousel.getImageUrl());
                    }

                    handler.post(() -> {
                        // 设置适配器
                        viewPager.setAdapter(new HomeCarouselAdapter(images));

                        // 只有多于1张图时启动自动轮播
                        if (images.size() > 1) {
                            startAutoCarousel();
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // 添加自动轮播控制方法
    private void startAutoCarousel() {
        // 移除之前的任务
        stopAutoCarousel();

        carouselRunnable = new Runnable() {
            @Override
            public void run() {
                int currentItem = viewPager.getCurrentItem();
                int totalItems = viewPager.getAdapter().getItemCount();
                viewPager.setCurrentItem((currentItem + 1) % totalItems, true);
                handler.postDelayed(this, CAROUSEL_DELAY);
            }
        };
        handler.postDelayed(carouselRunnable, CAROUSEL_DELAY);
    }

    private void stopAutoCarousel() {
        if (carouselRunnable != null) {
            handler.removeCallbacks(carouselRunnable);
        }
    }

    /**
     * 加载推荐商品
     */
    private void loadGoods() {
        if (isLoading || !hasMore) return;
        isLoading = true;

        // 添加分页参数
        String Url = new WebUrl().getBASE_URL() + "/api/recommend?page=" + currentPage + "&size=8";
        Request request = new Request.Builder().url(Url).build();

        okHttpClient = new OkHttpClient.Builder().build();
        // 使用 enqueue 异步处理
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                handler.post(() -> {
                    Log.e("HomeFragment", "加载失败: " + e.getMessage());
                    isLoading = false;
                    swipeRefreshLayout.setRefreshing(false);
                    showPrivacyPolicyDialog("错误", "加载失败，请重试！", "确定", "");
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    handler.post(() -> {
                        Log.e("HomeFragment", "响应异常: HTTP " + response.code());
                        isLoading = false;
                        swipeRefreshLayout.setRefreshing(false);
                    });
                    return;
                }

                String res = response.body().string();
//                Log.d("HomeFragment", "响应数据: " + res);

                Type type = new TypeToken<PageResponse<Product>>() {
                }.getType();
                PageResponse<Product> pageResponse = new Gson().fromJson(res, type);
                List<Product> newItems = pageResponse.getContent();

                handler.post(() -> {
                    if (newItems != null && !newItems.isEmpty()) {
                        // 第一页直接替换，后续追加
                        if (currentPage == 0) {
                            goodsdata.clear();
                            goodsdata.addAll(newItems);
                            if (homeGoods_adapter == null) {
                                homeGoods_adapter = new GridViewAdapter(getActivity(), goodsdata);
                                homeGoodsView.setAdapter(homeGoods_adapter);
                            } else {
                                homeGoods_adapter.notifyDataSetChanged();
                            }
                        } else {
                            int startPos = goodsdata.size();
                            goodsdata.addAll(newItems);
                            homeGoods_adapter.notifyItemRangeInserted(startPos, newItems.size());
                        }
                        currentPage++;
                        hasMore = !pageResponse.isLast();
                    } else {
                        hasMore = false; // 无更多数据
                    }

                    // 更新空数据提示
                    homeGoodsIsNull.setVisibility(goodsdata.isEmpty() ? View.VISIBLE : View.GONE);
                    isLoading = false;
                    swipeRefreshLayout.setRefreshing(false);
                });
            }
        });
    }

    /**
     * 刷新
     */
    private void refreshData() {
        // 重置分页参数
        currentPage = 0;
        hasMore = true;
        isLoading = false;

        // 清空旧数据
        goodsdata.clear();
        if (homeGoods_adapter != null) {
            homeGoods_adapter.notifyDataSetChanged();
        }

        // 初始化UI组件（防止视图残留）
        homeGoodsView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        homeGoodsView.setAdapter(homeGoods_adapter);

        // 设置SwipeRefreshLayout的刷新监听器
        swipeRefreshLayout.setOnRefreshListener(this::refreshData);
        swipeRefreshLayout.setColorSchemeResources(R.color.purple_500);
        swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(true));

        // 3. 取消之前的网络请求
        if (okHttpClient != null) {
            okHttpClient.dispatcher().cancelAll();
        }
        okHttpClient = new OkHttpClient.Builder().build();

        // 延迟加载避免主线程阻塞
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (isNetworkConnected(requireContext())) {
                loadGoods();      // 加载商品
                loadviewPager();  // 加载轮播图
            } else {
                showPrivacyPolicyDialog("提示", "请检查网络连接！", "确定", "");
            }
            swipeRefreshLayout.setRefreshing(false);
        }, 100);
    }

    // 是否有网络连接
    public boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    // 对话框
    public void showPrivacyPolicyDialog(String d_title, String d_mytext, String btu1, String btu2) {
        // 创建一个AlertDialog.Builder对象
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        // 设置对话框的标题
        builder.setTitle(d_title);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_privacy_policy, null);
        TextView privacyPolicyTextView = dialogView.findViewById(R.id.privacy_policy_text);
        privacyPolicyTextView.setText(d_mytext);
        builder.setView(dialogView);
        // 设置点击阴影对话框不可取消
        builder.setCancelable(false);

        // 设置对话框的按钮
        builder.setPositiveButton(btu1, (dialog, which) -> {
            System.exit(0);
        });
        builder.setNegativeButton(btu2, (dialog, which) -> {

        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}