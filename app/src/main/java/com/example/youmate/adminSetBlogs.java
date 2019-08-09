package com.example.youmate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.youmate.TabSwitcher.ChromeTabs;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;


public class adminSetBlogs extends AppCompatActivity {

    FloatingActionButton newBlog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_set_blogs);
        newBlog=findViewById(R.id.addnewBlogButton);
        newBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(adminSetBlogs.this,ActivityNewBlog.class);
                startActivity(i);
            }
        });
    }
    public  class blogsadapter extends RecyclerView.Adapter<adminSetBlogs.blogsadapter.myviewholder>
    {



        List<blogs> Blogs;
        Context c;

        public blogsadapter(Context c, List<blogs> Blogs)
        {
            this.c = c;
            this.Blogs =Blogs;
        }

        @NonNull
        @Override
        public adminSetBlogs.blogsadapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            View v = LayoutInflater.from(c).inflate(R.layout.bookmarkadapter,parent,false);
            return new adminSetBlogs.blogsadapter.myviewholder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull adminSetBlogs.blogsadapter.myviewholder holder, int position)
        {
            final int p=position;
            holder.Title.setText(Blogs.get(position).getTitle());
            holder.Description.setText(Blogs.get(position).getDescription());
            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(Blogs.get(position).getImgurl());
            // ImageView in your Activity
            ImageView imageView = findViewById(R.id.imageView);
            // Download directly from StorageReference using Glide// (See MyAppGlideModule for Loader registration)

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent i=new Intent(getApplicationContext(), .class);
//                    iweb.putExtra("IP",bookmarks.get(p));
//                    startActivity(iweb);
                }
            });


        }

        @Override
        public int getItemCount()
        {
            if(Blogs!=null)
            {
                return Blogs.size();
            }
            return 0;
        }

        public class myviewholder extends RecyclerView.ViewHolder
        {

            public TextView Title;
            public TextView Description;
            public ImageView img;

            public myviewholder(@NonNull View itemView)
            {
                super(itemView);
                this.Title = itemView.findViewById(R.id.edtitle);
                this.Description = itemView.findViewById(R.id.descriptiontxt);
                this.img=itemView.findViewById(R.id.imageView2);
            }


        }
    }
}
