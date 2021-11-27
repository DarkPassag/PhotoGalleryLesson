package com.ch.ni.an.photogallery

import com.google.gson.annotations.SerializedName

data class GalleryItem(
    @SerializedName("title") val title :String = "null",
    @SerializedName("id") val id :String = "null",
    @SerializedName("url_s") val url :String = "null",
) {}