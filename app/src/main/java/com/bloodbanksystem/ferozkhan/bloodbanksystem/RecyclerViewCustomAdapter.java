package com.bloodbanksystem.ferozkhan.bloodbanksystem;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class RecyclerViewCustomAdapter extends RecyclerView.Adapter<RecyclerViewCustomAdapter.CustomViewHolder>
{
    private ArrayList<Donors> data;
    public RecyclerViewCustomAdapter()
    {

    }
    public RecyclerViewCustomAdapter(ArrayList<Donors> data)
    {
        this.data = data;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recyclerview_data,parent,false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecyclerView recyclerView = (RecyclerView) view.getParent();
                CustomViewHolder currentViewHolder = (CustomViewHolder) recyclerView.getChildViewHolder(view);
                int currentPosition = currentViewHolder.getAdapterPosition();
                Toast.makeText(view.getContext(), currentViewHolder.name.getText(), Toast.LENGTH_LONG).show();
            }
        });
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

        String name = (String) data.get(position).getName();
        String email = (String) data.get(position).getEmail();
        holder.name.setText(name);
        holder.email.setText(email);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView profilePic;
        private TextView name;
        private TextView email;
        public CustomViewHolder(View itemView) {
            super(itemView);
            profilePic = itemView.findViewById(R.id.rprofilepic);
            name = itemView.findViewById(R.id.rname);
            email = itemView.findViewById(R.id.remail);
        }
    }
}
