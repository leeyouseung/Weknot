package com.example.weknot_android.view.fragment

import android.content.Intent

import androidx.lifecycle.Observer

import com.example.weknot_android.BR
import com.example.weknot_android.R
import com.example.weknot_android.base.fragment.BaseFragment
import com.example.weknot_android.databinding.SocialFragmentBinding
import com.example.weknot_android.view.activity.PrivateChatActivity
import com.example.weknot_android.view.activity.ProfileActivity
import com.example.weknot_android.viewmodel.SocialViewModel

class SocialFragment : BaseFragment<SocialFragmentBinding, SocialViewModel>() {

    override fun getLayoutId(): Int {
        return R.layout.social_fragment
    }

    override fun getViewModel(): Class<SocialViewModel> {
        return SocialViewModel::class.java
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun initObserver() {
        with(viewModel) {

            onErrorEvent.observe(this@SocialFragment, Observer {
                simpleToast(it.message)
            })

            with(receiveAdapter) {
                checkFriendEvent.observe(this@SocialFragment, Observer {
                    friendRequest.value = it
                    putFriend()
                })

                openChatRoom.observe(this@SocialFragment, Observer {
                    val intent = Intent(context, PrivateChatActivity::class.java)
                    intent.putExtra("id", it)
                    startActivity(intent)
                })

                openProfile.observe(this@SocialFragment, Observer {
                    val intent = Intent(context, ProfileActivity::class.java)
                    intent.putExtra("id", it)
                    startActivity(intent)
                })
            }

            with(friendAdapter) {

                openChatRoom.observe(this@SocialFragment, Observer {
                    val intent = Intent(context, PrivateChatActivity::class.java)
                    intent.putExtra("id", it)
                    startActivity(intent)
                })

                openProfile.observe(this@SocialFragment, Observer {
                    val intent = Intent(context, ProfileActivity::class.java)
                    intent.putExtra("id", it)
                    startActivity(intent)
                })
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.setUp()
    }
}