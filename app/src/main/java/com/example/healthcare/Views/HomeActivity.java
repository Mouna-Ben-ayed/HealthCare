package com.example.healthcare.Views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

        // Boutons / images
        setupClickListener(R.id.medecinetime, MedicineTimeActivity.class);
        setupClickListener(R.id.icon1, MedicineTimeActivity.class);

        setupClickListener(R.id.doctortime, DoctorTimeActivity.class);
        setupClickListener(R.id.icon2, DoctorTimeActivity.class);

        setupClickListener(R.id.doctorinfo, DoctorInfoActivity.class);
        setupClickListener(R.id.icon3, DoctorInfoActivity.class);

        setupClickListener(R.id.healthinfo, NewsActivity.class);
        setupClickListener(R.id.icon5, NewsActivity.class);


        setupClickListener(R.id.logout, LoginActivity.class, true);
        setupClickListener(R.id.icon6, LoginActivity.class, true);
    }

    private void setupClickListener(int viewId, Class<?> targetActivity) {
        setupClickListener(viewId, targetActivity, false);
    }

    private void setupClickListener(int viewId, Class<?> targetActivity, boolean clearPrefs) {
        findViewById(viewId).setOnClickListener(v -> {
            Log.d("CLICK_TEST", "Clicked: " + getResources().getResourceEntryName(viewId));
            if (clearPrefs) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
            }
            startActivity(new Intent(HomeActivity.this, targetActivity));
            if (clearPrefs) finish(); // Ferme HomeActivity après logout
        });
    }
}
