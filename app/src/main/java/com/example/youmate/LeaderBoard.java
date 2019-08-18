package com.example.youmate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youmate.Modals.ProfileModel;
import com.example.youmate.TabSwitcher.ChromeTabs;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class LeaderBoard extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    SharedPreferences settings;
    FirebaseAuth firebaseAuth;
    String userId,userEmail,userLevel,userPoint;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ProfileModel dataModel;
    ArrayList<ProfileModel> userData = new ArrayList<>();
    RecyclerView rvPlacesData;


    TextView email,level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);

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
                        break;
                    case R.id.item3:
                        startActivity(new Intent(getApplicationContext(), ChromeTabs.class));
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
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        userId = settings.getString("USER_ID", "");

        rvPlacesData = findViewById(R.id.datarecycle);

        /*email = findViewById(R.id.feedback);
        level = findViewById(R.id.button);*/

        //fire store method
        databaseQuery();
    }

    private void databaseQuery() {
        userData.clear();

       // String userMail = auth.getCurrentUser().getEmail();
try {
    CollectionReference collectionReference = db.collection("UserInfo");
    collectionReference.orderBy("level", Query.Direction.DESCENDING).limit(10)
            .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

        @Override
        public void onSuccess( QuerySnapshot queryDocumentSnapshots ) {
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {

                dataModel = documentSnapshot.toObject(ProfileModel.class);
                userData.add(new ProfileModel(dataModel.getUsername(), dataModel.getAge(), dataModel.getEmail(), dataModel.getPhoneno(), dataModel.getPaymentid(),dataModel.getAddress(),dataModel.getAccountno(),dataModel.getAccountholdername(),dataModel.getBankname(),dataModel.getUrl(), dataModel.getLevel(), dataModel.getPoint()));

            }

            rvPlacesData.setAdapter(new LeaderBoard.MyAdapter());
            rvPlacesData.setLayoutManager(new LinearLayoutManager(LeaderBoard.this));
            //set value
//                email.setText(dataModel.getEmail());
//                level.setText("Level: "+dataModel.getLevel());

        }
    }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure( @NonNull Exception e ) {
            Toast.makeText(LeaderBoard.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    });
}
catch (Exception e)
{
    Toast.makeText(LeaderBoard.this, e.getMessage(), Toast.LENGTH_SHORT).show();
}
    }

class MyAdapter extends RecyclerView.Adapter<LeaderBoard.MyAdapter.MyViewHolder>{


    @NonNull
    @Override
    public LeaderBoard.MyAdapter.MyViewHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType ) {
        return new LeaderBoard.MyAdapter.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.levellist,parent,false));
    }


    @Override
    public void onBindViewHolder( @NonNull LeaderBoard.MyAdapter.MyViewHolder holder, int position ) {
        holder.tvemail.setText("Email: "+userData.get(position).getEmail());
        holder.tvpoints.setText("Points: " + userData.get(position).getPoint());
        holder.tvlevel.setText("Level: " + userData.get(position).getLevel());
    }


    @Override
    public int getItemCount() {
        return userData.size();
    }

    public class MyViewHolder  extends RecyclerView.ViewHolder{
        TextView tvemail,tvlevel,tvpoints;
        public MyViewHolder( @NonNull View itemView ) {
            super(itemView);

            tvemail=itemView.findViewById(R.id.tvemail);
            tvpoints=itemView.findViewById(R.id.tvpoints);
            tvlevel=itemView.findViewById(R.id.tvlevel);
        }

    }
}

}
