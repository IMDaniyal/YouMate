package com.example.youmate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.youmate.TabSwitcher.ChromeTabs;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener;
import com.google.firebase.auth.FirebaseAuth;

public class AccountActivity extends AppCompatActivity {

    TextView tvprofile,tvpoint,tvproof,tvhistory,tvhelp,tvsetting,tvleaderboard,tvbookmarks;
    //TextView changechannel;


    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    //    Intent home = new Intent(getApplicationContext(),Main2Activity.class);
     //   home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
     //   startActivity(home);

    }

    BottomNavigationView bottomNavigationView;
    SharedPreferences settings;
    FirebaseAuth firebaseAuth;
    String userId;
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        Intent old = getIntent();
        final Bundle data = old.getExtras();
        final int chromecheck= old.getIntExtra("chorme",-1);
        tvprofile=findViewById(R.id.tvprofile);
        tvpoint=findViewById(R.id.tvpoint);
        tvhistory=findViewById(R.id.tvhistory);
        tvproof=findViewById(R.id.tvproof);
        tvhelp=findViewById(R.id.tvhelp);
        tvsetting=findViewById(R.id.tvsetting);
        tvbookmarks=findViewById(R.id.tvbookmarks);

        tvleaderboard=findViewById(R.id.tvleaderboard);
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        userId = settings.getString("USER_ID", "");
        //changechannel = findViewById(R.id.setchannel);

        bottomNavigationView=findViewById(R.id.nav1);

        bottomNavigationView.setOnNavigationItemSelectedListener(
            new OnNavigationItemSelectedListener()
            {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
                {
                    boolean flag = true;
                    switch (menuItem.getItemId())
                    {
                        case R.id.item3:
                            flag=true;
                            if(chromecheck==1)
                            {
                                finish();
                            }
                            else
                            {
                                startActivity(new Intent(getApplicationContext(),ChromeTabs.class));
                                finish();
                            }
                            break;

                    }
                    return flag;
                }
            });

        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected( @NonNull MenuItem menuItem ) {
                switch (menuItem.getItemId())
                {
                    case R.id.item1:
                        Intent i = new Intent(getApplicationContext(),Main2Activity.class);
                        if(data !=null)
                        {
                            i.putExtras(data);
                        }
                        startActivity(i);
                        finish();
                        break;
                    case R.id.item2:
                        i = new Intent(getApplicationContext(),MainTry.class);
                        if(data !=null)
                        {
                            i.putExtras(data);
                        }
                        startActivity(i);
                        finish();
                        break;
                    /*
                        case R.id.item3:
                        if(chromecheck==1)
                        {
                            finish();
                        }
                        else
                        {
                            startActivity(new Intent(getApplicationContext(), ChromeTabs.class));
                            finish();
                        }
                        break;
                        */
                    case R.id.item4:
                         i = new Intent(getApplicationContext(),Download.class);
                        if(data !=null)
                        {
                            i.putExtras(data);
                        }
                        startActivity(i);
                        break;
                    case R.id.item5:
                        Toast.makeText(getApplicationContext(),"You already in Account Activity",Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        });

        bottomNavigationView.setSelectedItemId(R.id.item5);

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
        tvbookmarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                Intent profil=new Intent(AccountActivity.this,BookMarkPage.class);
                startActivity(profil);
            }
        });
        /*
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
*/
    }
}
