package com.example.youmate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.youmate.TabSwitcher.ChromeTabs;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


public class adminSetBlogs extends AppCompatActivity {

    FloatingActionButton newBlog;
    blogsadapter badapter;
    List<blogs> Blogs;
    RecyclerView rc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Blogs=new ArrayList<blogs>();
        setContentView(R.layout.activity_admin_set_blogs);
        newBlog=findViewById(R.id.addnewBlogButton);
        newBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(adminSetBlogs.this,ActivityNewBlog.class);
                startActivity(i);
            }
        });
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query myMostViewedPostsQuery = databaseReference.child("blogs");
        myMostViewedPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the post
                    if(postSnapshot.exists()) {
                        Blogs.add(postSnapshot.getValue(blogs.class));
                    }
                }
                badapter=new blogsadapter(adminSetBlogs.this,Blogs);
                ArrayAdapter<CharSequence> adapter1=ArrayAdapter.createFromResource(adminSetBlogs.this,R.array.text,android.R.layout.simple_spinner_item);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                rc= findViewById(R.id.blogListSet);
                rc.setLayoutManager(new LinearLayoutManager(adminSetBlogs.this));
                rc.setAdapter(badapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w( "loadPost:onCancelled", databaseError.toException());
                // ...
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
            View v = LayoutInflater.from(c).inflate(R.layout.bloglistrecyclerview,parent,false);
            return new adminSetBlogs.blogsadapter.myviewholder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull final adminSetBlogs.blogsadapter.myviewholder holder, int position)
        {
            final int p=position;
            holder.Title.setText(Blogs.get(position).getTitle());
            holder.Description.setText(Blogs.get(position).getDescription());
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            storageRef.child("images/blogs/"+ Blogs.get(position).getEntry_num()+".jpg").getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    // Use the bytes to display the image
                    // ImageView in your Activity
                    // Download directly from StorageReference using Glide// (See MyAppGlideModule for Loader registration)
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    // Set the Bitmap data to the ImageView
                    holder.img.setImageBitmap(bmp);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
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
