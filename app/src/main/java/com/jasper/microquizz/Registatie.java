package com.jasper.microquizz;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Registatie extends AppCompatActivity {

    private Button btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registatie);

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
