package com.khokan_gorain_nsubusservices_app.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.khokan_gorain_nsubusservices_app.AdapterClass.AllBusListAd;
import com.khokan_gorain_nsubusservices_app.ApiController.ApiController;
import com.khokan_gorain_nsubusservices_app.ModelClass.SearchingBusList;
import com.khokan_gorain_nsubusservices_app.R;
import com.khokan_gorain_nsubusservices_app.databinding.FragmentBusListBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusListFragment extends Fragment {
    private FragmentBusListBinding binding;
    ArrayList<SearchingBusList> busListArray;

    public BusListFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBusListBinding.inflate(inflater,container,false);


        getAllBusList();


        return binding.getRoot();
    }

    public void getAllBusList(){
        Call<List<SearchingBusList>> call = ApiController
                .getInstance()
                .getApi()
                .getAllBusList();

        call.enqueue(new Callback<List<SearchingBusList>>() {
            @Override
            public void onResponse(Call<List<SearchingBusList>> call, Response<List<SearchingBusList>> response) {
                List<SearchingBusList> data = response.body();
                if(data != null){
                    binding.busListLyt.setVisibility(View.VISIBLE);
                    binding.busListShimmer.setVisibility(View.GONE);
                    busListArray = new ArrayList<>();
                    for(int i=0;i<data.size();i++){
                        SearchingBusList searchingBusList = new SearchingBusList(data.get(i).getId(),data.get(i).getStatus(),data.get(i).getBus_number()
                                ,data.get(i).getDv_id(),data.get(i).getLatitude_gmap(),data.get(i).getLongitude_gmap(),data.get(i).getDv_name(),
                                data.get(i).getDv_phone_no(),data.get(i).getFrom_area(),data.get(i).getTo_area(),data.get(i).getDv_profile_img());
                        busListArray.add(searchingBusList);
                    }

                    AllBusListAd allBusListAd = new AllBusListAd(busListArray,getContext());
                    binding.busListRecView.setAdapter(allBusListAd);
                    allBusListAd.notifyDataSetChanged();

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                    binding.busListRecView.setLayoutManager(linearLayoutManager);
                    binding.busListRecView.setHasFixedSize(true);


                } else {
                    Toast.makeText(getContext(), "Sorry! Bus not found...", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<SearchingBusList>> call, Throwable t) {
                Toast.makeText(getContext(), t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}