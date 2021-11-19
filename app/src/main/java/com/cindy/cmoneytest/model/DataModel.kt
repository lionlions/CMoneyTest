package com.cindy.cmoneytest.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DataModel(
    var description: String?,
    var copyright: String?,
    var title: String?,
    var url: String?,
    var apod_site: String?,
    var date: String?,
    var media_type: String?,
    var hdurl: String?
) : Parcelable
