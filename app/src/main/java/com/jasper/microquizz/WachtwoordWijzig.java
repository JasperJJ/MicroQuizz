package com.jasper.microquizz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.net.wifi.hotspot2.pps.Credential;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.EmailAuthProvider;

public class WachtwoordWijzig extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private EditText etHuidig;
    private EditText etNieuw;
    private EditText etHerhaal;
    private Button bVerander;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wachtwoord_wijzig);
        firebaseAuth = FirebaseAuth.getInstance();

        initControl();

        bVerander.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String huidig = etHuidig.getText().toString();
                final String nieuw = etNieuw.getText().toString();
                String herhaal = etNieuw.getText().toString();

                if (huidig.isEmpty() || nieuw.isEmpty() || herhaal.isEmpty()){
                    Toast.makeText(WachtwoordWijzig.this, "Voer alstublieft alles in", Toast.LENGTH_SHORT).show();
                }else{

                    if (nieuw.equals(herhaal)) {

                        final FirebaseUser user = firebaseAuth.getCurrentUser();
                        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail().toString(),huidig);
                        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    user.updatePassword(nieuw).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            //progressDialog.dismiss();
                                            if (task.isSuccessful()) {
                                                Toast.makeText(WachtwoordWijzig.this, "Wachtwoord is gewijzigd", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                            else {
                                                Toast.makeText(WachtwoordWijzig.this, "Wachtwoord is niet gewijzigd, probeer opnieuw", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });
                                }
                                else {
                                    Toast.makeText(WachtwoordWijzig.this, "Incorrect huidig wachtwoord", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });



                    }
                    else{
                        Toast.makeText(WachtwoordWijzig.this, "Wachtwoorden komen niet overeen", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    public void initControl(){
        etHuidig=findViewById(R.id.wwHuidig);
        etNieuw=findViewById(R.id.wwNieuw);
        etHerhaal=findViewById(R.id.wwHerhaal);
        bVerander=findViewById(R.id.bWachtwoord);
    }
}
