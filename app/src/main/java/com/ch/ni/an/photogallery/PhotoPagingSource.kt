package com.ch.ni.an.photogallery


import androidx.paging.PagingSource
import androidx.paging.PagingState
import java.io.IOException


const val PAGE_SIZE = 100

class PhotoPagingSource(
    private val flickrFetch :FlickrFetch
): PagingSource<Int, GalleryItem>() {


    override fun getRefreshKey(state :PagingState<Int, GalleryItem>) :Int? {
       return state.anchorPosition?.let {
           state.closestPageToPosition(it)?.prevKey?.plus(1)
               ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
       }
    }



    override suspend fun load(params :LoadParams<Int>) :LoadResult<Int, GalleryItem> {

        return try {
            val page = params.key ?: 1

            val response = flickrFetch.fetch(page)
            val photos: List<GalleryItem> = response
            val nextLey = if(photos.size < 100 ) null else page + 1
            val prefKey = if(page == 1) null else -1
            LoadResult.Page(photos, prefKey, nextLey)
        } catch (e: IOException){
            LoadResult.Error(e)
        }




    }
}