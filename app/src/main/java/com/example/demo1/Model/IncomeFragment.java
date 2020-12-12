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


public class IncomeFragment extends Fragment {

    //Recycler view
    private RecyclerView incomeRecyclerView;
    private IncomeRecyclerAdapter incomeRecyclerAdapter;
    private Context context;
    private DbOperations dbOperations;
    private List<Data> income = new ArrayList<>();

    //Кнопка закрытия диалоговых окон
    private ImageView cross_close;

    //Календарь
    private Calendar calendar;
    private TextView date_tv;
    private int day, month, year;

    //Сумма
    private TextView income_sum;
    private long sum;

    //Обновление данных
    private EditText editAmount;
    private EditText editType;
    private EditText editNote;

    private Button btnUpdate;
    private Button btnDelete;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_income, container, false);

        context = myView.getContext();
        income_sum = myView.findViewById(R.id.income_txt_result);
        incomeRecyclerView = myView.findViewById(R.id.recycler_income);

        dbOperations = new DbOperations(context);
        dbOperations.openDb();
        incomeSummary();
        income = dbOperations.getIncomeFromDb();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        incomeRecyclerView.setHasFixedSize(true);
        incomeRecyclerView.setLayoutManager(layoutManager);
        incomeRecyclerAdapter = new IncomeRecyclerAdapter(income);
        incomeRecyclerView.setAdapter(incomeRecyclerAdapter);
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
        

        editAmount = myview.findViewById(R.id.amount_txt);
        editType = myview.findViewById(R.id.type_txt);
        editNote = myview.findViewById(R.id.note_txt);

        btnUpdate = myview.findViewById(R.id.btn_update);
        btnDelete = myview.findViewById(R.id.btn_delete);

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
        editType.setText(type);
        editType.setSelection(type.length());

        editAmount.setText(update_amount);
        editAmount.setSelection(update_amount.length());

        try {
            editNote.setText(note);
            editNote.setSelection(note.length());
        }
        catch (NullPointerException e) {
            editNote.setText("");
        }

        date_tv.setText(date);

        //Задаем обработчик событий для кнопок обновить и удалить
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Записываем измененные значения в переменные-хранилища
                String update_type = editType.getText().toString().trim();
                String update_amount = editAmount.getText().toString().trim();
                String update_note = editNote.getText().toString().trim();
                String update_date = date_tv.getText().toString().trim();

                //Вызываем метод обновления значений в бд
                boolean updating = dbOperations.updateDb(true, id, update_amount, update_type, update_note,
                        update_date);

                //Обработка ошибок
                if (updating == true) {
                    Toast.makeText(getContext(), "Updating successful", Toast.LENGTH_SHORT).show();
                    income = dbOperations.getIncomeFromDb();
                    incomeRecyclerAdapter = new IncomeRecyclerAdapter(income);
                    incomeRecyclerView.setAdapter(incomeRecyclerAdapter);
                    incomeSummary();
                }
                else {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Вызываем метод удаления значений из бд
                boolean deleting = dbOperations.deleteData(true, id);

                if (deleting == true) {
                    Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                    income = dbOperations.getIncomeFromDb();
                    incomeRecyclerAdapter = new IncomeRecyclerAdapter(income);
                    incomeRecyclerView.setAdapter(incomeRecyclerAdapter);
                    incomeSummary();
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

    //Записываем сумму в текствью
    private void incomeSummary() {
        sum = dbOperations.incomeSum();
        income_sum.setText("+" + String.valueOf(sum));
    }

    public class IncomeRecyclerAdapter extends RecyclerView.Adapter<IncomeRecyclerAdapter.IncomeViewHolder>
    {
        private List<Data> list;

        public IncomeRecyclerAdapter(List<Data> list) {
            this.list = list;
        }

        public class IncomeViewHolder extends RecyclerView.ViewHolder {
            private TextView income_amount_vh;
            private TextView income_type_vh;
            private TextView income_note_vh;
            private TextView income_date_vh;

            public IncomeViewHolder(@NonNull View itemView) {
                super(itemView);
                income_amount_vh = (TextView)itemView.findViewById(R.id.amount_txt_operation);
                income_type_vh = (TextView)itemView.findViewById(R.id.type_txt_operation);
                income_note_vh = (TextView)itemView.findViewById(R.id.note_txt_operation);
                income_date_vh = (TextView)itemView.findViewById(R.id.date_txt_operation);

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
        public IncomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.extra_recycler_data, parent, false);
            return new IncomeViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull IncomeViewHolder holder, int position) {
            String str = String.valueOf(list.get(position).getAmount());
            holder.income_amount_vh.setText(str);
            holder.income_type_vh.setText(list.get(position).getType());
            holder.income_note_vh.setText(list.get(position).getNote());
            holder.income_date_vh.setText(list.get(position).getDate());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

}