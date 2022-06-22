package com.client.anyquestion.speecher

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.client.anyquestion.util.PreferenceManager
import com.client.anyquestion.R
import com.client.anyquestion.databinding.ActivitySpeecherBinding
import com.client.anyquestion.network.APIS
import com.launchdarkly.eventsource.EventSource
import okhttp3.Headers
import retrofit2.Call
import retrofit2.Response
import java.lang.Exception
import java.net.URI

class SpeecherActivity : AppCompatActivity() {
    var eventHandler : MessageEventHandler? = null
    val preferenceManager = PreferenceManager()
    val mContext = this
    val apis = APIS.create()
    val url = "http://192.168.0.9:8080/speecher/create"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySpeecherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.speecherLayout.visibility=View.GONE

        eventHandler = MessageEventHandler(this, binding)

        setSupportActionBar(binding.speecherToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.speecherToolbar.title="Speecher"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)

        var eventSource : EventSource? = null

        binding.createGroup.setOnClickListener {
            try {
                eventSource = EventSource.Builder(eventHandler, URI.create("http://192.168.0.9:8080/speecher/create"))
                    .headers(Headers.Builder().add("Authorization", preferenceManager.getString(mContext, "accessToken")!!).build())
                    .build()
                eventSource!!.start()
            }
            catch (e : Exception)
            {
                Log.e("kimurzzoo", e.toString())
            }

            binding.createGroup.isEnabled=false
            binding.createGroup.visibility=View.GONE
            binding.deleteGroup.isEnabled=true
            binding.deleteGroup.visibility=View.VISIBLE
            binding.speecherLayout.visibility=View.VISIBLE
        }

        binding.deleteGroup.setOnClickListener {
            eventSource!!.close()
            apis.get_speecher_delete(preferenceManager.getString(mContext, "accessToken")!!).enqueue(object :
                retrofit2.Callback<GroupDeleteResult> {
                override fun onResponse(call: Call<GroupDeleteResult>, response: Response<GroupDeleteResult>) {
                    if(response.body() != null)
                    {
                        if(response.body()!!.ok)
                        {
                            binding.createGroup.isEnabled=true
                            binding.createGroup.visibility=View.VISIBLE
                            binding.deleteGroup.isEnabled=false
                            binding.deleteGroup.visibility=View.GONE
                            binding.speecherLayout.visibility=View.GONE
                            binding.questionerNumber.text=""
                            binding.questionerName.text=""
                            binding.roomPassword.text=""
                            Toast.makeText(mContext, "group delete success", Toast.LENGTH_SHORT).show()
                            Log.d("kimurzzoo", "group delete success")
                        }
                        else
                        {
                            Toast.makeText(mContext, "group delete failed not ok", Toast.LENGTH_SHORT).show()
                            Log.e("kimurzzoo", "group delete failed not ok")
                        }
                    }
                    else
                    {
                        Toast.makeText(mContext, "group delete failed but server responsed", Toast.LENGTH_SHORT).show()
                        Log.e("kimurzzoo", "group delete failed but server responsed")
                    }

                }

                override fun onFailure(call: Call<GroupDeleteResult>, t: Throwable) {
                    Toast.makeText(mContext, "group delete failed : " + t.toString(), Toast.LENGTH_SHORT).show()
                    Log.e("kimurzzoo", "group delete failed")
                }
            })
        }

        binding.nextButton.setOnClickListener {
            apis.get_speecher_next(preferenceManager.getString(mContext, "accessToken")!!).enqueue(object : retrofit2.Callback<Unit>
            {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                }
            })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        finish()
    }
}