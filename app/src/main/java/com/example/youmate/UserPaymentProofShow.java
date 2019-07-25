package com.example.youmate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class UserPaymentProofShow extends AppCompatActivity
{

    BottomNavigationView bottomNavigationView;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    DocumentReference documentReference;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    SharedPreferences settings;
    FirebaseAuth firebaseAuth;
    String userId;
    RecyclerView rc;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ImageView imageView;
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_payment_proof_show);

        ArrayAdapter<CharSequence> adapter1=ArrayAdapter.createFromResource(this,R.array.text,android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rc= findViewById(R.id.imgrc);
        rc.setLayoutManager(new LinearLayoutManager(this));
    //    rc.setAdapter();
     //   imageView=findViewById(R.id.imagee);


        bottomNavigationView=findViewById(R.id.nav1);
        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected( @NonNull MenuItem menuItem ) {
                switch (menuItem.getItemId()){
                    case R.id.item1:
                        startActivity(new Intent(getApplicationContext(),Main2Activity.class));
                        break;
                    case R.id.item2:
                        startActivity(new Intent(getApplicationContext(),Login.class));
                        break;
                    case R.id.item3:
                        startActivity(new Intent(getApplicationContext(), ChromeTabs.class));
                        break;
                    case R.id.item4:
                        startActivity(new Intent(getApplicationContext(),Download.class));
                        break;
                    case R.id.item5:
                        startActivity(new Intent(getApplicationContext(),AccountActivity.class));
                        break;
                }

            }
        });
        downloadImage();
    }
/*
    public void downloadImage()
    {

        try {
              final String uMail = auth.getCurrentUser().getEmail();
            Toast.makeText(UserPaymentProofShow.this, uMail, Toast.LENGTH_SHORT).show();
            //ed1=(EditText)findViewById(R.id.ed1);
           //String u=ed1.getText().toString();
                 documentReference =db.collection("UserPayProof").document(uMail);

                 documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                     @Override
                     public void onSuccess( DocumentSnapshot documentSnapshot ) {

                         //String imageurl=;
                         String ur=documentSnapshot.getString("url");
                         String url="https://firebasestorage.googleapis.com/v0/b/proweb-3fa6e.appspot.com/o/images%2F"+ur;

                         Toast.makeText(UserPaymentProofShow.this, url, Toast.LENGTH_SHORT).show();
                         Glide.with(UserPaymentProofShow.this)
                                 .load(url)
                                 .into(imageView);

                     }
                 }).addOnFailureListener(new OnFailureListener() {
                     @Override
                     public void onFailure( @NonNull Exception e ) {

                         Toast.makeText(UserPaymentProofShow.this, "Fail to get image!", Toast.LENGTH_SHORT).show();
                     }
                 });

        }
        catch (Exception e)
        {
            Toast.makeText(UserPaymentProofShow.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    */

    public void downloadImage()
    {

        try {
            final String uMail = auth.getCurrentUser().getEmail();
            Toast.makeText(UserPaymentProofShow.this, uMail, Toast.LENGTH_SHORT).show();
            DatabaseReference folderRef = rootRef.child("images/").child(uMail);

            ValueEventListener eventListener = new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    List<String> list = new ArrayList<>();
                    for(DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        String imageName = ds.getKey();
                        String imageUrl = ds.getValue(String.class);
                        list.add(imageUrl);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            folderRef.addListenerForSingleValueEvent(eventListener);
        }
        catch (Exception e)
        {
            Toast.makeText(UserPaymentProofShow.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    public  class imgadapter extends RecyclerView.Adapter<UserPaymentProofShow.imgadapter.myviewholder>
{

    int Layout;

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position)
    {

    }

    @Override
    public int getItemCount()
    {
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
