package com.example.youmate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterMySaves extends RecyclerView.Adapter<AdapterMySaves.MyHolder> {
    public interface onItemClickListener{
        void onItemclick(int position);
    }
    Context context;
    public List<ModelMySaves> listSaves;
    public  onItemClickListener listener;

    public AdapterMySaves(Context context, List<ModelMySaves> listSaves, onItemClickListener listener) {
        this.context=context;
        this.listSaves = listSaves;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.mysavelayout,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.bind(listSaves.get(position),position,listener);
    }

    @Override
    public int getItemCount() {
        return listSaves.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public MyHolder(View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.videoname);
        }
        public void bind(ModelMySaves modelMySaves, final int position, final onItemClickListener listener) {
            textView.setText(modelMySaves.getVideoName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemclick(position);
                }
            });
        }
    }
}
