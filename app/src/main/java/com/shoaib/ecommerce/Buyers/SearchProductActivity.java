package com.shoaib.ecommerce.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shoaib.ecommerce.Model.Products;
import com.shoaib.ecommerce.R;
import com.shoaib.ecommerce.ViewHolder.ProductViewHolder;
import com.squareup.picasso.Picasso;

public class SearchProductActivity extends AppCompatActivity {

    private Button searchBtn;
    private EditText inputText;
    private RecyclerView searchList;
    private String searchInput;
    private ProgressBar progressBar;
    private TextView emptySearchedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);


        inputText = findViewById(R.id.search_product_name);
        searchBtn = findViewById(R.id.search_btn);
        searchList = findViewById(R.id.search_list);
        progressBar = findViewById(R.id.progressBar);
        emptySearchedText = findViewById(R.id.searched_product_empty);
        searchList.setLayoutManager(new LinearLayoutManager(SearchProductActivity.this));



        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);
                searchInput = inputText.getText().toString();
                onStart();
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Products");

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery( reference.orderByChild("pname").startAt(searchInput), Products.class)
                .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model) {

                holder.txtProductName.setText(model.getPname());
                holder.txtProductDescription.setText(model.getShortDesciption());
                holder.txtProductPrice.setText("Rs " +  model.getPrice());
                Picasso.get().load(model.getImage()).into(holder.imageView);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(SearchProductActivity.this, ProductDetailsActivity.class);
                        i.putExtra("sid",model.getSid());
                        i.putExtra("pid",model.getPid());
                        startActivity(i);
                    }
                });
            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout,parent,false);
                ProductViewHolder holder = new ProductViewHolder(view);

                return holder;
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();

                if (getItemCount()==0){
                    progressBar.setVisibility(View.GONE);
                    emptySearchedText.setVisibility(View.VISIBLE);
                }else {
                    progressBar.setVisibility(View.GONE);
                    emptySearchedText.setVisibility(View.GONE);
                }
            }
        };

        searchList.setAdapter(adapter);
        adapter.startListening();

    }
}
