package com.bloodbanksystem.ferozkhan.bloodbanksystem;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewCustomAdapter extends RecyclerView.Adapter<RecyclerViewCustomAdapter.CustomViewHolder>
{
    private Context context;
    private List<Donors> data;
    private String uuid;
    public RecyclerViewCustomAdapter()
    {

    }
    public RecyclerViewCustomAdapter(Context context, List<Donors> data)
    {
        this.context = context;
        this.data = data;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recyclerview_data,parent,false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecyclerView recyclerView = (RecyclerView) view.getParent();
                CustomViewHolder currentViewHolder = (CustomViewHolder) recyclerView.getChildViewHolder(view);
                int currentPosition = currentViewHolder.getAdapterPosition();
                Intent intent = new Intent(context,Request_Blood.class);
                intent.putExtra("UUID",data.get(currentPosition).getUuid());
                context.startActivity(intent);
            }
        });
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        String name = (String) data.get(position).getName();
        String email = (String) data.get(position).getEmail();
        String image = data.get(position).getImage();
        uuid = data.get(position).getUuid();
        holder.name.setText(name);
        holder.email.setText(email);
        if(image != null)
        {
            Glide.with(context)
                    .load(image)
                    .into(holder.profilePic);
        }
        /*holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), uuid, Toast.LENGTH_LONG).show();
            }
        });*/
    }

    @Override
    public int getItemCount() {
        if(data == null)
        {
            return 5;
        }
        else
        {
            return data.size();
        }
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder
    {
        View mView;
        private CircleImageView profilePic;
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
