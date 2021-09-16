package com.bombadu.techpop.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bombadu.techpop.local.NewsEntity
import com.bombadu.techpop.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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

    fun deleteOldData(){
        viewModelScope.launch {
            //repository.deleteOldLocalData()
            getNewsFromFirebase()
        }
    }


    //Checks if update is needed
    /*private fun checkApiUpdate() {
        viewModelScope.launch {
            try {

                repository.checkUpdate()
            } catch (e: Exception) {
                Log.e(TAG, "Update Check Failed")
            }

        }
    }*/

    fun getNewsFromFirebase() {
        viewModelScope.launch {
            repository.getNewsFromFirebase()
        }
    }

    companion object {
        const val TAG = "HomeViewModel"
    }
}