package com.client.anyquestion

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

interface APIS {

    @POST("/auth/login")
    @Headers("accept: application/json", "content-type: application/json")
    fun post_user_login(@Body jsonparams : UserDTO) : Call<TokenDTO>

    @POST("/auth/register")
    @Headers("accept: application/json", "content-type: application/json")
    fun post_user_register(@Body jsonparams: AccountDTO) : Call<RegisterResultDTO>

    @POST("/auth/reissue")
    @Headers("accept: application/json", "content-type: application/json")
    fun post_user_reissue(@Header("Authorization") accessToken: String, @Body jsonparams: TokenReissueDTO) : Call<TokenDTO>

    @GET("/auth/logout")
    @Headers("accept: application/json")
    fun get_user_logout(@Header("Authorization") accessToken: String) : Call<LogoutDTO>

    @GET("/auth/withdrawal")
    @Headers("accept: application/json")
    fun get_user_withdrawal(@Header("Authorization") accessToken: String) : Call<WithdrawalDTO>

    @POST("/auth/forgotpassword")
    @Headers("accept: application/json", "content-type: application/json")
    fun post_user_forgotpassword(@Body jsonparams: EmailDTO) : Call<TempPasswordDTO>

    @POST("/auth/changepassword")
    @Headers("accept: application/json", "content-type: application/json")
    fun post_user_changepassword(@Header("Authorization") accessToken: String, @Body jsonparams: ChangePasswordDTO) : Call<ChangePasswordResultDTO>

    @GET("/speecher/delete")
    @Headers("accept: application/json")
    fun get_speecher_delete(@Header("Authorization") accessToken : String) : Call<GroupDeleteResult>

    @GET("/speecher/next")
    fun get_speecher_next(@Header("Authorization") accessToken : String) : Call<Unit>

    @POST("/questioner/search")
    @Headers("accept: application/json", "content-type: application/json")
    fun post_questioner_search(@Header("Authorization") accessToken: String, @Body jsonparams: GroupSearchDTO) : Call<GroupSearchResultDTO>

    @GET("/questioner/meout")
    @Headers("accept: application/json")
    fun get_questioner_meout(@Header("Authorization") accessToken: String) : Call<MeOutResultDTO>




    companion object {
        private const val BASE_URL = "http://192.168.0.9:8080" // 주소

        fun create(): APIS {

            val gson :Gson =   GsonBuilder().setLenient().create();
            /**
             * Retrofit SSL 우회 접속 통신
             */
            /*fun getUnsafeOkHttpClient(): OkHttpClient.Builder {
                val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                    override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {

                    }

                    override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {

                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }
                })

                val sslContext = SSLContext.getInstance("SSL")
                sslContext.init(null, trustAllCerts, SecureRandom())

                val sslSocketFactory = sslContext.socketFactory

                val builder = OkHttpClient.Builder()
                builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                builder.hostnameVerifier { hostname, session -> true }

                return builder
            }*/

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                //.client(getUnsafeOkHttpClient().build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(APIS::class.java)
        }
    }
}