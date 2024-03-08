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

import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.khokan_gorain_nsubusservices_app.Activityes.MapsActivity;
import com.khokan_gorain_nsubusservices_app.AdapterClass.SearchingBusListAd;
import com.khokan_gorain_nsubusservices_app.AdapterClass.TeacherRouteInchargeListAd;
import com.khokan_gorain_nsubusservices_app.ApiController.ApiController;
import com.khokan_gorain_nsubusservices_app.ConstantData.Constant;
import com.khokan_gorain_nsubusservices_app.ModelClass.AreaName;
import com.khokan_gorain_nsubusservices_app.ModelClass.SearchingBusList;
import com.khokan_gorain_nsubusservices_app.R;
import com.khokan_gorain_nsubusservices_app.databinding.FragmentFindBusWithAreaNameBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FindBusWithAreaNameFragment extends Fragment {

    FragmentFindBusWithAreaNameBinding binding;
    ArrayList<String> areaNamesArray;
    ArrayAdapter<String> areaNameArrayAdapter;
    ArrayList<SearchingBusList> busListArray;
    SearchingBusListAd searchingBusListAd;

    public FindBusWithAreaNameFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFindBusWithAreaNameBinding.inflate(inflater,container,false);


        getAreaName();

        binding.startingPoint.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Constant.startingPoint = String.valueOf(adapterView.getItemAtPosition(i).toString());
            }
        });

        binding.endingPoint.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Constant.endingPoint = String.valueOf(adapterView.getItemAtPosition(i).toString());
            }
        });

        binding.findMyBusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  getSearchingBusList();
//                Intent intent = new Intent(getContext(), MapsActivity.class);
//                startActivity(intent);
            }
        });




        return binding.getRoot();
    }

    public void getAreaName(){
        Call<List<AreaName>> call = ApiController
                .getInstance()
                .getApi()
                .getAreaName();

        call.enqueue(new Callback<List<AreaName>>() {
            @Override
            public void onResponse(Call<List<AreaName>> call, Response<List<AreaName>> response) {
                List<AreaName> data = response.body();
                if(data != null){
                    areaNamesArray = new ArrayList<>();
                    for(int i=0;i<data.size();i++) {
                        areaNamesArray.add(data.get(i).getName());
                    }
                    areaNameArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_activated_1,areaNamesArray);
                    binding.startingPoint.setThreshold(2);
                    binding.startingPoint.setAdapter(areaNameArrayAdapter);
                    binding.endingPoint.setThreshold(2);
                    binding.endingPoint.setAdapter(areaNameArrayAdapter);

                } else {
                    Toast.makeText(getContext(), "Unable to fetch location. Please tyr again...", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<AreaName>> call, Throwable t) {
                Toast.makeText(getContext(), "Please check your internet connection and try again...", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getSearchingBusList(){

        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Searching...");

        String fromArea = Constant.startingPoint;
        String toArea = Constant.endingPoint;
        if(fromArea.isEmpty()){
            binding.startingPoint.setError("Empty Field...");
        } else if(toArea.isEmpty()){
            binding.endingPoint.setError("Empty Field...");
        } else {
            progressDialog.show();
            Call<List<SearchingBusList>> call = ApiController
                    .getInstance()
                    .getApi()
                    .getBusListFromAreaName(Constant.startingPoint,Constant.endingPoint);

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
                    Toast.makeText(getContext(), "Opps! Something went wrong. Please try again...", Toast.LENGTH_LONG).show();
                }
            });
        }

    }
}