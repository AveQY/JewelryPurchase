package com.example.jewelrypurchase.ui.personCenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.jewelrypurchase.R;
import com.example.jewelrypurchase.jpWeb.Result;
import com.example.jewelrypurchase.jpWeb.WebUrl;
import com.example.jewelrypurchase.models.AddressUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AddressEdit extends AppCompatActivity {

    private EditText addressUserEdit;
    private EditText addressPhoneEdit;
    private EditText accurateAddressEdit;

    // 地址
    private Map<String, Map<String, List<String>>> addressData;
    private Spinner provinceSpinner, citySpinner, areaSpinner;
    private List<String> provinces, cities, areas;

    private OkHttpClient okHttpClient;
    private Handler handler = new Handler(Looper.getMainLooper());
    private SharedPreferences sharedPreferences;
    private String addressAuthor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        addressUserEdit = findViewById(R.id.addressUserEdit);
        addressPhoneEdit = findViewById(R.id.addressPhoneEdit);
        accurateAddressEdit = findViewById(R.id.accurateAddressEdit);
        provinceSpinner = findViewById(R.id.province_spinner);
        citySpinner = findViewById(R.id.city_spinner);
        areaSpinner = findViewById(R.id.area_spinner);

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

        // 选择地区
        // 异步加载数据
        new Thread(() -> {
            try {
                InputStream is = getAssets().open("area_China.json");
                addressData = AddressUtils.parseAddressData(is);
                runOnUiThread(this::setupSpinners);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        CardView saveAddress = findViewById(R.id.saveAddress);
        saveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAddressMessage(getSelectedAddress());
            }
        });
    }

    private void saveAddressMessage(String address) {
        String name = String.valueOf(addressUserEdit.getText());
        String phone = String.valueOf(addressPhoneEdit.getText());
        String addressAll = address + " " + accurateAddressEdit.getText();

        if(!name.isEmpty() && !phone.isEmpty() && !addressAll.isEmpty()) {
            String Url = new WebUrl().getBASE_URL() + "/api/add/address";
            FormBody formBody = new FormBody.Builder()
                    .add("userId", addressAuthor)
                    .add("addressName", addressAll)
                    .add("phone", phone)
                    .add("username", name)
                    .build();
            Request RcarouselUrl = new Request.Builder()
                    .url(Url)
                    .post(formBody)
                    .build();
            okHttpClient = new OkHttpClient.Builder().build();

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
                                    if (Objects.equals(result.getCode(), "200")) {
                                        finish();
                                        Toast.makeText(AddressEdit.this, "增加成功！", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(AddressEdit.this, "请检查数据是否错误！", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } catch (JsonSyntaxException e) {
                            Toast.makeText(AddressEdit.this, "服务器错误！", Toast.LENGTH_SHORT).show();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }else {
            Toast.makeText(this, "请补全信息！", Toast.LENGTH_SHORT).show();
        }

    }

    private void setupSpinners() {
        // 初始化省份
        provinces = AddressUtils.getProvinceList(addressData);
        ArrayAdapter<String> provinceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, provinces);
        provinceSpinner.setAdapter(provinceAdapter);

        // 省份选择监听
        provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String province = provinces.get(position);
                cities = AddressUtils.getCityList(addressData, province);
                ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(AddressEdit.this, android.R.layout.simple_spinner_item, cities);
                citySpinner.setAdapter(cityAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // 城市选择监听
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String province = provinces.get(provinceSpinner.getSelectedItemPosition());
                String city = cities.get(position);
                areas = AddressUtils.getAreaList(addressData, province, city);
                ArrayAdapter<String> areaAdapter = new ArrayAdapter<>(AddressEdit.this, android.R.layout.simple_spinner_item, areas);
                areaSpinner.setAdapter(areaAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    // 获取最终选择的地址
    public String getSelectedAddress() {
        String province = provinceSpinner.getSelectedItem().toString();
        String city = citySpinner.getSelectedItem().toString();
        String area = areaSpinner.getSelectedItem().toString();
        return province + " " + city + " " + area;
    }

}
