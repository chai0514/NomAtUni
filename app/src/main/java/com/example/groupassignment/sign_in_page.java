package com.example.groupassignment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class sign_in_page extends AppCompatActivity {

    Button signInButton, googleSignInButton;
    CheckBox showPasswordCheckBox;
    TextView register;
    EditText userName, password;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    GoogleSignInOptions signInOptions;
    GoogleSignInClient signInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_page);

        register = findViewById(R.id.registerButton);
        userName = findViewById(R.id.username);
        password = findViewById(R.id.password);

        showPasswordCheckBox = findViewById(R.id.showPasswordCheckBox);
        signInButton = findViewById(R.id.signIn);
        googleSignInButton = findViewById(R.id.googleSignIn);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(sign_in_page.this, sign_up_page.class));
            }
        });

        // Set the password visibility based on the checkbox state
        showPasswordCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int inputType = isChecked ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
            password.setInputType(inputType);
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateUsername() | !validatePassword()) {

                } else {
                    checkUser();
                }
            }
        });


        /*----------------------------------google sign in----------------------------------------*/

        signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        signInClient = GoogleSignIn.getClient(this, signInOptions);

        googleSignInButton.setOnClickListener(view -> signIn());


        /*-------------------------------------google sign in---------------------------------*/
    }


    // check username
    public boolean validateUsername() {
        String val = userName.getText().toString();
        if (val.isEmpty()) {
            userName.setError("Username cannot be empty");
            return false;
        } else {
            userName.setError(null);
            return true;
        }
    }

    // check password
    public boolean validatePassword() {
        String val = password.getText().toString();
        if (val.isEmpty()) {
            password.setError("Password cannot be empty");
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }

    public void checkUser() {
        String userUsername = userName.getText().toString().trim();
        String userPassword = password.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    userName.setError(null);

                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        // Retrieve user data
                        String passwordFromDB = userSnapshot.child("password").getValue(String.class);

                        // Check if passwordFromDB is not null and matches userPassword
                        if (passwordFromDB != null && passwordFromDB.equals(userPassword)) {
                            // Passwords match, save credentials and start main_menu activity
                            saveCredentialsToSharedPreferences(userSnapshot);

                            Toast.makeText(sign_in_page.this, "Login successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(sign_in_page.this, main_menu.class);
                            startActivity(intent);
                            finish();
                            return;
                        }
                    }

                    // Passwords don't match, show error
                    password.setError("Invalid Credentials");
                    password.requestFocus();
                } else {
                    // User does not exist, show error
                    userName.setError("User does not exist");
                    userName.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }

    private void saveCredentialsToSharedPreferences(DataSnapshot userSnapshot) {
        SharedPreferences preferences = getSharedPreferences("user_credentials", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("name", userSnapshot.child("name").getValue(String.class));
        editor.putString("username", userSnapshot.child("username").getValue(String.class));
        editor.putString("email", userSnapshot.child("email").getValue(String.class));
        editor.putString("phone", userSnapshot.child("phone").getValue(String.class));
        editor.putString("password", userSnapshot.child("password").getValue(String.class));
        editor.apply();
    }

    /*--------------------------------------------------------google sign in-------------------------------------------------------*/

    private void signIn() {
        Intent intent = signInClient.getSignInIntent();
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                e.printStackTrace();
                Toast.makeText(this, "Google Sign In Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            Log.d("GoogleSignIn", "User: " + user.getDisplayName() + ", " + user.getEmail());

                            // Update user data in SharedPreferences
                            updateCredentialsInSharedPreferences(user.getDisplayName(), user.getEmail(), "", "");

                            // Start main_menu activity
                            Toast.makeText(sign_in_page.this, "Login successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(sign_in_page.this, main_menu.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Toast.makeText(sign_in_page.this, "Google Sign In Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateCredentialsInSharedPreferences(String name, String email, String phone, String password) {
        SharedPreferences preferences = getSharedPreferences("user_credentials", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("name", name);
        editor.putString("username", name);
        editor.putString("email", email);
        editor.putString("phone", phone);
        editor.putString("password", password);
        editor.apply();
    }
}
