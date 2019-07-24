package com.example.youmate;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Adi on 21-01-2017.
 */
public class PrefManager {
    private static final String TAG = PrefManager.class.getSimpleName();
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    public static final int FileNameCount_VARIABLE = 0;
    public static final String CURRENT_DOWNLOAD_VAR="currentdownloadvar";
    private static final String PREF_NAME = "AwesomeWallpapers";
    private static String fileName="fileName";
    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);

    }
   /* public String getCurrentVideoDownloading() {
        return pref.getString(currentVideoDownloading, CURRENT_DOWNLOAD_VAR);
    }

    public  void setCurrentVideoDownloading(String currentVideoDownloadings) {
        editor = pref.edit();
        editor.putString(currentVideoDownloading, currentVideoDownloadings);
        editor.commit();
    }
   public  void setCurrentVideoDownloading(String downloadingString,String isFromDownloadReciever)
   {
       if(isFromDownloadReciever.equals("fromDownload"))
       {
           ArrayList<String> arrayListremove = new ArrayList<String>();
           if (pref.contains(CURRENT_DOWNLOAD_VAR)) {
               String json = pref.getString(CURRENT_DOWNLOAD_VAR, null);
               Log.d("Settingsfrombroadcast", json);
               Gson gson = new Gson();
               String[] albumArry = gson.fromJson(json, String[].class);
               Log.d("Settingsfrombroadcast", albumArry.toString());
               arrayListremove.addAll(Arrays.asList(albumArry));
               arrayListremove.remove(downloadingString);
               editor = pref.edit();
               editor.putString(CURRENT_DOWNLOAD_VAR, gson.toJson(arrayListremove));
               editor.commit();
           }
       }
       else
       {
           ArrayList<String> arrayListadd = new ArrayList<String>();
           if (pref.contains(CURRENT_DOWNLOAD_VAR)) {
               String json = pref.getString(CURRENT_DOWNLOAD_VAR, null);
               Log.d("Settingsfrombroadcast", json);
               Gson gson = new Gson();
               String[] albumArry = gson.fromJson(json, String[].class);
               Log.d("Settingsfrombroadcast", albumArry.toString());
               arrayListadd.addAll(Arrays.asList(albumArry));
               arrayListadd.add(downloadingString);
               editor = pref.edit();
               editor.putString(CURRENT_DOWNLOAD_VAR, gson.toJson(arrayListadd));
               editor.commit();
           }
       }
   }*/
    public int getFileName() {
        return pref.getInt(fileName, FileNameCount_VARIABLE);
    }

    public  void setFileName(int fileNames) {
        editor = pref.edit();
        editor.putInt(fileName, fileNames);
        editor.commit();
    }

}
