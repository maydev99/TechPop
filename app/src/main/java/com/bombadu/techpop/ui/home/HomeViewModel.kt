package com.bombadu.techpop.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bombadu.techpop.local.NewsEntity
import com.bombadu.techpop.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    repository: MainRepository) : ViewModel() {

    val newsArticles: LiveData<List<NewsEntity>> = repository.observeAllNewsData()

}