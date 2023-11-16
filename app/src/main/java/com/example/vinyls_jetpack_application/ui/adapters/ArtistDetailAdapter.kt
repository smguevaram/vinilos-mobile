package com.example.vinyls_jetpack_application.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.vinyls_jetpack_application.R
import com.example.vinyls_jetpack_application.databinding.ArtistDetailBinding
import com.example.vinyls_jetpack_application.databinding.ArtistItemBinding
import com.example.vinyls_jetpack_application.models.Artist
import com.example.vinyls_jetpack_application.ui.ArtistFragmentDirections

class ArtistDetailAdapter: RecyclerView.Adapter<ArtistDetailAdapter.ArtistViewHolder>(){

    var artistsDetail : Artist? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {

        val withDataBinding: ArtistDetailBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            ArtistViewHolder.LAYOUT_DETAIL,
            parent,
            false)
        return ArtistViewHolder(withDataBinding)
    }

    override fun getItemCount(): Int {
        return if (artistsDetail != null) 1 else 0
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        holder.viewDataBinding.also {
            Log.d("ArtistsDetailAdapter", "artist: $artistsDetail")
            it.artist = artistsDetail
        }
    }



    class ArtistViewHolder(val viewDataBinding: ArtistDetailBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT_DETAIL = R.layout.artist_detail
        }
    }


}