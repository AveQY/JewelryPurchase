package com.example.jewelrypurchase.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jewelrypurchase.R;
import com.example.jewelrypurchase.jpWeb.Address;
import com.example.jewelrypurchase.jpWeb.WebUrl;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    private List<Address> addresses;
    private Context context;
    private OnItemClickListener listener;

    private OkHttpClient okHttpClient;
    private Handler handler = new Handler(Looper.getMainLooper());

    public AddressAdapter(Context context, List<Address> addresses) {
        this.addresses = addresses;
        this.context = context;
    }

    @NonNull
    @Override
    public AddressAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address_list, parent, false);
        return new AddressAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Address address = addresses.get(position);

        holder.addressManageName.setText(address.getUsername());
        holder.addressManagePhone.setText(address.getPhone());
        holder.addressManageAccurate.setText(address.getAddressName());

        holder.addressItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // 添加position参数
                deleteDashboardGoodsDialog(
                        address.getAddressName(),
                        address.getAddressId(),
                        position // 传递当前项的position
                );
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return addresses == null ? 0 : addresses.size();
    }

    // 定义接口
    public interface OnItemClickListener {
        void onItemClick(Address address);
    }

    // 设置监听器
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView addressManageName;
        private TextView addressManagePhone;
        private TextView addressManageAccurate;
        private LinearLayout addressItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            addressManageName = itemView.findViewById(R.id.addressManageName);
            addressManagePhone = itemView.findViewById(R.id.addressManagePhone);
            addressManageAccurate = itemView.findViewById(R.id.addressManageAccurate);
            addressItem = itemView.findViewById(R.id.addressItem);

            addressItem.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(addresses.get(position));
                }
            });
        }
    }

    // 删除地址
    public void deleteDashboardGoodsDialog(String name, int addressId, int position) {
        // 创建一个AlertDialog.Builder对象
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // 点击外部是否可以取消
        builder.setCancelable(false);
        // 设置对话框的内容
        builder.setTitle("删除地址");
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_privacy_policy, null);
        TextView editTextView = dialogView.findViewById(R.id.privacy_policy_text);
        editTextView.setText("是否删除 " + name + "?");
        builder.setView(dialogView);

        // 设置对话框的按钮
        builder.setPositiveButton("确定", (dialog, which) -> {
            okHttpClient = new OkHttpClient.Builder().build();
            String Url = new WebUrl().getBASE_URL() + "/api/delete/address/" + addressId;
            Request RcarouselUrl = new Request.Builder()
                    .url(Url)
                    .delete()
                    .build();

            new Thread(() -> {
                try {
                    Response response = okHttpClient.newCall(RcarouselUrl).execute();
                    if (response.isSuccessful()) {
                        handler.post(() -> {
                            // 1. 从本地数据源移除对应项
                            addresses.remove(position);
                            // 2. 通知适配器数据变化
                            notifyDataSetChanged();
                            // 3. 可选：若删除后列表为空，更新空视图
                            if (getItemCount() == 0) {
                                // 触发空列表UI逻辑（需自行实现）
                            }
                            Toast.makeText(context, "删除成功！", Toast.LENGTH_SHORT).show();
                        });
                    }
                } catch (IOException e) {
                    handler.post(() ->
                            Toast.makeText(context, "删除失败！", Toast.LENGTH_SHORT).show()
                    );
                    e.printStackTrace();
                }
            }).start();
        });
        builder.setNegativeButton("取消", (dialog, which) -> {

        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
