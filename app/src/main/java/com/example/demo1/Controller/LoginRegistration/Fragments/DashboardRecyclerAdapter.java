package com.example.demo1.Controller.LoginRegistration.Fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo1.Model.Data;
import com.example.demo1.R;

import java.util.List;

public class DashboardRecyclerAdapter extends RecyclerView.Adapter<DashboardRecyclerAdapter.MyViewHolder> {

   private List<Data> list;


    public DashboardRecyclerAdapter(List<Data> list) {
            this.list = list;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView income_amount_vh;
        private TextView income_type_vh;
        private TextView income_note_vh;
        private TextView income_date_vh;

        private TextView expense_amount_vh;
        private TextView expense_type_vh;
        private TextView expense_note_vh;
        private TextView expense_date_vh;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            income_amount_vh = (TextView)itemView.findViewById(R.id.amount_dash);
            income_type_vh = (TextView)itemView.findViewById(R.id.type_dash);
            income_note_vh = (TextView)itemView.findViewById(R.id.note_dash);
            income_date_vh = (TextView)itemView.findViewById(R.id.date_dash);

            expense_amount_vh = (TextView)itemView.findViewById(R.id.amount_dash);
            expense_type_vh = (TextView)itemView.findViewById(R.id.type_dash);
            expense_note_vh = (TextView)itemView.findViewById(R.id.note_dash);
            expense_date_vh = (TextView)itemView.findViewById(R.id.date_dash);
        }
    }

    @NonNull
    @Override
    public DashboardRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.dashboard_operations_recycler, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        String str = String.valueOf(list.get(position).getAmount());
        holder.income_amount_vh.setText(str);
        holder.income_type_vh.setText(list.get(position).getType());
        holder.income_note_vh.setText(list.get(position).getNote());
        holder.income_date_vh.setText(list.get(position).getDate());

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
