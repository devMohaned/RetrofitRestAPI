package com.rrest.api.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rrest.api.models.Employee;
import com.squareup.picasso.Picasso;
import com.rrest.api.R;
import com.rrest.api.view_holders.EmployeeViewHolder;

import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeViewHolder> {

    private List<Employee> list;
    public EmployeeAdapter(List<Employee> list)
    {
        this.list = list;
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EmployeeViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_employee, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {
        Employee currentEmployee = list.get(position);
        holder.mEmployeeId.setText("Id: " + currentEmployee.getId());
        holder.mEmployeeName.setText("Name: " + currentEmployee.getEmployeeName());
        holder.mEmployeeSalary.setText("Salary: " + currentEmployee.getEmployeeSalary());
        holder.mEmployeeAge.setText("Age: " + currentEmployee.getEmployeeAge());
        Picasso.get()
                .load((currentEmployee.getProfileImage().length() > 0) ? currentEmployee.getProfileImage() : "FAKE_IMG_URL")
                .into(holder.mEmployeeImage);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
