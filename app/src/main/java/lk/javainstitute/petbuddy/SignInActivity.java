package lk.javainstitute.petbuddy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class SignInActivity extends AppCompatActivity {

    private TextInputEditText emailInput, passwordInput;
    private TextInputLayout emailLayout, passwordLayout;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);

        // Initialize SharedPreferences to store login state
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        // Check if the user is already logged in (persistent login)
        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            // User is logged in, navigate to HomeActivity directly
            Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        // Handle system insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI components
        emailInput = findViewById(R.id.et_email);
        passwordInput = findViewById(R.id.et_password);
        emailLayout = findViewById(R.id.emailInput);
        passwordLayout = findViewById(R.id.passwordInput);
        Button signInButton = findViewById(R.id.loginButton);
        TextView signupText = findViewById(R.id.loginText);

        // Sign-in button click listener
        signInButton.setOnClickListener(view -> {
            if (validateInputs()) {
                FirebaseFirestore firestore = FirebaseFirestore.getInstance();

                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();

                // Check if email exists in Firestore
                firestore.collection("user")
                        .whereEqualTo("email", email)
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            if (queryDocumentSnapshots.isEmpty()) {
                                // Email not found
                                emailLayout.setError("Email is not registered");
                            } else {
                                // Email found, check password
                                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                    String storedPassword = document.getString("password");

                                    if (storedPassword != null && storedPassword.equals(password)) {
                                        // Password matches, proceed to HomeActivity
                                        // Save login state in SharedPreferences
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putBoolean("isLoggedIn", true);  // Set the login state to true
                                        editor.putString("email", email);  // Store the email
                                        editor.apply();

                                        Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        // Incorrect password
                                        passwordLayout.setError("Incorrect password");
                                    }
                                }
                            }
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(SignInActivity.this, "Error checking email", Toast.LENGTH_SHORT).show();
                        });
            }
        });

        // Sign-up link click listener
        signupText.setOnClickListener(view -> {
            Intent intent = new Intent(SignInActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    // Validate email and password fields
    private boolean validateInputs() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        boolean isValid = true;

        // Email validation
        if (TextUtils.isEmpty(email)) {
            emailLayout.setError("Email cannot be empty");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError("Enter a valid email address");
            isValid = false;
        } else {
            emailLayout.setError(null);
        }

        // Password validation
        if (TextUtils.isEmpty(password)) {
            passwordLayout.setError("Password cannot be empty");
            isValid = false;
        } else if (password.length() < 6) {
            passwordLayout.setError("Password must be at least 6 characters");
            isValid = false;
        } else {
            passwordLayout.setError(null);
        }

        return isValid;
    }
}
