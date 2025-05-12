package com.example.jewelrypurchase.models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddressUtils {

    public static Map<String, Map<String, List<String>>> parseAddressData(InputStream inputStream) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, Map<String, List<String>>>>() {}.getType();
            return gson.fromJson(json.toString(), type);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 获取省份列表
    public static List<String> getProvinceList(Map<String, Map<String, List<String>>> data) {
        return new ArrayList<>(data.keySet());
    }

    // 获取城市列表
    public static List<String> getCityList(Map<String, Map<String, List<String>>> data, String province) {
        return new ArrayList<>(data.get(province).keySet());
    }

    // 获取区县列表
    public static List<String> getAreaList(Map<String, Map<String, List<String>>> data, String province, String city) {
        return data.get(province).get(city);
    }
}