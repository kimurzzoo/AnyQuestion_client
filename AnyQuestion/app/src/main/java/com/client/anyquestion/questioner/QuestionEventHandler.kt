package com.client.anyquestion.questioner

import android.util.Log
import android.view.View
import com.client.anyquestion.R
import com.client.anyquestion.databinding.ActivityQuestionerBinding
import com.client.anyquestion.network.DefaultEventHandler
import com.launchdarkly.eventsource.MessageEvent

class QuestionEventHandler(val act : QuestionerActivity, val binding : ActivityQuestionerBinding) :
    DefaultEventHandler {
    override fun onMessage(event: String, messageEvent: MessageEvent) {
        super.onMessage(event, messageEvent)
        val data = messageEvent.data
        Log.d("kimurzzoo", "questionevent : " + data)

        if(data.equals("your turn"))
        {
            Log.d("kimurzzoo", "its my turn")
            act.runOnUiThread(Runnable {
                binding.questionerMeButton.setBackgroundColor(R.drawable.green_button)
            })
        }
        else if(data.equals("your question is ended"))
        {
            Log.d("kimurzzoo", "my turn ended")
            act.runOnUiThread(Runnable {
                binding.questionerMeButton.setBackgroundColor(R.drawable.yellow_button)
                binding.questionerMeButton.text="Me!"
                binding.questionerMeButton.isEnabled=true
            })

            act.questionEventSource?.close()
        }
        else if(data.equals("group deleted"))
        {
            Log.d("kimurzzoo", "group already deleted")
            act.runOnUiThread(Runnable {
                binding.questionerMeButton.setBackgroundColor(R.drawable.yellow_button)
                binding.questionerMeButton.text="Me!"
                binding.questionerMeButton.isEnabled=false
                binding.questionerMeButton.visibility= View.INVISIBLE

                binding.questionerMeoutButton.isEnabled=false
                binding.questionerMeoutButton.visibility=View.INVISIBLE

                binding.questionerGroupSearchButton.isEnabled=true
                binding.questionerGroupSearchButton.visibility=View.INVISIBLE

                binding.questionerPasswordText.isEnabled=true
            })

            act.questionEventSource?.close()
        }
        else
        {
            binding.questionerMeButton.setText(data)
        }
    }
}