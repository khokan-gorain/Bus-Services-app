package com.khokan_gorain_nsubusservices_app.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.khokan_gorain_nsubusservices_app.Activityes.MapsActivity;
import com.khokan_gorain_nsubusservices_app.AdapterClass.SearchingBusListAd;
import com.khokan_gorain_nsubusservices_app.ApiController.ApiController;
import com.khokan_gorain_nsubusservices_app.ConstantData.Constant;
import com.khokan_gorain_nsubusservices_app.ModelClass.BusNumber;
import com.khokan_gorain_nsubusservices_app.ModelClass.SearchingBusList;
import com.khokan_gorain_nsubusservices_app.R;
import com.khokan_gorain_nsubusservices_app.databinding.FragmentFindBusWithBusNumberBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FindBusWithBusNumberFragment extends Fragment {

    FragmentFindBusWithBusNumberBinding binding;
    ArrayList<String> busNumberArray;
    ArrayAdapter<String> busNumber;
    ArrayList<SearchingBusList> busListArray;
    SearchingBusListAd searchingBusListAd;

    public FindBusWithBusNumberFragment() {


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFindBusWithBusNumberBinding.inflate(inflater,container,false);

        getBusNumber();

        binding.busNumber.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Constant.busNumber = String.valueOf(adapterView.getItemAtPosition(i).toString());
            }
        });

        binding.findMyBusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getBusListFromBusNumber();
            }
        });


        return binding.getRoot();
    }

    public void getBusNumber(){
        Call<List<BusNumber>> call = ApiController
                .getInstance()
                .getApi()
                .getBusNumber();

        call.enqueue(new Callback<List<BusNumber>>() {
            @Override
            public void onResponse(Call<List<BusNumber>> call, Response<List<BusNumber>> response) {
                List<BusNumber> data = response.body();
                if(data.isEmpty())
                {
                    Toast.makeText(getContext(), "Unable is fetch bus number. Please try again...", Toast.LENGTH_LONG).show();
                } else {
                    busNumberArray = new ArrayList<>();
                    for(int i=0;i<data.size();i++){
                        busNumberArray.add(data.get(i).getBus_number());
                    }
                    busNumber = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,busNumberArray);
                    binding.busNumber.setAdapter(busNumber);
                }
            }

            @Override
            public void onFailure(Call<List<BusNumber>> call, Throwable t) {
                Toast.makeText(getContext(),"Please check your internet connection and please try again...", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getBusListFromBusNumber(){

        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Searching...");

        String  busNumber = Constant.busNumber;
        if(busNumber.isEmpty()){
            binding.busNumber.setError("Empty Field...");
        } else {

            progressDialog.show();
            Call<List<SearchingBusList>> call = ApiController
                    .getInstance()
                    .getApi()
                    .getBusListFromBusNumber(busNumber);

            call.enqueue(new Callback<List<SearchingBusList>>() {
                @Override
                public void onResponse(Call<List<SearchingBusList>> call, Response<List<SearchingBusList>> response) {
                    List<SearchingBusList> data = response.body();
                    if(data == null){
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Sorry! Bus not found...", Toast.LENGTH_LONG).show();
                    } else {
                        progressDialog.dismiss();
                        busListArray = new ArrayList<>();
                        for(int i=0;i<data.size();i++){
                            SearchingBusList searchingBusList = new SearchingBusList(data.get(i).getId(),data.get(i).getStatus(),data.get(i).getBus_number()
                                    ,data.get(i).getDv_id(),data.get(i).getLatitude_gmap(),data.get(i).getLongitude_gmap(),data.get(i).getDv_name(),
                                    data.get(i).getDv_phone_no(),data.get(i).getFrom_area(),data.get(i).getTo_area(),data.get(i).getDv_profile_img());
                            busListArray.add(searchingBusList);

                           // Toast.makeText(getContext(), String.valueOf(data.get(i).getId()), Toast.LENGTH_SHORT).show();
                        }

                        searchingBusListAd = new SearchingBusListAd(busListArray, getContext(), new SearchingBusListAd.ItemClickListener() {
                            @Override
                            public void onItemClick(SearchingBusList searchingBusList) {
                                int busId = searchingBusList.getId();
                                Intent intent = new Intent(getContext(),MapsActivity.class);
                                intent.putExtra("busId",busId);
                                startActivity(intent);
                            }
                        });

                        binding.busListRecView.setAdapter(searchingBusListAd);
                        searchingBusListAd.notifyDataSetChanged();


                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
                        binding.busListRecView.setLayoutManager(linearLayoutManager);
                        binding.busListRecView.setHasFixedSize(true);
                    }
                }

                @Override
                public void onFailure(Call<List<SearchingBusList>> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), t.toString(), Toast.LENGTH_LONG).show();

                }
            });
        }

    }
}