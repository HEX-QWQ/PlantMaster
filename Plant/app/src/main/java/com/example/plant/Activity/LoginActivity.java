package com.example.plant.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.plant.R;

import com.example.plant.utils.Constant;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    public static final int MSG_LOGIN_ERR = 1; //登录错误
    public static final int MSG_CONNET_ERR = 2; //网络链接错误
    public static String username;
    private Context context;
    private EditText et_number;
    private EditText et_password;
    private Button bt_login;
    private TextView rl_register;
    private LoginHandler login_handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        InitView();
        login_handler = new LoginHandler();
        Init();
    }
    private void InitView()
    {
        et_number = (EditText)findViewById(R.id.account);
        et_password = (EditText)findViewById(R.id.pwd);
        bt_login = (Button)findViewById(R.id.button);
        rl_register = (TextView) findViewById(R.id.ClickToSignUp);
    }
    private void Init() {
        //设置提示的颜色 37
        et_number.setHintTextColor(getResources().getColor(R.color.black));
        et_password.setHintTextColor(getResources().getColor(R.color.black));

        //登录
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (judge()) {
                    loginInfo();
                }
            }
        });

        //注册
        rl_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "注册", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
    }

            /**登录*/
    private void loginInfo() {
        String userId = et_number.getText().toString();
        String userPassword = et_password.getText().toString();
        //get 请求
        String login_check_url = Constant.LOGIN_URL +"?username="+userId+"&password="+userPassword;
        Log.e("TAG",login_check_url);
        okhttp3.Callback callback = new okhttp3.Callback()
        {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
            String responseData = response.body().string();
            Log.e("LoginActivity","onResponse");
            Log.e("LoginActivity",responseData);
            Log.e("RES",responseData);
            if(responseData.equals("success")) {
                Intent intent1=new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent1);
                finish();
                username=userId;
            }
 else {
                Message msg = new Message();
                msg.what = MSG_LOGIN_ERR;
                login_handler.sendMessage(msg);
            }
        }
            @Override
            public void onFailure(Call call, IOException e) {
            String responseData = "Error!";
            Log.e("LoginActivity","Failure");
            Log.e("LoginActivity",responseData);
            Message msg = new Message();
            msg.what = MSG_CONNET_ERR;
            login_handler.sendMessage(msg);

        }
        };
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(login_check_url)
                .build();
        //发送请求 126
        client.newCall(request).enqueue(callback);
    }

    //判断登录信息是否合法 130
    private boolean judge() {
        if (TextUtils.isEmpty(et_number.getText().toString()) ) {
            Toast.makeText(context, "用户名不能为空",Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(et_password.getText().toString())) {
            Toast.makeText(context, "密码不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
            /**
             * 事件捕获
             */
    class LoginHandler extends Handler {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case MSG_LOGIN_ERR:
                    et_number.setText("");
                    et_password.setText("");
                    et_number.requestFocus();
                    new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("注意")
                        .setMessage("用户名或密码输入不正确，请重新输入")
                        .setPositiveButton("确定",null)
                        .create()
                        .show();
                    bt_login.setEnabled(true);
                    break;
                case MSG_CONNET_ERR:
                    new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("注意")
                        .setMessage("网络连接错误，请检查网络")
                        .setPositiveButton("确定",null)
                        .create()
                        .show();
                    break;
            }
        }
    }
}