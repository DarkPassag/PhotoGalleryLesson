package com.ch.ni.an.photogallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

//class PhotoViewModelFactory(
//   private val fetch :FlickrFetch
//
//): ViewModelProvider.Factory {
//
//    override fun <T : ViewModel?> create(modelClass :Class<T>) :T {
//        if(modelClass.isAssignableFrom(PhotoGalleryViewModel::class.java)){
//          return  PhotoGalleryViewModel(fetch) as T
//        }
//        throw IllegalArgumentException(" Unknowing view model class")
//
//
//    }
//}