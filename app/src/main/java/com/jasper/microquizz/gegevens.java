package com.jasper.microquizz;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gegevens);
        firebaseAuth = FirebaseAuth.getInstance();

        initControl();
        setBackGroundColors();
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

        bEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                //AuthCredential credential = EmailAuthProvider
                       // .getCredential(firebaseUser.getEmail().toString(), "password1234");
                //firebaseUser.reauthenticate(credential);

                String nieuwEmail = editEmail.getText().toString();
                firebaseUser.updateEmail(nieuwEmail);

                


                String gebruiker = firebaseUser.getEmail().toString();
                showemail.setText(gebruiker);

                editEmail.setVisibility(View.INVISIBLE);
                showemail.setVisibility(View.VISIBLE);
                bEmail.setText("pas aan");
            }
        });
    }


    public void setBackGroundColors() {
        GradientDrawable bVerwijder_bg = (GradientDrawable) bVerwijder.getBackground();
        GradientDrawable bUitloggen_bg = (GradientDrawable) bUitloggen.getBackground();

        bVerwijder_bg.setColor(getResources().getColor(R.color.colorGreen));
        bUitloggen_bg.setColor(getResources().getColor(R.color.colorBlue));

    }

}
