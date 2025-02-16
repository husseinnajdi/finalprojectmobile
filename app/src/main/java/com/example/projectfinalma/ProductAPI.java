
package com.example.projectfinalma;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ProductAPI {
    @GET("products/allproduct")
    Call<List<Product>> getallproduct();


    @Multipart
    @POST("products/upload")
    Call<ResponseBody> uploadImage(@Part MultipartBody.Part image);

    @POST("products/addproduct")
    Call<ResponseBody> addProduct(@Body Product product);

    @DELETE("products/deletproduct/{id}")
    Call<Product> deletproduct(@Path("id") int id);

    @GET("products/getoneproduct/{id}")
    Call<Product> getoneproduct(@Path("id") int productId);

    @PUT("products/updateproduct/{id}")
    Call<Product> updateproduct(@Path("id") int productId,@Body Product product);
}
