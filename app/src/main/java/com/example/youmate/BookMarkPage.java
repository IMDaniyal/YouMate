package com.example.youmate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youmate.TabSwitcher.ChromeTabs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BookMarkPage extends AppCompatActivity {

    public interface OnUrlClickListener {
        void onUrllClick(String item);
    }

    public interface OnremoveClickListener {
        void onremovelClick(String item);
    }


    RecyclerView b;
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


        String[] arrOfStr = a.split("\\s*,\\s*");
        List<String> result = new ArrayList<String>(Arrays.asList(arrOfStr));

        b= findViewById(R.id.rcBookmark);
        b.setLayoutManager(new LinearLayoutManager(this));
        b.setHasFixedSize(true);
        b.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.HORIZONTAL));

        b.setItemAnimator(new DefaultItemAnimator());
        bookmarkadapter bkm=new bookmarkadapter(result,getApplicationContext(),a);
        b.setAdapter(bkm);



    }




    public  class bookmarkadapter extends RecyclerView.Adapter<BookMarkPage.bookmarkadapter.myviewholder>
    {



        List<String> bookmarks;
        Context c;
        String allurl;

        public bookmarkadapter(List<String> urls, Context c,String a)
        {
            this.bookmarks = urls;
            this.c = c;
            this.allurl =a;
        }

        @NonNull
        @Override
        public BookMarkPage.bookmarkadapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            View v = LayoutInflater.from(c).inflate(R.layout.bookmarkadapter,parent,false);
            return new BookMarkPage.bookmarkadapter.myviewholder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull myviewholder holder, int position)
        {
            final int p=position;
            holder.url.setText(bookmarks.get(position));
            holder.url.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent iweb=new Intent(getApplicationContext(), ChromeTabs.class);
                    iweb.putExtra("IP",bookmarks.get(p));
                    startActivity(iweb);
                }
            });
            holder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final SharedPreferences sharedPreferences=getSharedPreferences("MyPref",MODE_PRIVATE);

                    String remove = ","+bookmarks.get(p);
                    allurl=allurl.replace(remove,"");
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("Url",allurl);
                    editor.commit();
                    bookmarks.remove(p);
                    notifyDataSetChanged();
                }
            });

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

            public TextView url;
            public Button remove;

            public myviewholder(@NonNull View itemView)
            {
                super(itemView);
                this.url = itemView.findViewById(R.id.adapterUrl);
                this.remove = itemView.findViewById(R.id.adapterRemove);
            }


        }
    }



}
