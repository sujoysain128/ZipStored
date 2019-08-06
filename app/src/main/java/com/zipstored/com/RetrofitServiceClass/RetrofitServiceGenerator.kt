package com.liquorworldkotlin.RetrofitServiceClass

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitServiceGenerator {

    private var retrofit: Retrofit? = null
    private val httpClient = OkHttpClient.Builder()
        .readTimeout(600, TimeUnit.SECONDS)
        .connectTimeout(600, TimeUnit.SECONDS)
    private val builder = Retrofit.Builder()
        .baseUrl(mServiceList.Base_URL)
        .addConverterFactory(GsonConverterFactory.create())


    fun <S> createService(serviceClass: Class<S>): S {
        builder.client(httpClient.build())
        retrofit = builder.build()
        return retrofit!!.create(serviceClass)
    }
}
