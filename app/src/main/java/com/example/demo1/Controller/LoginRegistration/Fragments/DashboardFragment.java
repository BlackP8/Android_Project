package com.example.demo1.Controller.LoginRegistration.Fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo1.Model.Data;
import com.example.demo1.Model.Database.DbOperations;
import com.example.demo1.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment  {

    //Активная кнопка
    private FloatingActionButton float_main_btn;
    private FloatingActionButton float_income_btn;
    private FloatingActionButton float_expense_btn;

    //Текст кнопки
    private TextView float_income_txt;
    private TextView float_expense_txt;
    private boolean isOpen = false;

    //Календарь
    private TextView date_tv;
    private Calendar calendar;
    private int day, month, year;

    //Кнопка закрытия диалоговых окон
    private ImageView cross_close;

    //Сумма
    private TextView income_dash_sum;
    private TextView expense_dash_sum;
    private long dash_sum = 0;

    //Анимация кнопки
    private Animation fadOpen, fadClose;

    private boolean add_data = true;

    //Recycler
    private DashboardRecyclerAdapter dashboardRecyclerAdapter;
    private RecyclerView recyclerIncome;
    private RecyclerView recyclerExpense;
    private Context context;
    private DbOperations dbOperations;
    public List<Data> values = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        context = myView.getContext();

        income_dash_sum = myView.findViewById(R.id.income_set_res);
        expense_dash_sum = myView.findViewById(R.id.expense_set_res);


        //Присоединяем историю операций
        recyclerIncome = myView.findViewById(R.id.rlr_dash_income);
        recyclerIncome.setHasFixedSize(true);
        recyclerExpense = myView.findViewById(R.id.rlr_dash_expense);
        recyclerExpense.setHasFixedSize(true);

        //Привязываем активную кнопку к экрану
        float_main_btn = myView.findViewById(R.id.float_main_btn);
        float_income_btn = myView.findViewById(R.id.income_float_btn);
        float_expense_btn = myView.findViewById(R.id.expense_float_btn);

        //Привязываем текст кнопки
        float_income_txt = myView.findViewById(R.id.income_text);
        float_expense_txt = myView.findViewById(R.id.expense_text);


        //Присоединяем анимацию
        fadOpen = AnimationUtils.loadAnimation(getActivity(), R.anim.float_open);
        fadClose = AnimationUtils.loadAnimation(getActivity(), R.anim.float_close);


        float_main_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addOperation();
                floatBtnAnimation ();
            }
        });


        //Прописываем логику отображения истории операций
        dbOperations = new DbOperations(context);
        dbOperations.openDb();
        incomeDashSummary();
        expenseDashSummary();
        values = dbOperations.getIncomeFromDb();
        LinearLayoutManager layoutManagerIncome = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        layoutManagerIncome.setStackFromEnd(true);
        layoutManagerIncome.setReverseLayout(true);
        recyclerIncome.setLayoutManager(layoutManagerIncome);
        dashboardRecyclerAdapter = new DashboardRecyclerAdapter(values);
        recyclerIncome.setAdapter(dashboardRecyclerAdapter);

        values = dbOperations.getExpenseFromDb();
        LinearLayoutManager layoutManagerExpense = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        layoutManagerExpense.setStackFromEnd(true);
        layoutManagerExpense.setReverseLayout(true);
        recyclerExpense.setLayoutManager(layoutManagerExpense);
        dashboardRecyclerAdapter = new DashboardRecyclerAdapter(values);
        recyclerExpense.setAdapter(dashboardRecyclerAdapter);

        return myView;
    }

    //Анимация исчезновения кнопок доходы и расходы после добавления данных
    private void floatBtnAnimation () {

        if (isOpen) {
            float_income_btn.startAnimation(fadClose);
            float_expense_btn.startAnimation(fadClose);
            float_income_btn.setClickable(false);
            float_expense_btn.setClickable(false);

            float_income_txt.startAnimation(fadClose);
            float_expense_txt.startAnimation(fadClose);
            float_income_txt.setClickable(false);
            float_expense_txt.setClickable(false);
            isOpen = false;
        }
        else {
            float_income_btn.startAnimation(fadOpen);
            float_expense_btn.startAnimation(fadOpen);
            float_income_btn.setClickable(true);
            float_expense_btn.setClickable(true);

            float_income_txt.startAnimation(fadOpen);
            float_expense_txt.startAnimation(fadOpen);
            float_income_txt.setClickable(true);
            float_expense_txt.setClickable(true);
            isOpen = true;
        }
    }

    private void addOperation() {
        //Кнопка доходов
        float_income_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                incomeDataInput();
            }
        });

        //Кнопка расходов
        float_expense_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                expenseDataInput();
            }
        });
    }

    //Записываем сумму в текствью
    private void incomeDashSummary() {
        dash_sum = dbOperations.incomeSum();
        income_dash_sum.setText("+" + String.valueOf(dash_sum));
    }

    //Записываем сумму в текствью
    private void expenseDashSummary() {
        dash_sum = dbOperations.expenseSum();
        expense_dash_sum.setText("-" + String.valueOf(dash_sum));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public TextView getData(final TextView date_tv) {
        calendar = Calendar.getInstance();

        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        month = month + 1;

        date_tv.setText(day + "." + month + "." + year);

        return date_tv;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void incomeDataInput () {
        //Создаем диалоговое окно
        AlertDialog.Builder mydialog = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        //Создаем объект вида нашего окна
        View myview = inflater.inflate(R.layout.input_dialog_layout, null);

        mydialog.setView(myview);
        final AlertDialog dialog = mydialog.create();

        //Если нажать на пространство вне окна, оно закроется
        dialog.setCancelable(false);

        //Задаем поля окна
        final EditText amount_ed = myview.findViewById(R.id.amount_txt);
        final EditText type_ed = myview.findViewById(R.id.type_txt);
        final EditText note_ed = myview.findViewById(R.id.note_txt);
        date_tv = myview.findViewById(R.id.date_txt);


        date_tv = getData(date_tv);

        date_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month +1;
                        date_tv.setText(dayOfMonth + "." + month + "." + year);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        //Задаем кнопки окна
        Button btnSave = myview.findViewById(R.id.btn_save);
        cross_close = myview.findViewById(R.id.close_input_dialog);

        //Создаем обработчик событий для кнопки сохранить
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Записываем значения полей в строки
                String type = type_ed.getText().toString().trim();
                String amount = "+" + amount_ed.getText().toString().trim();
                String note = note_ed.getText().toString().trim();
                String date = date_tv.getText().toString().trim();

                if (TextUtils.isEmpty(type)) {
                    type_ed.setError("Вы ничего не ввели!");
                    return;
                }

                if (TextUtils.isEmpty(amount)) {
                    amount_ed.setError("Вы ничего не ввели!");
                    return;
                }
                add_data = dbOperations.insertDb(true, amount, type, note, date);

                //Обработка ошибок
                if (add_data == true) {
                    Toast.makeText(getContext(), "Added successfully", Toast.LENGTH_SHORT).show();
                    values = dbOperations.getIncomeFromDb();
                    dashboardRecyclerAdapter = new DashboardRecyclerAdapter(values);
                    recyclerIncome.setAdapter(dashboardRecyclerAdapter);
                    incomeDashSummary();
                }
                else {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                }

                dialog.dismiss();
            }
        });

        //Обработчик кнопки закрытия
        cross_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void expenseDataInput () {
        //Создаем диалоговое окно
        AlertDialog.Builder mydialog = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        //Создаем объект вида нашего окна
        View myview = inflater.inflate(R.layout.input_dialog_layout, null);

        mydialog.setView(myview);
        final AlertDialog dialog = mydialog.create();

        //Если нажать на пространство вне окна, оно закроется
        dialog.setCancelable(false);

        //Задаем поля окна
        final EditText amount_exp = myview.findViewById(R.id.amount_txt);
        final EditText type_exp = myview.findViewById(R.id.type_txt);
        final EditText note_exp = myview.findViewById(R.id.note_txt);
        final TextView date_ed = myview.findViewById(R.id.date_txt);
        date_tv = myview.findViewById(R.id.date_txt);


        date_tv = getData(date_tv);

        date_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month +1;
                        date_tv.setText(dayOfMonth + "." + month + "." + year);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        //Задаем кнопки окна
        Button btnSave = myview.findViewById(R.id.btn_save);
        cross_close = myview.findViewById(R.id.close_input_dialog);

        //Создаем обработчик событий для кнопки сохранить
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Записываем значения полей в строки
                String expense_type = type_exp.getText().toString().trim();
                String expense_amount = "-" + amount_exp.getText().toString().trim();
                String expense_note = note_exp.getText().toString().trim();
                String expense_date = date_ed.getText().toString().trim();

                //Проверка ввода для полей
                if (TextUtils.isEmpty(expense_date)) {
                    date_ed.setError("Вы ничего не ввели!");
                    return;
                }

                if (TextUtils.isEmpty(expense_type)) {
                    type_exp.setError("Вы ничего не ввели!");
                    return;
                }

                if (TextUtils.isEmpty(expense_amount)) {
                    amount_exp.setError("Вы ничего не ввели!");
                    return;
                }

                add_data = dbOperations.insertDb(false, expense_amount, expense_type, expense_note, expense_date);

                //Обработка ошибок
                if (add_data == true) {
                    Toast.makeText(getContext(), "Added successfully", Toast.LENGTH_SHORT).show();
                    values = dbOperations.getExpenseFromDb();
                    dashboardRecyclerAdapter = new DashboardRecyclerAdapter(values);
                    recyclerExpense.setAdapter(dashboardRecyclerAdapter);
                    expenseDashSummary();
                }
                else {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                }

                dialog.dismiss();
            }
        });

        //Обработчик кнопки закрытия
        cross_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}