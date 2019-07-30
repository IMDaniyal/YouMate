package com.example.youmate;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.example.youmate.TabSwitcher.ChromeTabs;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;


public class MainTry extends AppCompatActivity {
    private static String youtubeLink;
    BottomNavigationView bottomNavigationView;
    private LinearLayout mainLayout;
    private ProgressBar mainProgressBar;
    EditText texturl;
    Button DownloadBtn, GalleryBtn;
    String fileN = null ;
    Dialog main_dialog, downloadDialog;
    boolean result;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 123;
    SharedPreferences settings;
    FirebaseAuth firebaseAuth;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_try);
        Intent old = getIntent();
        final Bundle data = old.getExtras();
        final int chromecheck= old.getIntExtra("chorme",-1);

        bottomNavigationView=findViewById(R.id.nav1);
        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected( @NonNull MenuItem menuItem ) {
                switch (menuItem.getItemId()){
                    case R.id.item1:
                        Intent i = new Intent(getApplicationContext(),Main2Activity.class);
                      if(data !=null)
                      {
                        i.putExtras(data);
                      }
                        startActivity(i);
                        finish();
                        break;
                    case R.id.item2:
                        Toast.makeText(getApplicationContext(),"You already in Youtube Activity",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.item3:
                        if(chromecheck==1)
                        {
                            finish();
                        }
                        else
                        {
                            startActivity(new Intent(getApplicationContext(), ChromeTabs.class));
                            finish();
                        }
                        break;
                    case R.id.item4:
                         i = new Intent(getApplicationContext(),Download.class);
                      if(data !=null)
                      {
                        i.putExtras(data);
                      }
                        startActivity(i);
                        break;
                    case R.id.item5:
                         i = new Intent(getApplicationContext(),AccountActivity.class);
                      if(data !=null)
                      {
                        i.putExtras(data);
                      }
                        startActivity(i);
                        finish();
                        break;
                }

            }
        });

        bottomNavigationView.setSelectedItemId(R.id.item2);
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        userId = settings.getString("USER_ID", "");
        result = checkPermission();
        if(result){
            checkFolder();
        }
        if (!isConnectingToInternet(this)) {
            Toast.makeText(this, "Please Connect to Internet", Toast.LENGTH_LONG).show();
        }
        texturl = findViewById(R.id.texturl);
        DownloadBtn = findViewById(R.id.DownloadBtn);
        GalleryBtn = findViewById(R.id.MyDownloadBtn);
        mainLayout = findViewById(R.id.main_layout);
        mainProgressBar = findViewById(R.id.prgrBar);

        GalleryBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        DownloadBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String ytLink = texturl.getText().toString();
