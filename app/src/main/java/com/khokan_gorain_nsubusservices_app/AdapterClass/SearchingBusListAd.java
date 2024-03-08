package com.khokan_gorain_nsubusservices_app.AdapterClass;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.khokan_gorain_nsubusservices_app.ConstantData.Constant;
import com.khokan_gorain_nsubusservices_app.ModelClass.SearchingBusList;
import com.khokan_gorain_nsubusservices_app.R;

import java.util.ArrayList;

public class SearchingBusListAd extends RecyclerView.Adapter<SearchingBusListAd.ViewHolder> {

    ArrayList<SearchingBusList> list;
    Context context;
    private ItemClickListener itemClickListener;

    public SearchingBusListAd(ArrayList<SearchingBusList> list, Context context,ItemClickListener itemClickListener) {
        this.list = list;
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public SearchingBusListAd.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bus_list_lyt,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchingBusListAd.ViewHolder holder, int position) {
        SearchingBusList searchingBusList = list.get(position);
        holder.busLocation.setText(searchingBusList.getFrom_area()+" To "+searchingBusList.getTo_area());
        Glide.with(context).load(Constant.DRIVER_IMG+searchingBusList.getDv_profile_img()).into(holder.dvProfileImg);
        holder.stopBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Coming Version 2.0...", Toast.LENGTH_LONG).show();
            }
        });
        int busStatusCheck = searchingBusList.getStatus();
        if(busStatusCheck == 1){
            holder.busStatus.setText("Running...");
            holder.busStatus.setTextColor(Color.GREEN);
        } else {
            holder.busStatus.setText("Not Running...");
            holder.busStatus.setTextColor(Color.RED);
        }

       // Glide.with(context).load(Constant.MAIN_URL+)

            holder.itemView.setOnClickListener(view -> {
                itemClickListener.onItemClick(list.get(position));
            });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

   public interface ItemClickListener{
        void onItemClick(SearchingBusList searchingBusList);
   }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView busLocation,stopBus;
        TextView busStatus;
        ImageView dvProfileImg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dvProfileImg = itemView.findViewById(R.id.dvProfile);
            busLocation = itemView.findViewById(R.id.busLocation);
            busStatus = itemView.findViewById(R.id.busStatus);
            stopBus = itemView.findViewById(R.id.stopBus);
        }
    }
}
