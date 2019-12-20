package com.jasper.microquizz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jasper.microquizz.models.Musea;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PlayActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private DrawerLayout drawerLayout;

    private Musea musea;

    private TextView tv_vraag;
    private RadioButton rb_antwoord1;
    private RadioButton rb_antwoord2;
    private RadioButton rb_antwoord3;

    private Object[] questionID;
    private int questionNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        configureNavigationDrawer();
        configureToolbar();

        initControl();
        getQuestions();
        setQuestions();
    }

    public void initControl() {
        tv_vraag = findViewById(R.id.tv_vraag);
        rb_antwoord1 = findViewById(R.id.rb_antwoord1);
        rb_antwoord2= findViewById(R.id.rb_antwoord2);
        rb_antwoord3 = findViewById(R.id.rb_antwoord3);

//        btn_location.setOnClickListener(this);
//        btn_play.setOnClickListener(this);
    }

    private void getQuestions() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("musea");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Map<String, Object> values = (Map<String, Object>) dataSnapshot.getValue();
                musea = new Musea(values);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void setQuestions() {

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
                    Intent intent = new Intent(PlayActivity.this, HomeActivity.class);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.action_musea) {
                    Intent intent = new Intent(PlayActivity.this, LocatiesActivity.class);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.uitloggen) {
                    Intent intent = new Intent(PlayActivity.this, Beginscherm.class);
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

}
