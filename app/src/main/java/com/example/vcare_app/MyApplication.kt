package com.example.vcare_app

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                Log.i("Application", "onActivityCreated: ${activity.componentName.className}")
            }

            override fun onActivityStarted(activity: Activity) {
                Log.i("Application", "onActivityStarted: ")
            }

            override fun onActivityResumed(activity: Activity) {
                Log.i("Application", "onActivityResumed: ")
            }

            override fun onActivityPaused(activity: Activity) {
                Log.i("Application", "onActivityPaused: ")
            }

            override fun onActivityStopped(activity: Activity) {
                Log.i("Application", "onActivityStopped: ${activity.componentName.className}")
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                Log.i("Application", "onActivitySaveInstanceState: ")
            }

            override fun onActivityDestroyed(activity: Activity) {
                Log.i("Application", "onActivityDestroyed: ")
            }

        })
    }
}