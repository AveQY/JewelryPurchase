plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.jewelrypurchase"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.jewelrypurchase"
        minSdk = 28
        targetSdk = 33
        versionCode = 1
        versionName = "1.0.5"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        release {
            isMinifyEnabled = false  // 启用代码混淆
//            proguardFiles(
//                getDefaultProguardFile("proguard-android-optimize.txt"),
//                "proguard-rules.pro"
//            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }

}



dependencies {
    // 添加Glide依赖
    implementation("com.github.bumptech.glide:glide:4.12.0")
    // 添加annotations依赖
    implementation("androidx.annotation:annotation:1.3.0")
    // MySQL JDBC驱动
    implementation(files("libs/mysql-connector-j-8.4.0.jar"))
    // Retrofit库来与Spring Boot后端进行通信
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.google.code.gson:gson:2.8.6")
    // OkHttp
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")
    implementation("com.alibaba:fastjson:1.2.62")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    // release
    implementation("io.opentelemetry:opentelemetry-api:1.20.0")
    implementation("io.opentelemetry:opentelemetry-sdk:1.20.0")
    implementation("io.opentelemetry:opentelemetry-exporter-logging:1.20.0")
    implementation("javax.xml.stream:stax-api:1.0-2")
    // Markdown
    implementation ("io.noties.markwon:core:4.6.2")        // 核心库
    implementation ("io.noties.markwon:html:4.6.2")        // 支持HTML
    implementation ("io.noties.markwon:image:4.6.2")       // 支持图片（需配合图片加载库）
    implementation ("io.noties.markwon:linkify:4.6.2")
    // Other
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.recyclerview)
    implementation(libs.swiperefreshlayout)
    implementation(libs.activity)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

}