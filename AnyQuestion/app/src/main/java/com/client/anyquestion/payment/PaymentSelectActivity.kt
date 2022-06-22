package com.client.anyquestion.payment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.client.anyquestion.R
import com.client.anyquestion.databinding.ActivityPaymentSelectBinding
import com.client.anyquestion.payment.paypal.PaypalActivity

class PaymentSelectActivity : AppCompatActivity() {
    val mContext= this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPaymentSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.paymentSelectToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.paymentSelectToolbar.title="Register"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)

        binding.paymentSelectPaypalButton.setOnClickListener {
            val intent = Intent(mContext, PaypalActivity::class.java)
            startActivity(intent)
        }

        binding.paymentSelectTossButton.setOnClickListener {
            val intent = Intent()
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