package com.example.jewelrypurchase.ui.personCenter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jewelrypurchase.R;
import com.example.jewelrypurchase.jpWeb.Result;
import com.example.jewelrypurchase.jpWeb.User;
import com.example.jewelrypurchase.jpWeb.WebUrl;
import com.example.jewelrypurchase.jpWeb.util.MD5;
import com.example.jewelrypurchase.ui.MainActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;

import io.noties.markwon.Markwon;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TGA = "loginResult";
    private CheckBox loginAgreementCheckBox;
    private TextView loginButton;
    private EditText phoneEditText;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private OkHttpClient okHttpClient;
    private OkHttpClient okHttpClient2;
    private Handler handler = new Handler(Looper.getMainLooper());
    private MD5 en_md5;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 隐藏标题栏
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // 隐藏状态栏和导航栏（全屏）
        getWindow().getDecorView().setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_FULLSCREEN |          // 隐藏状态栏
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION    // 隐藏导航栏
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY  // 沉浸式粘性模式（滑动后自动隐藏）
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE     // 保持布局稳定
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN // 内容延伸到状态栏区域
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION // 内容延伸到导航栏区域
        );

        loginAgreementCheckBox = findViewById(R.id.login_agreement_CheckBox);
        loginButton = findViewById(R.id.loginButton);
        phoneEditText = findViewById(R.id.emailEditText);
        usernameEditText = findViewById(R.id.loginUserEditText);
        passwordEditText = findViewById(R.id.loginPasswordEditText);
        checkBox = findViewById(R.id.login_agreement_CheckBox);

        checkBox.setChecked(false);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBox.isChecked()) {
                    onClickloginButton();
                    overridePendingTransition(0, R.anim.fade_out);
                } else {
                    Toast.makeText(LoginActivity.this, "请先同意《隐私政策》和《用户协议》", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 检查网络连接状态
        if (!isNetworkConnected(getApplicationContext())) {
            showPrivacyPolicyDialog("提示", "请检查网络连接！", "确定");
        }
    }

    // 是否有网络连接
    public boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    // 点击隐私政策
    public void clickLoginTerms(View view) {
        myShowDialog("隐私政策", getPrivacyPolicyText());
    }

    // 点击用户协议
    public void clickUseProtocol(View view) {
        myShowDialog("用户协议", getUseProtocolText());
    }

    // 登录判断
    private void onClickloginButton() {
        if (phoneEditText.getText().length() == 11) {
            if (passwordEditText.getText().length() >= 8 && passwordEditText.getText().length() <= 16) {
                if (usernameEditText.getText().length() > 1 && usernameEditText.getText().length() <= 8) {
                    okHttpClient = new OkHttpClient.Builder().build();
                    String enPassword = en_md5.enMd5(String.valueOf(passwordEditText.getText()));
                    String searchUserUrl = new WebUrl().getBASE_URL() + "/api/login?username=" + usernameEditText.getText()
                            + "&password=" + enPassword
                            + "&phone=" + phoneEditText.getText();
                    String registerUrl = new WebUrl().getBASE_URL() + "/api/register?username=" + usernameEditText.getText()
                            + "&password=" + enPassword
                            + "&phone=" + phoneEditText.getText();
                    Request RcarouselUrl = new Request.Builder()
                            .url(searchUserUrl)
                            .build();
                    Request RcarouselUrl2 = new Request.Builder()
                            .url(registerUrl)
                            .build();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Response response = okHttpClient.newCall(RcarouselUrl).execute();
                                Type type = new TypeToken<Result<User>>() {
                                }.getType();
                                Result<User> loginToast = new Gson().fromJson(response.body().string(), type);

                                if (loginToast.getCode().equals("502")) {
                                    Response response2 = okHttpClient.newCall(RcarouselUrl2).execute();
                                    Result<User> registerToast = new Gson().fromJson(response2.body().string(), type);

                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (registerToast.getCode().equals("501")) {
                                                Toast.makeText(LoginActivity.this, "账号密码错误", Toast.LENGTH_SHORT).show();
                                            } else if (registerToast.getCode().equals("505")) {
                                                Toast.makeText(LoginActivity.this, "登录失败，请检查用户名和手机号是否正确", Toast.LENGTH_SHORT).show();
                                            } else if (registerToast.getCode().equals("201")) {
                                                loginSuccess(registerToast.getToken(), usernameEditText.getText().toString(), String.valueOf(registerToast.getData().getUserId()));
                                            } else {
                                                Toast.makeText(LoginActivity.this, "服务器错误", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else if (loginToast.getCode().equals("200")) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            User loginUser = loginToast.getData();
                                            Log.e("loginUser", String.valueOf(loginUser.getUserId()));
                                            loginSuccess(loginToast.getToken(), usernameEditText.getText().toString(), String.valueOf(loginUser.getUserId()));
                                        }
                                    });
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } else {
                    Toast.makeText(this, "用户名至少2个字符", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "密码由8至16数字、英文或符号组成", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "手机号格式错误", Toast.LENGTH_SHORT).show();
        }

    }

    // 登录成功
    public void loginSuccess(String token, String username, String id) {
        // 获取 SharedPreferences 对象
        SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);

        // 获取 Editor 对象
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // 保存用户信息
        editor.putString("username", username);
        editor.putString("token", token); // token：唯一，仅保存在客户端作为验证手段
        editor.putString("userId", id);

        // 提交保存（apply() 是异步的，commit() 是同步的）
        editor.apply();

        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();

        // 创建一个新的Intent对象
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        // 销毁之前所有界面
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        finish(); // 关闭当前Activity
        startActivity(intent); // 重新启动Activity
    }

    // 隐私政策对话框
    public void myShowDialog(String title, String content) {
        // 创建一个AlertDialog.Builder对象
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 设置对话框的标题
        builder.setTitle(title);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_privacy_policy, null);
        TextView privacyPolicyTextView = dialogView.findViewById(R.id.privacy_policy_text);
        privacyPolicyTextView.setText(content);
        // 初始化Markwon
        Markwon markwon = Markwon.builder(this).build();
        // 解析并设置到TextView
        markwon.setMarkdown(privacyPolicyTextView, content);
        builder.setView(dialogView);

        // 设置对话框的按钮
        builder.setPositiveButton("同意", (dialog, which) -> {
            loginAgreementCheckBox.setChecked(true);
        });
        builder.setNegativeButton("取消", (dialog, which) -> {
            loginAgreementCheckBox.setChecked(false);
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private String getPrivacyPolicyText() {
        // 返回隐私政策的文本内容
        return "## 1. 概述\n" +
                "我们重视您的隐私，并承诺保护您的个人信息。本隐私政策描述了我们如何收集、使用和保护您的信息。您在使用我们的服务时需要同意本隐私政策。\n\n" +
                "## 2. 我们收集的信息\n" +
                "我们可能会收集以下类型的信息：\n" +
                "- 个人身份信息：包括但不限于您的姓名、电子邮件地址、电话号码、邮寄地址等。\n" +
                "- 账户信息：包括您的用户名、密码和账户设置。\n" +
                "- 交易信息：包括购买记录、支付信息、发票和送货地址。\n" +
                "- 使用数据：包括您的IP地址、浏览器类型、访问时间、浏览历史等。\n" +
                "- 设备信息：包括设备类型、操作系统、唯一设备标识符等。\n\n" +
                "## 3. 信息的使用\n" +
                "我们使用收集的信息用于以下目的：\n" +
                "- 提供和维护服务：确保我们的服务正常运行并提供您所需的功能。\n" +
                "- 处理交易：处理您的订单、支付和送货。\n" +
                "- 客户支持：解决您的问题和提供客户服务。\n" +
                "- 改进服务：分析使用数据以改进我们的服务和用户体验。\n" +
                "- 营销和通信：发送促销信息、更新和其他相关通信（如果您选择接收）。\n" +
                "- 法律合规：遵守法律法规和政府要求。\n\n" +
                "## 4. 信息的共享\n" +
                "我们不会向第三方出售、交易或转让您的个人信息，除非在以下情况下：\n" +
                "- 获得您的明确同意。\n" +
                "- 遵守法律法规或政府要求。\n" +
                "- 保护我们的权利、财产或安全。\n" +
                "- 向我们的服务提供商共享信息，这些提供商代表我们处理信息并需要遵守严格的保密义务。\n\n" +
                "## 5. 信息的保护\n" +
                "我们采取各种安全措施以保护您的个人信息免遭未经授权的访问、使用或泄露。这些措施包括但不限于数据加密、访问控制和定期安全审计。\n\n" +
                "## 6. 您的权利\n" +
                "您有权访问、更正或删除您的个人信息，并可以随时联系我们以行使这些权利。您也可以选择退出营销通信。\n\n" +
                "## 7. 隐私政策的变更\n" +
                "我们可能会不时更新本隐私政策。任何更改将在本页面上发布，并在更改生效前通过电子邮件或其他显著方式通知您。\n\n" +
                "## 8. 联系我们\n" +
                "如果您对本隐私政策有任何疑问或需要进一步信息，请通过以下方式联系我们：\n" +
                "- 电子邮件：2049898109@qq.com\n";
    }

    private String getUseProtocolText() {
        return "# **“珠宝购”用户服务协议**\n" +
                "\n" +
                "**生效日期**：2025年04月20日\n" +
                "\n" +
                "---\n" +
                "\n" +
                "### **一、协议确认与接受**\n" +
                "\n" +
                "1. 您在使用“珠宝购”平台（以下简称“本平台”）前，请务必仔细阅读并充分理解本协议的全部内容。\n" +
                "  \n" +
                "2. 当您完成注册、登录或使用本平台服务时，即视为已**完全同意并接受**本协议及平台相关规则（包括隐私政策、交易规则等）。\n" +
                "  \n" +
                "\n" +
                "---\n" +
                "\n" +
                "### **二、用户注册与账户管理**\n" +
                "\n" +
                "1. **实名认证**：\n" +
                "  \n" +
                "  - 用户需提供真实姓名、身份证号、联系方式等完成实名认证，否则无法进行交易。\n" +
                "    \n" +
                "  - 企业用户需提供营业执照及法定代表人信息。\n" +
                "    \n" +
                "2. **账户安全**：\n" +
                "  \n" +
                "  - 您需对账户密码、验证码等信息保密，因个人泄露导致的损失由用户自行承担。\n" +
                "3. **账户注销**：\n" +
                "  \n" +
                "  - 注销账户需清偿所有交易款项及纠纷，注销后历史记录可能无法恢复。\n" +
                "\n" +
                "---\n" +
                "\n" +
                "### **三、交易规则**\n" +
                "\n" +
                "1. **商品发布**：\n" +
                "  \n" +
                "  - 您需保证发布的珠宝信息（如成色、材质、证书等）真实、合法，禁止伪造鉴定证书或虚假描述。\n" +
                "    \n" +
                "  - 禁止交易仿制品、赃物、侵权商品等违禁物品。\n" +
                "    \n" +
                "2. **交易流程**：\n" +
                "  \n" +
                "  - 买家支付款项后，资金由支付宝平台托管，确认收货且无纠纷后结算给卖家。\n" +
                "    \n" +
                "  - 若交易争议（如真伪问题），平台有权冻结资金并要求提供鉴定证明。\n" +
                "    \n" +
                "3. **退换货政策**：\n" +
                "  \n" +
                "  - 买家需在签收后**48小时内**提交退换货申请，并提供有效证据（如专业机构鉴定报告）。\n" +
                "    \n" +
                "  - 定制商品、已损坏商品或描述一致的二手商品不支持无理由退货。\n" +
                "    \n" +
                "\n" +
                "---\n" +
                "\n" +
                "### **四、真伪验证与责任声明**\n" +
                "\n" +
                "1. **平台角色**：\n" +
                "  \n" +
                "  - 本平台仅提供信息发布及交易撮合服务，**不参与实际交易**，亦不对商品质量、真伪承担保证责任。\n" +
                "2. **鉴定建议**：\n" +
                "  \n" +
                "  - 建议交易双方通过**权威鉴定机构**（如NGTC、GIA等）验证商品真伪，相关费用由用户自理。\n" +
                "3. **争议解决**：\n" +
                "  \n" +
                "  - 若买卖双方无法协商一致，平台可介入调解，但调解结果对双方无强制约束力。\n" +
                "\n" +
                "---\n" +
                "\n" +
                "### **五、隐私与数据保护**\n" +
                "\n" +
                "1. **信息收集**：\n" +
                "  \n" +
                "  - 为完成交易，您需授权平台收集必要信息（如联系方式、地址、交易记录等），详见《隐私政策》。\n" +
                "2. **数据使用**：\n" +
                "  \n" +
                "  - 未经用户同意，平台不会向第三方出售用户数据，但依法向监管部门提供的情形除外。\n" +
                "\n" +
                "---\n" +
                "\n" +
                "### **六、免责条款**\n" +
                "\n" +
                "1. 平台不对以下情况承担责任：\n" +
                "  \n" +
                "  - 因不可抗力（如黑客攻击、系统故障）导致的服务中断或数据丢失。\n" +
                "    \n" +
                "  - 用户间私下交易产生的纠纷或损失。\n" +
                "    \n" +
                "  - 珠宝在物流过程中损坏、丢失（建议用户投保并留存凭证）。\n" +
                "    \n" +
                "\n" +
                "---\n" +
                "\n" +
                "### **七、协议修改与终止**\n" +
                "\n" +
                "1. 平台有权根据法律法规或业务调整修改本协议，修改后将通过公告、站内信等方式通知。\n" +
                "  \n" +
                "2. 若您继续使用服务，视为接受修改后的协议；否则应停止使用并注销账户。\n" +
                "  \n" +
                "\n" +
                "---\n" +
                "\n" +
                "### **八、法律适用与争议管辖**\n" +
                "\n" +
                "1. 本协议适用中华人民共和国法律。\n" +
                "  \n" +
                "2. 因本协议产生的争议，双方应协商解决；协商不成，可向**平台所在地有管辖权的人民法院**提起诉讼。\n" +
                "  \n" +
                "\n" +
                "---\n" +
                "\n" +
                "### **九、联系我们**\n" +
                "\n" +
                "- 反馈邮箱：2049898109@qq.com\n" +
                "  \n" +
                "\n" +
                "---\n" +
                "\n" +
                "**附：重点提示**\n" +
                "\n" +
                "- **交易风险**：二手珠宝存在成色差异、市场波动等风险，请谨慎评估。\n" +
                "  \n" +
                "- **资金安全**：请勿脱离平台进行线下交易或直接转账，否则无法享受平台保障。\n" +
                "  \n" +
                "\n" +
                "---\n" +
                "\n" +
                "**请务必完整阅读并理解本协议。**";
    }

    // 对话框
    public void showPrivacyPolicyDialog(String d_title, String d_mytext, String btu1) {
        // 创建一个AlertDialog.Builder对象
        AlertDialog.Builder builder2 = new AlertDialog.Builder(LoginActivity.this);

        // 设置对话框的标题
        builder2.setTitle(d_title);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_privacy_policy, null);
        TextView privacyPolicyTextView = dialogView.findViewById(R.id.privacy_policy_text);
        privacyPolicyTextView.setText(d_mytext);
        builder2.setView(dialogView);
        // 设置点击阴影对话框不可取消
        builder2.setCancelable(false);

        // 设置对话框的按钮
        builder2.setPositiveButton(btu1, (dialog, which) -> {
            System.exit(0);
        });

        AlertDialog dialog2 = builder2.create();
        dialog2.show();
    }
}