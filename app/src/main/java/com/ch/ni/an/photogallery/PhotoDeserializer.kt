package com.ch.ni.an.photogallery


import com.ch.ni.an.photogallery.api.PhotoResponse
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class PhotoDeserializer: JsonDeserializer<PhotoResponse> {
    override fun deserialize(
        json :JsonElement?,
        typeOfT :Type?,
        context :JsonDeserializationContext?,
    ) :PhotoResponse {
        val responseObject = json?.asJsonObject?.getAsJsonObject("photos")
        val response = responseObject?.getAsJsonArray("photo")
        val photoResponse = PhotoResponse()
        val galleryItems = mutableListOf<GalleryItem>()
        response?.forEach {
            val photoElement = it.asJsonObject
            val galleryItem = GalleryItem(
                photoElement.get("title").asString,
                photoElement.get("id").asString,
                photoElement.get("url_s").asString
            )
            galleryItems.add(galleryItem)
        }
        photoResponse.galleryItem = galleryItems
        return photoResponse
    }
}