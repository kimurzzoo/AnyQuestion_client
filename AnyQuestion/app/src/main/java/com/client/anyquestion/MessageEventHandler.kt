package com.client.anyquestion

import android.util.Log
import com.client.anyquestion.databinding.ActivitySpeecherBinding
import com.launchdarkly.eventsource.MessageEvent


class MessageEventHandler(val act : SpeecherActivity, val binding : ActivitySpeecherBinding) : DefaultEventHandler {
    override fun onMessage(event: String, messageEvent: MessageEvent) {
        super.onMessage(event, messageEvent)
        val data = messageEvent.data
        Log.d("kimurzzoo", "message : " + data)
        val dataParsing = data.split(":")

        if(data.equals("no"))
        {
            Log.d("kimurzzoo", "no here")
            act.runOnUiThread(Runnable {
                binding.questionerName.text = ""
                binding.questionerNumber.text = "Waiting..."
            })
        }
        else if(dataParsing[0].equals("next"))
        {
            Log.d("kimurzzoo", "next here")

            act.runOnUiThread(Runnable {
                binding.questionerName.text = dataParsing[1]
                binding.questionerNumber.text = dataParsing[2]
            })
        }
        else if(dataParsing[0].equals("password"))
        {
            Log.d("kimurzzoo", "password here")
            act.runOnUiThread(Runnable {
                binding.roomPassword.text = dataParsing[1]
            })
        }
    }
}