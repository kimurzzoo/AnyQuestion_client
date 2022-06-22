package com.client.anyquestion.auth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.client.anyquestion.base.MainActivity
import com.client.anyquestion.util.PreferenceManager
import com.client.anyquestion.databinding.ActivityLoginBinding
import com.client.anyquestion.network.APIS
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    val api = APIS.create()
    val preferenceManager : PreferenceManager = PreferenceManager()
    val mContext = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.loginToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.loginToolbar.title="Login"

        binding.loginButton.setOnClickListener {
            Log.d("kimurzzoo", "login button clicked")
            val email = binding.emailEdit.text.toString()
            val pw = binding.pwEdit.text.toString()

            if(binding.autoLoginCheckbox.isChecked)
            {
                preferenceManager.setBoolean(mContext,"auto_login", true)
                preferenceManager.setString(mContext, "email", email)
                preferenceManager.setString(mContext, "pw", pw)
                Log.d("kimurzzoo", "login button clicked and checked")
            }

            val userDTO = UserDTO(email, pw)

            Log.d("kimurzzoo", "login button clicked and login method")
            loginMethod(mContext, userDTO)

        }

        binding.registerButton.setOnClickListener {
            val intent : Intent = Intent(mContext, RegisterActivity::class.java)
            startActivity(intent)
        }

        if(preferenceManager.getBoolean(mContext, "auto_login"))
        {
            val newUserDTO : UserDTO = UserDTO(preferenceManager.getString(mContext, "email")!!, preferenceManager.getString(mContext, "pw")!!)
            loginMethod(mContext, newUserDTO)
        }
        else
        {

        }

        binding.forgotPasswordButton.setOnClickListener {
            val intent = Intent(mContext, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    fun loginMethod(context : Context, userDTO : UserDTO)
    {
        Log.d("kimurzzoo", "loginmethod start")
        api.post_user_login(userDTO).enqueue(object : Callback<TokenDTO> {
            override fun onResponse(call: Call<TokenDTO>, response: Response<TokenDTO>) {
                Log.d("kimurzzoo", "loginmethod onresponse start")
                if(response.body() != null)
                {
                    Log.d("kimurzzoo", "accesstoken : " + response.body()!!.accessToken)
                    Log.d("kimurzzoo", "refreshtoken : " + response.body()!!.refreshToken)
                    preferenceManager.setString(mContext, "accessToken", response.body()!!.accessToken)
                    preferenceManager.setString(mContext, "refreshToken", response.body()!!.refreshToken)
                    val intent : Intent = Intent(context, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    Log.d("kimurzzoo", "login success")
                }
                else
                {
                    Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
                    Log.d("kimurzzoo", "login failed and server didnt response")
                }
                Log.d("kimurzzoo", "loginmethod onresponse end")
            }

            override fun onFailure(call: Call<TokenDTO>, t: Throwable) {
                Toast.makeText(context, "Login failed : " + t.toString(), Toast.LENGTH_SHORT).show()
                Log.d("kimurzzoo", "loginmethod onfailure : " + t.toString())
            }
        })
        Log.d("kimurzzoo", "loginmethod ended")
    }

    override fun onBackPressed() {
        finish()
    }
}