package com.client.anyquestion

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.client.anyquestion.databinding.ActivityForgotPasswordBinding
import retrofit2.Call
import retrofit2.Response

class ForgotPasswordActivity : AppCompatActivity() {
    val api = APIS.create()
    val mContext = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.forgotPasswordToolbar)
        binding.forgotPasswordToolbar.title="Forgot Password"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)

        binding.forgotPasswordSendButton.setOnClickListener{
            api.post_user_forgotpassword(EmailDTO(binding.forgotPasswordType.text.toString()))
                .enqueue(object : retrofit2.Callback<TempPasswordDTO>{
                    override fun onResponse(
                        call: Call<TempPasswordDTO>,
                        response: Response<TempPasswordDTO>
                    ) {
                        if(response.body() != null)
                        {
                            if(response.body()!!.tempPassword)
                            {
                                Log.d("kimurzzoo", "password changed to temporary password")
                                finish()
                            }
                            else
                            {
                                Log.d("kimurzzoo", "email doesn't fit any account")
                            }
                        }
                        else
                        {
                            Log.d("kimurzzoo", "body null")
                        }
                    }

                    override fun onFailure(call: Call<TempPasswordDTO>, t: Throwable) {
                        Log.d("kimurzzoo", "server didn't response")
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