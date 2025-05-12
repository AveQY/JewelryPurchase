package com.example.jewelrypurchase.jpWeb;

public class Result<T> {
    private String code;
    private String token;
    private T data;

    public Result(String code, String token, T data) {
        this.code = code;
        this.token = token;
        this.data = data;
    }

    // Getter 和 Setter

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
