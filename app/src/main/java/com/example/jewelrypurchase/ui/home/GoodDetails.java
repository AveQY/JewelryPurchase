package com.example.jewelrypurchase.ui.home;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.jewelrypurchase.R;
import com.example.jewelrypurchase.adapter.GoodsImgAdaper;
import com.example.jewelrypurchase.jpWeb.Product;
import com.example.jewelrypurchase.jpWeb.ProductCategory;
import com.example.jewelrypurchase.jpWeb.ProductImg;
import com.example.jewelrypurchase.jpWeb.WebUrl;
import com.example.jewelrypurchase.models.DashboardBottomDialog;
import com.example.jewelrypurchase.ui.dashboard.OrderDetails;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GoodDetails extends AppCompatActivity {

    private TextView rGoodsName;
    private TextView rGoodsPrice;
    private TextView rGoodsQuantity;
    private TextView rGoodsIntroduce;
    private ViewPager2 rGoodsMessageImgPager;
    private OkHttpClient okHttpClient;
    private String goodsID;
    private String categoryId;
    private String goodsImgUrl;
    private String token;

    private Handler handler = new Handler(Looper.getMainLooper());
    private TextView goodsCategory;
    private CardView addToDashboard;
    private String username;
    private ImageView chatMessageImg;
    private CardView buyThisGoods;
    private TextView productViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_message);

        // 隐藏标题栏
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        rGoodsName = findViewById(R.id.rGoodsName);
        rGoodsPrice = findViewById(R.id.rGoodsPrice);
        rGoodsQuantity = findViewById(R.id.rGoodsQuantity);
        rGoodsIntroduce = findViewById(R.id.rGoodsIntroduce);
        rGoodsMessageImgPager = findViewById(R.id.rGoodsMessageImgPager);
        goodsCategory = findViewById(R.id.goodsCategory);
        chatMessageImg = findViewById(R.id.chatMessageImg);
        addToDashboard = findViewById(R.id.addToDashboard);
        buyThisGoods = findViewById(R.id.buyThisGoods);
        productViews = findViewById(R.id.productViews);
        CardView goodsDetailBack = findViewById(R.id.goodsDetailBack);

        goodsDetailBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 获取 SharedPreferences 对象
        SharedPreferences sharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        username = sharedPreferences.getString("username", "");

        // 获取传递过来的Intent
        Intent intent = getIntent();
        goodsID = intent.getStringExtra("goodsID");

        // 显示传递过来的参数
        okHttpClient = new OkHttpClient.Builder().build();
        String rGoodsUrl = new WebUrl().getBASE_URL() + "/api/search/product?productId=" + goodsID;
        Request goodsRcarouselUrl = new Request.Builder()
                .url(rGoodsUrl)
                .build();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = okHttpClient.newCall(goodsRcarouselUrl).execute();
                    String res = response.body().string();

                    Log.e("GoodDetails", res);

                    Gson gson = new Gson();
                    Type listType = new TypeToken<Product>() {
                    }.getType();
                    Product product = gson.fromJson(res, listType);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            rGoodsName.setText(product.getName());
                            rGoodsPrice.setText(product.getPrice());
                            rGoodsQuantity.setText(product.getStock());
                            rGoodsIntroduce.setText(product.getDescription());
                            categoryId = product.getCategoryId();
                            goodsImgUrl = product.getImageUrl();
                            productViews.setText(String.valueOf(product.getClickStats().getTotalClicks()));

                            // 加载商品分类
                            loadCategory();
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // 加载商品图片
        loadviewPager();

        // 联系
        chatMessageImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myShowDialog("是否联系？", "即将将您的联系电话发送给");
            }
        });

        /**
         * 添加商品至购物车
         */
        addToDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 显示对话框
                DashboardBottomDialog dialog = DashboardBottomDialog.newInstance(goodsImgUrl, rGoodsPrice.getText().toString(), rGoodsQuantity.getText().toString(), false);
                dialog.show(getSupportFragmentManager(), "GoodsDialog");
                dialog.setOnConfirmListener(quantity -> {
                    addGoodsToDashboard(String.valueOf(quantity));
                });

            }
        });

        /**
         * 购买商品
         */
        buyThisGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 显示对话框
                DashboardBottomDialog dialog = DashboardBottomDialog.newInstance(goodsImgUrl, rGoodsPrice.getText().toString(), rGoodsQuantity.getText().toString(), true);
                dialog.show(getSupportFragmentManager(), "GoodsDialog");
                dialog.setOnConfirmListener(quantity -> {
                    Intent intent = new Intent(GoodDetails.this, OrderDetails.class);
                    intent.putExtra("Token", token);
                    intent.putExtra("username", username);
                    intent.putExtra("goodsID", goodsID);
                    intent.putExtra("purchaseQuantity", String.valueOf(quantity));
                    intent.putExtra("Status", "CREATE");
                    startActivity(intent);
                });

            }
        });

    }

    private void loadCategory() {
        okHttpClient = new OkHttpClient.Builder().build();
        String inventoryUrl3 = new WebUrl().getBASE_URL() + "/api/search/productCategory?categoryId=" + categoryId;
        Request RcarouselUrl3 = new Request.Builder()
                .url(inventoryUrl3)
                .build();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = okHttpClient.newCall(RcarouselUrl3).execute();
                    String res = response.body().string();

                    Type listType3 = new TypeToken<ProductCategory>() {
                    }.getType();
                    ProductCategory result = new Gson().fromJson(res, listType3);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            goodsCategory.setText(result.getCategoriesName());
                        }
                    });

                } catch (IOException e) {
                    Toast.makeText(GoodDetails.this, "服务器错误！", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void loadviewPager() {
        List<String> images = new ArrayList<>(Arrays.asList());

        okHttpClient = new OkHttpClient.Builder().build();
        String rGoodsImgUrl = new WebUrl().getBASE_URL() + "/api/search/productImg?id=" + goodsID;
        Request RcarouselUrl = new Request.Builder()
                .url(rGoodsImgUrl)
                .build();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = okHttpClient.newCall(RcarouselUrl).execute();
                    String res = response.body().string();

                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<ProductImg>>() {
                    }.getType();
                    List<ProductImg> imageItemList = gson.fromJson(res, listType);

                    for (ProductImg productImg : imageItemList) {
                        images.add(productImg.getImageUrl());
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            rGoodsMessageImgPager.setAdapter(new GoodsImgAdaper(images));
                        }
                    });

                } catch (IOException e) {
                    Toast.makeText(GoodDetails.this, "服务器错误！", Toast.LENGTH_SHORT).show();
                }
            }
        }).start();

    }

    private void addGoodsToDashboard(String goodsQuantity) {
        okHttpClient = new OkHttpClient.Builder().build();
        String Url = new WebUrl().getBASE_URL() + "/api/add/dashboard";
        FormBody formBody = new FormBody.Builder().add("goodsNum", goodsQuantity)
                .add("productId", goodsID)
                .add("buyer", username)
                .build();
        Request RcarouselUrl = new Request.Builder()
                .url(Url)
                .post(formBody)
                .build();

        new Thread(() -> {
            try {
                Response response = okHttpClient.newCall(RcarouselUrl).execute();
                handler.post(() -> {
                    if (response.isSuccessful()) {
                        Toast.makeText(GoodDetails.this, "商品已添加到购物车", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(GoodDetails.this, "添加失败!", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (IOException e) {
                handler.post(() ->
                        Toast.makeText(GoodDetails.this, "网络错误，请重试", Toast.LENGTH_SHORT).show()
                );
                e.printStackTrace();
            }
        }).start();
    }

    // 联系
    public void myShowDialog(String title, String content) {
        // 创建一个AlertDialog.Builder对象
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 设置对话框的标题
        builder.setTitle(title);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_privacy_policy, null);
        TextView privacyPolicyTextView = dialogView.findViewById(R.id.privacy_policy_text);
        privacyPolicyTextView.setText(content);
        builder.setView(dialogView);

        // 设置对话框的按钮
        builder.setPositiveButton("确定", (dialog, which) -> {
            Toast.makeText(this, "等待卖家确认", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("取消", (dialog, which) -> {
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}