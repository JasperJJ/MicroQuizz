package com.jasper.microquizz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
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

public class inlogscherm extends AppCompatActivity {

    private Button btn_inloggen;

    private EditText naam;
    private EditText wachtwoord;
    private Button login;
    private TextView inlogpoging;
    //teller voor inlogpogingen
    private int loginTeller = 3;
    private TextView userRegistration;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    // laat een bericht zien tijdens het laden bij het inloggen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inlogscherm);


        //definieer de buttons in inlogscherm xml bij id.
        naam = (EditText)findViewById(R.id.et_naam);
        wachtwoord = (EditText)findViewById(R.id.et_wachtwoord);
        login = (Button)findViewById(R.id.btn_inloggen);
        inlogpoging = (TextView)findViewById(R.id.tv_inlogpoging);
        //userRegistration = (TextView)findViewById(R.id.btn_register);

       // inlogpoging.setText("Aantal pogingen over: 5");

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
    }

   // public void findByID() {
    //    btn_inloggen = findViewById(R.id.btn_inloggen);
   // }

   // public void setBackGroundColors() {
     //   GradientDrawable btn_inloggen_bg = (GradientDrawable) btn_inloggen.getBackground();

     //  btn_inloggen_bg.setColor(getResources().getColor(R.color.colorBlue));
    //}

    //functie voor het inloggen te verifieren

    private void inlogBevestig(String gebruikersnaam, String gebruikerswachtwoord) {

        progressDialog.setMessage("Laden");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(gebruikersnaam, gebruikerswachtwoord).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        //nietmeer nodig omdat je niet wilt inloggen gelukt en dan probeer opnieuw
                       // Toast.makeText(inlogscherm.this, "Inloggen gelukt", Toast.LENGTH_LONG).show();
                        // email verificatie zodat je niet direct wordt doorgestuurd maar eerst moet verificeren of je email al bestaat
                        checkEmailVerificatie();
                   // startActivity(new Intent(inlogscherm.this, HomeActivity.class));
                }
                else{
                    Toast.makeText(inlogscherm.this, "Inloggen mislukt", Toast.LENGTH_LONG).show();
                    loginTeller --;
                    inlogpoging.setText("Aantal pogingen over: " + String.valueOf(loginTeller));
                    if (loginTeller == 0){
                        login.setEnabled(false);
                    }
                }
            }
        });

        // hieronder mocht gedelete worden,
        // ik heb het maar in comments gezet voor toekomstige referentie
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

    private void checkEmailVerificatie() {

        FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        Boolean emailvlag = firebaseUser.isEmailVerified();

        if (emailvlag) {
            finish();
            startActivity(new Intent(inlogscherm.this, HomeActivity.class));
        }else {
            // als gebruiker email niet heeft bevestigd dan krijgt de gebruiker een bericht
            Toast.makeText(this, "Bevestig uw Email-adress", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }


}
