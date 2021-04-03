package com.randy.bfaa2.viemodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.randy.bfaa2.model.GithubUser
import com.randy.bfaa2.retrofit.UserRetrofit
import com.randy.bfaa2.utils.Resource
import com.randy.bfaa2.utils.TypeView

class FollowViewModel : ViewModel() {
    private val username: MutableLiveData<String> = MutableLiveData()

    private lateinit var type: TypeView

    val dataFollow: LiveData<Resource<List<GithubUser>>> = Transformations
        .switchMap(username) {
            when (type) {
                TypeView.FOLLOWER -> {
                    UserRetrofit.getFollowers(it)
                }
                TypeView.FOLLOWING -> {
                    UserRetrofit.getFollowing(it)
                }
            }
        }

    fun setFollow(user: String, typeView: TypeView) {
        if (username.value == user) {
            return
        }
        username.value = user
        type = typeView
    }
}