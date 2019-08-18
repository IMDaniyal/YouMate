package com.example.youmate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.youmate.TabSwitcher.ChromeTabs;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    EditText emailField,passField;
    TextView resetPass;
    String emailStr,PassStr;
    FirebaseAuth auth;
    ProgressDialog pd;
    SharedPreferences sharedPreferences;

    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        bottomNavigationView=findViewById(R.id.nav1);


        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected( @NonNull MenuItem menuItem ) {
                switch (menuItem.getItemId()){
                    case R.id.item1:
                        startActivity(new Intent(getApplicationContext(),Main2Activity.class));
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

                        break;
                    case R.id.item5:
                        startActivity(new Intent(getApplicationContext(),AccountActivity.class));
                        finish();
                        break;
                }

            }
        });
        emailField=findViewById(R.id.email_text);
        passField=findViewById(R.id.password_Text);
        auth = FirebaseAuth.getInstance();
        resetPass = findViewById(R.id.reset_password);
        pd = new ProgressDialog(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ForgotPasswordActivity.class));
            }
        });


    }

    public boolean checkData() {
        emailStr = emailField.getText().toString();
        PassStr = passField.getText().toString();

        if (emailStr.length() < 14 && PassStr.length() < 6) {
            showToast("Please! Enter Correct Details");
            return false;
        } else if (emailStr.length() < 14 && PassStr.length() < 6) {
            if (emailStr.length() < 14) {
                showToast("Invalid Email");
                return false;
            }


            if (PassStr.length() < 6) {
                showToast("Password Should be greater than 6 letter");
                return false;

            }

        }
        else if (emailStr.isEmpty() || PassStr.isEmpty()){
            showToast("Fill Informationn");
            return false;
        }
        else {


            return true;
        }

        return false;
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    public void onClickMethod(View view)
    {
        int btnId = view.getId();
        Intent intent;
        String mail="admin@proweb";
        String pass="adminproweb";
        emailStr=emailField.getText().toString();
        PassStr=passField.getText().toString();
        if(emailStr.isEmpty() || PassStr.isEmpty())
        {
            showToast("Fill Informationn");
        }
        else if(emailStr.equals(mail) && PassStr.equals(pass))
        {
            startActivity(new Intent(getApplicationContext(),Admin.class));
        }
        else {
            showToast("Please Enter your Correct Admin Info!");

        }
        switch (btnId) {
            case R.id.btsignup:
                if (!emailStr.equals(mail) && !PassStr.equals(pass)) {
                    if (checkData()) {
                        //authenticate user

                        pd.setMessage("Authenticating User");
                        pd.setCancelable(false);
                        pd.show();
                        // pd.dismiss();
                        auth.signInWithEmailAndPassword(emailStr, PassStr).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete( @NonNull Task<AuthResult> task ) {
                                if (!task.isSuccessful()) {
                                    showToast("Authentication Failed");
                                    pd.dismiss();
                                } else {
                                    pd.dismiss();
                                    FirebaseUser user = auth.getCurrentUser();
                                    if (user.isEmailVerified()) {

                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("USER_ID", auth.getCurrentUser().getUid());
                                        editor.apply();
                                        startActivity(new Intent(getApplicationContext(), Main2Activity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Your Email not verified\n Please verfied it from inbox", Toast.LENGTH_SHORT).show();
                                        pd.dismiss();
                                    }

                                }
                            }
                        });

                    }
                    break;


                }

        }
        emailField.getText().clear();
        passField.getText().clear();
    }

    public void onClickMethod2( View view ) {
        startActivity(new Intent(getApplicationContext(), Register.class));
    }
}
