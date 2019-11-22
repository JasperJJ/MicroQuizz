package com.jasper.microquizz;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class inlogscherm extends AppCompatActivity {

    private Button btn_inloggen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inlogscherm);

        findByID();
        setBackGroundColors();

        btn_inloggen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    public void findByID() {
        btn_inloggen = findViewById(R.id.btn_inloggen);
    }

    public void setBackGroundColors() {
        GradientDrawable btn_inloggen_bg = (GradientDrawable) btn_inloggen.getBackground();

        btn_inloggen_bg.setColor(getResources().getColor(R.color.colorBlue));
    }
}
