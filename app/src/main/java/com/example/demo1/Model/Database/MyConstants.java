package com.example.demo1.Model.Database;

public class MyConstants {

    //Задаем названия столбцов таблиц
    public static final String TABLE_1_NAME = "Income";
    public static final String TABLE_2_NAME = "Expense";
    public static final String TABLE_3_NAME = "Users";
    public static final String _ID = "_id";
    public static final String LOGIN = "Login";
    public static final String PASSWORD = "Password";
    public static final String AMOUNT = "Amount";
    public static final String TYPE = "Type";
    public static final String NOTE = "Note";
    public static final String DATE = "Date";
    public static final String DATABASE_NAME = "bill.db";
    /* Версия нужна для фиксации изменений в структуре бд.
       Например при изменении или добавлении колонны необходимо
       присвоить номеру версии другое значение*/
    public static final int DATABASE_VERSION = 1;

    //Константа, создающая структуру первой таблицы
    public static final String CREATE_TABLE_1_STRUCTURE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_1_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY, " +
                    AMOUNT + " INTEGER, " + TYPE + " TEXT, " +
                    NOTE + " TEXT, " + DATE + " TEXT" + ");";

    //Константа, создающая структуру второй таблицы
    public static final String CREATE_TABLE_2_STRUCTURE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_2_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY, " +
                    AMOUNT + " INTEGER, " + TYPE + " TEXT, " +
                    NOTE + " TEXT, " + DATE + " TEXT" + ");";

    //Константа, создающая структуру третьей таблицы
    public static final String CREATE_TABLE_3_STRUCTURE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_3_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY, " +
                    LOGIN + " TEXT, " + PASSWORD + " TEXT" + ");";

    //Константа, удаляющая структуру первой таблицы
    public static final String DELETE_TABLE_1_STRUCTURE =
            "DROP TABLE IF EXISTS " + TABLE_1_NAME;

    //Константа, удаляющая структуру второй таблицы
    public static final String DELETE_TABLE_2_STRUCTURE =
            "DROP TABLE IF EXISTS " + TABLE_2_NAME;

    //Константа, удаляющая структуру третьей таблицы
    public static final String DELETE_TABLE_3_STRUCTURE =
            "DROP TABLE IF EXISTS " + TABLE_3_NAME;
}
