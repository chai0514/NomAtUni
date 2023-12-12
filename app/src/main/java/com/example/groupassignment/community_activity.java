package com.example.groupassignment;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class community_activity extends AppCompatActivity {

    private TextView communityTitle;
    private Button dietTabButton;
    private Button restaurantTabButton;
    private EditText reviewEditText;
    private Button postButton;
    private RecyclerView recyclerView;
    private community_adapter_class communityAdapter;
    private List<post_class> postList;

    // Track current selected tab (Diet or Restaurant)
    private String selectedTab = "Diet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        communityTitle = findViewById(R.id.Community);
        dietTabButton = findViewById(R.id.button);
        restaurantTabButton = findViewById(R.id.button2);
        reviewEditText = findViewById(R.id.Review);
        postButton = findViewById(R.id.button3);
        recyclerView = findViewById(R.id.recyclerView);

        postList = new ArrayList<>();
        communityAdapter = new community_adapter_class(postList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(communityAdapter);

        // Set initial tab selection
        setTabSelection(selectedTab);

        dietTabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTabSelection("Diet");
            }
        });

        restaurantTabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTabSelection("Restaurant");
            }
        });

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitComment();
            }
        });
    }

    private void setTabSelection(String tab) {
        selectedTab = tab;
        communityTitle.setText("Community - " + tab);
        // Update UI or fetch posts based on the selected tab
        // dummy data for testing
        updatePostList();
    }

    private void submitComment() {
        String comment = reviewEditText.getText().toString().trim();
        if (!comment.isEmpty()) {
            // For simplicity, username is hardcoded; replace with actual user data
            post_class post = new post_class("Chai00", comment, System.currentTimeMillis());
            postList.add(post);

            // Notify the adapter that the data has changed
            communityAdapter.notifyDataSetChanged();

            // Clear the comment input
            reviewEditText.setText("");
        }
    }

    private void updatePostList() {
        // Fetch and display posts based on the selected tab
        // dummy data for testing
        postList.clear();
        postList.add(new post_class("JaneSmith", "Trying a new recipe tonight!", System.currentTimeMillis()));
        postList.add(new post_class("BobJohnson", "Any recommendations for a low-carb diet?", System.currentTimeMillis()));
        communityAdapter.notifyDataSetChanged();
    }
}

