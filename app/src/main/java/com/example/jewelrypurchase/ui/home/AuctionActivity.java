package com.example.jewelrypurchase.ui.home;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jewelrypurchase.R;
import com.example.jewelrypurchase.adapter.AuctionViewAdapter;
import com.example.jewelrypurchase.jpWeb.Auction;

import java.util.ArrayList;
import java.util.List;

public class AuctionActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.search));
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        // 隐藏标题栏
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        recyclerView = findViewById(R.id.auction_goods_recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getBaseContext(), 1));
        recyclerView.setAdapter(new AuctionViewAdapter(getBaseContext(), getSampleData()));

    }

    private List<Auction> getSampleData() {
        List<Auction> items = new ArrayList<>();
        items.add(new Auction(1,"无烧锡兰蓝宝石项链拍卖",
                "http://aweqy.asia/alist/d/123%E4%BA%91%E7%9B%98/%E5%9B%BE%E7%89%87/%E7%8F%A0%E5%AE%9D%E8%B4%AD/p1.jpg?sign=Qlg9s21EluyZh1ME6YCH6Tcxhm_JUo1Zxmnogd6bwOo=:0",
                "1",
                "2025.03.29 12:30",
                "梧桐"));
        items.add(new Auction(2,"传奇祖母绿戒指拍卖",
                "http://aweqy.asia/alist/d/123%E4%BA%91%E7%9B%98/%E5%9B%BE%E7%89%87/%E7%8F%A0%E5%AE%9D%E8%B4%AD/p2.jpg?sign=87_f2ythEiqtrrYBNFovk9hk-eU3q0oQzC8w3_rQ6z0=:0",
                "1",
                "2025.03.18 12:30",
                "梧桐"));
        items.add(new Auction(3,"大克拉黄钻戒指拍卖",
                "http://aweqy.asia/alist/d/123%E4%BA%91%E7%9B%98/%E5%9B%BE%E7%89%87/%E7%8F%A0%E5%AE%9D%E8%B4%AD/p3.jpg?sign=-gvLseM8NP5SvUgkT1HzdRXjc-b_EcxaXAYAco52hzg=:0",
                "1",
                "2025.03.06 12:30",
                "不会吸猫"));
        items.add(new Auction(4,"帕拉伊巴碧玺项链拍卖",
                "http://aweqy.asia/alist/d/123%E4%BA%91%E7%9B%98/%E5%9B%BE%E7%89%87/%E7%8F%A0%E5%AE%9D%E8%B4%AD/p4.jpg?sign=2juhN1dDj06a5MAvOMw0Vj0mgxYC7Wjd-TELbnZfD5E=:0",
                "1",
                "2025.02.26 12:30",
                "不会吸猫"));
        items.add(new Auction(5,"Winston Kaleidoscope 绿碧玺项链拍卖",
                "http://aweqy.asia/alist/d/123%E4%BA%91%E7%9B%98/%E5%9B%BE%E7%89%87/%E7%8F%A0%E5%AE%9D%E8%B4%AD/p5.jpg?sign=jZvwHvWDqDtuLNnqOiQ5X_3S8EJyx6koIpKCm94Gpm4=:0",
                "1",
                "2025.02.26 12:30",
                "patience"));
        return items;
    }
}