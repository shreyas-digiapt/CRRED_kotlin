package com.vdrop.kotline_crrud.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.vdrop.kotline_crrud.db.ContentRoomDB
import com.vdrop.kotline_crrud.models.ContentModel
import com.vdrop.kotline_crrud.respository.ContentRepo
import kotlinx.coroutines.launch

class ContentViewModel(application: Application): AndroidViewModel(application) {

    private val repository: ContentRepo

    val getAllItems: LiveData<List<ContentModel>>

    init {
        val contentDAO = ContentRoomDB.getDataBase(application).contentDao()
        repository = ContentRepo(contentDAO)
        getAllItems = repository.getAllItems
    }

    fun insertItem(model: ContentModel) = viewModelScope.launch {
        repository.insertItem(model)
    }

    fun updateItem(model: ContentModel) = viewModelScope.launch {
        repository.updateItem(model)
    }

    fun deleteItem(id: Int) = viewModelScope.launch {
        repository.deleteItem(id)
    }

    fun getParicularItem(id: Int): LiveData<ContentModel> {
        return repository.getParicularItem(id)
    }
}