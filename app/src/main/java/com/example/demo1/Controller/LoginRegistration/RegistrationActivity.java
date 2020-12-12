package com.example.demo1.Controller.LoginRegistration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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

public class RegistrationActivity extends AppCompatActivity {

    //Регистрационные данные
    private EditText email_reg;
    private EditText password_reg;
    private Button btnReg;
    //Поле уже зарегистрированы
    private TextView register_alr;

    private DbOperations dbOperations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        dbOperations = new DbOperations(getApplicationContext());
        dbOperations.openDb();
        registration();
    }

    //Регистрация пользователя
    private void registration () {
        email_reg = findViewById(R.id.username_reg);
        password_reg = findViewById(R.id.password_reg);
        btnReg = findViewById(R.id.btn_reg);
        register_alr = findViewById(R.id.alr_acc);

        email_reg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) email_reg.setHint("");
                else email_reg.setHint("Email");
            }
        });

        password_reg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) password_reg.setHint("");
                else password_reg.setHint("Password");
            }
        });

        //Обработчик кнопки регистрации
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = email_reg.getText().toString().trim();
                String password = password_reg.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    email_reg.setError("Введите email!");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    password_reg.setError("Введите пароль!");
                    return;
                }

                boolean check_email = dbOperations.checkEmail(email);
                if (!check_email) {
                    boolean insert_user = dbOperations.insertUser(email, password);
                    if (insert_user == false) {
                        Toast.makeText(getApplicationContext(), "Registration successful", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (check_email == true) {
                    Toast.makeText(getApplicationContext(), "User already exists", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //Обработчик поля уже зарегистрирован
        register_alr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbOperations.closeDb();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }
}