package com.example.jewelrypurchase.models;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.jewelrypurchase.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class DashboardBottomDialog extends BottomSheetDialogFragment {

    private ImageView ivGoods;
    private TextView tvPrice;
    private TextView tvCount;
    private int currentCount = 1;
    private OnConfirmListener listener;
    private String goodsNumber;
    private boolean setBuyText = false;
    private Button btnConfirm;

    public interface OnConfirmListener {
        void onConfirm(int quantity);
    }

    public void setOnConfirmListener(OnConfirmListener listener) {
        this.listener = listener;
    }

    @Override
    public void onStart() {
        super.onStart();
        // 确保在视图初始化后设置数据
        if (getArguments() != null) {
            String imageUrl = getArguments().getString("imageUrl");
            String price = getArguments().getString("price");
            goodsNumber = getArguments().getString("goodsNumber");
            setBuyText = Boolean.parseBoolean(getArguments().getString("setBuyText"));
            setGoodsInfo(imageUrl, price, setBuyText);
        }
    }

    // 新增方法：通过 Bundle 传递参数
    public static DashboardBottomDialog newInstance(String imageUrl, String price, String goodsNumber, boolean setBuyText) {
        DashboardBottomDialog dialog = new DashboardBottomDialog();
        Bundle args = new Bundle();
        args.putString("imageUrl", imageUrl);
        args.putString("price", price);
        args.putString("goodsNumber", goodsNumber);
        args.putString("setBuyText", String.valueOf(setBuyText));
        dialog.setArguments(args);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_goods_selector, null);
        dialog.setContentView(view);

        // 初始化视图
        ivGoods = view.findViewById(R.id.iv_goods);
        tvPrice = view.findViewById(R.id.tv_price);
        tvCount = view.findViewById(R.id.tv_count);
        btnConfirm = view.findViewById(R.id.btn_confirm);
        ImageButton btnMinus = view.findViewById(R.id.btn_minus);
        ImageButton btnPlus = view.findViewById(R.id.btn_plus);

        // 设置初始数量
        tvCount.setText(String.valueOf(currentCount));

        // 数量加减逻辑
        btnMinus.setOnClickListener(v -> {
            if (currentCount > 1) {
                currentCount--;
                tvCount.setText(String.valueOf(currentCount));
            }
        });

        btnPlus.setOnClickListener(v -> {
            if(Integer.valueOf(goodsNumber) > currentCount){
                currentCount++;
                tvCount.setText(String.valueOf(currentCount));
            }
        });

        // 确定按钮
        btnConfirm.setOnClickListener(v -> {
            if (listener != null) {
                listener.onConfirm(currentCount);
            }
            dismiss();
        });

        // 设置圆角背景(弃用)
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_round_bg);
        }

        return dialog;
    }

    // 设置商品信息
    public void setGoodsInfo(String imageUrl, String price, boolean setBuyText) {
        tvPrice.setText("￥ " + price);
        if(setBuyText) btnConfirm.setText("立即购买");
        Glide.with(requireContext())
                .load(imageUrl)
                .centerCrop()
                .error(R.drawable.error_picture)
                .into(ivGoods);
    }

}
