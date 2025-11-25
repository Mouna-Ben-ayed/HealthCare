package com.example.healthcare.Views;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthcare.Manager.AppointmentHandler;
import com.example.healthcare.Models.Appointment;
import com.example.healthcare.R;

import java.util.ArrayList;
import java.util.List;

public class DoctorTimeActivity extends AppCompatActivity {

    private List<Appointment> appointmentList;
    private ArrayAdapter<Appointment> appointmentAdapter;
    private AppointmentHandler appointmentHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_time);

        ListView appointmentListView = findViewById(R.id.appointment_list_view);
        Button addBtn = findViewById(R.id.add_appointment_button);
        Button deleteAllBtn = findViewById(R.id.btnDeleteAppointment);

        appointmentHandler = new AppointmentHandler(this);

        appointmentList = new ArrayList<>();
        appointmentAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, appointmentList);
        appointmentListView.setAdapter(appointmentAdapter);

        loadAppointments();

        appointmentListView.setOnItemClickListener((p, v, pos, id) -> {
            Appointment ap = appointmentList.get(pos);
            showDeleteSingleDialog(ap);
        });

        addBtn.setOnClickListener(v -> showAddDialog());
        deleteAllBtn.setOnClickListener(v -> showDeleteAllDialog());
    }

    private void loadAppointments() {
        appointmentList.clear();
        appointmentList.addAll(appointmentHandler.getAppointments());
        appointmentAdapter.notifyDataSetChanged();
    }

    private void showAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Appointment");

        final android.view.View view = getLayoutInflater().inflate(R.layout.dialog_add_appointment, null);
        builder.setView(view);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String doctorName = ((android.widget.EditText) view.findViewById(R.id.doctor_name_edit_text)).getText().toString();
            String datetime = ((android.widget.EditText) view.findViewById(R.id.appointment_date_time_edit_text)).getText().toString();

            appointmentHandler.addAppointment(doctorName, datetime);
            loadAppointments();
            Toast.makeText(this, "Appointment added", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void showDeleteSingleDialog(Appointment ap) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Appointment")
                .setMessage("Delete this appointment?")
                .setPositiveButton("Delete", (d, w) -> {
                    appointmentHandler.deleteAppointment(ap.getId());
                    loadAppointments();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showDeleteAllDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete All Appointments")
                .setMessage("Are you sure?")
                .setPositiveButton("Delete", (d, w) -> {
                    appointmentHandler.deleteAllAppointments();
                    loadAppointments();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
