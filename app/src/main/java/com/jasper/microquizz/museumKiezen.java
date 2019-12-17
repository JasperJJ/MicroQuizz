package com.jasper.microquizz;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.jasper.microquizz.adapters.museumAdapter;

import java.util.ArrayList;

public class museumKiezen extends AppCompatActivity implements museumAdapter.ItemClickListener {

    private DrawerLayout drawerLayout;

    private RecyclerView recyclerView;
    private museumAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_museum_kiezen);

        configureNavigationDrawer();
        configureToolbar();

        recyclerView = findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<String> list = new ArrayList<>();
        list.add("Een korte beschrijving van het museum");
        list.add("Een korte beschrijving van het museum");
        list.add("Een korte beschrijving van het museum");
        list.add("Een korte beschrijving van het museum");
        list.add("Een korte beschrijving van het museum");

        // specify an adapter (see also next example)
        mAdapter = new museumAdapter(this, list);
        mAdapter.setClickListener(this);
        recyclerView.setAdapter(mAdapter);
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
                    Intent intent = new Intent(museumKiezen.this, HomeActivity.class);
                    startActivity(intent);
                    return true;
                }
                else if (itemId == R.id.action_logout) {
                    Intent intent = new Intent(museumKiezen.this, Beginscherm.class);
                    startActivity(intent);
                    return true;
                }
                else if (itemId == R.id.action_musea) {
                    Intent intent = new Intent(museumKiezen.this, LocatiesActivity.class);
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

    @Override
    public void onItemClick(TextView textView, int position) {
        Intent intent = new Intent(this, LocatiesActivity.class);
        this.startActivity(intent);
    }

}
