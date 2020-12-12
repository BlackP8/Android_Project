package com.example.demo1.Controller.LoginRegistration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo1.Model.Data.DbOperations;
import com.example.demo1.R;

public class ResetPassActivity extends AppCompatActivity {

    private EditText email_res;
    private EditText new_pass;
    private EditText repeat_pass;
    private Button reset;
    private TextView next;

    private DbOperations dbOperations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);

        email_res = findViewById(R.id.email_res);
        new_pass = findViewById(R.id.new_password);
        repeat_pass = findViewById(R.id.new_password_repeat);
        reset = findViewById(R.id.btn_reset);
        next = findViewById(R.id.alr_acc);

        dbOperations = new DbOperations(getApplicationContext());
        dbOperations.openDb();

        email_res.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) email_res.setHint("");
                else email_res.setHint("Input your email");
            }
        });

        new_pass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) new_pass.setHint("");
                else new_pass.setHint("Input new password");
            }
        });

        repeat_pass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) repeat_pass.setHint("");
                else repeat_pass.setHint("Confirm password");
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPass();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }

    //Сброс пароля
    public void resetPass() {
        String pass = new_pass.getText().toString().trim();
        String pass_repeat = repeat_pass.getText().toString().trim();
        String email = email_res.getText().toString().trim();

        //Проверем заполнение полей
        if (pass.isEmpty() || pass_repeat.isEmpty() || email.isEmpty()) {
            new_pass.setError("Введите пароль!");
            repeat_pass.setError("Повторите пароль!");
            email_res.setError("Введите логин!");
        }

        //Сравниваем пароли
        if (!pass.contentEquals(pass_repeat)) {
            Toast.makeText(getApplicationContext(), "Password does not match!", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean check_email = dbOperations.checkEmail(email);
        //Проверяем логин
        if (!check_email) {
            Toast.makeText(getApplicationContext(), "User does not exist!", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            dbOperations.updatePassword(email, pass);
            Toast.makeText(getApplicationContext(), "Reset successful!", Toast.LENGTH_SHORT).show();
            dbOperations.closeDb();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }

    }
}