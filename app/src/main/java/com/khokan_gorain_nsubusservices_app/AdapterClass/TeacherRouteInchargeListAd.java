package com.khokan_gorain_nsubusservices_app.AdapterClass;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.khokan_gorain_nsubusservices_app.ConstantData.Constant;
import com.khokan_gorain_nsubusservices_app.ModelClass.TeacherRouthInchargeList;
import com.khokan_gorain_nsubusservices_app.R;

import java.util.ArrayList;


public class TeacherRouteInchargeListAd extends RecyclerView.Adapter<TeacherRouteInchargeListAd.ViewHolder> {
    ArrayList<TeacherRouthInchargeList> list;
    Context context;

    public TeacherRouteInchargeListAd(ArrayList<TeacherRouthInchargeList> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public TeacherRouteInchargeListAd.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts_item_lyt,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherRouteInchargeListAd.ViewHolder holder, int position) {

        TeacherRouthInchargeList teacherRouthInchargeList = list.get(position);
        holder.teacherName.setText(teacherRouthInchargeList.getTeacherName());
        holder.areaName.setText(teacherRouthInchargeList.getAreaName());
       // Glide.with(context).load(teacherRouthInchargeList.getProfileImg()).into(holder.teacherProfileImg);

        holder.callNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withContext(view.getContext())
                        .withPermission(Manifest.permission.CALL_PHONE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                                Intent i = new Intent(Intent.ACTION_CALL);
                                i.setData(Uri.parse("tel:"+teacherRouthInchargeList.getPhoneNumber()));
                                context.startActivity(i);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", view.getContext().getPackageName(), null);
                                intent.setData(uri);
                                context.startActivity(intent);

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView teacherName,areaName,teacherPhoneNumber;
        ImageView teacherProfileImg;
        ImageView callNowBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            teacherName = itemView.findViewById(R.id.teacherName);
            areaName = itemView.findViewById(R.id.areaName);
            callNowBtn = itemView.findViewById(R.id.callNowBtn);
            teacherProfileImg = itemView.findViewById(R.id.teacherProfile);
        }
    }
}
