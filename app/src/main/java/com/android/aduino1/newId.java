package com.android.aduino1;

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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class newId extends AppCompatActivity {
    EditText id, pw, name;
    Button b1;
    private DatabaseReference mDatabase;
    FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_id);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        id = (EditText)findViewById(R.id.id);
        pw = (EditText)findViewById(R.id.pw);
        name = (EditText)findViewById(R.id.name);
        b1 = (Button) findViewById(R.id.b1);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
                addUser();
            }
        });

    }
    private void addUser() {
        if (id.getText().toString().isEmpty()|| pw.getText().toString().isEmpty()|| name.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "내용을 입력하세요. ", Toast.LENGTH_SHORT).show();
            return;
        }else{
            User user = new User(id.getText().toString(), pw.getText().toString(), name.getText().toString());
            mDatabase.push().setValue(user);
        }
    }

    //Firebse creating a new user
    private void registerUser(){
        //사용자가 입력하는 email, password를 가져온다.
        String idEmail = id.getText().toString().trim();
        String password = pw.getText().toString().trim();
        //email과 password가 비었는지 아닌지를 체크 한다.
        if(TextUtils.isEmpty(idEmail)){
            Toast.makeText(this, "ID를 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Password를 입력해 주세요.", Toast.LENGTH_SHORT).show();
        }
        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(idEmail, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(newId.this, "회원가입 완료!", Toast.LENGTH_SHORT).show();
                        } else {
                            //에러발생시
                            Toast.makeText(newId.this, "등록 에러!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
