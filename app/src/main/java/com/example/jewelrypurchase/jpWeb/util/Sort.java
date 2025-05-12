package com.example.jewelrypurchase.jpWeb.util;

import com.google.gson.annotations.SerializedName;

public class Sort {
    @SerializedName("empty")
    private boolean empty;

    @SerializedName("sorted")
    private boolean sorted;

    @SerializedName("unsorted")
    private boolean unsorted;

    // Getter & Setter...

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public boolean isSorted() {
        return sorted;
    }

    public void setSorted(boolean sorted) {
        this.sorted = sorted;
    }

    public boolean isUnsorted() {
        return unsorted;
    }

    public void setUnsorted(boolean unsorted) {
        this.unsorted = unsorted;
    }
}