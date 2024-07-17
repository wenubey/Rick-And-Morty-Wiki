package com.wenubey.rickandmortywiki.ui.widget

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.StrictMode
import android.util.Log
import java.io.IOException
import java.io.InputStream
import java.net.URL

class ImageLoader {
    private var bitmap: Bitmap? = null

    companion object {
        private val instance: ImageLoader by lazy { ImageLoader() }

        fun get(): ImageLoader {
            return instance
        }
    }

    fun load(imageUrl: String): ImageLoader {

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val inputStream: InputStream
        try {
            inputStream = URL(imageUrl).openStream()
            bitmap = BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            Log.e("TAG", "load:ERROR ", e)
        }
        return this

    }

    fun bitmap(): Bitmap? = bitmap

}