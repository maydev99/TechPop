package com.bombadu.techpop.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bombadu.techpop.local.NewsEntity
import com.bombadu.techpop.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MainRepository,
    ) : ViewModel() {

    val newsArticles: LiveData<List<NewsEntity>> = repository.observeAllNewsData()

    init {
        deleteOldData()
    }

    private fun deleteOldData(){
        viewModelScope.launch {
            //repository.deleteOldLocalData()
            getNewsFromFirebase()
        }
    }


    fun getNewsFromFirebase() {
        viewModelScope.launch {
            repository.getNewsFromFirebase()
        }
    }

}