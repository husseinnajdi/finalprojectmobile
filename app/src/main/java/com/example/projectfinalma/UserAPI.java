package com.example.projectfinalma;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
interface UserAPI{
    @GET("users")
    Call<List<User>> getAllUsers();

    @POST("users/login")
    Call<User> getLoginUser(@Body User user);


    @GET("users/{id}")
    Call<User> getUser(@Path("userid") int id);

    @POST("users/sign")
    Call<Void> addUser(@Body User user);
}