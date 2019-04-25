package com.example.top10downloads

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*

class FeedEntry{
    var name: String= ""
    var artist: String= ""
    var releaseDate: String= ""
    var summary: String= ""
    var imgURL: String= ""
    var title: String= ""
}

private const val TAG = "MainActivity"
private const val STATE_URL= "feedUrl"
private const val STATE_LIMIT= "feedLimit"

class MainActivity : AppCompatActivity() {

    private var feedUrl: String= "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml"
    private var feedLimit= 10
    private val feedViewModel : FeedViewModel by lazy{ViewModelProviders.of(this).get(FeedViewModel :: class.java)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val feedAdapter= FeedAdapter(this, R.layout.list_record, EMPTY_FEED_LIST)
        xmlListView.adapter= feedAdapter

        if(savedInstanceState != null){
            feedUrl= savedInstanceState.getString(STATE_URL)!!
            feedLimit= savedInstanceState.getInt(STATE_LIMIT)

        }
        feedViewModel.feedEntries.observe(this,
            Observer<List<FeedEntry>> {feedEntries -> feedAdapter.setFeedList(feedEntries ?: EMPTY_FEED_LIST)})

        feedViewModel.downloadUrl(feedUrl.format(feedLimit))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.feeds_menu, menu)
        if(feedLimit == 10){
            menu?.findItem(R.id.mnu10)?.isChecked= true
        }else{
            menu?.findItem(R.id.mnu25)?.isChecked= true
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.mnuFree ->
                feedUrl= "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml"
            R.id.mnuPaid ->
                feedUrl= "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/toppaidapplications/limit=%d/xml"
            R.id.mnuSongs ->
                feedUrl= "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=%d/xml"
            R.id.mnuMovies ->
                feedUrl= "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topMovies/limit=%d/xml"
            R.id.mnuSeasons ->
                feedUrl= "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topTvSeasons/limit=%d/xml"
            R.id.mnu10, R.id.mnu25 -> {
                if(! item.isChecked){
                    item.isChecked= true
                    feedLimit= 35 - feedLimit
                }
            }
            R.id.mnuRefresh -> feedViewModel.invalidate()

            else ->
                return super.onOptionsItemSelected(item)
        }
        feedViewModel.downloadUrl(feedUrl.format(feedLimit))
        return true
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putString(STATE_URL, feedUrl)
        outState?.putInt(STATE_LIMIT, feedLimit)
    }
}
