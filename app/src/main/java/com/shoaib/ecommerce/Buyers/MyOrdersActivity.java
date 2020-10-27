package com.shoaib.ecommerce.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shoaib.ecommerce.Model.BuyerAllOrders;
import com.shoaib.ecommerce.Model.Cart;
import com.shoaib.ecommerce.Prevalent.Prevalent;
import com.shoaib.ecommerce.R;
import com.shoaib.ecommerce.ViewHolder.BuyerAllOrdersViewHolder;
import com.shoaib.ecommerce.ViewHolder.CartViewHolder;
import com.squareup.picasso.Picasso;

public class MyOrdersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    String rupeeSymbol = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);


        rupeeSymbol = String.valueOf(Html.fromHtml("\u20B9"));

        recyclerView = findViewById(R.id.my_orders_recyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Getting the reference of the My Orders Node
        final DatabaseReference myOrdersRef = FirebaseDatabase.getInstance().getReference()
                .child("My Orders");

        FirebaseRecyclerOptions<BuyerAllOrders> options = new FirebaseRecyclerOptions.Builder<BuyerAllOrders>()
                .setQuery(myOrdersRef.child(Prevalent.currentOnlineUser.getPhone()), BuyerAllOrders.class).
                        build();

        FirebaseRecyclerAdapter<BuyerAllOrders, BuyerAllOrdersViewHolder> adapter = new FirebaseRecyclerAdapter<BuyerAllOrders, BuyerAllOrdersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull BuyerAllOrdersViewHolder holder, int position, @NonNull BuyerAllOrders model) {
                holder.txtProductQuantity.setText("Qty:" + model.getQuantity());
                holder.txtProductPrice.setText(rupeeSymbol + model.getPrice());
                holder.txtProductName.setText(model.getPname());
                holder.txtProductSellerName.setText("Sold By: Not disclosed");

                holder.txtProductStatus.setText(model.getState());

                holder.txtProductDate.setText(model.getDate());
                holder.txtProductTime.setText(model.getTime());
                holder.txtProductBuyerName.setText(model.getName());
                holder.txtProductAddress.setText(model.getAddress());
                holder.txtProductCity.setText(model.getCity());
                holder.txtProductPincode.setText(model.getPincode());

                Picasso.get().load(model.getImage())
                        .into(holder.imgProductImage);
            }

            @NonNull
            @Override
            public BuyerAllOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_orders_list_layout, parent, false);
                BuyerAllOrdersViewHolder holder = new BuyerAllOrdersViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
}
