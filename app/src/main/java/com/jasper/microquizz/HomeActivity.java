package com.jasper.microquizz;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

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

        initControl();
        setBackGroundColors();
        configureNavigationDrawer();
        configureToolbar();
    }

    private void configureToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setHomeAsUpIndicator(R.drawable.ic_hamburger_menu);
            actionbar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void configureNavigationDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navView = findViewById(R.id.navigation);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.action_home) {
                    Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.action_musea) {
                    Intent intent = new Intent(HomeActivity.this, LocatiesActivity.class);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.action_logout) {
                    Intent intent = new Intent(HomeActivity.this, Beginscherm.class);
                    startActivity(intent);
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

    public void initControl() {
        tv_description = findViewById(R.id.tv_description);
        btn_play = findViewById(R.id.btn_play);
        btn_highscore = findViewById(R.id.btn_highscore);
        btn_location = findViewById(R.id.btn_location);
        btn_plus = findViewById(R.id.btn_plus);

        btn_location.setOnClickListener(this);
        btn_play.setOnClickListener(this);
    }

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
