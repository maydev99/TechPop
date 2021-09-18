package com.bombadu.techpop.ui.detail

import androidx.lifecycle.ViewModel
import com.bombadu.techpop.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    val repository: MainRepository,

) : ViewModel() {


    /*fun insertSavedArticle(savedEntity: SavedEntity) {
        viewModelScope.launch {
            repository.insertSavedArticle(savedEntity)
        }
    }*/
}