package com.example.jewelrypurchase.jpWeb;

public class PayMethod {
    private int id;
    private int icon;
    private String name;

    public PayMethod() {
    }

    public PayMethod(int id, int icon, String name) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
