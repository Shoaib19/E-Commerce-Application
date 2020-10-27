package com.shoaib.ecommerce.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
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
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.shoaib.ecommerce.Model.Products;
import com.shoaib.ecommerce.Prevalent.Prevalent;
import com.shoaib.ecommerce.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class ProductDetailsActivity extends AppCompatActivity {

    private Button addToCartButton;
    private ImageView productImage;
    private TextView productPrice, productShortDescription, productName,quantityText,productDetailedDescription;
    private String productID = "", state = "Normal", sellerID, imageUri;
    private String orderId = String.valueOf(1);
    private String time1,rupeeSymbol = null;
    private Spinner spinner;
    ArrayAdapter<String> dataAdapter;
    public String[] spinnerData = new String[] {"1","2","3","4","5"};
    private String quantityValue = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productID = getIntent().getStringExtra("pid");
        sellerID = getIntent().getStringExtra("sid");



        rupeeSymbol = String.valueOf(Html.fromHtml("\u20B9"));

        final DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productID);

        spinner = findViewById(R.id.spinner);
        addToCartButton = findViewById(R.id.pd_add_to_cart_button);
        productImage = findViewById(R.id.product_image_details);
        productName = findViewById(R.id.product_name_details);
        productPrice = findViewById(R.id.product_price_details);
        productShortDescription = findViewById(R.id.product_description_short);
        productDetailedDescription = findViewById(R.id.product_description_detailed);
        quantityText = findViewById(R.id.quantity_text);


        getProductDetails(productID);



        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (state.equals("Order Placed") || state.equals("Order Shipped")) {
//                    Toast.makeText(ProductDetailsActivity.this, "You can add more products, once your order is shipped or confirmed", Toast.LENGTH_SHORT).show();
//                } else {
                    addingToCartList();
                //}
            }
        });

        GettingImageUri(productsRef);

        //Setting up spinner
        dataAdapter = new ArrayAdapter<String>(ProductDetailsActivity.this,R.layout.spinner_item,spinnerData);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                quantityValue = spinnerData[position];
             //   Toast.makeText(ProductDetailsActivity.this, "Position " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void GettingImageUri(DatabaseReference productsRef) {

        productsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Products products = snapshot.getValue(Products.class);
                assert products != null;
                imageUri = products.getImage();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(ProductDetailsActivity.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

       // CheckOrderState();
    }

    private void addingToCartList() {

        final String saveCurrentTime, saveCurrentDate;

        Calendar callForDate = Calendar.getInstance();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(callForDate.getTime());

        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(callForDate.getTime());

        SimpleDateFormat cTime = new SimpleDateFormat("HH:mm:ss:SSS a", Locale.getDefault());
        time1 = cTime.format(callForDate.getTime());

        Log.d("TAG", "addingToCartList: " +Prevalent.currentOnlineUser.getPhone() );
        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        final DatabaseReference addedToCartRef = FirebaseDatabase.getInstance().getReference().child("Added To Cart")
                .child(Prevalent.currentOnlineUser.getPhone())
                .child(sellerID);


        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid", productID);
        cartMap.put("pname", productName.getText().toString());
        cartMap.put("price", productPrice.getText().toString());
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("quantity",quantityValue);
        cartMap.put("discount", "");
        cartMap.put("image", imageUri);
        cartMap.put("sid", sellerID);
        cartMap.put("state", "Not Confirmed");


        final HashMap<String, Object> sellerMap = new HashMap<>();
        sellerMap.put("pid", productID);
        sellerMap.put("pname", productName.getText().toString());
        sellerMap.put("price", productPrice.getText().toString());
        sellerMap.put("date", saveCurrentDate);
        sellerMap.put("time", saveCurrentTime);
        sellerMap.put("quantity", quantityValue);
        sellerMap.put("discount", "");
        sellerMap.put("sid", sellerID);
        sellerMap.put("name", Prevalent.currentOnlineUser.getName());
        sellerMap.put("image", imageUri);
        sellerMap.put("phone", Prevalent.currentOnlineUser.getPhone());
        sellerMap.put("state", "Not Confirmed");

        time1 = time1.replace(".", "");
        //Log.d("", "addingToCartList: " + time1);


        //Log.d("MainActivity", "addingToCartList: " + );

        cartListRef.child("User View").child(Prevalent.currentOnlineUser.getPhone()).
                child("Products").child(productID).updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            cartListRef.child("Admin View").child(Prevalent.currentOnlineUser.getPhone()).
                                    child("Products").child(productID).updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()) {
                                                addedToCartRef.child(Prevalent.currentOnlineUser.getPhone() + productID).updateChildren(sellerMap)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                if (task.isSuccessful()){

                                                                    Toast.makeText(ProductDetailsActivity.this, "Added to Cart List", Toast.LENGTH_SHORT).show();

                                                                    Intent intent = new Intent(ProductDetailsActivity.this, HomeActivity.class);
                                                                    //   intent.putExtra("phone",Prevalent.currentOnlineUser.getPhone());
                                                                    startActivity(intent);

                                                                }

                                                            }
                                                        });
                                            }
                                        }
                                    });
                        }

                    }
                });
    }

    private void getProductDetails(final String productID) {

        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Products");

        productRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    Products products = snapshot.getValue(Products.class);

                 //   quantityText.setText("Qty:");
                    productName.setText(products.getPname());
                    productPrice.setText(products.getPrice());
                    Log.d("TAG", "Short Description: "+ products.getShortDesciption());
                    productShortDescription.setText(products.getShortDesciption());
                    productDetailedDescription.setText(products.getDescription());
                    Picasso.get().load(products.getImage())
                            .resize(800, 0)
                            .centerCrop()
                            .into(productImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void CheckOrderState() {

        DatabaseReference ordersRef;
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone());

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    String shippingState = snapshot.child("state").getValue().toString();

                    if (shippingState.equals("shipped")) {

                        state = "Order Shipped";

                    } else if (shippingState.equals("not shipped")) {

                        state = "Order Placed";

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}