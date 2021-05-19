package com.example.myapplication.MainViewModel


import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.models.Countitem
import com.example.myapplication.repositry.MainRepository
import com.example.myapplication.others.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ViewModelMain @ViewModelInject constructor(
    private val mainRepository: MainRepository
): ViewModel(){

    private val _res = MutableStateFlow<Resource<String>>(Resource.loading(null))


    fun getData()  = viewModelScope.launch {
        _res.emit(Resource.loading(null))
        mainRepository.getArticales().let {
            if (it.isSuccessful){
                _res.emit(Resource.success(it.body()))
                it.body()?.let { it1 -> mainRepository.add(it1) }
            }else{
                _res.emit(Resource.error(it.errorBody().toString(), null))
            }
        }

    }

    @ExperimentalCoroutinesApi
    fun getUsers(): StateFlow<Resource<String>> {
        return _res
    }

    fun getLocalData()  = viewModelScope.launch {
        _res.emit(Resource.loading(null))
        mainRepository.getData().let {
            it.collect {
                _res.emit(Resource.success(it))
            }
        }
    }


}