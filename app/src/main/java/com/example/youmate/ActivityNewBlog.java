package com.example.youmate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class ActivityNewBlog extends AppCompatActivity {

    ImageView iv;
    TextView title,description,link;
    Button bt;
    final int imagerequest=71;
    blogs b;
    Uri imagepath;
    String templink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_blog);
        iv=findViewById(R.id.imageView5);
        title=findViewById(R.id.titlenewblogtxt);
        description=findViewById(R.id.descnewblogtxt);
        link=findViewById(R.id.urlnewblogtxt);
        bt=findViewById(R.id.button2);
        templink="";
        b=new blogs();

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i=new Intent();
                    i.setType("image/*");
                    i.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(i,imagerequest);

                }
                catch (Exception e)
                {
                    Toast.makeText(ActivityNewBlog.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(title.getText().toString()!=null && link.getText().toString()!=null)
                {
                    if(!templink.equals("")) {
                        b.setTitle(title.getText().toString());
                        b.setDescription(description.getText().toString());
                        b.setImgurl(templink);
                        b.setLink(link.getText().toString());
                        upload();
                        finish();
                    }
                    else
                    {
                        Toast.makeText(ActivityNewBlog.this,"Choose the Image for the blog", Toast.LENGTH_SHORT).show();

                    }
                }
                else
                {
                    Toast.makeText(ActivityNewBlog.this,"Fill the empty fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
            Toast.makeText(ActivityNewBlog.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return null;
    }
    public void upload()
    {

        DatabaseReference mDatabase;
// ...
        mDatabase = FirebaseDatabase.getInstance().getReference();
        b.setEntry_num(mDatabase.child("blogs").push().getKey());
        mDatabase.child("blogs").child(b.getEntry_num()).setValue(b);





        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        StorageReference storageReference;
        storageReference= FirebaseStorage.getInstance().getReference();

        final String imgname=b.getEntry_num()+"."+getExtention(imagepath);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        final StorageReference ref = storageReference.child("images/blogs"+"/"+imgname);
        ref.putFile(imagepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                {
                    @Override
                    public void onSuccess(Uri uri)
                    {
                        HashMap<String, String> map= new HashMap<>();
                        String url = uri.toString();
                        map.put("url",url);
                        db.collection("Blogs").document(b.getEntry_num()).collection("images").document(imgname).set(map).addOnSuccessListener(new OnSuccessListener<Void>()
                        {
                            @Override
                            public void onSuccess( Void aVoid )
                            {
                                Toast.makeText(ActivityNewBlog.this, "Url Uploaded", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener()
                        {
                            @Override
                            public void onFailure( @NonNull Exception e ) {
                                Toast.makeText(ActivityNewBlog.this, "Url failed to Uploaded", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
            }
        });


    }

    @Override
    protected void onActivityResult( int requestCode, int resultCode, @Nullable Intent data ) {
        try {

            super.onActivityResult(requestCode, resultCode, data);

            if(requestCode==imagerequest && resultCode== RESULT_OK && data!=null && data.getData()!=null)
            {
                imagepath=data.getData();
                templink=imagepath.toString();
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),imagepath);
                iv.setImageBitmap(bitmap);
            }

        }
        catch (Exception e)
        {
            Toast.makeText(ActivityNewBlog.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
