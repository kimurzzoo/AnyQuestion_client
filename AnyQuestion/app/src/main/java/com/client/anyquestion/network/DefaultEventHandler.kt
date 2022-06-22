package com.client.anyquestion.network

import android.util.Log
import com.launchdarkly.eventsource.*

interface DefaultEventHandler : EventHandler {

    @Throws(Exception::class)
    override fun onOpen() {
        Log.i("open","open")
    }

    @Throws(Exception::class)
    override fun onClosed() {
        Log.i("close","close")
    }

    @Throws(Exception::class)
    override fun onMessage(event: String, messageEvent: MessageEvent) {
        Log.i("event", messageEvent.data)
    }

    override fun onError(t: Throwable) {
        Log.e("error", t.toString())
    }

    override fun onComment(comment: String) {
        Log.i("event", comment)
    }
}