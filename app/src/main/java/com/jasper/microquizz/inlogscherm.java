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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class inlogscherm extends AppCompatActivity {

    private Button btn_inloggen;
    private EditText naam;
    private EditText wachtwoord;
    private Button login;
    private TextView inlogpoging;
    private TextView userRegistration;

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
        naam = (EditText)findViewById(R.id.etUserEmail);
        wachtwoord = (EditText)findViewById(R.id.et_wachtwoord);
        login = (Button)findViewById(R.id.btn_inloggen);
        inlogpoging = (TextView)findViewById(R.id.tv_inlogpoging);
        userRegistration = (TextView)findViewById(R.id.tvRegister);

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
                inlogBevestig(naam.getText().toString(),wachtwoord.getText().toString());
            }
        });

        // dit is de knop voor het registreren als je niet bent ingelogd.
        userRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(inlogscherm.this, Registatie.class));
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
                    startActivity(new Intent(inlogscherm.this, HomeActivity.class));
                }
                else{

                    // als het inloggen niet in gelukt dan geven we een melding inloggen mislukt, wordt er 1 afgetrokken van de inlog teller.
                    Toast.makeText(inlogscherm.this, "Inloggen mislukt", Toast.LENGTH_LONG).show();
                    loginTeller --;
                    inlogpoging.setText("Aantal pogingen over: " + String.valueOf(loginTeller));
                    if (loginTeller == 0){
                        // als de inlogteller 0 is kan er niet meer op inloggen worden gedrukt.
                        login.setEnabled(false);
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
