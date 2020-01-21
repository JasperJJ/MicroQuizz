package com.jasper.microquizz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class gegevens extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    private Button bUitloggen;
    private Button bVerwijder;
    private TextView showemail;
    private Button bEmail;
    private EditText editEmail;
    private ProgressDialog progressDialog;
    private DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gegevens);
        firebaseAuth = FirebaseAuth.getInstance();

        initControl();
        setBackGroundColors();
        configureNavigationDrawer();
        configureToolbar();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        String gebruiker = firebaseUser.getEmail().toString();
        showemail.setText(gebruiker);

        bUitloggen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logout();
            }
        });
        bVerwijder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUp();
            }
        });

        boolean email = false;
        bEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                veranderEmail();
            }
        });





    }

    public void initControl() {
        bUitloggen= findViewById(R.id.bUitloggen);
        bVerwijder= findViewById(R.id.bVerwijder);
        showemail = findViewById(R.id.showemail);
        bEmail = findViewById(R.id.bEmail);
        editEmail = findViewById(R.id.editEmail);
    }

    private void Logout(){
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(gegevens.this, Beginscherm.class));

    }

    private void VerwijderAccount(){

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("server/saving-data/fireblog");
        DatabaseReference postsRef = ref.child("posts");
        DatabaseReference newPostRef = postsRef.push();
        firebaseAuth = FirebaseAuth.getInstance();


        firebaseUser.delete();

        finish();
        startActivity(new Intent(gegevens.this, Beginscherm.class));

    }

    private void popUp(){
        AlertDialog.Builder builder = new AlertDialog.Builder(gegevens.this);

        builder.setCancelable(true);
        builder.setTitle("Weet je zeker dat je jouw account wilt verwijderen?");

        builder.setNegativeButton("nee", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               dialogInterface.cancel();
            }
        });

        builder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                VerwijderAccount();
            }
        });
        builder.show();
    }

    private void veranderEmail(){
        showemail.setVisibility(View.INVISIBLE);
        editEmail.setVisibility(View.VISIBLE);
        bEmail.setText("verstuur");
        progressDialog = new ProgressDialog(this);
        bEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();


                //AuthCredential credential = EmailAuthProvider
                       // .getCredential(firebaseUser.getEmail().toString(), "password1234");
                //firebaseUser.reauthenticate(credential);


                String nieuwEmail = editEmail.getText().toString().trim();



                editEmail.setVisibility(View.INVISIBLE);
                showemail.setVisibility(View.VISIBLE);
                bEmail.setText("pas aan");

                if (editEmail.getText().toString().isEmpty()) {
                    Toast.makeText(gegevens.this, "Voer astublieft uw emailadres in", Toast.LENGTH_SHORT).show();
                }
                else {


                    progressDialog.setMessage("Laden");
                    //laat het laden zien
                    progressDialog.show();


                    firebaseUser.updateEmail(nieuwEmail).addOnCompleteListener(new OnCompleteListener<Void>(){
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {

                                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                String gebruiker = firebaseUser.getEmail().toString();
                                showemail.setText(gebruiker);

                                editEmail.setVisibility(View.INVISIBLE);
                                showemail.setVisibility(View.VISIBLE);
                                bEmail.setText("pas aan");
                                Toast.makeText(gegevens.this, "Emailadres wijzigen is gelukt", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(gegevens.this, "Emailadres wijzigen is niet gelukt", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });





                 }
            }
        });
    }


    public void setBackGroundColors() {
        GradientDrawable bVerwijder_bg = (GradientDrawable) bVerwijder.getBackground();
        GradientDrawable bUitloggen_bg = (GradientDrawable) bUitloggen.getBackground();

        bVerwijder_bg.setColor(getResources().getColor(R.color.colorGreen));
        bUitloggen_bg.setColor(getResources().getColor(R.color.colorBlue));

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

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(android.R.color.black));
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void configureNavigationDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navView = findViewById(R.id.navigation);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.action_home) {
                    Intent intent = new Intent(gegevens.this, HomeActivity.class);
                    startActivity(intent);
                    return true;
                }
                else if (itemId == R.id.uitloggen) {
                    Intent intent = new Intent(gegevens.this, Beginscherm.class);
                    startActivity(intent);
                    return true;
                }
                else if (itemId == R.id.action_musea) {
                    Intent intent = new Intent(gegevens.this, LocatiesActivity.class);
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

    //@Override
    public void onItemClick(TextView textView, int position) {
        Intent intent = new Intent(this, LocatiesActivity.class);
        this.startActivity(intent);
    }

}
