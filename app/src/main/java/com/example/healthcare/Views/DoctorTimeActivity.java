package com.example.healthcare.Views;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
    private void showAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Appointment");

        final View view = getLayoutInflater().inflate(R.layout.dialog_add_appointment, null);
        builder.setView(view);

        EditText doctorNameEdit = view.findViewById(R.id.doctor_name_edit_text);
        EditText dateTimeEdit = view.findViewById(R.id.appointment_date_time_edit_text);

        // Quand on clique sur le champ date/heure
        dateTimeEdit.setOnClickListener(v -> showDateTimePicker(dateTimeEdit));

        builder.setPositiveButton("Add", (dialog, which) -> {
            String doctorName = doctorNameEdit.getText().toString().trim();
            String datetime = dateTimeEdit.getText().toString().trim();

            if (!doctorName.isEmpty() && !datetime.isEmpty()) {
                appointmentHandler.addAppointment(doctorName, datetime);
                loadAppointments();
                Toast.makeText(this, "Appointment added", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
    private void showDateTimePicker(EditText editText) {
        final Calendar calendar = Calendar.getInstance();

        // Date picker
        DatePickerDialog datePicker = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    // Après la date, afficher le TimePicker
                    TimePickerDialog timePicker = new TimePickerDialog(this,
                            (timeView, hourOfDay, minute) -> {
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);

                                // Formater la date et l'heure
                                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
                                editText.setText(sdf.format(calendar.getTime()));

                            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);

                    timePicker.show();

                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePicker.show();
    }


}
