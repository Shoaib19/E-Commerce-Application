package com.shoaib.ecommerce.Buyers;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Html;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shoaib.ecommerce.Admin.AdminMaintainProductsActivity;
import com.shoaib.ecommerce.Model.Products;
import com.shoaib.ecommerce.R;
import com.shoaib.ecommerce.ViewHolder.ProductViewHolder;
import com.squareup.picasso.Picasso;

public class ProductListActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private static final String STATE_LIST = "State Adapter Data";
    private Parcelable mListState;
    private DatabaseReference ProductRef;
    private String keyOne, keyTwo;
    private TextView emptyTextView;
    private Toolbar toolbar;

    private String rupeeSymbol = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        emptyTextView = findViewById(R.id.empty_page_textView);

        toolbar = findViewById(R.id.toolbarProductList);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();

        if (intent.getStringExtra("keyOne") != null) {
            keyOne = intent.getStringExtra("keyOne");
        } else if (intent.getStringExtra("keyTwo") != null) {
            keyTwo = intent.getStringExtra("keyTwo");
        } else {
            Toast.makeText(this, "Error Try Again", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        rupeeSymbol = String.valueOf(Html.fromHtml("\u20B9"));
        recyclerView = findViewById(R.id.recycler_product_list);


        ProductRef = FirebaseDatabase.getInstance().getReference().child("Products");


        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    }

    @Override
    protected void onStart() {
        super.onStart();


        //Retaining the position of recycler View after clicking on any Item.
        if (mListState != null) {
            layoutManager.onRestoreInstanceState(mListState);
        } else {

            FirebaseRecyclerOptions<Products> options;

            //If User is looking for a particular product category.
            if (keyTwo != null) {

                options = new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ProductRef.orderByChild("mergedCategory").equalTo(keyTwo), Products.class)
                        .build();
            } else {
                options = new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ProductRef.orderByChild("category").equalTo(keyOne), Products.class)
                        .build();
            }

            final FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                    new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {


                        @Override
                        protected void onBindViewHolder(@NonNull final ProductViewHolder holder, final int position, @NonNull final Products model) {

                            holder.txtProductName.setText(model.getPname());
                            holder.txtProductDescription.setText(model.getShortDesciption());
                            holder.txtProductPrice.setText(rupeeSymbol + model.getPrice());
                            Picasso.get().load(model.getImage())
                                    .into(holder.imageView);


                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                @Override
                                public void onClick(View v) {

                                    Intent intent = new Intent(ProductListActivity.this, ProductDetailsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    intent.putExtra("sid", model.getSid());

                                    holder.imageView.setTransitionName("imageTransition");
                                    Pair[] pairs = new Pair[1];
                                    ActivityOptions options = null;
                                    pairs[0] = new Pair<View, String>(holder.imageView, "imageTransition");
                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                        options = ActivityOptions.makeSceneTransitionAnimation(ProductListActivity.this, pairs);
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
                            if (getItemCount() == 0) {
                                emptyTextView.setVisibility(View.VISIBLE);
                            } else {
                                emptyTextView.setVisibility(View.GONE);
                            }
                        }
                    };

            recyclerView.setAdapter(adapter);
            adapter.startListening();


        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mListState = layoutManager.onSaveInstanceState();
        outState.putParcelable(STATE_LIST, mListState);
    }

    @Override
    public void onRestoreInstanceState(Bundle outState) {
        super.onRestoreInstanceState(outState);

        if (outState != null) {
            mListState = outState.getParcelable(STATE_LIST);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mListState != null) {
            layoutManager.onRestoreInstanceState(mListState);
        }
    }


}


