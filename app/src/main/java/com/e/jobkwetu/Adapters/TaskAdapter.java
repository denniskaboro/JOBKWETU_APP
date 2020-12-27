package com.e.jobkwetu.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.e.jobkwetu.Model.TasksList;
import com.e.jobkwetu.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> {

    private Context context;
    private List<TasksList> tasksList;
    private static TaskAdapter.OnItemClickListener mListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tasks_title1;
        public TextView tasks_description1;
        public TextView tasks_skills1;
        public TextView tasks_date1;

        public MyViewHolder(View view) {
            super(view);
            tasks_title1 = view.findViewById(R.id.tasks_title);
            tasks_description1 = view.findViewById(R.id.tasks_description);
            tasks_skills1 = view.findViewById(R.id.tasks_skills);
            tasks_date1 = view.findViewById(R.id.tasks_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && mListener != null) {
                        mListener.onItemClick(position);
                    }
                    //Views
                    //ImageView mImage = view.findViewById(R.id.entertainment_image);
                   /* String categ = tasks.getText().toString();
                    Taskers_list_Fragment task = new Taskers_list_Fragment();
                    Bundle args = new Bundle();
                    args.putString("category", categ);
                    args.putString("tasks",value);
                    task.setArguments(args);
                    ((FragmentActivity)context).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container,task)
                            .addToBackStack(null)
                            .commit();

                    */
               }
            });
        }
    }


    public TaskAdapter(Context context, List<TasksList> tasksList) {
        this.context = context;
        this.tasksList = tasksList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category2_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TasksList tasks = tasksList.get(position);

        holder.tasks_title1.setText(tasks.getTitle());

        // Displaying dot from HTML character code
        holder.tasks_description1.setText(tasks.getDescription());
        holder.tasks_skills1.setText(tasks.getSkills());
        holder.tasks_date1.setText(tasks.getStart_date());
    }

    @Override
    public int getItemCount() {
        return tasksList.size();
    }

    /**
     * Formatting timestamp to `MMM d` format
     * Input: 2018-02-21 00:15:42
     * Output: Feb 21
     */
    private String formatDate(String dateStr) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = fmt.parse(dateStr);
            SimpleDateFormat fmtOut = new SimpleDateFormat("MMM d");
            return fmtOut.format(date);
        } catch (ParseException e) {

        }
        return "";
    }
    // inteface to send callbacks
    public interface OnItemClickListener {
        Void onItemClick(int position);

        Void onItemLongClick(int position);
    }

    public void setOnClickListener(TaskAdapter.OnItemClickListener listener) {
        mListener = listener;

    }
}
