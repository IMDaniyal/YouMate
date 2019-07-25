package com.example.youmate;

import android.view.View.OnClickListener;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.youmate.TabSwitcher.ChromeTabs;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class AccountActivity extends AppCompatActivity {

    TextView tvprofile,tvpoint,tvproof,tvhistory,tvhelp,tvsetting,tvleaderboard;
    TextView changechannel;


    BottomNavigationView bottomNavigationView;
    SharedPreferences settings;
    FirebaseAuth firebaseAuth;
    String userId;
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        tvprofile=findViewById(R.id.tvprofile);
        tvpoint=findViewById(R.id.tvpoint);
        tvhistory=findViewById(R.id.tvhistory);
        tvproof=findViewById(R.id.tvproof);
        tvhelp=findViewById(R.id.tvhelp);
        tvsetting=findViewById(R.id.tvsetting);
        tvleaderboard=findViewById(R.id.tvleaderboard);
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        userId = settings.getString("USER_ID", "");
        changechannel = findViewById(R.id.setchannel);

        bottomNavigationView=findViewById(R.id.nav1);
        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected( @NonNull MenuItem menuItem ) {
                switch (menuItem.getItemId())
                {
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
                        Toast.makeText(getApplicationContext(),"You already in Account Activity",Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        });


        tvprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                Intent profile=new Intent(AccountActivity.this,UserInfo.class);
                startActivity(profile);
            }
        });
        tvpoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                Intent profile=new Intent(AccountActivity.this,Profile.class);
                startActivity(profile);
            }
        });
        /*tvhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                Intent profile=new Intent(AccountActivity.this,Profile.class);
                startActivity(profile);
            }
        });*/
        tvproof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                startActivity(new Intent(getApplicationContext(),UserPaymentProofShow.class));
            }
        });
        tvhelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                Intent profile=new Intent(AccountActivity.this,Help.class);
                startActivity(profile);
            }
        });
        tvsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                Intent profile=new Intent(AccountActivity.this,ForgotPasswordActivity.class);
                startActivity(profile);
            }
        });

        tvleaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                Intent profil=new Intent(AccountActivity.this,LeaderBoard.class);
                startActivity(profil);
            }
        });
        changechannel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profil=new Intent(AccountActivity.this,WebActivity.class);
                profil.putExtra("setchannel",1);
                profil.putExtra("IP","https://www.youtube.com");
                profil.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(profil);


            }
        });

    }
}
