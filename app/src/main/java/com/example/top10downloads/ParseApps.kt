package com.example.top10downloads

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory

class ParseApps {
    private val TAG= "ParseApps"
    val applications= ArrayList<FeedEntry>()

    fun parse(xmlData: String): Boolean{
        var status = true
        var inEntry = false
        var textValue = ""

        try{
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val xpp = factory.newPullParser()
            xpp.setInput(xmlData.reader())
            var eventType = xpp.eventType
            var currentRecord = FeedEntry()
            while (eventType != XmlPullParser.END_DOCUMENT) {
                val tagName = xpp.name?.toLowerCase()
                when (eventType) {
                    XmlPullParser.START_TAG -> {
//                        Log.d(TAG, "parse: Starting tag for: $tagName")
                        if (tagName == "entry") {
                            inEntry = true
                        }
                    }
                    XmlPullParser.TEXT -> textValue= xpp.text
                    XmlPullParser.END_TAG -> {
//                        Log.d(TAG, "parse: ending tag for: $tagName")
                        if (inEntry) {
                            when (tagName) {
                                "entry" -> {
                                    applications.add(currentRecord)
                                    inEntry = false
                                    currentRecord = FeedEntry()
                                }
                                "name" -> currentRecord.name = textValue
                                "artist" -> currentRecord.artist = textValue
                                "releasedate" -> currentRecord.releaseDate = textValue
                                "summary" -> currentRecord.summary = textValue
                                "image" -> currentRecord.imgURL = textValue
                            }
                        }else{
                            if(tagName== "title"){
                                currentRecord.title= textValue
                            }
                        }
                    }
                }
                eventType = xpp.next()
            }
        }catch (e: Exception){
            e.printStackTrace()
            status = false
        }
        return status
    }

}