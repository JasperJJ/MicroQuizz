package com.jasper.microquizz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Beginscherm extends AppCompatActivity {

    private Button btn_register;
    private Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beginscherm);

        initControl();
        setBackGroundColors();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), Registatie.class);
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), inlogscherm.class);
                startActivity(intent);
            }
        });
    }
    public void initControl() {
        btn_register = findViewById(R.id.button2);
        btn_login = findViewById(R.id.button);
    }

    public void setBackGroundColors() {
        GradientDrawable btn_register_bg = (GradientDrawable) btn_register.getBackground();
        GradientDrawable btn_login_bg = (GradientDrawable) btn_login.getBackground();

        btn_register_bg.setColor(getResources().getColor(R.color.colorBlue));
        btn_login_bg.setColor(getResources().getColor(R.color.colorGreen));
    }
}
