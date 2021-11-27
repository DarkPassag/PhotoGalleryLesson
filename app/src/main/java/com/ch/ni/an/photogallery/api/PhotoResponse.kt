package com.ch.ni.an.photogallery.api

import com.ch.ni.an.photogallery.GalleryItem
import com.google.gson.annotations.SerializedName

class PhotoResponse {
    @SerializedName("photo")
    lateinit var galleryItem : List<GalleryItem>
}