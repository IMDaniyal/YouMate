package com.example.youmate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.youmate.Modals.ProfileModel;
import com.example.youmate.Modals.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class BankTransfer extends AppCompatActivity {

    EditText edaccno, edaccholder, edbank, edadd;
    FirebaseAuth auth;
    SharedPreferences settings;
    ProgressDialog pd;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    String userId;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_transfer);
        edadd = findViewById(R.id.register_add);
        edaccholder = findViewById(R.id.register_accountholdername);
        edaccno = findViewById(R.id.register_accountno);
        edbank = findViewById(R.id.register_bankname);
        pd = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        userId = settings.getString("USER_ID", "");

    }

    public void onClickbank( View view ) {

try {
    int btnId = view.getId();

    String accadd = edadd.getText().toString().trim();
    String acchold = edaccholder.getText().toString().trim();
    String accno = edaccno.getText().toString().trim();
    String accbank = edbank.getText().toString().trim();

    if (btnId == R.id.register_submit) {

        pd.setMessage("Update User");
        pd.setCancelable(false);
        pd.show();
        pd.cancel();
        String userMail = auth.getCurrentUser().getEmail();

        UserModel user = new UserModel();

        db.collection("UserInfo").document(userMail)
                .update(
                        "address", accadd,
                        "accountholdername", acchold,
                        "accountno", accno,
                        "bankname", accbank
                ).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete( @NonNull Task<Void> task ) {
                if (task.isSuccessful()) {
                    Toast.makeText(BankTransfer.this, "Data Update", Toast.LENGTH_SHORT).show();

                    finish();


                } else {
                    Toast.makeText(BankTransfer.this, "Data Update", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
}
catch (Exception e)
{
    Toast.makeText(getApplicationContext(), "Error"+e.getMessage(), Toast.LENGTH_SHORT).show();
}
        startActivity(new Intent(getApplicationContext(),UserPaymentProofShow.class));
    }
}

