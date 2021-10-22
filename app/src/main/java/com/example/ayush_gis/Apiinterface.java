package com.example.ayush_gis;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Apiinterface {

    //---------------------emp_form_activity


    @GET("get_list_of_district")
    Call<response_to_get_dis_list> get_dis_list();

    //-------------------------------------------------------
    @POST("get_ayush_stream_from_district")
    @FormUrlEncoded
    Call<response_to_get_ayush_type_list>get_ayush_stream(@Field("district_name") String district_name);

    //-------------------------------------------------------
    @POST("get_office_type_from_ayush_stream_and_dis")
    @FormUrlEncoded
    Call<response_to_get_office_type_from_ayush_stream_and_dis>get_office_type(@Field("district_name") String district_name,@Field("ayush_stream") String ayush_stream);


    @POST("get_list_of_office_from_district_stream_office_type")
    @FormUrlEncoded
    Call<response_to_get_list_of_offices>get_office_list(@Field("district_name") String district_name,@Field("ayush_stream") String ayush_stream,@Field("office_category") String office_category);


    @POST("submitData")
    @FormUrlEncoded
    Call<response_to_submit>insert_data(

            @Field("development_authority_name") String development_authority_name,
            @Field("facility") String facility,
            @Field("construction_year") String construction_year,
            @Field("level") String level,
            @Field("remarks") String remarks,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude,
            @Field("image") String image);


    //---------------------------emp_view_data_activity





    @POST("login")   ///method's name from eclipse
    @FormUrlEncoded
    Call<response_to_login> login(@Field("user_name") String user_name, @Field("password") String password);

//    Call<response_to_submit> insert_data(String ayush_type, String district, String block, String vidhansabha, String post_name, String class_name, String building_type, String office_name, String office_address, String landmark, String police_station, String pincode, String no_of_post, String total_area, String post_filled, String post_vacant, String estb_year, String rooms_available, String latitude, @Field("name") String user_name, @Field("password") String password);
//
//    Call<response_to_submit> insert_data( String opd_type, String ipd_type,String male_opd_old, String female_opd_old, String child_opd_old, String male_opd_new, String female_opd_new, String child_opd_new, String male_ipd_old, String female_ipd_old, String child_ipd_old, String male_ipd_new, String female_ipd_new, String child_ipd_new, String adhar_patient, String mobile_no, String total);

//    Call<response_to_submit> insert_data(String ayush_type, String district, String office_type, String block, String vidhansabha, String post_name, String class_name, String building_type, String office_name, String office_address, String landmark, String police_station, String pincode, String no_of_post, String total_area, String post_filled, String post_vacant, String estb_year, String rooms_available, String latitude, String longitude, String photo);

    Call<response_to_submit> insert_data(String ayush_type, String district, String office_type, String block, String vidhansabha, String post_name, String class_name, String building_type, String office_name, String office_address, String landmark, String police_station, String pincode, String no_of_post, String total_area, String post_filled, String post_vacant, String estb_year, String rooms_available, String bathroom_yes_use, String bathroom_yes__not_use, String bathroom_no, String furniture_sufficient, String furniture_insufficient, String internet_yes_use, String internet_yes_not_use, String internet_no, String internet_not_applicable, String latitude, String longitude, String photo);

//    Call<response_to_submit> insert_data(String opd_type, String ipd_type, String male_opd_old, String female_opd_old, String male_opd_new, String female_opd_new, String male_ipd_old, String female_ipd_old, String male_ipd_new, String female_ipd_new, String adhar_patient, String mobile_no, String total);

    @POST("regenerate_password")
    @FormUrlEncoded
    Call<response_to_submit> send_updated_password(@Field("confirmed_password") String updated_password, @Field("get_user_num") String get_user_num);

    @POST("login_number")
    @FormUrlEncoded
    Call<response_to_number_login> send_number(@Field("user_number") String given_number);


//    Call<response_to_submit> insert_data(String opd_type, String ipd_type, String male_opd_old, String female_opd_old, String adhar_patient_opd_old, String mobile_no_opd_old, String total_opd_old, String male_opd_new, String female_opd_new, String adhar_patient_opd_new, String mobile_no_opd_new, String total_opd_new, String male_ipd_old, String female_ipd_old, String adhar_patient_ipd_old, String mobile_no_ipd_old, String total_ipd_old, String male_ipd_new, String female_ipd_new, String adhar_patient_ipd_new, String mobile_no_ipd_new, String total_ipd_new);

    Call<response_to_submit> insert_data(String opd_choose, String ipd_choose, String opd_new, String opd_old, String ipd_new, String ipd_old, String male_opd_old, String female_opd_old, String adhar_patient_opd_old, String mobile_no_opd_old, String total_opd_old, String male_opd_new, String female_opd_new, String adhar_patient_opd_new, String mobile_no_opd_new, String total_opd_new, String male_ipd_old, String female_ipd_old, String adhar_patient_ipd_old, String mobile_no_ipd_old, String total_ipd_old, String male_ipd_new, String female_ipd_new, String adhar_patient_ipd_new, String mobile_no_ipd_new, String total_ipd_new);
}


