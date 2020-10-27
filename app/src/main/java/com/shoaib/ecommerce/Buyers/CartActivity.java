package com.shoaib.ecommerce.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shoaib.ecommerce.Model.Cart;
import com.shoaib.ecommerce.Prevalent.Prevalent;
import com.shoaib.ecommerce.R;
import com.shoaib.ecommerce.ViewHolder.CartViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity{

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private Button nextProcessButton;
    private TextView txtTotalAmount, txtMsg1, totalPriceTxt;
    private int oneTypeProductPrice = 0;
    private int orderTotalPrice = 0;
    private int totalPrice = 0;
    private String rupeeSymbol = null;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        toolbar = findViewById(R.id.toolbarCart);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        nextProcessButton = findViewById(R.id.next_process_btn);
        txtTotalAmount = findViewById(R.id.title);
        txtMsg1 = findViewById(R.id.msg1);
        totalPriceTxt = findViewById(R.id.cart_total_price);
        rupeeSymbol = String.valueOf(Html.fromHtml("\u20B9"));



        getTotalPrice();
        if (totalPrice != 0) {
            Log.d("TAG", "onCreate:TotalPrice " + totalPrice);
        }
        nextProcessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                txtTotalAmount.setText("Total Price = " + orderTotalPrice);

                Intent intent = new Intent(CartActivity.this, ConfirmFinalOrderActivity.class);
                intent.putExtra("Total Price", String.valueOf(orderTotalPrice));
                startActivity(intent);
                finish();
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();

       // CheckOrderState();
        CheckCartState();


        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        final DatabaseReference addedToCartRef = FirebaseDatabase.getInstance().getReference().child("Added To Cart")
                .child(Prevalent.currentOnlineUser.getPhone());

        final DatabaseReference myOrdersRef = FirebaseDatabase.getInstance().getReference()
                .child("My Orders")
                .child(Prevalent.currentOnlineUser.getPhone());

        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>().setQuery(cartListRef.child("User View")
                .child(Prevalent.currentOnlineUser.getPhone()).child("Products"), Cart.class).
                build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Cart model) {

                holder.txtProductQuantity.setText("Qty:"  + model.getQuantity() );
                holder.txtProductPrice.setText(rupeeSymbol + model.getPrice());
                holder.txtProductName.setText(model.getPname());
                holder.textSellerName.setText("Sold By: Not disclosed");
                oneTypeProductPrice = ((Integer.parseInt(model.getPrice()))) * Integer.parseInt(model.getQuantity());
                orderTotalPrice += oneTypeProductPrice;

                holder.txtProductPrice.setText(rupeeSymbol + oneTypeProductPrice);
                Picasso.get().load(model.getImage())
                        .into(holder.imgProductImage);



                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[] = new CharSequence[]{
                                "Edit",
                                "Remove"
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Cart Options:");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    Intent intent = new Intent(CartActivity.this, ProductDetailsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    intent.putExtra("sid", model.getSid());
                                    startActivity(intent);
                                }
                                if (which == 1) {
                                    cartListRef.child("User View")
                                            .child(Prevalent.currentOnlineUser.getPhone())
                                            .child("Products")
                                            .child(model.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(CartActivity.this, "Item Removed Successfully", Toast.LENGTH_SHORT).show();

                                                        //Refreshing the activity with less blinking
                                                        finish();
                                                        overridePendingTransition(0, 0);
                                                        startActivity(getIntent());
                                                        overridePendingTransition(0, 0);
                                                    }
                                                }
                                            });

                                    cartListRef.child("Admin View")
                                            .child(Prevalent.currentOnlineUser.getPhone())
                                            .child("Products")
                                            .child(model.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {

                                                    }
                                                }
                                            });

                                    //Removing the item from add to cart list in firebase Database
                                    addedToCartRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                for (final DataSnapshot d1 : snapshot.getChildren()) {
                                                    addedToCartRef.child(String.valueOf(d1.getKey()))
                                                            .child(Prevalent.currentOnlineUser.getPhone() + model.getPid())
                                                            .removeValue();

                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                }
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


    private void CheckOrderState() {

        DatabaseReference ordersRef;
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone());

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    String shippingState = snapshot.child("state").getValue().toString();
                    String userName = snapshot.child("name").getValue().toString();

                    if (shippingState.equals("shipped")) {

                        txtTotalAmount.setText("Dear " + userName + "\n your order is shipped successfully");
                        recyclerView.setVisibility(View.GONE);

                        txtMsg1.setVisibility(View.VISIBLE);
                        txtMsg1.setText("Congratulations, Your Final Order has been Shipped, Soon you will receive your order");
                        nextProcessButton.setVisibility(View.GONE);
                        totalPriceTxt.setVisibility(View.GONE);

                        Toast.makeText(CartActivity.this, "You can purchase more once you received your final order", Toast.LENGTH_SHORT).show();

                    } else if (shippingState.equals("not shipped")) {

                        txtTotalAmount.setText("Shipping State = Not Shipped");
                        recyclerView.setVisibility(View.GONE);

                        txtMsg1.setVisibility(View.VISIBLE);
                        txtMsg1.setText("Congratulations, Your Final Order has been placed, soon it will be verified");

                        nextProcessButton.setVisibility(View.GONE);

                        Toast.makeText(CartActivity.this, "You can purchase more once you received your final order", Toast.LENGTH_SHORT).show();


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void CheckCartState() {

        DatabaseReference dRef;
        dRef = FirebaseDatabase.getInstance().getReference().child("Cart List")
                .child("User View").child(Prevalent.currentOnlineUser.getPhone()).child("Products");

        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    txtTotalAmount.setText("Your Orders");
                    nextProcessButton.setVisibility(View.VISIBLE);
                    totalPriceTxt.setVisibility(View.VISIBLE);
                    txtMsg1.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    //getting the total price of the product from our database
    private void getTotalPrice() {

        final DatabaseReference userViewRef = FirebaseDatabase.getInstance().getReference().child("Cart List")
                .child("User View")
                .child(Prevalent.currentOnlineUser.getPhone())
                .child("Products");

        userViewRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {


                    for (final DataSnapshot d1 : snapshot.getChildren()) {
                        Log.d("TAG", "onDataChange: " + d1.getKey());

                        userViewRef.child(d1.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if (snapshot.exists()) {
                                    int price = Integer.parseInt(snapshot.child("price").getValue().toString());
                                    int quantity = Integer.parseInt(snapshot.child("quantity").getValue().toString());
                                    price = price*quantity;
                                    totalPrice = totalPrice + price;
                                    // totalPriceTxt.setTypeface();
                                    totalPriceTxt.setText(rupeeSymbol + totalPrice);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }

}
