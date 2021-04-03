package com.randy.bfaa2.retrofit

import android.os.Parcelable
import com.randy.bfaa2.model.GithubUser
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchRespond(
    val total_count : String,
    val incomplete_results: Boolean? = null,
    val items : List<GithubUser>
): Parcelable