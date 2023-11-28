package com.example.vinyls_jetpack_application.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vinyls_jetpack_application.R
import com.example.vinyls_jetpack_application.database.dao.AppDatabase
import com.example.vinyls_jetpack_application.databinding.CollectorDetailFragmentBinding
import com.example.vinyls_jetpack_application.models.Collector
import com.example.vinyls_jetpack_application.ui.adapters.CollectorDetailAdapter
import com.example.vinyls_jetpack_application.viewmodels.CollectorDetailViewModel

class CollectorDetailFragment: Fragment() {

    private var _binding: CollectorDetailFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: CollectorDetailViewModel
    private var viewModelAdapter: CollectorDetailAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CollectorDetailFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModelAdapter = CollectorDetailAdapter()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.testCollectorDetailRv
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = viewModelAdapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        activity.actionBar?.title = getString(R.string.title_artist_detail)
        val args: CollectorDetailFragmentArgs by navArgs()
        Log.d("Args", args.collectorId.toString())

        val database = AppDatabase.getDatabase(activity.application)
        val collectorDao = database.collectorDao()

        viewModel = ViewModelProvider(this, CollectorDetailViewModel.Factory(activity.application,args.collectorId, collectorDao)).get(
            CollectorDetailViewModel::class.java)
        viewModel.collector.observe(viewLifecycleOwner, Observer<Collector> {
            it.apply {
                viewModelAdapter!!.collectorDetail = this
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