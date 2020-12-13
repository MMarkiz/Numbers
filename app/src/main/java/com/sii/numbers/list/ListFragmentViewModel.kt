package com.sii.numbers.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sii.numbers.data.Model

class ListFragmentViewModel : ViewModel(){

    val dataModels = MutableLiveData<List<Model>>()

    val selectedItem = MutableLiveData<Model>()
}