package com.example.youmate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Help extends AppCompatActivity {

    Button btnok;
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        btnok=findViewById(R.id.btnok);
    }

    public void onClickMethod( View view ) {
        onBackPressed();
    }
}
