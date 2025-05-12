package com.example.jewelrypurchase.jpWeb.util;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PageResponse<T> {
    @SerializedName("content")
    private List<T> content;

    @SerializedName("pageable")
    private Pageable pageable;

    @SerializedName("last")
    private boolean last;

    @SerializedName("totalPages")
    private int totalPages;

    @SerializedName("totalElements")
    private long totalElements;

    @SerializedName("size")
    private int size;

    @SerializedName("number")
    private int number;

    @SerializedName("sort")
    private Sort sort;

    @SerializedName("numberOfElements")
    private int numberOfElements;

    @SerializedName("first")
    private boolean first;

    @SerializedName("empty")
    private boolean empty;

    // 判断是否有更多数据（用于分页加载）
    public boolean hasMore() {
        return !last;
    }

    // Getter & Setter...

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public Pageable getPageable() {
        return pageable;
    }

    public void setPageable(Pageable pageable) {
        this.pageable = pageable;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }
}