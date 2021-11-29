package com.ch.ni.an.photogallery

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PhotoGalleryViewModel: ViewModel() {

    val galleryItemLiveData:LiveData<List<GalleryItem>>


    init {
        galleryItemLiveData = FlickrFetch().fetch()
    }

    val fragmentLifecycle : MutableLiveData<LifecycleOwner> by lazy {
        MutableLiveData()
    }


    override fun onCleared() {
        super.onCleared()

    }
}