package com.dongseo.vegechecker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MaterialRecyclerAdapter extends RecyclerView.Adapter<MaterialRecyclerAdapter.ViewHolder> {

    private ArrayList<MaterialItem> materialItems;

    @NonNull
    @Override
    public MaterialRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_materialist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MaterialRecyclerAdapter.ViewHolder holder, int position) {
        holder.onBind(materialItems.get(position));
    }

    public void setMaterialItems(ArrayList<MaterialItem> materialItems){
        this.materialItems = materialItems;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return materialItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView isMeat;
        TextView material;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            isMeat = (ImageView) itemView.findViewById(R.id.isMeat);
            material = (TextView) itemView.findViewById(R.id.materialItem);
        }

        void onBind(MaterialItem item){
            isMeat.setImageResource(R.drawable.alert);
            material.setText(item.getMaterial());
            if (item.isMeat) isMeat.setVisibility(View.VISIBLE);
            else isMeat.setVisibility(View.GONE);
        }
    }
}