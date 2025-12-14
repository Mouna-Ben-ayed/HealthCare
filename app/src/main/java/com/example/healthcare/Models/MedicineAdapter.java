package com.example.healthcare.Views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.healthcare.Models.Medicine;
import com.example.healthcare.R;

import java.util.List;

public class MedicineAdapter extends BaseAdapter {

    private Context context;
    private List<Medicine> medicineList;

    public MedicineAdapter(Context context, List<Medicine> medicineList) {
        this.context = context;
        this.medicineList = medicineList;
    }

    @Override
    public int getCount() {
        return medicineList.size();
    }

    @Override
    public Object getItem(int position) {
        return medicineList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return medicineList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            row = inflater.inflate(R.layout.list_item_medicine, parent, false);
        }

        TextView tvMedicineName = row.findViewById(R.id.tvMedicineName);
        TextView tvMedicineTime = row.findViewById(R.id.tvMedicineTime);
        ImageButton btnDelete = row.findViewById(R.id.btnDelete);

        Medicine medicine = medicineList.get(position);
        tvMedicineName.setText(medicine.getName());
        tvMedicineTime.setText(String.format("%02d:%02d", medicine.getHour(), medicine.getMinute()));

        btnDelete.setOnClickListener(v -> {
            if (context instanceof MedicineTimeActivity) {
                ((MedicineTimeActivity) context).deleteMedicine(medicine);
            }
        });

        return row;
    }

}
