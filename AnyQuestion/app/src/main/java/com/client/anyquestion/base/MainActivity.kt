package com.client.anyquestion.base

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.client.anyquestion.auth.SettingsActivity
import com.client.anyquestion.databinding.ActivityMainBinding
import com.client.anyquestion.network.APIS
import com.client.anyquestion.questioner.QuestionerActivity
import com.client.anyquestion.speecher.SpeecherActivity
import com.client.anyquestion.util.BackgroundService
import com.client.anyquestion.util.PreferenceManager

class MainActivity : AppCompatActivity() {
    val api = APIS.create()
    val preferenceManager : PreferenceManager = PreferenceManager()
    val mContext = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.speecherButton.setOnClickListener {
            val intent = Intent(mContext, SpeecherActivity::class.java)
            startActivity(intent)
        }

        binding.questionerButton.setOnClickListener {
            val intent = Intent(mContext, QuestionerActivity::class.java)
            startActivity(intent)
        }

        binding.settingsButton.setOnClickListener{
            val intent = Intent(mContext, SettingsActivity::class.java)
            startActivity(intent)
        }
        startService(Intent(this, BackgroundService::class.java))
    }

    override fun onBackPressed() {
        finish()
    }
}