package com.randy.bfaa2.viemodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.randy.bfaa2.model.GithubUser
import com.randy.bfaa2.retrofit.UserRetrofit
import com.randy.bfaa2.utils.Resource

class DetailViewModel : ViewModel() {

    private val username: MutableLiveData<String> = MutableLiveData()

    val dataDetail: LiveData<Resource<GithubUser>> = Transformations
        .switchMap(username) {
            UserRetrofit.getDetailUser(it)
        }

    fun setDetail(userid: String) {
        if (username.value == userid) {
            return
        }
        username.value = userid
    }
}