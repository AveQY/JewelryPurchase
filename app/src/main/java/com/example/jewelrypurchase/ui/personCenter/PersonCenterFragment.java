package com.example.jewelrypurchase.ui.personCenter;

import android.app.AlertDialog;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jewelrypurchase.R;
import com.example.jewelrypurchase.adapter.GridViewAdapter;
import com.example.jewelrypurchase.databinding.FragmentNotificationsBinding;
import com.example.jewelrypurchase.jpWeb.Product;
import com.example.jewelrypurchase.jpWeb.WebUrl;
import com.example.jewelrypurchase.jpWeb.util.PageResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.noties.markwon.Markwon;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * 个人中心界面
 */
public class PersonCenterFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private TextView center_username;
    private TextView Personal_Center_Login_status;
    private LinearLayout outLoginBtu;
    private LinearLayout Shipping_address;
    private LinearLayout aboutAppMessage;
    private CardView personCenterWalletP;

    private OkHttpClient okHttpClient;
    private Handler handler = new Handler(Looper.getMainLooper());
    private int currentPage = 0;
    private boolean isLoading = false;
    private boolean hasMore = true;
    private List<Product> goodsdata = new ArrayList<>();
    private GridViewAdapter goods_adapter;
    private RecyclerView goods_recyclerView;
    private LinearLayout orderManage;

    private String userID;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        aboutAppMessage = root.findViewById(R.id.aboutAppMessage);
        center_username = root.findViewById(R.id.Personal_Center_Username);
        Personal_Center_Login_status = root.findViewById(R.id.Personal_Center_Login_status);
        outLoginBtu = root.findViewById(R.id.outLoginBtu);
        orderManage = root.findViewById(R.id.order_manage);
        Shipping_address = root.findViewById(R.id.Shipping_address);
        personCenterWalletP = root.findViewById(R.id.personCenterWalletP);
        goods_recyclerView = root.findViewById(R.id.goods_recyclerView);

        // 获取 SharedPreferences 对象
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);

        Random random = new Random();
        String token = sharedPreferences.getString("token", "");
        String username = sharedPreferences.getString("username", "");
        userID = sharedPreferences.getString("userId", "");

        if (token != "") {
            center_username.setText(username);
            Personal_Center_Login_status.setText(RandomQuotes(random.nextInt(20)));
        } else {
            outLoginBtu.setVisibility(View.GONE);
            Shipping_address.setVisibility(View.GONE);
        }
        // 钱包功能（已弃方案）
        personCenterWalletP.setVisibility(View.GONE);

        /**
         * 订单管理
         */
        orderManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireActivity(), OrderManage.class);
                startActivity(intent);
            }
        });

        /**
         * 地址管理
         */
        Shipping_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireActivity(), AddressManage.class);
                startActivity(intent);
            }
        });

        /**
         * 关于软件
         */
        aboutAppMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aboutApp();
            }
        });

        /**
         * 退出登录
         */
        outLoginBtu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                outLogin();
            }
        });

        goods_adapter = new GridViewAdapter(requireContext(), goodsdata); // 使用Activity上下文
        goods_recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2)); // 确保布局管理器存在
        goods_recyclerView.setAdapter(goods_adapter);
        loadGoods();

        goods_recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void loadGoods() {
        if (isLoading || !hasMore) return;
        isLoading = true;

        // 添加分页参数
        String Url = new WebUrl().getBASE_URL() + "/api/recommend/personal/" + userID + "?page=" + currentPage;
        Request request = new Request.Builder().url(Url).build();

        // 使用 enqueue 异步处理
        okHttpClient = new OkHttpClient.Builder().build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                handler.post(() -> {
                    Log.e("PersonCeneter", "加载失败: " + e.getMessage());
                    isLoading = false;
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    handler.post(() -> {
                        Log.e("PersonCeneter", "响应异常: HTTP " + response.code());
                        isLoading = false;
                    });
                    return;
                }

                String res = response.body().string();
//                Log.d("PersonCeneter", "响应数据: " + res);

                Type type = new TypeToken<PageResponse<Product>>() {
                }.getType();
                PageResponse<Product> pageResponse = new Gson().fromJson(res, type);
                List<Product> newItems = pageResponse.getContent();

                handler.post(() -> {
                    if (newItems != null && !newItems.isEmpty()) {
                        int startPosition = goodsdata.size();
                        goodsdata.addAll(newItems);

                        if (startPosition == 0) {
                            goods_adapter.notifyDataSetChanged();
                        } else {
                            goods_adapter.notifyItemRangeInserted(startPosition, newItems.size());
                        }

                        currentPage++;
                        hasMore = !pageResponse.isLast();
                    } else {
                        hasMore = false; // 无更多数据
                    }

                    isLoading = false;
                });
            }
        });

    }

    // 关于软件
    private void aboutApp() {
        // 创建一个AlertDialog.Builder对象
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        // 设置对话框的标题
//        builder.setTitle("关于软件");
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_privacy_policy, null);
        TextView privacyPolicyTextView = dialogView.findViewById(R.id.privacy_policy_text);
        // 初始化Markwon
        Markwon markwon = Markwon.builder(getContext()).build();
        // 解析并设置到TextView
        markwon.setMarkdown(privacyPolicyTextView, returnAppMessage());
        builder.setView(dialogView);

        // 设置对话框的按钮
        builder.setPositiveButton("知道了", (dialog, which) -> {
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // 退出登录
    private void outLogin() {
        // 创建一个AlertDialog.Builder对象
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        // 设置对话框的标题
        builder.setTitle("退出登录");
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_privacy_policy, null);
        TextView privacyPolicyTextView = dialogView.findViewById(R.id.privacy_policy_text);
        privacyPolicyTextView.setText("确定退出登录？");
        builder.setView(dialogView);

        // 设置对话框的按钮
        builder.setPositiveButton("确定", (dialog, which) -> {
            // 删除保存的token
            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            // 登录界面
            Intent intent = new Intent(requireActivity(), LoginActivity.class);
            requireActivity().finish();
            startActivity(intent);
        });
        builder.setNegativeButton("取消", (dialog, which) -> {

        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public String RandomQuotes(int num) {
        List<String> quotes = List.of(
                "你的收藏里还缺一件能与之对话的珠宝，试试这个？",
                "珠宝是女人最亲密的朋友，永远不会背叛你的美丽。",
                "一颗钻石恒久远，但你的风格可以千变万化。",
                "真正的奢华，不在于价格，而在于它如何让你闪耀。",
                "珠宝不是装饰，而是你气质的延伸。",
                "有些光芒，只有珠宝才能赋予。",
                "一件珠宝是优雅，两件是风格，三件才是你的个性宣言。",
                "最美的搭配，不是单件的惊艳，而是整体的和谐。",
                "你的项链在等待它的耳环伴侣，让它们相遇吧。",
                "珠宝就像爱情，找到对的组合，才能绽放最美的光芒。",
                "为什么只选一件？真正的魅力，藏在层叠搭配的艺术里。",
                "你已经拥有了一件经典，现在是时候为它寻找完美搭档了。",
                "珠宝的魔力在于组合——让它们一起讲述你的故事。",
                "单独佩戴是优雅，叠戴才是态度。",
                "真正的奢侈，是独一无二的你，配上独一无二的它。",
                "不是所有珠宝都值得拥有，但这一件，注定属于你。",
                "经典的设计，只为配得上你的品味。",
                "珠宝是沉默的情书，让TA每次佩戴都想起你。",
                "纪念日的礼物，应该像你们的爱情一样永恒。",
                "有些心意，只有珠宝才能表达。"
        );
        return quotes.get(num);
    }

    private String returnAppMessage() {
        return "# **珠宝购使用说明**\n" +
                "\n" +
                "**版本号**：v1.0.5  \n" +
                "**最后更新日期**：2025年4月30日\n" +
                "\n" +
                "## **1. 软件简介**\n" +
                "\n" +
                "- **功能概述**：二手珠宝自由交易市场。\n" +
                "  \n" +
                "- **适用场景**：对珠宝感兴趣，有购买/出售二手珠宝的想法。\n" +
                "  \n" +
                "\n" +
                "## **2. 快速入门**\n" +
                "\n" +
                "### **2.1 主界面介绍**\n" +
                "\n" +
                "- 首页展示各大卖家的闲置二手珠宝，可自由交易。\n" +
                "\n" +
                "### **2.2 基础操作**\n" +
                "\n" +
                "- **示例：发布闲置珠宝**\n" +
                "  \n" +
                "  1. 点击 `库存` > `右下角 + `。\n" +
                "    \n" +
                "  2. 填写相关信息，点击 `发布`。\n" +
                "    \n" +
                "  3. 发布成功后，商品会显示在首页中。\n" +
                "    \n" +
                "- **示例：购买闲置珠宝**\n" +
                "  \n" +
                "  1. 选择你想要的珠宝。\n" +
                "    \n" +
                "  2. 点击 `立即购买` 按钮（首次购买需要填写收货信息）。\n" +
                "    \n" +
                "  3. 等待订单生成后 > `支付`\n" +
                "    \n" +
                "  4. `卖家发货` > `确认货品无误，收货` > `订单完成`\n" +
                "    \n" +
                "\n" +
                "## **3. 功能详解**\n" +
                "\n" +
                "### **3.1 客服**\n" +
                "\n" +
                "- **用途**：帮助用户了解珠宝购买流程以及相关珠宝知识。\n" +
                "\n" +
                "## **4. 故障排除**\n" +
                "\n" +
                "- **问题1**：无法连接服务器。\n" +
                "  \n" +
                "  - 检查网络是否正常。\n" +
                "    \n" +
                "  - 重新启动软件。\n" +
                "    \n" +
                "- **问题2**：软件运行卡顿。\n" +
                "  \n" +
                "  - 关闭其他占用内存的程序。\n" +
                "\n" +
                "## **5. 技术支持与反馈**\n" +
                "\n" +
                "- **反馈邮箱**：2049898109@qq.com\n" +
                "  \n" +
                "- **提交 Bug**：在 邮箱 中描述问题并附上日志文件。\n" +
                "  \n" +
                "\n" +
                "## **版本历史**\n" +
                "\n" +
                "**v1.0.5 2025-04-30**\n" +
                "\n" +
                "- 重构登录界面，修复一些bug\n" +
                "\n" +
                "**v1.0.4 2025-04-29**\n" +
                "\n" +
                "- 优化界面布局，修复控件冲突问题\n" +
                "\n" +
                "**v1.0.3 2025-04-26**\n" +
                "\n" +
                "- 新增商品浏览量\n" +
                "\n" +
                "**v1.0.2 2025-04-24**\n" +
                "\n" +
                "- 修复登录一系列bug\n" +
                "  \n" +
                "- 增加个人中心一言，优化订单界面\n" +
                "  \n" +
                "- 个人中心商品个性化推送\n" +
                "  \n" +
                "\n" +
                "**v1.0.1 2025-04-23**\n" +
                "\n" +
                "- 修复购物车、订单删除操作显示问题\n" +
                "  \n" +
                "- 修复并发超卖问题\n" +
                "  \n" +
                "\n" +
                "**v1.0.0 2025-04-20**\n" +
                "\n" +
                "- 初始版本发布\n" +
                "\n" +
                "**版权声明**  \n" +
                "© 2025 珠宝购 保留所有权利。";
    }

}