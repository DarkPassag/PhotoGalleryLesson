package com.ch.ni.an.photogallery.api

import retrofit2.Call
import retrofit2.http.GET

interface FlickrApi {

    @GET(
        "services/rest/?method=flickr.interestingness.getList" +
                "&api_key=$API_KEY" +
                "&format=json" +
                "&nojsoncallback=1" +
                "&extras=url_s"
    )
    fun fetchPhotos() : Call<PhotoResponse>
}

            /**
                You must have your own api key flickr
                Just reg on flickr.com and get it.
                Insert your key into the API_KEY value and enjoy
            */

const val API_KEY = ""