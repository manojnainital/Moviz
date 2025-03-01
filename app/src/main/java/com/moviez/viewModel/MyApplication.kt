package com.moviez.viewModel

import android.app.Application
import com.moviez.model.data.local.MovieDatabase

class MyApplication : Application() {
    val database: MovieDatabase by lazy { MovieDatabase.getDatabase(this) }
}