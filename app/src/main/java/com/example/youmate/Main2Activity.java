package com.example.youmate;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youmate.Modals.YoutubeDataModel;
import com.example.youmate.TabSwitcher.ChromeTabs;
import com.example.youmate.adapters.VideoPostAdapter;
import com.example.youmate.interfaces.OnItemClickListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity  {

    private static String GOOGLE_YOUTUBE_API_KEY= "AIzaSyBWkAQzRAz3_kD6zTnZQd8nTBm8-8prHg4";  // "AIzaSyDIChdnRLqZWj9JduzMDkjuHmlcxaOx2bs";//"AIzaSyDcotn0895Qc0VPyLMuqcTz239sCtqKL6E";
    private static String CHANNEL_ID =   "UCDDFDMN72wWNZoobe7TI5vg"; /*"UCDDFDMN72wWNZoobe7TI5vg";*///"UC-lHJZR3Gqxm24_Vd_AJ5Yw";
    private static String CHANNLE_GET_URL = "https://www.googleapis.com/youtube/v3/search?part=snippet&order=date&channelId="+CHANNEL_ID+"&maxResults=20&key="+GOOGLE_YOUTUBE_API_KEY+"";


    private BottomNavigationView bottomNavigationView;
    SharedPreferences settings;
    FirebaseAuth firebaseAuth;
    String userId;
    EditText edurl;
    ImageView imageqr,imgfb,imgyoutube,imginsta,imgcri,imggoogle,imgwiki,imglink,imgbbc,imggmail,imgtwit;
    TextView tvfb,tvyoutube,tvinsta,tvwiki,tvgoogle,tvcri,tvlink,tvbbc,tvgmail,tvtwit ;
    private RecyclerView mList_videos = null;
    private VideoPostAdapter adapter = null;
    private ArrayList<YoutubeDataModel> mListData = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        userId = settings.getString("USER_ID", "");
       // SharedPreferences pref = getApplicationContext().getSharedPreferences("channel_idpref", MODE_PRIVATE);
       // CHANNEL_ID = pref.getString("channelid", "UC_x5XG1OV2P6uZZ5FSM9Ttw");//"No name defined" is the default value.


        mList_videos=findViewById(R.id.recycler);
        edurl=findViewById(R.id.edurl);
        imgcri=findViewById(R.id.imagecri);
        imgfb=findViewById(R.id.imagefb);
        imgyoutube=findViewById(R.id.imageyoutube);
        imginsta=findViewById(R.id.imageinsta);
        imgwiki=findViewById(R.id.imagewiki);
        imggoogle=findViewById(R.id.imagegoogle);
        imglink=findViewById(R.id.imagelink);
        imgbbc=findViewById(R.id.imagebbc);
        imggmail=findViewById(R.id.imagegmail);
        imgtwit=findViewById(R.id.imagetwit);
        tvfb=findViewById(R.id.textfb);
        tvyoutube=findViewById(R.id.textyoutube);
        tvinsta=findViewById(R.id.textinsta);
        tvwiki=findViewById(R.id.textwiki);
        tvcri=findViewById(R.id.textcri);
        tvgoogle=findViewById(R.id.textgoogle);
        tvlink=findViewById(R.id.textlink);
        tvbbc=findViewById(R.id.textbbc);
        tvgmail=findViewById(R.id.textgmail);
        tvtwit=findViewById(R.id.texttwit);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("channel_id");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("Succes", "Value is: " + value);
                CHANNLE_GET_URL = "https://www.googleapis.com/youtube/v3/search?part=snippet&order=date&channelId="+value+"&maxResults=20&key="+GOOGLE_YOUTUBE_API_KEY+"";
                initList(mListData);
                new RequestYoutubeAPI().execute();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Error", "Failed to read value.", error.toException());
            }
        });


        edurl.setOnEditorActionListener(editorActionListener);
        bottomNavigationView=findViewById(R.id.nav1);
        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected( @NonNull MenuItem menuItem ) {
                switch (menuItem.getItemId()){
                    case R.id.item1:
                        Toast.makeText(Main2Activity.this,"You already in Home Activity",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.item2:
                        Intent i=new Intent(getApplicationContext(),MainTry.class);
                        startActivity(i);
                        finish();
                        break;
                    case R.id.item3:
                      //  Toast.makeText(Main2Activity.this, "You are Already on chrome tab", Toast.LENGTH_SHORT).show();
                           startActivity(new Intent(getApplicationContext(),ChromeTabs.class));
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
        //permission for storage
        ActivityCompat.requestPermissions(Main2Activity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        imgfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Main2Activity.this,MainActivityFacebook.class);
                startActivity(i);
            }
        });

        imgyoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Main2Activity.this,MainTry.class);
                startActivity(i);
            }
        });

        imginsta.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                String ip="https://www.instagram.com";
                Intent iweb=new Intent(Main2Activity.this,ChromeTabs.class);
                iweb.putExtra("IP",ip);
                startActivity(iweb);
            }
        });

        imgwiki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip="https://www.wikipedia.org";
                Intent iweb=new Intent(Main2Activity.this,ChromeTabs.class);
                iweb.putExtra("IP",ip);
                startActivity(iweb);
            }
        });

        imgcri.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String ip="https://www.cricbuzz.com";
                Intent iweb=new Intent(Main2Activity.this,ChromeTabs.class);
                iweb.putExtra("IP",ip);
                startActivity(iweb);
            }
        });

        imggoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip="https://google.com";
                Intent iweb=new Intent(Main2Activity.this,ChromeTabs.class);
                iweb.putExtra("IP",ip);
                startActivity(iweb);
            }
        });

        imglink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip="https://www.prashantx.com";
                Intent iweb=new Intent(Main2Activity.this,ChromeTabs.class);
                iweb.putExtra("IP",ip);
                startActivity(iweb);
            }
        });

        imgbbc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip="https://www.bbc.com/news";
                Intent iweb=new Intent(Main2Activity.this,ChromeTabs.class);
                iweb.putExtra("IP",ip);
                startActivity(iweb);
            }
        });

        imggmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip="https://www.google.com/gmail";
                Intent iweb=new Intent(Main2Activity.this,ChromeTabs.class);
                iweb.putExtra("IP",ip);
                startActivity(iweb);
            }
        });

        imgtwit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip="https://www.twitter.com/Twitter";
                Intent iweb=new Intent(Main2Activity.this,ChromeTabs.class);
                iweb.putExtra("IP",ip);
                startActivity(iweb);

            }
        });

    }


    private void initList(ArrayList<YoutubeDataModel> mListData) {
        mList_videos.setLayoutManager(new LinearLayoutManager(this));
        adapter = new VideoPostAdapter(this, mListData, new OnItemClickListener() {
            @Override
            public void onItemClick(YoutubeDataModel item) {
                YoutubeDataModel youtubeDataModel = item;
                Intent intent = new Intent(Main2Activity.this, DetailsActivity.class);
                intent.putExtra(YoutubeDataModel.class.toString(), youtubeDataModel);
                startActivity(intent);
                finish();
            }
        });
        mList_videos.setAdapter(adapter);

    }



    private class RequestYoutubeAPI extends AsyncTask<Void,String,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(CHANNLE_GET_URL);
            Log.e("URL", CHANNLE_GET_URL);
            try {
                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity httpEntity = response.getEntity();
                String json = EntityUtils.toString(httpEntity);
                return json;
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if(response!=null)
            {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("response", jsonObject.toString());
                    mListData = parseVideoListFromResponse(jsonObject);
                    initList(mListData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }


    public ArrayList<YoutubeDataModel> parseVideoListFromResponse(JSONObject jsonObject) {
        ArrayList<YoutubeDataModel> mList = new ArrayList<>();

        if (jsonObject.has("items")) {
            try {
                JSONArray jsonArray = jsonObject.getJSONArray("items");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = jsonArray.getJSONObject(i);
                    if (json.has("id")) {
                        JSONObject jsonID = json.getJSONObject("id");
                        String video_id = "";
                        if (jsonID.has("videoId")) {
                            video_id = jsonID.getString("videoId");
                        }
                        if (jsonID.has("kind")) {
                            if (jsonID.getString("kind").equals("youtube#video")) {
                                YoutubeDataModel youtubeObject = new YoutubeDataModel();
                                JSONObject jsonSnippet = json.getJSONObject("snippet");
                                String title = jsonSnippet.getString("title");
                                String description = jsonSnippet.getString("description");
                                String publishedAt = jsonSnippet.getString("publishedAt");
                                String thumbnail = jsonSnippet.getJSONObject("thumbnails").getJSONObject("high").getString("url");

                                youtubeObject.setTitle(title);
                                youtubeObject.setDescription(description);
                                youtubeObject.setPublishedAt(publishedAt);
                                youtubeObject.setThumbnail(thumbnail);
                                youtubeObject.setVideo_id(video_id);
                                mList.add(youtubeObject);

                            }
                        }
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return mList;

    }
    public void webquerySend( View view ) {
    }

    private TextView.OnEditorActionListener editorActionListener=new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction( TextView v, int actionId, KeyEvent event ) {

            switch (actionId)
            {
                case EditorInfo
                        .IME_ACTION_SEARCH:
                    String ip=edurl.getText().toString().trim();
                    Intent iweb=new Intent(Main2Activity.this,WebActivity.class);
                    iweb.putExtra("IP",ip);
                    startActivity(iweb);
                    break;
            }

            return false;
        }
    };

}
