package com.ch.ni.an.photogallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class PhotoGalleryViewModel: ViewModel() {

    val galleryItemLiveData:LiveData<List<GalleryItem>>


    init {
        galleryItemLiveData = FlickrFetch().fetch()
    }


    override fun onCleared() {
        super.onCleared()

    }
}