package com.client.anyquestion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.client.anyquestion.databinding.ActivityLogoutBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LogoutActivity : AppCompatActivity() {
    val api = APIS.create()
    val mContext = this
    val preferenceManager : PreferenceManager = PreferenceManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLogoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.logoutNoButton.setOnClickListener {
            finish()
        }

        binding.logoutYesButton.setOnClickListener {
            api.get_user_logout(preferenceManager.getString(mContext, "accessToken")!!)
                .enqueue(object : retrofit2.Callback<LogoutDTO>{
                    override fun onResponse(call: Call<LogoutDTO>, response: Response<LogoutDTO>) {
                        if(response.body() != null)
                        {
                            if(response.body()!!.ok)
                            {
                                preferenceManager.clear(mContext)
                                val intent = Intent(mContext, LoginActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                startActivity(intent)
                            }
                            else
                            {
                                Log.d("kimurzzoo", "logout failed")
                            }
                        }
                        else
                        {
                            Log.d("kimruzzoo", "server didnt response logout")
                        }
                    }

                    override fun onFailure(call: Call<LogoutDTO>, t: Throwable) {
                        Log.d("kimurzzoo", t.toString())
                    }
                })
        }
    }
}