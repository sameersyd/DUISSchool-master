package com.haze.sameer.duisschool;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DetailedNotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_notification);

        String titleString = null,msgString = null,dateString = null;
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            titleString = extras.getString("detailedTitle");
            msgString = extras.getString("detailedMsg");
            dateString = extras.getString("detailedDate");
        }
        TextView titleTxt = (TextView)findViewById(R.id.detailedNoti_titleTxt);
        TextView msgTxt = (TextView)findViewById(R.id.detailedNoti_msgTxt);
        TextView dateTxt = (TextView)findViewById(R.id.detailedNoti_dateTxt);
        Button close = (Button) findViewById(R.id.detailedNoti_closeBtn);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        titleTxt.setText(Html.fromHtml(titleString));
        msgTxt.setText(Html.fromHtml(msgString));
        dateTxt.setText(Html.fromHtml(dateString));
    }
}