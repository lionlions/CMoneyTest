package com.cindy.cmoneytest.api

interface ApiCallBack {
    fun onGetListDataDone(stringResponse: String?)
    fun onApiFailed(errorMessage: String?)
}