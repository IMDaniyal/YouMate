package com.example.youmate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.youmate.Modals.ProfileModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ResetPoints extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ProfileModel dataModel;
    ArrayList<ProfileModel> userData = new ArrayList<>();
    EditText edpoi,edlev;
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_points);
        edpoi=findViewById(R.id.tvpoi);
        edlev=findViewById(R.id.tvlev);
    }

    public void updateClick( View view ) {
        Bundle bundle = getIntent().getExtras();
        String id= bundle.getString("ID");
        String poi=edpoi.getText().toString();
        String lev=edlev.getText().toString();
        db.collection("UserInfo").document(id)
                .update(
                        "level", lev,
                        "point", poi
                ).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete( @NonNull Task<Void> task ) {
                if (task.isSuccessful()) {
                    Toast.makeText(ResetPoints.this, "Data Update", Toast.LENGTH_SHORT).show();

                    finish();
                    startActivity(getIntent());

                } else {
                    Toast.makeText(ResetPoints.this, "Data Update", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void resetClick( View view ) {

        Bundle bundle = getIntent().getExtras();
        String id= bundle.getString("ID");

        db.collection("UserInfo").document(id)
                .update(
                        "level", 0,
                        "point", 0
                ).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete( @NonNull Task<Void> task ) {
                if (task.isSuccessful()) {
                    Toast.makeText(ResetPoints.this, "Data Reset", Toast.LENGTH_SHORT).show();

                    finish();
                    startActivity(getIntent());

                } else {
                    Toast.makeText(ResetPoints.this, "Data Reset", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
