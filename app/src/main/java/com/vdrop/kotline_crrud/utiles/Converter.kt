package com.vdrop.kotline_crrud.utiles

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vdrop.kotline_crrud.models.SaveImage
import java.lang.reflect.Type

class Converter {

    @TypeConverter
    fun toSaveImage(jsonObject: String): SaveImage {
        val type = object : TypeToken<SaveImage>() {}.getType()
        return Gson().fromJson(jsonObject, type)
    }

    @TypeConverter
    fun toString(image: SaveImage): String {
        val type = object: TypeToken<SaveImage>() {}.type
        return Gson().toJson(image, type)
    }
}