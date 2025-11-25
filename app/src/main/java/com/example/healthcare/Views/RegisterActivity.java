package com.example.healthcare.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthcare.Manager.UserHandler;
import com.example.healthcare.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText edUsername, edEmail, edPassword, edConfirm;
    private Button btnRegister;
    private TextView tvExistingUser;
    private UserHandler userHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialiser UserHandler
        userHandler = new UserHandler(this);

        // Lier les vues
        edUsername = findViewById(R.id.editTextRegUserName);
        edEmail = findViewById(R.id.editTextRegEmail);
        edPassword = findViewById(R.id.editTextRegPassword);
        edConfirm = findViewById(R.id.editTextRegConfirmPassword);
        btnRegister = findViewById(R.id.buttonRegister);
        tvExistingUser = findViewById(R.id.textViewExistingUser);

        // Redirection vers LoginActivity si déjà utilisateur
        tvExistingUser.setOnClickListener(v ->
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class))
        );

        // Listener pour le bouton d'inscription
        btnRegister.setOnClickListener(v -> {
            String username = edUsername.getText().toString().trim();
            String email = edEmail.getText().toString().trim();
            String password = edPassword.getText().toString().trim();
            String confirm = edConfirm.getText().toString().trim();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(confirm)) {
                Toast.makeText(this, "Password and confirm didn't match", Toast.LENGTH_SHORT).show();
            } else if (!isValid(password)) {
                Toast.makeText(this, "Password must contain at least 8 characters, including letters, digits, and special symbols", Toast.LENGTH_LONG).show();
            } else {
                // Enregistrer l'utilisateur via UserHandler
                long userId = userHandler.registerUser(username, email, password);
                if (userId != -1) {
                    Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                } else {
                    Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Méthode pour valider le mot de passe
    public static boolean isValid(String password) {
        if (password.length() < 8) return false;

        boolean hasLetter = false, hasDigit = false, hasSpecial = false;
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) hasLetter = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else if (c >= 33 && c <= 46 || c == 64) hasSpecial = true;
        }
        return hasLetter && hasDigit && hasSpecial;
    }
}
