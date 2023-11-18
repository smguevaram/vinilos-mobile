package com.example.vinyls_jetpack_application.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.vinyls_jetpack_application.R
import com.example.vinyls_jetpack_application.databinding.AlbumItemBinding
import com.example.vinyls_jetpack_application.models.Album
import com.example.vinyls_jetpack_application.ui.AlbumFragmentDirections

class AlbumsAdapter : RecyclerView.Adapter<AlbumsAdapter.AlbumViewHolder>(){

    var albums :List<Album> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    var albumsDetail : Album? = null
        set(value) {
            field = value
            val posicionDelAlbum = albums.indexOf(value)
            if (posicionDelAlbum != -1) {
                notifyItemChanged(posicionDelAlbum)
            }
        }

    var mode: Boolean = false
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val withDataBinding: AlbumItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            AlbumViewHolder.LAYOUT,
            parent,
            false)
        return AlbumViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: AlbumsAdapter.AlbumViewHolder, position: Int) {
        holder.viewDataBinding.also {
            Log.d("AlbumsAdapter", "album: $albumsDetail")
            it.album = albums[position]
        }
        holder.viewDataBinding.root.setOnClickListener {
            Log.d("AlbumsAdapter", "Clicked on ${albums[position]}")
            Log.d("AlbumsAdapter", "mode: $mode")
            albumsDetail = albums[position]
            val action = AlbumFragmentDirections.actionAlbumFragmentToAlbumDetailFragment(albums[position].id)
            // Navigate using that action
            holder.viewDataBinding.root.findNavController().navigate(action)
        }
    }


    override fun getItemCount(): Int {
        return albums.size
    }


    class AlbumViewHolder(val viewDataBinding: AlbumItemBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT = R.layout.album_item
        }
    }


}