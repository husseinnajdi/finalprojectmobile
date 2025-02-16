package com.example.projectfinalma;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddProductfragment extends Fragment {
    private ProductAPI productAPI;
    private EditText _productname, _productdesc, _productprice, _productstock;
    private ImageButton _btnpickedimage;
    private ImageView imageView;
    private Button btnadd;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addproduct, container, false);

        _productname = view.findViewById(R.id.ETproductname);
        _productdesc = view.findViewById(R.id.ETproductdesc);
        _productprice = view.findViewById(R.id.ETproductprice);
        _productstock = view.findViewById(R.id.ETproductstock);
        _btnpickedimage = view.findViewById(R.id.IBimagepick);
        imageView = view.findViewById(R.id.IVimageshow);
        btnadd = view.findViewById(R.id.btnaddproduct);


        Retrofit retrofit = APIconnect.getClient();
        productAPI = retrofit.create(ProductAPI.class);

        _btnpickedimage.setOnClickListener(v -> openFileChooser());


        btnadd.setOnClickListener(v -> {
            String productname = _productname.getText().toString().trim();
            String productdesc = _productdesc.getText().toString().trim();
            String pricetext = _productprice.getText().toString().trim();
            String stocktext = _productstock.getText().toString().trim();

            if (!productname.isEmpty() && !productdesc.isEmpty() && !pricetext.isEmpty() && !stocktext.isEmpty() && imageUri != null) {
                try {
                    double productprice = Double.parseDouble(pricetext);
                    int productstock = Integer.parseInt(stocktext);

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


                                    Product product = new Product( productname, productdesc, productprice, productstock, imageUrl);
                                    Call<ResponseBody> productCall = productAPI.addProduct(product);
                                    productCall.enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                            if (response.isSuccessful()) {
                                                Toast.makeText(requireContext(), "Product Added Successfully", Toast.LENGTH_LONG).show();
                                                clearForm();
                                            } else {
                                                Toast.makeText(requireContext(), "Failed: " + response.code(), Toast.LENGTH_LONG).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                            Toast.makeText(requireContext(), "Network error", Toast.LENGTH_LONG).show();
                                        }
                                    });

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(requireContext(), "Image Upload Failed", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(requireContext(), "Network error", Toast.LENGTH_LONG).show();
                        }
                    });

                } catch (NumberFormatException e) {
                    Toast.makeText(requireContext(), "Invalid price or stock value", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(requireContext(), "Fill all fields and select an image", Toast.LENGTH_LONG).show();
            }
        });



        return view;
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
            imageView.setImageURI(imageUri);
            Toast.makeText(requireContext(), "Image Selected", Toast.LENGTH_SHORT).show();
        }
    }


    private void clearForm() {
        _productname.setText("");
        _productdesc.setText("");
        _productprice.setText("");
        _productstock.setText("");
        imageView.setImageURI(null);
        imageUri = null;
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