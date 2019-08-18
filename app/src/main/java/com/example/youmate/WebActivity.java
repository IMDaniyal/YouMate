package com.example.youmate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.youmate.TabSwitcher.ChromeTabs;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class WebActivity extends AppCompatActivity {


    WebView webView;
    ProgressBar progressBar;
    SharedPreferences settings;
    FirebaseAuth firebaseAuth;
    String userId;
    BottomNavigationView bottomNavigationView;
    MyHelper helper;
    FloatingActionButton donebutton;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    TextView urltext;
    Handler handler;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        urltext=findViewById(R.id.weburl);
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        userId = settings.getString("USER_ID", "");
        progressBar = findViewById(R.id.progressBar);
        webView = findViewById(R.id.web);
        donebutton = findViewById(R.id.floatingtick);
        webView.setVisibility(View.INVISIBLE);
        webView.getSettings().setJavaScriptEnabled(true);


        webView.setWebChromeClient(new WebChromeClient());
        Bundle bundle = getIntent().getExtras();

        String iip= bundle.getString("IP");

        helper=new MyHelper(WebActivity.this);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted( WebView view, String url, Bitmap favicon ) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);

            }


            @Override
            public void onPageFinished( WebView view, String url ) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);

            }
        });

        if(bundle.getInt("setchannel")==1)
        {
            webView.loadUrl(iip);

            //user come for setting channel
            donebutton.setVisibility(View.VISIBLE);

            donebutton.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v) {

                    String channelstring=webView.getUrl();
                    int index=-1;
                    String channel_id="";
                 if(channelstring.contains("user"))
                 {
                      index =channelstring.indexOf("user/");
                      index= index+5;
                      String username = channelstring.substring(index);
                      new channelidget(username).execute();
                 }
                 else if(channelstring.contains("channel/"))
                 {
                     int chan= channelstring.indexOf("channel/");
                     chan=chan+8;
                     channel_id= channelstring.substring(chan);
                     //addtofirebase
                     // Write a message to the database
                     FirebaseDatabase database = FirebaseDatabase.getInstance();
                     DatabaseReference myRef = database.getReference("channel_id");
                     myRef.setValue(channel_id).addOnCompleteListener(new OnCompleteListener<Void>() {
                         @Override
                         public void onComplete(@NonNull Task<Void> task) {
                             finish();
                         }
                     });

                   Toast.makeText(WebActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                 }
                 else
                 {
                     Toast.makeText(WebActivity.this, "Invalid page", Toast.LENGTH_SHORT).show();
                 }
                }
            });
        }
        else
        {
            donebutton.setVisibility(View.INVISIBLE);
            webView.loadUrl("https://www.google.com/search?q="+iip);

        }




//        String url=ed2.getText().toString();
//        Intent iweb=new Intent(WebActivity.this,BookMarkPage.class);
//        iweb.putExtra("Url",url);
//        startActivity(iweb);
bottomMenu();


        handler = new Handler();
        handler.postDelayed(runnable, 1000);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            /* my set of codes for repeated work */
            if(webView !=null)
            {
                if(urltext !=null)
                {
                    if(!urltext.getText().toString().equals(webView.getUrl()))
                    {
                        urltext.setText(webView.getUrl());
                    }

                }
            }
            handler.postDelayed(this, 1000); // reschedule the handler
        }
    };// new handler

    public  void bottomMenu(){
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

    }

    private class channelidget extends AsyncTask<Void,String,String> {

        String username="";

        public channelidget(String username) {
            this.username = username;
        }

        @Override
        protected String doInBackground(Void... params) {

            String url ="https://www.googleapis.com/youtube/v3/channels?key=AIzaSyDcotn0895Qc0VPyLMuqcTz239sCtqKL6E&forUsername="+username+"&part=id";
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
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
        protected void onPostExecute(String response)
        {
            super.onPostExecute(response);
            if(response!=null)
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonObject2= new JSONObject(jsonObject.getJSONArray("items").getString(0));
                    String channel_id=  jsonObject2.get("id").toString();
                    //addtofirebase
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("channel_id");

                    myRef.setValue(channel_id).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            finish();
                        }
                    });
                     Toast.makeText(WebActivity.this, "Updated", Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}



