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

public class UserInfo extends AppCompatActivity {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    SharedPreferences settings;
    FirebaseAuth firebaseAuth;
    String userId,userEmail,userLevel,userPoint;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ProfileModel dataModel;
    ArrayList<ProfileModel> userData = new ArrayList<>();


    TextView user,age,phone,eswaId,level,point,bankname,bankholder,bankaccountno,add;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        settings = PreferenceManager.getDefaultSharedPreferences(this);
        userId = settings.getString("USER_ID", "");


        user = findViewById(R.id.tvname);
        age = findViewById(R.id.tvage);
        phone = findViewById(R.id.tvphone);
        add = findViewById(R.id.tvadd);
        eswaId = findViewById(R.id.tveswaid);
        level = findViewById(R.id.tvlevel);
        point = findViewById(R.id.tvscoor);
        bankname = findViewById(R.id.tvbankname);
        bankholder = findViewById(R.id.tvaccholdname);
        bankaccountno = findViewById(R.id.tvaccno);
        bottomNavigationView=findViewById(R.id.nav1);
        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected( @NonNull MenuItem menuItem ) {
                switch (menuItem.getItemId()){
                    case R.id.item1:
                        startActivity(new Intent(getApplicationContext(), Main2Activity.class));
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
                        finish();
                        break;
                    case R.id.item5:
                        startActivity(new Intent(getApplicationContext(),AccountActivity.class));
                        finish();
                        break;
                }

            }
        });

        //fire store method
        databaseQuery();
    }

    private void databaseQuery() {
        try {
            final String userMail = auth.getCurrentUser().getEmail();

            db.collection("UserInfo")
                    .whereEqualTo("email",userMail)
                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {

                        dataModel = documentSnapshot.toObject(ProfileModel.class);
                        userData.add(new ProfileModel(dataModel.getUsername(), dataModel.getAge(), dataModel.getEmail(), dataModel.getPhoneno(), dataModel.getPaymentid(),dataModel.getAddress(),dataModel.getAccountno(),dataModel.getAccountholdername(),dataModel.getBankname(),dataModel.getUrl(), dataModel.getLevel(), dataModel.getPoint()));

                    }

                    String userstr = dataModel.getUsername();
                    String agestr = dataModel.getAge();
                    String phonestr = dataModel.getPhoneno();
                    String addstr = dataModel.getAddress();
                    String paystr = dataModel.getPaymentid();
                    String bankstr = dataModel.getBankname();
                    String accnostr = dataModel.getAccountno();
                    String accholdstr = dataModel.getAccountholdername();
                    int levelstr = dataModel.getLevel();
                    int pointstr = dataModel.getPoint();

                    //set value
                    user.setText("Name: " + userstr);
                    age.setText("Age: " + agestr);
                    phone.setText("Phone No: " + phonestr);
                    phone.setText("Address: " + addstr);
                    eswaId.setText("eswaID: " + paystr);
                    bankname.setText("Bank Name:" + bankstr);
                    bankaccountno.setText("Account No:" + accnostr);
                    bankholder.setText("AccHolderName: " + accholdstr);
                    level.setText("Level: " + levelstr);
                    point.setText("Points: " + pointstr);



                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure( @NonNull Exception e ) {
                    Toast.makeText(UserInfo.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }

        catch (Exception e)
        {
            onBackPressed();
            startActivity(new Intent(getApplicationContext(),Login.class));
            Toast.makeText(UserInfo.this, "Please First LogIn!", Toast.LENGTH_SHORT).show();
        }
    }
}

