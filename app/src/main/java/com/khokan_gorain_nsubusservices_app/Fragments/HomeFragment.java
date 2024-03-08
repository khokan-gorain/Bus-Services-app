package com.khokan_gorain_nsubusservices_app.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.khokan_gorain_nsubusservices_app.AdapterClass.StudentNoticeSliderAd;
import com.khokan_gorain_nsubusservices_app.ApiController.ApiController;
import com.khokan_gorain_nsubusservices_app.ConstantData.Constant;
import com.khokan_gorain_nsubusservices_app.ModelClass.AutoImageSlider;
import com.khokan_gorain_nsubusservices_app.ModelClass.StudentMsgClass;
import com.khokan_gorain_nsubusservices_app.R;
import com.khokan_gorain_nsubusservices_app.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

  public FragmentHomeBinding binding;
    ArrayList<SlideModel> sliderList;
    StudentNoticeSliderAd sliderMessageAd;
    ArrayList noticeMsg;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater,container,false);

        processData();
        getStudentNoticeMessage();

        return binding.getRoot();
    }

    public void processData() {
        Call<List<AutoImageSlider>> call = ApiController
                .getInstance()
                .getApi()
                .getSliderImage();

        call.enqueue(new Callback<List<AutoImageSlider>>() {
            @Override
            public void onResponse(Call<List<AutoImageSlider>> call, Response<List<AutoImageSlider>> response) {
                List<AutoImageSlider> list = response.body();
                if(list != null){
                    binding.homeFragmentLyt.setVisibility(View.VISIBLE);
                    binding.shimmerLyt.setVisibility(View.GONE);
                    sliderList = new ArrayList<>();
                    for(int i=0;i<list.size();i++) {
                        sliderList.add(new SlideModel(Constant.MAIN_URL+list.get(i).getImg(), ScaleTypes.FIT));
                    }

                    binding.imageSlider.setImageList(sliderList);
                } else {
                    binding.homeFragmentLyt.setVisibility(View.GONE);
                    binding.shimmerLyt.setVisibility(View.VISIBLE);
                }

            }
            @Override
            public void onFailure(Call<List<AutoImageSlider>> call, Throwable t) {
                Toast.makeText(getContext(), "Please check your internet connection...", Toast.LENGTH_LONG).show();
            }
        });

    }


    public void getStudentNoticeMessage() {
        Call<List<StudentMsgClass>> call = ApiController
                .getInstance()
                .getApi()
                .getStudentNoticeMessage();

        call.enqueue(new Callback<List<StudentMsgClass>>() {
            @Override
            public void onResponse(Call<List<StudentMsgClass>> call, Response<List<StudentMsgClass>> response) {
                List<StudentMsgClass> data = response.body();
                if(data.isEmpty()){
                    Toast.makeText(getContext(), "Unable to fetch data. Please try again...", Toast.LENGTH_LONG).show();
                } else {
                    noticeMsg = new ArrayList();
                    for(int i=0;i<data.size();i++){
                        noticeMsg.add(data.get(i).getMessage());
                    }
                    sliderMessageAd = new StudentNoticeSliderAd(getContext(),noticeMsg);
                    binding.viewPager.setAdapter(sliderMessageAd);
                    binding.dot.setViewPager(binding.viewPager);

                }
            }
            @Override
            public void onFailure(Call<List<StudentMsgClass>> call, Throwable t) {
                Toast.makeText(getContext(), "Please check your internet connection...", Toast.LENGTH_LONG).show();
            }
        });
    }
}