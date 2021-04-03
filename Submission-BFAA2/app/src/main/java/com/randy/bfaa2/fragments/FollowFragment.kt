package com.randy.bfaa2.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.randy.bfaa2.R
import com.randy.bfaa2.adapter.UserAdapter
import com.randy.bfaa2.databinding.FollowFragmentBinding
import com.randy.bfaa2.utils.ShowState
import com.randy.bfaa2.utils.State
import com.randy.bfaa2.utils.TypeView
import com.randy.bfaa2.viemodels.FollowViewModel

class FollowFragment : Fragment() {

    companion object {
        fun newInstance(username: String, type: String) =
            FollowFragment().apply {
                arguments = Bundle().apply {
                    putString(USERNAME, username)
                    putString(TYPE, type)
                }
            }
        const val stateFollowId = 2
        private const val TYPE = "type"
        private const val USERNAME = "username"
    }

    private lateinit var followBinding: FollowFragmentBinding
    private lateinit var usersAdapter: UserAdapter
    private lateinit var followViewModel: FollowViewModel
    private lateinit var username: String
    private var type: String? = null
    private var showState = ShowState(stateFollowId)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            username = it.getString(USERNAME).toString()
            type = it.getString(TYPE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        followBinding = FollowFragmentBinding.inflate(layoutInflater, container, false)
        return followBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        usersAdapter = UserAdapter(arrayListOf()) { user, _ ->
            Toast.makeText(context, user, Toast.LENGTH_SHORT).show()
        }

        followBinding.recylerFollow.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = usersAdapter
        }

        followViewModel = ViewModelProvider(
            this, ViewModelProvider.NewInstanceFactory()
        ).get(FollowViewModel::class.java)

        when (type) {
            resources.getString(R.string.following) -> followViewModel.setFollow(username, TypeView.FOLLOWING)
            resources.getString(R.string.followers) -> followViewModel.setFollow(username, TypeView.FOLLOWER)
            else -> showState.error(null, followBinding, null, resources)
        }
        observeFollow()
    }

    private fun observeFollow() {
        followViewModel.dataFollow.observe(viewLifecycleOwner, Observer {
            when (it.state) {
                State.SUCCESS ->
                    if (!it.data.isNullOrEmpty()) {
                        showState.success(null, followBinding)
                        usersAdapter.run { setData(it.data) }
                    } else {
                        showState.error(null,
                            followBinding,
                            resources.getString(R.string.not_have, username, type),
                            resources)
                    }
                State.LOADING -> showState.loading(null, followBinding)
                State.ERROR -> showState.error(null, followBinding, it.message, resources)
            }
        })
    }
}