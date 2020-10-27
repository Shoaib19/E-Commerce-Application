package com.shoaib.ecommerce.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shoaib.ecommerce.Buyers.ProductDetailsActivity;
import com.shoaib.ecommerce.Buyers.ProductListActivity;
import com.shoaib.ecommerce.Model.Products;
import com.shoaib.ecommerce.R;
import com.shoaib.ecommerce.ViewHolder.ProductViewHolder;
import com.squareup.picasso.Picasso;

public class AdminProductListActivity extends AppCompatActivity {

    private DatabaseReference productRef;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product_list);


        productRef = FirebaseDatabase.getInstance().getReference();
        recyclerView = findViewById(R.id.admin_recycler_product_list);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(productRef.child("Products"), Products.class)
                .build();


        final FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {



                    @Override
                    protected void onBindViewHolder(@NonNull final ProductViewHolder holder, final int position, @NonNull final Products model) {

                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductDescription.setText(model.getShortDesciption());
                        holder.txtProductPrice.setText("Rs " +  model.getPrice());
                        Picasso.get().load(model.getImage())
                                .into(holder.imageView);


                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                            @Override
                            public void onClick(View v) {

//                                    if (type.equals("Admin")) {
//
//                                        Intent i = new Intent(ProductListActivity.this, AdminMaintainProductsActivity.class);
//                                        i.putExtra("pid", model.getPid());
//                                        startActivity(i);
//                                    } else {
//                                    Intent i = new Intent(HomeActivity.this, ProductDetailsActivity.class);
//                                    i.putExtra("pid", model.getPid());
//                                    i.putExtra("sid", model.getSid());
//                                    startActivity(i);
//
                                Intent intent = new Intent(AdminProductListActivity.this, AdminMaintainProductsActivity.class);
                                intent.putExtra("pid", model.getPid());
                             //   intent.putExtra("sid", model.getSid());
                                //   intent.putExtra(ProductDetailsActivity., object);

                                holder.imageView.setTransitionName("imageTransition");
                                Pair[] pairs = new Pair[1];
                                ActivityOptions options = null;
                                pairs[0] = new Pair<View, String>(holder.imageView, "imageTransition");
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                    options = ActivityOptions.makeSceneTransitionAnimation(AdminProductListActivity.this, pairs);
                                }
                                if (options != null) {
                                    startActivity(intent, options.toBundle());
                                } else {
                                    startActivity(intent);
                                }
                            }
                            //   }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);

                        return holder;

                    }

                    @Override
                    public int getItemViewType(int position) {
                        return super.getItemViewType(position);
                    }

                    @Override
                    public long getItemId(int position) {
                        return super.getItemId(position);
                    }

                    @Override
                    public void onDataChanged() {
                        super.onDataChanged();


                        //If recycler View is empty we will show a text message
                        //If the data is empty, show text message
//                        if (getItemCount()==0) {
//                            emptyTextView.setVisibility(View.VISIBLE);
//                        } else {
//                            emptyTextView.setVisibility(View.GONE);
//                        }
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();



    }
}