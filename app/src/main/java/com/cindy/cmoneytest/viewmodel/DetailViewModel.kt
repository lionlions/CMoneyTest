package com.cindy.cmoneytest.viewmodel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL
import java.net.URLConnection

class DetailViewModel : ViewModel() {

    private val TAG: String = javaClass.simpleName

    var mBitmapLiveData: MutableLiveData<Bitmap?> = MutableLiveData()

    fun getImageFromInternet(url: String) {
        Log.v(TAG, "=== getImageFromInternet ===")
        CoroutineScope(Dispatchers.IO).launch {
            val bitmap: Bitmap? = downloadBitmap(url)
            withContext(Dispatchers.Main) {
                mBitmapLiveData.value = bitmap
            }
        }
    }

    private fun downloadBitmap(url: String): Bitmap? {
        Log.v(TAG, "=== downloadBitmap ===")
        return try {
            val connection: URLConnection = URL(url).openConnection()
            connection.connect()
            val inputStream = connection.getInputStream()
            val options = BitmapFactory.Options()
            options.inPreferredConfig = Bitmap.Config.RGB_565
            options.inSampleSize = 4
            val bitmap: Bitmap? = BitmapFactory.decodeStream(inputStream, null, options)
            inputStream.close()
            bitmap
        } catch (e: Exception) {
            Log.e(TAG, "Exception: $e")
            null
        }
    }

}