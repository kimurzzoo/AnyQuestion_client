package com.client.anyquestion.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.client.anyquestion.R
import com.client.anyquestion.databinding.ActivitySettingsBinding
import com.client.anyquestion.payment.PaymentSelectActivity
import com.client.anyquestion.util.PreferenceManager

class SettingsActivity : AppCompatActivity() {

    var dataSet : Array<String> = arrayOf("Logout", "Withdrawal", "Change Password", "Payment for Speecher")
    val mContext = this
    val preferenceManager : PreferenceManager = PreferenceManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.settingsToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.settingsToolbar.title="Register"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)

        val adapter = SettingsRecyclerViewAdapter(dataSet)
        binding.settingsRecyclerview.adapter = adapter
        binding.settingsRecyclerview.layoutManager = LinearLayoutManager(mContext)

        adapter.setItemClickListener(object : SettingsRecyclerViewAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                when
                {
                    position == 0 ->
                    {
                        val intent = Intent(mContext, LogoutActivity::class.java)
                        startActivity(intent)
                    }

                    position == 1 ->
                    {
                        val intent = Intent(mContext, WithdrawalActivity::class.java)
                        startActivity(intent)
                    }

                    position == 2 ->
                    {
                        val intent = Intent(mContext, ChangePasswordActivity::class.java)
                        startActivity(intent)
                    }

                    position == 3 ->
                    {
                        val intent = Intent(mContext, PaymentSelectActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        })
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