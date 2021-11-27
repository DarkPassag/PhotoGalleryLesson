package com.ch.ni.an.photogallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

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