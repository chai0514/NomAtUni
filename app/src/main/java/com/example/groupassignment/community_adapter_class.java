package com.example.groupassignment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class community_adapter_class extends RecyclerView.Adapter<community_adapter_class.PostViewHolder> {

    private List<post_class> postList;

    public community_adapter_class(List<post_class> postList) {
        this.postList = postList;
    }

    // ViewHolder class
    public static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        TextView contentTextView;

        public PostViewHolder(View view) {
            super(view);
            usernameTextView = view.findViewById(R.id.usernameTextView);
            contentTextView = view.findViewById(R.id.contentTextView);
        }
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        post_class post = postList.get(position);
        holder.usernameTextView.setText(post.getUsername());
        holder.contentTextView.setText(post.getContent());
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }
}
