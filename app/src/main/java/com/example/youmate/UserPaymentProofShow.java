package com.example.youmate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youmate.TabSwitcher.ChromeTabs;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

public class UserPaymentProofShow extends AppCompatActivity
{

    BottomNavigationView bottomNavigationView;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    SharedPreferences settings;
    FirebaseAuth firebaseAuth;
    String userId;
    RecyclerView rc;
    List<Bitmap> urls;
    imgadapter imgadapter;
    ImageLoader imageLoader = ImageLoader.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ImageView imageView;
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_payment_proof_show);
        urls= new ArrayList();
        imgadapter=new imgadapter(R.layout.user_paymentproof_recyclerrow,urls,UserPaymentProofShow.this);
        ArrayAdapter<CharSequence> adapter1=ArrayAdapter.createFromResource(this,R.array.text,android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rc= findViewById(R.id.imgrc);
        rc.setLayoutManager(new LinearLayoutManager(this));
        rc.setAdapter(imgadapter);
        imageLoader.init(ImageLoaderConfiguration.createDefault(getApplicationContext()));

    //    rc.setAdapter();
     //   imageView=findViewById(R.id.imagee);


        bottomNavigationView=findViewById(R.id.nav1);
        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected( @NonNull MenuItem menuItem ) {
                switch (menuItem.getItemId()){
                    case R.id.item1:
                        startActivity(new Intent(getApplicationContext(),Main2Activity.class));
                        finish();
                        break;
                    case R.id.item2:
                        Intent i=new Intent(getApplicationContext(),MainTry.class);
                        startActivity(i);
                         finish();
                        break;
                    case R.id.item3:
                        startActivity(new Intent(getApplicationContext(), ChromeTabs.class));
                        finish();
                        break;
                    case R.id.item4:
                        startActivity(new Intent(getApplicationContext(),Download.class));
                        break;
                    case R.id.item5:
                        startActivity(new Intent(getApplicationContext(),AccountActivity.class));
                        finish();
                        break;
                }

            }
        });
        downloadImage();
    }

    public void downloadImage()
    {



        try {

           final String uMail = auth.getCurrentUser().getEmail().replace(".","");
            Toast.makeText(UserPaymentProofShow.this, uMail, Toast.LENGTH_SHORT).show();
            //ed1=(EditText)findViewById(R.id.ed1);
           //String u=ed1.getText().toString();
            CollectionReference documentReference =db.collection("UserPayProof").document(uMail).collection("images");
           documentReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
               @Override
               public void onComplete(@NonNull Task<QuerySnapshot> task)
               {
                   for (QueryDocumentSnapshot document : task.getResult())
                   {

                       imageLoader.loadImage(document.getString("url").toString(), new SimpleImageLoadingListener()
                       {
                           @Override
                           public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage)
                           {
                               urls.add(loadedImage);
                               imgadapter.notifyDataSetChanged();
                           }

                           @Override
                           public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                               super.onLoadingFailed(imageUri, view, failReason);
                           }
                       });
                   }


               }
           });





        }
        catch (Exception e)
        {
            Toast.makeText(UserPaymentProofShow.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

/*
    public void downloadImage()
    {

        try {


            final String uMail = auth.getCurrentUser().getEmail().replace(".","");
            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference photosRef = rootRef.child("images").child(uMail);
            ValueEventListener eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<String> list = new ArrayList<>();
                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                        String imageName = ds.getKey();
                        String imageUrl = ds.getValue(String.class);
                        list.add(imageUrl);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            };
            photosRef.addListenerForSingleValueEvent(eventListener);
        }
        catch (Exception e)
        {
            Toast.makeText(UserPaymentProofShow.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    */


    public  class imgadapter extends RecyclerView.Adapter<UserPaymentProofShow.imgadapter.myviewholder>
{

    int Layout;
    List<Bitmap>urls;
    Context c;

    public imgadapter(int layout, List<Bitmap> urls, Context c) {
        Layout = layout;
        this.urls = urls;
        this.c = c;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(c).inflate(Layout,parent,false);
        return new myviewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final myviewholder holder, int position)
    {
        holder.img.setImageBitmap(urls.get(position));
    }



    @Override
    public int getItemCount()
    {
        if(urls!=null)
        {
            return urls.size();
        }
        return 0;
    }

    public class myviewholder extends RecyclerView.ViewHolder
    {

        ImageView img;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            img= itemView.findViewById(R.id.rcimgdesign);
        }
    }
}

}
