package com.example.top10downloads

import android.os.AsyncTask
import android.util.Log
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

private const val TAG= "DownloadData"


 class DownloadData(private val callback: DownloaderCallback): AsyncTask<String, Void, String>(){

     interface DownloaderCallback{
         fun onDataAvailable(data: List<FeedEntry>)
     }

    override fun doInBackground(vararg url: String): String {
        val rssFeed= downloadXML(url[0])
        if(rssFeed.isEmpty()){
            Log.e(TAG, "doIn: Error downloading")
        }
        return rssFeed
    }

    override fun onPostExecute(result: String) {
        val parseApps= ParseApps()
        if(result.isNotEmpty()){
            parseApps.parse(result)
        }
        callback.onDataAvailable(parseApps.applications)
    }

    private fun downloadXML(urlPath: String):String{
        try {
            return URL(urlPath).readText()
        } catch(e: MalformedURLException){
            Log.d(TAG, "Invalid XML: ${e.message}")
        } catch (e: IOException){
            Log.d(TAG, "IO exception: ${e.message}")
        }
        return ""
    }


}