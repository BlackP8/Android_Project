package com.example.demo1.Controller.LoginRegistration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo1.Model.Database.DbOperations;
import com.example.demo1.R;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class LoginActivity extends AppCompatActivity {

    //Поля входа
    private EditText password_log;
    private Button btnLog;
    private String password;

    //Текстовые поля
    private TextView forget_pass;
    private TextView register_here;

    private DbOperations dbOperations;

    private SharedPreferences preferences;
    private final String emptyPin = "pin";

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
        password_log = findViewById(R.id.pin_code);

        btnLog = findViewById(R.id.log_in);
        forget_pass = findViewById(R.id.forgot_password);
        register_here = findViewById(R.id.register_here);

        loadPin();

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
                password = password_log.getText().toString().trim();


                if (TextUtils.isEmpty(password)) {
                    password_log.setError("Введите пароль!");
                    return;
                }

                boolean check_user = dbOperations.checkUser(password);
                if (check_user == true) {
                    Toast.makeText(getApplicationContext(), "Authorization successful", Toast.LENGTH_SHORT).show();
                    savePin();
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                }
                else if (!check_user) {
                    Toast.makeText(getApplicationContext(), "Wrong pin-code. Please, try again.", Toast.LENGTH_SHORT).show();
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


    //Зашифровываем
    private String encrypt(String crypt_pin) {
        return Base64.encodeToString(crypt_pin.getBytes(), Base64.DEFAULT);
    }

    //Дешифровываем
    private String decrypt(String crypt_pin) {
        return new String(Base64.decode(crypt_pin, Base64.DEFAULT));
    }

    //Сохраняем пин в SharedPreferences
    private void savePin() {
        //Задаем имя файла хранения и приоритет доступа
        preferences = getSharedPreferences("Pin", MODE_PRIVATE);
        SharedPreferences.Editor ed = preferences.edit();

        //Записываем значение из текстового поля
        ed.putString(emptyPin, encrypt(password_log.getText().toString()));

        //Уведомляем об изменениях
        ed.apply();
    }

    private void loadPin() {
        preferences = getSharedPreferences("Pin", MODE_PRIVATE);

        //Извлекаем значение
        String savedPin = preferences.getString(emptyPin, "");
        password_log.setText(decrypt(savedPin));
    }
}