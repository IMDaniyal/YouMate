package com.example.youmate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.youmate.Modals.ProfileModel;

import java.util.ArrayList;

public class BookMarkPage extends AppCompatActivity {


    TextView tv1;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_mark_page);

//        Bundle bundle = getIntent().getExtras();
//        String u= bundle.getString("Url");

        tv1=findViewById(R.id.tv1);
        final SharedPreferences sharedPreferences=getSharedPreferences("MyPref",MODE_PRIVATE);
        String a=sharedPreferences.getString("Url","");
        tv1.setText(a);
        tv1.setVisibility(View.VISIBLE);
        ImageButton button=findViewById(R.id.bt1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                tv1.setText("");
                tv1.setVisibility(View.INVISIBLE);

                sharedPreferences.edit().remove("Url").commit();

            }
        });

        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                String ip=tv1.getText().toString().trim();
                Intent iweb=new Intent(BookMarkPage.this,WebActivity.class);
                iweb.putExtra("IP",ip);
                startActivity(iweb);
            }
        });

    }



}
