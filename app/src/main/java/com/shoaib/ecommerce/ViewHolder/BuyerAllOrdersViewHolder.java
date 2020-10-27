package com.shoaib.ecommerce.ViewHolder;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shoaib.ecommerce.R;

public class BuyerAllOrdersViewHolder extends RecyclerView.ViewHolder {

    public TextView txtProductName,txtProductPrice,txtProductQuantity,txtProductSellerName,txtProductDate,txtProductTime,txtProductStatus;
    public TextView txtProductBuyerName,txtProductAddress,txtProductCity,txtProductPincode;
    public ImageView imgProductImage;

    public BuyerAllOrdersViewHolder(@NonNull View itemView) {
        super(itemView);

        txtProductName = itemView.findViewById(R.id.my_orders_product_name);
        txtProductPrice = itemView.findViewById(R.id.my_orders_product_price);
        txtProductQuantity = itemView.findViewById(R.id.my_orders_product_quantity);
        txtProductSellerName = itemView.findViewById(R.id.my_orders_product_seller_name);
        imgProductImage = itemView.findViewById(R.id.my_orders_product_image);
        txtProductDate = itemView.findViewById(R.id.my_orders_product_date);

        txtProductTime = itemView.findViewById(R.id.my_orders_product_time);
        txtProductStatus = itemView.findViewById(R.id.my_orders_product_status);
        txtProductBuyerName = itemView.findViewById(R.id.my_orders_product_delivery_user_name);
        txtProductAddress = itemView.findViewById(R.id.my_orders_product_delivery_address);
        txtProductCity = itemView.findViewById(R.id.my_orders_product_city);
        txtProductPincode = itemView.findViewById(R.id.my_orders_product_pin_code);

    }
}
