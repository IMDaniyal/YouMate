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

import com.example.youmate.Modals.ProfileModel;
import com.example.youmate.Modals.UserModel;
import com.example.youmate.TabSwitcher.ChromeTabs;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class UpdateUser extends AppCompatActivity {

    EditText username,age,phoneno,payment;
    TextView email;
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
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        username = findViewById(R.id.register_user);
        age = findViewById(R.id.register_age);
        phoneno = findViewById(R.id.register_phoneno);
        payment = findViewById(R.id.register_paymentid);
        email = findViewById(R.id.txemail);
        pd = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
        settings = PreferenceManager.getDefaultSharedPreferences(this);
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
                        startActivity(new Intent(getApplicationContext(),Login.class));
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

    }

    public void onClickRegister( View view ) {

        int btnId = view.getId();
        final String usertext=username.getText().toString().trim();
        final String agetext=age.getText().toString().trim();
        final String phonetext=phoneno.getText().toString().trim();
        final String paymenttext=payment.getText().toString().trim();

        if (btnId == R.id.register_submit) {

            pd.setMessage("Update User");
            pd.setCancelable(false);
            pd.show();
            pd.cancel();



            UserModel user = new UserModel();

            Bundle bundle = getIntent().getExtras();
            final String userid=bundle.getString("ID");
            //firestore reference
            email.setText(userid);
            DocumentReference feedback = db.collection("UserInfo").document(userid);


            //call feedback model
            ProfileModel userProfile = new ProfileModel();

            userProfile.setEmail(userid);
            userProfile.setUsername(usertext);
            userProfile.setAge(agetext);
            userProfile.setPhoneno(phonetext);
            userProfile.setPaymentid(paymenttext);
            feedback.set(userProfile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete( @NonNull Task<Void> task ) {
                    if (task.isSuccessful()) {
                        Toast.makeText(UpdateUser.this, "Data is Updated", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(UpdateUser.this, "Data not Updated", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            Intent intent = new Intent(UpdateUser.this, Admin.class);
            startActivity(intent);
            finish();

        }





}

}

