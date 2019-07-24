package com.example.youmate;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Tabs extends AppCompatActivity {

    EditText edurl;
    ImageView imghome,imgtabs,imgaddtab,imgdownload,imguser,imageqr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);

        imghome=findViewById(R.id.imagehome);
        imgtabs=findViewById(R.id.imagetabs);
        imgaddtab=findViewById(R.id.imageadd);
        imgdownload=findViewById(R.id.imagedownload);
        imguser=findViewById(R.id.imageuser);
        edurl=findViewById(R.id.edurl);
        //imageqr=findViewById(R.id.imgsrch);

        imageqr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip=edurl.getText().toString().trim();
                Intent iweb=new Intent(Tabs.this,WebActivity.class);
                iweb.putExtra("IP",ip);
                startActivity(iweb);

            }
        });

        imghome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ihome=new Intent(Tabs.this,Main2Activity.class);
                startActivity(ihome);
            }
        });

        imgtabs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Tabs.this, "Tabs Page Already Open ", Toast.LENGTH_SHORT).show();
            }
        });

        imgaddtab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iatab=new Intent(getApplicationContext(),Main2Activity.class);
                startActivity(iatab);
            }
        });

        imgdownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent idownload=new Intent(Tabs.this,Download.class);
                startActivity(idownload);
            }
        });

        imguser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iprofile=new Intent(Tabs.this,Profile.class);
                startActivity(iprofile);
            }
        });
    }
}
