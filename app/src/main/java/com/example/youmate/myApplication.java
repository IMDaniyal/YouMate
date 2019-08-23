package com.example.youmate;

import android.app.Application;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.example.youmate.adapter.videomodel;
import java.util.ArrayList;
import java.util.List;

public class myApplication extends Application
{

 public List<videomodel> downloadingdata;

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
  }
}
