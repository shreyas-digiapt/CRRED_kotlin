package com.vdrop.kotline_crrud.respository

import androidx.lifecycle.LiveData
import com.vdrop.kotline_crrud.db.ContentDao
import com.vdrop.kotline_crrud.models.ContentModel

class ContentRepo(private val contentDao: ContentDao) {

    val getAllItems: LiveData<List<ContentModel>> = contentDao.getAllItems()

    suspend fun insertItem(model: ContentModel) {
        contentDao.insertItem(model)
    }

    suspend fun updateItem(model: ContentModel) {
        contentDao.updateItem(model)
    }

    suspend fun deleteItem(id: Int) {
        contentDao.deleteItem(id)
    }

//    suspend fun getParicularItem(id: Int): LiveData<ContentModel> {
//       return contentDao.getParicularItem(id)
//    }

    fun getParicularItem(id: Int): LiveData<ContentModel> {
        return contentDao.getParicularItem(id)
    }


}