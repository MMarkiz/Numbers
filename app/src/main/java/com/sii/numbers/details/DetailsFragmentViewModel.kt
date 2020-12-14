package com.sii.numbers.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sii.numbers.data.ModelDetails

class DetailsFragmentViewModel : ViewModel(){

    val modelDetails = MutableLiveData<ModelDetails>()

    var wasLastTimeTabletLandscape: Boolean? = null
}