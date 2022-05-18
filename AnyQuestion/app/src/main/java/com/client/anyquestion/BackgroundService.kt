package com.client.anyquestion

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import retrofit2.Call
import retrofit2.Response
import java.util.*

class BackgroundService : Service() {
    var reissueThread : ReissueThread? = null
    val api = APIS.create()
    val preferenceManager = PreferenceManager()

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        reissueThread = ReissueThread(this, api, preferenceManager)
        reissueThread!!.start()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        reissueThread!!.timer.cancel()
        logout()
        super.onDestroy()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        reissueThread!!.timer.cancel()
        logout()
        stopSelf()
    }

    fun logout()
    {
        api.get_user_logout(preferenceManager.getString(this, "accessToken")!!)
            .enqueue(object : retrofit2.Callback<LogoutDTO>{
                override fun onResponse(call: Call<LogoutDTO>, response: Response<LogoutDTO>) {
                    if(response.body() != null)
                    {
                        if(response.body()!!.ok)
                        {
                            Log.d("kimurzzoo", "logout ok")
                        }
                    }
                }

                override fun onFailure(call: Call<LogoutDTO>, t: Throwable) {
                    Log.d("kimurzzoo", "logout failed")
                }
            })
    }
}

class ReissueThread : Thread
{
    var thisContext : Context
    var api : APIS
    var preferenceManager : PreferenceManager
    var timer : Timer
    var timerTask: TimerTask

    constructor(mContext : Context, mapi : APIS, preferenceManager: PreferenceManager)
    {
        this.thisContext = mContext
        this.api = mapi
        this.preferenceManager = preferenceManager
        this.timer = Timer()
        this.timerTask = object : TimerTask() {
            override fun run() {
                api.post_user_reissue(preferenceManager.getString(thisContext,"accessToken")!!,
                    TokenReissueDTO(preferenceManager.getString(thisContext, "refreshToken")!!)
                ).enqueue(object : retrofit2.Callback<TokenDTO>{
                    override fun onResponse(call: Call<TokenDTO>, response: Response<TokenDTO>) {
                        if(response.body() != null)
                        {
                            preferenceManager.setString(thisContext, "accessToken", response.body()!!.accessToken)
                            preferenceManager.setString(thisContext, "refreshToken", response.body()!!.refreshToken)
                        }
                    }

                    override fun onFailure(call: Call<TokenDTO>, t: Throwable) {
                        Log.d("kimurzzoo", "reissue failed")
                    }
                })
            }
        }
    }

    override fun run()
    {
        timer.schedule(timerTask, 0, 1000*60*25)
    }
}