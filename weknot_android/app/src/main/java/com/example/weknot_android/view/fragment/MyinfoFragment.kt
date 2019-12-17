package com.example.weknot_android.view.fragment

import android.content.Intent

import android.os.Bundle

import android.view.View

import androidx.lifecycle.Observer

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.example.weknot_android.BR
import com.example.weknot_android.R
import com.example.weknot_android.base.fragment.BaseFragment
import com.example.weknot_android.databinding.MyinfoFragmentBinding
import com.example.weknot_android.util.Constants
import com.example.weknot_android.view.activity.PictureActivity
import com.example.weknot_android.view.activity.ProfileActivity
import com.example.weknot_android.viewmodel.MyinfoViewModel

class MyinfoFragment : BaseFragment<MyinfoFragmentBinding, MyinfoViewModel>() , SwipeRefreshLayout.OnRefreshListener {

    override fun getLayoutId(): Int {
        return R.layout.myinfo_fragment
    }

    override fun getViewModel(): Class<MyinfoViewModel> {
        return MyinfoViewModel::class.java
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun initObserver() {
        with(viewModel) {

            onErrorEvent.observe(this@MyinfoFragment, Observer {
                simpleToast(it.message)
            })

            openPictureEvent.observe(this@MyinfoFragment, Observer {
                val intent = Intent(context, PictureActivity::class.java)
                intent.putExtra("url", it)
                startActivity(intent)
            })

            with(feedAdapter) {

                likeEvent.observe(this@MyinfoFragment, Observer {
                    feedId.value = it
                    postFeedLike()
                })

                openProfile.observe(this@MyinfoFragment, Observer {
                    val intent = Intent(context, ProfileActivity::class.java)
                    intent.putExtra("id", it)
                    startActivity(intent)
                })

                openPicture.observe(this@MyinfoFragment, Observer {
                    val intent = Intent(context, PictureActivity::class.java)
                    intent.putExtra("url", Constants.MAIN_HOST + "/image/" + it)
                    startActivity(intent)
                })
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp()
    }

    override fun onResume() {
        super.onResume()
        viewModel.setUp()
    }

    override fun onRefresh() {
        viewModel.setUp()
        binding.swipeRefreshLayout.isRefreshing = false
    }

    private fun setUp() {
        viewModel.id.value = viewModel.getMyId()

        binding.swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent)
        binding.swipeRefreshLayout.setOnRefreshListener(this)
    }
}