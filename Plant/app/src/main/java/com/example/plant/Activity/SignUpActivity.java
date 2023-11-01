package com.example.plant.Activity;

import static com.example.plant.utils.Constant.SignUp_URL;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.plant.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignUpActivity extends AppCompatActivity {

    private EditText etId, etUsername, etPassword;
    private Button btnRegister;

    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etId = findViewById(R.id.Sname);
        etUsername = findViewById(R.id.SignUpAccount);
        etPassword = findViewById(R.id.SignUpPassword);

        btnRegister = findViewById(R.id.SignUpButton);

        client = new OkHttpClient();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = etId.getText().toString();
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                // 向服务端发送注册请求
                registerUser(id, username, password);
            }
        });
    }

    private void registerUser(String id, String username, String password) {
        // 创建请求体
        RequestBody requestBody = new FormBody.Builder()
                .add("id", id)
                .add("username", username)
                .add("password", password)
                .build();

        // 创建请求
        Request request = new Request.Builder()
                .url(SignUp_URL)
                .post(requestBody)
                .build();

        // 发送请求
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SignUpActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseData = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (responseData.equals("success")) {
                            Toast.makeText(SignUpActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(SignUpActivity.this, "注册失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });
    }
}
