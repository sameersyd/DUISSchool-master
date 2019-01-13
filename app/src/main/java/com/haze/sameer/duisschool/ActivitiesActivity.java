package com.haze.sameer.duisschool;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class ActivitiesActivity extends AppCompatActivity {

    int actPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            actPos = extras.getInt("actPos");
        }
        ImageView imageView = (ImageView)findViewById(R.id.activities_imageView);
        switch (actPos){
            case 0:
                imageView.setImageDrawable(getDrawable(R.drawable.act_karate));
                break;
            case 1:
                imageView.setImageDrawable(getDrawable(R.drawable.act_yoga));
                break;
            case 2:
                imageView.setImageDrawable(getDrawable(R.drawable.act_bharathanatiyam));
                break;
            case 3:
                imageView.setImageDrawable(getDrawable(R.drawable.act_drawing));
                break;
            case 4:
                imageView.setImageDrawable(getDrawable(R.drawable.act_music));
                break;
            case 5:
                imageView.setImageDrawable(getDrawable(R.drawable.act_keyboard));
                break;
        }
    }
}
