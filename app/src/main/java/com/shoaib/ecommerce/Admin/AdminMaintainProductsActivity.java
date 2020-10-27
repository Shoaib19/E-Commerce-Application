package com.shoaib.ecommerce.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shoaib.ecommerce.R;
import com.shoaib.ecommerce.Sellers.SellerProductCategoryActivity;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainProductsActivity extends AppCompatActivity {

    private Button applyChangesBtn, deleteBtn;
    private EditText name, price, shortDescription, description, category, subCategory, mergeCategory;
    private ImageView imageView;

    private String productID = "";
    private DatabaseReference productsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_products);


        productID = getIntent().getStringExtra("pid");
        productsRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productID);

        applyChangesBtn = findViewById(R.id.apply_changes_button);
        name = findViewById(R.id.product_name_maintain);
        price = findViewById(R.id.product_price_maintain);
        description = findViewById(R.id.product_description_maintain);
        imageView = findViewById(R.id.product_image_maintain);
        shortDescription = findViewById(R.id.product_short_description_maintain);
        category = findViewById(R.id.product_category_maintain);
        subCategory = findViewById(R.id.product_subCategory_maintain);
        mergeCategory = findViewById(R.id.product_merged_Category_maintain);

        deleteBtn = findViewById(R.id.delete_product_btn);

        displaySpecificProductInfo();

        applyChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                applyChanges();

            }
        });


        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteThisProduct();
            }
        });


    }

    private void deleteThisProduct() {

        productsRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Intent i = new Intent(getApplicationContext(), AdminHomeActivity.class);
                startActivity(i);
                finish();

                Toast.makeText(AdminMaintainProductsActivity.this, "Product is deleted Successfully", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void applyChanges() {

        String pName = name.getText().toString();
        String pPrice = price.getText().toString();
        String pDescription = description.getText().toString();
        String pShortDescription = shortDescription.getText().toString();
        String pCategory = category.getText().toString();
        String pSubCategory = subCategory.getText().toString();
        String pMergeCategory = mergeCategory.getText().toString();

        if (pName.equals("")){
            Toast.makeText(this, "Write Product Name", Toast.LENGTH_SHORT).show();
        } else if (pPrice.equals("")) {
            Toast.makeText(this, "Write Product Price", Toast.LENGTH_SHORT).show();
        } else if (pDescription.equals("")){
            Toast.makeText(this, "Write Product Description", Toast.LENGTH_SHORT).show();
        }  else if (pShortDescription.equals("")) {
            Toast.makeText(this, "Write Short Description Price", Toast.LENGTH_SHORT).show();
        } else if (pCategory.equals("")){
            Toast.makeText(this, "Write Product Category", Toast.LENGTH_SHORT).show();
        } else if (pSubCategory.equals("")) {
            Toast.makeText(this, "Write Product Sub Category", Toast.LENGTH_SHORT).show();
        } else if (pMergeCategory.equals("")){
            Toast.makeText(this, "Write Product Merged Category", Toast.LENGTH_SHORT).show();
        }else {
            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put("pid",productID);
            productMap.put("description",pDescription);
            productMap.put("price",pPrice);
            productMap.put("pname",pName);
            productMap.put("shortDesciption",pShortDescription);
            productMap.put("category",pCategory);
            productMap.put("subCategory",pSubCategory);
            productMap.put("mergedCategory",pMergeCategory);

            productsRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){

                        Intent i = new Intent(getApplicationContext(), AdminHomeActivity.class);
                        startActivity(i);
                        finish();
                        Toast.makeText(AdminMaintainProductsActivity.this, "Changes applied Successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void displaySpecificProductInfo() {

        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    String pName = snapshot.child("pname").getValue().toString();
                    String pPrice = snapshot.child("price").getValue().toString();
                    String pDescription = snapshot.child("description").getValue().toString();
                    String pImage = snapshot.child("image").getValue().toString();
                    String pShortDescription = snapshot.child("shortDesciption").getValue().toString();
                    String pCategory= snapshot.child("category").getValue().toString();
                    String pSubCategory = snapshot.child("subCategory").getValue().toString();
                    String pMergedCategory = snapshot.child("mergedCategory").getValue().toString();

                    name.setText(pName);
                    price.setText(pPrice);
                    description.setText(pDescription);
                    shortDescription.setText(pShortDescription);
                    category.setText(pCategory);
                    subCategory.setText(pSubCategory);
                    mergeCategory.setText(pMergedCategory);
                    Picasso.get().load(pImage).into(imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
