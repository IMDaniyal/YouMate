package com.example.youmate.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileObserver;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.downloader.Status;
import com.example.youmate.DBHelper;
import com.example.youmate.MainTry;
import com.example.youmate.MainTry.deleteddb;
import com.example.youmate.R;
import com.example.youmate.myApplication;
import java.util.List;
import java.util.zip.Inflater;
import org.w3c.dom.Text;


/**
 * Created by Daniel on 12/23/2014.
 */
public class FileViewerFragment extends Fragment {
    private static final String ARG_POSITION = "position";
    private static final String LOG_TAG = "FileViewerFragment";
    RecyclerView downloadingrc;
    downloadingadapter downloadingadapter;

    private int position;
    private FileViewerAdapter mFileViewerAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    public static FileViewerFragment newInstance(int position) {
        FileViewerFragment f = new FileViewerFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        observer.startWatching();
    }

    Handler  handler;
    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.view_files, container, false);
        final RecyclerView mRecyclerView = v.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        downloadingrc= v.findViewById(R.id.downloadingrc);
        downloadingrc.setLayoutManager(new LinearLayoutManager(getContext()));
        downloadingadapter=  new downloadingadapter( ((myApplication) getActivity().getApplication()).downloadingdata,(myApplication) getActivity().getApplication());
        downloadingrc.setAdapter(downloadingadapter);
        handler = new Handler();
        handler.postDelayed(runnable, 1000);


        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);

        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mFileViewerAdapter = new FileViewerAdapter(getActivity(), llm);
        mRecyclerView.setAdapter(mFileViewerAdapter);

        return v;
    }
    private Runnable runnable = new Runnable()
    {

        @Override
        public void run()
        {
            if(downloadingadapter!=null)
            {
                downloadingadapter.notifyDataSetChanged();
            }
            handler.postDelayed(this, 1000);
        }

    };

    FileObserver observer =
            new FileObserver(android.os.Environment.getExternalStorageDirectory().toString()
                    + "/VideoDownloader") {
                // set up a file observer to watch this directory on sd card
                @Override
                public void onEvent(int event, String file) {
                    if(event == FileObserver.DELETE){
                        // user deletes a recording file out of the app

                        String filePath = android.os.Environment.getExternalStorageDirectory().toString()
                                + "/VideoDownloader" + file + "]";

                        Log.d(LOG_TAG, "File deleted ["
                                + android.os.Environment.getExternalStorageDirectory().toString()
                                + "/VideoDownloader" + file + "]");

                        // remove file from database and recyclerview
                        mFileViewerAdapter.removeOutOfApp(filePath);
                    }
                }
            };


public class downloadingadapter extends RecyclerView.Adapter<downloadingadapter.myviewholder>
{

    List<videomodel> data;
    Context c;

    myApplication app;

    public downloadingadapter(List<videomodel> data,myApplication obb)
    {
        this.data = data;
        app=obb;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
       View v= LayoutInflater.from(getContext()).inflate(R.layout.downloadingrow,parent,false);
        return new myviewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final myviewholder holder, final int position)
    {
        holder.name.setText(data.get(position).name);
        holder.date.setText(data.get(position).time);
        holder.progress.setProgress(data.get(position).progress);

        holder.cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v)
            {

                PRDownloader.cancel(data.get(position).id);
                new deleteddb(c,data.get(position)).execute();
                data.remove(position);
            }
        });

        holder.resume.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (Status.RUNNING == PRDownloader.getStatus(data.get(position).id))
                {
                    PRDownloader.pause(data.get(position).id);
                    holder.resume.setText("Resume");
                }
                else if (Status.PAUSED == PRDownloader.getStatus(data.get(position).id))
                {
                    PRDownloader.resume(data.get(position).id);
                    holder.resume.setText("Pause");
                }
                else
                {
                    String path = Environment.getExternalStorageDirectory().toString() +"/VideoDownloader";

                    holder.resume.setText("Pause");

                    int downloadId = PRDownloader.download(data.get(position).getUrl(), path, data.get(position).name)
                        .build()

                        .setOnStartOrResumeListener(new OnStartOrResumeListener()
                        {
                            @Override
                            public void onStartOrResume()
                            {

                            }
                        })
                        .setOnProgressListener(new OnProgressListener()
                        {

                            @Override
                            public void onProgress(Progress progress)
                            {
                                if(progress !=null)
                                {
                                    if(data.size()>0)
                                    {
                                        long a = progress.currentBytes;
                                        long b = progress.totalBytes;
                                        int pro =  (int)(((double)a/b)*100);
                                        holder.progress.setProgress(pro);
                                        data.get(position).setProgress(pro);
                                    }

                                }

                            }
                        })
                        .start(new OnDownloadListener()
                        {
                            @Override
                            public void onDownloadComplete()
                            {


                                try{
                                    app.mDatabase.addRecording(data.get(position).getName(),data.get(position).getPath()+"/"+data.get(position).getName(),0,null);
                                    app.mDatabase.close();
                                    new deleteddb(c,data.get(position)).execute();
                                    data.remove(position);
                                }
                                catch (Exception e)
                                {

                                }


                            }

                            @Override
                            public void onError(Error error)
                            {

                            }
                        });

                }
            }
        });

        if (Status.RUNNING != PRDownloader.getStatus(data.get(position).id))
        {
            holder.resume.setText("Resume");
        }

    }

    public class deleteddb extends AsyncTask
    {
        Context c;
        videomodel obj;
        MyDatabase db;

        public deleteddb(Context c, videomodel obj) {
            this.c = c;
            this.obj = obj;
            db= MyDatabase.getAppDatabase(c);
        }

        @Override
        protected Object doInBackground(Object[] objects)
        {

            db.videoDao().delete(obj.getId());
            return null;
        }
    }

    @Override
    public int getItemCount()
    {
        if(data!=null)
        return data.size();
        return 0;
    }

    public class myviewholder extends RecyclerView.ViewHolder
    {

        TextView name;
        TextView date;
        Button resume;
        ProgressBar progress;
        Button cancel;
        public myviewholder(@NonNull View itemView)
        {
            super(itemView);
            name= itemView.findViewById(R.id.filename);
            date = itemView.findViewById(R.id.date);
            resume=itemView.findViewById(R.id.pausebtn);
            progress=itemView.findViewById(R.id.downloadingprogress);
            cancel = itemView.findViewById(R.id.cancelbtn);
        }
    }
}


}




