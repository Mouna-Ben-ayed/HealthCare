package com.example.healthcare.Views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthcare.R;

public class HomeActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sharedPreferences = getSharedPreferences("shared_perfs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        Toast.makeText(this, "Welcome " + username, Toast.LENGTH_SHORT).show();

        // On configure les clics sur les nouvelles CardViews
        // Chaque carte a maintenant son propre ID. Plus besoin de lier le texte et l'icône séparément.
        setupClickListener(R.id.card_medicine_time, MedicineTimeActivity.class);
        setupClickListener(R.id.card_doctor_time, DoctorTimeActivity.class);
        setupClickListener(R.id.card_doctor_info, DoctorInfoActivity.class);
        setupClickListener(R.id.card_health_info, NewsActivity.class);
        setupClickListener(R.id.card_logout, LoginActivity.class, true); // Le listener pour le logout
    }

    private void setupClickListener(int viewId, Class<?> targetActivity) {
        setupClickListener(viewId, targetActivity, false);
    }

    private void setupClickListener(int viewId, Class<?> targetActivity, boolean clearPrefs) {
        findViewById(viewId).setOnClickListener(v -> {
            Log.d("CLICK_TEST", "Card clicked: " + getResources().getResourceEntryName(viewId));
            if (clearPrefs) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                // Redirige vers l'écran de connexion et ferme l'activité actuelle
                Intent intent = new Intent(HomeActivity.this, targetActivity);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else {
                startActivity(new Intent(HomeActivity.this, targetActivity));
            }
        });
    }
}
