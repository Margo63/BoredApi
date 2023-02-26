package com.example.boredapi

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel:ViewModel() {
    var activity by mutableStateOf("")

    fun getActivity(){
        viewModelScope.launch {
            try{
                val apiService = RetrofitInterface.api
                activity = apiService.getActivity().activity
            }catch(e:Exception){
                Log.d("ERRORRET",e.message.toString())
            }
        }
    }
}