package com.example.youmate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.transition.Visibility;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.youmate.TabSwitcher.ChromeTabs;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class WebActivity extends AppCompatActivity {


    WebView webView;
    TextView ed2;
    ImageView imageView;
    ProgressBar progressBar;
    SharedPreferences settings;
    FirebaseAuth firebaseAuth;
    String userId;
    BottomNavigationView bottomNavigationView;
    MyHelper helper;
    FloatingActionButton donebutton;



    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ed2=findViewById(R.id.ed2);
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        userId = settings.getString("USER_ID", "");
        progressBar = findViewById(R.id.progressBar);
        webView = findViewById(R.id.web);
        donebutton = findViewById(R.id.floatingtick);
        imageView=findViewById(R.id.imgbook);
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
                Toast.makeText(WebActivity.this, "Page Started Loading", Toast.LENGTH_SHORT).show();

            }


            @Override
            public void onPageFinished( WebView view, String url ) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                Toast.makeText(WebActivity.this, "Page Loaded", Toast.LENGTH_SHORT).show();

            }
        });

        if(bundle.getInt("setchannel")==1)
        {
            webView.loadUrl(iip);
            ed2.setText("youtube");
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
                     SharedPreferences pref = getApplicationContext().getSharedPreferences("channel_idpref", MODE_PRIVATE);
                     Editor editor = pref.edit();
                     editor.putString("channelid", channel_id);
                     editor.apply();
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
            webView.loadUrl("https://www.google.co.id/search?q="+iip);
            ed2.setText("https://www.google.co.id/search?q="+iip);
        }



        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                SharedPreferences sharedPreferences=getSharedPreferences("MyPref",MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("Url",ed2.getText().toString());
                editor.commit();
                Toast.makeText(WebActivity.this, "BookMarked", Toast.LENGTH_SHORT).show();

            }
        });

//        String url=ed2.getText().toString();
//        Intent iweb=new Intent(WebActivity.this,BookMarkPage.class);
//        iweb.putExtra("Url",url);
//        startActivity(iweb);
bottomMenu();

    }

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


                    SharedPreferences pref = getApplicationContext().getSharedPreferences("channel_idpref", MODE_PRIVATE);
                    Editor editor = pref.edit();
                    editor.putString("channelid", channel_id);
                    editor.apply();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}



