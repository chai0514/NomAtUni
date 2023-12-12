package com.example.groupassignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class edit_account_page extends AppCompatActivity {

    EditText editFullName, editUserName, editPhoneNumber, editPassword;
    TextView viewEmail;
    Button confirmButton;
    String userName, userUserName, userEmail, userPhone, userPassword;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_page);

        reference = FirebaseDatabase.getInstance().getReference("users");

        editFullName = findViewById(R.id.accname);
        editUserName = findViewById(R.id.accusername);
        viewEmail = findViewById(R.id.accemail);
        editPhoneNumber = findViewById(R.id.accphone);
        editPassword = findViewById(R.id.accpassword);
        confirmButton = findViewById(R.id.confirmButton);

        // Retrieve data directly from the intent
        Intent intent = getIntent();
        userName = intent.getStringExtra("name");
        userUserName = intent.getStringExtra("username");
        userEmail = intent.getStringExtra("email");
        userPhone = intent.getStringExtra("phone");
        userPassword = intent.getStringExtra("password");

        // Set the text of the corresponding fields
        editFullName.setText(userName);
        editUserName.setText(userUserName);
        viewEmail.setText(userEmail);
        editPhoneNumber.setText(userPhone);
        editPassword.setText(userPassword);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNameChanged() || isUsernameChanged() || isPhoneChanged() || isPasswordChanged()) {
                    updateUserData();
                } else {
                    Toast.makeText(edit_account_page.this, "No changes found", Toast.LENGTH_SHORT).show();
                    //no changes made, bring back to account page
                    startActivity(new Intent(edit_account_page.this, account_page.class));
                    finish();
                }
            }
        });
    }

    private boolean isNameChanged() {
        return !userName.equals(editFullName.getText().toString());
    }

    private boolean isUsernameChanged() {
        return !userUserName.equals(editUserName.getText().toString());
    }

    private boolean isPhoneChanged() {
        return !userPhone.equals(editPhoneNumber.getText().toString());
    }

    private boolean isPasswordChanged() {
        return !userPassword.equals(editPassword.getText().toString());
    }

    private void updateUserData() {
        String newFullName = editFullName.getText().toString();
        String newUserName = editUserName.getText().toString();
        String newPhone = editPhoneNumber.getText().toString();
        String newPassword = editPassword.getText().toString();

        DatabaseReference userRef = reference.child(userName);

        userRef.child("name").setValue(newFullName);
        userRef.child("username").setValue(newUserName);
        userRef.child("phone").setValue(newPhone);
        userRef.child("password").setValue(newPassword);

        Toast.makeText(edit_account_page.this, "Profile has been updated", Toast.LENGTH_SHORT).show();

        // Navigate back to account_page
        Intent intent = new Intent(edit_account_page.this, account_page.class);

        // Pass  updated data to account_page
        intent.putExtra("fromEditPage", true);
        intent.putExtra("name", newFullName);
        intent.putExtra("username", newUserName);
        intent.putExtra("email", userEmail);  // Assuming userEmail is still valid
        intent.putExtra("phone", newPhone);
        intent.putExtra("password", newPassword);

        startActivity(intent);
        finish();
    }

}
