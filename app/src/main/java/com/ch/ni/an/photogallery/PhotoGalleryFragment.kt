package com.ch.ni.an.photogallery

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.w3c.dom.Text

class PhotoGalleryFragment: Fragment() {

    private lateinit var recyclerView :RecyclerView
    private lateinit var myModel: PhotoGalleryViewModel
    private lateinit var adapter: PhotoAdapter
    private lateinit var widthScreen :SizeScreen

    override fun onCreate(savedInstanceState :Bundle?) {
        super.onCreate(savedInstanceState)

        myModel = ViewModelProvider(this).get(PhotoGalleryViewModel::class.java)
        widthScreen = SizeScreen(requireContext())
    }

    override fun onCreateView(
        inflater :LayoutInflater,
        container :ViewGroup?,
        savedInstanceState :Bundle?
    ) :View? {
        val photoFragment = inflater.inflate(R.layout.fragment_photo_gallery, container, false)
        adapter = PhotoAdapter()
        val width = widthScreen.widthScreen

        var spanCount = 3

        if(width in 1081..1799) spanCount = 4
        if(width > 1800) spanCount = 5

        recyclerView = photoFragment.findViewById(R.id.photo_recycler_view)
        recyclerView.layoutManager = GridLayoutManager(context, spanCount)
        recyclerView.adapter = adapter


        return photoFragment

    }

    override fun onViewCreated(view :View, savedInstanceState :Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch{

            myModel.flow.collectLatest {
                adapter.submitData(it)
                recyclerView.adapter = adapter

            }
        }




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
        item :View
    ): RecyclerView.ViewHolder(item){
        val bindTitle: (CharSequence) -> Unit = {
            itemView.findViewById<TextView>(R.id.titleTextView).text = it
        }
    }


    private class PhotoAdapter(

    ): PagingDataAdapter<GalleryItem, PhotoHolder>(PhotoDiffUtil()) {
        override fun onBindViewHolder(holder :PhotoHolder, position :Int) {
            val item = getItem(position)?.title ?: "Empty"
            holder.bindTitle(item)
        }

        override fun onCreateViewHolder(parent :ViewGroup, viewType :Int) :PhotoHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.reyclerview_item, parent, false)
            return PhotoHolder(view)
        }
    }

        private class PhotoDiffUtil: DiffUtil.ItemCallback<GalleryItem>(){
            override fun areItemsTheSame(oldItem :GalleryItem, newItem :GalleryItem) :Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem :GalleryItem, newItem :GalleryItem) :Boolean {
                return oldItem == newItem
            }

        }
    }
