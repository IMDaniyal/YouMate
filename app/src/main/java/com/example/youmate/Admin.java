package com.example.youmate;

import android.view.View.OnClickListener;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.youmate.Modals.ProfileModel;
import com.example.youmate.Modals.UserModel;
import com.example.youmate.TabSwitcher.ChromeTabs;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Admin extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    SharedPreferences settings;
    FirebaseAuth firebaseAuth;
    String userId,userEmail,userLevel,userPoint;
    String TAG;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ProfileModel dataModel;
    ArrayList<ProfileModel> userData = new ArrayList<>();
    RecyclerView rvPlacesData;
    TextView email,level;
    ImageView imghome,imgtabs,imgaddtab,imgdownload,imguser;
    Button channel ;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        channel = findViewById(R.id.channelset);
        channel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profil=new Intent(Admin.this,WebActivity.class);
                profil.putExtra("setchannel",1);
                profil.putExtra("IP","https://www.youtube.com");
               // profil.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(profil);
            }
        });
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        userId = settings.getString("USER_ID", "");

        rvPlacesData = findViewById(R.id.adminrecycler);

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
        databaseQuery();
    }

    private void databaseQuery() {
        userData.clear();

//         String userMail = auth.getCurrentUser().getEmail();


        CollectionReference collectionReference=db.collection("UserInfo");
        collectionReference.orderBy("email", Query.Direction.DESCENDING).limit(10)

                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot: queryDocumentSnapshots.getDocuments()){

                    dataModel = documentSnapshot.toObject(ProfileModel.class);

                    userData.add(new ProfileModel(dataModel.getUsername(), dataModel.getAge(), dataModel.getEmail(), dataModel.getPhoneno(), dataModel.getPaymentid(),dataModel.getAddress(),dataModel.getAccountno(),dataModel.getAccountholdername(),dataModel.getBankname(),dataModel.getUrl(), dataModel.getLevel(), dataModel.getPoint()));


                }


                rvPlacesData.setAdapter(new Admin.MyAdapter());
                rvPlacesData.setLayoutManager(new LinearLayoutManager(Admin.this));
                //set value
//                email.setText(dataModel.getEmail());
//                level.setText("Level: "+dataModel.getLevel());

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Admin.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private class MyAdapter extends RecyclerView.Adapter<Admin.MyAdapter.MyViewHolder>{


        @NonNull
        @Override
        public Admin.MyAdapter.MyViewHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType ) {
            return new Admin.MyAdapter.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adminrecyclerview,parent,false));


        }


        @Override
        public void onBindViewHolder( Admin.MyAdapter.MyViewHolder holder, final int position ) {


            holder.tvemail.setText("Email: "+userData.get(position).getEmail());


            holder.btnshow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick( View v ) {
                    Intent iweb=new Intent(Admin.this,User.class);
                    iweb.putExtra("ID",userData.get(position).getEmail());
                    startActivity(iweb);
                }
            });

            holder.btnupdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick( View v ) {

                    Intent iweb=new Intent(Admin.this,UpdateUser.class);
                    iweb.putExtra("ID",userData.get(position).getEmail());
                    startActivity(iweb);
                }
            });

            holder.btnreset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick( View v ) {

                    String idd=userData.get(position).getEmail();
                    UserModel user = new UserModel();
                    Intent iweb=new Intent(Admin.this,ResetPoints.class);
                    iweb.putExtra("ID",idd);
                    startActivity(iweb);



                }
            });
            holder.btndelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick( View v ) {

                    try {
                        String id = userData.get(position).getEmail();
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        user.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete( @NonNull Task<Void> task ) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(Admin.this, "Successfully delete user", Toast.LENGTH_SHORT).show();
                                            Log.d(TAG, "User account deleted.");

                                            finish();
                                            startActivity(getIntent());
                                        }
                                    }
                                });


                        db.collection("UserInfo").document(id)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess( Void aVoid ) {
                                        Toast.makeText(Admin.this, "Successfully delete user", Toast.LENGTH_SHORT).show();
                                        Log.d(TAG, "DocumentSnapshot successfully deleted!");

                                        finish();
                                        startActivity(getIntent());
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure( @NonNull Exception e ) {
                                        Log.w(TAG, "Error deleting document", e);
                                    }
                                });
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(Admin.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            });

            holder.btnpproof.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick( View v ) {

                    Intent iweb=new Intent(Admin.this,PaymentProof.class);
                    iweb.putExtra("ID",userData.get(position).getEmail());
                    startActivity(iweb);
                }
            });


        }

        @Override
        public int getItemCount() {
            return userData.size();
        }

        public class MyViewHolder  extends RecyclerView.ViewHolder{
            TextView tvemail;
            Button btnshow,btnupdate,btnreset,btndelete,btnpproof;
            public MyViewHolder( @NonNull View itemView ) {
                super(itemView);

                tvemail=itemView.findViewById(R.id.tvmail);
                btnshow=itemView.findViewById(R.id.btnshow);
                btnupdate=itemView.findViewById(R.id.btnupdate);
                btnreset=itemView.findViewById(R.id.btnreset);
                btndelete=itemView.findViewById(R.id.btndelete);
                btnpproof=itemView.findViewById(R.id.btnpproof);
            }

        }
    }

}
