package com.shoaib.ecommerce.Sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shoaib.ecommerce.Model.AdminOrders;
import com.shoaib.ecommerce.Model.SellerNewOrders;
import com.shoaib.ecommerce.Prevalent.Prevalent;
import com.shoaib.ecommerce.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class SellerNewOrdersActivity extends AppCompatActivity {

    private RecyclerView ordersList;
    private DatabaseReference sellerOrderRef;
    private DatabaseReference ordersRefAdmin;
    private DatabaseReference myOrdersRef;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_new_orders);

        sellerOrderRef = FirebaseDatabase.getInstance().getReference().child("Seller Orders");
        ordersRefAdmin = FirebaseDatabase.getInstance().getReference().child("Cart List").child("Admin View");
        myOrdersRef = FirebaseDatabase.getInstance().getReference().child("My Orders");


        ordersList = findViewById(R.id.seller_orders_list);
        ordersList.setLayoutManager(new LinearLayoutManager(SellerNewOrdersActivity.this));

        CheckOrderConfirmation();


    }

    private void CheckOrderConfirmation() {
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Log.d("MAin", "onStart: " + "Chal to rha hei honga");

        DatabaseReference df = FirebaseDatabase.getInstance().getReference().child("Seller Orders");


        FirebaseRecyclerOptions<SellerNewOrders> options =
                new FirebaseRecyclerOptions.Builder<SellerNewOrders>()
                        .setQuery(df.child(FirebaseAuth.getInstance().getCurrentUser().getUid()), SellerNewOrders.class)
                        .build();

        //Log.d("MAin", "onStart: " + df.orderByChild("sid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()));


        FirebaseRecyclerAdapter<SellerNewOrders, SellerOrdersViewHolder> adapter =
                new FirebaseRecyclerAdapter<SellerNewOrders, SellerOrdersViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull SellerOrdersViewHolder holder, final int position, @NonNull SellerNewOrders model) {


                        holder.userName.setText(model.getName());
                        holder.userPhoneNumber.setText("Phone: " + model.getPhone());
                        holder.userTotalPrice.setText(model.getPrice() + "Rs");
                        holder.userDateTime.setText("Time: " + model.getDate());
                        holder.userShippingAddress.setText("Address: " + model.getAddress() + model.getCity());
                        holder.productPincode.setText("Pincode: " + model.getPincode());
                        holder.productName.setText(model.getPname());

                        holder.productStatus.setText("Status: " + model.getState());

                        Picasso.get().load(model.getImage()).into(holder.productImage);
                        holder.productQuantity.setText("Qty:" + model.getQuantity());



                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CharSequence options[] = new CharSequence[]
                                        {
                                                "Canceled",
                                                "Confirmed",
                                                "Shipping",
                                                "Shipped",
                                                "Delivered"
                                        };
                                AlertDialog.Builder builder = new AlertDialog.Builder(SellerNewOrdersActivity.this);
                                builder.setTitle("Have you shipped this order products ?");

                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        String uID;

                                        if (which == 0) {
                                            uID = getRef(position).getKey();
                                            updateToDatabase(uID,"Cancelled");
                                        } else if (which == 1) {
                                            uID = getRef(position).getKey();

                                            updateToDatabase(uID,"Confirmed");
                                        }else if (which == 2) {
                                            uID = getRef(position).getKey();

                                            updateToDatabase(uID,"Shipping");
                                        } else if (which == 3) {
                                            uID = getRef(position).getKey();

                                            updateToDatabase(uID,"Shipped");
                                        } else if (which == 4) {
                                            uID = getRef(position).getKey();

                                            updateToDatabase(uID,"Delivered");
                                        }  else {
                                            finish();
                                        }
                                    }


                                });

                                builder.show();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public SellerOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_new_orders_view, parent, false);
                        return new SellerOrdersViewHolder(view);
                    }
                };
        ordersList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class SellerOrdersViewHolder extends RecyclerView.ViewHolder {

        public TextView userName, userPhoneNumber, userTotalPrice, userDateTime,
                userShippingAddress, productQuantity, productName, productPincode, productStatus;
        private ImageView productImage;


        public SellerOrdersViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.confirm_order_buyer_name);
            userPhoneNumber = itemView.findViewById(R.id.confirm_order_buyer_phone);
            userTotalPrice = itemView.findViewById(R.id.confirm_order_product_price);
            userDateTime = itemView.findViewById(R.id.confirm_order_buyer_time);
            userShippingAddress = itemView.findViewById(R.id.confirm_order_buyer_address);
            productQuantity = itemView.findViewById(R.id.confirm_order_product_quantity);
            productName = itemView.findViewById(R.id.confirm_order_product_name);
            productImage = itemView.findViewById(R.id.confirm_order_product_image);
            productPincode = itemView.findViewById(R.id.confirm_order_buyer_address_pincode);
            productStatus = itemView.findViewById(R.id.confirm_order_buyer_status);

        }
    }

    private void RemoveOrder(String uID) {

        sellerOrderRef.child(uID).removeValue();
        ordersRefAdmin.child(uID).removeValue();
    }

    private void updateToDatabase(final String uID, final String status) {

        final HashMap<String, Object> statusRef = new HashMap<>();

        statusRef.put("state",status);

        myOrdersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    for (final DataSnapshot d1 : snapshot.getChildren()) {
                        if (d1.getKey() != null){
                            myOrdersRef.child(d1.getKey()).child(uID).updateChildren(statusRef).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    sellerOrderRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(uID).updateChildren(statusRef).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(SellerNewOrdersActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                        }

                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });

    }

}
