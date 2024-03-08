package com.khokan_gorain_nsubusservices_app.AdapterClass;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.khokan_gorain_nsubusservices_app.ConstantData.Constant;
import com.khokan_gorain_nsubusservices_app.ModelClass.SearchingBusList;
import com.khokan_gorain_nsubusservices_app.R;

import java.util.ArrayList;

public class AllBusListAd extends RecyclerView.Adapter<AllBusListAd.ViewHolder> {
    private ArrayList<SearchingBusList> busList;
    private Context context;

    public AllBusListAd(ArrayList<SearchingBusList> busList, Context context) {
        this.busList = busList;
        this.context = context;
    }

    @NonNull
    @Override
    public AllBusListAd.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_bus_list_lyt,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllBusListAd.ViewHolder holder, int position) {
        SearchingBusList searchingBusList = busList.get(position);
        holder.areaName.setText(searchingBusList.getFrom_area()+" To "+searchingBusList.getTo_area());
        holder.busNumber.setText(String.valueOf(searchingBusList.getBus_number()));
        Glide.with(context).load(Constant.DRIVER_IMG+searchingBusList.getDv_profile_img()).into(holder.dvImage);
        int busStatus = searchingBusList.getStatus();
        if(busStatus == 1){
            holder.busStatus.setText("Running...");
            holder.busStatus.setTextColor(Color.GREEN);
        } else {
            holder.busStatus.setText("Stop Bus");
            holder.busStatus.setTextColor(Color.RED);
        }

    }

    @Override
    public int getItemCount() {
        return busList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView dvImage;
        TextView areaName,busStatus,busStop,busNumber;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dvImage = itemView.findViewById(R.id.dvProfile);
            areaName = itemView.findViewById(R.id.busLocation);
            busStatus = itemView.findViewById(R.id.busStatus);
            busNumber = itemView.findViewById(R.id.busNumber);
        }
    }
}
