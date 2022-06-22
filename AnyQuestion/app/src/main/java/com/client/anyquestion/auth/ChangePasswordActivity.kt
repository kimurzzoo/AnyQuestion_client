package com.client.anyquestion.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.client.anyquestion.util.PreferenceManager
import com.client.anyquestion.R
import com.client.anyquestion.databinding.ActivityChangePasswordBinding
import com.client.anyquestion.network.APIS
import retrofit2.Call
import retrofit2.Response

class ChangePasswordActivity : AppCompatActivity() {
    val api = APIS.create()
    val mContext = this
    val preferenceManager : PreferenceManager = PreferenceManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.changePasswordToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.changePasswordToolbar.title="Change Password"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)

        binding.changePasswordButton.setOnClickListener {
            api.post_user_changepassword(preferenceManager.getString(mContext, "accessToken")!!,
                ChangePasswordDTO(binding.changePasswordNowPasswordType.text.toString(),
                                binding.changePasswordNewPasswordType.text.toString(),
                                binding.changePasswordNewPasswordConfirmType.text.toString())
            ).enqueue(object : retrofit2.Callback<ChangePasswordResultDTO>{
                override fun onResponse(
                    call: Call<ChangePasswordResultDTO>,
                    response: Response<ChangePasswordResultDTO>
                ) {
                    if(response.body() != null)
                    {
                        if(response.body()!!.ok)
                        {
                            Log.d("kimurzzoo", "password changed successfully")
                            finish()
                        }
                        else
                        {
                            Log.d("kimurzzoo", "something wrong with your typing")
                        }
                    }
                    else
                    {
                        Log.d("kimurzzoo", "body null")
                    }
                }

                override fun onFailure(call: Call<ChangePasswordResultDTO>, t: Throwable) {
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