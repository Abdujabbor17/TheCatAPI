package com.masterandroid.thecatapi_app.networking

import com.masterandroid.thecatapi_app.model.ResponseItem
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface HomeService {


    @GET("images/search?limit=100&&page=1")
    fun listPhotos(): Call<ArrayList<ResponseItem>>



    @GET("images?limit=50")
    fun listMyPhotos(): Call<ArrayList<ResponseItem>>


    @Multipart
    @POST("images/upload")
    fun createPhoto(@Part  image: MultipartBody.Part): Call<ResponseItem>


    @DELETE("images/{id}")
    fun deletePhoto(@Path("id")id:String): Call<ResponseItem>

}