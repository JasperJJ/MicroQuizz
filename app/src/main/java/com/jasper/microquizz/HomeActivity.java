package com.jasper.microquizz;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    private TextView tv_description;
    private Button bt_play;
    private Button bt_highscore;
    private Button bt_location;
    private Button bt_plus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        findByID();
        setBackGroundColors();
    }

    public void findByID() {
        tv_description = findViewById(R.id.tv_description);
        bt_play = findViewById(R.id.bt_play);
        bt_highscore = findViewById(R.id.bt_highscore);
        bt_location = findViewById(R.id.bt_location);
        bt_plus = findViewById(R.id.bt_plus);
    }

    public void setBackGroundColors() {
        GradientDrawable tv_description_bg = (GradientDrawable)tv_description.getBackground();
        GradientDrawable bt_play_bg = (GradientDrawable)bt_play.getBackground();
        GradientDrawable bt_highscore_bg = (GradientDrawable)bt_highscore.getBackground();
        GradientDrawable bt_location_bg = (GradientDrawable)bt_location.getBackground();
        GradientDrawable bt_plus_bg = (GradientDrawable)bt_plus.getBackground();

        tv_description_bg.setColor(getResources().getColor(R.color.colorBlue));
        bt_play_bg.setColor(getResources().getColor(R.color.colorBlue));
        bt_highscore_bg.setColor(getResources().getColor(R.color.colorBlue));
        bt_location_bg.setColor(getResources().getColor(R.color.colorGreen));
        bt_plus_bg.setColor(getResources().getColor(R.color.colorGreen));
    }
}
