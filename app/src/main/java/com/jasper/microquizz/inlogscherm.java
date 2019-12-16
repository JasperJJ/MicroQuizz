package com.jasper.microquizz;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class inlogscherm extends AppCompatActivity {

    private Button btn_inloggen;

    private EditText naam;
    private EditText wachtwoord;
    private Button login;
    private TextView inlogpoging;
    //teller voor inlogpogingen
    private int loginTeller = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inlogscherm);


        //definieer de buttons in inlogscherm xml bij id.
        naam = (EditText)findViewById(R.id.et_naam);
        wachtwoord = (EditText)findViewById(R.id.et_wachtwoord);
        login = (Button)findViewById(R.id.btn_inloggen);
        inlogpoging = (TextView)findViewById(R.id.tv_inlogpoging);

        inlogpoging.setText("Aantal pogingen over: 5");

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
        //check of gebruikersnaam en wachtwoord kloppen
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

    }


}
