package com.example.youmate;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MySaves extends AppCompatActivity {
    Button btn_v;
    RecyclerView rv;
    int clickedbtn=0;
    List<ModelMySaves> list;
    String name,slctdUrl,typeofFile;
    private static final String FILEPATH = "filepath";
    AdapterMySaves adapterMySaves;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_saves);
        btn_v=findViewById(R.id.btn_v);
          list=new ArrayList<>();

        rv=findViewById(R.id.rv);
        btn_v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                   /* Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()
                            + "/VideoDownloader/");
                    intent.setDataAndType(uri, "video/mp4");
                    startActivity(Intent.createChooser(intent, "Open folder"));*/



                clickedbtn = 1;
                String path = (Environment.getExternalStorageDirectory().getAbsolutePath() + "/VideoDownloader");
               // String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/VideoDownloader";
                //String path = Environment.getExternalStorageDirectory() + File.separator + "VideoDownloader/";

                Log.d("Files", "Path: " + path);
                File directory = new File(path);
                File[] files = directory.listFiles();
                Log.d("Files", "Size: "+ files.length);
                for (int i = 0; i < files.length; i++)
                {
                    Log.d("Files", "FileName:" + files[i].getName());
                    String name1 = files[i].getName();

                    list.add(new ModelMySaves(files[i].getName()));
                    adapterMySaves(list, name1);
                }
//                getNoFiles(movieDir);
            }
        });
  }



    private void getNoFiles(String path) {

        Log.d("FilesPath", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        Log.d("FilesPath1", "Path: " + files.length+files.toString());

        if(files.length!=0) {
            Log.d("Files", "Size: " + files.length);
            for (int i = 0; i < files.length; i++) {
                Log.d("Files", "FileName:" + files[i].getName());
                String name1 = files[i].getName();

                list.add(new ModelMySaves(files[i].getName()));
                adapterMySaves(list, name1);
            }
        }
        else{
            Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
        }
    }

    private void adapterMySaves(List<ModelMySaves> list, final String name1) {
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapterMySaves=new AdapterMySaves(this, list, new AdapterMySaves.onItemClickListener() {
            @Override
            public void onItemclick(int position) {

                Toast.makeText(MySaves.this, position+" "+ adapterMySaves.listSaves.get(position).getVideoName(), Toast.LENGTH_SHORT).show();
                String moviename=adapterMySaves.listSaves.get(position).getVideoName();
                Log.d("moviename",""+moviename);
                   /* slctdUrl =Environment.getExternalStorageDirectory().getPath()
                            + "/MOVIES/"+moviename;
                    Intent intent = new Intent(MySaves.this, PreviewActivity.class);
                    intent.putExtra(FILEPATH, slctdUrl);
                    startActivity(intent);*/



            }
        });
        rv.setAdapter(adapterMySaves);
    }
}

