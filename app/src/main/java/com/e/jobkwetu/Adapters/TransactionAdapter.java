package com.e.jobkwetu.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.e.jobkwetu.Model.trans;
import com.e.jobkwetu.R;

import java.util.ArrayList;

public class TransactionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity activity;
    private LayoutInflater inflater;
    private ArrayList<trans> transArrayList;



    public TransactionAdapter(Activity activity, ArrayList<trans> transArrayList) {
        this.activity = activity;
        this.transArrayList = transArrayList;
    }

    @Override
    public int getItemCount() {
        return transArrayList.size();
    }


    @Override
    public long getItemId(int position) {
        return position;

    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transaction_row, parent, false);



        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        // getting model data for the row
        trans m =transArrayList.get(position);
       ((ViewHolder) holder).id.setText(m.getId());
        ((ViewHolder) holder).date.setText(m.getDate());
        ((ViewHolder) holder).transation.setText(m.getTransaction());
        ((ViewHolder) holder).status.setText(m.getStatus());
        ((ViewHolder) holder).amount.setText(m.getAmount());


    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView id,date,transation,status,amount;
        public ViewHolder(View view) {
            super(view);
            id = (TextView) view.findViewById(R.id.trans_id);
            date = (TextView) view.findViewById(R.id.trans_date);
            transation = (TextView) view.findViewById(R.id.trans_code);
            status = (TextView) view.findViewById(R.id.trans_name);
            amount = (TextView) view.findViewById(R.id.trans_amount);

        }
    }
}
