package com.shoaib.ecommerce.ViewHolder;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shoaib.ecommerce.Interface.ItemClickListener;
import com.shoaib.ecommerce.R;

import java.util.ArrayList;
import java.util.List;

public class CartViewHolder extends RecyclerView.ViewHolder{

    public TextView txtProductName, txtProductPrice,txtProductQuantity,textSellerName;
    public ImageView imgProductImage;

    public CartViewHolder(@NonNull final View itemView) {
        super(itemView);

        textSellerName = itemView.findViewById(R.id.cart_seller_name);
        txtProductName = itemView.findViewById(R.id.cart_product_name);
        txtProductPrice = itemView.findViewById(R.id.cart_product_price);
        txtProductQuantity = itemView.findViewById(R.id.cart_product_quantity);
        imgProductImage = itemView.findViewById(R.id.cart_product_image);

    }

}
