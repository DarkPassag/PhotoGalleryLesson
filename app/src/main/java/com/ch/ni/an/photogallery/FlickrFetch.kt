package com.ch.ni.an.photogallery

import android.util.Log
import com.ch.ni.an.photogallery.api.FlickrApi
import com.ch.ni.an.photogallery.api.PhotoResponse
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

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


   suspend fun fetch(page :Int) :List<GalleryItem> {
        var galleryItems :MutableList<GalleryItem> = mutableListOf()
        val flickrRequest = flickrApi.fetchPhotos(page)
           return if(flickrRequest.isSuccessful){
                val photoResponse = flickrRequest.body()
                galleryItems = photoResponse?.galleryItem?.toMutableList() ?: mutableListOf()
                galleryItems.filterNot {
                    it.url.isBlank()
                }
                galleryItems
            } else {
              emptyList()
            }
    }




    }



