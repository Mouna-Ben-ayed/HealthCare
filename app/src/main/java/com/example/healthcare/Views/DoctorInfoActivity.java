package com.example.healthcare.Views;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcare.Manager.DoctorHandler;
import com.example.healthcare.Manager.UserHandler;
import com.example.healthcare.Models.Doctor;
import com.example.healthcare.Models.DoctorAdapter;
import com.example.healthcare.R;

import java.util.List;

public class DoctorInfoActivity extends AppCompatActivity {

    private EditText firstNameEditText, lastNameEditText, phoneEditText, addressEditText;
    private Button saveButton;

    private List<Doctor> doctorsList;
    private DoctorAdapter doctorAdapter;
    private RecyclerView recyclerView;
    private DoctorHandler doctorHandler;
    private UserHandler userHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_info);

        firstNameEditText = findViewById(R.id.first_name_edit_text);
        lastNameEditText = findViewById(R.id.last_name_edit_text);
        phoneEditText = findViewById(R.id.phone_edit_text);
        addressEditText = findViewById(R.id.address_edit_text);
        saveButton = findViewById(R.id.save_button);

        userHandler = new UserHandler(this);
        doctorHandler = new DoctorHandler(this);

        String currentUsername = "current_user";
        int currentUserId = userHandler.getUserIdByUsername(currentUsername);

        doctorsList = doctorHandler.getDoctorsByUserId(currentUserId);

        recyclerView = findViewById(R.id.doctor_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        doctorAdapter = new DoctorAdapter(doctorsList, doctorHandler);
        recyclerView.setAdapter(doctorAdapter);

        saveButton.setOnClickListener(v -> saveDoctor(currentUserId));
    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadDoctors();
    }

    private void reloadDoctors() {
        String currentUsername = "current_user";
        int currentUserId = userHandler.getUserIdByUsername(currentUsername);

        doctorsList = doctorHandler.getDoctorsByUserId(currentUserId);
        doctorAdapter = new DoctorAdapter(doctorsList, doctorHandler);
        recyclerView.setAdapter(doctorAdapter);
    }

    private void saveDoctor(int userId) {
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();

        if (firstName.isEmpty() || lastName.isEmpty()) {
            Toast.makeText(this, "Please enter first and last name", Toast.LENGTH_SHORT).show();
            return;
        }

        Doctor doctor = new Doctor(firstName, lastName, phone, address);
        int newId = doctorHandler.insertDoctor(userId, doctor);

        if (newId != -1) {
            doctor.setId(newId);
            doctorsList.add(doctor);
            doctorAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Doctor saved", Toast.LENGTH_SHORT).show();
            clearFields();
        } else {
            Toast.makeText(this, "Failed to save doctor", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearFields() {
        firstNameEditText.setText("");
        lastNameEditText.setText("");
        phoneEditText.setText("");
        addressEditText.setText("");
    }
}
