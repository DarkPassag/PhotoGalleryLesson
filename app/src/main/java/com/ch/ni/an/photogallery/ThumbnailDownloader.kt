package com.ch.ni.an.photogallery

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Message
import android.util.Log
import android.util.LruCache
import androidx.lifecycle.*
import java.util.concurrent.ConcurrentHashMap


private const val TAG = "ThumbnailDownloader"
private const val MESSAGE_DOWNLOAD = 0

class ThumbnailDownloader< in T>(
    private val responseHandler: Handler,
    private val onThumbnailDownloader : (T, Bitmap) -> Unit

): HandlerThread(TAG), LifecycleEventObserver {

    private var hasQuit = false



    companion object {

        private val cacheImage = CacheImage<String, Bitmap>()
    }

    private lateinit var requestHandler: Handler
    private val requestMap = ConcurrentHashMap<T, String>()
    private val flickrFetch = FlickrFetch()




    @Suppress("UNCHECKED_CAST")
    @SuppressLint("HandlerLeak")
    override fun onLooperPrepared() {
        requestHandler = object : Handler(Looper.myLooper()!!){
            override fun handleMessage(msg :Message) {
                if(msg.what == MESSAGE_DOWNLOAD) {
                    val target = msg.obj as T
                    handleRequest(target)
                }

            }
        }
        super.onLooperPrepared()
    }


    override fun quit() :Boolean {
        hasQuit = true
        return super.quit()
    }





    fun queueThumbnail(target : T, url: String) {
        requestMap[target] = url
        requestHandler.obtainMessage(MESSAGE_DOWNLOAD, target).sendToTarget()
    }

    private fun handleRequest(target :T){
        val url = requestMap[target] ?: return
        var bitmap: Bitmap? = cacheImage.get(url)

        if(bitmap == null){
            bitmap = flickrFetch.fetchPhoto(url) ?: return
            cacheImage.put(url, bitmap)
            responseHandler.post(Runnable {
                if(requestMap[target] != url || hasQuit){
                    return@Runnable
                }
                requestMap.remove(target)
                onThumbnailDownloader(target, bitmap)
            })
        } else {
            responseHandler.post(Runnable {
                if(requestMap[target] != url || hasQuit){
                    return@Runnable
                }
                requestMap.remove(target)
                onThumbnailDownloader(target, bitmap)
            })
        }
    }


    override fun onStateChanged(source :LifecycleOwner, event :Lifecycle.Event) {
        when(event){
            Lifecycle.Event.ON_CREATE -> {
                Log.e("TAG", "StateChanged: Create")
            }
            Lifecycle.Event.ON_START -> {}
            Lifecycle.Event.ON_RESUME -> {}
            Lifecycle.Event.ON_PAUSE -> {}
            Lifecycle.Event.ON_STOP -> {}
            Lifecycle.Event.ON_DESTROY -> {
                requestMap.clear()
                quit()

            }
            Lifecycle.Event.ON_ANY -> {}

        }
    }


}