package com.example.projectfinalma;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class settingFragment extends Fragment {

    private Button btnshowproduct,btnshowuser,btnadduser;
    private TextView tvusername;

    private static final String ARG_PARAM1 = "param1";

    private String mParam1;

    public settingFragment() {
    }

    public static settingFragment newInstance(String param1) {
        settingFragment fragment = new settingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        btnshowproduct=view.findViewById(R.id.btnshowproducts);
        btnshowuser=view.findViewById(R.id.btnshowuser);
        tvusername=view.findViewById(R.id.tvusername);



        btnshowproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new showproductFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
        btnshowuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new showuser())
                        .addToBackStack(null)
                        .commit();
            }
        });
    return view;
    }
}