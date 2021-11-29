package com.ch.ni.an.photogallery

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PhotoGalleryFragment: Fragment() {

    private lateinit var recyclerView :RecyclerView
    private lateinit var myModel: PhotoGalleryViewModel
    private lateinit var thumbnailDownloader :ThumbnailDownloader<PhotoHolder>


    override fun onCreate(savedInstanceState :Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        myModel = ViewModelProvider(this).get(PhotoGalleryViewModel::class.java)
        val responseHandler = Handler()
        thumbnailDownloader = ThumbnailDownloader(
            responseHandler) { photoHolder, bitmap ->
            val drawable = BitmapDrawable(resources, bitmap)
            photoHolder.bindTitle(drawable)
        }
        lifecycle.addObserver(thumbnailDownloader.fragmentLifecycleObserver)
        myModel.fragmentLifecycle.value = viewLifecycleOwnerLiveData.value

    }

    override fun onCreateView(
        inflater :LayoutInflater,
        container :ViewGroup?,
        savedInstanceState :Bundle?
    ) :View? {
        val photoFragment = inflater.inflate(R.layout.fragment_photo_gallery, container, false)

//        lifecycle.addObserver(thumbnailDownloader.viewLifecycleObserver)


        myModel.galleryItemLiveData.observe(viewLifecycleOwner, {
            recyclerView.adapter = PhotoAdapter(it)
        })


        myModel.fragmentLifecycle.value = viewLifecycleOwnerLiveData.value

        myModel.fragmentLifecycle.observe(viewLifecycleOwner, {
            it?.lifecycle?.addObserver(thumbnailDownloader.viewLifecycleObserver)
            if(it?.lifecycle != null){
                val state = it.lifecycle.currentState.name
                Log.i("StartObserved", state)
            }
            if(it?.lifecycle == null){
                it.lifecycle.removeObserver(thumbnailDownloader.viewLifecycleObserver)
            }



        })



        recyclerView = photoFragment.findViewById(R.id.photo_recycler_view)
        recyclerView.layoutManager = GridLayoutManager(context, 3)

        return photoFragment

    }

    override fun onViewCreated(view :View, savedInstanceState :Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myModel.fragmentLifecycle.value = viewLifecycleOwnerLiveData.value
    }

    override fun onDestroyView() {
        super.onDestroyView()
        lifecycle.removeObserver(thumbnailDownloader.viewLifecycleObserver)
        myModel.fragmentLifecycle.value = viewLifecycleOwnerLiveData.value
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(thumbnailDownloader.fragmentLifecycleObserver)
        myModel.fragmentLifecycle.value = viewLifecycleOwnerLiveData.value
    }

    companion object {
        fun newInstance() : PhotoGalleryFragment{
            val args = Bundle()

            val fragment = PhotoGalleryFragment()
            fragment.arguments = args
            return fragment
        }
    }


    private class PhotoHolder(
        itemImageView :ImageView
    ): RecyclerView.ViewHolder(itemImageView){
        val bindTitle: (Drawable) -> Unit = itemImageView::setImageDrawable
    }


    private inner class PhotoAdapter(
        private val galleryItems: List<GalleryItem>
    ): RecyclerView.Adapter<PhotoHolder>() {

        override fun onCreateViewHolder(parent :ViewGroup, viewType :Int) :PhotoHolder {
           val view = layoutInflater.inflate(
               R.layout.list_item_gallery,
               parent,
               false
           ) as ImageView
            return PhotoHolder(view)
        }

        override fun onBindViewHolder(holder :PhotoHolder, position :Int) {
            val item = galleryItems[position]
            val placeHolder: Drawable = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.bill_up_close
            ) ?: ColorDrawable()
            holder.bindTitle(placeHolder)
            thumbnailDownloader.queueThumbnail(holder, item.url)
        }

        override fun getItemCount() :Int = galleryItems.size
    }
}