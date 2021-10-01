package com.company.indiafightscovid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.indiafightscovid.model.daos.AllIndiaDao
import com.company.indiafightscovid.model.daos.CitiesDao
import com.company.indiafightscovid.model.entities.AllIndiaData
import com.company.indiafightscovid.model.entities.CitiesContainer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {

    private var _allIndiaData=MutableLiveData<AllIndiaData>()
    val allIndiaData:LiveData<AllIndiaData> get()=_allIndiaData

    private var _progressBarChecker=MutableLiveData(false)
       val progressBarChecker:LiveData<Boolean> get() = _progressBarChecker
    private val citiesDao=CitiesDao()
    private val allIndiaDao=AllIndiaDao()


    private var _citiesListAndData= MutableLiveData<ArrayList<CitiesContainer>>()
    val citiesListAndData:LiveData<ArrayList<CitiesContainer>> get() = _citiesListAndData

    init {
        getAllIndiaDataFromFirestore()
    }

     fun refreshCitiesDataFromFirestore(){
         _progressBarChecker.value=true
           viewModelScope.launch(Dispatchers.IO) {
               val cities=citiesDao.getCitiesDataFromFirestore()
               withContext(Dispatchers.Main){
                   _citiesListAndData.value=cities
                   _progressBarChecker.value=false
               }
           }
    }

    private fun getAllIndiaDataFromFirestore(){
        _progressBarChecker.value=true
        viewModelScope.launch(Dispatchers.IO) {
            val allIndiaData=allIndiaDao.getAllIndiaData().toObject(AllIndiaData::class.java)
            withContext(Dispatchers.Main){
                _allIndiaData.value=allIndiaData!!
                _progressBarChecker.value=false
            }
        }
    }


}
