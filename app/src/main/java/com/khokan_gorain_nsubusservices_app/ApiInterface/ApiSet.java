package com.khokan_gorain_nsubusservices_app.ApiInterface;

import com.khokan_gorain_nsubusservices_app.ModelClass.AreaName;
import com.khokan_gorain_nsubusservices_app.ModelClass.AutoImageSlider;
import com.khokan_gorain_nsubusservices_app.ModelClass.BusNumber;
import com.khokan_gorain_nsubusservices_app.ModelClass.DatabaseResponseMsg;
import com.khokan_gorain_nsubusservices_app.ModelClass.SearchingBusList;
import com.khokan_gorain_nsubusservices_app.ModelClass.StudentMsgClass;
import com.khokan_gorain_nsubusservices_app.ModelClass.TeacherRouthInchargeList;
import com.khokan_gorain_nsubusservices_app.ModelClass.UserLoginInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiSet {

    @GET("image_slider.php")
    Call<List<AutoImageSlider>> getSliderImage();

    @GET("area_name.php")
    Call<List<AreaName>> getAreaName();

    @GET("bus_number.php")
    Call<List<BusNumber>> getBusNumber();

    @GET("get_bus_list_area_name.php")
    Call<List<SearchingBusList>> getBusListFromAreaName(@Query("fromArea") String fromArea, @Query("toArea") String toArea);

    @GET("get_bus_list_from_bus_number.php")
    Call<List<SearchingBusList>> getBusListFromBusNumber(@Query("busNumber") String  busNumber);

    @GET("get_bus_current_location.php")
    Call<List<SearchingBusList>> getBusCurrentLocation(@Query("busId") int busId);

    @GET("get_all_bus_list.php")
    Call<List<SearchingBusList>> getAllBusList();

    @GET("get_user_login_data.php")
    Call<List<UserLoginInfo>> getUserLoginData(@Query("userName") String userName, @Query("userPassword") String userPassword);

    @GET("check_user_phone_no_isexist.php")
    Call<DatabaseResponseMsg> isPhoneNumberExist(@Query("userPhoneNumber") String userPhoneNumber);

    @GET("change_student_password.php")
    Call<DatabaseResponseMsg> changeUserPassword(@Query("userPhoneNumber") String userPhoneNumber, @Query("userPassword") String userPassword);

    @GET("student_details_update.php")
    Call<DatabaseResponseMsg> updateUserData(@Query("userName") String userName, @Query("userEmailId") String userEmailId,
                                             @Query("userPhoneNumber") String userPhoneNumber,
                                             @Query("userProfileImg") String  userProfileImg);

    @GET("teacher_route_incharge_list.php")
    Call<List<TeacherRouthInchargeList>> getTeacherRouteInchargeList();

    @GET("student_notice_message.php")
    Call<List<StudentMsgClass>> getStudentNoticeMessage();
    
}
