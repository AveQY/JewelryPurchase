package com.example.jewelrypurchase.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.jewelrypurchase.R;
import com.example.jewelrypurchase.jpWeb.PayMethod;

import java.util.List;

public class PayMethodAdaper extends RecyclerView.Adapter<PayMethodAdaper.ViewHolder> {
    private List<PayMethod> payMethods;
    private Context context;
    private int selectedPosition = 0; // 当前选中项的位置

    public PayMethodAdaper(Context context, List<PayMethod> payMethods) {
        this.context = context;
        this.payMethods = payMethods;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment_method, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PayMethod payMethod = payMethods.get(position);

        // 加载图标和名称
        Glide.with(holder.itemView.getContext())
                .load(payMethod.getIcon())
                .error(R.drawable.error_picture)
                .into(holder.payMethodIcon);
        holder.payMethodName.setText(payMethod.getName());

        // 根据选中位置设置CheckBox状态
        holder.paymentMethodCheck.setChecked(position == selectedPosition);

        // 点击整个项时触发单选逻辑
        holder.payMethodSelect.setOnClickListener(v -> {
            int previousSelected = selectedPosition;
            selectedPosition = holder.getAdapterPosition();

            // 刷新前一个选中项和当前选中项
            notifyItemChanged(previousSelected);
            notifyItemChanged(selectedPosition);
        });

        holder.paymentMethodCheck.setOnClickListener(v -> {
            int previousSelected = selectedPosition;
            selectedPosition = holder.getAdapterPosition();

            // 刷新前一个选中项和当前选中项
            notifyItemChanged(previousSelected);
            notifyItemChanged(selectedPosition);
        });
    }

    @Override
    public int getItemCount() {
        return payMethods == null ? 0 : payMethods.size();
    }

    // 获取当前选中的支付方式
    public int getSelectedPayMethod() {
        if (selectedPosition != -1 && selectedPosition < payMethods.size()) {
            return payMethods.get(selectedPosition).getId();
        }
        return 1;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView payMethodIcon;
        private TextView payMethodName;
        private CheckBox paymentMethodCheck;
        private LinearLayout payMethodSelect;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            payMethodIcon = itemView.findViewById(R.id.payMethodIcon);
            payMethodName = itemView.findViewById(R.id.payMethodName);
            paymentMethodCheck = itemView.findViewById(R.id.payment_method_check);
            payMethodSelect = itemView.findViewById(R.id.payMethodSelect);
        }
    }
}