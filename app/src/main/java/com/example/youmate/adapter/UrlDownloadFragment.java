package com.example.youmate.adapter;

import android.app.DownloadManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentManager;
import com.example.youmate.PrefManager;
import com.example.youmate.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Adi on 25-03-2017.
 */
public class UrlDownloadFragment extends Fragment {
    ImageView imageButton;
    EditText editText;
    HomeFragment homeFragment;
    private PrefManager pref;
    private static final String ARG_POSITION = "position";
    public static UrlDownloadFragment newInstance(int position) {
        UrlDownloadFragment f = new UrlDownloadFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);

        return f;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {

        View rootView = inflater.inflate(R.layout.urldownload, container, false);
        homeFragment=new HomeFragment();
        imageButton= rootView.findViewById(R.id.download);
        editText= rootView.findViewById(R.id.editText);
        pref = new PrefManager(getContext());
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().length()==0)
                {
                    Toast.makeText(getActivity().getApplicationContext(),"Please Paste or Enter the Url", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    downloadVideo(editText.getText().toString());
                }
            }
        });
        return rootView;
    }

    private void downloadVideo(String pathvideo) {
        /*if (!(pathvideo.contains("fbcdn")))
        {
            Toast.makeText(getContext(), "Please Enter Only Video Url", Toast.LENGTH_LONG).show();
        }*/
        if(pathvideo.contains("story"))
        {
            //homeFragment=new HomeFragment();
            homeFragment.getUrlfromUrlDownload(pathvideo);
        }
        else
        {
                File directory = new File(Environment.getExternalStorageDirectory() + File.separator + "FacebookVideos");
                directory.mkdirs();
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(pathvideo));
                request.allowScanningByMediaScanner();
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                File root = new File(Environment.getExternalStorageDirectory() + File.separator + "Facebook Videos");
                int Number = pref.getFileName();
                Uri path = Uri.withAppendedPath(Uri.fromFile(root), "Video-" + Number + ".mp4");
                request.setDestinationUri(path);
                DownloadManager dm = (DownloadManager) getActivity().getSystemService(getActivity().DOWNLOAD_SERVICE);
                ArrayList<String> urldownloadFragmentList = (new HomeFragment()).getList();
                if(urldownloadFragmentList.contains(pathvideo))
                {
                    Toast.makeText(getActivity().getApplicationContext(),"The Video is Already Downloading", Toast.LENGTH_LONG).show();
                }
                else
                {
                    urldownloadFragmentList.add(pathvideo);
                    dm.enqueue(request);
                    Toast.makeText(getActivity().getApplicationContext(),"Downloading Video-"+Number+".mp4", Toast.LENGTH_LONG).show();
                    Number++;
                    pref.setFileName(Number);
                }
        }
    }
}