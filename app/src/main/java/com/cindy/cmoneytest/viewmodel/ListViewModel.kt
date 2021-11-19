package com.cindy.cmoneytest.viewmodel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cindy.cmoneytest.R
import com.cindy.cmoneytest.api.ApiCallBack
import com.cindy.cmoneytest.api.ApiRepository
import com.cindy.cmoneytest.model.DataModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONTokener
import java.net.URL
import java.net.URLConnection

class ListViewModel(private val mRepository: ApiRepository?) : ViewModel() {

    private val TAG: String = javaClass.simpleName

    //LiveData
    var mDataModelListLiveData: MutableLiveData<List<DataModel>> = MutableLiveData()
    var mBitmapLiveData: MutableLiveData<Int> = MutableLiveData()
    var mErrorMessage: MutableLiveData<Int> = MutableLiveData()
    var isLoading: MutableLiveData<Boolean> = MutableLiveData()

    //Event
    private val _navigateToDetailFragment = MutableLiveData<Event<DataModel>>()

    val navigateToDetailFragment: LiveData<Event<DataModel>>
        get() = _navigateToDetailFragment

    lateinit var mBitmapList: MutableList<Bitmap?>
    var isScrollIdol: Boolean = true

    init {
        getListData()
    }

    private fun getListData() {
        isLoading.value = true
        mRepository?.getListData(object : ApiCallBack {
            override fun onGetListDataDone(stringResponse: String?) {
                Log.v(TAG, "=== onGetListDataDone ===")
                if (stringResponse != null) {
                    val dataModelList = parserData(stringResponse)
                    if(!dataModelList.isNullOrEmpty()){
                        mDataModelListLiveData.value = dataModelList
                        mBitmapList = MutableList<Bitmap?>(dataModelList.size){null}
                        isLoading.value = false
                    }
                } else {
                    //TODO Error handle
                    mErrorMessage.value = R.string.list_page_error_message
                    isLoading.value = false
                }
            }

            override fun onApiFailed(errorMessage: String?) {
                //TODO Error handle
                mErrorMessage.value = R.string.list_page_error_message
                isLoading.value = false
            }
        })
    }

    private fun parserData(stringResponse: String): List<DataModel> {
        val list: MutableList<DataModel> = mutableListOf()
        val json = JSONTokener(stringResponse).nextValue()
        if (json is JSONArray) {
            for (index in 0 until json.length()) {
                val data = json.optJSONObject(index)
                if (data != null) {
                    val dataModel = DataModel(
                        data.optString("description"),
                        data.optString("copyright"),
                        data.optString("title"),
                        data.optString("url"),
                        data.optString("apod_site"),
                        data.optString("date"),
                        data.optString("media_type"),
                        data.optString("hdurl")
                    )
                    list.add(dataModel)
                }
            }
        }
        return list.toList()
    }

    fun getImageFromInternet(index: Int, url: String){
        Log.v(TAG, "=== getImageFromInternet === index: $index")
        CoroutineScope(Dispatchers.IO).launch {
            val bitmap: Bitmap? = downloadBitmap(url)
            if(bitmap!=null){
                mBitmapList.add(index, bitmap)
                withContext(Dispatchers.Main){
                    mBitmapLiveData.value = index
                }
            }
        }
    }

    private fun downloadBitmap(url: String): Bitmap?{
        Log.v(TAG, "=== downloadBitmap ===")
        return try{
            val connection: URLConnection = URL(url).openConnection()
            connection.connect()
            val inputStream = connection.getInputStream()
            val options = BitmapFactory.Options()
            options.inPreferredConfig = Bitmap.Config.RGB_565
            val bitmap: Bitmap? = BitmapFactory.decodeStream(inputStream, null, options)
            inputStream.close()
            bitmap
        }catch (e: Exception){
            Log.e(TAG, "Exception: $e")
            null
        }
    }

    fun onItemClick(dataModel: DataModel){
        Log.v(TAG, "=== onItemClick ===")
        _navigateToDetailFragment.value = Event(dataModel)
    }

}