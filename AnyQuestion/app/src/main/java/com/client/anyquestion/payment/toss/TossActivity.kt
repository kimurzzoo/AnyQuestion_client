package com.client.anyquestion.payment.toss

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.net.http.SslError
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.*
import com.client.anyquestion.databinding.ActivityTossBinding
import com.client.anyquestion.util.PreferenceManager
import java.net.URISyntaxException

class TossActivity : AppCompatActivity() {

    val mContext= this
    val preferenceManager : PreferenceManager = PreferenceManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTossBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tossWebview.apply {
            val headerMap = hashMapOf<String, String>()
            headerMap.put("Authorization", preferenceManager.getString(mContext, "accessToken")!!)

            webViewClient = object : WebViewClient() {
                override fun onReceivedSslError(
                    view: WebView?,
                    handler: SslErrorHandler?,
                    error: SslError?
                ) {
                    handler?.proceed()
                }

                override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                    request.requestHeaders.put("Authorization", preferenceManager.getString(mContext, "accessToken")!!)

                    request.url.toString().let {
                        if (!URLUtil.isNetworkUrl(request.url.toString()) && !URLUtil.isJavaScriptUrl(request.url.toString())) {
                            val uri = try {
                                Uri.parse(request.url.toString())
                            } catch (e: Exception) {
                                return false
                            }

                            return when (uri.scheme) {
                                "intent" -> {
                                    startSchemeIntent(it)
                                }
                                else -> {
                                    return try {
                                        startActivity(Intent(Intent.ACTION_VIEW, uri))
                                        true
                                    } catch (e: java.lang.Exception) {
                                        false
                                    }
                                }
                            }
                        } else {
                            return false
                        }
                    }
                }

                private fun startSchemeIntent(url: String?): Boolean {
                    val schemeIntent: Intent = try {
                        Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                    } catch (e: URISyntaxException) {
                        return false
                    }
                    try {
                        startActivity(schemeIntent)
                        return true
                    } catch (e: ActivityNotFoundException) {
                        val packageName = schemeIntent.getPackage()

                        if (!packageName.isNullOrBlank()) {
                            startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("market://details?id=$packageName")
                                )
                            )
                            return true
                        }
                    }
                    return false
                }
            }
            settings.javaScriptEnabled = true
            loadUrl("http://192.168.0.9:8080/payment/toss/submit", headerMap)
        }
    }
}