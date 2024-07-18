package com.wenubey.rickandmortywiki.ui.widget

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.os.StrictMode
import android.util.Log
import java.io.IOException
import java.io.InputStream
import java.net.URL

class ImageLoader {
    var bitmap: Bitmap? = null
        private set

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

    fun clip(cornerRadius: Float): ImageLoader {
        bitmap?.let { originalBitmap ->
            val output = Bitmap.createBitmap(
                originalBitmap.width,
                originalBitmap.height,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(output)
            val paint = Paint()
            val rect = Rect(0, 0, originalBitmap.width, originalBitmap.height)
            val rectF = RectF(rect)
            paint.isAntiAlias = true
            canvas.drawARGB(0, 0, 0, 0)
            paint.color = Color.WHITE
            canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paint)

            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            canvas.drawBitmap(originalBitmap, rect, rect, paint)
            bitmap = output
        }
        return this
    }
}