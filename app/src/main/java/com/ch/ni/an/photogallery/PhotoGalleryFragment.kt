package com.ch.ni.an.photogallery

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PhotoGalleryFragment: Fragment() {

    private lateinit var recyclerView :RecyclerView
    private lateinit var myModel: PhotoGalleryViewModel

    override fun onCreate(savedInstanceState :Bundle?) {
        super.onCreate(savedInstanceState)
        myModel = ViewModelProvider(this).get(PhotoGalleryViewModel::class.java)
    }

    override fun onCreateView(
        inflater :LayoutInflater,
        container :ViewGroup?,
        savedInstanceState :Bundle?
    ) :View? {
        val photoFragment = inflater.inflate(R.layout.fragment_photo_gallery, container, false)


        myModel.galleryItemLiveData.observe(viewLifecycleOwner, {
            recyclerView.adapter = PhotoAdapter(it)
        })



        recyclerView = photoFragment.findViewById(R.id.photo_recycler_view)
        recyclerView.layoutManager = GridLayoutManager(context, 3)

        return photoFragment

    }

    override fun onViewCreated(view :View, savedInstanceState :Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        itemTextView :TextView
    ): RecyclerView.ViewHolder(itemTextView){
        val bindTitle: (CharSequence) -> Unit = itemTextView::setText
    }


    private class PhotoAdapter(
        private val galleryItems: List<GalleryItem>
    ): RecyclerView.Adapter<PhotoHolder>() {
        override fun onCreateViewHolder(parent :ViewGroup, viewType :Int) :PhotoHolder {
           val tetView: TextView = TextView(parent.context)
            return PhotoHolder(tetView)
        }

        override fun onBindViewHolder(holder :PhotoHolder, position :Int) {
            val item = galleryItems[position]
            holder.bindTitle(item.title)
        }

        override fun getItemCount() :Int = galleryItems.size
    }
}