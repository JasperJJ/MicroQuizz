package com.jasper.microquizz;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Forgot extends AppCompatActivity {

    private EditText etForgotEmail;

    private Button btForgot;

    private FirebaseAuth auth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forgot);

        etForgotEmail = (EditText) findViewById(R.id.etForgotEmail);

        btForgot = (Button) findViewById(R.id.btForgot);

        setBackGroundColors();

        auth = FirebaseAuth.getInstance();



        btForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = etForgotEmail.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplication(), "Enter your registered email id", Toast.LENGTH_SHORT).show();
                    return;
                }



                auth.sendPasswordResetEmail(email)

                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Forgot.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Forgot.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                                }


                            }
                        });
            }
        });
    }

    public void setBackGroundColors() {
        btForgot.setTextColor(Color.WHITE);
        GradientDrawable btForgot_bg = (GradientDrawable) btForgot.getBackground();
        btForgot_bg.setColor(getResources().getColor(R.color.colorBlue));
    }
}
