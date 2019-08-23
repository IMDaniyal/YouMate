package com.example.youmate.adapter;

import android.annotation.SuppressLint;
import android.os.Bundle;
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
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.downloader.PRDownloader;
import com.downloader.Status;
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
         downloadingadapter=  new downloadingadapter( ((myApplication) getActivity().getApplication()).downloadingdata );
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

    public downloadingadapter(List<videomodel> data)
    {
        this.data = data;
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
        holder.resume.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Status.RUNNING == PRDownloader.getStatus(data.get(position).id))
                {
                    PRDownloader.pause(data.get(position).id);
                    holder.resume.setText("Resume");
                }
                else
                {
                    PRDownloader.resume(data.get(position).id);
                    holder.resume.setText("Pause");
                }
            }
        });
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
        public myviewholder(@NonNull View itemView)
        {
            super(itemView);
            name= itemView.findViewById(R.id.filename);
            date = itemView.findViewById(R.id.date);
            resume=itemView.findViewById(R.id.pausebtn);
            progress=itemView.findViewById(R.id.downloadingprogress);
        }
    }
}

}




