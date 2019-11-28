package com.jasper.microquizz;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
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
    }
    public void findByID() {
        btn_register = findViewById(R.id.btn_register);
    }

    public void setBackGroundColors() {
        GradientDrawable btn_register_bg = (GradientDrawable) btn_register.getBackground();

        btn_register_bg.setColor(getResources().getColor(R.color.colorBlue));
    }
}
