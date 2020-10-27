package com.shoaib.ecommerce.Sellers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shoaib.ecommerce.R;

import java.sql.Array;
import java.util.ArrayList;

public class SellerChooseMiddleLevelCategoryActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> listData = new ArrayList<>();
    String category;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_choose_middle_level_category);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Products Category");
        listView = findViewById(R.id.listView_choose_category);

        category = getIntent().getStringExtra("clickedCategory");

        if (category != null) {


            switch (category) {
                case "Men":
                    listData.add("Shirts");
                    listData.add("TShirts");
                    listData.add("Shorts");
                    listData.add("Jeans");
                    listData.add("Trouser");
                    listData.add("Jackets");
                    listData.add("Suits");
                    listData.add("Sneakers");
                    listData.add("Footwear");
                    listData.add("Sunglasses");
                    listData.add("Sports Wear");
                    listData.add("Other");

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(SellerChooseMiddleLevelCategoryActivity.this,
                            android.R.layout.simple_list_item_1, android.R.id.text1, listData);

                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String item = listData.get(position);
                            Intent intent = new Intent(SellerChooseMiddleLevelCategoryActivity.this, SellerAddNewProductActivity.class);
                            intent.putExtra("mainCategory", "Men");
                            intent.putExtra("subCategory", item);

                            startActivity(intent);

                        }
                    });
                    break;
                case "Women":
                    listData.add("Kurtas");
                    listData.add("Dresses");
                    listData.add("Tops");
                    listData.add("TShirts");
                    listData.add("Shorts & Trousers");
                    listData.add("Sarees");
                    listData.add("Shoes");
                    listData.add("Flip-Flops");
                    listData.add("Jeans");
                    listData.add("Innerwear & Sleepwear");
                    listData.add("Sunglasses");
                    listData.add("Other");

                    ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(SellerChooseMiddleLevelCategoryActivity.this,
                            android.R.layout.simple_list_item_1, android.R.id.text1, listData);

                    listView.setAdapter(adapter1);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String item = listData.get(position);
                            Intent intent = new Intent(SellerChooseMiddleLevelCategoryActivity.this, SellerAddNewProductActivity.class);
                            intent.putExtra("mainCategory", "Women");
                            intent.putExtra("subCategory", item);

                            startActivity(intent);

                        }
                    });

                    break;
                case "Boy":
                    listData.add("Bodysuit");
                    listData.add("Footwear");
                    listData.add("Jackets");
                    listData.add("Jeans");
                    listData.add("Shorts & Trousers");
                    listData.add("Shirts");
                    listData.add("Sleepsuit");
                    listData.add("TShirts");
                    listData.add("Leggings");
                    listData.add("Dresses");
                    listData.add("Jeggings");
                    listData.add("Other");

                    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(SellerChooseMiddleLevelCategoryActivity.this,
                            android.R.layout.simple_list_item_1, android.R.id.text1, listData);

                    listView.setAdapter(adapter2);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String item = listData.get(position);
                            Intent intent = new Intent(SellerChooseMiddleLevelCategoryActivity.this, SellerAddNewProductActivity.class);
                            intent.putExtra("mainCategory", "Boy");
                            intent.putExtra("subCategory", item);

                            startActivity(intent);

                        }
                    });
                    break;
                case "Girl":
                    listData.add("Bodysuit");
                    listData.add("Capris");
                    listData.add("SweatShirts");
                    listData.add("Coats");
                    listData.add("Dresses");
                    listData.add("Hat");
                    listData.add("Casual Shoes");
                    listData.add("Flip Flops");
                    listData.add("Heels");
                    listData.add("Trouser");
                    listData.add("Tops");
                    listData.add("Jackets");
                    listData.add("Jeans");
                    listData.add("Kurta Sets");
                    listData.add("Lehenga Choli");
                    listData.add("Shirts");
                    listData.add("TShirts");
                    listData.add("Nightdress");
                    listData.add("Palazzos");
                    listData.add("Skirts");
                    listData.add("Other");

                    ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(SellerChooseMiddleLevelCategoryActivity.this,
                            android.R.layout.simple_list_item_1, android.R.id.text1, listData);

                    listView.setAdapter(adapter3);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String item = listData.get(position);
                            Intent intent = new Intent(SellerChooseMiddleLevelCategoryActivity.this, SellerAddNewProductActivity.class);
                            intent.putExtra("mainCategory", "Girl");
                            intent.putExtra("subCategory", item);

                            startActivity(intent);

                        }
                    });
                    break;
            }

        }


    }
}
