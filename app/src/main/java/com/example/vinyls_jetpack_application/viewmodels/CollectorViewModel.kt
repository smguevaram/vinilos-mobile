package com.example.vinyls_jetpack_application.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.vinyls_jetpack_application.database.dao.ArtistDao
import com.example.vinyls_jetpack_application.database.dao.CollectorDao
import com.example.vinyls_jetpack_application.models.Artist
import com.example.vinyls_jetpack_application.models.Collector
import com.example.vinyls_jetpack_application.network.NetworkServiceAdapter
import com.example.vinyls_jetpack_application.repository.ArtistRepository
import com.example.vinyls_jetpack_application.repository.CollectorRepository
import kotlinx.coroutines.launch

class CollectorViewModel(application: Application, collectorsDao: CollectorDao) :  AndroidViewModel(application) {

    private val _collectors = MutableLiveData<List<Collector>>()

    val collectors: LiveData<List<Collector>>
        get() = _collectors

    val _collectorsRepository = CollectorRepository(application, collectorsDao)

    private var _eventNetworkError = MutableLiveData<Boolean>(false)

    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)

    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    init {
        refreshDataFromNetwork()
    }

    private fun refreshDataFromNetwork() {
        viewModelScope.launch {
            var data: List<Collector>? = _collectorsRepository.refreshCollectors()

            if(data != null) {
                _collectors.postValue(data)
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

    class Factory(val app: Application, val collectorsDao: CollectorDao) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CollectorViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CollectorViewModel(app, collectorsDao) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
