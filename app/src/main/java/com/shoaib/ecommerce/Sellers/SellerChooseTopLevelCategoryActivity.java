package com.shoaib.ecommerce.Sellers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shoaib.ecommerce.R;

public class SellerChooseTopLevelCategoryActivity extends AppCompatActivity {

    ImageView imageMen,imageWomen,imageBoy,imageGirl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_choose_top_level_category);

        imageMen = findViewById(R.id.image_men);
        imageWomen = findViewById(R.id.image_women);
        imageBoy = findViewById(R.id.image_boy);
        imageGirl = findViewById(R.id.image_girl);


        imageMen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SellerChooseTopLevelCategoryActivity.this,SellerChooseMiddleLevelCategoryActivity.class);
                intent.putExtra("clickedCategory","Men");
                startActivity(intent);

            }
        });

        imageWomen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SellerChooseTopLevelCategoryActivity.this,SellerChooseMiddleLevelCategoryActivity.class);
                intent.putExtra("clickedCategory","Women");
                startActivity(intent);

            }
        });


        imageBoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SellerChooseTopLevelCategoryActivity.this,SellerChooseMiddleLevelCategoryActivity.class);
                intent.putExtra("clickedCategory","Boy");
                startActivity(intent);
            }
        });


        imageGirl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SellerChooseTopLevelCategoryActivity.this,SellerChooseMiddleLevelCategoryActivity.class);
                intent.putExtra("clickedCategory","Girl");
                startActivity(intent);
            }
        });



    }
}
