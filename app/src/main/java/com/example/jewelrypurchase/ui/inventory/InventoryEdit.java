package com.example.jewelrypurchase.ui.inventory;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.jewelrypurchase.R;
import com.example.jewelrypurchase.jpWeb.Product;
import com.example.jewelrypurchase.jpWeb.ProductCategory;
import com.example.jewelrypurchase.jpWeb.ProductImg;
import com.example.jewelrypurchase.jpWeb.Result;
import com.example.jewelrypurchase.jpWeb.WebUrl;
import com.example.jewelrypurchase.jpWeb.util.FileUploader;
import com.example.jewelrypurchase.ui.MainActivity;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class InventoryEdit extends AppCompatActivity {

    private TextView incentoryEcard1;
    private TextView incentoryEcard2;
    private TextView incentoryEcard3;
    private TextView incentoryEcard4;
    private ImageView incentoryBack;
    private LinearLayout inventorySType;
    private LinearLayout inventorySPrice;
    private TextView inventoryGprice;
    private TextView inventoryGtype;
    private CardView inventoryEupdata;
    private TextView iEditNames;
    private TextView iEditDescription;
    private ImageView incentoryEImg1;
    private ImageView incentoryEImg2;
    private ImageView incentoryEImg3;
    private ImageView incentoryEImg4;
    private static final int REQUEST_CODE_PICK_FILE = 100;
    private String getOnb;
    private SharedPreferences sharedPreferences;
    private OkHttpClient okHttpClient;
    private int imgBturd = 0;
    private String categoryId;

    List<ProductImg> images = new ArrayList<ProductImg>(Arrays.asList());

    private Handler handler = new Handler(Looper.getMainLooper());
    private TextView inventoryGm;
    private LinearLayout inventorySI;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_edit);

        // 隐藏标题栏
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        iEditNames = findViewById(R.id.iEditNames);
        iEditDescription = findViewById(R.id.iEditDescription);
        incentoryEcard1 = findViewById(R.id.incentoryEcard1);
        incentoryEImg1 = findViewById(R.id.incentoryEImg1);
        incentoryEcard2 = findViewById(R.id.incentoryEcard2);
        incentoryEImg2 = findViewById(R.id.incentoryEImg2);
        incentoryEcard3 = findViewById(R.id.incentoryEcard3);
        incentoryEImg3 = findViewById(R.id.incentoryEImg3);
        incentoryEcard4 = findViewById(R.id.incentoryEcard4);
        incentoryEImg4 = findViewById(R.id.incentoryEImg4);
        incentoryBack = findViewById(R.id.incentoryBack);
        inventorySType = findViewById(R.id.inventorySType);
        inventorySPrice = findViewById(R.id.inventorySPrice);
        inventoryGtype = findViewById(R.id.inventoryGtype);
        inventoryGprice = findViewById(R.id.inventoryGprice);
        inventoryEupdata = findViewById(R.id.inventoryEupdata);
        inventorySI = findViewById(R.id.inventorySI);
        inventoryGm = findViewById(R.id.inventoryGm);
        getOnb = generateOrderNumber();

        sharedPreferences = getSharedPreferences("inventoryEdit_info", MODE_PRIVATE);

        Intent intent = getIntent();
        String inventoryId = intent.getStringExtra("inventoryId");

        String token = sharedPreferences.getString("token", "");

        // 待办： 保存为草稿
        if (token != "") {

        }

        if (inventoryId != null) {
            getOnb = inventoryId;
            okHttpClient = new OkHttpClient.Builder().build();

            String inventoryUrl = new WebUrl().getBASE_URL() + "/api/search/product?productId=" + inventoryId;
            String inventoryUrl2 = new WebUrl().getBASE_URL() + "/api/search/productImg?id=" + inventoryId;

            Request RcarouselUrl = new Request.Builder().url(inventoryUrl).build();
            Request RcarouselInventoryImgUrl = new Request.Builder().url(inventoryUrl2).build();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Response response = okHttpClient.newCall(RcarouselUrl).execute();
                        Response responseImg = okHttpClient.newCall(RcarouselInventoryImgUrl).execute();
                        String res = response.body().string();
                        String res2 = responseImg.body().string();

                        Type listType = new TypeToken<Product>() {
                        }.getType();
                        Type listType2 = new TypeToken<List<ProductImg>>() {
                        }.getType();
                        Product result = new Gson().fromJson(res, listType);
                        List<ProductImg> resultImg = new Gson().fromJson(res2, listType2);
                        images.addAll(resultImg);

                        // 在主线程中更新UI
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                iEditNames.setText(result.getName());
                                iEditDescription.setText(result.getDescription());
                                inventoryGprice.setText(result.getPrice());
                                inventoryGm.setText(result.getStock());
                                for (int i = 0; i < resultImg.size(); i++) {
                                    String url = images.get(i).getImageUrl();
                                    ImageView imgView = incentoryEImg1;
                                    if (i == 1) {
                                        imgView = incentoryEImg2;
                                    } else if (i == 2) {
                                        imgView = incentoryEImg3;
                                    } else if (i == 3) {
                                        imgView = incentoryEImg4;
                                    }
                                    Glide.with(getApplicationContext())
                                            .load(url)
                                            .centerCrop()
                                            .error(R.drawable.error_picture)
                                            .into(imgView);
                                }

                                categoryId = result.getCategoryId();
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
                                                    inventoryGtype.setText(result.getCategoriesName());
                                                }
                                            });

                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();
                            }
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }

        // 点击返回
        incentoryBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /**
         * 添加图片 未做：最多4张图片，超出将最先图片在数据库删除，并在清理对应文件
         */
        incentoryEcard1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgBturd = 0;
                pickFile();
            }
        });
        incentoryEcard2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgBturd = 1;
                pickFile();
            }
        });
        incentoryEcard3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgBturd = 2;
                pickFile();
            }
        });
        incentoryEcard4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgBturd = 3;
                pickFile();
            }
        });

        // 分类
        inventorySType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTpyeDialog();
            }
        });

        // 价格
        inventorySPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPriceDialog();
            }
        });

        // 库存
        inventorySI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInventoryDialog();
            }
        });

        // 发布
        inventoryEupdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newInventory();
            }
        });
    }

    // 分类输入对话框
    public void showTpyeDialog() {
        // 创建一个AlertDialog.Builder对象
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 点击外部是否可以取消
        builder.setCancelable(false);
        // 设置对话框的内容
        builder.setTitle("类别");
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_input_type, null);
        EditText editTextView = dialogView.findViewById(R.id.editDescription);
        editTextView.setHint("请输入宝贝类别");
        editTextView.setText(inventoryGtype.getText());
        builder.setView(dialogView);

        // 设置对话框的按钮
        builder.setPositiveButton("确定", (dialog, which) -> {
            inventoryGtype.setText(editTextView.getText());

            okHttpClient = new OkHttpClient.Builder().build();
            String categoryUrl = new WebUrl().getBASE_URL() + "/api/productCategory?categoriesName=" + editTextView.getText();
            Request btuRcarouselUrl = new Request.Builder()
                    .url(categoryUrl)
                    .build();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Response response = okHttpClient.newCall(btuRcarouselUrl).execute();
                        String res = response.body().string();

                        Gson gson = new Gson();
                        Type listType = new TypeToken<Result<ProductCategory>>() {
                        }.getType();
                        Result<ProductCategory> result = gson.fromJson(res, listType);

                        // 在主线程中更新UI
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                categoryId = result.getToken();
                            }
                        });

                    } catch (IOException e) {
                        Toast.makeText(InventoryEdit.this, "服务器错误！", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }).start();
        });
        builder.setNegativeButton("取消", (dialog, which) -> {

        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // 价格输入对话框
    public void showPriceDialog() {
        // 创建一个AlertDialog.Builder对象
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 点击外部是否可以取消
        builder.setCancelable(false);
        // 设置对话框的内容
        builder.setTitle("请输入宝贝价格");
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_input_price, null);
        EditText editTextView = dialogView.findViewById(R.id.editDescription);
        editTextView.setHint(inventoryGprice.getText());
        builder.setView(dialogView);

        // 设置对话框的按钮
        builder.setPositiveButton("确定", (dialog, which) -> {
            String str = String.valueOf(editTextView.getText());
            if (!str.isEmpty()) {
                double number = Double.parseDouble(str);
                String result = String.format("%.2f", number);
                inventoryGprice.setText(result);
            }
        });
        builder.setNegativeButton("取消", (dialog, which) -> {

        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // 库存输入框
    public void showInventoryDialog() {
        // 创建一个AlertDialog.Builder对象
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 点击外部是否可以取消
        builder.setCancelable(false);
        // 设置对话框的内容
        builder.setTitle("宝贝有多少库存");
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_input_price, null);
        EditText editTextView = dialogView.findViewById(R.id.editDescription);
        editTextView.setHint(inventoryGm.getText());
        builder.setView(dialogView);

        // 设置对话框的按钮
        builder.setPositiveButton("确定", (dialog, which) -> {
            String str = String.valueOf(editTextView.getText());
            if (!str.isEmpty()) {
                String cleanStr = str.trim().replace(".", "");
                inventoryGm.setText(cleanStr);
            }
        });
        builder.setNegativeButton("取消", (dialog, which) -> {

        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // 发布
    private void newInventory() {
        String name = iEditNames.getText().toString();
        String description = iEditDescription.getText().toString();
        String tpye = inventoryGtype.getText().toString();
        String price = inventoryGprice.getText().toString();
        String inventoryMath = inventoryGm.getText().toString();
        sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        if (!name.isEmpty() && !description.isEmpty() && !tpye.isEmpty() &&
                incentoryEImg1.getDrawable() != null && !price.isEmpty() && !inventoryMath.isEmpty()) {

            okHttpClient = new OkHttpClient.Builder().build();

            String inventoryUrl = new WebUrl().getBASE_URL() + "/api/addInventory";

            FormBody formBody = new FormBody.Builder()
                    .add("productId", getOnb)
                    .add("name", name)
                    .add("description", description)
                    .add("price", price)
                    .add("categoryId", categoryId)
                    .add("stock", inventoryMath)
                    .add("imageUrl", images.get(0).getImageUrl())
                    .add("author", username)
                    .add("isSell", "1")
                    .build();

            Request RcarouselUrl = new Request.Builder()
                    .url(inventoryUrl)
                    .post(formBody)
                    .build();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Response response = okHttpClient.newCall(RcarouselUrl).execute();
                        String res = response.body().string();

                        try {
                            Gson gson = new Gson();
                            Type listType = new TypeToken<Result>() {
                            }.getType();
                            Result result = gson.fromJson(res, listType);
                            // 在主线程中更新UI
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (result.getCode().equals("200")) {
                                        Toast.makeText(InventoryEdit.this, "发布成功", Toast.LENGTH_SHORT).show();
                                    } else if (result.getCode().equals("201")) {
                                        Toast.makeText(InventoryEdit.this, "更新成功", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(InventoryEdit.this, "宝贝信息有误！", Toast.LENGTH_SHORT).show();
                                    }

                                    // 创建一个新的Intent对象
                                    Intent intent = new Intent(InventoryEdit.this, MainActivity.class);
                                    // 销毁之前所有界面
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    finish(); // 关闭当前Activity
                                    startActivity(intent); // 重新启动Activity
                                }
                            });
                        } catch (JsonSyntaxException e) {
                            Toast.makeText(InventoryEdit.this, "服务器错误！", Toast.LENGTH_SHORT).show();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        } else {
            Toast.makeText(this, "请补全宝贝信息", Toast.LENGTH_SHORT).show();
        }
    }

    // 启动文件选择器
    private void pickFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*"); // 限定选择图片类型
        startActivityForResult(intent, REQUEST_CODE_PICK_FILE);
    }

    // 处理文件选择结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_FILE && resultCode == RESULT_OK && data != null) {
            Uri fileUri = data.getData();
            if (fileUri != null) {
                // 调用上传方法
                uploadFile(fileUri);
            }
        }
    }

    // 执行上传操作
    private void uploadFile(Uri fileUri) {
        WebUrl webUrl = new WebUrl();

        FileUploader.uploadFile(
                this,
                fileUri,
                webUrl.getBASE_URL() + "/api/picture_upload",
                getOnb,
                new FileUploader.UploadCallback() {
                    @Override
                    public void onSuccess(String response) {
                        // 上传成功处理
                        loadImg();
                    }

                    @Override
                    public void onFailure(String error) {
                        // 上传失败处理
                    }
                });
    }


    // 随机生成订单号
    private static String generateOrderNumber() {
        // 获取当前时间戳（精确到毫秒）
        SimpleDateFormat sdf = new SimpleDateFormat("dHsS");
        String timestamp = sdf.format(new Date());

        // 生成随机数（4 位）
        Random random = new Random();
        int randomValue = random.nextInt(100);

        // 组合时间戳和随机数
        return timestamp + String.format("%02d", randomValue);
    }

    // 保存为草稿 (暂未使用)
    private void saveDraft(String onbNum, String token, String name, String decre,
                           String img1, String img2, String img3, String img4, String tpye, String price, String inventory) {
        // 获取 Editor 对象
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // 保存信息
        editor.putString("onbNum", onbNum);
        editor.putString("token", token);
        editor.putString("name", name);
        editor.putString("dec", decre);
        editor.putString("img1", img1);
        editor.putString("img2", img2);
        editor.putString("img3", img3);
        editor.putString("img4", img4);
        editor.putString("tpye", tpye);
        editor.putString("price", price);
        editor.putString("inventory", inventory);

        // 提交保存（apply() 是异步的，commit() 是同步的）
        editor.apply();
    }

    private void loadImg() {
        okHttpClient = new OkHttpClient.Builder().build();

        String Url = new WebUrl().getBASE_URL() + "/api/search/productImg?id=" + getOnb;

        Request RcarouselUrl = new Request.Builder()
                .url(Url)
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

                    images.addAll(imageItemList);

                    // 在主线程中更新UI
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            ImageView imgView = incentoryEImg1;
                            if (imgBturd == 1) {
                                imgView = incentoryEImg2;
                            } else if (imgBturd == 2) {
                                imgView = incentoryEImg3;
                            } else if (imgBturd == 3) {
                                imgView = incentoryEImg4;
                            }
                            Glide.with(getApplicationContext())
                                    .load(images.get(images.size() - 1).getImageUrl())
                                    .centerCrop()
                                    .error(R.drawable.error_picture)
                                    .into(imgView);
                        }
                    });
                } catch (
                        IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}