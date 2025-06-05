package fr.ensim.android.testapp.utils

import android.content.Context
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import fr.ensim.android.testapp.model.ArtItem

fun loadJocondeData(context: Context): List<ArtItem> {
    val jsonString = context.assets.open("joconde.json").bufferedReader().use { it.readText() }
    val listType = object : TypeToken<List<ArtItem>>() {}.type
    return Gson().fromJson(jsonString, listType)
}
