package com.ch.ni.an.photogallery

import android.content.Context

class SizeScreen(
    private val context :Context
): GetWidthScreen {

    private val metrics = context.resources.displayMetrics

    val widthScreen: Int

    init {
        widthScreen = getWidth()
    }

    override fun getWidth() :Int = metrics.widthPixels
}