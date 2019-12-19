package com.example.weknot_android.widget.recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.databinding.DataBindingUtil

import androidx.recyclerview.widget.RecyclerView.Adapter

import com.example.weknot_android.R
import com.example.weknot_android.model.feed.Feed
import com.example.weknot_android.widget.SingleLiveEvent
import com.example.weknot_android.widget.recyclerview.holder.FeedViewHolder
import com.example.weknot_android.widget.recyclerview.navigator.feed.FeedAdapterNavigator

import kotlin.collections.ArrayList

class FeedAdapter : Adapter<FeedViewHolder>(), FeedAdapterNavigator {

    private lateinit var feeds: List<Feed>

    val likeEvent = SingleLiveEvent<Int>()
    val openProfile = SingleLiveEvent<String>()
    val openPicture = SingleLiveEvent<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        return FeedViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.feed_item, parent, false))
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        holder.setNavigator(this)
        holder.bind(feeds[position])
    }

    override fun like(feed: Feed) {
        val position = feeds.indexOf(feed)
        
        feed.like = if (feed.like == 1) 0 else 1
        feed.likeCount = if (feed.like == 1) feed.likeCount + 1 else feed.likeCount - 1
        (feeds as ArrayList<Feed>)[position] = feed

        likeEvent.value = feed.feedId
    }

    override fun openProfile(id: String) {
        openProfile.value = id
    }

    override fun openPicture(url: String) {
        openPicture.value = url
    }

    fun updateList(feeds: List<Feed>?) {
        this.feeds = feeds!!
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if(::feeds.isInitialized) feeds.size else 0
    }
}
