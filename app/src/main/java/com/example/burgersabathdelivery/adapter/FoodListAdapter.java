package com.example.burgersabathdelivery.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.example.burgersabathdelivery.Activity.DetailActivity;
import com.example.burgersabathdelivery.Domain.FoodDomain;
import com.example.burgersabathdelivery.R;

import java.util.ArrayList;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.ViewHolder> {

    ArrayList<FoodDomain> itens;
    Context context;

    public FoodListAdapter(ArrayList<FoodDomain> itens) {
        this.itens = itens;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate= LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_food_list,parent,false);
        context = parent.getContext();
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtTitle.setText(itens.get(position).getTitle());
        holder.txtdesc.setText(itens.get(position).getDescription());
        holder.txtprice.setText("R$" + itens.get(position).getPrice());
        int drawableResourceId = holder.itemView.getResources().getIdentifier(itens.get(position).getPicUrl(),"drawable", holder.itemView.getContext().getPackageName());

        Glide.with(holder.itemView.getContext()).load(drawableResourceId).transform(new GranularRoundedCorners(30,30,0,0))
                .into(holder.pic);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), DetailActivity.class);
                intent.putExtra("object", itens.get(position));
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itens.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
TextView txtTitle, txtdesc, txtprice;
ImageView pic;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtdesc = itemView.findViewById(R.id.txtdesc);
            txtprice = itemView.findViewById(R.id.txtPrice);
            pic = itemView.findViewById(R.id.pic);
        }
    }
}
