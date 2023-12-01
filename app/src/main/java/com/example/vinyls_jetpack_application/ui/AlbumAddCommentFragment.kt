package com.example.vinyls_jetpack_application.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.vinyls_jetpack_application.R
import com.example.vinyls_jetpack_application.database.dao.AppDatabase
import com.example.vinyls_jetpack_application.databinding.AlbumAddDataFragmentBinding
import com.example.vinyls_jetpack_application.databinding.AlbumCommentFragmentBinding
import com.example.vinyls_jetpack_application.viewmodels.AlbumDetailViewModel
import com.example.vinyls_jetpack_application.viewmodels.AlbumViewModel
import java.util.Calendar

class AlbumAddCommentFragment: Fragment()  {

    private var _binding: AlbumCommentFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AlbumDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AlbumCommentFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val buttonAddComment = binding.btnAddComment


        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }

        val args: AlbumAddCommentFragmentArgs by navArgs()

        val database = AppDatabase.getDatabase(activity.application)
        val albumsDao = database.albumDao()

        binding.tvAddComment.setText("Agregar comentario a ${args.albumName}")

        viewModel = ViewModelProvider(this, AlbumDetailViewModel.Factory(activity.application,args.albumId, albumsDao)).get(
            AlbumDetailViewModel::class.java)


        viewModel.eventCommentAdded.observe(viewLifecycleOwner) {
            if (it) {
                Toast.makeText(context, "Comentario agregado", Toast.LENGTH_SHORT).show()

                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    val action = AlbumAddCommentFragmentDirections.actionAlbumCommentFragmentToAlbumDetailFragment(args.albumId)
                    findNavController().navigate(action)
                }, 2000)
            }
        }


        buttonAddComment.setOnClickListener {
            addComment(args.albumId)
        }

        viewModel.eventNetworkError.observe(viewLifecycleOwner) {
            if (it) onNetworkError()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addComment(albumId:Int) {

        val description = binding.description.text.toString()
        val rating = binding.spinnerRating.selectedItem.toString().toInt()

        viewModel.addComment(albumId,description, rating)

    }

    private fun onNetworkError() {
        if(!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }
}