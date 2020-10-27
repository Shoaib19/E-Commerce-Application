package com.shoaib.ecommerce.Sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.shoaib.ecommerce.R;

public class SellerLoginActivity extends AppCompatActivity {

    private Button loginSellerBtn;
    private EditText emailInput, passwordInput;
    private FirebaseAuth firebaseAuth;

    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login);


        loadingBar = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();

        emailInput = findViewById(R.id.seller_login_email);
        passwordInput = findViewById(R.id.seller_login_password);
        loginSellerBtn = findViewById(R.id.seller_login_btn);

        loginSellerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginSeller();
            }
        });

    }

    private void loginSeller() {

        final String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

        if ( !email.equals("") && !password.equals("")){

            loadingBar.setTitle("Seller Account Login");
            loadingBar.setMessage("Please wait, we are checking the credential.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            firebaseAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){

                                loadingBar.dismiss();
                                Toast.makeText(SellerLoginActivity.this, "You are registered successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SellerLoginActivity .this, SellerHomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    loadingBar.dismiss();
                    Toast.makeText(SellerLoginActivity.this, "Check email or password again", Toast.LENGTH_SHORT).show();
                }
            });

        }
        else {

            Toast.makeText(this, "Please write email and password", Toast.LENGTH_SHORT).show();

        }


    }
}
