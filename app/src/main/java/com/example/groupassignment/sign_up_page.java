package com.example.groupassignment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class sign_up_page extends AppCompatActivity {

    TextView login;
    EditText userFullName, userName, userPhone, userEmail, userPassword;
    CheckBox showPasswordCheckBox;
    Button signUpButton, googleSignUpButton;

    FirebaseDatabase database;
    DatabaseReference reference;

    /*For google sign in*/
    GoogleSignInOptions signInOptions;
    GoogleSignInClient signInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        login = findViewById(R.id.loginButton);

        userFullName = findViewById(R.id.fullname);
        userName = findViewById(R.id.username);
        userPhone = findViewById(R.id.phone);
        userEmail = findViewById(R.id.email);
        userPassword = findViewById(R.id.password);

        showPasswordCheckBox = findViewById(R.id.showPasswordCheckBox);

        signUpButton = findViewById(R.id.signUp);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(sign_up_page.this, sign_in_page.class));
            }
        });

        // Set the password visibility based on the checkbox state
        showPasswordCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int inputType = isChecked ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
            userPassword.setInputType(inputType);
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Retrieve user input
                String name = userFullName.getText().toString();
                String username = userName.getText().toString();
                String phone = userPhone.getText().toString();
                String email = userEmail.getText().toString();
                String password = userPassword.getText().toString();

                // Check if any fields is empty
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(username) || TextUtils.isEmpty(phone) ||
                        TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    // Show a Toast message if any field is empty
                    Toast.makeText(sign_up_page.this, "Please fill in all the details", Toast.LENGTH_SHORT).show();
                } else {

                    // Sign up
                    database = FirebaseDatabase.getInstance();
                    reference = database.getReference("users");

                    helper_class helper_class = new helper_class(name, username, email, password, phone);
                    reference.child(name).setValue(helper_class);

                    Toast.makeText(sign_up_page.this, "You have signed up successfully", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(sign_up_page.this, sign_in_page.class);
                    startActivity(intent);
                }
            }
        });



        /*---------------------------------------------Sign up with google----------------------------------------------------*/

        googleSignUpButton = findViewById(R.id.googleSignUp);

        signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        signInClient = GoogleSignIn.getClient(this, signInOptions);

        googleSignUpButton.setOnClickListener(view -> signUp());
    }

    public void signUp() {
        Intent intent = signInClient.getSignInIntent();
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                dashboard();
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void dashboard() {
        finish();
        Intent intent = new Intent(getApplicationContext(), main_menu.class);
        startActivity(intent);
    }
}
