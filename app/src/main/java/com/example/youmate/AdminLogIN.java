package com.example.youmate;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.youmate.TabSwitcher.ChromeTabs;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminLogIN extends AppCompatActivity {

    EditText emailtxt,passtxt;
    Button btnsignin;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_log_in);

        emailtxt=findViewById(R.id.email_text);
        passtxt=findViewById(R.id.password_Text);
        btnsignin=findViewById(R.id.btsignin);
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
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    public boolean onClickMethod( View view ) {
        String mail="admin@proweb";
        String pass="adminproweb";
        String mailstr=emailtxt.getText().toString();
        String passstr=passtxt.getText().toString();
        if(mailstr.isEmpty() || passstr.isEmpty())
        {
            showToast("Fill Informationn");
            return false;
        }
        else if(mailstr.equals(mail) && passstr.equals(pass))
        {
            startActivity(new Intent(getApplicationContext(),Admin.class));
        }
        else {
            showToast("Please Enter your Correct Admin Info!");

        }

        return false;
    }
}
