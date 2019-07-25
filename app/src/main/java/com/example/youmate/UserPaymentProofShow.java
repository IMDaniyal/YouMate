package com.example.youmate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.youmate.TabSwitcher.ChromeTabs;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UserPaymentProofShow extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FirebaseStorage storage = FirebaseStorage.getInstance();

    StorageReference storageRef = storage.getReference();
    DocumentReference documentReference;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    SharedPreferences settings;
    FirebaseAuth firebaseAuth;
    String userId;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ImageView imageView;
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_payment_proof_show);

        ArrayAdapter<CharSequence> adapter1=ArrayAdapter.createFromResource(this,R.array.text,android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        imageView=findViewById(R.id.imagee);


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
    public void downloadImage() {


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



}
