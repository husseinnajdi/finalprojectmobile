package com.example.projectfinalma;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {
    private  ProductAPI productApi;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ListView listview = view.findViewById(R.id.LVproduct);

        Retrofit retrofit =APIconnect.getClient();

        productApi=retrofit.create(ProductAPI.class);

        Call<List<Product>> call = productApi.getallproduct();
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (Product product : response.body()) {
                        Log.d("API Response", "Product: " + product.getName() + " Image URL: " + product.getImage());
                    }
                    List<Product> products = response.body();
                    productadaptar adapter = new productadaptar(getActivity(), products);
                    listview.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                t.printStackTrace();
            }
        });

        return view;

    }
}
