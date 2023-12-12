package com.example.groupassignment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class account_page extends AppCompatActivity {

    TextView fullname, username, email, phone, password;
    Button signoutButton, editProfileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_page);

        fullname = findViewById(R.id.accname);
        username = findViewById(R.id.accusername);
        email = findViewById(R.id.accemail);
        phone = findViewById(R.id.accphone);
        password = findViewById(R.id.accpassword);
        editProfileButton = findViewById(R.id.editProfileButton);
        signoutButton = findViewById(R.id.signOutButton);

        // retrieve data from Firebase database
        showUserData();

        // If user wants to edit the profile
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passUserData();
            }
        });

        // If user wants to sign out
        signoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut(); // Sign out from Firebase
                finish();
                startActivity(new Intent(getApplicationContext(), sign_in_page.class));
            }
        });
    }

    public void showUserData() {
        Intent intent = getIntent();

        if (intent.getBooleanExtra("fromEditPage", false)) {
            // Retrieve data from Intent if from edit_account_page
            String userName = intent.getStringExtra("name");
            String userUsername = intent.getStringExtra("username");
            String userEmail = intent.getStringExtra("email");
            String userPhone = intent.getStringExtra("phone");
            String userPassword = intent.getStringExtra("password");

            fullname.setText(userName);
            username.setText(userUsername);
            email.setText(userEmail);
            phone.setText(userPhone);
            password.setText(userPassword);
        } else {
            // Retrieve user credentials from SharedPreferences
            SharedPreferences preferences = getSharedPreferences("user_credentials", MODE_PRIVATE);
            String userName = preferences.getString("name", "");
            String userUsername = preferences.getString("username", "");
            String userEmail = preferences.getString("email", "");
            String userPhone = preferences.getString("phone", "");
            String userPassword = preferences.getString("password", "");

            fullname.setText(userName);
            username.setText(userUsername);
            email.setText(userEmail);
            phone.setText(userPhone);
            password.setText(userPassword);
        }
    }


    public void passUserData() {
        String userUsername = username.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Assuming there is only one user with the specified username
                    DataSnapshot userSnapshot = snapshot.getChildren().iterator().next();

                    String nameFromDB = userSnapshot.child("name").getValue(String.class);
                    String usernameFromDB = userSnapshot.child("username").getValue(String.class);
                    String emailFromDB = userSnapshot.child("email").getValue(String.class);
                    String phoneFromDB = userSnapshot.child("phone").getValue(String.class);
                    String passwordFromDB = userSnapshot.child("password").getValue(String.class);

                    Intent intent = new Intent(account_page.this, edit_account_page.class);

                    intent.putExtra("name", nameFromDB);
                    intent.putExtra("username", usernameFromDB);
                    intent.putExtra("email", emailFromDB);
                    intent.putExtra("phone", phoneFromDB);
                    intent.putExtra("password", passwordFromDB);

                    startActivity(intent);
                } else {
                    Toast.makeText(account_page.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
                Toast.makeText(account_page.this, "Error retrieving data", Toast.LENGTH_SHORT).show();
            }
        });
    }

}