package com.example.burgersabathdelivery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.example.burgersabathdelivery.Domain.FoodDomain;
import com.example.burgersabathdelivery.R;
import com.example.burgersabathdelivery.model.ChangerNumberItensListener;
import com.example.burgersabathdelivery.model.ManagementCart;

import java.util.ArrayList;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.ViewHolder> {

    ArrayList<FoodDomain> listFoodSelected;
    private ManagementCart managementcart;
    ChangerNumberItensListener changerNumberItensListener;

    public CartListAdapter(ArrayList<FoodDomain> listFoodSelected, Context context, ChangerNumberItensListener changerNumberItensListener) {
        this.listFoodSelected = listFoodSelected;
        managementcart = new ManagementCart(context);
        this.changerNumberItensListener = changerNumberItensListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_cart, parent, false);

        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(listFoodSelected.get(position).getTitle());
        holder.feeEachItem.setText("R$" + listFoodSelected.get(position).getPrice());
        holder.totalEachItem.setText("R$" + Math.round((listFoodSelected.get(position).getNumCart() * listFoodSelected.get(position).getPrice())));
        holder.num.setText(String.valueOf(listFoodSelected.get(position).getNumCart()));

        int drawableResourceID=holder.itemView.getContext().getResources().getIdentifier(listFoodSelected.get(position).getPicUrl(),"drawable",holder.itemView.getContext().getPackageName());

        Glide.with(holder.itemView.getContext())
                .load(drawableResourceID)
                .transform(new GranularRoundedCorners(30,30,30,30))
                .into(holder.pic);

        holder.btnPlusCart.setOnClickListener(v -> managementcart.plusNumberFood(listFoodSelected, position, () -> {
            notifyDataSetChanged();
            changerNumberItensListener.changed();
        }));

        holder.btnMinusItem.setOnClickListener(v -> managementcart.minusNumberFood(listFoodSelected, position, () -> {
            notifyDataSetChanged();
            changerNumberItensListener.changed();
        }));
    }

    @Override
    public int getItemCount() {
        return listFoodSelected.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title, feeEachItem, btnPlusCart, btnMinusItem;
        ImageView pic;
        TextView totalEachItem, num;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleTxt);
            pic = itemView.findViewById(R.id.pic);
            feeEachItem = itemView.findViewById(R.id.feeEachItem);
            totalEachItem = itemView.findViewById(R.id.totalEachItem);
            btnPlusCart = itemView.findViewById(R.id.btnPlusCart);
            btnMinusItem = itemView.findViewById(R.id.btnMinusCart);
            num = itemView.findViewById(R.id.numItemTxt);
        }
    }
}
