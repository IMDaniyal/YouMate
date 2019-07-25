package com.example.youmate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.youmate.Modals.ProfileModel;
import com.example.youmate.Modals.UserModel;
import com.example.youmate.TabSwitcher.ChromeTabs;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Register extends AppCompatActivity  {

    EditText username,age,phoneno,payment,email, password;
    String nameStr, passStr;
    FirebaseAuth auth;
    SharedPreferences settings;
    ProgressDialog pd;
    BottomNavigationView bottomNavigationView;
    String TAG= Register.class.getSimpleName();
    public static final String usern="UserName";
    public static final String Age="Age";
    public static final String phone="PhoneNo";
    public static final String Email="Email";
    public static final String pass="Password";
    public static final String paymentid="PaymentID";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username = findViewById(R.id.register_user);
        age = findViewById(R.id.register_age);
        phoneno = findViewById(R.id.register_phoneno);
        payment = findViewById(R.id.register_paymentid);
        password = findViewById(R.id.register_password);
        email = findViewById(R.id.register_email);
        pd = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        nameStr = email.getText().toString();
        passStr = password.getText().toString();
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

    public void onClickRegister(View view){
        int btnId = view.getId();
        Bundle bundle = getIntent().getExtras();

        final String usertext=username.getText().toString().trim();
        final String agetext=age.getText().toString().trim();
        String emailtext=email.getText().toString().trim();
        final String passtext=password.getText().toString().trim();
        final String phonetext=phoneno.getText().toString().trim();


        if (btnId == R.id.register_submit) {

            if (checkData())
            {

                try {
                    pd.setMessage("Registering User");
                    pd.setCancelable(false);
                    pd.show();
                    auth.createUserWithEmailAndPassword(nameStr, passStr).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete( @NonNull Task<AuthResult> task ) {
                            Toast.makeText(Register.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                            pd.cancel();
                            if (!task.isSuccessful()) {
                                Toast.makeText(Register.this, "Authentication failed." + task.getException(),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                String userId = auth.getCurrentUser().getUid();
                                sendverificationemail();
                                UserModel user = new UserModel();
                                user.email = nameStr;
                                user.password = passStr;
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putString("USER_ID", userId);
                                editor.apply();

                                String userid = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                                //firestore reference

                                DocumentReference feedback = db.collection("UserInfo").document(userid);


                                //call feedback model
                                ProfileModel userProfile = new ProfileModel();

                                userProfile.setUsername(usertext);
                                userProfile.setAge(agetext);
                                userProfile.setEmail(userid);
                                userProfile.setPhoneno(phonetext);
                                userProfile.setLevel(0);
                                userProfile.setPoint(5);
                                feedback.set(userProfile).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete( @NonNull Task<Void> task ) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(Register.this, "Data is added", Toast.LENGTH_SHORT).show();

                                        } else {
                                            Toast.makeText(Register.this, "Data not added", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                                Intent intent = new Intent(getApplicationContext(), EswaTransfer.class);
                                intent.putExtra("email",nameStr);
                                startActivity(intent);
                                finish();

                            }

                        }
                    });
                }
                catch (Exception e)
                {
                    Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }else {
            if (btnId==R.id.register_link){
                Intent intent = new Intent(getApplicationContext() , Login.class);
                startActivity(intent);
                finish();

            }

        }



    }

    //email verification code
    public void sendverificationemail() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(Register.this, "Email verification sent to "+user.getEmail(), Toast.LENGTH_SHORT).show();
                    } else{
                        Log.e(TAG,"sendEmailVerification", task.getException());
                        Toast.makeText(Register.this,
                                "Failed to send verification email.",
                                Toast.LENGTH_SHORT).show();
                    }
                    pd.dismiss();
                }
            });
        }
    }

    public boolean checkData() {
        nameStr = email.getText().toString();
        passStr = password.getText().toString();

        if (nameStr.isEmpty()  && passStr.length() < 6) {
            showToast("Invalid Arguments");
            return false;
        } else if (nameStr.isEmpty() || passStr.length() < 6  ) {

            if (nameStr.isEmpty()) {
                showToast("Email is Empty");
                return false;
            }


            if (passStr.length() < 6) {
                showToast("Password should be greater than 6 letter");
                return false;
            }




        } else {

            return true;

        }

        return false;
    }


    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        startActivity(new Intent(Register.this, MainActivity.class));
//        finish();
    }


}