package com.khokan_gorain_nsubusservices_app.ApiController;

import com.khokan_gorain_nsubusservices_app.ApiInterface.ApiSet;
import com.khokan_gorain_nsubusservices_app.ConstantData.Constant;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiController {
    private static ApiController apiController;
    private static Retrofit retrofit;

    ApiController()
    {
        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.MAIN_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized ApiController getInstance()
    {
        if(apiController == null)
            apiController = new ApiController();

        return apiController;
    }

    public ApiSet getApi()
    {
        return retrofit.create(ApiSet.class);
    }
}
