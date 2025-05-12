package com.example.jewelrypurchase.ui.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jewelrypurchase.R;
import com.example.jewelrypurchase.adapter.ProductChatAdapter;
import com.example.jewelrypurchase.databinding.FragmentMessageBinding;
import com.example.jewelrypurchase.jpWeb.ProductChatItem;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {
    private FragmentMessageBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMessageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = root.findViewById(R.id.rv_chat_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new ProductChatAdapter(getContext(), getSampleData()));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private List<ProductChatItem> getSampleData() {
        List<ProductChatItem> items = new ArrayList<>();
        items.add(new ProductChatItem(
                "0",
                "珠宝购智能AI客服",
                "咨询",
                "您好，我能帮助到什么？",
                System.currentTimeMillis(),
                0,
                R.drawable.customer_service
        ));
        items.add(new ProductChatItem(
                "1",
                "DeepSeek R1 pro",
                "DeepSeek",
                "您好！我是基于 DeepSeek R1 模型而诞生的超级助手，有什么能帮助到您？",
                System.currentTimeMillis(),
                0,
                R.drawable.deepseek
        ));
        return items;
    }
}
