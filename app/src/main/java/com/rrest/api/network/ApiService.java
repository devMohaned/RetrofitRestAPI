package com.rrest.api.network;

import com.rrest.api.models.Employee;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    // Register new employee
    @FormUrlEncoded
    @POST("create")
    Single<Employee> createEmployee(@Field("name") String employeeName,
                                    @Field("salary") int employeeSalary,
                                    @Field("age") int employeeAge);

    // Fetch all employees
    @GET(/* TODO (2): READ TODO(1) "employees"*/"data")
    Single<List<Employee>> fetchAllEmployees();

    @GET("employee/{id}")
    Single<Employee> fetchEmployeeById(@Path("id") int employeeId);

    // Update single employee
    @FormUrlEncoded
    @PUT("update/{id}")
    Completable updateEmployee(@Path("id") int employeeId,
                               @Field("name") String employeeName,
                               @Field("salary") int employeeSalary,
                               @Field("age") int employeeAge);

    // Delete employee
    @DELETE("delete/{id}")
    Completable deleteEmployee(@Path("id") int employeeId);

}
