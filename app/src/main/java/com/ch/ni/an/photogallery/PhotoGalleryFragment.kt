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
        adapter = PhotoAdapter()

        recyclerView = photoFragment.findViewById(R.id.photo_recycler_view)
        recyclerView.layoutManager = GridLayoutManager(context, 3)
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
        itemTextView :TextView
    ): RecyclerView.ViewHolder(itemTextView){
        val bindTitle: (CharSequence) -> Unit = itemTextView::setText
    }


    private class PhotoAdapter(

    ): PagingDataAdapter<GalleryItem, PhotoHolder>(PhotoDiffUtil()) {
        override fun onBindViewHolder(holder :PhotoHolder, position :Int) {
            val item = getItem(position)?.title ?: "Empty"
            holder.bindTitle(item)
        }

        override fun onCreateViewHolder(parent :ViewGroup, viewType :Int) :PhotoHolder {
            val textView = TextView(parent.context)
            return PhotoHolder(textView)
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
