//package com.example.youmate;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.Lis<C>> {
//
//    Lis<C> list;
//    Context context;
//    int resource;
//
//    public CustomAdapter( Lis<C> list, Context context, int resource ) {
//        this.list = list;
//        this.context = context;
//        this.resource = resource;
//    }
//
//    @NonNull
//    @Override
//    public Lis<C> onCreateViewHolder( @NonNull ViewGroup parent, int viewType ) {
//        LayoutInflater layoutInflater=LayoutInflater.from(this.context);
//        View view=layoutInflater.inflate(resource,null,false);
//
//        return new Lis<C>(view);
//    }
//
//
//    @Override
//    public void onBindViewHolder( @NonNull Lis<C> holder, int position ) {
//
//
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return list.size() ;
//    }
//
//    public class Lis<C> extends RecyclerView.ViewHolder {
//        public Lis( @NonNull View itemView ) {
//            super(itemView);
//        }
//    }
//}
