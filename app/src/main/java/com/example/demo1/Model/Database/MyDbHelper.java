package com.example.demo1.Model.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDbHelper extends SQLiteOpenHelper {

    //Инициализируем создание бд с определенным именем и   версией
    public MyDbHelper(@Nullable Context context) {
        super(context, MyConstants.DATABASE_NAME, null, MyConstants.DATABASE_VERSION);
    }

    //Метод, работающий при первом создании бд
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Передаем команду на создание таблиц
        db.execSQL(MyConstants.CREATE_TABLE_1_STRUCTURE);
        db.execSQL(MyConstants.CREATE_TABLE_2_STRUCTURE);
        db.execSQL(MyConstants.CREATE_TABLE_3_STRUCTURE);
    }

    //Вызывается при изменении бд
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Передаем команду на удаление
        db.execSQL(MyConstants.DELETE_TABLE_1_STRUCTURE);
        db.execSQL(MyConstants.DELETE_TABLE_2_STRUCTURE);
        db.execSQL(MyConstants.DELETE_TABLE_3_STRUCTURE);
        onCreate(db);
    }
}
