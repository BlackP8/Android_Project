package com.example.demo1.Model.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.demo1.Model.Data;

import java.util.ArrayList;
import java.util.List;

public class DbOperations {
    private Context context;
    private MyDbHelper myDbHelper;
    private SQLiteDatabase database;
    long result = 0;

    public DbOperations(Context context) {
        this.context = context;
    }

    //Открытие бд
    public DbOperations openDb() throws SQLException {
        myDbHelper = new MyDbHelper(context);
        return this;
    }

    //Запись в бд
    public boolean insertDb(boolean signal, String  amount, String type, String note, String date) {
        //Создаем новые строки
        result = 0;
        database = myDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
            contentValues.put(MyConstants.AMOUNT, amount);
            contentValues.put(MyConstants.TYPE, type);
            contentValues.put(MyConstants.NOTE, note);
            contentValues.put(MyConstants.DATE, date);

            //Вставляем строки в таблицу
            if (signal == true) {
                result = database.insert(MyConstants.TABLE_1_NAME, null, contentValues);
            }
            else if (signal == false) {
                result = database.insert(MyConstants.TABLE_2_NAME, null, contentValues);
            }

            if (result == -1) return false;
            else return true;
    }

    //Считываем данные
    public List<Data> getIncomeFromDb() {
        List<Data> data_income = new ArrayList<>();

        database = myDbHelper.getReadableDatabase();
        String columns = "SELECT * FROM " + MyConstants.TABLE_1_NAME;

        //Указываем считывателю откуда считывать
        Cursor cursor = database.rawQuery(columns, null);

        //Создаем цикл для перебора элементов
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(MyConstants._ID));
            String am = cursor.getString(cursor.getColumnIndex(MyConstants.AMOUNT));
            String ty = cursor.getString(cursor.getColumnIndex(MyConstants.TYPE));
            String no = cursor.getString(cursor.getColumnIndex(MyConstants.NOTE));
            String da = cursor.getString(cursor.getColumnIndex(MyConstants.DATE));
            data_income.add(new Data(id, am, ty, no, da));
        }
        cursor.close();
        database.close();
        return data_income;
    }

    //Считываем данные
    public List<Data> getExpenseFromDb() {

        List<Data> data_expense = new ArrayList<>();

        database = myDbHelper.getReadableDatabase();
        String columns = "SELECT * FROM " + MyConstants.TABLE_2_NAME;

        //Указываем считывателю откуда считывать
        Cursor cursor = database.rawQuery(columns, null);

        //Создаем цикл для перебора элементов
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(MyConstants._ID));
            String  am = cursor.getString (cursor.getColumnIndex(MyConstants.AMOUNT));
            String ty = cursor.getString(cursor.getColumnIndex(MyConstants.TYPE));
            String no = cursor.getString(cursor.getColumnIndex(MyConstants.NOTE));
            String da = cursor.getString(cursor.getColumnIndex(MyConstants.DATE));
            data_expense.add(new Data(id, am, ty, no, da));
        }
        cursor.close();
        database.close();
        return data_expense;

    }

    //Добавление пользователя
    public boolean insertUser(String login, String password) {
        //Создаем новые строки
        database = myDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MyConstants.LOGIN, login);
        contentValues.put(MyConstants.PASSWORD, password);
        long ins = database.insert(MyConstants.TABLE_3_NAME, null, contentValues);
        database.close();
        if (ins == -1) return true;
        else return false;
    }

    //Проверка есть уже такой пользователь
    public boolean checkEmail(String login) {
        database = myDbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + MyConstants.TABLE_3_NAME +
                " WHERE login = ? ", new String[] {login});
        if (cursor.getCount() > 0) {
            cursor.close();
            database.close();
            return true;
        }
        else {
            cursor.close();
            database.close();
            return false;
        }
    }

    //Проверяем введенные данные на авторизации
    public boolean checkUser(String password) {
        database = myDbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + MyConstants.TABLE_3_NAME +
                " WHERE password = ? ", new String[] {password});
        if (cursor.getCount() > 0) {
            cursor.close();
            database.close();
            return true;
        }
        else {
            cursor.close();
            database.close();
            return false;
        }
    }

    //Обновляем бд
    public boolean updateDb(boolean signal, int id, String amount, String type, String note, String date) {
        result = 0;
        database = myDbHelper.getWritableDatabase();
        //Указываем какие строки таблицы нужно обновить
        ContentValues contentValues = new ContentValues();
        contentValues.put(MyConstants.AMOUNT, amount);
        contentValues.put(MyConstants.TYPE, type);
        contentValues.put(MyConstants.NOTE, note);
        contentValues.put(MyConstants.DATE, date);;

        if (signal == true) {
                result = database.update(MyConstants.TABLE_1_NAME, contentValues, MyConstants._ID + " = " + id, null);
        }
        else {
                result = database.update(MyConstants.TABLE_2_NAME, contentValues, MyConstants._ID + " = " + id, null);
        }
        database.close();

        if (result == -1) return false;
        else return true;

    }

    //Удаляем выбранные данные
    public boolean deleteData(boolean signal, int id) {
        result = 0;

        database = myDbHelper.getWritableDatabase();

        if (signal == true) {
            result = database.delete(MyConstants.TABLE_1_NAME, MyConstants._ID + " = " + id, null);

        }
        else {
            result = database.delete(MyConstants.TABLE_2_NAME, MyConstants._ID + " = " + id, null);
        }
        database.close();
        if (result == -1) return false;
        else return true;
    }

    //Метод подсчета суммы расходов
    public long expenseSum() {
        result = 0;
        database = myDbHelper.getWritableDatabase();

        //Указываем считывателю откуда считывать
        Cursor cur = database.rawQuery("SELECT SUM(" + MyConstants.AMOUNT +") FROM " + MyConstants.TABLE_2_NAME, null);
        if(cur.moveToFirst())
        {
            result =  cur.getInt(0);
        }
        cur.close();
        database.close();
        return result;
    }

    //Метод подсчета суммы доходов
    public long incomeSum() {
        result = 0;
        database = myDbHelper.getWritableDatabase();

        //Указываем считывателю откуда считывать
        Cursor cur = database.rawQuery("SELECT SUM(" + MyConstants.AMOUNT +") FROM " + MyConstants.TABLE_1_NAME, null);
        if(cur.moveToFirst())
        {
            result =  cur.getInt(0);
        }
        cur.close();
        database.close();
        return result;
    }

    //Обновление пароля
    public void updatePassword(String email, String password) {
        database = myDbHelper.getWritableDatabase();

        //Считываем пароль
        ContentValues contentValues = new ContentValues();
        contentValues.put(MyConstants.PASSWORD, password);

        //Обновляем
        database.update(MyConstants.TABLE_3_NAME, contentValues, MyConstants.LOGIN + " = ?",
                new String[] {email});
        database.close();
    }

    //Закрытие бд
    public void closeDb() {
        myDbHelper.close();
    }
}
