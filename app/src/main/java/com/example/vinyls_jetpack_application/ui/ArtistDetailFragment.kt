package com.example.vinyls_jetpack_application.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vinyls_jetpack_application.R
import com.example.vinyls_jetpack_application.databinding.ArtistDetailFragmentBinding
import com.example.vinyls_jetpack_application.databinding.CommentFragmentBinding
import com.example.vinyls_jetpack_application.models.Artist
import com.example.vinyls_jetpack_application.models.Comment
import com.example.vinyls_jetpack_application.ui.adapters.ArtistDetailAdapter
import com.example.vinyls_jetpack_application.ui.adapters.ArtistsAdapter
import com.example.vinyls_jetpack_application.ui.adapters.CommentsAdapter
import com.example.vinyls_jetpack_application.viewmodels.ArtistDetailViewModel
import com.example.vinyls_jetpack_application.viewmodels.CommentViewModel

class ArtistDetailFragment : Fragment() {

    private var _binding: ArtistDetailFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: ArtistDetailViewModel
    private var viewModelAdapter: ArtistDetailAdapter? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ArtistDetailFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModelAdapter = ArtistDetailAdapter()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.testArtistDetailRv
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = viewModelAdapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        activity.actionBar?.title = getString(R.string.title_artist_detail)
        val args: ArtistDetailFragmentArgs by navArgs()
        Log.d("Args", args.artistId.toString())
        viewModel = ViewModelProvider(this, ArtistDetailViewModel.Factory(activity.application,args.artistId)).get(ArtistDetailViewModel::class.java)
        viewModel.artist.observe(viewLifecycleOwner, Observer<Artist> {
            it.apply {
                viewModelAdapter!!.artistsDetail = this
                if(this == null){
                    binding.testText.visibility = View.VISIBLE
                }else{
                    binding.testText.visibility = View.GONE
                }
            }
        })
        viewModel.eventNetworkError.observe(viewLifecycleOwner, Observer<Boolean> { isNetworkError ->
            if (isNetworkError) onNetworkError()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onNetworkError() {
        if(!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }

}