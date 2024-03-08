package com.khokan_gorain_nsubusservices_app.Fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.khokan_gorain_nsubusservices_app.Activityes.UcropperActivity;
import com.khokan_gorain_nsubusservices_app.ApiController.ApiController;
import com.khokan_gorain_nsubusservices_app.ConstantData.Constant;
import com.khokan_gorain_nsubusservices_app.ModelClass.DatabaseResponseMsg;
import com.khokan_gorain_nsubusservices_app.ModelClass.UserLoginInfo;
import com.khokan_gorain_nsubusservices_app.databinding.FragmentProfileBinding;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragment extends Fragment {

    public FragmentProfileBinding binding;
    String userName,userPhoneNumber,userEmailId;
    ActivityResultLauncher<String> cropImage;
    Bitmap bitmap;
    Uri uri;
    String encodedUserProfileImg;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater,container,false);

        binding.userPhoneNo.setText(Constant.userPhoneNumber);
        binding.userName.setText(Constant.userName);
        binding.userEmailId.setText(Constant.userEmailId);
        Glide.with(getContext()).load(Constant.MAIN_URL+Constant.userProfile).into(binding.userProfileImg);
        //Toast.makeText(getContext(), Constant.MAIN_URL+Constant.userProfile, Toast.LENGTH_LONG).show();


        cropImage = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            Intent intent = new Intent(getContext(), UcropperActivity.class);
            intent.putExtra("SendImageData",result.toString());
            startActivityForResult(intent,100);
        });

        binding.userProfileUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserData();
            }
        });

        binding.fbAddImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withContext(getContext())
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                cropImage.launch("image/*");
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        return binding.getRoot();

    }

    private void encodedImageBitmap(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        encodedUserProfileImg = android.util.Base64.encodeToString(bytes, android.util.Base64.DEFAULT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == 100 && resultCode == 101 && data != null){
            String result = data.getStringExtra("CROP");
            uri = data.getData();
            if(result != null){
                uri = Uri.parse(result);
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                    encodedImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getContext(), "Something went wrong. Please try again...", Toast.LENGTH_SHORT).show();
            }
            binding.userProfileImg.setImageURI(uri);
        } else {
            Toast.makeText(getContext(), "Image not selected. Please try again...", Toast.LENGTH_LONG).show();
        }
    }

    public void updateUserData(){

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        userName = binding.userName.getText().toString().trim();
        userPhoneNumber = binding.userPhoneNo.getText().toString().trim();
        userEmailId = binding.userEmailId.getText().toString().trim();

        if(userName.isEmpty()){
            binding.userName.setError("Name is empty...");
        } else if(userEmailId.isEmpty()){
            binding.userEmailId.setError("Email id is empty...");
        } else if(userPhoneNumber.isEmpty()){
            binding.userPhoneNo.setError("Phone number is empty...");
        } else if(userPhoneNumber.length() != 10){
            Toast.makeText(getContext(), "Please Enter a valid phone number.", Toast.LENGTH_LONG).show();
        } else {

            progressDialog.show();
            Call<DatabaseResponseMsg> call = ApiController
                    .getInstance()
                    .getApi()
                    .updateUserData(userName,userEmailId,userPhoneNumber,encodedUserProfileImg);

            call.enqueue(new Callback<DatabaseResponseMsg>() {
                @Override
                public void onResponse(Call<DatabaseResponseMsg> call, Response<DatabaseResponseMsg> response) {
                    DatabaseResponseMsg data = response.body();
                    if(data != null){
                        String result = data.getMessage();
                        if(result.equalsIgnoreCase("success")){
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Data Update Successful...", Toast.LENGTH_LONG).show();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "OPPS! Something went wrong. Please try again...", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Image size should be less then 1 MB", Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<DatabaseResponseMsg> call, Throwable t){
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), t.toString(), Toast.LENGTH_LONG).show();
                }

            });

        }
    }
}