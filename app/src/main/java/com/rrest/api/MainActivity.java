package com.rrest.api;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.rrest.api.adapters.EmployeeAdapter;
import com.rrest.api.models.Employee;
import com.rrest.api.network.ApiClient;
import com.rrest.api.network.ApiService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private ApiService apiService;
    private CompositeDisposable disposable = new CompositeDisposable();


    @BindView(R.id.root)
    LinearLayout root;
    @BindView(R.id.ID_employee_recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @OnClick(R.id.fab)
    void refresh(View view){
     fetchAllEmployees();
    }

    List<Employee> mEmployeeList = new ArrayList<Employee>();
    EmployeeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mAdapter = new EmployeeAdapter(mEmployeeList);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        apiService = ApiClient.getClient(getApplicationContext()).create(ApiService.class);
        fetchAllEmployees();
    }


    private void registerEmployee() {
        // unique id to identify the device
        int randomId = (int) Math.round(Math.random());
        disposable.add(
                apiService
                        .createEmployee("Random Name X" + randomId, randomId, 1010)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<Employee>() {
                            @Override
                            public void onSuccess(Employee employee) {
                                Toast.makeText(getApplicationContext(),
                                        "Registered New Employee YAAY ",
                                        Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, "onError: " + e.getMessage());
                                showError(e);
                            }
                        }));
    }


    private void fetchAllEmployees() {
        disposable.add(
                apiService.fetchAllEmployees()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<List<Employee>, List<Employee>>() {
                            @Override
                            public List<Employee> apply(List<Employee> employees) throws Exception {
                                // TODO - note about sort
                                Collections.sort(employees, new Comparator<Employee>() {
                                    @Override
                                    public int compare(Employee n1, Employee n2) {
                                        return n2.getId() - n1.getId();
                                    }
                                });
                                return employees;
                            }
                        })
                        .subscribeWith(new DisposableSingleObserver<List<Employee>>() {
                            @Override
                            public void onSuccess(List<Employee> employees) {
                                mEmployeeList.clear();
                                mEmployeeList.addAll(employees);
                                mAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, "onError: " + e.getMessage());
                                showError(e);
                            }
                        })
        );
    }

    private void getEmployeeById(int employeeId) {
        disposable.add(
                apiService.fetchEmployeeById(employeeId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<Employee>() {
                            @Override
                            public void onSuccess(Employee employee) {
                                mEmployeeList.clear();
                                mEmployeeList.add(employee);
                                mAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, "onError: " + e.getMessage());
                                showError(e);
                            }
                        })
        );
    }


    private void updateEmployee(int employeeId, final String employeeName, final int employeeSalary, final int employeeAge, final int position) {
        disposable.add(
                apiService.updateEmployee(employeeId, employeeName, employeeSalary, employeeAge)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableCompletableObserver() {
                            @Override
                            public void onComplete() {
                                Log.d(TAG, "Employee updated!");

                                Employee e = mEmployeeList.get(position);
                                e.setEmployeeName(employeeName);
                                e.setEmployeeAge(employeeAge);
                                e.setEmployeeSalary(employeeSalary);
                                // Update item and notify adapter
                                mEmployeeList.set(position, e);
                                mAdapter.notifyItemChanged(position);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, "onError: " + e.getMessage());
                                showError(e);
                            }
                        }));
    }


    private void deleteEmployee(final int employeeId, final int position) {
        Log.e(TAG, "deleteEmployee: " + employeeId + ", " + position);
        disposable.add(
                apiService.deleteEmployee(employeeId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableCompletableObserver() {
                            @Override
                            public void onComplete() {
                                Log.d(TAG, "Employee deleted! " + employeeId);

                                // Remove and notify adapter about item deletion
                                mEmployeeList.remove(position);
                                mAdapter.notifyItemRemoved(position);

                                Toast.makeText(MainActivity.this, "Employee deleted!", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, "onError: " + e.getMessage());
                                showError(e);
                            }
                        })
        );
    }



    /**
     * Showing a Snackbar with error message
     * The error body will be in json format
     * {"error": "Error message!"}
     */
    private void showError(Throwable e) {
        String message = "";
        try {
            if (e instanceof IOException) {
                message = "No internet connection!";
            } else if (e instanceof HttpException) {
                HttpException error = (HttpException) e;
                String errorBody = error.response().errorBody().string();
                JSONObject jObj = new JSONObject(errorBody);

                message = jObj.getString("error");
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (JSONException e1) {
            e1.printStackTrace();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        if (TextUtils.isEmpty(message)) {
            message = "Unknown error occurred! Check LogCat.";
        }

        Snackbar snackbar = Snackbar
                .make(root, message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
