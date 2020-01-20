package com.jasper.microquizz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class HighscoreActivity extends AppCompatActivity {
    private TextView highscore;
    private TextView tv_terug;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        final String gebruiker = user.getEmail();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        initControl();
        setBackGroundColors();

        getHighscore();

//        btn_verstuur.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
//                final FirebaseDatabase database = FirebaseDatabase.getInstance();
//                DatabaseReference ref = database.getReference("server/saving-data/fireblog");
//                DatabaseReference postsRef = ref.child("posts");
//                DatabaseReference newPostRef = postsRef.push();
//
//                // de data wordt uit de vakjes gehaald en in een string gezet
//
//                    progressDialog.setMessage("laden");
//                    progressDialog.show();
//                    // Generate a reference to a new location and add some data using push()
//                    DatabaseReference pushedPostRef = postsRef.push();
//
//                    // de data wordt in de database gezet
//                    mDatabase.child("highscore").child("gebruiker").setValue(highscore);
//
//
//
//                    startActivity(new Intent(HighscoreActivity.this, HomeActivity.class));
//                    progressDialog.dismiss();
//                    Toast.makeText(HighscoreActivity.this, "Bedankt voor de feedback", Toast.LENGTH_LONG).show();
//
//            }
//        });


        tv_terug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HighscoreActivity.this, HomeActivity.class));
            }
        });
     }

    public void initControl() {
        tv_terug = (TextView)findViewById(R.id.tvTerug);
        highscore = findViewById(R.id.EThighscore);
        //btn_verstuur = setOnClickListener(this);
    }

    public void setBackGroundColors() {
        //GradientDrawable btn_verstuur_bg = (GradientDrawable) btn_verstuur.getBackground();

        //btn_verstuur_bg.setColor(getResources().getColor(R.color.colorBlue));
    }

    public void getHighscore() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("highscore");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                HashMap<String, String> post = (HashMap) dataSnapshot.getValue();
                highscore.setText(post.get("gebruiker"));
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }




}

