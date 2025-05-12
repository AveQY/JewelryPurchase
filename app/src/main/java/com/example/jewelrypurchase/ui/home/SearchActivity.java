package com.example.jewelrypurchase.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jewelrypurchase.R;
import com.example.jewelrypurchase.jpWeb.util.StatusBar;

public class SearchActivity extends AppCompatActivity {

    private EditText searchEditText;
    private CardView searchButton;
    private RecyclerView searchResultsRecyclerView;
    private ImageView searchBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // 隐藏标题栏
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // 设置状态栏
        new StatusBar().defaultStyle(getWindow(),this);

        searchBack = findViewById(R.id.searchBack);
        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        searchResultsRecyclerView = findViewById(R.id.searchHistoryRecyclerView);

        // 点击返回
        searchBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(0, R.anim.fade_out);
            }
        });

        // 点击搜索
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchEditText.getText().toString();
                if (TextUtils.isEmpty(query)) {
                    Toast.makeText(SearchActivity.this, "你什么也没有输入", Toast.LENGTH_SHORT).show();
                } else {
                    performSearch(query);
                }
            }
        });
    }

    // 搜索
    private void performSearch(String query) {
        Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
        intent.putExtra("query", query);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in,0);
    }
}