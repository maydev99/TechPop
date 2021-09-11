package com.bombadu.techpop.ui.saved

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bombadu.techpop.local.SavedEntity
import com.bombadu.techpop.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedViewModel @Inject constructor(
    private val repository: MainRepository,
) : ViewModel() {

    val savedArticles: LiveData<List<SavedEntity>> = repository.observeAllSavedArticles()


    fun deleteSavedItem(savedEntity: SavedEntity) {
        viewModelScope.launch {
            repository.deleteSavedArticle(savedEntity)
        }
    }


}