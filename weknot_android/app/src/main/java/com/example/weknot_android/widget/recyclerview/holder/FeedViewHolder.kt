package com.example.weknot_android.widget.recyclerview.holder

import android.annotation.SuppressLint

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils

import com.example.weknot_android.R
import com.example.weknot_android.base.BaseViewHolder
import com.example.weknot_android.databinding.FeedItemBinding
import com.example.weknot_android.model.feed.Feed
import com.example.weknot_android.widget.recyclerview.navigator.feed.FeedAdapterNavigator
import com.example.weknot_android.widget.recyclerview.navigator.feed.FeedItemNavigator
import com.example.weknot_android.widget.recyclerview.viewmodel.FeedItemViewModel

class FeedViewHolder(val binding: FeedItemBinding) : BaseViewHolder<FeedAdapterNavigator>(binding.root), FeedItemNavigator {

    private val viewModel = FeedItemViewModel()
    private lateinit var feed: Feed

    override fun likeOnEvent() {
        binding.likeOnAnimation.startAnimation(animLikeOffFirst)
    }

    override fun likeOffEvent() {
        binding.likeOffAnimation.startAnimation(animLikeOnFirst)
    }

    override fun openProfile() {
        getNavigator().openProfile(feed.writer)
    }

    override fun openPicture() {
        getNavigator().openPicture(feed.picture)
    }

    fun bind(data: Feed) {
        viewModel.bind(data)
        feed = data
        viewModel.setNavigator(this)
        setUp()
        binding.viewModel = viewModel
        binding.likeOnBtn.visibility = View.INVISIBLE
        binding.likeOffBtn.visibility = View.INVISIBLE
    }

    private fun setUp() {
        setUpLikeOnListener()
        setUpLikeOffListener()
    }

    private val animLikeOnFirst : Animation = AnimationUtils.loadAnimation(binding.root.context, R.anim.animation_like_on_first)
    private val animLikeOnSecond : Animation = AnimationUtils.loadAnimation(binding.root.context, R.anim.animation_like_on_second)
    private val animLikeOffFirst : Animation = AnimationUtils.loadAnimation(binding.root.context, R.anim.animation_like_off_first)
    private val animLikeOffSecond : Animation = AnimationUtils.loadAnimation(binding.root.context, R.anim.animation_like_off_second)

    private fun setUpLikeOnListener() {
        animLikeOnFirst.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {}
            override fun onAnimationEnd(p0: Animation?) {
                binding.likeOnAnimation.startAnimation(animLikeOnSecond)
            }
            override fun onAnimationStart(p0: Animation?) {
                binding.likeOffBtn.visibility = View.INVISIBLE
            }
        })
        animLikeOnSecond.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {}
            @SuppressLint("SetTextI18n")
            override fun onAnimationEnd(p0: Animation?) {
                binding.likeOnBtn.visibility = View.VISIBLE
                binding.likeLeftCount.text = (viewModel.feed.value!!.likeCount + 1).toString() + "개"
                getNavigator().like(viewModel.feed.value!!)
            }
            override fun onAnimationStart(p0: Animation?) {}
        })
    }

    private fun setUpLikeOffListener() {
        animLikeOffFirst.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {}
            override fun onAnimationEnd(p0: Animation?) {
                binding.likeOffAnimation.startAnimation(animLikeOffSecond)
            }

            override fun onAnimationStart(p0: Animation?) {
                binding.likeOnBtn.visibility = View.INVISIBLE
            }
        })
        animLikeOffSecond.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {}
            @SuppressLint("SetTextI18n")
            override fun onAnimationEnd(p0: Animation?) {
                binding.likeOffBtn.visibility = View.VISIBLE
                binding.likeRightCount.text = (viewModel.feed.value!!.likeCount - 1).toString() + "개"
                getNavigator().like(viewModel.feed.value!!)
            }

            override fun onAnimationStart(p0: Animation?) {}
        })
    }
}