package fr.ensim.android.artgallery.ui.theme.db.typeconverters

import androidx.room.TypeConverter
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fr.ensim.android.artgallery.model.Artwork
import fr.ensim.android.artgallery.ui.theme.model.Artwork

class Converters {

    @TypeConverter
    fun fromStringList(value: List<String>?): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String>? {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArtworkList(value: List<Artwork>?): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toArtworkList(value: String): List<Artwork>? {
        val listType = object : TypeToken<List<Artwork>>() {}.type
        return Gson().fromJson(value, listType)
    }
}