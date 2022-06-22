package com.client.anyquestion.questioner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.client.anyquestion.util.PreferenceManager
import com.client.anyquestion.R
import com.client.anyquestion.databinding.ActivityQuestionerBinding
import com.client.anyquestion.network.APIS
import com.google.gson.Gson
import com.launchdarkly.eventsource.EventSource
import okhttp3.Headers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Response
import java.lang.Exception
import java.net.URI

class QuestionerActivity : AppCompatActivity() {

    var questionEventHandler : QuestionEventHandler? = null
    val api = APIS.create()
    val preferenceManager = PreferenceManager()
    val mContext = this
    var room_password : String = ""
    var questionEventSource : EventSource?= null
    var gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivityQuestionerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.questionerToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.questionerToolbar.title="Questioner"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)

        binding.questionerGroupSearchButton.setOnClickListener {
            api.post_questioner_search(preferenceManager.getString(mContext, "accessToken")!!, GroupSearchDTO(binding.questionerPasswordText.text.toString()))
                .enqueue(object : retrofit2.Callback<GroupSearchResultDTO>{
                    override fun onResponse(
                        call: Call<GroupSearchResultDTO>,
                        response: Response<GroupSearchResultDTO>
                    ) {
                        if(response.body() != null)
                        {
                            if(response.body()!!.ok)
                            {
                                binding.questionerMeButton.visibility=View.VISIBLE
                                binding.questionerMeButton.isEnabled=true
                                room_password = binding.questionerPasswordText.text.toString()
                            }
                            else
                            {
                                Toast.makeText(mContext, "There is no such room", Toast.LENGTH_SHORT).show()
                            }
                        }
                        else
                        {
                            Toast.makeText(mContext, "Server has problems", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<GroupSearchResultDTO>, t: Throwable) {
                        Toast.makeText(mContext, "Server didn't response", Toast.LENGTH_SHORT).show()
                    }
                })
        }

        binding.questionerMeButton.setOnClickListener {
            questionEventHandler = QuestionEventHandler(this, binding)
            binding.questionerMeButton.isEnabled=false
            try {
                questionEventSource = EventSource.Builder(questionEventHandler, URI.create("http://192.168.0.9:8080/questioner/me"))
                    .method("POST")
                    .headers(Headers.Builder().add("Authorization", preferenceManager.getString(mContext, "accessToken")!!).build())
                    .body(
                        gson.toJson(MeDTO(room_password)).toString()
                            .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
                    )
                    .build()
                questionEventSource?.start()
            }
            catch (e : Exception)
            {
                Log.e("kimurzzoo", e.toString())
            }
            binding.questionerMeoutButton.isEnabled=true
            binding.questionerMeoutButton.visibility=View.VISIBLE
            binding.questionerGroupSearchButton.isEnabled=false
            binding.questionerGroupSearchButton.visibility=View.INVISIBLE
            binding.questionerPasswordText.isEnabled=false
        }

        binding.questionerMeoutButton.setOnClickListener {
            questionEventSource?.close()

            binding.questionerMeoutButton.isEnabled=false
            binding.questionerMeoutButton.visibility=View.INVISIBLE

            binding.questionerMeButton.text="Me!"
            binding.questionerMeButton.setBackgroundColor(R.drawable.yellow_button)
            binding.questionerMeButton.isEnabled=false
            binding.questionerMeButton.visibility=View.INVISIBLE

            binding.questionerPasswordText.isEnabled=true

            binding.questionerGroupSearchButton.isEnabled=true
            binding.questionerGroupSearchButton.visibility=View.VISIBLE
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