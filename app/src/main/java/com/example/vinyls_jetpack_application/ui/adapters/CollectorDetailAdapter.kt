package com.example.vinyls_jetpack_application.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.vinyls_jetpack_application.R
import com.example.vinyls_jetpack_application.databinding.CollectorDetailBinding
import com.example.vinyls_jetpack_application.models.Collector

class CollectorDetailAdapter: RecyclerView.Adapter<CollectorDetailAdapter.CollectorViewHolder>(){

    var collectorDetail : Collector? = null
        set(value) {
            field = value
            notifyItemChanged(0)
        }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectorViewHolder {

        val withDataBinding: CollectorDetailBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            CollectorViewHolder.LAYOUT_DETAIL,
            parent,
            false)
        return CollectorViewHolder(withDataBinding)
    }

    override fun getItemCount(): Int {
        return if (collectorDetail != null) 1 else 0
    }

    override fun onBindViewHolder(holder: CollectorViewHolder, position: Int) {
        holder.viewDataBinding.also {
            Log.d("CollectorDetailAdapter", "artist: $collectorDetail")
            it.collector = collectorDetail
        }
    }
    class CollectorViewHolder(val viewDataBinding: CollectorDetailBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        companion object {
            @LayoutRes
            val LAYOUT_DETAIL = R.layout.collector_detail
        }
    }
}