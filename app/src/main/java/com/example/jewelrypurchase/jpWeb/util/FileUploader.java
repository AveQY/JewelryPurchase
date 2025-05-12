package com.example.jewelrypurchase.jpWeb.util;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FileUploader {

    // 回调接口（成功/失败）
    public interface UploadCallback {
        void onSuccess(String response);
        void onFailure(String error);
    }

    /**
     * 上传文件并携带订单号
     *
     * @param context     上下文
     * @param fileUri     文件Uri
     * @param serverUrl   服务端接口URL
     * @param orderNumber 订单号（新增参数）
     * @param callback    回调接口
     */
    public static void uploadFile(Context context, Uri fileUri, String serverUrl,
                                  String orderNumber, UploadCallback callback) {
        new Thread(() -> {
            try {
                // 1. 校验订单号（示例校验非空）
                if (orderNumber == null || orderNumber.trim().isEmpty()) {
                    notifyFailure(context, "订单号不能为空", callback);
                    return;
                }

                // 2. 获取文件路径
                String filePath = FileUtils.getPathFromUri(context, fileUri);
                if (filePath == null) {
                    notifyFailure(context, "文件路径获取失败", callback);
                    return;
                }

                // 3. 构建多部分请求体（添加订单号字段）
                File file = new File(filePath);
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("orderNumber", orderNumber) // 新增订单号字段
                        .addFormDataPart("file", file.getName(),
                                RequestBody.create(file, MediaType.parse("image/*")))
                        .build();

                // 4. 构建请求
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(serverUrl)
                        .post(requestBody)
                        .build();

                // 5. 异步上传
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        notifyFailure(context, "上传失败: " + e.getMessage(), callback);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String result = response.body().string();
                            notifySuccess(context, result, callback);
                        } else {
                            notifyFailure(context, "服务器错误: " + response.code(), callback);
                        }
                    }
                });

            } catch (Exception e) {
                notifyFailure(context, "异常: " + e.getMessage(), callback);
            }
        }).start();
    }

    // 主线程通知成功
    private static void notifySuccess(Context context, String result, UploadCallback callback) {
        new Handler(Looper.getMainLooper()).post(() -> {
            if (callback != null) callback.onSuccess(result);
//            Toast.makeText(context, "上传成功", Toast.LENGTH_SHORT).show();
        });
    }

    // 主线程通知失败
    private static void notifyFailure(Context context, String error, UploadCallback callback) {
        new Handler(Looper.getMainLooper()).post(() -> {
            if (callback != null) callback.onFailure(error);
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
        });
    }
}