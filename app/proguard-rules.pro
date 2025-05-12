

-keep class com.alibaba.fastjson.** { *; }
-dontwarn com.alibaba.fastjson.**

-keep class io.opentelemetry.** { *; }
-dontwarn io.opentelemetry.**

-keep class com.mysql.cj.** { *; }
-dontwarn com.mysql.cj.**

# 保留 Retrofit/OkHttp
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# 保留 Gson 反序列化需要的无参构造方法
-keepclassmembers class com.example.jewelrypurchase.models.** {
    public <init>();
}

# 保留 Gson 注解（如 @SerializedName）
-keepattributes Signature
-keepattributes *Annotation*

