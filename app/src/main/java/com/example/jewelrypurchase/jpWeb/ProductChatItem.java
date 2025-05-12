package com.example.jewelrypurchase.jpWeb;

public class ProductChatItem {
    private String productId;
    private String productName;
    private String lastSender;
    private String lastMessage;
    private long timestamp;
    private int unreadCount;
    private int productImageResId;

    public ProductChatItem(String productId, String productName,
                           String lastSender, String lastMessage,
                           long timestamp, int unreadCount,
                           int productImageResId) {
        this.productId = productId;
        this.productName = productName;
        this.lastSender = lastSender;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
        this.unreadCount = unreadCount;
        this.productImageResId = productImageResId;
    }

    // Getters
    public String getProductId() { return productId; }
    public String getProductName() { return productName; }
    public String getLastSender() { return lastSender; }
    public String getLastMessage() { return lastMessage; }
    public long getTimestamp() { return timestamp; }
    public int getUnreadCount() { return unreadCount; }
    public int getProductImageResId() { return productImageResId; }
}
