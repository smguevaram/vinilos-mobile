package com.example.vinyls_jetpack_application.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.vinyls_jetpack_application.R
import com.example.vinyls_jetpack_application.databinding.AlbumDetailBinding
import com.example.vinyls_jetpack_application.databinding.AlbumItemBinding
import com.example.vinyls_jetpack_application.models.Album
import com.example.vinyls_jetpack_application.ui.AlbumFragmentDirections

class AlbumDetailAdapter: RecyclerView.Adapter<AlbumDetailAdapter.AlbumViewHolder>(){

    var albumsDetail : Album? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {

        val withDataBinding: AlbumDetailBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            AlbumViewHolder.LAYOUT_DETAIL,
            parent,
            false)
        return AlbumViewHolder(withDataBinding)
    }

    override fun getItemCount(): Int {
        return if (albumsDetail != null) 1 else 0
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.viewDataBinding.also {
            Log.d("AlbumsDetailAdapter", "album: $albumsDetail")
            it.album = albumsDetail
        }
    }



    class AlbumViewHolder(val viewDataBinding: AlbumDetailBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT_DETAIL = R.layout.album_detail
        }
    }


}