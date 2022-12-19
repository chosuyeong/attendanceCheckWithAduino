package com.android.aduino1;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class login extends AppCompatActivity implements BeaconConsumer {

    private BeaconManager beaconManager;
    // 감지된 비콘들을 임시로 담을 리스트
    private List<Beacon> beaconList = new ArrayList<>();
    Button b1;
    ListView lv1;
    ArrayList<String> settings = new ArrayList();
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 실제로 비콘을 탐지하기 위한 비콘매니저 객체를 초기화
        beaconManager = BeaconManager.getInstanceForApplication(this);
        //textView = (TextView)findViewById(R.id.Textview);
        lv1 = (ListView) findViewById(R.id.lv1);
        mDatabase = FirebaseDatabase.getInstance().getReference("RoomNumber");
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        // 여기가 중요한데, 기기에 따라서 setBeaconLayout 안의 내용을 바꿔줘야 하는듯 싶다.
        // 필자의 경우에는 아래처럼 하니 잘 동작했음.
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));

        // 비콘 탐지를 시작한다. 실제로는 서비스를 시작하는것.
        beaconManager.bind(this);

        BottomNavigationView bottom = findViewById(R.id.bottom_navigation);
        bottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_login:
                        Intent a = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(a);
                        break;
                    case R.id.action_classN:
                        break;
                    case R.id.action_check:
//                        Intent b = new Intent(getApplicationContext(), attendance.class);
//                        b.putExtra("StudentID", studentId);
//                        startActivity(b);

                        break;
                }
                return false;
            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            // 비콘이 감지되면 해당 함수가 호출된다. Collection<Beacon> beacons에는 감지된 비콘의 리스트가,
            // region에는 비콘들에 대응하는 Region 객체가 들어온다.
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    beaconList.clear();
                    for (Beacon beacon : beacons) {
                        beaconList.add(beacon);

                    }
                }
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {   }
    }

    // 버튼이 클릭되면 textView 에 비콘들의 정보를 뿌린다.
    public void OnButtonClicked(View view){
        // 아래에 있는 handleMessage를 부르는 함수. 맨 처음에는 0초간격이지만 한번 호출되고 나면
        // 1초마다 불러온다.
        AudioManager  mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        // 비콘의 아이디와 거리를 측정하여 list에 넣는다.
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, settings);
        adapter.clear();
        adapter.notifyDataSetChanged();

        for(Beacon beacon : beaconList){
            if(beacon.getBluetoothAddress().equals("D8:A9:8B:AE:AE:CC")) {
                name = "수정관 201호";
                settings.add(name);
                lv1.setAdapter(adapter);
                lv1.setOnItemClickListener(onItemClickListener);


            }
            if(beacon.getBluetoothAddress().equals("D8:A9:8B:AE:B8:DE")) {
                name = "수정관 202호";
                settings.add(name);
                lv1.setAdapter(adapter);
                lv1.setOnItemClickListener(onItemClickListener);

                }
            }
        }
    String name;
    static String DataName;
    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            AudioManager  mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
            Object vo = (Object)parent.getAdapter().getItem(position);
            for(int a = 0; a < lv1.getCount() ; a++ ) {
                if (vo.equals("수정관 201호")) {
                    DataName = (String) vo;
                    mAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);  //진동
                    Intent intent = new Intent(login.this, ClassName.class);
                    startActivity(intent);
                    RoomNumber RM = new RoomNumber(DataName);
                    mDatabase.child(DataName).setValue(RM);
                    break;
                }else if(vo.equals("수정관 202호")){
                    DataName = (String) vo;
                    mAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);  //진동
                    Intent intent = new Intent(login.this, ClassName.class);
                    startActivity(intent);
                    RoomNumber RM = new RoomNumber(DataName);
                    mDatabase.child(DataName).setValue(RM);
                    break;
                }

            }
        }
    };

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}