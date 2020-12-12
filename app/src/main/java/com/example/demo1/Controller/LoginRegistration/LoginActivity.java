package com.example.demo1.Controller.LoginRegistration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo1.Model.Data.DbOperations;
import com.example.demo1.R;

public class LoginActivity extends AppCompatActivity {

    //Поля входа
    private EditText email_log;
    private EditText password_log;
    private Button btnLog;

    //Текстовые поля
    private TextView forget_pass;
    private TextView register_here;

    private DbOperations dbOperations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dbOperations = new DbOperations(getApplicationContext());
        dbOperations.openDb();
        login();
    }

    //Ввод данных пользователя для входа
    private void login() {
        email_log = findViewById(R.id.username_log);
        password_log = findViewById(R.id.password_log);
        btnLog = findViewById(R.id.log_in);
        forget_pass = findViewById(R.id.forgot_password);
        register_here = findViewById(R.id.register_here);

        email_log.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
               if (hasFocus) email_log.setHint("");
               else email_log.setHint("Email");
            }
        });

        password_log.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) password_log.setHint("");
                else password_log.setHint("Password");
            }
        });

        //Обработка кнопки вход
        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = email_log.getText().toString().trim();
                String password = password_log.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    email_log.setError("Введите email!");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    password_log.setError("Введите пароль!");
                    return;
                }

                boolean check_user = dbOperations.checkUser(email, password);
                if (check_user == true) {
                    Toast.makeText(getApplicationContext(), "Authorization successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                }
                else if (!check_user) {
                    Toast.makeText(getApplicationContext(), "Wrong email or password. Please, try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Обработка поля регистрации
        register_here.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
            }
        });

        //Обработка поля забыл пароль
        forget_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ResetPassActivity.class));
            }
        });
    }
}