package com.liquorworldkotlin.RetrofitServiceClass

import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.POST
import retrofit2.http.Multipart



interface ServiceClient {

    @FormUrlEncoded
    @POST(mServiceList.master_slots)
    fun getSlotResponse(@FieldMap fieldMap: Map<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST(mServiceList.master_amenities)
    fun getAmenitiesResponse(@FieldMap fieldMap: Map<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST(mServiceList.login)
    fun getLogInResponse(@FieldMap fieldMap: Map<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST(mServiceList.logout)
    fun getLogOutResponse(@FieldMap fieldMap: Map<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST(mServiceList.forgot_password)
    fun getForgotPasswordResponse(@FieldMap fieldMap: Map<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST(mServiceList.register)
    fun getSignUpResponse(@FieldMap fieldMap: Map<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST(mServiceList.storage_providers_list)
    fun getStorageListResponse(@FieldMap fieldMap: Map<String, String>): Call<ResponseBody>


    @FormUrlEncoded
    @POST(mServiceList.storage_providers_details)
    fun getStorageDetailsResponse(@FieldMap fieldMap: Map<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST(mServiceList.location_list)
    fun getLocationListResponse(@FieldMap fieldMap: Map<String, String>): Call<ResponseBody>



}
