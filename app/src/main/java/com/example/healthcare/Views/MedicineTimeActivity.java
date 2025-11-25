package com.example.healthcare.Views;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthcare.Manager.MedicineHandler;
import com.example.healthcare.Models.Medicine;
import com.example.healthcare.Views.MedicineAdapter;
import com.example.healthcare.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MedicineTimeActivity extends AppCompatActivity {

    private TimePicker timePicker;
    private Button addButton;
    private EditText editTextMedicineName;
    private ListView listView;

    private MedicineHandler medicineHandler;
    private List<Medicine> medicineList;
    private MedicineAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_time);

        // Initialize views
        timePicker = findViewById(R.id.time_picker);
        addButton = findViewById(R.id.add_button);
        editTextMedicineName = findViewById(R.id.edit_text_medicine_name);
        listView = findViewById(R.id.list_view);

        // Initialize database handler
        medicineHandler = new MedicineHandler(this);

        // Initialize list and adapter
        medicineList = new ArrayList<>();
        adapter = new MedicineAdapter(this, medicineList);
        listView.setAdapter(adapter);

        // Load existing medicines
        loadMedicinesFromDatabase();

        // Button click
        addButton.setOnClickListener(v -> addMedicine());
    }

    private void addMedicine() {
        String medicineName = editTextMedicineName.getText().toString().trim();
        if (medicineName.isEmpty()) {
            Toast.makeText(this, "Please enter a medicine name", Toast.LENGTH_SHORT).show();
            return;
        }

        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();
        int userId = getUserId();

        // Save to database
        long id = medicineHandler.addMedicine(userId, medicineName, hour, minute);

        if (id != -1) {
            Medicine medicine = new Medicine(userId, medicineName, hour, minute);
            medicine.setMedicineId(id);
            medicineList.add(medicine);
            adapter.notifyDataSetChanged();

            Toast.makeText(this, "Medicine added successfully", Toast.LENGTH_SHORT).show();

            // Reset input
            editTextMedicineName.setText("");
            timePicker.setHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
            timePicker.setMinute(Calendar.getInstance().get(Calendar.MINUTE));
        } else {
            Toast.makeText(this, "Error adding medicine", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadMedicinesFromDatabase() {
        int userId = getUserId();
        medicineList.clear();
        medicineList.addAll(medicineHandler.getMedicinesByUserId(userId));
        adapter.notifyDataSetChanged();
    }

    private int getUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("user_id", 0);
    }
}
