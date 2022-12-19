package com.android.aduino1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    Button newid, login;
    EditText pw;
    FirebaseAuth firebaseAuth;
    static String nn;
    static EditText id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newid = (Button)findViewById(R.id.newid);
        login = (Button)findViewById(R.id.login);
        id = (EditText)findViewById(R.id.id);
        pw = (EditText)findViewById(R.id.pw);

        firebaseAuth = FirebaseAuth.getInstance();

        newid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 회원가입창
                Intent intent=new Intent(getApplicationContext(), newId.class);
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });


    }
    //firebase userLogin method
    private void userLogin(){
        final String idEmail = id.getText().toString().trim();
        String password = pw.getText().toString().trim();

        if(TextUtils.isEmpty(idEmail)){
            Toast.makeText(this, "ID를 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "password를 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        //logging in the user
        firebaseAuth.signInWithEmailAndPassword(idEmail, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()) {
                            finish();
                            Intent intent = new Intent(getApplicationContext(), login.class);
                            startActivity(intent);

                        } else {
                            Toast.makeText(getApplicationContext(), "로그인 실패!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

}
