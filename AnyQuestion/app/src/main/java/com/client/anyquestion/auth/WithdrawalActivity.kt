package com.client.anyquestion.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.client.anyquestion.util.PreferenceManager
import com.client.anyquestion.databinding.ActivityWithdrawalBinding
import com.client.anyquestion.network.APIS
import retrofit2.Call
import retrofit2.Response

class WithdrawalActivity : AppCompatActivity() {
    val api = APIS.create()
    val mContext = this
    val preferenceManager : PreferenceManager = PreferenceManager()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =  ActivityWithdrawalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.withdrawalNoButton.setOnClickListener {
            finish()
        }

        binding.withdrawalYesButton.setOnClickListener {
            if(binding.withdrawalText.text.toString().equals("withdrawal"))
            {
                api.get_user_withdrawal(preferenceManager.getString(mContext, "accessToken")!!)
                    .enqueue(object : retrofit2.Callback<WithdrawalDTO>{
                        override fun onResponse(
                            call: Call<WithdrawalDTO>,
                            response: Response<WithdrawalDTO>
                        ) {
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
                                    Log.d("kimurzzoo", "withdrawal failed")
                                }
                            }
                            else
                            {
                                Log.d("kimurzzoo", "server didnt response withdrawal")
                            }
                        }

                        override fun onFailure(call: Call<WithdrawalDTO>, t: Throwable) {
                            Log.d("kimurzzoo", t.toString())
                        }
                    })
            }
        }
    }
}