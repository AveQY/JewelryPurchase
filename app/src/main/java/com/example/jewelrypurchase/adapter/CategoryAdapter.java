package com.example.jewelrypurchase.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jewelrypurchase.R;
import com.example.jewelrypurchase.jpWeb.ProductCategory;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private List<ProductCategory> categories;
    private OnCategoryClickListener listener;
    private int selectedPosition = -1;// 跟踪选中位置

    public void setOnCategoryClickListener(OnCategoryClickListener listener) {
        this.listener = listener;
    }

    public CategoryAdapter(List<ProductCategory> categories) {
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        ProductCategory productCategory = categories.get(position);
        holder.categoryName.setText(productCategory.getCategoriesName());

        // 根据选中状态设置背景颜色
        if (position == selectedPosition) {
            holder.categoryName.setBackgroundColor(Color.WHITE); // 选中项白色
        } else {
            holder.categoryName.setBackgroundColor(Color.TRANSPARENT); // 其他项透明
        }

        holder.categoryName.setOnClickListener(v -> {
            int previousSelected = selectedPosition;
            selectedPosition = holder.getAdapterPosition();

            // 刷新前一个选中项和当前项
            notifyItemChanged(previousSelected);
            notifyItemChanged(selectedPosition);

            if (listener != null) {
                listener.onCategoryClick(selectedPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories == null ? 0 : categories.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView categoryName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.categoryNameText);
        }
    }

    public interface OnCategoryClickListener {
        void onCategoryClick(int position);
    }
}
