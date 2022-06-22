package com.client.anyquestion.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.client.anyquestion.R
import com.client.anyquestion.databinding.ActivityRegisterBinding
import com.client.anyquestion.network.APIS
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {
    val api = APIS.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mContext = this

        setSupportActionBar(binding.registerToolbar)
        binding.registerToolbar.title="Register"
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)

        binding.registerButton.setOnClickListener {
            val email_text = binding.emailEdit.text.toString()
            val name_text = binding.nameEdit.text.toString()
            val pw = binding.pwEdit.text.toString()
            val pw_cf = binding.pwConfirmEdit.text.toString()

            if(pw.length < 10)
            {
                Toast.makeText(mContext, "password is too short", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(!pw.equals(pw_cf))
            {
                Toast.makeText(mContext, "password confirmation isn`t right", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(name_text.length < 2)
            {
                Toast.makeText(mContext, "name is too short", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val pattern : Pattern = android.util.Patterns.EMAIL_ADDRESS
            if(!pattern.matcher(email_text).matches()) {
                Toast.makeText(mContext, "email isn`t appropriate", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val accountDTO : AccountDTO = AccountDTO(name_text, email_text, pw, pw_cf)

            api.post_user_register(accountDTO).enqueue(object : Callback<RegisterResultDTO> {
                override fun onResponse(
                    call: Call<RegisterResultDTO>,
                    response: Response<RegisterResultDTO>
                ) {
                    if(response.body() != null)
                    {
                        if(response.body()!!.ok)
                        {
                            Toast.makeText(mContext, "Register success", Toast.LENGTH_SHORT).show()
                            val intent = Intent(mContext, LoginActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)
                        }
                        else
                        {
                            Toast.makeText(mContext, "Register failed but server responsed", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else
                    {
                        Toast.makeText(mContext, "Register failed", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<RegisterResultDTO>, t: Throwable) {
                    Toast.makeText(mContext, "Register failed", Toast.LENGTH_SHORT).show()
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