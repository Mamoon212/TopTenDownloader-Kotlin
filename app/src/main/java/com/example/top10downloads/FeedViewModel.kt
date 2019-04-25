package com.example.top10downloads

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import java.util.*

private const val TAG= "FeedViewModel"
val EMPTY_FEED_LIST: List<FeedEntry> = Collections.emptyList()

class FeedViewModel : ViewModel(), DownloadData.DownloaderCallback {
    private var downloadData: DownloadData? = null
    private var feedChachedUrl= "invalidated"

    private val feed= MutableLiveData<List<FeedEntry>>()
    val feedEntries: LiveData<List<FeedEntry>>
    get() = feed

    init {
        feed.postValue(EMPTY_FEED_LIST)
    }

    fun downloadUrl(feedUrl: String){
        if(feedUrl!=feedChachedUrl) {
            downloadData = DownloadData(this)
            downloadData?.execute(feedUrl)
            feedChachedUrl= feedUrl
        }
    }

    fun invalidate(){
        feedChachedUrl = "INVALIDATE"
    }

    override fun onDataAvailable(data: List<FeedEntry>) {
        feed.value= data
    }

    override fun onCleared() {
        downloadData?.cancel(true)
    }
}