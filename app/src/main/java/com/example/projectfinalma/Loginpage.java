package com.example.projectfinalma;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class Loginpage extends AppCompatActivity {
    private  UserAPI UserApi;
    EditText usernameET,userpasswordET;
    Button Loginbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usernameET=findViewById(R.id.ETusername);
        userpasswordET=findViewById(R.id.ETpassword);
        Loginbtn=findViewById(R.id.btnlogin);

        Retrofit retrofit= APIconnect.getClient();



        UserApi=retrofit.create(UserAPI.class);

        Loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameET.getText().toString().trim();
                String userpassword = userpasswordET.getText().toString().trim();

                if (username.isEmpty() || userpassword.isEmpty()) {
                    Toast.makeText(Loginpage.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                User user = new User(username, userpassword);

                UserApi.getLoginUser(user).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(Loginpage.this, "Login Success", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Loginpage.this, Homepage.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            try {
                                String errorResponse = response.errorBody().string(); // Get error message
                                Toast.makeText(Loginpage.this, "Error: " + errorResponse, Toast.LENGTH_LONG).show();
                                System.out.println("Server Response: " + errorResponse);
                            } catch (Exception e) {
                                Toast.makeText(Loginpage.this, "Error parsing response", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(Loginpage.this, "Network Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                        System.out.println( t.getMessage());
                    }
                });
            }
        });

    }
    public void Signuptext(View v) {
        Intent intent=new Intent(this,Signuppage.class);
        startActivity(intent);
    }
}