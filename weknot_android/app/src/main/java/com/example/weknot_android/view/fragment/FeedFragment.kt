package com.example.weknot_android.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.weknot_android.BR
import com.example.weknot_android.R
import com.example.weknot_android.base.fragment.BaseListFragment
import com.example.weknot_android.databinding.FeedFragmentBinding
import com.example.weknot_android.util.Constants
import com.example.weknot_android.view.activity.FeedWriteActivity
import com.example.weknot_android.view.activity.PictureActivity
import com.example.weknot_android.view.activity.ProfileActivity
import com.example.weknot_android.viewmodel.FeedViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FeedFragment : BaseListFragment<FeedFragmentBinding, FeedViewModel>() ,SwipeRefreshLayout.OnRefreshListener {

    override fun getLayoutId(): Int {
        return R.layout.feed_fragment
    }

    override fun getViewModel(): Class<FeedViewModel> {
        return FeedViewModel::class.java
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun initObserver() {
        with(viewModel) {
            openFeedWrite.observe(this@FeedFragment, Observer {
                startActivityWithFinish(FeedWriteActivity::class.java)
            })

            onErrorEvent.observe(this@FeedFragment, Observer {
                simpleToast(it.message)
            })

            with(feedAdapter) {
                likeEvent.observe(this@FeedFragment, Observer {
                    feedId.value = it
                    postFeedLike()
                })

                openProfile.observe(this@FeedFragment, Observer {
                    val intent = Intent(context, ProfileActivity::class.java)
                    intent.putExtra("id", it)
                    startActivity(intent)
                })

                openPicture.observe(this@FeedFragment, Observer {
                    val intent = Intent(context, PictureActivity::class.java)
                    intent.putExtra("url", Constants.MAIN_HOST + "/image/" + it)
                    startActivity(intent)
                })
            }
        }
    }

    override fun setBtn(): FloatingActionButton {
        return binding.writeBtn
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp()
    }

    override fun onResume() {
        super.onResume()

        viewModel.getFeeds()
    }

    override fun onRefresh() {
        viewModel.getFeeds()
        binding.swipeRefreshLayout.isRefreshing = false
    }


    private fun setUp() {
        binding.feedRecyclerview.addOnScrollListener(scrollListener)

        binding.swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent)
        binding.swipeRefreshLayout.setOnRefreshListener(this)
    }
}