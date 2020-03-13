package com.rrest.api.models;

import com.google.gson.annotations.SerializedName;

public class BaseResponse {
    @SerializedName("status")
    String status;

    public String getStatus() {
        return status;
    }
}