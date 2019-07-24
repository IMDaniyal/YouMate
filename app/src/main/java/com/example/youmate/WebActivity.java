package com.example.youmate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
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
import com.google.firebase.auth.FirebaseAuth;

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
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        ed2=findViewById(R.id.ed2);
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        userId = settings.getString("USER_ID", "");
        progressBar = findViewById(R.id.progressBar);
        webView = findViewById(R.id.web);
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

        webView.loadUrl("https://www.google.co.id/search?q="+iip);
        ed2.setText("https://www.google.co.id/search?q="+iip);
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
}



