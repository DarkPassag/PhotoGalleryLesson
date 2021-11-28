package com.ch.ni.an.photogallery


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn


class PhotoGalleryViewModel(): ViewModel() {

    val flickrFetch: FlickrFetch



    init {
        flickrFetch = FlickrFetch()
    }

    val flow = Pager(
        PagingConfig(PAGE_SIZE, initialLoadSize = 1)
    ) {
        PhotoPagingSource(flickrFetch)
    }.flow
        .cachedIn(viewModelScope)












}