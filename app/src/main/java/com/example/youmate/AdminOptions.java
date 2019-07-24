package com.example.youmate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AdminOptions extends AppCompatActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_options);
    }

    public void onClickMethod( View view ) {

    }

    public void onClickMethod2( View view ) {
        startActivity(new Intent(getApplicationContext(),Admin.class));
    }
}
