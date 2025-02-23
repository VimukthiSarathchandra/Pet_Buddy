package lk.javainstitute.petbuddy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText fullNameInput, emailInput, passwordInput;
    private CheckBox rememberMeCheckbox;
    private SharedPreferences sharedPreferences;
    private TextView loginText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        // Initialize SharedPreferences to store login state
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        // Check if the user is already logged in (persistent login)
        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            // User is logged in, navigate to HomeActivity directly
            Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        // Handle system insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI Components
        fullNameInput = findViewById(R.id.et_fullName);
        emailInput = findViewById(R.id.et_email1);
        passwordInput = findViewById(R.id.et_password1);
        rememberMeCheckbox = findViewById(R.id.rememberMeCheckbox);
        Button registerButton = findViewById(R.id.loginButton);
        loginText = findViewById(R.id.loginText);

        // Register button click event
        registerButton.setOnClickListener(view -> {
            String fullName = fullNameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            FirebaseFirestore firestore = FirebaseFirestore.getInstance();

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("name", fullName);
            hashMap.put("email", email);
            hashMap.put("password", password);

            if (validateInputs()) {
                // Save credentials if "Remember Me" is checked
                if (rememberMeCheckbox.isChecked()) {
                    saveUserCredentials(email, password);
                }

                firestore.collection("user")
                        .add(hashMap)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                // After registration, save login state in SharedPreferences
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("isLoggedIn", true);  // Set login state to true
                                editor.putString("email", email);  // Store the email
                                editor.apply();

                                // Navigate to HomeActivity after successful registration
                                Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(Exception e) {
                                Toast.makeText(RegisterActivity.this, "Registration Failed!", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        // Already have an account? Login click event
        loginText.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterActivity.this, SignInActivity.class);
            startActivity(intent);
        });
    }

    // Save user credentials to SharedPreferences
    private void saveUserCredentials(String email, String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email);
        editor.putString("password", password);
        editor.putBoolean("rememberMe", true);
        editor.apply();
    }

    // Validate user inputs
    private boolean validateInputs() {
        String fullName = fullNameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        boolean isValid = true;

        // Full Name validation
        if (TextUtils.isEmpty(fullName)) {
            fullNameInput.setError("Full Name cannot be empty");
            isValid = false;
        }

        // Email validation
        if (TextUtils.isEmpty(email)) {
            emailInput.setError("Email cannot be empty");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Enter a valid email address");
            isValid = false;
        }

        // Password validation
        if (TextUtils.isEmpty(password)) {
            passwordInput.setError("Password cannot be empty");
            isValid = false;
        } else if (password.length() < 6) {
            passwordInput.setError("Password must be at least 6 characters");
            isValid = false;
        }

        return isValid;
    }
}
