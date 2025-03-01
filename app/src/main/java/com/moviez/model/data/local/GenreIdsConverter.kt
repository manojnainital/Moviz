package com.moviez.model.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class GenreIdsConverter {

    @TypeConverter
    fun fromGenreIds(genreIds: List<Int>): String {
        return Gson().toJson(genreIds)
    }

    @TypeConverter
    fun toGenreIds(genreIdsString: String): List<Int> {
        val type = object : TypeToken<List<Int>>() {}.type
        return Gson().fromJson(genreIdsString, type)
    }
}