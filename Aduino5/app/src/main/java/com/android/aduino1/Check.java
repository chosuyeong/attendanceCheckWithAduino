package com.android.aduino1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Check extends AppCompatActivity {
    TextView className;
    Button button;
    long diff;
    long min;
    FirebaseDatabase database;
    String studentId;
    String email;

    private DatabaseReference rootClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        Intent intent = getIntent();
        email = intent.getExtras().getString("email");
        database = FirebaseDatabase.getInstance();
        studentId = email.substring(0,8);

        rootClass = FirebaseDatabase.getInstance().getReference();

        className = findViewById(R.id.className);
        button = findViewById(R.id.button);

        SimpleDateFormat dFormatter = new SimpleDateFormat("yyyy/MM/dd", Locale.KOREA);
        Date currentDate = new Date();
        String date = dFormatter.format(currentDate);

        SimpleDateFormat tFormatter = new SimpleDateFormat("HH:mm:ss", Locale.KOREA);
        Date currentTime = new Date();
        String ctime = tFormatter.format(currentTime);

        className.setText(date + " CEM " + ctime );

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Toast.makeText(Check.this, studentId, Toast.LENGTH_SHORT).show();
            }
        });

        try {
            Date start = tFormatter.parse("18:00:00");
            Date now = tFormatter.parse(ctime);
            diff = now.getTime() - start.getTime();
            min = diff / 60000;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (min <= 5) {
            button.setVisibility(View.VISIBLE);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rootClass.child("출석").push().setValue(studentId);
                    Toast.makeText(Check.this, "출석하셨습니다", Toast.LENGTH_SHORT).show();
                }
            });
        }

        else if (min <= 30) {
            button.setText("지각");
            button.setVisibility(View.VISIBLE);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rootClass.child("지각").push().setValue(studentId);
                    Toast.makeText(Check.this, "지각하셨습니다", Toast.LENGTH_SHORT).show();
                }
            });
        }

        else {
            button.setText("결석");
            button.setVisibility(View.VISIBLE);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rootClass.child("결석").push().setValue(studentId);
                    Toast.makeText(Check.this, "결석하셨습니다", Toast.LENGTH_SHORT).show();
                }
            });
        }


    }
}
