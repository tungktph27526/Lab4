package com.example.lab4.Api;

import com.example.lab4.Model.ServerRequest;
import com.example.lab4.Model.ServerResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RequestInterface {
    @POST("learn-login-register/")
    Call<ServerResponse> operation(@Body ServerRequest request);
}
