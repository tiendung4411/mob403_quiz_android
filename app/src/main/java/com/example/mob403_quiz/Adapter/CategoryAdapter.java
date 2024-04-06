package com.example.mob403_quiz.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.mob403_quiz.Models.Categories;
import com.example.mob403_quiz.R;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private ArrayList<Categories> categoriesList;
    private Context context;
    private OnItemClickListener listener; // Interface instance for click listener

    // Interface to handle ite3m click events
    public interface OnItemClickListener {
        void onItemClick(Categories category);
    }

    public CategoryAdapter(Context context, ArrayList<Categories> categoriesList, OnItemClickListener listener) {
        this.categoriesList = categoriesList;
        this.context = context;
        this.listener = listener; // Assign the listener instance
    }
    public CategoryAdapter(Context context, ArrayList<Categories> categoriesList) {
        this.categoriesList = categoriesList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.category_item_card, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Categories category = categoriesList.get(position);
        holder.nameCategory.setText(category.getName());
        Glide.with(context)
                .load(category.getImg())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.login_failed)
                        .transform(new CenterCrop(), new RoundedCorners(20)))
                .into(holder.imgCategory);

        // Set click listener on the item view
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(category); // Call onItemClick method of the listener
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (categoriesList != null) ? categoriesList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgCategory;
        public TextView nameCategory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCategory = itemView.findViewById(R.id.item_img);
            nameCategory = itemView.findViewById(R.id.txt_name);
        }
    }
}
