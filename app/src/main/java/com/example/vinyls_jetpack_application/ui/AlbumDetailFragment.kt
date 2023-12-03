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
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vinyls_jetpack_application.R
import com.example.vinyls_jetpack_application.database.dao.AppDatabase
import com.example.vinyls_jetpack_application.databinding.AlbumDetailFragmentBinding
import com.example.vinyls_jetpack_application.databinding.CommentFragmentBinding
import com.example.vinyls_jetpack_application.models.Album
import com.example.vinyls_jetpack_application.models.Comment
import com.example.vinyls_jetpack_application.ui.adapters.AlbumDetailAdapter
import com.example.vinyls_jetpack_application.ui.adapters.AlbumsAdapter
import com.example.vinyls_jetpack_application.ui.adapters.CommentsAdapter
import com.example.vinyls_jetpack_application.viewmodels.AlbumDetailViewModel
import com.example.vinyls_jetpack_application.viewmodels.CommentViewModel

class AlbumDetailFragment : Fragment() {

    private var _binding: AlbumDetailFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: AlbumDetailViewModel
    private var viewModelAdapter: CommentsAdapter? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AlbumDetailFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModelAdapter = CommentsAdapter()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.commentsRv
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = viewModelAdapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        activity.actionBar?.title = "detalle album"
        val args: AlbumDetailFragmentArgs by navArgs()
        Log.d("Args", args.albumId.toString())

        val database = AppDatabase.getDatabase(activity.application)
        val albumsDao = database.albumDao()

        viewModel = ViewModelProvider(this, AlbumDetailViewModel.Factory(activity.application,args.albumId, albumsDao)).get(AlbumDetailViewModel::class.java)
        viewModel.album.observe(viewLifecycleOwner, Observer<Album> {
            it.apply {
                binding.album = it
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

        viewModel.comments.observe(viewLifecycleOwner, Observer<List<Comment>> { comments ->
            comments.apply {
                viewModelAdapter?.comments = comments
            }
        })

        binding.btnAddComment.setOnClickListener {
            val action = AlbumDetailFragmentDirections.actionAlbumFragmentToAlbumAddCommentFragment(args.albumId,binding.textAlbumName.text.toString())
            this.view?.let { it1 -> action.let { it2 -> it1.findNavController().navigate(it2) } }

        }
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