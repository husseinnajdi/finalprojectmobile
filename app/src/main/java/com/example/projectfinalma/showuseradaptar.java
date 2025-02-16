package com.example.projectfinalma;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class showuseradaptar extends ArrayAdapter<User> {
    private UserAPI userAPI;
    private Activity activity;
    private List<User> users;

    public showuseradaptar(Activity activity, List<User> users) {
        super(activity, R.layout.showuser_item, users);
        this.activity = activity;
        this.users = users;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(activity).inflate(R.layout.showuser_item, parent, false);
        }

        User user = users.get(position);

        TextView tvuserid = convertView.findViewById(R.id.tvuserid);
        TextView tvusername = convertView.findViewById(R.id.tvusername);
        TextView tvuserpass = convertView.findViewById(R.id.tvuserpassword);

        Retrofit retrofit = APIconnect.getClient();
        userAPI = retrofit.create(UserAPI.class);

        tvuserid.setText("ID: " + String.valueOf(user.getId()));
        tvusername.setText("Username: "+user.getUsername());
        tvuserpass.setText("Password: "+user.getPassword() );

        return convertView;
    }

}

