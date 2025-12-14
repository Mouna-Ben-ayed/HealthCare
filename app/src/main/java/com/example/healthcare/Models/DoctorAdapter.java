package com.example.healthcare.Models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast; // Ajout pour le feedback utilisateur (bonne pratique)

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcare.Manager.DoctorHandler; // ⚠️ CORRECTION 1: Utilisation du Handler
import com.example.healthcare.R;

import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder> {

    private final List<Doctor> doctorList;
    private final DoctorHandler doctorHandler;

    // CORRECTION 2: Le constructeur prend DoctorHandler
    public DoctorAdapter(List<Doctor> doctorList, DoctorHandler doctorHandler) {
        this.doctorList = doctorList;
        this.doctorHandler = doctorHandler;
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_doctor, parent, false);
        return new DoctorViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        Doctor doctor = doctorList.get(position);

        // 1. Affichage des données
        holder.nameTextView.setText(doctor.getFirstName() + " " + doctor.getLastName());
        holder.phoneTextView.setText(doctor.getPhone());
        holder.addressTextView.setText(doctor.getAddress());

        // 2. Gestion de l'événement de suppression
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();

                if (adapterPosition != RecyclerView.NO_POSITION) {
                    Doctor deletedDoctor = doctorList.get(adapterPosition);
                    int doctorId = deletedDoctor.getId();

                    // ⚠️ CORRECTION 3: Appelle la méthode deleteDoctor du DoctorHandler (Manager)
                    int rowsDeleted = doctorHandler.deleteDoctor(doctorId);

                    if (rowsDeleted > 0) {
                        // Supprime de la liste et notifie l'adaptateur
                        doctorList.remove(adapterPosition);
                        notifyItemRemoved(adapterPosition);
                        // notifyItemRangeChanged n'est souvent pas nécessaire après notifyItemRemoved
                        // notifyItemRangeChanged(adapterPosition, getItemCount());
                        Toast.makeText(v.getContext(), "Doctor deleted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(v.getContext(), "Error deleting doctor", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }

    // Le ViewHolder reste inchangé
    static class DoctorViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView phoneTextView;
        TextView addressTextView;
        Button deleteButton;

        DoctorViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.doctor_name_textview);
            phoneTextView = itemView.findViewById(R.id.doctor_phone_textview);
            addressTextView = itemView.findViewById(R.id.doctor_address_textview);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }
}