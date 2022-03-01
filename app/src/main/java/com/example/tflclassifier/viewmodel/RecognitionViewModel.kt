package com.example.tflclassifier.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class RecognitionViewModel : ViewModel() {

    private var _recognition = MutableLiveData<String>()
    val recognition: LiveData<String> get() = _recognition

    fun updateData(recognition: Recognition) {
        _recognition.value = recognition.label
    }
}

/**
 * Simple Data object with two fields for the label and probability
 */
data class Recognition(val label:String)