package com.android.aduino1;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CheckAgain extends AppCompatActivity {
    FirebaseDatabase fb=FirebaseDatabase.getInstance();
    DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
    TextView lec, rnum;
    String stid= MainActivity.id.getText().toString();
    int idx= stid.indexOf("@");
    String stnum=stid.substring(0, idx);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        lec = (TextView) findViewById(R.id.lecture);
        rnum = (TextView) findViewById(R.id.roomnum);

        dbref = fb.getReference("출석");
        dbref.child("출석").equalTo(stnum).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String lecname = dataSnapshot.getValue().toString();
                lec.setText(lecname);
                //강의명 디비 추가되면 수정하기


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
