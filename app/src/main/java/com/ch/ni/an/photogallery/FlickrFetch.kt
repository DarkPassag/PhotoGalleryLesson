package com.ch.ni.an.photogallery

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ch.ni.an.photogallery.api.FlickrApi
import com.ch.ni.an.photogallery.api.FlickrResponse
import com.ch.ni.an.photogallery.api.PhotoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FlickrFetch {

    private val flickrApi: FlickrApi

    init {
        val retrofit : Retrofit = Retrofit.Builder()
            .baseUrl("https://api.flickr.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        flickrApi = retrofit.create(FlickrApi::class.java)
    }


    fun fetch(): LiveData<List<GalleryItem>>{
        val responseLiveData: MutableLiveData<List<GalleryItem>> = MutableLiveData()

        val flickrRequest = flickrApi.fetchPhotos()

        flickrRequest.enqueue(object : Callback<FlickrResponse>{
            override fun onResponse(
                call :Call<FlickrResponse>,
                response :Response<FlickrResponse>,
            ) {
                val flickrResponse: FlickrResponse? = response.body()
                val photoResponse: PhotoResponse? = flickrResponse?.photos
                val galleryItems: List<GalleryItem> = photoResponse?.galleryItem
                    ?: mutableListOf()
                galleryItems.filterNot {
                    it.url.isBlank()
                }
                responseLiveData.value = galleryItems
            }

            override fun onFailure(call :Call<FlickrResponse>, t :Throwable) {
                Log.e("Fail", t.message.toString())
            }
        })

        return responseLiveData
    }



}