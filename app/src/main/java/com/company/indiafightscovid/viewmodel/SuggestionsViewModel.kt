package com.company.indiafightscovid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.indiafightscovid.model.daos.SuggestionsDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SuggestionsViewModel:ViewModel() {


    private val suggestionsDao=SuggestionsDao()
    private val _text = MutableLiveData<String>().apply {
        value = "This is suggestions Fragment"
    }
    val text: LiveData<String> = _text

    fun addPost(suggestions: String) {
        viewModelScope.launch(Dispatchers.Main) {
            suggestionsDao.addSuggestions(suggestions)
        }
    }
    fun updateSuggestionsLikes(suggestionsId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            suggestionsDao.updateLikes(suggestionsId)
        }
    }

    fun updateSuggestionsDislikes(suggestionsId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            suggestionsDao.updateDislikes(suggestionsId)
        }
    }
}