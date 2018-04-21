package com.bloodbanksystem.ferozkhan.bloodbanksystem;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class RecyclerViewCustomAdapter extends RecyclerView.Adapter<RecyclerViewCustomAdapter.CustomViewHolder>
{
    private ArrayList data;
    public RecyclerViewCustomAdapter()
    {

    }
    public RecyclerViewCustomAdapter(ArrayList data)
    {
        this.data = data;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recyclerview_data,parent,false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        String name = (String) data.get(position);
        holder.name.setText(name);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView profilePic;
        private TextView name;
        public CustomViewHolder(View itemView) {
            super(itemView);
            profilePic = itemView.findViewById(R.id.rprofilepic);
            name = itemView.findViewById(R.id.rname);
        }
    }
}
