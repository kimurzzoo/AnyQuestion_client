package com.client.anyquestion.payment.paypal

import android.net.http.SslError
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.SslErrorHandler
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.client.anyquestion.R
import com.client.anyquestion.databinding.ActivityPaypalBinding
import com.client.anyquestion.util.PreferenceManager

class PaypalActivity : AppCompatActivity() {
    val mContext = this
    val preferenceManager : PreferenceManager = PreferenceManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPaypalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.paypalWebview.apply {
            val headerMap = hashMapOf<String, String>()
            headerMap.put("Authorization", preferenceManager.getString(mContext, "accessToken")!!)

            webViewClient = object: WebViewClient()
            {
                override fun onReceivedSslError(
                    view: WebView?,
                    handler: SslErrorHandler?,
                    error: SslError?
                ) {
                    handler?.proceed()
                }

                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    request?.requestHeaders?.put("Authorization", preferenceManager.getString(mContext, "accessToken")!!)
                    return super.shouldOverrideUrlLoading(view, request)
                }
            }

            settings.javaScriptEnabled = true
            loadUrl("http://192.168.0.9:8080/payment/paypal/submit", headerMap)
        }
    }
}