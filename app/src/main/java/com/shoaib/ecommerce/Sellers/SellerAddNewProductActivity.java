package com.shoaib.ecommerce.Sellers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shoaib.ecommerce.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import id.zelory.compressor.Compressor;

public class SellerAddNewProductActivity extends AppCompatActivity {

    private String CategoryName, Description, Price, Pname, saveCurrentDate, saveCurrentTime,subCategoryName,shortDescription;
    private Button addNewProductButton;
    private ImageView inputProductImage;
    private EditText inputProductName, inputProductDescription, inputProductPrice, inputShortDescription;
    private static final int GalleryPick = 1;
    private Uri imageUri;
    private String productRandomKey, downloadImageUrl;
    private StorageReference productImagesRef;
    private DatabaseReference productsRef, sellerRef,categoryProductsRef;
    private String sName, sAddress, sPhone, sEmail, sID;
    private Compressor imageCompressor;

    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_add_new_product);

       // CategoryName = getIntent().getExtras().get("mainCategory").toString();
        CategoryName = getIntent().getStringExtra("mainCategory");
        subCategoryName = getIntent().getStringExtra("subCategory");

        productImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        productsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        sellerRef = FirebaseDatabase.getInstance().getReference().child("Sellers");
        categoryProductsRef = FirebaseDatabase.getInstance().getReference()
                .child("Products Category").child(CategoryName);

        addNewProductButton = findViewById(R.id.add_new_product);
        inputProductImage = findViewById(R.id.select_product_image);
        inputProductName = findViewById(R.id.product_name);
        inputProductDescription = findViewById(R.id.product_description);
        inputProductPrice = findViewById(R.id.product_price);
        inputShortDescription = findViewById(R.id.short_description);
        loadingBar = new ProgressDialog(this);

        Toast.makeText(getApplicationContext(), CategoryName, Toast.LENGTH_SHORT).show();

        inputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }


        });

        //Setting the click listener on Add New Product Button
        addNewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    ValidateProductData();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("TAG", "onClick: Exception" + e);
                }
            }
        });

        //Getting seller information
        sellerRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            sName = snapshot.child("name").getValue().toString();
                            sAddress = snapshot.child("address").getValue().toString();
                            sPhone = snapshot.child("phone").getValue().toString();
                            sEmail = snapshot.child("email").getValue().toString();
                            sID = snapshot.child("sid").getValue().toString();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }


    private void OpenGallery() {

        // start picker to get image for cropping and then use the image in cropping activity
        CropImage.activity()
                .setRequestedSize(300, 400, CropImageView.RequestSizeOptions.RESIZE_FIT)
               // .setMinCropResultSize(300, 500)
                //.setMaxCropResultSize(300, 500)
                .start(this);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && data != null) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            inputProductImage.setImageURI(imageUri);
        } else {
            Toast.makeText(this, "Error Occured, please try again.", Toast.LENGTH_SHORT).show();
            finish();
        }

    }


    private void ValidateProductData() throws IOException {

        Description = inputProductDescription.getText().toString();
        Price = inputProductPrice.getText().toString();
        Pname = inputProductName.getText().toString();
        shortDescription = inputShortDescription.getText().toString();

        if (imageUri == null) {
            Toast.makeText(this, "Product image is mandatory.....", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Description)) {

            Toast.makeText(this, "Please Write product description....", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Price)) {

            Toast.makeText(this, "Please Write product Price....", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Pname)) {

            Toast.makeText(this, "Please Write product name....", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(shortDescription)){

            Toast.makeText(this, "Please Write product detail...", Toast.LENGTH_SHORT).show();
        }
        else {
            StoreProductInformation();
        }
    }

    private void StoreProductInformation() throws IOException {

        loadingBar.setTitle("Add New Product");
        loadingBar.setMessage("Please wait, while we are adding new Product");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar c = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy", Locale.getDefault());
        saveCurrentDate = currentDate.format(c.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a", Locale.getDefault());
        saveCurrentTime = currentTime.format(c.getTime());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        productRandomKey = sdf.format(c.getTime());

        //Uploading and resizing the image uploaded.....
        final StorageReference filePath = productImagesRef.child(imageUri.getLastPathSegment() + productRandomKey + ".jpg");

        Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        //uploading the image
        final UploadTask uploadTask = filePath.putBytes(data);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                String message = e.toString();
                Toast.makeText(SellerAddNewProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(SellerAddNewProductActivity.this, "Product Image Uploaded Successfully...", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        if (task.isSuccessful()) {
                            downloadImageUrl = task.getResult().toString();
                            saveProductInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void saveProductInfoToDatabase() {

        final HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", productRandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("description", Description);
        productMap.put("image", downloadImageUrl);
        productMap.put("category", CategoryName);
        productMap.put("subCategory",subCategoryName);
        productMap.put("price", Price);
        productMap.put("pname", Pname);
        productMap.put("shortDesciption",shortDescription);
        productMap.put("mergedCategory",CategoryName + "_" + subCategoryName);

        productMap.put("sellerName", sName);
        productMap.put("sellerAddress", sAddress);
        productMap.put("sellerPhone", sPhone);
        productMap.put("sellerEmail", sEmail);
        productMap.put("sid", sID);
        productMap.put("productState", "Not Approved");


        productRandomKey = productRandomKey.replace(".", "");

        productsRef.child(productRandomKey).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    categoryProductsRef.child(productRandomKey).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            loadingBar.dismiss();
                            Toast.makeText(SellerAddNewProductActivity.this, "Product added Successfully", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(), SellerHomeActivity.class);
                            startActivity(i);
                        }
                    });



                } else {
                    loadingBar.dismiss();
                    String message = task.getException().toString();
                    Toast.makeText(SellerAddNewProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
