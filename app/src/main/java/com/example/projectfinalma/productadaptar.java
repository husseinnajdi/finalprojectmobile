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

public class productadaptar extends ArrayAdapter<Product> {
    private ProductAPI productAPI;
    private Activity activity;
    private List<Product> products;

    public productadaptar(Activity activity, List<Product> products) {
        super(activity, R.layout.product_item, products);
        this.activity = activity;
        this.products = products;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(activity).inflate(R.layout.product_item, parent, false);
        }

        Product product = products.get(position);

        ImageView imageview=convertView.findViewById(R.id.IVimage);
        TextView titleView = convertView.findViewById(R.id.TVname);
        TextView descriptionView = convertView.findViewById(R.id.TVdescription);
        TextView price = convertView.findViewById(R.id.TVprice);
        TextView stock=convertView.findViewById(R.id.TVstock);
        Button delete=convertView.findViewById(R.id.Btndelete);
        Button update=convertView.findViewById(R.id.Btnupdate);

        Retrofit retrofit = APIconnect.getClient();
        productAPI = retrofit.create(ProductAPI.class);

        String imageUrl = product.getImage();
        Log.d("image url: ", "getView: "+imageUrl+""+product.getPrice());

        Glide.with(activity)
                .load(imageUrl)
                .placeholder(R.drawable.ic_menu_camera)
                .error(R.drawable.ic_menu_slideshow)
                .into(imageview);


        titleView.setText(product.getName() != null ? product.getName()+"   " : "No Name Available");
        descriptionView.setText(product.getDescription() != null ? product.getDescription() : "No Description Available");
        price.setText(product.getPrice() != 0 ?"Price: "+ String.valueOf(product.getPrice())+"$    " : "Price Unavailable");
        stock.setText(product.getStock() != 0 ?"stock: "+ String.valueOf(product.getStock()) : "Stock Info Unavailable");

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int productid=product.getId();

                productAPI.deletproduct(productid).enqueue(new Callback<Product>() {
                    @Override
                    public void onResponse(Call<Product> call, Response<Product> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(getContext(), "Deleted successful", Toast.LENGTH_LONG).show();
                            products.remove(position);

                            notifyDataSetChanged();
                        }else{
                            Toast.makeText(getContext(), "errror", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<Product> call, Throwable t) {
                        Toast.makeText(getContext(), "error"+t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });


            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int productid=product.getId();

                Updatepage updateFragment = Updatepage.newInstance(productid);

                if (activity != null && activity instanceof AppCompatActivity) {
                    AppCompatActivity appCompatActivity = (AppCompatActivity) activity;
                    FragmentManager fragmentManager = appCompatActivity.getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, updateFragment)
                            .addToBackStack(null)
                            .commit();
                } else {
                    Log.e("productadaptar", "Activity is null or not an instance of AppCompatActivity!");
                }

            }
        });


        return convertView;
    }

}

