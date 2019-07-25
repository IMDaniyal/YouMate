package com.example.youmate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.youmate.Modals.ProfileModel;
import com.example.youmate.TabSwitcher.ChromeTabs;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.xml.transform.Result;

public class PaymentProof extends AppCompatActivity {

    String userEmail="";
    ImageView imageView;
    EditText edname;
    final int imagerequest=71;
    Uri imagepath;
    BottomNavigationView bottomNavigationView;
    StorageReference storageReference;
    DocumentReference documentReference;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    SharedPreferences settings;
    FirebaseAuth firebaseAuth;
    String userId;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ProfileModel dataModel;
    ArrayList<ProfileModel> userData = new ArrayList<>();
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_proof);

        Intent previous = getIntent();
        userEmail= previous.getStringExtra("ID");
        imageView=findViewById(R.id.imagev);
        edname= findViewById(R.id.edname);


        storageReference= FirebaseStorage.getInstance().getReference();
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
    }

    public void selectImage( View view )
    {
        try {
            Intent i=new Intent();
            i.setType("image/*");
            i.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(i,imagerequest);

        }
        catch (Exception e)
        {
            Toast.makeText(PaymentProof.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult( int requestCode, int resultCode, @Nullable Intent data ) {
       try {

           super.onActivityResult(requestCode, resultCode, data);

           if(requestCode==imagerequest && resultCode== RESULT_OK && data!=null && data.getData()!=null)
           {
               imagepath=data.getData();
               Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),imagepath);
               imageView.setImageBitmap(bitmap);
           }

       }
       catch (Exception e)
        {
            Toast.makeText(PaymentProof.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public <string> void uploadImage( View view)
    {
        try {

            if( imagepath!=null)
            {
              final  String userMail =userEmail.replace(",",""); // auth.getCurrentUser().getEmail();

                String imgname=edname.getText().toString()+"."+getExtention(imagepath);
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading...");
                progressDialog.show();
                //String imagetext=ed1.getText().toString()+"."+getExtention(imagepath);
                StorageReference ref = storageReference.child("images/"+userMail+"/"+imgname);
                ref.putFile(imagepath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss();

                                Toast.makeText(PaymentProof.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete( @NonNull Task<UploadTask.TaskSnapshot> task ) {
                        if(task.isSuccessful())
                        {
                            HashMap<String, String> map= new HashMap<>();
                            map.put("url",task.getResult().toString());
                            db.collection("UserPayProof").document(userMail)
                                    .set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess( Void aVoid ) {
                                    Toast.makeText(PaymentProof.this, "Url Uploaded", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure( @NonNull Exception e ) {
                                    Toast.makeText(PaymentProof.this, "Url failed to Uploaded", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(PaymentProof.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                        .getTotalByteCount());
                                progressDialog.setMessage("Uploaded "+(int)progress+"%");
                            }
                        });
            }

            else {
                Toast.makeText(PaymentProof.this, "Invalid Arrguments", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e)
        {
            Toast.makeText(PaymentProof.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private String getExtention(Uri uri)
    {
        try {

            ContentResolver contentResolver = getContentResolver();
            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
        }
        catch (Exception e)
        {
            Toast.makeText(PaymentProof.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return null;
    }


}
