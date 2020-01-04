package com.vdrop.kotline_crrud.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.vdrop.kotline_crrud.models.ContentModel

@Dao
interface ContentDao {

    @Query("SELECT * from kotline_details_table")
    fun getAllItems(): LiveData<List<ContentModel>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertItem(model: ContentModel)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateItem(model: ContentModel)

    @Query("DELETE from kotline_details_table WHERE content_id = :id")
    suspend fun deleteItem(id: Int)

    @Query("SELECT * from kotline_details_table where content_id= :id")
    fun getParicularItem(id: Int): LiveData<ContentModel>


}