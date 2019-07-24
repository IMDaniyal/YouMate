package com.example.youmate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
 import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Main3Activity extends AppCompatActivity {
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        auth = FirebaseAuth.getInstance();
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        final String userId = settings.getString("USER_ID", "");
        FirebaseUser user=auth.getCurrentUser();


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(Main3Activity.this, Main2Activity.class));
                finish();
                /*if (!userId.equals("")) {

                    startActivity(new Intent(MainActivity.this, Main2Activity.class));
                    finish();


                }
                else
                {
                    startActivity(new Intent(MainActivity.this , Login.class));
                    finish();

                }*/
            }
        },3000);
    }
}
