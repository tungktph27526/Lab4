package com.example.lab4;

import com.example.lab4.model.ServerRequest;
import com.example.lab4.model.ServerResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RequestInterface {
    @POST("learn-login-register/")
    Call<ServerResponse> operation(@Body ServerRequest request);
}
