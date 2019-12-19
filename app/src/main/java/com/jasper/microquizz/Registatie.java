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
//import com.google.firebase.auth.FirebaseUser;



public class Registatie extends AppCompatActivity {
    // buttons e.d. voor registratie aanmaken

    private EditText userName, userPassword, userEmail;
    private Button regButton;
    private TextView userLogin;
    private FirebaseAuth firebaseAuth;

    private Button btn_register;

    private void setupUIViews() {
        userPassword = (EditText) findViewById(R.id.et_wachtwoord);
        userEmail = (EditText) findViewById(R.id.et_email);
        regButton = (Button) findViewById(R.id.btn_register);
        userName = findViewById(R.id.et_naam);
    }

    private boolean validate(){
        Boolean result = false;

        String name = userName.getText().toString();
        String password = userPassword.getText().toString();
        String email = userEmail.getText().toString();

        if(name.isEmpty() || password.isEmpty() || email.isEmpty()) {
            Toast.makeText(Registatie.this, "Voer alles in ", Toast.LENGTH_SHORT).show();
        } else {
            result = true;
        }
        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registatie);
        firebaseAuth = FirebaseAuth.getInstance();

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    //upload naar de database;
                    String user_email= userEmail.getText().toString().trim();
                    String user_password = userPassword.getText().toString().trim();
                    firebaseAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()) {
                                Toast.makeText(Registatie.this , "Registreren succes",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Registatie.this, Beginscherm.class));
                            } else {
                                Toast.makeText(Registatie.this , "Registreren niet succesvol",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });

        findByID();
        setBackGroundColors();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), HomeActivity.class);
                startActivity(intent);
            }
        });
    }
    public void findByID() {
        btn_register = findViewById(R.id.btn_register);
    }

    public void setBackGroundColors() {
        GradientDrawable btn_register_bg = (GradientDrawable) btn_register.getBackground();

        btn_register_bg.setColor(getResources().getColor(R.color.colorBlue));
    }
}
