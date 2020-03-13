package com.rrest.api.view_holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rrest.api.R;
import com.rrest.api.models.Employee;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EmployeeViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.ID_employee_id)
    public TextView mEmployeeId;
    @BindView(R.id.ID_employee_image)
    public ImageView mEmployeeImage;
    @BindView(R.id.ID_employee_name)
    public TextView mEmployeeName;
    @BindView(R.id.ID_employee_age)
    public TextView mEmployeeAge;
    @BindView(R.id.ID_employee_salary)
    public TextView mEmployeeSalary;

    public EmployeeViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
