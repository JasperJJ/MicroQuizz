package com.jasper.microquizz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class inlogscherm extends AppCompatActivity {

    private Button btn_inloggen;
    private EditText mNaam;
    private EditText mWachtwoord;
    private Button login;
    private TextView userRegistration;
    private TextView userForgot;

    //teller voor inlogpogingen
    private int loginTeller = 3;

    //inloggen database
    private FirebaseAuth firebaseAuth;

    // laat een bericht zien tijdens het laden bij het inloggen
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inlogscherm);


        //definieer de buttons in inlogscherm xml bij id.
        mNaam = findViewById(R.id.etUserEmail);
        mWachtwoord = findViewById(R.id.et_wachtwoord);
        login = findViewById(R.id.btn_inloggen);
        userRegistration = findViewById(R.id.tvRegister);
        userForgot = findViewById(R.id.tvForgot);

        // inlogpoging.setText("Aantal pogingen over: 3");

        //haal de huidige sessie op verbind met de database.
        firebaseAuth = FirebaseAuth.getInstance();


        progressDialog = new ProgressDialog(this);
        FirebaseUser user = firebaseAuth.getCurrentUser();

        // als de gebruiker al is ingelogd gaat hij naar het startscherm.
        if (user != null) {
            finish();
            startActivity(new Intent(inlogscherm.this, HomeActivity.class));
        }

        //findByID();
        //setBackGroundColors();

        // als we op login klikken dan roepen we de functie aan inlogBevestig(gebruikersnaam, gebruikerswachtwoord);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String naam = mNaam.getText().toString().trim();
                String wachtwoord = mWachtwoord.getText().toString().trim();

                if (naam.isEmpty() || wachtwoord.isEmpty()) {
                    Toast.makeText(inlogscherm.this, "Voer alstublieft alles in", Toast.LENGTH_SHORT).show();
                } else {
                    inlogBevestig(naam, wachtwoord);
                }
            }
        });

        // dit is de knop voor het registreren als je niet bent ingelogd.
        userRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(inlogscherm.this, Registatie.class));
            }
        });

        userForgot.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(inlogscherm.this, Forgot.class));
            }
        });
    }

    // public void findByID() {
    //    btn_inloggen = findViewById(R.id.btn_inloggen);
    // }

    // public void setBackGroundColors() {
    //   GradientDrawable btn_inloggen_bg = (GradientDrawable) btn_inloggen.getBackground();

    //  btn_inloggen_bg.setColor(getResources().getColor(R.color.colorBlue));
    //}

    //functie voor het inloggen te verifieren

    private void inlogBevestig(String userEmail, String userPassword) {

        // als je gaat inloggen geef een bericht met laden
        progressDialog.setMessage("Laden");
        //laat het laden zien
        progressDialog.show();

        //login met email en wachtwoord
        firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                // als je bent ingelogd haal dan het laden weg
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    // wanneer succesvol ingelogd geef dan inloggen gelukt en verwijs naar de homeactivity
                    Toast.makeText(inlogscherm.this, "Inloggen gelukt", Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(new Intent(inlogscherm.this, HomeActivity.class));
                } else {

                    // als het inloggen niet in gelukt dan geven we een melding inloggen mislukt, wordt er 1 afgetrokken van de inlog teller.
                    Snackbar.make(mNaam, "Inloggen mislukt", Snackbar.LENGTH_LONG).show();
                    loginTeller--;
                    if (loginTeller == 0) {
                        // als de inlogteller 0 is kan er niet meer op inloggen worden gedrukt.
                        login.setEnabled(false);
                        Snackbar.make(mNaam, "U kunt niet meer inloggen", Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        });


        // ! Dit is oude code voor het statisch inloggen met een gebruikersnaam en wachtwoord zonder database.
       /* //check of gebruikersnaam en wachtwoord kloppen
        if ((gebruikersnaam.equals("Admin")) && (gebruikerswachtwoord.equals("1234"))) {
        //ga naar home scherm
        Intent intent = new Intent(inlogscherm.this, HomeActivity.class);
        startActivity(intent);
        }
        // als het wachtwoord niet klopt doe dan dit ( else )
        else {
            // hier tellen we de inlogpogingen
            loginTeller--;
            inlogpoging.setText("Aantal pogingen over: " + String.valueOf(loginTeller));
            if (loginTeller == 0) {
                login.setEnabled(false);

            }

        }
        */
    }


}
