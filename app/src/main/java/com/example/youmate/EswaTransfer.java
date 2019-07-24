package com.example.youmate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.youmate.Modals.ProfileModel;
import com.example.youmate.Modals.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class EswaTransfer extends AppCompatActivity {


    FirebaseAuth auth;
    SharedPreferences settings;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    String userId;
    EditText edeswaid,edadd;
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eswa_transfer);
        edeswaid=findViewById(R.id.register_eswaid);
        edadd=findViewById(R.id.register_address);
        auth = FirebaseAuth.getInstance();
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        userId = settings.getString("USER_ID", "");

    }

    public void onClickeswa( View view ) {


        try {
            int btnId = view.getId();

            String accadd = edadd.getText().toString().trim();
            String eswaid = edeswaid.getText().toString().trim();

            if (btnId == R.id.register_submit) {

                String userMail = auth.getCurrentUser().getEmail();

                UserModel user = new UserModel();

                db.collection("UserInfo").document(userMail)
                        .update(
                                "address", accadd,
                                "paymentid", eswaid
                        ).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete( @NonNull Task<Void> task ) {
                        if (task.isSuccessful()) {

                            finish();
                            startActivity(new Intent(getApplicationContext(),UserPaymentProofShow.class));

                        } else {
                            Toast.makeText(EswaTransfer.this, "Data Update", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        }
catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(), "Error"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }


    }
}
