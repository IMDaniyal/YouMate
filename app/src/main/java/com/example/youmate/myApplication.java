package com.example.youmate;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.example.youmate.adapter.MyDatabase;
import com.example.youmate.adapter.videomodel;
import java.util.ArrayList;
import java.util.List;

public class myApplication extends Application
{

 public List<videomodel> downloadingdata;
  public  DBHelper mDatabase;

  @Override
  public void onCreate()
  {
    super.onCreate();
    PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
        .setDatabaseEnabled(true)
        .setReadTimeout(30_000)
        .setConnectTimeout(30_000)
        .build();
    PRDownloader.initialize(getApplicationContext(), config);
    downloadingdata = new ArrayList();
    mDatabase= new DBHelper(getApplicationContext());
   new getall(getApplicationContext()).execute();
  }

  public class getall extends AsyncTask
  {
    Context c;
    MyDatabase db;
    List<videomodel> data;


    public getall(Context c) {
      this.c = c;
      db= MyDatabase.getAppDatabase(c);

    }

    @Override
    protected Object doInBackground(Object[] objects)
    {

     data = db.videoDao().getall();
      return null;
    }

    @Override
    protected void onPostExecute(Object o)
    {
      super.onPostExecute(o);
      if(data !=null && data.size()>0)
      {
        downloadingdata = data;
      }
    }
  }
}
