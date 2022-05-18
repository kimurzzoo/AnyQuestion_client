package com.client.anyquestion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.client.anyquestion.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mContext = this

        binding.startImage.setImageResource(R.drawable.start_face)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent : Intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }, 1500)
    }
}