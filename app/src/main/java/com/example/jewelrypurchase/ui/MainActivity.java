package com.example.jewelrypurchase.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.jewelrypurchase.R;
import com.example.jewelrypurchase.databinding.ActivityMainBinding;
import com.example.jewelrypurchase.jpWeb.util.StatusBar;
import com.example.jewelrypurchase.ui.home.AuctionActivity;
import com.example.jewelrypurchase.ui.home.CategoryActivity;
import com.example.jewelrypurchase.ui.personCenter.LoginActivity;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private static final int REQUEST_CODE_PICK_FILE = 100;
    private static final int PERMISSION_REQUEST_READ_STORAGE = 200;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home,
                R.id.navigation_dashboard,
                R.id.navigation_inventory,
                R.id.navigation_message,
                R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        // 隐藏标题栏
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // 设置状态栏
        new StatusBar().defaultStyle(getWindow(),this);

        // 获取 SharedPreferences 对象
        SharedPreferences sharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        if(token == ""){
            // 删除保存的token
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            // 登录界面
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            this.finish();
            startActivity(intent);
        }


    }


    /**
     * 点击事件
     */
    // 主页
    public void clickHomeCom1(View view) {
        // 拍卖界面
        Intent intent = new Intent(MainActivity.this, AuctionActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, 0);
    }

    public void clickHomeCom2(View view){
        Toast.makeText(this, "敬请期待~", Toast.LENGTH_SHORT).show();
    }

    public void clickHomeCom3(View view){
        // 分类界面
        Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, 0);
    }

    public void clickHomeCom4(View view){
        Toast.makeText(this, "敬请期待~", Toast.LENGTH_SHORT).show();
    }

    // 我的
    public void clickLoginIcon(View view){
        // 获取 SharedPreferences 对象
        SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
        if(sharedPreferences.getString("token", "") == ""){
            // 登录界面
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }else{
            intoPersonalInformation();
        }
    }

    public void clickInformation(View view){
        intoPersonalInformation();
    }

    public void intoPersonalInformation(){
        Toast.makeText(this, "暂时还不支持更改个人信息！", Toast.LENGTH_SHORT).show();
    }

}