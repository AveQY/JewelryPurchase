package com.example.jewelrypurchase.ui.dashboard;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.jewelrypurchase.R;
import com.example.jewelrypurchase.adapter.AddressAdapter;
import com.example.jewelrypurchase.adapter.PayMethodAdaper;
import com.example.jewelrypurchase.jpWeb.Address;
import com.example.jewelrypurchase.jpWeb.Orders;
import com.example.jewelrypurchase.jpWeb.PayMethod;
import com.example.jewelrypurchase.jpWeb.Product;
import com.example.jewelrypurchase.jpWeb.Result;
import com.example.jewelrypurchase.jpWeb.User;
import com.example.jewelrypurchase.jpWeb.WebUrl;
import com.example.jewelrypurchase.ui.personCenter.AddressManage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OrderDetails extends AppCompatActivity {

    private ImageView orderDetailsBack;
    private TextView orderNowStatus;
    private TextView orderAddress;
    private CardView orderModifyAddress;
    private ImageView orderGoodsImg;
    private TextView orderGoodsName;
    private TextView orderPersonalAddress;
    private TextView orderGoodsPrice;
    private TextView orderGoodsNumber;
    private TextView orderIDText;
    private TextView orderCrateTime;
    private LinearLayout orderBottomBtu;
    private TextView orderAllMoney;
    private CardView orderOverGOPay;
    private RecyclerView paymentMethodRecycler;
    private LinearLayout orderDetailsBottomItem;
    private PayMethodAdaper adapter;

    private String orderId;
    private String username;
    private String Token;
    private String STATUS = "CREATE";
    private String goodsID;
    private String purchaseQuantity = "1";
    private int userId;
    private int addressId;
    private String totalAmount = "0.00";

    private OkHttpClient okHttpClient;
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        orderDetailsBack = findViewById(R.id.orderDetailsBack);
        orderNowStatus = findViewById(R.id.orderNowStatus);
        orderAddress = findViewById(R.id.orderPersonalAddress);
        orderModifyAddress = findViewById(R.id.orderModifyAddress);
        orderPersonalAddress = findViewById(R.id.orderPersonalAddress);
        orderGoodsImg = findViewById(R.id.orderGoodsImg);
        orderGoodsName = findViewById(R.id.orderGoodsName);
        orderGoodsPrice = findViewById(R.id.orderGoodsPrice);
        orderGoodsNumber = findViewById(R.id.orderGoodsNumber);
        orderIDText = findViewById(R.id.orderIDText);
        orderCrateTime = findViewById(R.id.orderCrateTime);
        orderBottomBtu = findViewById(R.id.orderBottomBtu);
        orderAllMoney = findViewById(R.id.orderAllMoney);
        orderOverGOPay = findViewById(R.id.orderOverGOPay);
        paymentMethodRecycler = findViewById(R.id.paymentMethodRecycler);
        orderDetailsBottomItem = findViewById(R.id.orderDetailsBottomItem);

        // 隐藏标题栏
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // 返回
        orderDetailsBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 加载支付方法
        List<PayMethod> payMethods = new ArrayList<>();
        payMethods.add(new PayMethod(1, R.drawable.wechat, "微信支付"));
        payMethods.add(new PayMethod(2, R.drawable.alipay, "支付宝"));
        adapter = new PayMethodAdaper(getBaseContext(), payMethods);
        paymentMethodRecycler.setLayoutManager(new GridLayoutManager(getBaseContext(), 1));
        paymentMethodRecycler.setAdapter(adapter);

        // 获取传递过来的Intent
        Intent intent = getIntent();
        String status = intent.getStringExtra("Status");
        SharedPreferences sharedPreferences = this.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        Token = sharedPreferences.getString("token", "");

        /**
         * 是否需要创建订单
         */
        if (status.equals(STATUS)) {
            username = intent.getStringExtra("username");
            goodsID = getIntent().getStringExtra("goodsID");
            purchaseQuantity = getIntent().getStringExtra("purchaseQuantity");
            // 确保userId已赋值后，再执行后续逻辑
            loadUserId(() -> {
                loadAddressId(new OnAddressIdLoadedListener() {
                    @Override
                    public void onAddressIdLoaded() {
                        loadGoodsMessage(true);
                    }
                });
            });
        } else {
            orderId = intent.getStringExtra("ORDERID");
            Log.e("OrderDetails", "id: " + orderId);
            String resId = intent.getStringExtra("orderImage");
            String orderPrice = intent.getStringExtra("orderPrice");
            Glide.with(this)
                    .load(resId)
                    .centerCrop()
                    .error(R.drawable.error_picture)
                    .into(orderGoodsImg);
            orderGoodsPrice.setText(orderPrice);
            loadOrderMessage();
        }

        /**
         * 修改地址
         */
        orderModifyAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reworkAddress();
            }
        });

        /**
         * 支付：暂未接入sdk
         */
        orderOverGOPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payOrder();
            }
        });

        /**
         * 取消订单
         */
        orderBottomBtu.setOnClickListener(view -> {
            deleteOrder();
        });
    }

    /**
     * 创建订单
     */
    private void createOrder() {
        String Url = new WebUrl().getBASE_URL() + "/api/add/order";
        FormBody formBody = new FormBody.Builder().add("productId", goodsID)
                .add("purchaseQuantity", purchaseQuantity)
                .add("userId", String.valueOf(userId))
                .add("addressId", String.valueOf(addressId))
                .add("totalAmount", totalAmount)
                .add("paymentMethodCode", "1")
                .build();
        Request request = new Request.Builder()
                .url(Url)
                .post(formBody)
                .build();
        okHttpClient = new OkHttpClient.Builder().build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                handler.post(() -> {
                    Log.e("Order", "fail: " + e.getMessage());
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e("Order", "fail!");
                    return;
                }

                String res = response.body().string();

                Log.e("ordersDetail", "res: " + res);

                Type type = new TypeToken<Result<Orders>>() {
                }.getType();
                Result<Orders> pageResponse = new Gson().fromJson(res, type);
                Orders order = pageResponse.getData();

                handler.post(() -> {
                    if (!pageResponse.getCode().equals("200")) {
                        Toast.makeText(OrderDetails.this, "创建订单失败！", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        orderNowStatus.setText(order.getStatus());
                        orderIDText.setText(String.valueOf(order.getOrderId()) == null ? "" : String.valueOf(order.getOrderId()));
                        orderCrateTime.setText(String.valueOf(order.getCreateTime()));
                        orderId = String.valueOf(order.getOrderId());
                    }
                    orderModifyAddress.setVisibility(View.VISIBLE);
                });
            }
        });
    }

    /**
     * 加载订单
     */
    private void loadOrderMessage() {
        String Url = new WebUrl().getBASE_URL() + "/api/order?orderId=" + orderId;
        Log.e("OrderDetails", "Url: " + Url);
        Request request = new Request.Builder().url(Url).build();
        okHttpClient = new OkHttpClient.Builder().build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                handler.post(() -> {
                    Log.e("OrderDetails", "fail: " + e.getMessage());
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String res = response.body().string();

                Log.e("ordersDetail", "res: " + res);

                Type type = new TypeToken<Result<Orders>>() {
                }.getType();
                Result<Orders> pageResponse = new Gson().fromJson(res, type);
                Orders order = pageResponse.getData();

                handler.post(() -> {
                    if (!pageResponse.getCode().equals("200")) {
                        Toast.makeText(OrderDetails.this, "获取订单失败！", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        goodsID = String.valueOf(order.getProductId());
                        loadGoodsMessage(false);
                        addressId = order.getAddressId();
                        loadOrderAddressId();
                        orderNowStatus.setText(order.getStatus());
                        orderGoodsNumber.setText("×" + order.getPurchaseQuantity());
                        orderAllMoney.setText(order.getTotalAmount().toString());
                        orderIDText.setText(String.valueOf(order.getOrderId()) == null ? "" : String.valueOf(order.getOrderId()));
                        orderCrateTime.setText(String.valueOf(order.getCreateTime()));
                        adapter.setSelectedPosition(order.getPaymentMethod().equals("微信支付") ? 0 : 1);
                        adapter.notifyDataSetChanged();
                    }
                    if (!order.getStatus().equals("待支付")) {
                        orderOverGOPay.setVisibility(View.GONE);
                        paymentMethodRecycler.setVisibility(View.GONE);
                    } else {
                        orderModifyAddress.setVisibility(View.VISIBLE);
                    }
                    if (order.getStatus().equals("已取消")) {
                        orderBottomBtu.setVisibility(View.GONE);
                        paymentMethodRecycler.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    /**
     * 修改地址
     */
    private void reworkAddress() {
        String Url = new WebUrl().getBASE_URL() + "/api/search/address?userToken=" + Token;

        Log.e("AddressToken", Url);

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

                Log.e("OrderAddress", res);

                Type type = new TypeToken<List<Address>>() {
                }.getType();
                List<Address> pageResponse = new Gson().fromJson(res, type);

                handler.post(() -> {
                    List<Address> items = new ArrayList<>();
                    items.addAll(pageResponse);
                    AddressAdapter adapter = new AddressAdapter(OrderDetails.this, items);

                    AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetails.this);
                    View dialogView = LayoutInflater.from(OrderDetails.this).inflate(R.layout.dialog_list, null);
                    builder.setView(dialogView);

                    RecyclerView recyclerView = dialogView.findViewById(R.id.recyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(OrderDetails.this));

                    AlertDialog dialog = builder.create();
                    dialog.show();

                    adapter.setOnItemClickListener(address -> {
                        okHttpClient = new OkHttpClient.Builder().build();
                        String Url = new WebUrl().getBASE_URL() + "/api/update/order";
                        FormBody formBody = new FormBody.Builder().add("orderId", orderId)
                                .add("statusCode", "1")
                                .add("addressId", String.valueOf(address.getAddressId()))
                                .build();
                        Request RcarouselUrl = new Request.Builder().url(Url).post(formBody).build();

                        new Thread(() -> {
                            try {
                                Response response1 = okHttpClient.newCall(RcarouselUrl).execute();
                                if (response1.isSuccessful()) {
                                    handler.post(() -> {
                                        orderPersonalAddress.setText(address.getUsername() + " " + address.getPhone() + " " + address.getAddressName());
                                        dialog.dismiss();
                                    });
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }).start();
                    });

                    recyclerView.setAdapter(adapter);

                });
            }
        });
    }

    /**
     * 支付
     */
    private void payOrder() {
        okHttpClient = new OkHttpClient.Builder().build();
        String Url = new WebUrl().getBASE_URL() + "/api/update/order";
        String Url2 = new WebUrl().getBASE_URL() + "/api/updateProductStock";
        FormBody formBody = new FormBody.Builder().add("orderId", orderId)
                .add("statusCode", "2")
                .add("paymentMethod", String.valueOf(adapter.getSelectedPayMethod()))
                .build();
        FormBody formBody2 = new FormBody.Builder().add("productId", goodsID)
                .add("reduceStock", purchaseQuantity)
                .build();
        Request RcarouselUrl = new Request.Builder().url(Url).post(formBody).build();
        Request RcarouselUrl2 = new Request.Builder().url(Url2).post(formBody2).build();

        new Thread(() -> {
            try {
                Response response = okHttpClient.newCall(RcarouselUrl).execute();
                Response response2 = okHttpClient.newCall(RcarouselUrl2).execute();

                String res = response2.body().string();

//                System.out.println(res);

                Type type = new TypeToken<Result<Orders>>() {
                }.getType();
                Result<Orders> result = new Gson().fromJson(res, type);

                if (response.isSuccessful() && response2.isSuccessful()) {
                    handler.post(() -> {
                        Toast.makeText(this, result.getToken(), Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }
            } catch (IOException e) {
                handler.post(() ->
                        Toast.makeText(this, "支付失败！", Toast.LENGTH_SHORT).show()
                );
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * 取消订单
     */
    private void deleteOrder() {
        okHttpClient = new OkHttpClient.Builder().build();
        String Url = new WebUrl().getBASE_URL() + "/api/update/order";
        String Url2 = new WebUrl().getBASE_URL() + "/api/updateProductStock";
        FormBody formBody = new FormBody.Builder().add("orderId", orderId)
                .add("statusCode", "5")
                .build();
        FormBody formBody2 = new FormBody.Builder().add("productId", goodsID)
                .add("reduceStock", "-" + purchaseQuantity)
                .build();
        Request RcarouselUrl = new Request.Builder().url(Url).post(formBody).build();
        Request RcarouselUrl2 = new Request.Builder().url(Url2).post(formBody2).build();

        new Thread(() -> {
            try {
                Response response = okHttpClient.newCall(RcarouselUrl).execute();
                Response response2 = okHttpClient.newCall(RcarouselUrl2).execute();
                if (response.isSuccessful() && response2.isSuccessful()) {
                    handler.post(() -> {
                        Toast.makeText(this, "取消成功！", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }
            } catch (IOException e) {
                handler.post(() ->
                        Toast.makeText(this, "失败！", Toast.LENGTH_SHORT).show()
                );
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * 创建订单时加载商品数据
     */
    private void loadGoodsMessage(boolean isCreatOreder) {
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

                    Gson gson = new Gson();
                    Type listType = new TypeToken<Product>() {
                    }.getType();
                    Product product = gson.fromJson(res, listType);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            orderGoodsName.setText(product.getName());
                            orderGoodsPrice.setText(product.getPrice());
                            orderGoodsNumber.setText("×" + purchaseQuantity);
                            // 设置数据到视图
                            Glide.with(getApplicationContext())
                                    .load(product.getImageUrl())
                                    .centerCrop()
                                    .error(R.drawable.error_picture)
                                    .into(orderGoodsImg);

                            // 计算总金额
                            try {
                                // 获取购买数量并转换为整数
                                int quantity = Integer.parseInt(purchaseQuantity);

                                // 获取商品价格并清理非数字字符（如￥、$等）
                                String priceStr = orderGoodsPrice.getText().toString().replaceAll("[^\\d.]", "");
                                BigDecimal price = new BigDecimal(priceStr);

                                // 计算总金额（使用BigDecimal避免精度丢失）
                                BigDecimal total = price.multiply(new BigDecimal(quantity));

                                // 格式化为两位小数并转为String
                                DecimalFormat df = new DecimalFormat("#0.00");
                                totalAmount = df.format(total);

                            } catch (NumberFormatException e) {
                                Log.e("OrderTotalAmount", e.getMessage());
                                Toast.makeText(OrderDetails.this, "金额异常！", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            orderAllMoney.setText(totalAmount);

                            if (isCreatOreder) {
                                // 所有数据加载完毕，创建订单
                                createOrder();
                            }
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public interface OnAddressIdLoadedListener {
        void onAddressIdLoaded();
    }

    /**
     * 创建订单时使用默认地址
     *
     * @param listener
     */
    private void loadAddressId(OnAddressIdLoadedListener listener) {
        String Url = new WebUrl().getBASE_URL() + "/api/search/address?userToken=" + Token;
        Request request = new Request.Builder().url(Url).build();

        okHttpClient = new OkHttpClient.Builder().build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                handler.post(() -> Log.e("OrderLoadAddressId", "加载失败: " + e.getMessage()));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e("OrderLoadAddressId", "加载失败");
                    return;
                }

                String res = response.body().string();
                Type type = new TypeToken<List<Address>>() {
                }.getType();
                List<Address> addresses = new Gson().fromJson(res, type);

                handler.post(() -> {
                    if (!addresses.isEmpty()) {
                        Address address = addresses.get(0);
                        addressId = address.getAddressId();
                        orderPersonalAddress.setText(address.getUsername() + " " + address.getPhone() + " " + address.getAddressName());
                        // 通知addressId已加载
                        if (listener != null) {
                            listener.onAddressIdLoaded();
                        }
                    } else {
                        Toast.makeText(OrderDetails.this, "请前往地址管理补充收货信息！", Toast.LENGTH_SHORT).show();
                        finish();
                        Intent intent = new Intent(OrderDetails.this, AddressManage.class);
                        startActivity(intent);
                    }
                });
            }
        });
    }

    /**
     * 获取订单地址信息
     */
    private void loadOrderAddressId() {
        String Url = new WebUrl().getBASE_URL() + "/api/get/address?addressId=" + addressId;
        Request request = new Request.Builder().url(Url).build();
        okHttpClient = new OkHttpClient.Builder().build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                handler.post(() -> Log.e("OrderLoadAddressId", "加载失败: " + e.getMessage()));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String res = response.body().string();
                Type type = new TypeToken<Result<Address>>() {
                }.getType();
                Result<Address> address = new Gson().fromJson(res, type);

                handler.post(() -> {
                    if (address.getCode().equals("200")) {
                        Address address1 = address.getData();
                        addressId = address1.getAddressId();
                        orderPersonalAddress.setText(address1.getUsername() + " " + address1.getPhone() + " " + address1.getAddressName());
                    }
                });
            }
        });
    }

    private interface OnUserIdLoadedListener {
        void onUserIdLoaded();
    }

    /**
     * 加载用户id
     *
     * @param listener
     */
    private void loadUserId(OnUserIdLoadedListener listener) {
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


}