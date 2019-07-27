package com.example.youmate;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
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

import com.example.youmate.UserPaymentProofShow.imgadapter;
import com.example.youmate.UserPaymentProofShow.imgadapter.myviewholder;
import java.util.ArrayList;
import java.util.List;

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


    public  class bookmarkadapter extends RecyclerView.Adapter<BookMarkPage.bookmarkadapter.myviewholder>
    {

        int Layout;
        List<String> bookmarks;
        Context c;

        public bookmarkadapter(int layout, List<String> urls, Context c)
        {
            Layout = layout;
            this.bookmarks = urls;
            this.c = c;
        }

        @NonNull
        @Override
        public BookMarkPage.bookmarkadapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            View v = LayoutInflater.from(c).inflate(Layout,parent,false);
            return new BookMarkPage.bookmarkadapter.myviewholder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull myviewholder holder, int position) {

        }



        @Override
        public int getItemCount()
        {
            if(bookmarks!=null)
            {
                return bookmarks.size();
            }
            return 0;
        }

        public class myviewholder extends RecyclerView.ViewHolder
        {

            public myviewholder(@NonNull View itemView)
            {
                super(itemView);
            }
        }
    }



}
