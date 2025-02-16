package com.example.projectfinalma;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.projectfinalma.Product;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Updatepage extends Fragment {

    private EditText etProductName, etProductDescription, etProductPrice, etProductStock;
    private Button btnUpdateProduct;
    private ImageView imageview;
    private ImageButton imagebtn;
    private boolean picknewimage=false;
    private String imageurl;

    private ProductAPI productAPI;
    private Uri imageUri;
    private static final int PICK_IMAGE_REQUEST = 1;

    public Updatepage() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_updatepage, container, false);

        int productId = getArguments().getInt("productId");
        etProductName = view.findViewById(R.id.ETproductname);
        etProductDescription = view.findViewById(R.id.ETproductdesc);
        etProductPrice = view.findViewById(R.id.ETproductprice);
        etProductStock = view.findViewById(R.id.ETproductstock);
        imageview = view.findViewById(R.id.IVimageshow);
        btnUpdateProduct = view.findViewById(R.id.btnupdateproduct);
        imagebtn=view.findViewById(R.id.IBimagepick);

        Retrofit retrofit = APIconnect.getClient();
        productAPI = retrofit.create(ProductAPI.class);

        Log.d("get product id::::::", "onCreateView: "+productId);

        Call<Product> products = productAPI.getoneproduct(productId);
        products.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Product product = response.body();
                    etProductName.setText(product.getName());
                    etProductDescription.setText(product.getDescription());
                    etProductPrice.setText(String.valueOf(product.getPrice()));
                    etProductStock.setText(String.valueOf(product.getStock()));
                    imageurl=product.getImage();
                    Glide.with(getActivity()).load(imageurl)
                            .placeholder(R.drawable.ic_menu_camera).error(R.drawable.ic_menu_slideshow)
                            .into(imageview);
                } else {
                    Toast.makeText(getContext(), "Failed to load product details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        imagebtn.setOnClickListener(v -> openFileChooser());

        btnUpdateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etProductName.getText().toString().trim();
                String description = etProductDescription.getText().toString().trim();
                String priceStr = etProductPrice.getText().toString().trim();
                String stockStr = etProductStock.getText().toString().trim();

                if (name.isEmpty() ||description.isEmpty() ||priceStr.isEmpty() ||stockStr.isEmpty()) {
                    Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                double price = Double.parseDouble(priceStr);
                int stock = Integer.parseInt(stockStr);

                if(picknewimage==false){
                    Product product1=new Product(name,description,price,stock,imageurl);
                    productAPI.updateproduct(productId,product1).enqueue(new Callback<Product>() {
                        @Override
                        public void onResponse(Call<Product> call, Response<Product> response) {
                            if(response.isSuccessful()){
                                Toast.makeText(getContext(), "Product updated succefully", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getContext(), "Error updating product", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Product> call, Throwable t) {
                            Toast.makeText(requireContext(), "Network error", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else {

                    File file = new File(getRealPathFromURI(imageUri));
                    RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
                    MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

                    Call<ResponseBody> uploadCall = productAPI.uploadImage(imagePart);
                    uploadCall.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                try {
                                    String imageUrl = response.body().string();
                                    Log.d("Upload Response", "image url is : " + imageUrl);

                                    Product product = new Product(name, description, price, stock, imageUrl);

                                    productAPI.updateproduct(productId, product).enqueue(new Callback<Product>() {
                                        @Override
                                        public void onResponse(Call<Product> call, Response<Product> response) {
                                            if (response.isSuccessful()) {
                                                Toast.makeText(getContext(), "Product updated succefully", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getContext(), "Error updating product", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Product> call, Throwable t) {
                                            Toast.makeText(requireContext(), "Network error", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                } catch (Exception e) {
                                    Toast.makeText(requireContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                }
            }
        });

        return view;
    }


    public static Updatepage newInstance(int productId) {
        Updatepage fragment = new Updatepage();
        Bundle args = new Bundle();
        args.putInt("productId", productId);
        fragment.setArguments(args);
        return fragment;
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            picknewimage=true;
            imageview.setImageURI(imageUri);
            Toast.makeText(requireContext(), "Image Selected", Toast.LENGTH_SHORT).show();
        }
    }


    private void clearForm() {
        etProductName.setText("");
        etProductDescription.setText("");
        etProductPrice.setText("");
        etProductStock.setText("");
        imageview.setImageURI(null);
        //im = null;
    }
    private String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = requireContext().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(columnIndex);
            cursor.close();
            return path;
        }
        return null;
    }
}
