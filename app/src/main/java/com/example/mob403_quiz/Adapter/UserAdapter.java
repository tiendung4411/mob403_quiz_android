package com.example.mob403_quiz.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mob403_quiz.Models.Users;
import com.example.mob403_quiz.R;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private ArrayList<Users> usersList;
    private Context context;

    public UserAdapter(ArrayList<Users> usersList, Context context) {
        this.usersList = (usersList != null) ? usersList : new ArrayList<Users>();
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvName.setText(usersList.get(position).getName());
        holder.tvAge.setText(String.valueOf(usersList.get(position).getAge()));
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete user
            }
        });
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Edit user
            }
        });
    }

    @Override
    public int getItemCount() {
        return (usersList != null) ? usersList.size() : 0;
    }

    public static class  ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvName, tvAge;
        public ImageView btnEdit, btnDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvAge = itemView.findViewById(R.id.tvAge);
            btnEdit = itemView.findViewById(R.id.edit_icon);
            btnDelete = itemView.findViewById(R.id.delete_icon);
        }
    }
}
