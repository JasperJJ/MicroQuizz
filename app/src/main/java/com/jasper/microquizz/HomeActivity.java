package com.jasper.microquizz;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    private TextView tv_description;
    private Button bt_play;
    private Button bt_highscore;
    private Button bt_location;
    private Button bt_plus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        findByID();
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
        }
    }

    private void configureNavigationDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navView = findViewById(R.id.navigation);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
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

    public void findByID() {
        tv_description = findViewById(R.id.tv_description);
        bt_play = findViewById(R.id.bt_play);
        bt_highscore = findViewById(R.id.bt_highscore);
        bt_location = findViewById(R.id.bt_location);
        bt_plus = findViewById(R.id.bt_plus);
    }

    public void setBackGroundColors() {
        GradientDrawable tv_description_bg = (GradientDrawable) tv_description.getBackground();
        GradientDrawable bt_play_bg = (GradientDrawable) bt_play.getBackground();
        GradientDrawable bt_highscore_bg = (GradientDrawable) bt_highscore.getBackground();
        GradientDrawable bt_location_bg = (GradientDrawable) bt_location.getBackground();
        GradientDrawable bt_plus_bg = (GradientDrawable) bt_plus.getBackground();

        tv_description_bg.setColor(getResources().getColor(R.color.colorBlue));
        bt_play_bg.setColor(getResources().getColor(R.color.colorBlue));
        bt_highscore_bg.setColor(getResources().getColor(R.color.colorBlue));
        bt_location_bg.setColor(getResources().getColor(R.color.colorGreen));
        bt_plus_bg.setColor(getResources().getColor(R.color.colorGreen));
    }
}
