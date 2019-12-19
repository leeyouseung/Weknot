package com.example.weknot_android.view.activity

import android.content.Intent

import android.os.Bundle

import android.view.Menu
import android.view.MenuItem
import android.view.View

import androidx.lifecycle.Observer

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.example.weknot_android.BR
import com.example.weknot_android.R
import com.example.weknot_android.base.activity.BaseActivity
import com.example.weknot_android.databinding.ProfileActivityBinding
import com.example.weknot_android.util.Constants
import com.example.weknot_android.viewmodel.ProfileViewModel
import com.example.weknot_android.widget.extension.shortToast

class ProfileActivity : BaseActivity<ProfileActivityBinding, ProfileViewModel>() , SwipeRefreshLayout.OnRefreshListener {

    override val TAG: String
        get() = this.javaClass.name

    override fun getLayoutId(): Int {
        return R.layout.profile_activity
    }

    override fun getViewModel(): Class<ProfileViewModel> {
        return ProfileViewModel::class.java
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun initObserver() {
        with(viewModel) {

            onErrorEvent.observe(this@ProfileActivity, Observer {
                this@ProfileActivity.shortToast(it!!.message)
            })

            onRefreshEvent.observe(this@ProfileActivity, Observer {
                setUp()
            })

            openPictureEvent.observe(this@ProfileActivity, Observer {
                startActivity(Intent(this@ProfileActivity, PictureActivity::class.java)
                        .putExtra("url", it))
            })

            with(feedAdapter) {
                likeEvent.observe(this@ProfileActivity, Observer {
                    feedId.value = it
                    postFeedLike()
                })

                openProfile.observe(this@ProfileActivity, Observer {
                    startActivity(Intent(this@ProfileActivity, ProfileActivity::class.java)
                            .putExtra("id", it))
                })

                openPicture.observe(this@ProfileActivity, Observer {
                    startActivity(Intent(this@ProfileActivity, PictureActivity::class.java)
                            .putExtra("url", Constants.MAIN_HOST + "/image/" + it))
                })
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (supportActionBar != null) supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_profile, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onRefresh() {
        viewModel.setUp()
        binding.swipeRefreshLayout.isRefreshing = false
    }

    private fun setUp() {
        viewModel.id.value = intent.getStringExtra("id")
        viewModel.setUp()

        binding.swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent)
        binding.swipeRefreshLayout.setOnRefreshListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        else if (item.itemId == R.id.menu_profile) {
            when {
                binding.friendBtn.visibility == View.INVISIBLE -> this@ProfileActivity.shortToast(R.string.check_my_id_message)
                viewModel.userStatus.value!! == "친구" -> {
                    startActivity(Intent(this, PrivateChatActivity::class.java)
                            .putExtra("id", viewModel.id.value))
                }
                else -> this@ProfileActivity.shortToast(R.string.check_friend_message)
            }
            return true
        }
        return false
    }
}