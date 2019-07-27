package com.example.youmate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.youmate.Modals.ProfileModel;
import com.example.youmate.TabSwitcher.ChromeTabs;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Profile extends AppCompatActivity {


    FirebaseAuth auth = FirebaseAuth.getInstance();
    SharedPreferences settings;
    FirebaseAuth firebaseAuth;
    String userId,userEmail,userLevel,userPoint;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ProfileModel dataModel;
    ArrayList<ProfileModel> userData = new ArrayList<>();


    TextView email,level,scoor;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        settings = PreferenceManager.getDefaultSharedPreferences(this);
        userId = settings.getString("USER_ID", "");


        email = findViewById(R.id.feedback);
        level = findViewById(R.id.button);
        scoor = findViewById(R.id.textView26);
        bottomNavigationView=findViewById(R.id.nav1);
        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected( @NonNull MenuItem menuItem ) {
                switch (menuItem.getItemId()){
                    case R.id.item1:
                        startActivity(new Intent(getApplicationContext(),Main2Activity.class));
                        break;
                    case R.id.item2:
                        Intent i=new Intent(getApplicationContext(),MainTry.class);
                        startActivity(i);
                        finish();
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
        //fire store method
        databaseQuery();
    }

    private void databaseQuery() {

        try {

            String userMail = auth.getCurrentUser().getEmail();

            db.collection("UserInfo")
                    .whereEqualTo("email", userMail)
                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess( QuerySnapshot queryDocumentSnapshots ) {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {

                        dataModel = documentSnapshot.toObject(ProfileModel.class);

                        userData.add(new ProfileModel(dataModel.getUsername(), dataModel.getAge(), dataModel.getEmail(), dataModel.getPhoneno(), dataModel.getPaymentid(),dataModel.getAddress(),dataModel.getAccountno(),dataModel.getAccountholdername(),dataModel.getBankname(),dataModel.getUrl(), dataModel.getLevel(), dataModel.getPoint()));


                    }

                    //set value
                    email.setText(dataModel.getEmail());
                    level.setText("Level: " + dataModel.getLevel());
                    scoor.setText("Points: " + dataModel.getPoint());

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure( @NonNull Exception e ) {
                    Toast.makeText(Profile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
        catch (Exception e)
        {
            onBackPressed();
            startActivity(new Intent(getApplicationContext(), Login.class));
            Toast.makeText(Profile.this, "Please First LogIn!", Toast.LENGTH_SHORT).show();
        }
    }

}
