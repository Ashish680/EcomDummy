package com.ashish.e_comdummy.application

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.ashish.e_comdummy.roomDB.AppDatabase

class YourApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        AppDatabase(this)
    }

    override fun attachBaseContext(context: Context) {
        MultiDex.install(context)
        super.attachBaseContext(context)
    }

    companion object {
        val TAG = YourApplication::class.java.simpleName

        @get:Synchronized
        var instance: YourApplication? = null
            private set
    }
}