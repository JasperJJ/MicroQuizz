package com.jasper.microquizz;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

// importeer firebase extensie
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private Button logout;
    private DrawerLayout drawerLayout;
    private TextView tv_description;
    private Button btn_play;
    private Button btn_highscore;
    private Button btn_location;
    private Button btn_plus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // haal huidige sessie op
        firebaseAuth = FirebaseAuth.getInstance();


// test code om via highscore uit te loggen dit kan dus voor andere doeleinde worden gebruikt.
        //logout = (Button)findViewById(R.id.btn_highscore);
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Logout();
//            }
//        });



        initControl();
        setBackGroundColors();
        configureNavigationDrawer();
        configureToolbar();
    }

    //logout functie om uit te roepen en te verwijzen naar het inlogscherm
    private void Logout(){
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(HomeActivity.this, Beginscherm.class));

    }


    // dubbele code voor het menu, dit was niet nodig en veroorzaakte problemen met het inloggen.
    //@Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu, menu);
//        return true;
//    }

//    @Override
//    public  boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.uitloggen: {
//                Logout();
//            }
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


    // zet de toolbaar in de applicatie waar de sidebar aan gekoppeld is.
    private void configureToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setHomeAsUpIndicator(R.drawable.ic_hamburger_menu);
            actionbar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(android.R.color.black));
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }


    // dit is voor de sidebar als er op een menu item wordt gedrukt.
    private void configureNavigationDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navView = findViewById(R.id.navigation);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.action_home) {
                    // verwijs naar home als er op action_home wordt gedrukt.
                    Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.action_musea) {
                    Intent intent = new Intent(HomeActivity.this, LocatiesActivity.class);
                    // verwijs naar een andere activiteit locaties.
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.uitloggen) {
                    // als er op logout wordt gedrukt dan roepen we de uitlog functie op.
                    Logout();
                   // Intent intent = new Intent(HomeActivity.this, Beginscherm.class);
                   // startActivity(intent);
                    return true;


                }
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return true;
    }

    // een fucntie om variabelen te koppelen aan de buttons, textvieuws etc.
    public void initControl() {
        tv_description = findViewById(R.id.tv_description);
        btn_play = findViewById(R.id.btn_play);
        btn_highscore = findViewById(R.id.btn_highscore);
        btn_location = findViewById(R.id.btn_location);
        btn_plus = findViewById(R.id.btn_plus);

        btn_location.setOnClickListener(this);
        btn_play.setOnClickListener(this);
    }


// bepaal kleuren voor id's etc.
    public void setBackGroundColors() {
        GradientDrawable tv_description_bg = (GradientDrawable) tv_description.getBackground();
        GradientDrawable bt_play_bg = (GradientDrawable) btn_play.getBackground();
        GradientDrawable bt_highscore_bg = (GradientDrawable) btn_highscore.getBackground();
        GradientDrawable bt_location_bg = (GradientDrawable) btn_location.getBackground();
        GradientDrawable bt_plus_bg = (GradientDrawable) btn_plus.getBackground();

        tv_description_bg.setColor(getResources().getColor(R.color.colorBlue));
        bt_play_bg.setColor(getResources().getColor(R.color.colorBlue));
        bt_highscore_bg.setColor(getResources().getColor(R.color.colorBlue));
        bt_location_bg.setColor(getResources().getColor(R.color.colorGreen));
        bt_plus_bg.setColor(getResources().getColor(R.color.colorGreen));
    }

        // verwijzen naar andere schermen wanneer er op x wordt gedrukt.
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_play) {
            Intent intent = new Intent(this, PlayActivity.class);
            this.startActivity(intent);
        } else if (v.getId() == R.id.btn_location) {
            Intent intent = new Intent(this, museumKiezen.class);
            this.startActivity(intent);
        }
    }
}
