package com.khokan_gorain_nsubusservices_app.Fragments;

import android.os.Bundle;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.khokan_gorain_nsubusservices_app.AdapterClass.TeacherRouteInchargeListAd;
import com.khokan_gorain_nsubusservices_app.ApiController.ApiController;
import com.khokan_gorain_nsubusservices_app.ModelClass.TeacherRouthInchargeList;
import com.khokan_gorain_nsubusservices_app.R;
import com.khokan_gorain_nsubusservices_app.databinding.FragmentContactsBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.recyclerview.widget.LinearLayoutManager.*;

public class ContactsFragment extends Fragment {

    FragmentContactsBinding binding;
    ArrayList<TeacherRouthInchargeList> list;


    public ContactsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentContactsBinding.inflate(inflater,container,false);

        getTeacherRouteInchargeList();

        return binding.getRoot();
    }

    public void getTeacherRouteInchargeList(){
      Call<List<TeacherRouthInchargeList>> call = ApiController
              .getInstance()
              .getApi()
              .getTeacherRouteInchargeList();

      call.enqueue(new Callback<List<TeacherRouthInchargeList>>() {
          @Override
          public void onResponse(Call<List<TeacherRouthInchargeList>> call, Response<List<TeacherRouthInchargeList>> response) {
              List<TeacherRouthInchargeList> data =  response.body();
              if(data.isEmpty()){
                  Toast.makeText(getContext(), "Unable to fetch data. Please try again...", Toast.LENGTH_LONG).show();
              } else {
                  binding.teacherContactsListLyt.setVisibility(View.VISIBLE);
                  binding.shimmerLyt.setVisibility(View.GONE);
                  list = new ArrayList<>();

                  for(int i=0;i<data.size();i++){
                      TeacherRouthInchargeList teacherRouthInchargeList = new TeacherRouthInchargeList
                              (data.get(i).getTeacherName(),data.get(i).getAreaName(),
                                      data.get(i).getProfileImg(),data.get(i).getPhoneNumber(),
                                      data.get(i).getStatus(),data.get(i).getId());

                      list.add(teacherRouthInchargeList);
                  }

                  TeacherRouteInchargeListAd teacherRouteInchargeListAd = new TeacherRouteInchargeListAd(list,getContext());
                  binding.teacherListRecView.setAdapter(teacherRouteInchargeListAd);
                  teacherRouteInchargeListAd.notifyDataSetChanged();

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
                    binding.teacherListRecView.setLayoutManager(linearLayoutManager);
                    binding.teacherListRecView.setHasFixedSize(true);

              }
          }

          @Override
          public void onFailure(Call<List<TeacherRouthInchargeList>> call, Throwable t) {
              Toast.makeText(getContext(), "Opps! Something went wrong. Please try again...", Toast.LENGTH_SHORT).show();

          }
      });
    }
}