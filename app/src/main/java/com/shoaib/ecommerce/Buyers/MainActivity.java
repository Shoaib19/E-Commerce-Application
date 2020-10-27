package com.shoaib.ecommerce.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shoaib.ecommerce.Model.Users;
import com.shoaib.ecommerce.Prevalent.Prevalent;
import com.shoaib.ecommerce.R;
import com.shoaib.ecommerce.Sellers.SellerHomeActivity;
import com.shoaib.ecommerce.Sellers.SellerRegistrationActivity;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    Button joinNowButton,loginButton;
    private ProgressDialog loadingBar,networkCheck;
    private TextView sellerBegin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        joinNowButton = findViewById(R.id.main_join_now_btn);
        loginButton = findViewById(R.id.main_login_btn);
        loadingBar = new ProgressDialog(this);
        networkCheck = new ProgressDialog(this);
        sellerBegin = findViewById(R.id.seller_begin);


        checkConnectivity();

        Paper.init(this);



       sellerBegin.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(MainActivity.this, SellerRegistrationActivity.class);
               startActivity(intent);
           }
       });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });


        joinNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        String UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);
        
        if (UserPhoneKey != ""  && UserPasswordKey != "" ){

            if (!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey)){

                AllowAccess(UserPhoneKey,UserPasswordKey);

                loadingBar.setTitle("Already logged in");
                loadingBar.setMessage("Please wait.....");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        }

    }

    private void AllowAccess(final String phone, final String password) {

        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("Users").child(phone).exists()) {

                    Users usersData = dataSnapshot.child("Users").child(phone).getValue(Users.class);

                    if (usersData.getPhone().equals(phone)) {

                        if (usersData.getPassword().equals(password)) {

                            Toast.makeText(getApplicationContext(), "Please Wait, You are already logged in", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Prevalent.currentOnlineUser = usersData;
                            Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(i);

                        } else {
                            loadingBar.dismiss();
                            Toast.makeText(getApplicationContext(), "Incorrect Password", Toast.LENGTH_SHORT).show();
                        }
                    }


                } else {
                    Toast.makeText(getApplicationContext(), "Account with this " + phone + " do not exist.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null){
            Intent intent = new Intent(MainActivity.this, SellerHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        loadingBar.dismiss();
        networkCheck.dismiss();
    }

    @Override
    protected void onPause() {
        super.onPause();
        loadingBar.dismiss();
        networkCheck.dismiss();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("TAG", "onResume: ");
        checkConnectivity();
    }

    //Checking for active internet connection.
    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }

    private void checkConnectivity(){

        networkCheck.setTitle("Checking Internet Connectivity");
        networkCheck.setMessage("Please wait.....");
        networkCheck.setCanceledOnTouchOutside(false);
        networkCheck.show();

        if (isConnected()){
                    networkCheck.dismiss();
        }

        else {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    networkCheck.dismiss();
                    startActivity(new Intent(MainActivity.this,NoInternetConnectionActivity.class));
                }
            }, 3000);

        }
    }
}
