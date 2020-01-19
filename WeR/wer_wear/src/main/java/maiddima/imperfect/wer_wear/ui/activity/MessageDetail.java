package maiddima.imperfect.wer_wear.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.widget.TextView;

import maiddima.imperfect.wer_wear.R;

public class MessageDetail extends WearableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);

        String studentMessage = "";
        int studentId = 0;
        double gpsLat = 0.0, gpsLong = 0.0;

        Intent intent = getIntent();
        if (null != intent){
            studentId = intent.getIntExtra("maiddima.imperfect.wer_wear.STUDENT_ID",-1);
            studentMessage = intent.getStringExtra("maiddima.imperfect.wer_wear.MESSAGE");
            gpsLat = intent.getDoubleExtra("maiddima.imperfect.wer_wear.GPS_LAT",-1);
            gpsLong = intent.getDoubleExtra("maiddima.imperfect.wer_wear.GPS_TOP",-1);
        }

        TextView tv_studentId = findViewById(R.id.tv_studentID);
        tv_studentId.setText("Student ID: "+Integer.toString(studentId));


        TextView tv_studentMessage = findViewById(R.id.tv_message);
        tv_studentMessage.setText(studentMessage);

        TextView tv_gpsLat = findViewById(R.id.tv_gpsLat);
        String stringLat = Double.toString(gpsLat);
        tv_gpsLat.setText("Latitude: "+stringLat);

        TextView tv_gpsLong = findViewById(R.id.tv_gpsLong);
        String stringLong = Double.toString(gpsLong);
        tv_gpsLong.setText("Longitude: "+stringLong);


    }
}
