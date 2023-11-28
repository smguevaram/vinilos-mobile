package com.example.vinyls_jetpack_application.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.vinyls_jetpack_application.database.dao.ArtistDao
import com.example.vinyls_jetpack_application.database.dao.CollectorDao
import com.example.vinyls_jetpack_application.models.Artist
import com.example.vinyls_jetpack_application.models.Collector
import com.example.vinyls_jetpack_application.repository.CollectorRepository
import kotlinx.coroutines.launch

class CollectorDetailViewModel(application: Application, collectorId: Int, private val collectorDao: CollectorDao) :  AndroidViewModel(application)  {

    private val _collector = MutableLiveData<Collector>();

    private val _collectorRepository = CollectorRepository(application, collectorDao)

    val collector: MutableLiveData<Collector>
        get() = _collector

    private var _eventNetworkError = MutableLiveData<Boolean>(false)

    val eventNetworkError: MutableLiveData<Boolean>
        get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)

    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    val id:Int = collectorId

    init {
        refreshDataFromNetwork()
    }

    private fun refreshDataFromNetwork() {
        viewModelScope.launch {
            var data: Collector? = _collectorRepository.getCollectorById(id)

            if(data != null) {
                _collector.postValue(data)
                _eventNetworkError.value = false
                _isNetworkErrorShown.value = false
            } else {
                _eventNetworkError.value = true
            }
        }
    }


    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    class Factory(val app: Application, val collectorId: Int, val collectorDao: CollectorDao) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CollectorDetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CollectorDetailViewModel(app, collectorId, collectorDao) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}