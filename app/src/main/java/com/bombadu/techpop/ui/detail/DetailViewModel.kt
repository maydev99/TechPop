package com.bombadu.techpop.ui.detail

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.Settings.Global.getString
import androidx.core.content.ContextCompat.startActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bombadu.techpop.R
import com.bombadu.techpop.TechPopApplication
import com.bombadu.techpop.local.SavedEntity
import com.bombadu.techpop.repository.MainRepository
import com.bombadu.techpop.util.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.internal.aggregatedroot.codegen._com_bombadu_techpop_TechPopApplication
import kotlinx.coroutines.launch
import java.net.MalformedURLException
import java.net.URISyntaxException
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    val repository: MainRepository,

) : ViewModel() {


    fun insertSavedArticle(savedEntity: SavedEntity) {
        viewModelScope.launch {
            repository.insertSavedArticle(savedEntity)
        }
    }
}