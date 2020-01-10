package com.jasper.microquizz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.app.ProgressDialog;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

public class FeedbackActivity extends AppCompatActivity {
    private EditText tekstvak;
    private EditText emailadres;
    private Button btn_verstuur;
    private TextView tv_terug;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        initControl();
        setBackGroundColors();

        btn_verstuur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Bedankt voor de feedback");
                progressDialog.show();


                progressDialog.dismiss();
                String tekst = tekstvak.getText().toString().trim();
                String email = emailadres.getText().toString().trim();
                if (tekst.isEmpty() || email.isEmpty()) {
                    Toast.makeText(FeedbackActivity.this, "Voer alstublieft alles in", Toast.LENGTH_SHORT).show();
                } else {
                    mDatabase.child("feedback").child("tekst").child("0").setValue(tekst);
                    mDatabase.child("feedback").child("email").child("0").setValue(email);

                    startActivity(new Intent(FeedbackActivity.this, HomeActivity.class));
                }
            }
        });


        tv_terug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FeedbackActivity.this, HomeActivity.class));
            }
        });
     }

    public void initControl() {
        btn_verstuur = (Button) findViewById(R.id.btn_verstuur);
        tv_terug = (TextView)findViewById(R.id.tvTerug);
        tekstvak = findViewById(R.id.ET_tekstvak);
        emailadres = findViewById(R.id.ET_email);
        //btn_verstuur = setOnClickListener(this);
    }

    public void setBackGroundColors() {
        //GradientDrawable btn_verstuur_bg = (GradientDrawable) btn_verstuur.getBackground();

        //btn_verstuur_bg.setColor(getResources().getColor(R.color.colorBlue));
    }



}