// Check how it was started and if we can get the youtube link
                Log.d("ghghghg", "" + ytLink);

                if (ytLink != null && (ytLink.contains("://youtu.be/") || ytLink.contains("youtube.com/watch?v="))) {
                    youtubeLink = ytLink;
                    // We have a valid link
                    getYoutubeDownloadUrl(youtubeLink);
                    mainLayout.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(MainTry.this, "err", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });


    }

    private void getYoutubeDownloadUrl(String youtubeLink) {
        new YouTubeExtractor(this) {

            @Override
            public void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta vMeta) {
                mainProgressBar.setVisibility(View.GONE);

                if (ytFiles == null) {
                    // Something went wrong we got no urls. Always check this.
                    finish();
                    return;
                }
                // Iterate over itags
                for (int i = 0, itag; i < ytFiles.size(); i++) {
                    itag = ytFiles.keyAt(i);
                    // ytFile represents one file with its url and meta data
                    YtFile ytFile = ytFiles.get(itag);

                    // Just add videos in a decent format => height -1 = audio
                    if (ytFile.getFormat().getHeight() == -1 || ytFile.getFormat().getHeight() >= 360) {
                        addButtonToMainLayout(vMeta.getTitle(), ytFile);
                    }
                }
            }
        }.extract(youtubeLink, true, false);
    }

    private void addButtonToMainLayout(final String videoTitle, final YtFile ytfile) {
        // Display some buttons and let the user choose the format
        String btnText = (ytfile.getFormat().getHeight() == -1) ? "Audio " +
                ytfile.getFormat().getAudioBitrate() + " kbit/s" :
                ytfile.getFormat().getHeight() + "p";
        btnText += (ytfile.getFormat().isDashContainer()) ? " dash" : "";
        Button btn = new Button(this);
        btn.setText(btnText);
        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String filename;
                if (videoTitle.length() > 55) {
                    filename = videoTitle.substring(0, 55) + "." + ytfile.getFormat().getExt();
                } else {
                    filename = videoTitle + "." + ytfile.getFormat().getExt();
                }
                filename = filename.replaceAll("[\\\\><\"|*?%:#/]", "");
                downloadFromUrl(ytfile.getUrl(), videoTitle, filename);
         //       final DownloadTask downloadTask = new DownloadTask(MainTry.this);
           //     downloadTask.execute(ytfile.getUrl());
                updataLevel();

            }
        });
        mainLayout.addView(btn);
    }


    public class DownloadTask extends AsyncTask<String, Integer, String> {

        private Context context;
        private PowerManager.WakeLock mWakeLock;

        public DownloadTask(Context context) {
            this.context = context;
        }
        private NumberProgressBar bnp;
        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                int fileLength = connection.getContentLength();

                input = connection.getInputStream();
                fileN = "VideoDownloader" + UUID.randomUUID().toString().substring(0, 10) + ".mp4";
                File filename = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                        Constants.FOLDER_NAME, fileN);
                output = new FileOutputStream(filename);

                byte[] data = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    if (fileLength > 0)
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                Log.d("Exceptionnn",""+e.getMessage());

                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
            LayoutInflater dialogLayout = LayoutInflater.from(MainTry.this);
            View DialogView = dialogLayout.inflate(R.layout.progress_dialog, null);
            downloadDialog = new Dialog(MainTry.this, R.style.CustomAlertDialog);
            downloadDialog.setContentView(DialogView);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(downloadDialog.getWindow().getAttributes());
            lp.width = (getResources().getDisplayMetrics().widthPixels);
            lp.height = (int)(getResources().getDisplayMetrics().heightPixels*0.65);
            downloadDialog.getWindow().setAttributes(lp);

            final Button cancel = DialogView.findViewById(R.id.cancel_btn);
            cancel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //stopping the Asynctask
                    cancel(true);
                    downloadDialog.dismiss();

                }
            });

             downloadDialog.setCancelable(false);
            downloadDialog.setCanceledOnTouchOutside(false);
            bnp = DialogView.findViewById(R.id.number_progress_bar);
            bnp.setProgress(0);
            bnp.setMax(100);
            downloadDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            bnp.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
            downloadDialog.dismiss();
            if (result != null)
                Toast.makeText(context, "Download error: " + result, Toast.LENGTH_LONG).show();
            else
                Toast.makeText(context, "File downloaded", Toast.LENGTH_SHORT).show();
            MediaScannerConnection.scanFile(MainTry.this,
                    new String[]{Environment.getExternalStorageDirectory().getAbsolutePath() +
                            Constants.FOLDER_NAME + fileN}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String newpath, Uri newuri) {
                            Log.i("ExternalStorage", "Scanned " + newpath + ":");
                            Log.i("ExternalStorage", "-> uri=" + newuri);
                        }
                    });

        }
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean checkPermission() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(MainTry.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainTry.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainTry.this);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Write Storage permission is necessary to Download Images and Videos!!!");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainTry.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions(MainTry.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
    public void checkFolder() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/VideoDownloader/";
        File dir = new File(path);
        boolean isDirectoryCreated = dir.exists();
        if (!isDirectoryCreated) {
            isDirectoryCreated = dir.mkdir();
        }
        if (isDirectoryCreated) {
            // do something\
            Log.d("Folder", "Already Created");
        }
    }
    public static boolean isConnectingToInternet(Context context) {

        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
    public void updataLevel () {
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            final String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            final DocumentReference noteRef = db.collection("UserInfo").document(userEmail);
            noteRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Double valueLevel = documentSnapshot.getDouble("level");
                    Double valuePoint = documentSnapshot.getDouble("point");
                    try {
                        //checking for level
                        if (valuePoint == 50 || valuePoint == 100 || valuePoint == 150 || valuePoint == 200) {

                            noteRef.update("email", userEmail,
                                    "level", valueLevel + 1, "point", valuePoint + 5).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(MainTry.this, "Congratulations for next level", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainTry.this, "Points not updated", Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else {
                            noteRef.update("email", userEmail,
                                    "point", valuePoint + 5).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(MainTry.this, "Points updated", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainTry.this, "Points not updated", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    } catch (Exception e) {
                        Toast.makeText(MainTry.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else{
            Toast.makeText(MainTry.this, "Login to get points.", Toast.LENGTH_SHORT).show();
        }
    }

   private void downloadFromUrl(String youtubeDlUrl, String downloadTitle, String fileName) {
        Uri uri = Uri.parse(youtubeDlUrl);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(downloadTitle);

        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDescription("Android Data download using DownloadManager.");

        request.setDestinationInExternalPublicDir("/VideoDownloader", fileName);

        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);

    }
}
