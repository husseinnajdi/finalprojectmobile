package com.example.projectfinalma;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class showuser extends Fragment {
private  UserAPI userAPI;
    public showuser() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_showuser, container, false);

        ListView listview = view.findViewById(R.id.LVusers);

        Retrofit retrofit =APIconnect.getClient();

        userAPI=retrofit.create(UserAPI.class);
        Call<List<User>> call = userAPI.getAllUsers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    List<User> users = response.body();
                    showuseradaptar adapter = new showuseradaptar(getActivity(), users);
                    listview.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });
        return  view;
    }
}