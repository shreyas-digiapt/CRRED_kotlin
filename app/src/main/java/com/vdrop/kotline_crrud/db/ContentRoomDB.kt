package com.vdrop.kotline_crrud.db

import android.content.Context
import androidx.room.*
import com.vdrop.kotline_crrud.models.ContentModel
import com.vdrop.kotline_crrud.utiles.Converter

@Database(entities = arrayOf(ContentModel::class), version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class ContentRoomDB : RoomDatabase() {

    abstract fun contentDao(): ContentDao

    companion object {

        private var INSTANCE: ContentRoomDB? = null

        fun getDataBase(context: Context): ContentRoomDB {
            val tempINSTANCE = INSTANCE
            if (tempINSTANCE != null) {
                return tempINSTANCE
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext,
                    ContentRoomDB::class.java,
                    "kotline_details_table").build()
                INSTANCE = instance
                return instance
            }
        }
    }

}