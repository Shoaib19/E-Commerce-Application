package com.shoaib.ecommerce.Buyers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.shoaib.ecommerce.Model.SellerNewOrders;
import com.shoaib.ecommerce.Prevalent.Prevalent;
import com.shoaib.ecommerce.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import static com.shoaib.ecommerce.Prevalent.Prevalent.currentOnlineUser;

public class ConfirmFinalOrderActivity extends AppCompatActivity {

    private EditText nameEditText, phoneEditText, addressEditText, cityEditText,pincodeEditText;
    private Button confirmOrderBtn;
    public static int orderNumber = 0;
    private String saveCurrentTime;
    private String totalAmount = "", phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);


        Calendar c = Calendar.getInstance();

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a", Locale.getDefault());
        saveCurrentTime = currentTime.format(c.getTime());

        saveCurrentTime = saveCurrentTime.replace(".","");
        Log.d("Tag", "onCreate: " + saveCurrentTime);

        totalAmount = getIntent().getStringExtra("Total Price");
        phone = getIntent().getStringExtra("phone");
        Toast.makeText(this, "Total Price =" + totalAmount, Toast.LENGTH_LONG).show();

        confirmOrderBtn = findViewById(R.id.confirm_final_order_btn);
        nameEditText = findViewById(R.id.shipment_name);
        phoneEditText = findViewById(R.id.shipment_phone_number);
        addressEditText = findViewById(R.id.shipment_address);
        cityEditText = findViewById(R.id.shipment_city);
        pincodeEditText = findViewById(R.id.shipment_pincode);

        confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                orderNumber += 1;
                Check();
            }
        });

    }

    private void Check() {

        if (TextUtils.isEmpty(nameEditText.getText().toString())) {

            Toast.makeText(this, "Please provide your full name.", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(phoneEditText.getText().toString())) {

            Toast.makeText(this, "Please provide your phone number.", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(addressEditText.getText().toString())) {

            Toast.makeText(this, "Please provide your address.", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(cityEditText.getText().toString())) {

            Toast.makeText(this, "Please provide your city and State name.", Toast.LENGTH_SHORT).show();

        }else if (TextUtils.isEmpty(pincodeEditText.getText().toString())) {

            Toast.makeText(this, "Please provide your Pincode", Toast.LENGTH_SHORT).show();

        } else {

            ConfirmOrder();
        }
    }


    private void ConfirmOrder() {

        final String saveCurrentTime, saveCurrentDate;
        Calendar callForDate = Calendar.getInstance();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentDate = new SimpleDateFormat("dd MM yyyy");
        saveCurrentDate = currentDate.format(callForDate.getTime());

        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(callForDate.getTime());

        final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(currentOnlineUser.getPhone());

        final DatabaseReference addedToCartRef = FirebaseDatabase.getInstance().getReference()
                .child("Added To Cart").child(currentOnlineUser.getPhone());

        final DatabaseReference sellerOrdersRef = FirebaseDatabase.getInstance().getReference()
                .child("Seller Orders");

        HashMap<String, Object> ordersMap = new HashMap<>();
        ordersMap.put("totalAmount", totalAmount);
        ordersMap.put("name", nameEditText.getText().toString());
        ordersMap.put("phone", phoneEditText.getText().toString());
        ordersMap.put("date", saveCurrentDate);
        ordersMap.put("time", saveCurrentTime);
        ordersMap.put("address", addressEditText.getText().toString());
        ordersMap.put("pincode",pincodeEditText.getText().toString());
        ordersMap.put("city", cityEditText.getText().toString());
        ordersMap.put("state", "not shipped");

        updateRecord();
        myOrdersUpdate();


        ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    FirebaseDatabase.getInstance().getReference().child("Cart List")
                            .child("User View")
                            .child(currentOnlineUser.getPhone())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        addedToCartRef
                                                .removeValue()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Toast.makeText(ConfirmFinalOrderActivity.this, "Order Placed Successfully", Toast.LENGTH_SHORT).show();

                                                        Intent intent = new Intent(ConfirmFinalOrderActivity.this, HomeActivity.class);
                                                        intent.putExtra("confirm", "confirm");
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                });

                                    }
                                }
                            });


                }
            }
        });

    }

    private void myOrdersUpdate() {
    }

    public void updateRecord() {

        final DatabaseReference addedToCartRef = FirebaseDatabase.getInstance().getReference().child("Added To Cart")
                .child(currentOnlineUser.getPhone());

        addedToCartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    final DatabaseReference sellerOrder = FirebaseDatabase.getInstance().getReference().child("Seller Orders");
                    final DatabaseReference myOrdersRef = FirebaseDatabase.getInstance().getReference().child("My Orders");

                    for (final DataSnapshot d1 : snapshot.getChildren()) {

                        addedToCartRef.child(String.valueOf(d1.getKey())).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    for (final DataSnapshot d2 : snapshot.getChildren()) {

                                        //Updating the seller Order node with the confirmed order
                                        //and adding the address provided by user in it.
                                        String name = d2.child("name").getValue().toString();
                                        String pid = d2.child("pid").getValue().toString();
                                        String pname = d2.child("pname").getValue().toString();
                                        String price = d2.child("price").getValue().toString();
                                        String date = d2.child("date").getValue().toString();
                                        String time = d2.child("time").getValue().toString();
                                        String quantity = d2.child("quantity").getValue().toString();
                                        String discount = d2.child("discount").getValue().toString();
                                        String image = d2.child("image").getValue().toString();
                                        String phone = d2.child("phone").getValue().toString();
                                        String state = d2.child("state").getValue().toString();
                                        String sid = d2.child("sid").getValue().toString();

                                        HashMap<String, Object> sellerOrderMap = new HashMap<>();
                                        sellerOrderMap.put("pid", pid);
                                        sellerOrderMap.put("pname", pname);
                                        sellerOrderMap.put("price", price);
                                        sellerOrderMap.put("date", date);
                                        sellerOrderMap.put("time", time);
                                        sellerOrderMap.put("quantity", quantity);
                                        sellerOrderMap.put("discount", discount);
                                        sellerOrderMap.put("address", addressEditText.getText().toString());
                                        sellerOrderMap.put("city",cityEditText.getText().toString());
                                        sellerOrderMap.put("name", name);
                                        sellerOrderMap.put("image", image);
                                        sellerOrderMap.put("phone", phone);
                                        sellerOrderMap.put("state", state);
                                        sellerOrderMap.put("sid", sid);
                                        sellerOrderMap.put("pincode",pincodeEditText.getText().toString());

                                        if (d1.getKey()!=null && d2.getKey() != null) {

                                            sellerOrder.child(d1.getKey()).child(date + saveCurrentTime + d2.getKey()).updateChildren(sellerOrderMap);
                                        }else {

                                            Toast.makeText(ConfirmFinalOrderActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                                        }

                                        HashMap<String, Object> addressMap = new HashMap<>();
                                        addressMap.put("address", addressEditText.getText().toString() + ", " + cityEditText.getText().toString());

                                        addedToCartRef.child(String.valueOf(d1.getKey()))
                                                .child(String.valueOf(d2.getKey()))
                                                .updateChildren(addressMap).addOnCompleteListener(new OnCompleteListener<Void>() {

                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                }
                                            }
                                        });

                                        //Adding the orders in seller my orders list
                                        myOrdersRef.child(Prevalent.currentOnlineUser.getPhone())
                                                .child(date + saveCurrentTime + Prevalent.currentOnlineUser.getPhone() + pid)
                                                .updateChildren(sellerOrderMap)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()){

                                                    Toast.makeText(ConfirmFinalOrderActivity.this, "You can see your order status in My Orders", Toast.LENGTH_SHORT).show();
                                                }
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

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
