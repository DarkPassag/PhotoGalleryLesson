package com.ch.ni.an.photogallery

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ch.ni.an.photogallery.api.FlickrApi
import com.ch.ni.an.photogallery.api.FlickrResponse
import com.ch.ni.an.photogallery.api.PhotoResponse
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FlickrFetch {

    private val flickrApi: FlickrApi

    init {

        val gsonConverter = GsonBuilder()
            .registerTypeAdapter(
                PhotoResponse::class.java,
                PhotoDeserializer()
            ).create()


        val retrofit : Retrofit = Retrofit.Builder()
            .baseUrl("https://api.flickr.com/")
            .addConverterFactory(GsonConverterFactory.create(gsonConverter))
            .build()

        flickrApi = retrofit.create(FlickrApi::class.java)
    }


    fun fetch(): LiveData<List<GalleryItem>>{
        val responseLiveData: MutableLiveData<List<GalleryItem>> = MutableLiveData()

        val flickrRequest = flickrApi.fetchPhotos()

        flickrRequest.enqueue(object : Callback<PhotoResponse>{
            override fun onResponse(
                call :Call<PhotoResponse>,
                response :Response<PhotoResponse>,
            ) {
                val photoResponse: PhotoResponse? = response.body()
                Log.e("Tag", photoResponse?.galleryItem.toString())
                val galleryItems: List<GalleryItem> = photoResponse?.galleryItem
                    ?: mutableListOf()
                galleryItems.filterNot {
                    it.url.isBlank()
                }
                responseLiveData.value = galleryItems
            }

            override fun onFailure(call :Call<PhotoResponse>, t :Throwable) {
                Log.e("Fail", t.message.toString())
            }
        })

        return responseLiveData
    }



}