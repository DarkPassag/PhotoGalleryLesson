package com.ch.ni.an.photogallery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import com.ch.ni.an.photogallery.api.FlickrApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory



private const val TAG = "PhotoGalleryFragment"

class PhotoGalleryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState :Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_gallery)

        if(savedInstanceState == null){
            supportFragmentManager
                .beginTransaction()
                .add(
                    R.id.fragmentContainer,
                    PhotoGalleryFragment.newInstance()
                ).commit()
        }

    }
}