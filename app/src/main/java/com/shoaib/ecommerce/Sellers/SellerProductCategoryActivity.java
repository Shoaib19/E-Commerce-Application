package com.shoaib.ecommerce.Sellers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.shoaib.ecommerce.R;
import com.shoaib.ecommerce.Sellers.SellerAddNewProductActivity;

public class SellerProductCategoryActivity extends AppCompatActivity {

    private ImageView tShirts, sportsTShirts, femaleDresses, sweaters;
    private ImageView glasses, hatsCaps, walletsBagsPurses,shoes;
    private  ImageView headPhonesHandFree, laptops, watches, mobilePhones;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_product_category);



        tShirts = findViewById(R.id.t_shirts);
        sportsTShirts = findViewById(R.id.sports_t_shirts);
        femaleDresses = findViewById(R.id.female_dresses);
        sweaters = findViewById(R.id.sweaters);

        glasses = findViewById(R.id.glasses);
        hatsCaps = findViewById(R.id.hats_caps);
        walletsBagsPurses = findViewById(R.id.purses_bag_wallets);
        shoes = findViewById(R.id.shoes);

        headPhonesHandFree = findViewById(R.id.headphones_handfree);
        laptops = findViewById(R.id.laptop_pc);
        watches = findViewById(R.id.watches);
        mobilePhones = findViewById(R.id.mobilephones);

        tShirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SellerAddNewProductActivity.class);
                i.putExtra("category","tShirts");
                startActivity(i);

            }
        });


        sportsTShirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SellerAddNewProductActivity.class);
                i.putExtra("category","Sports tShirts");
                startActivity(i);

            }
        });


        femaleDresses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SellerAddNewProductActivity.class);
                i.putExtra("category","image_female Dresses");
                startActivity(i);

            }
        });


        sweaters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SellerAddNewProductActivity.class);
                i.putExtra("category","Sweaters");
                startActivity(i);

            }
        });




        glasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SellerAddNewProductActivity.class);
                i.putExtra("category","Glasses");
                startActivity(i);

            }
        });
        hatsCaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SellerAddNewProductActivity.class);
                i.putExtra("category","Hats Caps");
                startActivity(i);

            }
        });
        walletsBagsPurses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SellerAddNewProductActivity.class);
                i.putExtra("category","Wallets Bage Purses");
                startActivity(i);

            }
        });
        shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SellerAddNewProductActivity.class);
                i.putExtra("category","Shoes");
                startActivity(i);

            }
        });



        headPhonesHandFree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SellerAddNewProductActivity.class);
                i.putExtra("category","HeadPhones HandFree");
                startActivity(i);

            }
        });
        laptops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SellerAddNewProductActivity.class);
                i.putExtra("category","Laptops");
                startActivity(i);

            }
        });
        watches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SellerAddNewProductActivity.class);
                i.putExtra("category","Watches");
                startActivity(i);

            }
        });
        mobilePhones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SellerAddNewProductActivity.class);
                i.putExtra("category","Mobile Phones");
                startActivity(i);

            }
        });


    }
}
