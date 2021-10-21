package com.muslims.firebasemvvm.ui.advices

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muslims.firebasemvvm.R
import com.muslims.firebasemvvm.models.Advice
import com.muslims.firebasemvvm.services.AdvicesServices
import com.muslims.firebasemvvm.ui.home.FireStoreStatus
import kotlinx.coroutines.launch


class AdvicesViewModel : ViewModel() {

    private val _advices = MutableLiveData<List<Advice>>()
    val advices : LiveData<List<Advice>> = _advices

    private val _status = MutableLiveData<FireStoreStatus>()
    val status : LiveData<FireStoreStatus> = _status



    init {
        viewModelScope.launch{
            _status.value = FireStoreStatus.LOADING
            _advices.value = AdvicesServices.getAllAdvices()
            _status.value = FireStoreStatus.DONE
        }
    }

    fun addAdvice(advice:Advice){
        viewModelScope.launch {
            AdvicesServices.deleteAdvice("2")
        }
    }
}