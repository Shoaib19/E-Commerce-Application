package com.shoaib.ecommerce.Buyers;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Html;
import android.util.Log;

import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shoaib.ecommerce.Prevalent.Prevalent;
import com.shoaib.ecommerce.R;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String LIST_STATE_KEY = "1";
    private DatabaseReference ProductRef;
    private static final String STATE_LIST = "State Adapter Data";
    private String type = "";
    private String rupeeSymbol = null;
    private ImageView searchProductIcon,cartImageIcon,menImg,womenImg,boyImg
            ,girlImg,kurtaImg,topImg,jeansImg,sneakersImg,dressesImg,tshirtImg,shirtsImg,kidShoeImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        rupeeSymbol = String.valueOf(Html.fromHtml("\u20B9"));


        //Methods for initialising all the varialbles
        init();


        searchProductIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,SearchProductActivity.class));
            }
        });

        cartImageIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,CartActivity.class));
            }
        });

        menImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this,ProductListActivity.class);
                i.putExtra("keyOne","Men");
                startActivity(i);
            }
        });

        womenImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this,ProductListActivity.class);
                i.putExtra("keyOne","Women");
                startActivity(i);
            }
        });

        boyImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this,ProductListActivity.class);
                i.putExtra("keyOne","Boy");
                startActivity(i);
            }
        });

        girlImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this,ProductListActivity.class);
                i.putExtra("keyOne","Girl");
                startActivity(i);
            }
        });


        kurtaImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this,ProductListActivity.class);
                i.putExtra("keyTwo","Women_kurtas");
                startActivity(i);
            }
        });

        topImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this,ProductListActivity.class);
                i.putExtra("keyTwo","Women_Tops");
                startActivity(i);
            }
        });

        jeansImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this,ProductListActivity.class);
                i.putExtra("keyTwo","Men_Jeans");
                startActivity(i);
            }
        });

        sneakersImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this,ProductListActivity.class);
                i.putExtra("keyTwo","Men_Sneakers");
                startActivity(i);
            }
        });

        dressesImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this,ProductListActivity.class);
                i.putExtra("keyTwo","Women_Dresses");
                startActivity(i);
            }
        });

        tshirtImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this,ProductListActivity.class);
                i.putExtra("keyTwo","Men_TShirts");
                startActivity(i);
            }
        });
        shirtsImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this,ProductListActivity.class);
                i.putExtra("keyTwo","Men_Shirts");
                startActivity(i);
            }
        });
        kidShoeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this,ProductListActivity.class);
                i.putExtra("keyTwo","Boy_Footwear");
                startActivity(i);
            }
        });



       // Checking if the intent
        //
        Intent intent = getIntent();
        String s1 = intent.getStringExtra("Admin");
        if (s1 != null) {
            if (s1.equals("Admin"))
                type = "Admin";
        }


        ProductRef = FirebaseDatabase.getInstance().getReference().child("Products");
        Paper.init(this);


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);
        

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.user_profile_name);
        CircleImageView profileImageView = headerView.findViewById(R.id.user_profile_image);

        if (!type.equals("Admin")) {
            userNameTextView.setText(Prevalent.currentOnlineUser.getName());
            Log.d("TAG", "onCreate: " + Prevalent.currentOnlineUser.getName());
            Picasso.get().load(Prevalent.currentOnlineUser.getImage()).resize(800, 0)
                    .centerCrop().placeholder(R.drawable.profile).into(profileImageView);
        }


    }

    private void init() {

        searchProductIcon = findViewById(R.id.home_search_image);
        cartImageIcon = findViewById(R.id.home_cart_image);
        menImg = findViewById(R.id.home_menImage);
        womenImg = findViewById(R.id.home_womenImage);
        boyImg = findViewById(R.id.home_boyImage);
        girlImg = findViewById(R.id.home_girlImage);
        kurtaImg = findViewById(R.id.home_kurtaImage);
        topImg = findViewById(R.id.home_girlTopImage);
        jeansImg = findViewById(R.id.home_jeansImage);
        sneakersImg = findViewById(R.id.home_sneakersImage);
        dressesImg = findViewById(R.id.home_dressesImage);
        tshirtImg = findViewById(R.id.home_tShirtImage);
        shirtsImg = findViewById(R.id.home_shirtImage);
        kidShoeImg = findViewById(R.id.home_kidShoeImage);

    }

    @Override
    protected void onStart() {
        super.onStart();


    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.nav_cart:
                if (!type.equals("Admin")) {
                    Intent intentCart = new Intent(HomeActivity.this, CartActivity.class);
                    startActivity(intentCart);
                }
                break;
            case R.id.nav_search:
                if (!type.equals("Admin")) {
                    Intent intentSearch = new Intent(HomeActivity.this, SearchProductActivity.class);
                    startActivity(intentSearch);
                }
                break;

            case R.id.nav_settings:
                if (!type.equals("Admin")) {
                    Intent intentSetting = new Intent(HomeActivity.this, SettingsActivity.class);
                    startActivity(intentSetting);
                }
                break;

            case R.id.nav_my_orders:
                if (!type.equals("Admin")) {
                    Intent intentOrders = new Intent(HomeActivity.this,MyOrdersActivity.class);
                    startActivity(intentOrders);
                }

                break;

            case R.id.nav_logout:
                if (!type.equals("Admin")) {
                    Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
                    Paper.book().destroy();

                    Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                break;



            default:
                return true;

        }
        return true;
    }

}
