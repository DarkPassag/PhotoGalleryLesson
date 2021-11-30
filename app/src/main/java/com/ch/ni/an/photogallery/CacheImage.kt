package com.ch.ni.an.photogallery

import android.util.LruCache

class CacheImage<K, V>(
    private val maxSize: Int = 8 * 1024 * 1024
): LruCache<K, V>( maxSize ) {




}