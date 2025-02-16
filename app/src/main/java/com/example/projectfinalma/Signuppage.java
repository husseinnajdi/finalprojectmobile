package com.example.projectfinalma;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Signuppage extends AppCompatActivity {
    private  UserAPI UserApi;
EditText usernameET,passwordET;
Button signupbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signuppage);
        usernameET=findViewById(R.id.ETusername);
        passwordET=findViewById(R.id.ETpassword);
        signupbtn=findViewById(R.id.btnsignup);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.110:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserApi=retrofit.create(UserAPI.class);

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameET.getText().toString().trim();
                String userpassword = passwordET.getText().toString().trim();

                // Ensure username and password are not empty
                if (username.isEmpty() || userpassword.isEmpty()) {
                    Toast.makeText(Signuppage.this, "Username or Password cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }

                User newuser = new User(username, userpassword);

                Log.d("Signup", "User Data: " + newuser.getUsername() + ", " + newuser.getPassword());

                UserApi.addUser(newuser).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(Signuppage.this, "Sign up Success", Toast.LENGTH_LONG).show();
                            usernameET.setText("");
                            passwordET.setText("");
                        } else {
                            if (response.code() == 400) {
                                Toast.makeText(Signuppage.this, "Username already exists", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(Signuppage.this, "Failed " + response.code(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(Signuppage.this, t.toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
    public void Logintext(View v){
        Intent intent=new Intent(this, Loginpage.class);
        startActivity(intent);
    }
}