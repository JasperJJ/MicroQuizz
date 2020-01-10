package com.jasper.microquizz;

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

import org.w3c.dom.Text;

public class Registatie extends AppCompatActivity {

    private Button btn_register;

    private EditText userName, userPassword, userEmail;
    private Button regButton;
    private TextView userLogin;


    // maak verbinding met de database
    private FirebaseAuth firebaseAuth;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registatie);

        // variabelen ophalen en de referentie naar de velden maken
        setupUIViews();

        // Haal de huidige sessie op.
        firebaseAuth = FirebaseAuth.getInstance();

        // Als er op registreren wordt gedrukt doe :
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // validate geeft de output true of false
                if (validate()) {

                    // als true
                    //upload data naar de database
                    String user_email = userEmail.getText().toString().trim();
                    String user_password = userPassword.getText().toString().trim();

                    // firebase functie die de email en het wachtwoord gebruikt om een account aan te maken.
                    firebaseAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            //als registratie succesvol is geef melding registreren succevol en stuur door naar inlogscherm
                            if (task.isSuccessful()) {
                                Toast.makeText(Registatie.this, "Registreren succesvol.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Registatie.this, inlogscherm.class));
                            } else {
                                // als niet succesvol dan is het registreren mislukt.
                                Toast.makeText(Registatie.this, "Registreren mislukt.", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }
            }
        });


        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registatie.this,inlogscherm.class ));
            }
        });


        findByID();
        setBackGroundColors();

        // deze functie is dubbel en gaf een error dus er uitgehaald. Deze stuurde gelijk door na het inlogscherm wanneer er geregisteerd werd.
//        btn_register.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getBaseContext(), HomeActivity.class);
//                startActivity(intent);
//            }
//        });

    }

    // defineer alle velden en buttons
        private void setupUIViews() {
            userName = (EditText)findViewById(R.id.etUserName);
            userPassword = (EditText)findViewById(R.id.et_wachtwoord);
            userEmail = (EditText)findViewById(R.id.etUserEmail);
            regButton = (Button) findViewById(R.id.btnRegister);
            userLogin = (TextView)findViewById(R.id.tvUserLogin);
        }


        // valideer functie voor het registeren
        private Boolean validate() {

                boolean result = false;

                String name = userName.getText().toString();
                String password = userPassword.getText().toString();
                String email = userEmail.getText().toString();

                // als de naam niet is ingevuld, of het wachtwoord of de email is leeg geef de volgende melding:

                if (name.isEmpty() || password.isEmpty() || email.isEmpty()) {

                    Toast.makeText(Registatie.this, "Voer alstublieft alles in",Toast.LENGTH_SHORT).show();

                } else {
                        // als alles is ingevuld dan is het result true en kan de validate functie worden aangeroepen
                        result = true;

                }

                return result;
        }


    // btnRegister knop toewijzen aan variabele
    public void findByID() {
        btn_register = findViewById(R.id.btnRegister);
    }

    //Zet de kleur voor de registratieknop naar blauw
    public void setBackGroundColors() {
        GradientDrawable btn_register_bg = (GradientDrawable) btn_register.getBackground();

        btn_register_bg.setColor(getResources().getColor(R.color.colorBlue));
    }
}
