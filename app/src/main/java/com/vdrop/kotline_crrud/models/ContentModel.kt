package com.vdrop.kotline_crrud.models

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.vdrop.kotline_crrud.utiles.Converter

@Entity(tableName = "kotline_details_table")
data class ContentModel(
    
    @ColumnInfo(name = "content_image")
    @TypeConverters(Converter::class)
    var image: SaveImage,

    @NonNull
    @ColumnInfo(name = "content_name")
    var name: String,

    @ColumnInfo(name = "content_shortDesc")
    var shortDesc: String,

    @ColumnInfo(name = "content_shortLongDesc")
    var longDesc: String,

    @NonNull
    @ColumnInfo(name = "content_amount")
    var amount: String,

    @NonNull
    @ColumnInfo(name = "content_quantity")
    var quantity: String
){

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "content_id")
    var id:Int = 0

    
    
    


}