package com.example.demo1.Model;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo1.Model.Data.Data;
import com.example.demo1.Model.Data.DbOperations;
import com.example.demo1.R;

import java.util.ArrayList;
import java.util.List;


public class ExpenseFragment extends Fragment {

    //Recycler view
    private RecyclerView expenseRecyclerView;
    private ExpenseRecyclerAdapter expenseRecyclerAdapter;
    private Context context;
    private DbOperations dbOperations;
    private List<Data> expense = new ArrayList<>();

    //Кнопка закрытия диалоговых окон
    private ImageView cross_close;

    //Календарь
    private TextView date_tv;
    private Calendar calendar;
    int day, month, year;

    //Сумма
    private TextView expense_sum;
    private long sum = 0;

    //Обновление данных
    private EditText editAmount_exp;
    private EditText editType_exp;
    private EditText editNote_exp;

    private Button btnUpdate_exp;
    private Button btnDelete_exp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_expense, container, false);
        context = myView.getContext();
        expense_sum = myView.findViewById(R.id.expense_txt_result);

        expenseRecyclerView = myView.findViewById(R.id.recycler_expense);

        dbOperations = new DbOperations(context);
        dbOperations.openDb();
        expenseSummary();
        expense = dbOperations.getExpenseFromDb();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        expenseRecyclerView.setHasFixedSize(true);
        expenseRecyclerView.setLayoutManager(layoutManager);
        expenseRecyclerAdapter = new ExpenseRecyclerAdapter(expense);
        expenseRecyclerView.setAdapter(expenseRecyclerAdapter);


        return myView;
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

    //Прописываем появление окна обновления
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updOrDelData(final int id, final String amount, final String type, final String note, final String date) {
        AlertDialog.Builder mydialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View myview = inflater.inflate(R.layout.update_data, null);
        mydialog.setView(myview);

//        dbOperations.openDb();

        editAmount_exp = myview.findViewById(R.id.amount_txt);
        editType_exp = myview.findViewById(R.id.type_txt);
        editNote_exp = myview.findViewById(R.id.note_txt);


        btnUpdate_exp = myview.findViewById(R.id.btn_update);
        btnDelete_exp = myview.findViewById(R.id.btn_delete);

        cross_close = myview.findViewById(R.id.close_update_dialog);

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

        final AlertDialog dialog = mydialog.create();

        //Записываем значения полей в строк
        String update_amount = String.valueOf(amount);
        editType_exp.setText(type);
        editType_exp.setSelection(type.length());

        editAmount_exp.setText(update_amount);
        editAmount_exp.setSelection(update_amount.length());

        try {
            editNote_exp.setText(note);
            editNote_exp.setSelection(note.length());
        }
        catch (NullPointerException e) {
            editNote_exp.setText("");
        }

        date_tv.setText(date);

        //Задаем обработчик событий для кнопок обновить и удалить
        btnUpdate_exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Записываем измененные значения в переменные-хранилища
                String update_type = editType_exp.getText().toString().trim();
                String update_amount = editAmount_exp.getText().toString().trim();
                String update_note = editNote_exp.getText().toString().trim();
                String update_date = date_tv.getText().toString().trim();

                //Вызываем метод обновления значений в бд
                boolean updating = dbOperations.updateDb(false, id, update_amount, update_type, update_note,
                        update_date);

                //Обработка ошибок
                if (updating == true) {
                    Toast.makeText(getContext(), "Updating successful", Toast.LENGTH_SHORT).show();
                    expense = dbOperations.getExpenseFromDb();
                    expenseRecyclerAdapter = new ExpenseRecyclerAdapter(expense);
                    expenseRecyclerView.setAdapter(expenseRecyclerAdapter);
                    expenseSummary();
//                    expenseRecyclerAdapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnDelete_exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Вызываем метод удаления значений из бд
                boolean deleting = dbOperations.deleteData(false, id);

                if (deleting == true) {
                    Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                    expense = dbOperations.getExpenseFromDb();
                    expenseRecyclerAdapter = new ExpenseRecyclerAdapter(expense);
                    expenseRecyclerView.setAdapter(expenseRecyclerAdapter);
                    expenseSummary();
//                    expenseRecyclerAdapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                }
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

    public void expenseSummary() {
        sum = dbOperations.expenseSum();
        expense_sum.setText("-" + String.valueOf(sum));
    }

    public class ExpenseRecyclerAdapter extends RecyclerView.Adapter<ExpenseRecyclerAdapter.ExpenseViewHolder>
    {
        private List<Data> list;

        public ExpenseRecyclerAdapter(List<Data> list) {
            this.list = list;
        }

        public class ExpenseViewHolder extends RecyclerView.ViewHolder {
            private TextView expense_amount_vh;
            private TextView expense_type_vh;
            private TextView expense_note_vh;
            private TextView expense_date_vh;

            public ExpenseViewHolder(@NonNull View itemView) {
                super(itemView);
                expense_amount_vh = (TextView)itemView.findViewById(R.id.amount_txt_operation);
                expense_type_vh = (TextView)itemView.findViewById(R.id.type_txt_operation);
                expense_note_vh = (TextView)itemView.findViewById(R.id.note_txt_operation);
                expense_date_vh = (TextView)itemView.findViewById(R.id.date_txt_operation);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(View v) {
                        //LOAD DATA
                        final String note = list.get(getAdapterPosition()).getNote();
                        final String date = list.get(getAdapterPosition()).getDate();
                        final String amount = list.get(getAdapterPosition()).getAmount();
                        final String type = list.get(getAdapterPosition()).getType();
                        final int id = list.get(getAdapterPosition()).getId();
                        updOrDelData(id, amount, type, note, date);
                    }
                });
            }
        }

        @NonNull
        @Override
        public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.extra_recycler_data, parent, false);
            return new ExpenseViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
            String str = String.valueOf(list.get(position).getAmount());
            holder.expense_amount_vh.setText(str);
            holder.expense_type_vh.setText(list.get(position).getType());
            holder.expense_note_vh.setText(list.get(position).getNote());
            holder.expense_date_vh.setText(list.get(position).getDate());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

    }
}